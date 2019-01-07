import com.google.common.base.Stopwatch;
import com.opencsv.CSVReader;
import common.ProjectPropertiesGetter;
import db.DbUtils;
import mf.generator.MetafeatureGenerator;
import model.DMOperator;
import model.DMPlan;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import planner.Trainer;
import smile.clustering.GMeans;
import weka.knowledgeflow.Flow;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static planner.Trainer.makeFooterForTranslation;
import static planner.Trainer.makeHeaderForTranslation;

public class Test12 {
    private static Logger logger = LoggerFactory.getLogger(Test9.class);

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
//        int[] seeds = new int[]{1, 50, 100, 150, 200, 250, 300, 350, 400, 450, 500, 550, 600, 650, 700, 750, 800, 850, 900, 950};
//        int[] seeds = new int[]{1, 50, 100, 150, 200};
        int[] seeds = new int[]{1};

        /*Empty the evaluation experiment table*/
        dbUtils.dropEvaluationExperiment();

        /*Load the saved clustering model*/
        FileInputStream fi = new FileInputStream(new File("diabetes-engine/cluster_model.ser"));
        ObjectInputStream oi = new ObjectInputStream(fi);
        GMeans gm = (GMeans) oi.readObject();
        oi.close();

        /*Load files in the evaluation dataset folder*/
        File folder = new File(propGetter.getProperty("multithread.testing.dataset.collection"));
        File[] files = folder.listFiles();
        String filePath = propGetter.getProperty("multithread.testing.dataset.collection");
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
            String[] classifiers = new String[workflows.size()];
            String[] attributeSelections = new String[workflows.size()];

            for (int seed : seeds){
                Stopwatch stopwatch = new Stopwatch();
                stopwatch.start();

                String flowFileName = propGetter.getProperty("multiflow.weka.output.file");
                File kfFile = new File(flowFileName);
                makeHeaderForTranslation(flowFileName);
                int flowIndex = 0;
                int lastIndex = (workflows.size())-1;
                List<Map<String, Object>> multiFlowResult = new ArrayList();

                for (Map<String, Object> wk : workflows){
                    Map<String, Object> result = new HashMap<>();
                    multiFlowResult.add(result);

                    /*Extract the workflow details*/
                    String c = (String) wk.get("classifier");
                    String a = (String) wk.get("attributeSelection");
                    //-- Execution
                    Trainer trainer = Trainer.getSingleton();
                    boolean compiledResult = trainer.generateProblemFile(f, c, a);
                    if (compiledResult) {
                        //-- Generate the plan
                        DMPlan plan = trainer.generatePlan();

                        //-- Translate the plan to WEKA KNOWLEDGE FLOW file
                        kfFile = trainer.translatePlanToJSONForMultiFlows(
                                flowFileName,
                                seed,
                                filePath,
                                flowIndex,
                                plan,
                                lastIndex);
                        StringBuilder strBuilder = new StringBuilder();
                        for (DMOperator op : plan.getOperators()){
                            strBuilder.append(op.getType().name());
                            if (op.getNextOp() != null) strBuilder.append("|");
                        }
                        String workflowDesc = strBuilder.toString();

                        result.put("classifier", c);
                        result.put("attributeSelection", a);
                        result.put("seed", (double)seed);
                        result.put("flow", workflowDesc);

                        Trainer.reset();
                        logger.info(strBuilder.toString());
                    } else {
                        logger.warn("Compilation of problem.java failed!");
                    }
                    flowIndex++;
                }
                makeFooterForTranslation(flowFileName);

                Trainer trainer = Trainer.getSingleton();
                Flow flow = trainer.executeTranslatedPlan(kfFile);

                //-- Collect the flow results
                int m = 0;
                for (Map<String, Object> r : multiFlowResult){
                    Map<String, Double> result = trainer.getDataMiningResultFromMultiFlow(flow, m);
                    m++;
                    r.put("weightedAreaUnderROC", result.get("weightedAreaUnderROC"));
                    r.put("weightedFMeasure", result.get("weightedFMeasure"));
                    r.put("weightedPrecision", result.get("weightedPrecision"));
                    r.put("weightedRecall", result.get("weightedRecall"));
                    r.put("errorRate", result.get("errorRate"));
                }

                //-- Get the best flow based on AUC
                double bestErrorRate= 1;
                int bestFlowIndex = -1;
                int j = 0;
                for (Map<String, Object> r : multiFlowResult){
                    double errorRate = (Double)r.get("errorRate");
                    if (bestErrorRate > errorRate){
                        bestErrorRate = errorRate;
                        bestFlowIndex = j;
                    }
                    j++;
                }
                Map<String, Object> bestFlowResult = multiFlowResult.get(bestFlowIndex);

                //-- Optimize the best wf
                String c = (String)bestFlowResult.get("classifier");
                String a = (String)bestFlowResult.get("attributeSelection");
                Map<String, Object> r = trainer.executeWithOptimization(f,
                        seed,
                        c,
                        a,
                        false,
                        filePath);
                Thread.sleep(100);
                r.put("rank", 0);
                r.put("cluster", cluster);
                dbUtils.insertNewEvaluationExperiment(r);
            }

        }
        dbUtils.viewAllEvaluationExperiments();
        dbUtils.exportEvaluationExperimentsToCSV();
    }

}
