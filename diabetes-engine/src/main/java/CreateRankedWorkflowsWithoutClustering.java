import clustering.DoClustering;
import com.opencsv.CSVReader;
import common.ProjectPropertiesGetter;
import db.DbUtils;
import mf.generator.MetafeatureGenerator;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import smile.clustering.GMeans;

import java.io.*;
import java.sql.ResultSet;
import java.util.*;

/**
 * @author newbiettn on 7/7/18
 * @project DiabetesDiscoveryV2
 */

public class CreateRankedWorkflowsWithoutClustering {
    private static Logger logger = LoggerFactory.getLogger(CreateRankedWorkflowsWithoutClustering.class);
    private static void printLines(String name, InputStream ins) throws Exception {
        String line = null;
        BufferedReader in = new BufferedReader(
                new InputStreamReader(ins));
        while ((line = in.readLine()) != null) {
            logger.info(name + " " + line);
        }
    }

    private static void runProcess(String command) throws Exception {
        Process pro = Runtime.getRuntime()
                .exec(command, null, new File(ProjectPropertiesGetter.getSingleton().getProperty("R.utilities")));
        printLines(command + " :", pro.getInputStream());
        printLines(command + " stderr:", pro.getErrorStream());
        pro.waitFor();
        logger.info(command + " exitValue() " + pro.exitValue());
    }

    public static double[][] normalize(double[][] mf) throws Exception {
        /*Export to csv*/
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < mf.length; i++){
            for (int j = 0; j < mf[0].length; j++){
                if (j == mf[0].length - 1)
                    stringBuilder.append(mf[i][j]);
                else
                    stringBuilder.append(mf[i][j]).append(",");
            }
            stringBuilder.append("\n");
        }

        FileWriter fw = new FileWriter(
                ProjectPropertiesGetter.getSingleton().getProperty("R.utilities") + "mf.csv",
                false);
        BufferedWriter bw = new BufferedWriter(fw);
        PrintWriter out = new PrintWriter(bw);
        out.print(stringBuilder.toString());
        out.close();

        /*Normalize by R*/
        logger.info("Running Rscript to perform normalization...");
        runProcess("R CMD BATCH normalize.training.set.R");

        /*Read back and convert to 2D array*/
        String fname = ProjectPropertiesGetter.getSingleton().getProperty("R.utilities") + "normalized.mf.csv";
        CSVReader reader = new CSVReader(new FileReader(fname));
        String[] line;
        double[][] normalizedMf = new double[mf.length][mf[0].length];
        int row = 0;
        while ((line = reader.readNext()) != null) {
            for (int col = 0; col < mf[0].length; col++){
                normalizedMf[row][col] = Double.parseDouble(line[col]);
            }
            row++;
        }
        return normalizedMf;
    }

    public static void main(String[] args) throws Exception {
        DbUtils dbUtils = new DbUtils();
        MetafeatureGenerator mfGen = new MetafeatureGenerator();
        DoClustering dc = new DoClustering();

        dbUtils.viewAllNonSummarizedExperiments();
        dbUtils.dropDatasetMetafeature();

        /*Generate meta-features for a set of datasets*/
        File folder = new File(ProjectPropertiesGetter.getSingleton().getProperty("training.dataset.collection"));
        Set<Map<String, Object>> batchResult = mfGen.batchGenerate(folder);

        /*Store in db*/
        for (Map<String, Object> record : batchResult){
            dbUtils.insertDatasetMetafeature(record);
        }
        dbUtils.viewDatasetMetafeature();

        /*Clustering*/
        ResultSet rs = dbUtils.getAllDatasetMetafeature();
        Map<String, Object> m = dbUtils.transformDatasetMetafeature(rs);
        List<String> datasetNames = (ArrayList)m.get("datasetNames");
        double[][] mf = (double[][])m.get("metafeatures");
        double[][] normalizedMf = normalize(mf);
        GMeans gm = dc.performGmean(normalizedMf);

        String clusteringModelFilePath = "diabetes-engine/clustering_model_using_gmeans_ranking.ser";
        FileOutputStream f = new FileOutputStream(new File(clusteringModelFilePath), false);
        ObjectOutputStream o = new ObjectOutputStream(f);
        o.writeObject(gm);
        o.flush();
        o.close();

//        FileInputStream fi = new FileInputStream(new File("diabetes-engine/clustering_model_using_gmeans.ser"));
//        ObjectInputStream oi = new ObjectInputStream(fi);
//        GMeans gm = (GMeans) oi.readObject();
//        oi.close();

        dbUtils.dropDatasetCluster();
        int[] labels = gm.getClusterLabel();
        for (int k = 0; k < labels.length; k++){
            Map<String, Object> datasetCluster = new HashMap<>();
            datasetCluster.put("dataset", datasetNames.get(k));
            datasetCluster.put("cluster", labels[k]);
            dbUtils.insertDatasetCluster(datasetCluster);
        }
        dbUtils.viewAllDatasetCluster();
        dbUtils.exportAllDatasetCluster();

        /*Summarize the experiment (take average values)*/
        logger.info("Summarize the experiment by taking their average values of same group...");
        dbUtils.createSummarizedExperimentHistoryTable();
        dbUtils.viewAllSummarizedExperimentHistoryTable();

        /*Extract best workflow for each cluster*/
        int max_cluster = dbUtils.getMaxClusterNumber();
        dbUtils.dropBestWorkflowByClusterTable();

        /*Generate ranking file for a specific cluster*/
        List<String> datasets = dbUtils.getAllDataset();
        StringBuilder strBuilder = new StringBuilder();
        for (String d : datasets){
            List<Integer> ranking = dbUtils.getRankByDataset(d);
            for (int k = 0; k < ranking.size(); k++){
                if (k == (ranking.size()-1))
                    strBuilder.append(ranking.get(k));
                else
                    strBuilder.append(ranking.get(k) + ",");
            }
            strBuilder.append("\n");
        }

        try(FileWriter fw = new FileWriter(
                ProjectPropertiesGetter.getSingleton().getProperty("R.utilities") + "ranking.csv",
                false);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter out = new PrintWriter(bw)) { out.print(strBuilder.toString());
        } catch (IOException e) {
        }

        /*Execute R script to choose the best workflow for the cluster*/
        logger.info("Running Rscript to perform ranking...");
//        runProcess("R CMD BATCH ranking.R");

        /*Extract the top N workflows*/
        FileInputStream stream = new FileInputStream(
                new File(ProjectPropertiesGetter.getSingleton().getProperty("R.utilities") + "best_workflow_index.txt"));
        StringWriter writer = new StringWriter();
        IOUtils.copy(stream, writer);
        String input = writer.toString().replace("\n", "");
        String[] inputNumber =  input.split(",");

        StringBuilder stringBuilder = new StringBuilder();
        for (int k = 0; k < inputNumber.length; k++){
            int workflowIndex = Integer.parseInt(inputNumber[k].substring(1));
            workflowIndex = workflowIndex - 1; //since Java is from 0, R is from 1
            logger.info("The best workflow index: " + workflowIndex);

            /*Extract workflow correspondingly from db*/
            Map<String, String> wf = dbUtils.getWorkflowOfDatasetByIndex(datasets.get(0), workflowIndex);
            String classifier = wf.get("classifier");
            String attributeSelection = wf.get("attributeSelection");
            logger.info("The workflow V" + workflowIndex + " consists of " + classifier + " and " + attributeSelection);
            stringBuilder.append(attributeSelection + "&" + classifier + ",");

            /*Insert the workflow by cluster and its ranking to db*/
            int rank = k + 1;
            dbUtils.insertBestWorkflowByCluster(classifier, attributeSelection, rank, -1);
            dbUtils.viewAllBestWorkflowByCluster();
        }

        try(FileWriter fw = new FileWriter(
                ProjectPropertiesGetter.getSingleton().getProperty("R.utilities") + "recommended_wk_no_clustering.csv",
                true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter out = new PrintWriter(bw)) { out.print(stringBuilder.toString());
        } catch (IOException e) {
        }
    }
}
