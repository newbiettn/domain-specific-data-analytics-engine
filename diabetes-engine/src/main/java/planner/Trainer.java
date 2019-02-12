package planner;
/**
 * Singleton class to execute and translate generated plan.
 *
 * @project diabetes-engine
 * @author newbiettn on 26/2/18
 *
 */

import com.google.common.base.Stopwatch;
import common.ProjectPropertiesGetter;
import db.DbUtils;
import planner.translator.TranslateToKFForSPARQL;
import planner.translator.TranslateToKFToEvaluateAgainstAutoWeka;
import javafx.util.Pair;
import model.DMDataset;
import model.DMOperator;
import model.DMParameter;
import model.DMPlan;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import planner.jshop.ProblemClassLoader;
import planner.translator.PlanTranslator;
import planner.translator.ProblemTranslator;
import weka.core.Attribute;
import weka.core.Instances;
import weka.core.WekaException;
import weka.core.converters.ArffLoader.ArffReader;
import weka.knowledgeflow.*;
import weka.knowledgeflow.steps.ClassifierPerformanceEvaluator;

import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.channels.FileChannel;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;


public class Trainer {
    private static Logger logger = LoggerFactory.getLogger(Trainer.class);;
    private ProjectPropertiesGetter propGetter = ProjectPropertiesGetter.getSingleton();

    //-- singleton
    private static Trainer singleton = new Trainer();
    private DMPlan plan;

    //-- private constructor to avoid client applications
    // to use constructor
    private Trainer(){
        this.plan = new DMPlan();
    }

    public static Trainer getSingleton(){
        return singleton;
    }

    public static void reset() {
        singleton = new Trainer();
        logger.info("reset trainer");
    }

    /**
     * Initialize the operator on-the-fly in planning.
     * Using this function, we can setup operators with required outputs so that
     * we can use them later without knowing how to setup them after plan generated from HTN.
     *
     * @param opConfig a pair of operator name & its parameters.
     *
     */
    public void initializeOperator(Pair<String, java.util.List> opConfig){
        DMOperator op = new DMOperator();
        String opName = opConfig.getKey();
        java.util.List<String> params = opConfig.getValue();
        if (opName == DMOperator.OperatorType.LOAD_ARFF.toString()) {
            op.setName("ArffLoader");
            String fileName = params.get(0);
            DMParameter param = new DMParameter("filename", fileName);
            op.addParameter(param);
            op.setType(DMOperator.OperatorType.LOAD_ARFF);
        } else if (opName == DMOperator.OperatorType.LOAD_CSV.toString()) {
            op.setName("CSVLoader");
            String fileName = params.get(0);
            DMParameter param = new DMParameter("filename", fileName);
            op.addParameter(param);
            op.setType(DMOperator.OperatorType.LOAD_CSV);
        } else if (opName == DMOperator.OperatorType.RANDOMIZE.toString()){
            op.setName("Randomize");
            op.setType(DMOperator.OperatorType.RANDOMIZE);
        } else if (opName == DMOperator.OperatorType.CLASS_ASSIGNER.toString()) {
            op.setName("ClassAssigner");
            String className = params.get(0);
            DMParameter param = new DMParameter("classname", className);
            op.addParameter(param);
            op.setType(DMOperator.OperatorType.CLASS_ASSIGNER);
        } else if (opName == DMOperator.OperatorType.REPLACE_MISSING_VALUE.toString()) {
            op.setName("ReplaceMissingValues");
            String className = params.get(0);
            DMParameter param = new DMParameter("classname", className);
            op.addParameter(param);
            op.setType(DMOperator.OperatorType.REPLACE_MISSING_VALUE);
        } else if (opName == DMOperator.OperatorType.REMOVE_FEATURE.toString()) {
            op.setName("Remove");
            String className = params.get(0);
            DMParameter param = new DMParameter("classname", className);
            op.addParameter(param);
            op.setType(DMOperator.OperatorType.REMOVE_FEATURE);
        } else if (opName == DMOperator.OperatorType.CFS_SUBSET_EVAL.toString()) {
            op.setName("CfsSubsetEval");
            op.setType(DMOperator.OperatorType.CFS_SUBSET_EVAL);
        } else if (opName == DMOperator.OperatorType.CORRELATION_ATTR_EVAL.toString()) {
            op.setName("CorrelationAttrEval");
            op.setType(DMOperator.OperatorType.CORRELATION_ATTR_EVAL);
        } else if (opName == DMOperator.OperatorType.GAIN_RATIO_ATTR_EVAL.toString()) {
            op.setName("GainRatioAttrEval");
            op.setType(DMOperator.OperatorType.GAIN_RATIO_ATTR_EVAL);
        } else if (opName == DMOperator.OperatorType.INFO_GAIN_ATTR_EVAL.toString()) {
            op.setName("InfoGainAttrEval");
            op.setType(DMOperator.OperatorType.INFO_GAIN_ATTR_EVAL);
        } else if (opName == DMOperator.OperatorType.RELIEFF_ATTR_EVAL.toString()) {
            op.setName("RelieffAttrEval");
            op.setType(DMOperator.OperatorType.RELIEFF_ATTR_EVAL);
        } else if (opName == DMOperator.OperatorType.SYMMETRICAL_UNCERT_ATTR_EVAL.toString()) {
            op.setName("SymmetricalUnCertAttrEval");
            op.setType(DMOperator.OperatorType.SYMMETRICAL_UNCERT_ATTR_EVAL);
        } else if (opName == DMOperator.OperatorType.WRAPPER_SUBSET_EVAL_WITH_ADABOOST.toString()) {
            op.setName("WrapperSubsetEvalWithADABOOST");
            op.setType(DMOperator.OperatorType.WRAPPER_SUBSET_EVAL_WITH_ADABOOST);
        } else if (opName == DMOperator.OperatorType.WRAPPER_SUBSET_EVAL_WITH_RF.toString()) {
            op.setName("WrapperSubsetEvalWithRF");
            op.setType(DMOperator.OperatorType.WRAPPER_SUBSET_EVAL_WITH_RF);
        } else if (opName == DMOperator.OperatorType.STANDARDIZATION.toString()) {
            op.setName("Standardize");
            op.setType(DMOperator.OperatorType.STANDARDIZATION);
        } else if (opName == DMOperator.OperatorType.CLASSIFIER_PERFORMANCE_EVALUATOR.toString()){
            op.setName("ClassifierPerformanceEvaluator");
            op.setType(DMOperator.OperatorType.CLASSIFIER_PERFORMANCE_EVALUATOR);
        } else if (opName == DMOperator.OperatorType.CROSS_VALIDATION_FOLD_MAKER.toString()) {
            op.setName("CrossValidationFoldMaker");
            op.setType(DMOperator.OperatorType.CROSS_VALIDATION_FOLD_MAKER);
        } else if (opName == DMOperator.OperatorType.J48.toString()) {
            op.setName("J48");
            op.setType(DMOperator.OperatorType.J48);
        } else if (opName == DMOperator.OperatorType.NEURAL_NETWORK.toString()) {
            op.setName("MultilayerPerceptron");
            op.setType(DMOperator.OperatorType.NEURAL_NETWORK);
        } else if (opName == DMOperator.OperatorType.RANDOM_FOREST.toString()) {
            op.setName("RandomForest");
            op.setType(DMOperator.OperatorType.RANDOM_FOREST);
        } else if (opName == DMOperator.OperatorType.ADABOOST_M1.toString()) {
            op.setName("AdaboostM1");
            op.setType(DMOperator.OperatorType.ADABOOST_M1);
        } else if (opName == DMOperator.OperatorType.LOGISTIC.toString()) {
            op.setName("Logistic");
            op.setType(DMOperator.OperatorType.LOGISTIC);
        } else if (opName == DMOperator.OperatorType.SMO.toString()) {
            op.setName("SMO");
            op.setType(DMOperator.OperatorType.SMO);
        } else if (opName == DMOperator.OperatorType.KSTAR.toString()) {
            op.setName("KStar");
            op.setType(DMOperator.OperatorType.KSTAR);
        } else if (opName == DMOperator.OperatorType.IBK.toString()) {
            op.setName("IBk");
            op.setType(DMOperator.OperatorType.IBK);
        } else if (opName == DMOperator.OperatorType.LWL.toString()) {
            op.setName("LWL");
            op.setType(DMOperator.OperatorType.LWL);
        } else if (opName == DMOperator.OperatorType.BAGGING.toString()) {
            op.setName("Bagging");
            op.setType(DMOperator.OperatorType.BAGGING);
        } else if (opName == DMOperator.OperatorType.LOGITBOOST.toString()) {
            op.setName("LogitBoost");
            op.setType(DMOperator.OperatorType.LOGITBOOST);
        } else if (opName == DMOperator.OperatorType.RANDOM_SUBSPACE.toString()) {
            op.setName("RandomSubSpace");
            op.setType(DMOperator.OperatorType.RANDOM_SUBSPACE);
        } else if (opName == DMOperator.OperatorType.STACKING.toString()) {
            op.setName("Stacking");
            op.setType(DMOperator.OperatorType.STACKING);
        } else if (opName == DMOperator.OperatorType.VOTE.toString()) {
            op.setName("Vote");
            op.setType(DMOperator.OperatorType.VOTE);
        } else if (opName == DMOperator.OperatorType.DECISION_TABLE.toString()) {
            op.setName("DecisionTable");
            op.setType(DMOperator.OperatorType.DECISION_TABLE);
        } else if (opName == DMOperator.OperatorType.RESULT_VIEWER.toString()) {
            op.setName("TextViewer");
            op.setType(DMOperator.OperatorType.RESULT_VIEWER);
        } else {
            logger.info("Operator not found!");
            op = null;
        }

        if (op != null){
            logger.info(op.getName());
            this.plan.addOperator(op);
        }
    }

    /**
     * Must call JSHOP to generate plans first.
     *
     * @return a plan with all initialized operators.
     *
     */
    public DMPlan generatePlan() throws ClassNotFoundException, IllegalAccessException, InstantiationException, IOException, NoSuchMethodException, InvocationTargetException {
        ClassLoader parentClassLoader = ProblemClassLoader.class.getClassLoader();
        ProblemClassLoader classLoader = new ProblemClassLoader(parentClassLoader);
        Class myObjectClass = classLoader.loadClass("planner.jshop.problem");

        //-- Create new class loader so classes can be reloaded.
        classLoader = new ProblemClassLoader(parentClassLoader);
        myObjectClass = classLoader.loadClass("planner.jshop.problem");
        Method m = myObjectClass.getMethod("getFirstPlanOps"); // get the method you want to call
        String[] args = new String[0]; // the arguments. Change this if you want to pass different args
        m.invoke(null, null);  // invoke the method

        return this.plan;
    }

    /**
     * Translate DMPlan to JSON and store it as a file.
     * At the moment, the output file name should be typed of *.kf,
     * which is Weka knowledge flow file.
     *
     * @return a file contains JSON output
     */
    private File translatePlan2Json(String outFileName,
                                    int seed,
                                    String filePath){
        File f = new File(outFileName);
        PlanTranslator planTranslator = new PlanTranslator(seed, filePath);
        planTranslator.setTheComponent(this.plan);
        String json = planTranslator.translate();
        try {
            FileWriter fw = new FileWriter(f, false);
            fw.write(json);
            fw.close();
            return f;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public File translatePlan2JsonForOptimization(String outFileName,
                                    int seed,
                                    String filePath){
        File f = new File(outFileName);
        PlanTranslator planTranslator = new PlanTranslator(seed, filePath);
        planTranslator.setTheComponent(this.plan);
        String json = planTranslator.translateWithOptimization();
        try {
            FileWriter fw = new FileWriter(f, false);
            fw.write(json);
            fw.close();
            return f;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Translate DMPlan to JSON and store it as a file.
     * At the moment, the output file name should be typed of *.kf,
     * which is Weka knowledge flow file.
     *
     * @return a file contains JSON output
     */
    public File translatePlanToJSONForMultiFlows(String outFileName,
                                                 int seed,
                                                 String filePath,
                                                 int flowIndex,
                                                 DMPlan plan,
                                                 int lastIndex){
        File f = new File(outFileName);
        PlanTranslator planTranslator = new PlanTranslator(seed, filePath);
        planTranslator.setTheComponent(this.plan);
        String json = planTranslator.translateMultiFlowBody(flowIndex, lastIndex);
        try {
            FileWriter fw = new FileWriter(f, true);
            fw.write(json);
            fw.close();
            return f;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static File makeHeaderForTranslation(String outFileName){
        File f = new File(outFileName);
        PlanTranslator planTranslator = new PlanTranslator();
        String json = planTranslator.translateMultiflowHeader();
        try {
            FileWriter fw = new FileWriter(f, false);
            fw.write(json);
            fw.close();
            return f;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static File makeFooterForTranslation(String outFileName){
        File f = new File(outFileName);
        PlanTranslator planTranslator = new PlanTranslator();
        String json = planTranslator.translateMultiflowFooter();
        try {
            FileWriter fw = new FileWriter(f, true);
            fw.write(json);
            fw.close();
            return f;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Execute the JSON plan file.
     *
     * @param planFile a file that contains translated generated plan in JSON format.
     * @return a FlowExecutor that allows to inspect the plan.
     *
     */
    public Flow executeTranslatedPlan(File planFile){
        JSONFlowLoader loader = new JSONFlowLoader();
        try {
            Flow flow = loader.readFlow(planFile);
            BaseExecutionEnvironment execE = new BaseExecutionEnvironment();
            FlowExecutor flowExecutor = execE.getDefaultFlowExecutor();
            flowExecutor.setFlow(flow);
            flowExecutor.runParallel();
            flowExecutor.waitUntilFinished();
            return flow;
        } catch (WekaException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Obtain data mining results from the plan.
     * The result will be retrieved from the last operator, which should be TextViewer.
     *
     * @param flow a Weka data mining flow
     * @return a formatted result of the output.
     *
     */
    public Map<String, Double> getDataMiningResult(Flow flow){
        int lastIndx = this.plan.getOperators().size() - 2; //-- the index of ClassifierEvaluation component
        DMOperator lastOp = this.plan.getOperators().get(lastIndx);
        StepManager stepManager = flow.findStep(lastOp.getName());
        ClassifierPerformanceEvaluator eval = (ClassifierPerformanceEvaluator)stepManager.getManagedStep();

        Map<String, Double> results = new HashMap<>();
        results.put("weightedAreaUnderROC", eval.getM_eval().weightedAreaUnderROC());
        results.put("weightedFMeasure", eval.getM_eval().weightedFMeasure());
        results.put("weightedPrecision", eval.getM_eval().weightedPrecision());
        results.put("weightedRecall", eval.getM_eval().weightedRecall());
        results.put("errorRate", eval.getM_eval().errorRate());
        return results;
    }
    /**
     * Obtain data mining results from the knowledge flow for evaluation against AutoWeka experiment.
     * The result is retrieved from the hardcoded operator "ClassifierPerformanceEvaluator"
     *
     * @param flow a Weka data mining flow
     * @return a formatted result of the output.
     *
     */
    public Map<String, Double> getDataMiningResultFromEvaluation(Flow flow){
        String opName = "ClassifierPerformanceEvaluator"; // because I've hardcoded the name of the operator for i = 0,..
        StepManager stepManager = flow.findStep(opName);
        ClassifierPerformanceEvaluator eval = (ClassifierPerformanceEvaluator)stepManager.getManagedStep();

        Map<String, Double> results = new HashMap<>();
        if (eval.getM_eval() != null) {
            results.put("weightedAreaUnderROC", eval.getM_eval().weightedAreaUnderROC());
            results.put("weightedFMeasure", eval.getM_eval().weightedFMeasure());
            results.put("weightedPrecision", eval.getM_eval().weightedPrecision());
            results.put("weightedRecall", eval.getM_eval().weightedRecall());
            results.put("errorRate", eval.getM_eval().errorRate());
        }

        return results;
    }
    /**
     * Obtain data mining results from the knowledge flow of multi flows.
     * The result is retrieved from the hardcoded operator "ClassifierPerformanceEvaluator" + i
     * where "i" is the index of flows.
     *
     * @param flow a Weka data mining flow
     * @return a formatted result of the output.
     *
     */
    public Map<String, Double> getDataMiningResultFromMultiFlow(Flow flow, int i){
        String opName = "ClassifierPerformanceEvaluator" + i; // because I've hardcoded the name of the operator for i = 0,..
        StepManager stepManager = flow.findStep(opName);
        ClassifierPerformanceEvaluator eval = (ClassifierPerformanceEvaluator)stepManager.getManagedStep();

        Map<String, Double> results = new HashMap<>();
        results.put("weightedAreaUnderROC", eval.getM_eval().weightedAreaUnderROC());
        results.put("weightedFMeasure", eval.getM_eval().weightedFMeasure());
        results.put("weightedPrecision", eval.getM_eval().weightedPrecision());
        results.put("weightedRecall", eval.getM_eval().weightedRecall());
        results.put("errorRate", eval.getM_eval().errorRate());
        return results;
    }

    private void printLines(String name, InputStream ins) throws Exception {
        String line = null;
        BufferedReader in = new BufferedReader(
                new InputStreamReader(ins));
        while ((line = in.readLine()) != null) {
            logger.info(name + " " + line);
        }
    }

    private void runProcess(String command) throws Exception {
        Process pro = Runtime.getRuntime().exec(command, null, new File("diabetes-engine/src/main/java/planner/jshop"));
        printLines(command + " :", pro.getInputStream());
        printLines(command + " stderr:", pro.getErrorStream());
        pro.waitFor();
        logger.info(command + " exitValue() " + pro.exitValue());
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
        sb.append("flow");
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

    private void writeResultToFile(File f,
                                   String workflowDesc,
                                   String dsName,
                                   int seed,
                                   String classifierOp,
                                   String attrSelectionOp,
                                   Map<String, Double> results,
                                   long timeElapsed) {
        try(FileWriter fw = new FileWriter(f, true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter out = new PrintWriter(bw)) {
            out.print(classifierOp);
            out.print(",");
            out.print(attrSelectionOp);
            out.print(",");
            out.print(seed);
            out.print(",");
            out.print(dsName);
            out.print(",");
            out.print(workflowDesc);
            out.print(",");
            out.print(results.get("weightedAreaUnderROC"));
            out.print(",");
            out.print(results.get("weightedFMeasure"));
            out.print(",");
            out.print(results.get("weightedPrecision"));
            out.print(",");
            out.print(results.get("weightedRecall"));
            out.print(",");
            out.print(results.get("errorRate"));
            out.print(",");
            out.print(timeElapsed);
            out.print("\n");
        } catch (IOException e) {
        }
    }

    /**
     * Fix the problem.java file by manually inserting the "package ..." to the file.
     *
     * @param inFile
     * @param lineno
     * @param lineToBeInserted
     * @throws Exception
     */
    private void insertStringInFile (File inFile, int lineno, String lineToBeInserted)
            throws Exception {
        // temp file
        File outFile = new File("$$$$$$$$.tmp");

        // input
        FileInputStream fis  = new FileInputStream(inFile);
        BufferedReader in = new BufferedReader
                (new InputStreamReader(fis));

        // output
        FileOutputStream fos = new FileOutputStream(outFile);
        PrintWriter out = new PrintWriter(fos);

        String thisLine = "";
        int i =1;
        while ((thisLine = in.readLine()) != null) {
            if(i == lineno) out.println(lineToBeInserted);
            out.println(thisLine);
            i++;
        }
        out.flush();
        out.close();
        in.close();

        inFile.delete();
        outFile.renameTo(inFile);
    }

    /**
     * The problem file generated automatically, thus its "implements" is not declared. We have to manually
     * insert.
     *
     * @param filename
     * @param offset
     * @param content
     * @throws IOException
     */
    private void insertToProblem(String filename, long offset, byte[] content) throws IOException {
        RandomAccessFile r = new RandomAccessFile(filename, "rw");
        RandomAccessFile rtemp = new RandomAccessFile(filename+"Temp", "rw");
        long fileSize = r.length();
        FileChannel sourceChannel = r.getChannel();
        FileChannel targetChannel = rtemp.getChannel();
        sourceChannel.transferTo(offset, (fileSize - offset), targetChannel);
        sourceChannel.truncate(offset);
        r.seek(offset);
        r.write(content);
        long newOffset = r.getFilePointer();
        targetChannel.position(0L);
        sourceChannel.transferFrom(targetChannel, newOffset, (fileSize - offset));
        sourceChannel.close();
        targetChannel.close();
        rtemp.close();
        r.close();
    }

    /**
     * Read the input ARFF dataset and generate the problem file.
     *
     * @param f an ARFF file
     * @return true: problem.java compiled successfully, false otherwise
     * @throws Exception
     */
    public boolean generateProblemFile(File f,
                                       String classifier,
                                       String attributeSelection) throws Exception {
        //-- Generate problem file by reading ARFF dataset file
        BufferedReader reader = new BufferedReader(new FileReader(f.getPath()));
        ArffReader arff = new ArffReader(reader);
        Instances data = arff.getData();
        data.setClassIndex(data.numAttributes()-1);
        Attribute target = data.classAttribute();
        DMDataset dmDataset = new DMDataset(f.getAbsolutePath(), target.name());
        ProblemTranslator problemTranslator = new ProblemTranslator(dmDataset, classifier, attributeSelection);
        problemTranslator.outputFile();

        //-- Interpret JSHOP2 datamining & problem files
        runProcess("rm datamining.txt problem.java datamining.java");
        runProcess("java JSHOP2.InternalDomain datamining");
        runProcess("java JSHOP2.InternalDomain -r problem");

        Thread.sleep(300);

        //-- Automatically add package declaration to datamining.java and problem.java
        String dmFilePath = propGetter.getProperty("datamining.file");
        String probFilePath = propGetter.getProperty("problem.file");
        File dataminingFile = new File(dmFilePath);
        File problemFile = new File(probFilePath);
        insertStringInFile(dataminingFile, 1, "package planner.jshop;");
        insertStringInFile(problemFile, 1, "package planner.jshop;");

        //-- Manually insert the text to problem.java
        byte[] content = (" implements ProblemInterface\n").getBytes();
        insertToProblem(probFilePath, 90,content ); //90 is the exact location

        File fRun = new File(probFilePath);
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null);
        Iterable<? extends JavaFileObject> compUnits =  fileManager.getJavaFileObjects(fRun);
        Iterable<String> options = Arrays.asList(new String[] {"-d", propGetter.getProperty("compiled.file.directory")});
        boolean compRes = compiler.getTask(null, fileManager, null, options, null, compUnits).call();
        return compRes;
    }

    /**
     *
     *
     * @param dataset
     * @param outputFile
     * @param classifier
     * @param attributeSelection
     * @param seed
     * @param filePath
     */
    public Map<String, Object>  executeAutoWeka(String datasetName,
                                                     String trainingFileUrl,
                                                     String testFileUrl,
                                                     int seed) {
        TranslateToKFToEvaluateAgainstAutoWeka trans = new TranslateToKFToEvaluateAgainstAutoWeka();
        String wekaOutputFile = propGetter.getProperty("single.weka.output.file");
        File kfFile = trans.translateForAutoWeka(
                wekaOutputFile,
                trainingFileUrl,
                testFileUrl,
                seed);
        Flow flow = executeTranslatedPlan(kfFile);
        Map<String, Double> results = getDataMiningResultFromEvaluation(flow);
        logger.info("+++++++++++++");
        logger.info("Autoweka : " + results.get("weightedAreaUnderROC")+"");

        //-- returning object
        Map<String, Object> r = new HashMap<>();
        r.put("classifier", "AutoWeka");
        r.put("attributeSelection", "AutoWeka");
        r.put("seed", seed);
        r.put("dataset", datasetName);
        r.put("weightedAreaUnderROC", results.get("weightedAreaUnderROC"));
        r.put("weightedFMeasure", results.get("weightedFMeasure"));
        r.put("weightedPrecision", results.get("weightedPrecision"));
        r.put("weightedRecall", results.get("weightedRecall"));
        r.put("errorRate", results.get("errorRate"));

        return r;
    }

    /**
     *
     *
     * @param datasetName
     * @param outputFile
     * @param classifier
     * @param attributeSelection
     * @param seed
     * @param filePath
     */
    public Map<String, Object>  executeWithoutOptimizationForEvaluation(String datasetName,
                                                     String trainingFileUrl,
                                                     String testFileUrl,
                                                     File outputFile,
                                                     String classifier,
                                                     String attributeSelection,
                                                     int seed,
                                                     String filePath) {
        TranslateToKFToEvaluateAgainstAutoWeka trans = new TranslateToKFToEvaluateAgainstAutoWeka();
        String wekaOutputFile = propGetter.getProperty("single.weka.output.file");
        File kfFile = trans.transforForMyEngineWithoutOptimization(
                wekaOutputFile,
                trainingFileUrl,
                testFileUrl,
                seed,
                attributeSelection,
                classifier);
        Flow flow = executeTranslatedPlan(kfFile);
        Map<String, Double> results = getDataMiningResultFromEvaluation(flow);

        //-- returning object
        Map<String, Object> r = new HashMap<>();
        r.put("classifier", classifier);
        r.put("attributeSelection", attributeSelection);
        r.put("seed", seed);
        r.put("dataset", datasetName);
        r.put("weightedAreaUnderROC", results.get("weightedAreaUnderROC"));
        r.put("weightedFMeasure", results.get("weightedFMeasure"));
        r.put("weightedPrecision", results.get("weightedPrecision"));
        r.put("weightedRecall", results.get("weightedRecall"));
        r.put("errorRate", results.get("errorRate"));

        return r;
    }

    /**
     * @param datasetName
     * @param outputFile
     * @param classifier
     * @param attributeSelection
     * @param seed
     * @param filePath
     */
    public Map<String, Object>  executeForEvaluation(String datasetName,
                                                     String trainingFileUrl,
                                                     String testFileUrl,
                                                     File outputFile,
                                                     String classifier,
                                                     String attributeSelection,
                                                     int seed,
                                                     String filePath) {
        TranslateToKFToEvaluateAgainstAutoWeka trans = new TranslateToKFToEvaluateAgainstAutoWeka();
        String wekaOutputFile = propGetter.getProperty("single.weka.output.file");
        File kfFile = trans.translateForMyEngine(
                wekaOutputFile,
                trainingFileUrl,
                testFileUrl,
                seed,
                attributeSelection,
                classifier);
        Flow flow = executeTranslatedPlan(kfFile);
        Map<String, Double> results = getDataMiningResultFromEvaluation(flow);

        //-- returning object
        Map<String, Object> r = new HashMap<>();
        r.put("classifier", classifier);
        r.put("attributeSelection", attributeSelection);
        r.put("seed", seed);
        r.put("dataset", datasetName);
        r.put("weightedAreaUnderROC", results.get("weightedAreaUnderROC"));
        r.put("weightedFMeasure", results.get("weightedFMeasure"));
        r.put("weightedPrecision", results.get("weightedPrecision"));
        r.put("weightedRecall", results.get("weightedRecall"));
        r.put("errorRate", results.get("errorRate"));

        return r;
    }
    /**
     * Read the ARFF file, generate the plan, execute it and then store the experiment into db.
     *
     * @param f the ARFF file
     * @param seed the seed for randomness
     * @param classifier name of the classifier
     * @param attributeSelection name of the attribute selection algorithm
     * @param insertToDb whether the new experiment result inserted to db
     * @throws Exception
     */
    public Map<String, Object> executeWithOptimization(File f,
                                       int seed,
                                       String classifier,
                                       String attributeSelection,
                                       boolean insertToDb, String filePath) throws Exception {
        DbUtils dbUtils = new DbUtils();
        Trainer plannerExe = Trainer.getSingleton();
        logger.info(f.getName());

        //-- Generate the JSHOP problem file
        boolean compiledResult = generateProblemFile(f, classifier, attributeSelection);

        if (compiledResult){
            //-- Execute the plan and output *.kf Weka KnowledgeFlow file
            logger.info("Compilation of problem.java successful!");

            //-- Generate the plan
            DMPlan plan = plannerExe.generatePlan();

            //-- Translate the plan to WEKA KNOWLEDGE FLOW file
            String wekaOutputFile= propGetter.getProperty("weka.output.file");
            File kfFile = plannerExe.translatePlan2JsonForOptimization(
                    wekaOutputFile,
                    seed,
                    filePath);

            //-- Execute the flow together with its execution time
            try {
                Stopwatch stopwatch = new Stopwatch();
                stopwatch.start();
                Flow flow = plannerExe.executeTranslatedPlan(kfFile);
                stopwatch.stop();
                long timeElapsed = stopwatch.elapsed(TimeUnit.MILLISECONDS);

                //-- Collect the flow results
                Map<String, Double> results = plannerExe.getDataMiningResult(flow);

                //-- Describe the workflow
                StringBuilder strBuilder = new StringBuilder();
                for (DMOperator op : plan.getOperators()){
                    strBuilder.append(op.getType().toString());
                    if (op.getNextOp() != null) strBuilder.append("|");
                }
                logger.info(strBuilder.toString());
                String workflowDesc = strBuilder.toString();

                //-- Insert the experiment to db
                Map<String, Object> r = new HashMap<>();
                r.put("classifier", classifier);
                r.put("attributeSelection", attributeSelection);
                r.put("seed", (double)seed);
                r.put("dataset", f.getName());
                r.put("flow", workflowDesc);
                r.put("weightedAreaUnderROC", results.get("weightedAreaUnderROC"));
                r.put("weightedFMeasure", results.get("weightedFMeasure"));
                r.put("weightedPrecision", results.get("weightedPrecision"));
                r.put("weightedRecall", results.get("weightedRecall"));
                r.put("errorRate", results.get("errorRate"));
                r.put("timeElapsed", (double)timeElapsed);

                if (insertToDb) //only insert to db if required
                    dbUtils.insertNewExperiment(r);

                logger.info("==========================================================================================");
                reset();
                return r;
            } catch (Exception e){
                logger.warn(e+"");
            }
        } else {
            logger.warn("Compilation of problem.java failed!");
        }
        logger.info("==========================================================================================");
        reset();
        return null;
    }

    /**
     * Read the ARFF file, generate the plan, execute it and then store the experiment into db.
     *
     * @param f the ARFF file
     * @param seed the seed for randomness
     * @param classifier name of the classifier
     * @param attributeSelection name of the attribute selection algorithm
     * @param insertToDb whether the new experiment result inserted to db
     * @throws Exception
     */
    public Map<String, Object> execute(File f,
                        int seed,
                        String classifier,
                        String attributeSelection,
                        boolean insertToDb, String filePath) throws Exception {
        DbUtils dbUtils = new DbUtils();
        logger.info(f.getName());

        //-- Generate the JSHOP problem file
        boolean compiledResult = generateProblemFile(f, classifier, attributeSelection);

        if (compiledResult){
            //-- Execute the plan and output *.kf Weka KnowledgeFlow file
            logger.info("Compilation of problem.java successful!");

            //-- Generate the plan
            DMPlan plan = generatePlan();

            //-- Translate the plan to WEKA KNOWLEDGE FLOW file
            String wekaOutputFile= propGetter.getProperty("weka.output.file");
            File kfFile = translatePlan2Json(
                    wekaOutputFile,
                    seed,
                    filePath);

            //-- Execute the flow together with its execution time
            try {
                Stopwatch stopwatch = new Stopwatch();
                stopwatch.start();
                Flow flow = executeTranslatedPlan(kfFile);
                stopwatch.stop();
                long timeElapsed = stopwatch.elapsed(TimeUnit.MILLISECONDS);

                //-- Collect the flow results
                Map<String, Double> results = getDataMiningResult(flow);

                //-- Describe the workflow
                StringBuilder strBuilder = new StringBuilder();
                for (DMOperator op : plan.getOperators()){
                    strBuilder.append(op.getType().toString());
                    if (op.getNextOp() != null) strBuilder.append("|");
                }
                logger.info(strBuilder.toString());
                String workflowDesc = strBuilder.toString();

                //-- Insert the experiment to db
                Map<String, Object> r = new HashMap<>();
                r.put("classifier", classifier);
                r.put("attributeSelection", attributeSelection);
                r.put("seed", (double)seed);
                r.put("dataset", f.getName());
                r.put("flow", workflowDesc);
                r.put("weightedAreaUnderROC", results.get("weightedAreaUnderROC"));
                r.put("weightedFMeasure", results.get("weightedFMeasure"));
                r.put("weightedPrecision", results.get("weightedPrecision"));
                r.put("weightedRecall", results.get("weightedRecall"));
                r.put("errorRate", results.get("errorRate"));
                r.put("timeElapsed", (double)timeElapsed);

                if (insertToDb) //only insert to db if required
                    dbUtils.insertNewExperiment(r);

                logger.info("==========================================================================================");
                reset();
                return r;
            } catch (Exception e){
                logger.warn(e+"");
            }
        } else {
            logger.warn("Compilation of problem.java failed!");
        }
        logger.info("==========================================================================================");
        reset();
        return null;
    }
    public Map<String, Object> executeMultiFlows(File f,
                                       int seed,
                                       String[] classifier,
                                       String[] attributeSelection,
                                       boolean insertToDb, String filePath) throws Exception {
        DbUtils dbUtils = new DbUtils();
        logger.info(f.getName());

        String flowFileName = propGetter.getProperty("multiflow.weka.output.file");
        makeHeaderForTranslation(flowFileName);
        File kfFile = new File(flowFileName);
        int flowIndex = 0;
        for (String c : classifier){
            for (String a : attributeSelection){
                boolean compiledResult = generateProblemFile(f, c, a);
                if (compiledResult) {
                    //-- Generate the plan
                    DMPlan plan = generatePlan();
                    logger.info(plan.toString());

                    //-- Translate the plan to WEKA KNOWLEDGE FLOW file
//                    kfFile = translatePlanToJSONForMultiFlows(
//                            flowFileName,
//                            seed,
//                            filePath,
//                            flowIndex,
//                            plan,
//                            lastIndex);
                    StringBuilder strBuilder = new StringBuilder();
                    for (DMOperator op : this.plan.getOperators()){
                        strBuilder.append(op.getName());
                        if (op.getNextOp() != null) strBuilder.append("|");
                    }
                    flowIndex++;
                    reset();
                    logger.info(strBuilder.toString());
                } else {
                    logger.warn("Compilation of problem.java failed!");
                }

            }
        }
        makeFooterForTranslation(flowFileName);
        return null;
    }

    public static void main (String[] args) throws Exception {
        ProjectPropertiesGetter propGetter = ProjectPropertiesGetter.getSingleton();
        Trainer trainer = new Trainer();
        String[] classifiers = new String[]{
                "j48",
//                "adaboostm1",
                "randomforest"};
//                "neuralnetwork",
//                "logistic"};
//                "smo",
//                "kstar",
//                "ibk",
//                "lwl",
//                "bagging",
//                "logitboost",
//                "randomsubspace",
//                "stacking",
//                "vote",
//                "decisiontable"};
        String[] attributeSelections = new String[]{
                "no-attribute-selection"};
//                "cfs-subset-eval",
//                "correlation-attribute-eval",
//                "gain-ratio-attribute-eval",
//                "info-gain-attribute-eval",
//                "relieff-attribute-eval",
//                "symmetrical-uncert-attribute-eval",
//                "wrapper-subset-eval-with-adaboost-operator"};

        //-- Sequentially read all files from the data repository
        File folder = new File(propGetter.getProperty("multithread.testing.dataset.collection"));
        File[] files = folder.listFiles();
        String filePath = propGetter.getProperty("multithread.testing.dataset.collection");
        for (String c : classifiers){
            for (String a : attributeSelections){
                for(File f : files){
                    //-- MacOS always have DS_Store file, have to handle it
                    if (FilenameUtils.getExtension(f.getName()).equals("DS_Store"))
                        continue;

                    //-- Execution
//                    String classifier = "decisiontable";
//                    String attributeSelection = "relieff-attribute-eval";
                    if (c.equals("neuralnetwork"))
                        a = "no-attribute-selection";
                    for (int seed = 1; seed<=1; seed++){
                        trainer.execute(f, seed, c, a, false, filePath);
                    }
                }
            }
        }
    }

    /**
     *
     * @param datasetName
     * @param modelName
     * @param classifier
     * @param attributeSelection
     * @param seed
     * @param filePath
     */
    public void trainModelForSPARQL(String datasetName,
                                                    String trainingFileUrl,
                                                    String modelName,
                                                    String classifier,
                                                    String attributeSelection,
                                                    int seed,
                                                    String filePath) {
        TranslateToKFForSPARQL trans = new TranslateToKFForSPARQL();
        String wekaOutputFile = propGetter.getProperty("single.weka.output.file");
        File kfFile = trans.translateToSaveModelForSPARQL(
                wekaOutputFile,
                trainingFileUrl,
                modelName,
                seed,
                attributeSelection,
                classifier);
        Flow flow = executeTranslatedPlan(kfFile);
    }
}
