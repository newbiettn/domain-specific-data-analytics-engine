import com.opencsv.CSVReader;
import common.ProjectPropertiesGetter;
import db.DbUtils;
import mf.generator.MetafeatureGenerator;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import smile.clustering.GMeans;

import java.io.*;
import java.util.List;
import java.util.Map;

/**
 * @author newbiettn on 23/7/18
 * @project DiabetesDiscoveryV2
 */
public class EvaluateRankingGMeans {
    private static Logger logger = LoggerFactory.getLogger(EvaluateRankingGMeans.class);

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

    public static double[] normalize(double[] mf) throws Exception {
        /*Export to csv*/
        StringBuilder stringBuilder = new StringBuilder();
        for (int j = 0; j < mf.length; j++){
            if (j == mf.length - 1)
                stringBuilder.append(mf[j]);
            else
                stringBuilder.append(mf[j]).append(",");
        }
        stringBuilder.append("\n");

        FileWriter fw = new FileWriter(
                ProjectPropertiesGetter.getSingleton().getProperty("R.utilities") + "new.item.csv",
                false);
        BufferedWriter bw = new BufferedWriter(fw);
        PrintWriter out = new PrintWriter(bw);
        out.print(stringBuilder.toString());
        out.close();

        /*Normalize by R*/
        logger.info("Running Rscript to perform normalization...");
        runProcess("R CMD BATCH normalize.test.set.R");

        /*Read back and convert to 2D array*/
        String fname = ProjectPropertiesGetter.getSingleton().getProperty("R.utilities") + "normalized.new.item.csv";
        CSVReader reader = new CSVReader(new FileReader(fname));
        String[] line;
        double[] normalizedMf = new double[mf.length];
        while ((line = reader.readNext()) != null) {
            for (int col = 0; col < mf.length; col++){
                normalizedMf[col] = Double.parseDouble(line[col]);
//                System.out.print(normalizedMf[row][col] + ",");
            }
//            System.out.println();
        }
        return normalizedMf;
    }

    public static void main(String[] args) throws Exception {
        ProjectPropertiesGetter propGetter = ProjectPropertiesGetter.getSingleton();
        DbUtils dbUtils = new DbUtils();
        MetafeatureGenerator mfGen = new MetafeatureGenerator();

//        dbUtils.dropEvaluationExperiment();

        String filePath = propGetter.getProperty("ranking.dataset.collection");
        File folder = new File(propGetter.getProperty("ranking.dataset.collection"));
        File[] files = folder.listFiles();

        FileInputStream fi = new FileInputStream(new File("diabetes-engine/clustering_model_using_gmeans_ranking.ser"));
        ObjectInputStream oi = new ObjectInputStream(fi);
        GMeans gm = (GMeans) oi.readObject();
        oi.close();

        for(File f : files){
            //-- MacOS always have DS_Store file, have to handle it
            if (FilenameUtils.getExtension(f.getName()).equals("DS_Store"))
                continue;

            /*Generate the metafeature for the dataset & predict the cluster for it*/
            Map<String, Double> mf = mfGen.generate(f);
            mf.remove("NumberOfInstancesWithMissingValues");
            mf.remove("NumberOfMissingValues");
            mf.remove("PercentageOfMissingValues");
            mf.remove("PercentageOfInstancesWithMissingValues");
            double[] x = new double[mf.size()];
            int i = 0;
            for (String k : mf.keySet()){
                x[i] = mf.get(k);
                i++;
            }
            double[] normalizedX = normalize(x);
            int cluster = gm.predict(normalizedX);
            logger.info("The dataset belongs to the cluster: " + cluster);

            //-- Recommended workflows of this cluster
            List<String> datasets = dbUtils.getDatasetByCluster(cluster);
            List<Map<String, Object>> workflows = dbUtils.getWorkflowOfDatasetByCluster(cluster);

            StringBuilder strBuilder = new StringBuilder();
            strBuilder.append(f.getName()).append(",").append(String.valueOf(cluster)).append(",");
            List<Map<String, String>> list = dbUtils.getWorkflowOfDataset(datasets.get(0));
            for (Map<String, Object> best_wk : workflows){
                String classifier = (String) best_wk.get("classifier");
                String attributeSelection = (String) best_wk.get("attributeSelection");
                for (Map<String, String> wk : list){
                    String c = wk.get("classifier");
                    String a = wk.get("attributeSelection");
                    if (classifier.equals(c) && attributeSelection.equals(a)) {
                        logger.info(classifier + "/" + attributeSelection);
                        strBuilder.append(attributeSelection + "&" + classifier + ",");
                        break;
                    }
                }
            }
            strBuilder.deleteCharAt(strBuilder.length()-1);
            strBuilder.append("\n");

            try(FileWriter fw = new FileWriter(
                    ProjectPropertiesGetter.getSingleton().getProperty("R.utilities") + "recommended_wk_gmeans.csv",
                    true);
                BufferedWriter bw = new BufferedWriter(fw);
                PrintWriter out = new PrintWriter(bw)) { out.print(strBuilder.toString());
            } catch (IOException e) {
            }

//            for (int k = 0; k < dbUtils.getTotalNumberOfWorkflows(datasets.get(0)); k++){
//                Trainer trainer = Trainer.getSingleton();
//                Map<String, String> wk = dbUtils.getWorkflowOfDatasetByIndex(datasets.get(0), k);
//                String c = (String) wk.get("classifier");
//                String a = (String) wk.get("attributeSelection");
//                logger.info(c + "/" + a);
//
//                Map<String, Object> r  = trainer.execute(f, 1, c, a, false, filePath);
//                r.put("rank", -1);
//                r.put("cluster", -1);
//                dbUtils.insertNewEvaluationExperiment(r);
//            }
        }
//        dbUtils.exportEvaluationExperimentsToCSV();

    }
}
