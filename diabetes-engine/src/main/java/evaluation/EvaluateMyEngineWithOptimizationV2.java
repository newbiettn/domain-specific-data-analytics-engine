package evaluation;

import com.google.common.base.Stopwatch;
import com.opencsv.CSVReader;
import common.ProjectPropertiesGetter;
import db.DbUtils;
import mf.generator.MetafeatureGenerator;
import model.DMOperator;
import model.DMPlan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import planner.Trainer;
import planner.translator.PlanTranslator;
import smile.clustering.GMeans;
import weka.core.WekaException;
import weka.knowledgeflow.*;
import weka.knowledgeflow.steps.ClassifierPerformanceEvaluator;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

public class EvaluateMyEngineWithOptimizationV2 {
    private static Logger logger = LoggerFactory.getLogger(EvaluateMyEngineWithOptimizationV2.class);

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

    static void createResultFile(String fName) throws FileNotFoundException {
        PrintWriter pw = new PrintWriter(fName);
        StringBuilder sb = new StringBuilder();
        sb.append("classifier");
        sb.append(",");
        sb.append("attributeSelection");
        sb.append(',');
        sb.append("seed");
        sb.append(',');
        sb.append("dataset");
        sb.append(',');
        sb.append("weightedAreaUnderROC");
        sb.append(',');
        sb.append("weightedFMeasure");
        sb.append(',');
        sb.append("weightedPrecision");
        sb.append(',');
        sb.append("weightedRecall");
        sb.append(',');
        sb.append("errorRate");
        sb.append(',');
        sb.append("timeElapsed");
        sb.append('\n');

        pw.write(sb.toString());
        pw.close();
        logger.info("Output file created successfully!");
    }

    public static void main (String[] args) throws Exception {
        ProjectPropertiesGetter propGetter = ProjectPropertiesGetter.getSingleton();
        DbUtils dbUtils = new DbUtils();
        MetafeatureGenerator mfGen = new MetafeatureGenerator();
        int[] seeds = new int[]{1, 50, 100, 150, 200, 250, 300, 350, 400, 450, 500, 550, 600, 650, 700, 750, 800, 850, 900, 950};
        int clockTimeout = 20000;

        /*Load the saved clustering model*/
//        FileInputStream fi = new FileInputStream(new File("diabetes-engine/clustering_model_using_kmeans.ser"));
//        ObjectInputStream oi = new ObjectInputStream(fi);
//        KMeans gm = (KMeans) oi.readObject();
//        oi.close();
//
        FileInputStream fi = new FileInputStream(new File("diabetes-engine/clustering_model_using_gmeans.ser"));
        ObjectInputStream oi = new ObjectInputStream(fi);
        GMeans gm = (GMeans) oi.readObject();
        oi.close();

        /*Load files in the evaluation dataset folder*/
        String[] datasetNames = new String[]{"anneal", "audiology", "autos", "balance-scale",
                "breast-cancer", "breast-w", "colic", "credit-a", "credit-g",
                "diabetes", "glass", "heart-c", "heart-h",
                "heart-statlog", "hepatitis", "hypothyroid",
                "ionosphere", "iris", "kr-vs-kp", "labor",
                "letter", "lymph","mushroom", "primary-tumor", "segment",
                "sick", "sonar", "soybean", "splice", "vehicle",
                "vote", "vowel", "waveform-5000", "zoo"};
//        String[] datasetNames = new String[]{"splice", "vehicle",
//                "vote", "vowel", "waveform-5000", "zoo"};
        String filePath = propGetter.getProperty("benchmark.again.autoweka");
        String outputFileName = propGetter.getProperty("myengine.result.file");
        File output = new File(outputFileName);
        createResultFile(outputFileName);

        for(String datasetName : datasetNames) {
            String trainingFileUrl = filePath + "training_" + datasetName + "-Randomize-S800.arff";
            String testFileUrl = filePath + "test_" + datasetName + "-Randomize-S800.arff";
            File trainingset = new File(trainingFileUrl);

            /*Generate the metafeature for the dataset & predict the cluster for it*/
            Map<String, Double> mf = mfGen.generate(trainingset);
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
            logger.info("The size of workflows: " + workflows);
            String[] classifiers = new String[workflows.size()];
            String[] attributeSelections = new String[workflows.size()];

            for (int seed : seeds){
                Stopwatch stopwatch = new Stopwatch();
                stopwatch.start();

                int flowIndex = 0;
                int lastIndex = (workflows.size())-1;
                List<Map<String, Object>> multiFlowResult = new ArrayList();
                ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
                Runnable clockRun = () -> {
                    try {
                        Thread.sleep(clockTimeout);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                };
                Future<?> clockFuture = executor.submit(clockRun);

                List<Future<Boolean>> futures = new ArrayList<>();
                for (Map<String, Object> wk : workflows){
                    int finalFlowIndex = flowIndex;

                    Map<String, Object> results = new HashMap<>();
                    multiFlowResult.add(results);

                    /*Extract the workflow details*/
                    String c = (String) wk.get("classifier");
                    String a = (String) wk.get("attributeSelection");
                    //-- Execution
                    Trainer trainer = Trainer.getSingleton();
                    boolean compiledResult = false;
                    compiledResult = trainer.generateProblemFile(trainingset, c, a);
                    if (compiledResult) {
                        //-- Generate the plan
                        DMPlan plan = trainer.generatePlan();
                        File f = new File("diabetes-engine/out.single." + finalFlowIndex + ".kf");
                        PlanTranslator planTranslator = new PlanTranslator(seed, filePath);
                        planTranslator.setTheComponent(plan);
                        String json = planTranslator.translate();
                        FileWriter fw = new FileWriter(f, false);
                        fw.write(json);
                        fw.close();

                        Callable flowCallable = () -> {
                                JSONFlowLoader loader = new JSONFlowLoader();
                                Flow flow = null;
                                try {
                                    flow = loader.readFlow(f);
                                } catch (WekaException e) {
                                    e.printStackTrace();
                                }
                                BaseExecutionEnvironment execE = new BaseExecutionEnvironment();
                                FlowExecutor flowExecutor = execE.getDefaultFlowExecutor();
                                flowExecutor.setFlow(flow);
                                try {
                                    flowExecutor.runSequentially();
                                } catch (WekaException e) {
                                    e.printStackTrace();
                                }
                                flowExecutor.waitUntilFinished();

                                int lastIndx = plan.getOperators().size() - 2; //-- the index of ClassifierEvaluation component
                                DMOperator lastOp = plan.getOperators().get(lastIndx);
                                StepManager stepManager = flow.findStep(lastOp.getName());
                                ClassifierPerformanceEvaluator eval = (ClassifierPerformanceEvaluator)stepManager.getManagedStep();

                                results.put("classifier", c);
                                results.put("attributeSelection", a);
                                results.put("seed", seed);
                                results.put("weightedAreaUnderROC", eval.getM_eval().weightedAreaUnderROC());
                                results.put("weightedFMeasure", eval.getM_eval().weightedFMeasure());
                                results.put("weightedPrecision", eval.getM_eval().weightedPrecision());
                                results.put("weightedRecall", eval.getM_eval().weightedRecall());
                                results.put("errorRate", eval.getM_eval().errorRate());
                                logger.info(eval.getM_eval().errorRate() + "==================");
                                return true;
                        };
                        Future<Boolean> future = executor.submit(flowCallable);
                        futures.add(future);
                        Trainer.reset();
                    } else {
                            logger.warn("Compilation of problem.java failed!");
                        }
                    flowIndex++;
                }

                boolean isOneDone = false;
                while(!isOneDone){
                    for(Future<Boolean> future : futures){
                        if (!isOneDone){
                            isOneDone = future.isDone();
                        }
                    }
                }
                boolean clockDone = false;
                if (isOneDone) {
                    while (!clockDone){
                        boolean allDone = true;
                        clockDone = clockFuture.isDone();
                        for(Future<Boolean> future : futures){
                            allDone &= future.isDone();
                        }
                        if (allDone)
                            clockDone = true;
                    }
                }
                logger.info("Shutdown now!!");
                executor.shutdown();
                executor.awaitTermination(1, TimeUnit.NANOSECONDS);

                //-- Get the best flow based on AUC
                double bestErrorRate= 1;
                int bestFlowIndex = -1;
                int j = 0;
                for (Map<String, Object> r : multiFlowResult){
                    if (r.get("errorRate") != null) {
                        double errorRate = (Double)r.get("errorRate");
                        if (bestErrorRate > errorRate){
                            bestErrorRate = errorRate;
                            bestFlowIndex = j;
                        }
                    }
                    j++;
                }
                Map<String, Object> bestFlowResult = multiFlowResult.get(bestFlowIndex);

                //-- Optimize the best wf and retrieve the result
                String c = (String)bestFlowResult.get("classifier");
                String a = (String)bestFlowResult.get("attributeSelection");
                logger.info(a + "/" + c);
                Map<String, Object> r = Trainer.getSingleton().executeForEvaluation(
                        datasetName,
                        trainingFileUrl,
                        testFileUrl,
                        output,
                        c,
                        a,
                        seed,
                        filePath);
                stopwatch.stop();
                long timeElapsed = stopwatch.elapsed(TimeUnit.MILLISECONDS);
                r.put("timeElapsed", timeElapsed);

                //-- Write to file
                try (FileWriter fw = new FileWriter(output, true);
                     BufferedWriter bw = new BufferedWriter(fw);
                     PrintWriter out = new PrintWriter(bw)) {
                    out.print(r.get("classifier"));
                    out.print(",");
                    out.print(r.get("attributeSelection"));
                    out.print(",");
                    out.print(seed);
                    out.print(",");
                    out.print(r.get("dataset"));
                    out.print(",");
                    out.print(r.get("weightedAreaUnderROC"));
                    out.print(",");
                    out.print(r.get("weightedFMeasure"));
                    out.print(",");
                    out.print(r.get("weightedPrecision"));
                    out.print(",");
                    out.print(r.get("weightedRecall"));
                    out.print(",");
                    out.print(r.get("errorRate"));
                    out.print(",");
                    out.print(r.get("timeElapsed"));
                    out.print("\n");
                } catch (IOException e) {
                    e.printStackTrace();
                }

                boolean everythingIsDone = false;
                while (!everythingIsDone){
                    everythingIsDone = true;
                    for(Future<Boolean> future : futures){
                        everythingIsDone &= future.get();
                    }
                }
                logger.info("=========================================================================================");
            }

        }
    }

}
