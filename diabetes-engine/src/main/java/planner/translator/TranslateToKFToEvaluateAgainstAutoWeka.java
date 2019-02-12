package planner.translator;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class TranslateToKFToEvaluateAgainstAutoWeka {
    public File translateForAutoWeka(String outFileName,
                            String trainingSet,
                            String testSet,
                            int seed){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("{\n" +
                "\t\"flow_name\" : \"evaluate_auto_weka\",\n" +
                "\t\"steps\" : [\n" +
                "\t\t{\n" +
                "\t\t\t\"class\" : \"weka.knowledgeflow.steps.Loader\",\n" +
                "\t\t\t\"properties\" : {\n" +
                "\t\t\t\t\"loader\" : {\n" +
                "\t\t\t\t\t\"type\" : \"loader\",\n" +
                "\t\t\t\t\t\"class\" : \"weka.core.converters.ArffLoader\",\n" +
                "\t\t\t\t\t\"filePath\" : \"" + trainingSet + "\",\n" +
                "\t\t\t\t\t\"useRelativePath\" : false\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t\"name\" : \"ArffLoader\"\n" +
                "\t\t\t},\n" +
                "\t\t\t\"connections\" : {\n" +
                "\t\t\t\t\"dataSet\" : [\n" +
                "\t\t\t\t\t\"ClassAssigner\"\n" +
                "\t\t\t\t]\n" +
                "\t\t\t},\n" +
                "\t\t\t\"coordinates\" : \"161,25\"\n" +
                "\t\t},\n" +
                "\t\t{\n" +
                "\t\t\t\"class\" : \"weka.knowledgeflow.steps.ClassAssigner\",\n" +
                "\t\t\t\"properties\" : {\n" +
                "\t\t\t\t\"classColumn\" : \"class\",\n" +
                "\t\t\t\t\"name\" : \"ClassAssigner\"\n" +
                "\t\t\t},\n" +
                "\t\t\t\"connections\" : {\n" +
                "\t\t\t\t\"dataSet\" : [\n" +
                "\t\t\t\t\t\"TrainingSetMaker\"\n" +
                "\t\t\t\t]\n" +
                "\t\t\t},\n" +
                "\t\t\t\"coordinates\" : \"319,25\"\n" +
                "\t\t},\n" +
                "\t\t{\n" +
                "\t\t\t\"class\" : \"weka.knowledgeflow.steps.TextViewer\",\n" +
                "\t\t\t\"properties\" : {\n" +
                "\t\t\t\t\"name\" : \"TextViewer\"\n" +
                "\t\t\t},\n" +
                "\t\t\t\"connections\" : {\n" +
                "\t\t\t},\n" +
                "\t\t\t\"coordinates\" : \"984,85\"\n" +
                "\t\t},\n" +
                "\t\t{\n" +
                "\t\t\t\"class\" : \"weka.knowledgeflow.steps.Classifier\",\n" +
                "\t\t\t\"properties\" : {\n" +
                "\t\t\t\t\"classifier\" : {\n" +
                "\t\t\t\t\t\"type\" : \"optionHandler\",\n" +
                "\t\t\t\t\t\"class\" : \"weka.classifiers.meta.AutoWEKAClassifier\",\n" +
                "\t\t\t\t\t\"options\" : \"-seed " + seed + " -timeLimit 2 -memLimit 8000 -nBestConfigs 1 -metric errorRate -parallelRuns 10\"\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t\"loadClassifierFileName\" : \"\",\n" +
                "\t\t\t\t\"name\" : \"AutoWEKAClassifier\",\n" +
                "\t\t\t\t\"resetIncrementalClassifier\" : false,\n" +
                "\t\t\t\t\"updateIncrementalClassifier\" : true\n" +
                "\t\t\t},\n" +
                "\t\t\t\"connections\" : {\n" +
                "\t\t\t\t\"batchClassifier\" : [\n" +
                "\t\t\t\t\t\"ClassifierPerformanceEvaluator\"\n" +
                "\t\t\t\t]\n" +
                "\t\t\t},\n" +
                "\t\t\t\"coordinates\" : \"668,86\"\n" +
                "\t\t},\n" +
                "\t\t{\n" +
                "\t\t\t\"class\" : \"weka.knowledgeflow.steps.ClassifierPerformanceEvaluator\",\n" +
                "\t\t\t\"properties\" : {\n" +
                "\t\t\t\t\"costMatrixString\" : \"\",\n" +
                "\t\t\t\t\"errorPlotPointSizeProportionalToMargin\" : false,\n" +
                "\t\t\t\t\"evaluateWithRespectToCosts\" : false,\n" +
                "\t\t\t\t\"evaluationMetricsToOutput\" : \"Correct,Incorrect,Kappa,Total cost,Average cost,KB relative,KB information,Correlation,Complexity 0,Complexity scheme,Complexity improvement,MAE,RMSE,RAE,RRSE,TP rate,FP rate,Precision,Recall,F-measure,MCC,ROC area,PRC area\",\n" +
                "\t\t\t\t\"name\" : \"ClassifierPerformanceEvaluator\"\n" +
                "\t\t\t},\n" +
                "\t\t\t\"connections\" : {\n" +
                "\t\t\t\t\"text\" : [\n" +
                "\t\t\t\t\t\"TextViewer\"\n" +
                "\t\t\t\t]\n" +
                "\t\t\t},\n" +
                "\t\t\t\"coordinates\" : \"839,86\"\n" +
                "\t\t},\n" +
                "\t\t{\n" +
                "\t\t\t\"class\" : \"weka.knowledgeflow.steps.TrainingSetMaker\",\n" +
                "\t\t\t\"properties\" : {\n" +
                "\t\t\t\t\"name\" : \"TrainingSetMaker\"\n" +
                "\t\t\t},\n" +
                "\t\t\t\"connections\" : {\n" +
                "\t\t\t\t\"trainingSet\" : [\n" +
                "\t\t\t\t\t\"AutoWEKAClassifier\"\n" +
                "\t\t\t\t]\n" +
                "\t\t\t},\n" +
                "\t\t\t\"coordinates\" : \"475,25\"\n" +
                "\t\t},\n" +
                "\t\t{\n" +
                "\t\t\t\"class\" : \"weka.knowledgeflow.steps.Loader\",\n" +
                "\t\t\t\"properties\" : {\n" +
                "\t\t\t\t\"loader\" : {\n" +
                "\t\t\t\t\t\"type\" : \"loader\",\n" +
                "\t\t\t\t\t\"class\" : \"weka.core.converters.ArffLoader\",\n" +
                "\t\t\t\t\t\"filePath\" : \"" + testSet + "\",\n" +
                "\t\t\t\t\t\"useRelativePath\" : false\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t\"name\" : \"ArffLoader2\"\n" +
                "\t\t\t},\n" +
                "\t\t\t\"connections\" : {\n" +
                "\t\t\t\t\"dataSet\" : [\n" +
                "\t\t\t\t\t\"ClassAssigner2\"\n" +
                "\t\t\t\t]\n" +
                "\t\t\t},\n" +
                "\t\t\t\"coordinates\" : \"162,154\"\n" +
                "\t\t},\n" +
                "\t\t{\n" +
                "\t\t\t\"class\" : \"weka.knowledgeflow.steps.ClassAssigner\",\n" +
                "\t\t\t\"properties\" : {\n" +
                "\t\t\t\t\"classColumn\" : \"/last\",\n" +
                "\t\t\t\t\"name\" : \"ClassAssigner2\"\n" +
                "\t\t\t},\n" +
                "\t\t\t\"connections\" : {\n" +
                "\t\t\t\t\"dataSet\" : [\n" +
                "\t\t\t\t\t\"TestSetMaker\"\n" +
                "\t\t\t\t]\n" +
                "\t\t\t},\n" +
                "\t\t\t\"coordinates\" : \"323,152\"\n" +
                "\t\t},\n" +
                "\t\t{\n" +
                "\t\t\t\"class\" : \"weka.knowledgeflow.steps.TestSetMaker\",\n" +
                "\t\t\t\"properties\" : {\n" +
                "\t\t\t\t\"name\" : \"TestSetMaker\"\n" +
                "\t\t\t},\n" +
                "\t\t\t\"connections\" : {\n" +
                "\t\t\t\t\"testSet\" : [\n" +
                "\t\t\t\t\t\"AutoWEKAClassifier\"\n" +
                "\t\t\t\t]\n" +
                "\t\t\t},\n" +
                "\t\t\t\"coordinates\" : \"471,153\"\n" +
                "\t\t}\n" +
                "\t]\n" +
                "}\n");
        File f = new File(outFileName);
        String json = stringBuilder.toString();
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

    public File transforForMyEngineWithoutOptimization(String outFileName,
                                                       String trainingSet,
                                                       String testSet,
                                                       int seed,
                                                       String attributeSelection,
                                                       String classifier){
        StringBuilder stringBuilder = new StringBuilder();
        String attributeSelectionSpec = "";
        if (attributeSelection.equals("cfs-subset-eval")){
            attributeSelectionSpec = "\"-E \\\"weka.attributeSelection.CfsSubsetEval -P 1 -E 1\\\" -S " +
                    "\\\"weka.attributeSelection.BestFirst -D 1 -N 5\\\"\"\n";
        } else if (attributeSelection.equals("correlation-attribute-eval")){
            attributeSelectionSpec = "\"-E \\\"weka.attributeSelection.CorrelationAttributeEval \\\" -S " +
                    "\\\"weka.attributeSelection.Ranker -T -1.7976931348623157E308 -N -1\\\"\"\n";
        } else if (attributeSelection.equals("gain-ratio-attribute-eval")){
            attributeSelectionSpec = "\"-E \\\"weka.attributeSelection.GainRatioAttributeEval \\\" -S " +
                    "\\\"weka.attributeSelection.Ranker -T -1.7976931348623157E308 -N -1\\\"\"\n";
        } else if (attributeSelection.equals("info-gain-attribute-eval")){
            attributeSelectionSpec = "\"-E \\\"weka.attributeSelection.InfoGainAttributeEval \\\" -S " +
                    "\\\"weka.attributeSelection.Ranker -T -1.7976931348623157E308 -N -1\\\"\"\n";
        } else if (attributeSelection.equals("relieff-attribute-eval")){
            attributeSelectionSpec = "\"-E \\\"weka.attributeSelection.ReliefFAttributeEval -M -1 -D 1 -K 10\\\" -S " +
                    "\\\"weka.attributeSelection.Ranker -T -1.7976931348623157E308 -N -1\\\"\"\n";
        } else if (attributeSelection.equals("symmetrical-uncert-attribute-eval")){
            attributeSelectionSpec = "\"-E \\\"weka.attributeSelection.SymmetricalUncertAttributeEval \\\" -S " +
                    "\\\"weka.attributeSelection.Ranker -T -1.7976931348623157E308 -N -1\\\"\"\n";
        } else if (attributeSelection.equals("wrapper-subset-eval-with-adaboost-operator")){
            attributeSelectionSpec = "\"-E \\\"weka.attributeSelection.WrapperSubsetEval -B weka.classifiers.meta.AdaBoostM1 -F 5 -T 0.01 -R 1 -E AUC -- -P 100 -S " + seed + " -I 10 -W weka.classifiers.trees.DecisionStump\\\\\" -S " +
                    "\\\"weka.attributeSelection.GreedyStepwise -T -1.7976931348623157E308 -N -1 -num-slots 1\\\"\"\n";
        }

        String classifierSpec = "";
        String classifierClassName = "";
        if (classifier.equals("randomforest")){
            classifierSpec = "{\n" +
                    "\t\t\t\"class\" : \"weka.knowledgeflow.steps.Classifier\",\n" +
                    "\t\t\t\"properties\" : {\n" +
                    "\t\t\t\t\"classifier\" : {\n" +
                    "\t\t\t\t\t\"type\" : \"optionHandler\",\n" +
                    "\t\t\t\t\t\"class\" : \"weka.classifiers.trees.RandomForest\",\n" +
                    "\t\t\t\t\t\"options\" : \"-P 100 -I 100 -num-slots 1 -K 0 -M 1.0 -V 0.001 -S " + seed + "\"\n" +
                    "\t\t\t\t},\n" +
                    "\t\t\t\t\"loadClassifierFileName\" : \"\",\n" +
                    "\t\t\t\t\"name\" : \"Classifier\",\n" +
                    "\t\t\t\t\"resetIncrementalClassifier\" : false,\n" +
                    "\t\t\t\t\"updateIncrementalClassifier\" : true\n" +
                    "\t\t\t},\n" +
                    "\t\t\t\"connections\" : {\n" +
                    "\t\t\t\t\"batchClassifier\" : [\n" +
                    "\t\t\t\t\t\"ClassifierPerformanceEvaluator\"\n" +
                    "\t\t\t\t]\n" +
                    "\t\t\t},\n" +
                    "\t\t\t\"coordinates\" : \"486,501\"\n" +
                    "\t\t}";
        } else if(classifier.equals("logitboost")){
            classifierSpec = "{\n" +
                    "\t\t\t\"class\" : \"weka.knowledgeflow.steps.Classifier\",\n" +
                    "\t\t\t\"properties\" : {\n" +
                    "\t\t\t\t\"classifier\" : {\n" +
                    "\t\t\t\t\t\"type\" : \"optionHandler\",\n" +
                    "\t\t\t\t\t\"class\" : \"weka.classifiers.meta.LogitBoost\",\n" +
                    "\t\t\t\t\t\"options\" : \"-P 100 -L -1.7976931348623157E308 -H 1.0 -Z 3.0 -O 1 -E 1 -S " + seed + " -I 10 -W weka.classifiers.trees.DecisionStump\"\n" +
                    "\t\t\t\t},\n" +
                    "\t\t\t\t\"loadClassifierFileName\" : \"\",\n" +
                    "\t\t\t\t\"name\" : \"Classifier\",\n" +
                    "\t\t\t\t\"resetIncrementalClassifier\" : false,\n" +
                    "\t\t\t\t\"updateIncrementalClassifier\" : true\n" +
                    "\t\t\t},\n" +
                    "\t\t\t\"connections\" : {\n" +
                    "\t\t\t\t\"batchClassifier\" : [\n" +
                    "\t\t\t\t\t\"ClassifierPerformanceEvaluator\"\n" +
                    "\t\t\t\t]\n" +
                    "\t\t\t},\n" +
                    "\t\t\t\"coordinates\" : \"506,697\"\n" +
                    "\t\t}";
        } else if (classifier.equals("smo")) {
            classifierSpec = "{\n" +
                    "\t\t\t\"class\" : \"weka.knowledgeflow.steps.Classifier\",\n" +
                    "\t\t\t\"properties\" : {\n" +
                    "\t\t\t\t\"classifier\" : {\n" +
                    "\t\t\t\t\t\"type\" : \"optionHandler\",\n" +
                    "\t\t\t\t\t\"class\" : \"weka.classifiers.functions.SMO\",\n" +
                    "\t\t\t\t\t\"options\" : \"-C 1.0 -L 0.001 -P 1.0E-12 -N 0 -V -1 -W " + seed + " -K \\\"weka.classifiers.functions.supportVector.PolyKernel -E 1.0 -C 250007\\\" -calibrator \\\"weka.classifiers.functions.Logistic -R 1.0E-8 -M -1 -num-decimal-places 4\\\"\"\n" +
                    "\t\t\t\t},\n" +
                    "\t\t\t\t\"loadClassifierFileName\" : \"\",\n" +
                    "\t\t\t\t\"name\" : \"Classifier\",\n" +
                    "\t\t\t\t\"resetIncrementalClassifier\" : false,\n" +
                    "\t\t\t\t\"updateIncrementalClassifier\" : true\n" +
                    "\t\t\t},\n" +
                    "\t\t\t\"connections\" : {\n" +
                    "\t\t\t\t\"batchClassifier\" : [\n" +
                    "\t\t\t\t\t\"ClassifierPerformanceEvaluator\"\n" +
                    "\t\t\t\t]\n" +
                    "\t\t\t},\n" +
                    "\t\t\t\"coordinates\" : \"506,697\"\n" +
                    "\t\t}";
        } else if (classifier.equals("randomsubspace")) {
            classifierSpec = "{\n" +
                    "\t\t\t\"class\" : \"weka.knowledgeflow.steps.Classifier\",\n" +
                    "\t\t\t\"properties\" : {\n" +
                    "\t\t\t\t\"classifier\" : {\n" +
                    "\t\t\t\t\t\"type\" : \"optionHandler\",\n" +
                    "\t\t\t\t\t\"class\" : \"weka.classifiers.meta.RandomSubSpace\",\n" +
                    "\t\t\t\t\t\"options\" : \"-P 0.5 -S " + seed + " -num-slots 1 -I 10 -W weka.classifiers.trees.REPTree -- -M 2 -V 0.001 -N 3 -S " + seed + " -L -1 -I 0.0\"\n" +
                    "\t\t\t\t},\n" +
                    "\t\t\t\t\"loadClassifierFileName\" : \"\",\n" +
                    "\t\t\t\t\"name\" : \"Classifier\",\n" +
                    "\t\t\t\t\"resetIncrementalClassifier\" : false,\n" +
                    "\t\t\t\t\"updateIncrementalClassifier\" : true\n" +
                    "\t\t\t},\n" +
                    "\t\t\t\"connections\" : {\n" +
                    "\t\t\t\t\"batchClassifier\" : [\n" +
                    "\t\t\t\t\t\"ClassifierPerformanceEvaluator\"\n" +
                    "\t\t\t\t]\n" +
                    "\t\t\t},\n" +
                    "\t\t\t\"coordinates\" : \"506,697\"\n" +
                    "\t\t}";
        } else if (classifier.equals("kstar")) {
            classifierSpec = "{\n" +
                    "\t\t\t\"class\" : \"weka.knowledgeflow.steps.Classifier\",\n" +
                    "\t\t\t\"properties\" : {\n" +
                    "\t\t\t\t\"classifier\" : {\n" +
                    "\t\t\t\t\t\"type\" : \"optionHandler\",\n" +
                    "\t\t\t\t\t\"class\" : \"weka.classifiers.lazy.KStar\",\n" +
                    "\t\t\t\t\t\"options\" : \"-B 20 -M a\"\n" +
                    "\t\t\t\t},\n" +
                    "\t\t\t\t\"loadClassifierFileName\" : \"\",\n" +
                    "\t\t\t\t\"name\" : \"Classifier\",\n" +
                    "\t\t\t\t\"resetIncrementalClassifier\" : false,\n" +
                    "\t\t\t\t\"updateIncrementalClassifier\" : true\n" +
                    "\t\t\t},\n" +
                    "\t\t\t\"connections\" : {\n" +
                    "\t\t\t\t\"batchClassifier\" : [\n" +
                    "\t\t\t\t\t\"ClassifierPerformanceEvaluator\"\n" +
                    "\t\t\t\t]\n" +
                    "\t\t\t},\n" +
                    "\t\t\t\"coordinates\" : \"506,697\"\n" +
                    "\t\t}";
        } else if (classifier.equals("j48")) {
            classifierSpec = "{\n" +
                    "\t\t\t\"class\" : \"weka.knowledgeflow.steps.Classifier\",\n" +
                    "\t\t\t\"properties\" : {\n" +
                    "\t\t\t\t\"classifier\" : {\n" +
                    "\t\t\t\t\t\"type\" : \"optionHandler\",\n" +
                    "\t\t\t\t\t\"class\" : \"weka.classifiers.trees.J48\",\n" +
                    "\t\t\t\t\t\"options\" : \"-C 0.25 -M 2 -Q " + seed +"\"\n" +
                    "\t\t\t\t},\n" +
                    "\t\t\t\t\"loadClassifierFileName\" : \"\",\n" +
                    "\t\t\t\t\"name\" : \"Classifier\",\n" +
                    "\t\t\t\t\"resetIncrementalClassifier\" : false,\n" +
                    "\t\t\t\t\"updateIncrementalClassifier\" : true\n" +
                    "\t\t\t},\n" +
                    "\t\t\t\"connections\" : {\n" +
                    "\t\t\t\t\"batchClassifier\" : [\n" +
                    "\t\t\t\t\t\"ClassifierPerformanceEvaluator\"\n" +
                    "\t\t\t\t]\n" +
                    "\t\t\t},\n" +
                    "\t\t\t\"coordinates\" : \"506,697\"\n" +
                    "\t\t}";
        } else if (classifier.equals("bagging")){
            classifierSpec = "{\n" +
                    "\t\t\t\"class\" : \"weka.knowledgeflow.steps.Classifier\",\n" +
                    "\t\t\t\"properties\" : {\n" +
                    "\t\t\t\t\"classifier\" : {\n" +
                    "\t\t\t\t\t\"type\" : \"optionHandler\",\n" +
                    "\t\t\t\t\t\"class\" : \"weka.classifiers.meta.Bagging\",\n" +
                    "\t\t\t\t\t\"options\" : \"-P 100 -S " + seed +" -num-slots 1 -I 10 -W weka.classifiers.trees.REPTree -- -M 2 -V 0.001 -N 3 -S 1 -L -1 -I 0.0\"\n" +
                    "\t\t\t\t},\n" +
                    "\t\t\t\t\"loadClassifierFileName\" : \"\",\n" +
                    "\t\t\t\t\"name\" : \"Classifier\",\n" +
                    "\t\t\t\t\"resetIncrementalClassifier\" : false,\n" +
                    "\t\t\t\t\"updateIncrementalClassifier\" : true\n" +
                    "\t\t\t},\n" +
                    "\t\t\t\"connections\" : {\n" +
                    "\t\t\t\t\"batchClassifier\" : [\n" +
                    "\t\t\t\t\t\"ClassifierPerformanceEvaluator\"\n" +
                    "\t\t\t\t]\n" +
                    "\t\t\t},\n" +
                    "\t\t\t\"coordinates\" : \"506,697\"\n" +
                    "\t\t}";
        } else if (classifier.equals("neuralnetwork")) {
            classifierSpec = "{\n" +
                    "\t\t\t\"class\" : \"weka.knowledgeflow.steps.Classifier\",\n" +
                    "\t\t\t\"properties\" : {\n" +
                    "\t\t\t\t\"classifier\" : {\n" +
                    "\t\t\t\t\t\"type\" : \"optionHandler\",\n" +
                    "\t\t\t\t\t\"class\" : \"weka.classifiers.functions.MultilayerPerceptron\",\n" +
                    "\t\t\t\t\t\"options\" : \"-L 0.3 -M 0.2 -N 500 -V 0 -S " +  seed + " -E 20 -H a\"\n" +
                    "\t\t\t\t},\n" +
                    "\t\t\t\t\"loadClassifierFileName\" : \"\",\n" +
                    "\t\t\t\t\"name\" : \"Classifier\",\n" +
                    "\t\t\t\t\"resetIncrementalClassifier\" : false,\n" +
                    "\t\t\t\t\"updateIncrementalClassifier\" : true\n" +
                    "\t\t\t},\n" +
                    "\t\t\t\"connections\" : {\n" +
                    "\t\t\t\t\"batchClassifier\" : [\n" +
                    "\t\t\t\t\t\"ClassifierPerformanceEvaluator\"\n" +
                    "\t\t\t\t]\n" +
                    "\t\t\t},\n" +
                    "\t\t\t\"coordinates\" : \"486,501\"\n" +
                    "\t\t}";
        } else if (classifier.equals("logistic")) {
            classifierSpec = "{\n" +
                    "\t\t\t\"class\" : \"weka.knowledgeflow.steps.Classifier\",\n" +
                    "\t\t\t\"properties\" : {\n" +
                    "\t\t\t\t\"classifier\" : {\n" +
                    "\t\t\t\t\t\"type\" : \"optionHandler\",\n" +
                    "\t\t\t\t\t\"class\" : \"weka.classifiers.functions.Logistic\",\n" +
                    "\t\t\t\t\t\"options\" : \"-R 1.0E-8 -M -1 -num-decimal-places 4 \"\n" +
                    "\t\t\t\t},\n" +
                    "\t\t\t\t\"loadClassifierFileName\" : \"\",\n" +
                    "\t\t\t\t\"name\" : \"Classifier\",\n" +
                    "\t\t\t\t\"resetIncrementalClassifier\" : false,\n" +
                    "\t\t\t\t\"updateIncrementalClassifier\" : true\n" +
                    "\t\t\t},\n" +
                    "\t\t\t\"connections\" : {\n" +
                    "\t\t\t\t\"batchClassifier\" : [\n" +
                    "\t\t\t\t\t\"ClassifierPerformanceEvaluator\"\n" +
                    "\t\t\t\t]\n" +
                    "\t\t\t},\n" +
                    "\t\t\t\"coordinates\" : \"506,697\"\n" +
                    "\t\t}";
        } else if (classifier.equals("adaboostm1")){
            classifierSpec = "{\n" +
                    "\t\t\t\"class\" : \"weka.knowledgeflow.steps.Classifier\",\n" +
                    "\t\t\t\"properties\" : {\n" +
                    "\t\t\t\t\"classifier\" : {\n" +
                    "\t\t\t\t\t\"type\" : \"optionHandler\",\n" +
                    "\t\t\t\t\t\"class\" : \"weka.classifiers.meta.AdaBoostM1\",\n" +
                    "\t\t\t\t\t\"options\" : \"-P 100 -S " + seed +" -I 10 -W weka.classifiers.trees.DecisionStump\"\n" +
                    "\t\t\t\t},\n" +
                    "\t\t\t\t\"loadClassifierFileName\" : \"\",\n" +
                    "\t\t\t\t\"name\" : \"Classifier\",\n" +
                    "\t\t\t\t\"resetIncrementalClassifier\" : false,\n" +
                    "\t\t\t\t\"updateIncrementalClassifier\" : true\n" +
                    "\t\t\t},\n" +
                    "\t\t\t\"connections\" : {\n" +
                    "\t\t\t\t\"batchClassifier\" : [\n" +
                    "\t\t\t\t\t\"ClassifierPerformanceEvaluator\"\n" +
                    "\t\t\t\t]\n" +
                    "\t\t\t},\n" +
                    "\t\t\t\"coordinates\" : \"486,501\"\n" +
                    "\t\t}";
        } else if (classifier.equals("ibk")){
            classifierSpec = "{\n" +
                    "\t\t\t\"class\" : \"weka.knowledgeflow.steps.Classifier\",\n" +
                    "\t\t\t\"properties\" : {\n" +
                    "\t\t\t\t\"classifier\" : {\n" +
                    "\t\t\t\t\t\"type\" : \"optionHandler\",\n" +
                    "\t\t\t\t\t\"class\" : \"weka.classifiers.lazy.IBk\",\n" +
                    "\t\t\t\t\t\"options\" : \"-K 1 -W 0 -A \\\"weka.core.neighboursearch.LinearNNSearch -A \\\\\\\"weka.core.EuclideanDistance -R first-last\\\\\\\"\\\"\"" +
                    "\t\t\t\t},\n" +
                    "\t\t\t\t\"loadClassifierFileName\" : \"\",\n" +
                    "\t\t\t\t\"name\" : \"Classifier\",\n" +
                    "\t\t\t\t\"resetIncrementalClassifier\" : false,\n" +
                    "\t\t\t\t\"updateIncrementalClassifier\" : true\n" +
                    "\t\t\t},\n" +
                    "\t\t\t\"connections\" : {\n" +
                    "\t\t\t\t\"batchClassifier\" : [\n" +
                    "\t\t\t\t\t\"ClassifierPerformanceEvaluator\"\n" +
                    "\t\t\t\t]\n" +
                    "\t\t\t},\n" +
                    "\t\t\t\"coordinates\" : \"506,697\"\n" +
                    "\t\t}";
        } else if (classifier.equals("stacking")) {
            classifierSpec = "{\n" +
                    "\t\t\t\"class\" : \"weka.knowledgeflow.steps.Classifier\",\n" +
                    "\t\t\t\"properties\" : {\n" +
                    "\t\t\t\t\"classifier\" : {\n" +
                    "\t\t\t\t\t\"type\" : \"optionHandler\",\n" +
                    "\t\t\t\t\t\"class\" : \"weka.classifiers.meta.Stacking\",\n" +
                    "\t\t\t\t\t\"options\" : \"-X 10 -M \\\"weka.classifiers.rules.ZeroR \\\" -S " + seed + " -num-slots 1 -B \\\"weka.classifiers.rules.ZeroR \\\"\"\n" +
                    "\t\t\t\t},\n" +
                    "\t\t\t\t\"loadClassifierFileName\" : \"\",\n" +
                    "\t\t\t\t\"name\" : \"Classifier\",\n" +
                    "\t\t\t\t\"resetIncrementalClassifier\" : false,\n" +
                    "\t\t\t\t\"updateIncrementalClassifier\" : true\n" +
                    "\t\t\t},\n" +
                    "\t\t\t\"connections\" : {\n" +
                    "\t\t\t\t\"batchClassifier\" : [\n" +
                    "\t\t\t\t\t\"ClassifierPerformanceEvaluator\"\n" +
                    "\t\t\t\t]\n" +
                    "\t\t\t},\n" +
                    "\t\t\t\"coordinates\" : \"506,697\"\n" +
                    "\t\t}";
        } else if (classifier.equals("decisiontable")){
            classifierSpec = "{\n" +
                    "\t\t\t\"class\" : \"weka.knowledgeflow.steps.Classifier\",\n" +
                    "\t\t\t\"properties\" : {\n" +
                    "\t\t\t\t\"classifier\" : {\n" +
                    "\t\t\t\t\t\"type\" : \"optionHandler\",\n" +
                    "\t\t\t\t\t\"class\" : \"weka.classifiers.rules.DecisionTable\",\n" +
                    "\t\t\t\t\t\"options\" : \"-X 1 -E auc -S \\\"weka.attributeSelection.BestFirst -D 1 -N 5\\\"\"\n" +
                    "\t\t\t\t},\n" +
                    "\t\t\t\t\"loadClassifierFileName\" : \"\",\n" +
                    "\t\t\t\t\"name\" : \"Classifier\",\n" +
                    "\t\t\t\t\"resetIncrementalClassifier\" : false,\n" +
                    "\t\t\t\t\"updateIncrementalClassifier\" : true\n" +
                    "\t\t\t},\n" +
                    "\t\t\t\"connections\" : {\n" +
                    "\t\t\t\t\"batchClassifier\" : [\n" +
                    "\t\t\t\t\t\"ClassifierPerformanceEvaluator\"\n" +
                    "\t\t\t\t]\n" +
                    "\t\t\t},\n" +
                    "\t\t\t\"coordinates\" : \"506,697\"\n" +
                    "\t\t}";
        } else if (classifier.equals("lwl")){
            classifierSpec = "{\n" +
                    "\t\t\t\"class\" : \"weka.knowledgeflow.steps.Classifier\",\n" +
                    "\t\t\t\"properties\" : {\n" +
                    "\t\t\t\t\"classifier\" : {\n" +
                    "\t\t\t\t\t\"type\" : \"optionHandler\",\n" +
                    "\t\t\t\t\t\"class\" : \"weka.classifiers.lazy.LWL\",\n" +
                    "\t\t\t\t\t\"options\" : \"-U 0 -K -1 -A \\\"weka.core.neighboursearch.LinearNNSearch -A \\\\\\\"weka.core.EuclideanDistance -R first-last\\\\\\\"\\\" -W weka.classifiers.trees.DecisionStump\"\n" +
                    "\t\t\t\t},\n" +
                    "\t\t\t\t\"loadClassifierFileName\" : \"\",\n" +
                    "\t\t\t\t\"name\" : \"Classifier\",\n" +
                    "\t\t\t\t\"resetIncrementalClassifier\" : false,\n" +
                    "\t\t\t\t\"updateIncrementalClassifier\" : true\n" +
                    "\t\t\t},\n" +
                    "\t\t\t\"connections\" : {\n" +
                    "\t\t\t\t\"batchClassifier\" : [\n" +
                    "\t\t\t\t\t\"ClassifierPerformanceEvaluator\"\n" +
                    "\t\t\t\t]\n" +
                    "\t\t\t},\n" +
                    "\t\t\t\"coordinates\" : \"506,697\"\n" +
                    "\t\t}";
        }

        if (attributeSelection.equals("no-attribute-selection")){
            stringBuilder.append("{\n" +
                    "\t\"flow_name\" : \"out.single\",\n" +
                    "\t\"steps\" : [\n" +
                    "\t\t{\n" +
                    "\t\t\t\"class\" : \"weka.knowledgeflow.steps.Loader\",\n" +
                    "\t\t\t\"properties\" : {\n" +
                    "\t\t\t\t\"loader\" : {\n" +
                    "\t\t\t\t\t\"type\" : \"loader\",\n" +
                    "\t\t\t\t\t\"class\" : \"weka.core.converters.ArffLoader\",\n" +
                    "\t\t\t\t\t\"filePath\" : \"" + trainingSet + "\",\n" +
                    "\t\t\t\t\t\"useRelativePath\" : false\n" +
                    "\t\t\t\t},\n" +
                    "\t\t\t\t\"name\" : \"ArffLoader\"\n" +
                    "\t\t\t},\n" +
                    "\t\t\t\"connections\" : {\n" +
                    "\t\t\t\t\"dataSet\" : [\n" +
                    "\t\t\t\t\t\"ClassAssigner\"\n" +
                    "\t\t\t\t]\n" +
                    "\t\t\t},\n" +
                    "\t\t\t\"coordinates\" : \"168,36\"\n" +
                    "\t\t},\n" +
                    "\t\t{\n" +
                    "\t\t\t\"class\" : \"weka.knowledgeflow.steps.ClassAssigner\",\n" +
                    "\t\t\t\"properties\" : {\n" +
                    "\t\t\t\t\"classColumn\" : \"class\",\n" +
                    "\t\t\t\t\"name\" : \"ClassAssigner\"\n" +
                    "\t\t\t},\n" +
                    "\t\t\t\"connections\" : {\n" +
                    "\t\t\t\t\"dataSet\" : [\n" +
                    "\t\t\t\t\t\"TrainingSetMaker\"\n" +
                    "\t\t\t\t]\n" +
                    "\t\t\t},\n" +
                    "\t\t\t\"coordinates\" : \"319,25\"\n" +
                    "\t\t},\n" +
                    "\t\t{\n" +
                    "\t\t\t\"class\" : \"weka.knowledgeflow.steps.TextViewer\",\n" +
                    "\t\t\t\"properties\" : {\n" +
                    "\t\t\t\t\"name\" : \"TextViewer\"\n" +
                    "\t\t\t},\n" +
                    "\t\t\t\"connections\" : {\n" +
                    "\t\t\t},\n" +
                    "\t\t\t\"coordinates\" : \"1135,84\"\n" +
                    "\t\t},\n" +
                    "\t\t{\n" +
                    "\t\t\t\"class\" : \"weka.knowledgeflow.steps.ClassifierPerformanceEvaluator\",\n" +
                    "\t\t\t\"properties\" : {\n" +
                    "\t\t\t\t\"costMatrixString\" : \"\",\n" +
                    "\t\t\t\t\"errorPlotPointSizeProportionalToMargin\" : false,\n" +
                    "\t\t\t\t\"evaluateWithRespectToCosts\" : false,\n" +
                    "\t\t\t\t\"evaluationMetricsToOutput\" : \"Correct,Incorrect,Kappa,Total cost,Average cost,KB relative,KB information,Correlation,Complexity 0,Complexity scheme,Complexity improvement,MAE,RMSE,RAE,RRSE,TP rate,FP rate,Precision,Recall,F-measure,MCC,ROC area,PRC area\",\n" +
                    "\t\t\t\t\"name\" : \"ClassifierPerformanceEvaluator\"\n" +
                    "\t\t\t},\n" +
                    "\t\t\t\"connections\" : {\n" +
                    "\t\t\t\t\"text\" : [\n" +
                    "\t\t\t\t\t\"TextViewer\"\n" +
                    "\t\t\t\t]\n" +
                    "\t\t\t},\n" +
                    "\t\t\t\"coordinates\" : \"969,85\"\n" +
                    "\t\t},\n" +
                    "\t\t{\n" +
                    "\t\t\t\"class\" : \"weka.knowledgeflow.steps.TrainingSetMaker\",\n" +
                    "\t\t\t\"properties\" : {\n" +
                    "\t\t\t\t\"name\" : \"TrainingSetMaker\"\n" +
                    "\t\t\t},\n" +
                    "\t\t\t\"connections\" : {\n" +
                    "\t\t\t\t\"trainingSet\" : [\n" +
                    "\t\t\t\t\t\"Classifier\"\n" +
                    "\t\t\t\t]\n" +
                    "\t\t\t},\n" +
                    "\t\t\t\"coordinates\" : \"476,28\"\n" +
                    "\t\t},\n" +
                    "\t\t{\n" +
                    "\t\t\t\"class\" : \"weka.knowledgeflow.steps.Loader\",\n" +
                    "\t\t\t\"properties\" : {\n" +
                    "\t\t\t\t\"loader\" : {\n" +
                    "\t\t\t\t\t\"type\" : \"loader\",\n" +
                    "\t\t\t\t\t\"class\" : \"weka.core.converters.ArffLoader\",\n" +
                    "\t\t\t\t\t\"filePath\" : \"" + testSet + "\",\n" +
                    "\t\t\t\t\t\"useRelativePath\" : false\n" +
                    "\t\t\t\t},\n" +
                    "\t\t\t\t\"name\" : \"ArffLoader2\"\n" +
                    "\t\t\t},\n" +
                    "\t\t\t\"connections\" : {\n" +
                    "\t\t\t\t\"dataSet\" : [\n" +
                    "\t\t\t\t\t\"ClassAssigner2\"\n" +
                    "\t\t\t\t]\n" +
                    "\t\t\t},\n" +
                    "\t\t\t\"coordinates\" : \"165,144\"\n" +
                    "\t\t},\n" +
                    "\t\t{\n" +
                    "\t\t\t\"class\" : \"weka.knowledgeflow.steps.ClassAssigner\",\n" +
                    "\t\t\t\"properties\" : {\n" +
                    "\t\t\t\t\"classColumn\" : \"/last\",\n" +
                    "\t\t\t\t\"name\" : \"ClassAssigner2\"\n" +
                    "\t\t\t},\n" +
                    "\t\t\t\"connections\" : {\n" +
                    "\t\t\t\t\"dataSet\" : [\n" +
                    "\t\t\t\t\t\"TestSetMaker\"\n" +
                    "\t\t\t\t]\n" +
                    "\t\t\t},\n" +
                    "\t\t\t\"coordinates\" : \"323,152\"\n" +
                    "\t\t},\n" +
                    "\t\t{\n" +
                    "\t\t\t\"class\" : \"weka.knowledgeflow.steps.TestSetMaker\",\n" +
                    "\t\t\t\"properties\" : {\n" +
                    "\t\t\t\t\"name\" : \"TestSetMaker\"\n" +
                    "\t\t\t},\n" +
                    "\t\t\t\"connections\" : {\n" +
                    "\t\t\t\t\"testSet\" : [\n" +
                    "\t\t\t\t\t\"Classifier\"\n" +
                    "\t\t\t\t]\n" +
                    "\t\t\t},\n" +
                    "\t\t\t\"coordinates\" : \"463,145\"\n" +
                    "\t\t},\n" +
                    classifierSpec +
                    "\t]\n" +
                    "}\n");
        } else {
            stringBuilder.append("{\n" +
                    "\t\"flow_name\" : \"tmp2\",\n" +
                    "\t\"steps\" : [\n" +
                    "\t\t{\n" +
                    "\t\t\t\"class\" : \"weka.knowledgeflow.steps.Loader\",\n" +
                    "\t\t\t\"properties\" : {\n" +
                    "\t\t\t\t\"loader\" : {\n" +
                    "\t\t\t\t\t\"type\" : \"loader\",\n" +
                    "\t\t\t\t\t\"class\" : \"weka.core.converters.ArffLoader\",\n" +
                    "\t\t\t\t\t\"filePath\" : \"" + trainingSet + "\",\n" +
                    "\t\t\t\t\t\"useRelativePath\" : false\n" +
                    "\t\t\t\t},\n" +
                    "\t\t\t\t\"name\" : \"ArffLoader\"\n" +
                    "\t\t\t},\n" +
                    "\t\t\t\"connections\" : {\n" +
                    "\t\t\t\t\"dataSet\" : [\n" +
                    "\t\t\t\t\t\"ClassAssigner\"\n" +
                    "\t\t\t\t]\n" +
                    "\t\t\t},\n" +
                    "\t\t\t\"coordinates\" : \"168,36\"\n" +
                    "\t\t},\n" +
                    "\t\t{\n" +
                    "\t\t\t\"class\" : \"weka.knowledgeflow.steps.ClassAssigner\",\n" +
                    "\t\t\t\"properties\" : {\n" +
                    "\t\t\t\t\"classColumn\" : \"class\",\n" +
                    "\t\t\t\t\"name\" : \"ClassAssigner\"\n" +
                    "\t\t\t},\n" +
                    "\t\t\t\"connections\" : {\n" +
                    "\t\t\t\t\"dataSet\" : [\n" +
                    "\t\t\t\t\t\"TrainingSetMaker\"\n" +
                    "\t\t\t\t]\n" +
                    "\t\t\t},\n" +
                    "\t\t\t\"coordinates\" : \"319,25\"\n" +
                    "\t\t},\n" +
                    "\t\t{\n" +
                    "\t\t\t\"class\" : \"weka.knowledgeflow.steps.TextViewer\",\n" +
                    "\t\t\t\"properties\" : {\n" +
                    "\t\t\t\t\"name\" : \"TextViewer\"\n" +
                    "\t\t\t},\n" +
                    "\t\t\t\"connections\" : {\n" +
                    "\t\t\t},\n" +
                    "\t\t\t\"coordinates\" : \"1135,84\"\n" +
                    "\t\t},\n" +
                    "\t\t{\n" +
                    "\t\t\t\"class\" : \"weka.knowledgeflow.steps.ClassifierPerformanceEvaluator\",\n" +
                    "\t\t\t\"properties\" : {\n" +
                    "\t\t\t\t\"costMatrixString\" : \"\",\n" +
                    "\t\t\t\t\"errorPlotPointSizeProportionalToMargin\" : false,\n" +
                    "\t\t\t\t\"evaluateWithRespectToCosts\" : false,\n" +
                    "\t\t\t\t\"evaluationMetricsToOutput\" : \"Correct,Incorrect,Kappa,Total cost,Average cost,KB relative,KB information,Correlation,Complexity 0,Complexity scheme,Complexity improvement,MAE,RMSE,RAE,RRSE,TP rate,FP rate,Precision,Recall,F-measure,MCC,ROC area,PRC area\",\n" +
                    "\t\t\t\t\"name\" : \"ClassifierPerformanceEvaluator\"\n" +
                    "\t\t\t},\n" +
                    "\t\t\t\"connections\" : {\n" +
                    "\t\t\t\t\"text\" : [\n" +
                    "\t\t\t\t\t\"TextViewer\"\n" +
                    "\t\t\t\t]\n" +
                    "\t\t\t},\n" +
                    "\t\t\t\"coordinates\" : \"969,85\"\n" +
                    "\t\t},\n" +
                    "\t\t{\n" +
                    "\t\t\t\"class\" : \"weka.knowledgeflow.steps.TrainingSetMaker\",\n" +
                    "\t\t\t\"properties\" : {\n" +
                    "\t\t\t\t\"name\" : \"TrainingSetMaker\"\n" +
                    "\t\t\t},\n" +
                    "\t\t\t\"connections\" : {\n" +
                    "\t\t\t\t\"trainingSet\" : [\n" +
                    "\t\t\t\t\t\"AttributeSelection\"\n" +
                    "\t\t\t\t]\n" +
                    "\t\t\t},\n" +
                    "\t\t\t\"coordinates\" : \"476,28\"\n" +
                    "\t\t},\n" +
                    "\t\t{\n" +
                    "\t\t\t\"class\" : \"weka.knowledgeflow.steps.Loader\",\n" +
                    "\t\t\t\"properties\" : {\n" +
                    "\t\t\t\t\"loader\" : {\n" +
                    "\t\t\t\t\t\"type\" : \"loader\",\n" +
                    "\t\t\t\t\t\"class\" : \"weka.core.converters.ArffLoader\",\n" +
                    "\t\t\t\t\t\"filePath\" : \"" + testSet + "\",\n" +
                    "\t\t\t\t\t\"useRelativePath\" : false\n" +
                    "\t\t\t\t},\n" +
                    "\t\t\t\t\"name\" : \"ArffLoader2\"\n" +
                    "\t\t\t},\n" +
                    "\t\t\t\"connections\" : {\n" +
                    "\t\t\t\t\"dataSet\" : [\n" +
                    "\t\t\t\t\t\"ClassAssigner2\"\n" +
                    "\t\t\t\t]\n" +
                    "\t\t\t},\n" +
                    "\t\t\t\"coordinates\" : \"165,144\"\n" +
                    "\t\t},\n" +
                    "\t\t{\n" +
                    "\t\t\t\"class\" : \"weka.knowledgeflow.steps.ClassAssigner\",\n" +
                    "\t\t\t\"properties\" : {\n" +
                    "\t\t\t\t\"classColumn\" : \"/last\",\n" +
                    "\t\t\t\t\"name\" : \"ClassAssigner2\"\n" +
                    "\t\t\t},\n" +
                    "\t\t\t\"connections\" : {\n" +
                    "\t\t\t\t\"dataSet\" : [\n" +
                    "\t\t\t\t\t\"TestSetMaker\"\n" +
                    "\t\t\t\t]\n" +
                    "\t\t\t},\n" +
                    "\t\t\t\"coordinates\" : \"323,152\"\n" +
                    "\t\t},\n" +
                    "\t\t{\n" +
                    "\t\t\t\"class\" : \"weka.knowledgeflow.steps.TestSetMaker\",\n" +
                    "\t\t\t\"properties\" : {\n" +
                    "\t\t\t\t\"name\" : \"TestSetMaker\"\n" +
                    "\t\t\t},\n" +
                    "\t\t\t\"connections\" : {\n" +
                    "\t\t\t\t\"testSet\" : [\n" +
                    "\t\t\t\t\t\"AttributeSelection\"\n" +
                    "\t\t\t\t]\n" +
                    "\t\t\t},\n" +
                    "\t\t\t\"coordinates\" : \"463,145\"\n" +
                    "\t\t},\n" +
                    "\t\t{\n" +
                    "\t\t\t\"class\" : \"weka.knowledgeflow.steps.Filter\",\n" +
                    "\t\t\t\"properties\" : {\n" +
                    "\t\t\t\t\"filter\" : {\n" +
                    "\t\t\t\t\t\"type\" : \"optionHandler\",\n" +
                    "\t\t\t\t\t\"class\" : \"weka.filters.supervised.attribute.AttributeSelection\",\n" +
                    "\t\t\t\t\t\"options\" : " + attributeSelectionSpec +
                    "\t\t\t\t},\n" +
                    "\t\t\t\t\"name\" : \"AttributeSelection\"\n" +
                    "\t\t\t},\n" +
                    "\t\t\t\"connections\" : {\n" +
                    "\t\t\t\t\"trainingSet\" : [\n" +
                    "\t\t\t\t\t\"Classifier\"\n" +
                    "\t\t\t\t],\n" +
                    "\t\t\t\t\"testSet\" : [\n" +
                    "\t\t\t\t\t\"Classifier\"\n" +
                    "\t\t\t\t]\n" +
                    "\t\t\t},\n" +
                    "\t\t\t\"coordinates\" : \"692,81\"\n" +
                    "\t\t},\n" +
                    classifierSpec +
                    "\t]\n" +
                    "}\n");
        }


        File f = new File(outFileName);
        String json = stringBuilder.toString();
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

    public File translateForMyEngine(String outFileName,
                          String trainingSet,
                          String testSet,
                          int seed,
                          String attributeSelection,
                          String classifier){
        StringBuilder stringBuilder = new StringBuilder();
        String attributeSelectionSpec = "";
        if (attributeSelection.equals("cfs-subset-eval")){
            attributeSelectionSpec = "\"-E \\\"weka.attributeSelection.CfsSubsetEval -P 1 -E 1\\\" -S " +
                    "\\\"weka.attributeSelection.BestFirst -D 1 -N 5\\\"\"\n";
        } else if (attributeSelection.equals("correlation-attribute-eval")){
            attributeSelectionSpec = "\"-E \\\"weka.attributeSelection.CorrelationAttributeEval \\\" -S " +
                    "\\\"weka.attributeSelection.Ranker -T -1.7976931348623157E308 -N -1\\\"\"\n";
        } else if (attributeSelection.equals("gain-ratio-attribute-eval")){
            attributeSelectionSpec = "\"-E \\\"weka.attributeSelection.GainRatioAttributeEval \\\" -S " +
                    "\\\"weka.attributeSelection.Ranker -T -1.7976931348623157E308 -N -1\\\"\"\n";
        } else if (attributeSelection.equals("info-gain-attribute-eval")){
            attributeSelectionSpec = "\"-E \\\"weka.attributeSelection.InfoGainAttributeEval \\\" -S " +
                    "\\\"weka.attributeSelection.Ranker -T -1.7976931348623157E308 -N -1\\\"\"\n";
        } else if (attributeSelection.equals("relieff-attribute-eval")){
            attributeSelectionSpec = "\"-E \\\"weka.attributeSelection.ReliefFAttributeEval -M -1 -D 1 -K 10\\\" -S " +
                    "\\\"weka.attributeSelection.Ranker -T -1.7976931348623157E308 -N -1\\\"\"\n";
        } else if (attributeSelection.equals("symmetrical-uncert-attribute-eval")){
            attributeSelectionSpec = "\"-E \\\"weka.attributeSelection.SymmetricalUncertAttributeEval \\\" -S " +
                    "\\\"weka.attributeSelection.Ranker -T -1.7976931348623157E308 -N -1\\\"\"\n";
        } else if (attributeSelection.equals("wrapper-subset-eval-with-adaboost-operator")){
            attributeSelectionSpec = "\"-E \\\"weka.attributeSelection.WrapperSubsetEval -B weka.classifiers.meta.AdaBoostM1 -F 5 -T 0.01 -R 1 -E ACC -- -P 100 -S " + seed + " -I 10 -W weka.classifiers.trees.DecisionStump\\\\\" -S " +
                    "\\\"weka.attributeSelection.GreedyStepwise -T -1.7976931348623157E308 -N -1 -num-slots 1\\\"\"\n";
        }

        String classifierSpec = "";
        String classifierClassName = "";
        if (classifier.equals("randomforest")){
            classifierSpec = "\"-E ACC -search \\\"weka.core.setupgenerator.MathParameter -property numFeatures -min 0.0 -max 20.0 -step 4.0 -base 10.0 -expression I\\\" -search \\\"weka.core.setupgenerator.MathParameter -property numIterations -min 2.0 -max 200.0 -step 1.0 -base 10.0 -expression I\\\" -class-label 1 -algorithm \\\"weka.classifiers.meta.multisearch.RandomSearch -sample-size 100.0 -num-folds 2 -test-set . -num-iterations 200 -S 1 -num-slots 5\\\" -log-file /Applications/weka-3-9-1-oracle-jvm.app/Contents/Java -S "+ seed +" -W weka.classifiers.trees.RandomForest -- -P 100 -I 100 -num-slots 1 -K 0 -M 1.0 -V 0.001 -S "+ seed +"\"";
        } else if(classifier.equals("logitboost")){
            classifierSpec = "\"-E ACC -search \\\"weka.core.setupgenerator.MathParameter -property numIterations -min 2.0 -max 200.0 -step 1.0 -base 10.0 -expression I\\\" -class-label 1 -algorithm \\\"weka.classifiers.meta.multisearch.RandomSearch -sample-size 100.0 -num-folds 2 -test-set . -num-iterations 200 -S 1 -num-slots 5\\\" -log-file /Applications/weka-3-9-1-oracle-jvm.app/Contents/Java -S "+ seed +" -W weka.classifiers.meta.LogitBoost -- -P 100 -L -1.7976931348623157E308 -H 1.0 -Z 3.0 -O 1 -E 1 -S "+ seed +" -I 10 -W weka.classifiers.trees.DecisionStump\"";
        } else if (classifier.equals("smo")) {
            classifierSpec = "\"-E ACC -search \\\"weka.core.setupgenerator.ListParameter -property buildCalibrationModels -custom-delimiter , -list true,false\\\" -search \\\"weka.core.setupgenerator.ListParameter -property filterType -custom-delimiter , -list 0,1\\\" -search \\\"weka.core.setupgenerator.MathParameter -property c -min 1.0 -max 5.0 -step 1.0 -base 10.0 -expression I\\\" -class-label 1 -algorithm \\\"weka.classifiers.meta.multisearch.RandomSearch -sample-size 100.0 -num-folds 2 -test-set . -num-iterations 200 -S 1 -num-slots 5\\\" -log-file /Applications/weka-3-9-1-oracle-jvm.app/Contents/Java -S "+ seed +" -W weka.classifiers.functions.SMO -- -C 1.0 -L 0.001 -P 1.0E-12 -N 0 -V -1 -W 1 -K \\\"weka.classifiers.functions.supportVector.PolyKernel -E 1.0 -C 250007\\\" -calibrator \\\"weka.classifiers.functions.Logistic -R 1.0E-8 -M -1 -num-decimal-places 4\\\"\"";
        } else if (classifier.equals("randomsubspace")) {
            classifierSpec = "\"-E ACC -search \\\"weka.core.setupgenerator.MathParameter -property subSpaceSize -min 0.5 -max 1.0 -step 0.1 -base 10.0 -expression I\\\" -search \\\"weka.core.setupgenerator.MathParameter -property numIterations -min 2.0 -max 64.0 -step 2.0 -base 10.0 -expression I\\\" -class-label 1 -algorithm \\\"weka.classifiers.meta.multisearch.RandomSearch -sample-size 100.0 -num-folds 2 -test-set . -num-iterations 200 -S 1 -num-slots 5\\\" -log-file /Applications/weka-3-9-1-oracle-jvm.app/Contents/Java -S "+ seed +" -W weka.classifiers.meta.RandomSubSpace -- -P 0.5 -S 1 -num-slots 1 -I 10 -W weka.classifiers.trees.REPTree -- -M 2 -V 0.001 -N 3 -S "+ seed +" -L -1 -I 0.0\"";
        } else if (classifier.equals("kstar")) {
            classifierSpec = "\"-E ACC -search \\\"weka.core.setupgenerator.MathParameter -property globalBlend -min 10.0 -max 100.0 -step 10.0 -base 10.0 -expression I\\\" -class-label 1 -algorithm \\\"weka.classifiers.meta.multisearch.RandomSearch -sample-size 100.0 -num-folds 2 -test-set . -num-iterations 200 -S 1 -num-slots 5\\\" -log-file /Applications/weka-3-9-1-oracle-jvm.app/Contents/Java -S "+ seed +" -W weka.classifiers.lazy.KStar -- -B 20 -M a\"";
        } else if (classifier.equals("j48")) {
            classifierSpec = "\"-E ACC -search \\\"weka.core.setupgenerator.MathParameter -property minNumObj -min 1.0 -max 64.0 -step 1.0 -base 10.0 -expression I\\\" -search \\\"weka.core.setupgenerator.MathParameter -property confidenceFactor -min 0.1 -max 0.3 -step 0.01 -base 10.0 -expression I\\\" -class-label 1 -algorithm \\\"weka.classifiers.meta.multisearch.RandomSearch -sample-size 100.0 -num-folds 2 -test-set . -num-iterations 200 -S 1 -num-slots 5\\\" -log-file /Applications/weka-3-9-1-oracle-jvm.app/Contents/Java -S "+ seed +" -W weka.classifiers.trees.J48 -- -C 0.25 -M 2 -Q "+ seed +" \"";
        } else if (classifier.equals("bagging")){
            classifierSpec = "\"-E ACC -search \\\"weka.core.setupgenerator.MathParameter -property bagSizePercent -min 10.0 -max 100.0 -step 10.0 -base 10.0 -expression I\\\" -search \\\"weka.core.setupgenerator.MathParameter -property numIterations -min 10.0 -max 100.0 -step 10.0 -base 10.0 -expression I\\\" -class-label 1 -algorithm \\\"weka.classifiers.meta.multisearch.RandomSearch -sample-size 100.0 -num-folds 2 -test-set . -num-iterations 200 -S 1 -num-slots 5\\\" -log-file /Applications/weka-3-9-1-oracle-jvm.app/Contents/Java -S "+ seed +" -W weka.classifiers.meta.Bagging -- -P 100 -S " + seed + " -num-slots 1 -I 10 -W weka.classifiers.trees.REPTree -- -M 2 -V 0.001 -N 3 -S 1 -L -1 -I 0.0\"";
        } else if (classifier.equals("neuralnetwork")) {
            classifierSpec = "\"-E ACC -search \\\"weka.core.setupgenerator.ListParameter -property learningRate -custom-delimiter , -list 0.3\\\" -class-label 1 -algorithm \\\"weka.classifiers.meta.multisearch.RandomSearch -sample-size 100.0 -num-folds 2 -test-set . -num-iterations 200 -S 1 -num-slots 5\\\" -log-file /Applications/weka-3-9-1-oracle-jvm.app/Contents/Java -S "+ seed +" -W weka.classifiers.functions.MultilayerPerceptron -- -L 0.3 -M 0.2 -N 500 -V 0 -S "+ seed +" -E 20 -H a\"";
        } else if (classifier.equals("logistic")) {
            classifierSpec = "\"-E ACC -search \\\"weka.core.setupgenerator.MathParameter -property ridge -min -12.0 -max 1.0 -step 1.0 -base 10.0 -expression pow(BASE,I)\\\" -class-label 1 -algorithm \\\"weka.classifiers.meta.multisearch.RandomSearch -sample-size 100.0 -num-folds 2 -test-set . -num-iterations 200 -S 1 -num-slots 5\\\" -log-file /Applications/weka-3-9-1-oracle-jvm.app/Contents/Java -S "+ seed +" -W weka.classifiers.functions.Logistic -- -R 1.0E-8 -M -1 -num-decimal-places 4\"";
        } else if (classifier.equals("adaboostm1")){
            classifierSpec = "\"-E ACC -search \\\"weka.core.setupgenerator.ListParameter -property useResampling -custom-delimiter , -list true,false\\\" -search \\\"weka.core.setupgenerator.MathParameter -property numIterations -min 10.0 -max 100.0 -step 10.0 -base 10.0 -expression I\\\" -class-label 1 -algorithm \\\"weka.classifiers.meta.multisearch.RandomSearch -sample-size 100.0 -num-folds 2 -test-set . -num-iterations 200 -S 1 -num-slots 5\\\" -log-file /Applications/weka-3-9-1-oracle-jvm.app/Contents/Java -S "+ seed +" -W weka.classifiers.meta.AdaBoostM1 -- -P 100 -S "+ seed + " -I 10 -W weka.classifiers.trees.DecisionStump\"";
        } else if (classifier.equals("ibk")){
            classifierSpec = "\"-E ACC -search \\\"weka.core.setupgenerator.ListParameter -property distanceWeighting -custom-delimiter , -list false,true\\\" -search \\\"weka.core.setupgenerator.MathParameter -property KNN -min 1.0 -max 64.0 -step 1.0 -base 10.0 -expression I\\\" -search \\\"weka.core.setupgenerator.ListParameter -property crossValidate -custom-delimiter , -list false,true\\\" -search \\\"weka.core.setupgenerator.ListParameter -property meanSquared -custom-delimiter , -list true,false\\\" -class-label 1 -algorithm \\\"weka.classifiers.meta.multisearch.RandomSearch -sample-size 100.0 -num-folds 2 -test-set . -num-iterations 200 -S 1 -num-slots 5\\\" -log-file /Applications/weka-3-9-1-oracle-jvm.app/Contents/Java -S "+ seed +" -W weka.classifiers.lazy.IBk -- -K 1 -W 0 -A \\\"weka.core.neighboursearch.LinearNNSearch -A \\\\\\\"weka.core.EuclideanDistance -R first-last\\\\\\\"\\\"\"";
        } else if (classifier.equals("stacking")) {
            classifierSpec = "\"-E ACC -search \\\"weka.core.setupgenerator.ListParameter -property numFolds -custom-delimiter , -list 10\\\" -class-label 1 -algorithm \\\"weka.classifiers.meta.multisearch.RandomSearch -sample-size 100.0 -num-folds 2 -test-set . -num-iterations 200 -S 1 -num-slots 5\\\" -log-file /Applications/weka-3-9-1-oracle-jvm.app/Contents/Java -S "+ seed +" -W weka.classifiers.meta.Stacking -- -X 10 -M \\\"weka.classifiers.rules.ZeroR \\\" -S "+ seed +" -num-slots 1 -B \\\"weka.classifiers.rules.ZeroR \\\"\"";
        } else if (classifier.equals("decisiontable")){
            classifierSpec = "\"-E ACC -search \\\"weka.core.setupgenerator.ListParameter -property useIbk -custom-delimiter , -list true,false\\\" -class-label 1 -algorithm \\\"weka.classifiers.meta.multisearch.RandomSearch -sample-size 100.0 -num-folds 2 -test-set . -num-iterations 200 -S 1 -num-slots 5\\\" -log-file /Applications/weka-3-9-1-oracle-jvm.app/Contents/Java -S 1 -W weka.classifiers.rules.DecisionTable -- -X 1 -S \\\"weka.attributeSelection.BestFirst -D 1 -N 5\\\"\"\n";
        }

        if (attributeSelection.equals("no-attribute-selection")){
            stringBuilder.append("{\n" +
                    "\t\"flow_name\" : \"out.single\",\n" +
                    "\t\"steps\" : [\n" +
                    "\t\t{\n" +
                    "\t\t\t\"class\" : \"weka.knowledgeflow.steps.Loader\",\n" +
                    "\t\t\t\"properties\" : {\n" +
                    "\t\t\t\t\"loader\" : {\n" +
                    "\t\t\t\t\t\"type\" : \"loader\",\n" +
                    "\t\t\t\t\t\"class\" : \"weka.core.converters.ArffLoader\",\n" +
                    "\t\t\t\t\t\"filePath\" : \"" + trainingSet + "\",\n" +
                    "\t\t\t\t\t\"useRelativePath\" : false\n" +
                    "\t\t\t\t},\n" +
                    "\t\t\t\t\"name\" : \"ArffLoader\"\n" +
                    "\t\t\t},\n" +
                    "\t\t\t\"connections\" : {\n" +
                    "\t\t\t\t\"dataSet\" : [\n" +
                    "\t\t\t\t\t\"ClassAssigner\"\n" +
                    "\t\t\t\t]\n" +
                    "\t\t\t},\n" +
                    "\t\t\t\"coordinates\" : \"168,36\"\n" +
                    "\t\t},\n" +
                    "\t\t{\n" +
                    "\t\t\t\"class\" : \"weka.knowledgeflow.steps.ClassAssigner\",\n" +
                    "\t\t\t\"properties\" : {\n" +
                    "\t\t\t\t\"classColumn\" : \"class\",\n" +
                    "\t\t\t\t\"name\" : \"ClassAssigner\"\n" +
                    "\t\t\t},\n" +
                    "\t\t\t\"connections\" : {\n" +
                    "\t\t\t\t\"dataSet\" : [\n" +
                    "\t\t\t\t\t\"TrainingSetMaker\"\n" +
                    "\t\t\t\t]\n" +
                    "\t\t\t},\n" +
                    "\t\t\t\"coordinates\" : \"319,25\"\n" +
                    "\t\t},\n" +
                    "\t\t{\n" +
                    "\t\t\t\"class\" : \"weka.knowledgeflow.steps.TextViewer\",\n" +
                    "\t\t\t\"properties\" : {\n" +
                    "\t\t\t\t\"name\" : \"TextViewer\"\n" +
                    "\t\t\t},\n" +
                    "\t\t\t\"connections\" : {\n" +
                    "\t\t\t},\n" +
                    "\t\t\t\"coordinates\" : \"1135,84\"\n" +
                    "\t\t},\n" +
                    "\t\t{\n" +
                    "\t\t\t\"class\" : \"weka.knowledgeflow.steps.ClassifierPerformanceEvaluator\",\n" +
                    "\t\t\t\"properties\" : {\n" +
                    "\t\t\t\t\"costMatrixString\" : \"\",\n" +
                    "\t\t\t\t\"errorPlotPointSizeProportionalToMargin\" : false,\n" +
                    "\t\t\t\t\"evaluateWithRespectToCosts\" : false,\n" +
                    "\t\t\t\t\"evaluationMetricsToOutput\" : \"Correct,Incorrect,Kappa,Total cost,Average cost,KB relative,KB information,Correlation,Complexity 0,Complexity scheme,Complexity improvement,MAE,RMSE,RAE,RRSE,TP rate,FP rate,Precision,Recall,F-measure,MCC,ROC area,PRC area\",\n" +
                    "\t\t\t\t\"name\" : \"ClassifierPerformanceEvaluator\"\n" +
                    "\t\t\t},\n" +
                    "\t\t\t\"connections\" : {\n" +
                    "\t\t\t\t\"text\" : [\n" +
                    "\t\t\t\t\t\"TextViewer\"\n" +
                    "\t\t\t\t]\n" +
                    "\t\t\t},\n" +
                    "\t\t\t\"coordinates\" : \"969,85\"\n" +
                    "\t\t},\n" +
                    "\t\t{\n" +
                    "\t\t\t\"class\" : \"weka.knowledgeflow.steps.TrainingSetMaker\",\n" +
                    "\t\t\t\"properties\" : {\n" +
                    "\t\t\t\t\"name\" : \"TrainingSetMaker\"\n" +
                    "\t\t\t},\n" +
                    "\t\t\t\"connections\" : {\n" +
                    "\t\t\t\t\"trainingSet\" : [\n" +
                    "\t\t\t\t\t\"MultiSearch\"\n" +
                    "\t\t\t\t]\n" +
                    "\t\t\t},\n" +
                    "\t\t\t\"coordinates\" : \"476,28\"\n" +
                    "\t\t},\n" +
                    "\t\t{\n" +
                    "\t\t\t\"class\" : \"weka.knowledgeflow.steps.Loader\",\n" +
                    "\t\t\t\"properties\" : {\n" +
                    "\t\t\t\t\"loader\" : {\n" +
                    "\t\t\t\t\t\"type\" : \"loader\",\n" +
                    "\t\t\t\t\t\"class\" : \"weka.core.converters.ArffLoader\",\n" +
                    "\t\t\t\t\t\"filePath\" : \"" + testSet + "\",\n" +
                    "\t\t\t\t\t\"useRelativePath\" : false\n" +
                    "\t\t\t\t},\n" +
                    "\t\t\t\t\"name\" : \"ArffLoader2\"\n" +
                    "\t\t\t},\n" +
                    "\t\t\t\"connections\" : {\n" +
                    "\t\t\t\t\"dataSet\" : [\n" +
                    "\t\t\t\t\t\"ClassAssigner2\"\n" +
                    "\t\t\t\t]\n" +
                    "\t\t\t},\n" +
                    "\t\t\t\"coordinates\" : \"165,144\"\n" +
                    "\t\t},\n" +
                    "\t\t{\n" +
                    "\t\t\t\"class\" : \"weka.knowledgeflow.steps.ClassAssigner\",\n" +
                    "\t\t\t\"properties\" : {\n" +
                    "\t\t\t\t\"classColumn\" : \"/last\",\n" +
                    "\t\t\t\t\"name\" : \"ClassAssigner2\"\n" +
                    "\t\t\t},\n" +
                    "\t\t\t\"connections\" : {\n" +
                    "\t\t\t\t\"dataSet\" : [\n" +
                    "\t\t\t\t\t\"TestSetMaker\"\n" +
                    "\t\t\t\t]\n" +
                    "\t\t\t},\n" +
                    "\t\t\t\"coordinates\" : \"323,152\"\n" +
                    "\t\t},\n" +
                    "\t\t{\n" +
                    "\t\t\t\"class\" : \"weka.knowledgeflow.steps.TestSetMaker\",\n" +
                    "\t\t\t\"properties\" : {\n" +
                    "\t\t\t\t\"name\" : \"TestSetMaker\"\n" +
                    "\t\t\t},\n" +
                    "\t\t\t\"connections\" : {\n" +
                    "\t\t\t\t\"testSet\" : [\n" +
                    "\t\t\t\t\t\"MultiSearch\"\n" +
                    "\t\t\t\t]\n" +
                    "\t\t\t},\n" +
                    "\t\t\t\"coordinates\" : \"463,145\"\n" +
                    "\t\t},\n" +
                    "\t\t{\n" +
                    "\t\t\t\"class\" : \"weka.knowledgeflow.steps.Classifier\",\n" +
                    "\t\t\t\"properties\" : {\n" +
                    "\t\t\t\t\"classifier\" : {\n" +
                    "\t\t\t\t\t\"type\" : \"optionHandler\",\n" +
                    "\t\t\t\t\t\"class\" : \"weka.classifiers.meta.MultiSearch\",\n" +
                    "\t\t\t\t\t\"options\" : "+ classifierSpec +
                    "\t\t\t\t},\n" +
                    "\t\t\t\t\"loadClassifierFileName\" : \"\",\n" +
                    "\t\t\t\t\"name\" : \"MultiSearch\",\n" +
                    "\t\t\t\t\"resetIncrementalClassifier\" : false,\n" +
                    "\t\t\t\t\"updateIncrementalClassifier\" : true\n" +
                    "\t\t\t},\n" +
                    "\t\t\t\"connections\" : {\n" +
                    "\t\t\t\t\"batchClassifier\" : [\n" +
                    "\t\t\t\t\t\"ClassifierPerformanceEvaluator\"\n" +
                    "\t\t\t\t]\n" +
                    "\t\t\t},\n" +
                    "\t\t\t\"coordinates\" : \"815,91\"\n" +
                    "\t\t}\n" +
                    "\t]\n" +
                    "}\n");
        } else {
            stringBuilder.append("{\n" +
                    "\t\"flow_name\" : \"tmp2\",\n" +
                    "\t\"steps\" : [\n" +
                    "\t\t{\n" +
                    "\t\t\t\"class\" : \"weka.knowledgeflow.steps.Loader\",\n" +
                    "\t\t\t\"properties\" : {\n" +
                    "\t\t\t\t\"loader\" : {\n" +
                    "\t\t\t\t\t\"type\" : \"loader\",\n" +
                    "\t\t\t\t\t\"class\" : \"weka.core.converters.ArffLoader\",\n" +
                    "\t\t\t\t\t\"filePath\" : \"" + trainingSet + "\",\n" +
                    "\t\t\t\t\t\"useRelativePath\" : false\n" +
                    "\t\t\t\t},\n" +
                    "\t\t\t\t\"name\" : \"ArffLoader\"\n" +
                    "\t\t\t},\n" +
                    "\t\t\t\"connections\" : {\n" +
                    "\t\t\t\t\"dataSet\" : [\n" +
                    "\t\t\t\t\t\"ClassAssigner\"\n" +
                    "\t\t\t\t]\n" +
                    "\t\t\t},\n" +
                    "\t\t\t\"coordinates\" : \"168,36\"\n" +
                    "\t\t},\n" +
                    "\t\t{\n" +
                    "\t\t\t\"class\" : \"weka.knowledgeflow.steps.ClassAssigner\",\n" +
                    "\t\t\t\"properties\" : {\n" +
                    "\t\t\t\t\"classColumn\" : \"class\",\n" +
                    "\t\t\t\t\"name\" : \"ClassAssigner\"\n" +
                    "\t\t\t},\n" +
                    "\t\t\t\"connections\" : {\n" +
                    "\t\t\t\t\"dataSet\" : [\n" +
                    "\t\t\t\t\t\"TrainingSetMaker\"\n" +
                    "\t\t\t\t]\n" +
                    "\t\t\t},\n" +
                    "\t\t\t\"coordinates\" : \"319,25\"\n" +
                    "\t\t},\n" +
                    "\t\t{\n" +
                    "\t\t\t\"class\" : \"weka.knowledgeflow.steps.TextViewer\",\n" +
                    "\t\t\t\"properties\" : {\n" +
                    "\t\t\t\t\"name\" : \"TextViewer\"\n" +
                    "\t\t\t},\n" +
                    "\t\t\t\"connections\" : {\n" +
                    "\t\t\t},\n" +
                    "\t\t\t\"coordinates\" : \"1135,84\"\n" +
                    "\t\t},\n" +
                    "\t\t{\n" +
                    "\t\t\t\"class\" : \"weka.knowledgeflow.steps.ClassifierPerformanceEvaluator\",\n" +
                    "\t\t\t\"properties\" : {\n" +
                    "\t\t\t\t\"costMatrixString\" : \"\",\n" +
                    "\t\t\t\t\"errorPlotPointSizeProportionalToMargin\" : false,\n" +
                    "\t\t\t\t\"evaluateWithRespectToCosts\" : false,\n" +
                    "\t\t\t\t\"evaluationMetricsToOutput\" : \"Correct,Incorrect,Kappa,Total cost,Average cost,KB relative,KB information,Correlation,Complexity 0,Complexity scheme,Complexity improvement,MAE,RMSE,RAE,RRSE,TP rate,FP rate,Precision,Recall,F-measure,MCC,ROC area,PRC area\",\n" +
                    "\t\t\t\t\"name\" : \"ClassifierPerformanceEvaluator\"\n" +
                    "\t\t\t},\n" +
                    "\t\t\t\"connections\" : {\n" +
                    "\t\t\t\t\"text\" : [\n" +
                    "\t\t\t\t\t\"TextViewer\"\n" +
                    "\t\t\t\t]\n" +
                    "\t\t\t},\n" +
                    "\t\t\t\"coordinates\" : \"969,85\"\n" +
                    "\t\t},\n" +
                    "\t\t{\n" +
                    "\t\t\t\"class\" : \"weka.knowledgeflow.steps.TrainingSetMaker\",\n" +
                    "\t\t\t\"properties\" : {\n" +
                    "\t\t\t\t\"name\" : \"TrainingSetMaker\"\n" +
                    "\t\t\t},\n" +
                    "\t\t\t\"connections\" : {\n" +
                    "\t\t\t\t\"trainingSet\" : [\n" +
                    "\t\t\t\t\t\"AttributeSelection\"\n" +
                    "\t\t\t\t]\n" +
                    "\t\t\t},\n" +
                    "\t\t\t\"coordinates\" : \"476,28\"\n" +
                    "\t\t},\n" +
                    "\t\t{\n" +
                    "\t\t\t\"class\" : \"weka.knowledgeflow.steps.Loader\",\n" +
                    "\t\t\t\"properties\" : {\n" +
                    "\t\t\t\t\"loader\" : {\n" +
                    "\t\t\t\t\t\"type\" : \"loader\",\n" +
                    "\t\t\t\t\t\"class\" : \"weka.core.converters.ArffLoader\",\n" +
                    "\t\t\t\t\t\"filePath\" : \"" + testSet + "\",\n" +
                    "\t\t\t\t\t\"useRelativePath\" : false\n" +
                    "\t\t\t\t},\n" +
                    "\t\t\t\t\"name\" : \"ArffLoader2\"\n" +
                    "\t\t\t},\n" +
                    "\t\t\t\"connections\" : {\n" +
                    "\t\t\t\t\"dataSet\" : [\n" +
                    "\t\t\t\t\t\"ClassAssigner2\"\n" +
                    "\t\t\t\t]\n" +
                    "\t\t\t},\n" +
                    "\t\t\t\"coordinates\" : \"165,144\"\n" +
                    "\t\t},\n" +
                    "\t\t{\n" +
                    "\t\t\t\"class\" : \"weka.knowledgeflow.steps.ClassAssigner\",\n" +
                    "\t\t\t\"properties\" : {\n" +
                    "\t\t\t\t\"classColumn\" : \"/last\",\n" +
                    "\t\t\t\t\"name\" : \"ClassAssigner2\"\n" +
                    "\t\t\t},\n" +
                    "\t\t\t\"connections\" : {\n" +
                    "\t\t\t\t\"dataSet\" : [\n" +
                    "\t\t\t\t\t\"TestSetMaker\"\n" +
                    "\t\t\t\t]\n" +
                    "\t\t\t},\n" +
                    "\t\t\t\"coordinates\" : \"323,152\"\n" +
                    "\t\t},\n" +
                    "\t\t{\n" +
                    "\t\t\t\"class\" : \"weka.knowledgeflow.steps.TestSetMaker\",\n" +
                    "\t\t\t\"properties\" : {\n" +
                    "\t\t\t\t\"name\" : \"TestSetMaker\"\n" +
                    "\t\t\t},\n" +
                    "\t\t\t\"connections\" : {\n" +
                    "\t\t\t\t\"testSet\" : [\n" +
                    "\t\t\t\t\t\"AttributeSelection\"\n" +
                    "\t\t\t\t]\n" +
                    "\t\t\t},\n" +
                    "\t\t\t\"coordinates\" : \"463,145\"\n" +
                    "\t\t},\n" +
                    "\t\t{\n" +
                    "\t\t\t\"class\" : \"weka.knowledgeflow.steps.Filter\",\n" +
                    "\t\t\t\"properties\" : {\n" +
                    "\t\t\t\t\"filter\" : {\n" +
                    "\t\t\t\t\t\"type\" : \"optionHandler\",\n" +
                    "\t\t\t\t\t\"class\" : \"weka.filters.supervised.attribute.AttributeSelection\",\n" +
                    "\t\t\t\t\t\"options\" : " + attributeSelectionSpec +
                    "\t\t\t\t},\n" +
                    "\t\t\t\t\"name\" : \"AttributeSelection\"\n" +
                    "\t\t\t},\n" +
                    "\t\t\t\"connections\" : {\n" +
                    "\t\t\t\t\"trainingSet\" : [\n" +
                    "\t\t\t\t\t\"MultiSearch\"\n" +
                    "\t\t\t\t],\n" +
                    "\t\t\t\t\"testSet\" : [\n" +
                    "\t\t\t\t\t\"MultiSearch\"\n" +
                    "\t\t\t\t]\n" +
                    "\t\t\t},\n" +
                    "\t\t\t\"coordinates\" : \"692,81\"\n" +
                    "\t\t},\n" +
                    "\t\t{\n" +
                    "\t\t\t\"class\" : \"weka.knowledgeflow.steps.Classifier\",\n" +
                    "\t\t\t\"properties\" : {\n" +
                    "\t\t\t\t\"classifier\" : {\n" +
                    "\t\t\t\t\t\"type\" : \"optionHandler\",\n" +
                    "\t\t\t\t\t\"class\" : \"weka.classifiers.meta.MultiSearch\",\n" +
                    "\t\t\t\t\t\"options\" : "+ classifierSpec +
                    "\t\t\t\t},\n" +
                    "\t\t\t\t\"loadClassifierFileName\" : \"\",\n" +
                    "\t\t\t\t\"name\" : \"MultiSearch\",\n" +
                    "\t\t\t\t\"resetIncrementalClassifier\" : false,\n" +
                    "\t\t\t\t\"updateIncrementalClassifier\" : true\n" +
                    "\t\t\t},\n" +
                    "\t\t\t\"connections\" : {\n" +
                    "\t\t\t\t\"batchClassifier\" : [\n" +
                    "\t\t\t\t\t\"ClassifierPerformanceEvaluator\"\n" +
                    "\t\t\t\t]\n" +
                    "\t\t\t},\n" +
                    "\t\t\t\"coordinates\" : \"815,91\"\n" +
                    "\t\t}\n" +
                    "\t]\n" +
                    "}\n");
        }


        File f = new File(outFileName);
        String json = stringBuilder.toString();
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
}
