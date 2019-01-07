import com.opencsv.CSVReader;
import common.ProjectPropertiesGetter;
import db.DbUtils;
import mf.generator.MetafeatureGenerator;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import planner.Trainer;
import smile.clustering.GMeans;

import java.io.*;
import java.util.List;
import java.util.Map;

public class EvaluateWorkflowsUsingGMeans {
    private static Logger logger = LoggerFactory.getLogger(EvaluateWorkflowsUsingGMeans.class);

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

    public static void main (String[] args) throws Exception {
        ProjectPropertiesGetter propGetter = ProjectPropertiesGetter.getSingleton();
        DbUtils dbUtils = new DbUtils();
        MetafeatureGenerator mfGen = new MetafeatureGenerator();
        int[] seeds = new int[]{1, 50, 100, 150, 200, 250, 300, 350, 400, 450, 500, 550, 600, 650, 700, 750, 800, 850, 900, 950};
//        int[] seeds = new int[]{1};

        /*Empty the evaluation experiment table*/
//        dbUtils.dropEvaluationExperiment();

        /*Load the saved clustering model*/
        FileInputStream fi = new FileInputStream(new File("diabetes-engine/clustering_model_using_gmeans.ser"));
        ObjectInputStream oi = new ObjectInputStream(fi);
        GMeans gm = (GMeans) oi.readObject();
        oi.close();

        /*Load files in the evaluation dataset folder*/
        File folder = new File(propGetter.getProperty("evaluation.dataset.collection"));
        File[] files = folder.listFiles();
        for(File f : files) {
            if (!FilenameUtils.getExtension(f.getName()).equals("arff"))
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

            /*Extract the top 5 workflows of the target cluster & apply to the dataset*/

            List<Map<String, Object>> workflows = dbUtils.getWorkflowOfDatasetByCluster(cluster);
            logger.info(workflows.size()+"");
            dbUtils.viewAllBestWorkflowByCluster();
            for (Map<String, Object> wk : workflows){
                /*Extract the workflow details*/
                String classifier = (String) wk.get("classifier");
                String attributeSelection = (String) wk.get("attributeSelection");
                int rank = (int) wk.get("rank");

                /* Repeat for different seeds*/
                for (int seed : seeds){
                    Trainer trainer = Trainer.getSingleton();
                    boolean insertToDb = false;
                    String filePath = propGetter.getProperty("evaluation.dataset.collection");

                    /*Call trainer to generate plan for the target dataset*/
                    Map<String, Object> r = trainer.execute(f,
                            seed,
                            classifier,
                            attributeSelection,
                            insertToDb,
                            filePath);
                    Thread.sleep(2000);
                    r.put("rank", rank);
                    r.put("cluster", cluster);
                    dbUtils.insertNewEvaluationExperiment(r);
                }
            }
        }
//        dbUtils.viewAllEvaluationExperiments();
        dbUtils.exportEvaluationExperimentsToCSV();
    }

}
