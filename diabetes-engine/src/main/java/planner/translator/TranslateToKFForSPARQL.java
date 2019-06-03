package planner.translator;

import common.ProjectPropertiesGetter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class TranslateToKFForSPARQL {
    public File generateTrainingProcess(String outFileName,
                                        String trainingSet,
                                        int seed,
                                        String attributeSelection,
                                        String classifier){

        String testFileNamePlaceholder = "<Test_File_Name>";

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
                    "\t\t\t\t\t\"PredictionAppender\"\n" +
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
                    "\t\t\t\t\t\"PredictionAppender\"\n" +
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
                    "\t\t\t\t\t\"PredictionAppender\"\n" +
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
                    "\t\t\t\t\t\"PredictionAppender\"\n" +
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
                    "\t\t\t\t\t\"PredictionAppender\"\n" +
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
                    "\t\t\t\t\t\"PredictionAppender\"\n" +
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
                    "\t\t\t\t\t\"PredictionAppender\"\n" +
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
                    "\t\t\t\t\t\"PredictionAppender\"\n" +
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
                    "\t\t\t\t\t\"PredictionAppender\"\n" +
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
                    "\t\t\t\t\t\"PredictionAppender\"\n" +
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
                    "\t\t\t\t\t\"PredictionAppender\"\n" +
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
                    "\t\t\t\t\t\"PredictionAppender\"\n" +
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
                    "\t\t\t\t\t\"PredictionAppender\"\n" +
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
                    "\t\t\t\t\t\"PredictionAppender\"\n" +
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
                    "\t\t\t\t\"classColumn\" : \"last\",\n" +
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
                    "\t\t\t\"class\" : \"weka.knowledgeflow.steps.PredictionAppender\",\n" +
                    "\t\t\t\"properties\" : {\n" +
                    "\t\t\t\t\"appendProbabilities\" : true,\n" +
                    "\t\t\t\t\"name\" : \"PredictionAppender\"\n" +
                    "\t\t\t},\n" +
                    "\t\t\t\"connections\" : {\n" +
                    "\t\t\t\t\"testSet\" : [\n" +
                    "\t\t\t\t\t\"TextViewer\"\n" +
                    "\t\t\t\t]\n" +
                    "\t\t\t},\n" +
                    "\t\t\t\"coordinates\" : \"945,215\"\n" +
                    "\t\t}," +
                    "\t\t{\n" +
                    "\t\t\t\"class\" : \"weka.knowledgeflow.steps.TextViewer\",\n" +
                    "\t\t\t\"properties\" : {\n" +
                    "\t\t\t\t\"name\" : \"TextViewer\"\n" +
                    "\t\t\t},\n" +
                    "\t\t\t\"connections\" : {\n" +
                    "\t\t\t},\n" +
                    "\t\t\t\"coordinates\" : \"1098,216\"\n" +
                    "\t\t}," +
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
                    "{\n" +
                    "\t\t\t\"class\" : \"weka.knowledgeflow.steps.Loader\",\n" +
                    "\t\t\t\"properties\" : {\n" +
                    "\t\t\t\t\"loader\" : {\n" +
                    "\t\t\t\t\t\"type\" : \"loader\",\n" +
                    "\t\t\t\t\t\"class\" : \"weka.core.converters.ArffLoader\",\n" +
                    "\t\t\t\t\t\"filePath\" : \"" + testFileNamePlaceholder + "\",\n" +
                    "\t\t\t\t\t\"useRelativePath\" : false\n" +
                    "\t\t\t\t},\n" +
                    "\t\t\t\t\"name\" : \"ArffLoader2\"\n" +
                    "\t\t\t},\n" +
                    "\t\t\t\"connections\" : {\n" +
                    "\t\t\t\t\"dataSet\" : [\n" +
                    "\t\t\t\t\t\"ClassAssigner2\"\n" +
                    "\t\t\t\t]\n" +
                    "\t\t\t},\n" +
                    "\t\t\t\"coordinates\" : \"80,314\"\n" +
                    "\t\t},\n" +
                    "\t\t{\n" +
                    "\t\t\t\"class\" : \"weka.knowledgeflow.steps.Filter\",\n" +
                    "\t\t\t\"properties\" : {\n" +
                    "\t\t\t\t\"filter\" : {\n" +
                    "\t\t\t\t\t\"type\" : \"optionHandler\",\n" +
                    "\t\t\t\t\t\"class\" : \"weka.filters.unsupervised.attribute.ClassAssigner\",\n" +
                    "\t\t\t\t\t\"options\" : \"-C last\"\n" +
                    "\t\t\t\t},\n" +
                    "\t\t\t\t\"name\" : \"ClassAssigner2\"\n" +
                    "\t\t\t},\n" +
                    "\t\t\t\"connections\" : {\n" +
                    "\t\t\t\t\"dataSet\" : [\n" +
                    "\t\t\t\t\t\"TestSetMaker\"\n" +
                    "\t\t\t\t]\n" +
                    "\t\t\t},\n" +
                    "\t\t\t\"coordinates\" : \"257,302\"\n" +
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
                    "\t\t\t\"coordinates\" : \"417,318\"\n" +
                    "\t\t}," +
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
                    "\t\t\t\t\"classColumn\" : \"last\",\n" +
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
                    "\t\t\t\"class\" : \"weka.knowledgeflow.steps.PredictionAppender\",\n" +
                    "\t\t\t\"properties\" : {\n" +
                    "\t\t\t\t\"appendProbabilities\" : true,\n" +
                    "\t\t\t\t\"name\" : \"PredictionAppender\"\n" +
                    "\t\t\t},\n" +
                    "\t\t\t\"connections\" : {\n" +
                    "\t\t\t\t\"testSet\" : [\n" +
                    "\t\t\t\t\t\"TextViewer\"\n" +
                    "\t\t\t\t]\n" +
                    "\t\t\t},\n" +
                    "\t\t\t\"coordinates\" : \"945,215\"\n" +
                    "\t\t}," +
                    "\t\t{\n" +
                    "\t\t\t\"class\" : \"weka.knowledgeflow.steps.TextViewer\",\n" +
                    "\t\t\t\"properties\" : {\n" +
                    "\t\t\t\t\"name\" : \"TextViewer\"\n" +
                    "\t\t\t},\n" +
                    "\t\t\t\"connections\" : {\n" +
                    "\t\t\t},\n" +
                    "\t\t\t\"coordinates\" : \"1098,216\"\n" +
                    "\t\t}," +
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
                    "{\n" +
                    "\t\t\t\"class\" : \"weka.knowledgeflow.steps.Loader\",\n" +
                    "\t\t\t\"properties\" : {\n" +
                    "\t\t\t\t\"loader\" : {\n" +
                    "\t\t\t\t\t\"type\" : \"loader\",\n" +
                    "\t\t\t\t\t\"class\" : \"weka.core.converters.ArffLoader\",\n" +
                    "\t\t\t\t\t\"filePath\" : \"" + testFileNamePlaceholder + "\",\n" +
                    "\t\t\t\t\t\"useRelativePath\" : false\n" +
                    "\t\t\t\t},\n" +
                    "\t\t\t\t\"name\" : \"ArffLoader2\"\n" +
                    "\t\t\t},\n" +
                    "\t\t\t\"connections\" : {\n" +
                    "\t\t\t\t\"dataSet\" : [\n" +
                    "\t\t\t\t\t\"ClassAssigner2\"\n" +
                    "\t\t\t\t]\n" +
                    "\t\t\t},\n" +
                    "\t\t\t\"coordinates\" : \"80,314\"\n" +
                    "\t\t},\n" +
                    "\t\t{\n" +
                    "\t\t\t\"class\" : \"weka.knowledgeflow.steps.Filter\",\n" +
                    "\t\t\t\"properties\" : {\n" +
                    "\t\t\t\t\"filter\" : {\n" +
                    "\t\t\t\t\t\"type\" : \"optionHandler\",\n" +
                    "\t\t\t\t\t\"class\" : \"weka.filters.unsupervised.attribute.ClassAssigner\",\n" +
                    "\t\t\t\t\t\"options\" : \"-C last\"\n" +
                    "\t\t\t\t},\n" +
                    "\t\t\t\t\"name\" : \"ClassAssigner2\"\n" +
                    "\t\t\t},\n" +
                    "\t\t\t\"connections\" : {\n" +
                    "\t\t\t\t\"dataSet\" : [\n" +
                    "\t\t\t\t\t\"TestSetMaker\"\n" +
                    "\t\t\t\t]\n" +
                    "\t\t\t},\n" +
                    "\t\t\t\"coordinates\" : \"257,302\"\n" +
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
                    "\t\t\t\"coordinates\" : \"417,318\"\n" +
                    "\t\t}," +
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
                    "\"connections\" : {\n" +
                    "\t\t\t\t\"trainingSet\" : [\n" +
                    "\t\t\t\t\t\"Classifier\"\n" +
                    "\t\t\t\t],\n" +
                    "\t\t\t\t\"testSet\" : [\n" +
                    "\t\t\t\t\t\"Classifier\"\n" +
                    "\t\t\t\t]\n" +
                    "\t\t\t}," +
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

    public File preparePredictingProcess(String modelName,
                                         int seed,
                                         String attributeSelection,
                                         String classifier){
        ProjectPropertiesGetter propGetter = ProjectPropertiesGetter.getSingleton();
        String modelFilePath = propGetter.getProperty("sparqlml.dm.model.filepath");
        String modelFileNamePrefix = propGetter.getProperty("sparqlml.dm.model.filename.prefix");
        String predictingProcessFP = propGetter.getProperty("sparqlml.dm.predicting.process.filepath");
        String processFileNamePrefix = propGetter.getProperty("sparqlml.dm.process.filename.prefix");
        String modelFilename = modelFilePath + modelName;
        String processFileName = predictingProcessFP + processFileNamePrefix + "_" + modelName + ".kf";
        String testFileNamePlaceholder = "<Test_File_Name>";

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

        if (classifier.equals("randomforest")){
            modelFilename += "_1_1_" + "RandomForest.model";
            classifierSpec = "{\n" +
                    "\t\t\t\"class\" : \"weka.knowledgeflow.steps.Classifier\",\n" +
                    "\t\t\t\"properties\" : {\n" +
                    "\t\t\t\t\"classifier\" : {\n" +
                    "\t\t\t\t\t\"type\" : \"optionHandler\",\n" +
                    "\t\t\t\t\t\"class\" : \"weka.classifiers.trees.RandomForest\",\n" +
                    "\t\t\t\t\t\"options\" : \"-P 100 -I 100 -num-slots 1 -K 0 -M 1.0 -V 0.001 -S " + seed + "\"\n" +
                    "\t\t\t\t},\n" +
                    "\t\t\t\t\"loadClassifierFileName\" : \"" + modelFilename + "\",\n" +
                    "\t\t\t\t\"name\" : \"Classifier\",\n" +
                    "\t\t\t\t\"resetIncrementalClassifier\" : false,\n" +
                    "\t\t\t\t\"updateIncrementalClassifier\" : true\n" +
                    "\t\t\t},\n" +
                    "\t\t\t\"connections\" : {\n" +
                    "\t\t\t\t\"batchClassifier\" : [\n" +
                    "\t\t\t\t\t\"PredictionAppender\"\n" +
                    "\t\t\t\t]\n" +
                    "\t\t\t},\n" +
                    "\t\t\t\"coordinates\" : \"486,501\"\n" +
                    "\t\t}";
        } else if(classifier.equals("logitboost")){
            modelFilename += "_1_1_" + "LogitBoost.model";
            classifierSpec = "{\n" +
                    "\t\t\t\"class\" : \"weka.knowledgeflow.steps.Classifier\",\n" +
                    "\t\t\t\"properties\" : {\n" +
                    "\t\t\t\t\"classifier\" : {\n" +
                    "\t\t\t\t\t\"type\" : \"optionHandler\",\n" +
                    "\t\t\t\t\t\"class\" : \"weka.classifiers.meta.LogitBoost\",\n" +
                    "\t\t\t\t\t\"options\" : \"-P 100 -L -1.7976931348623157E308 -H 1.0 -Z 3.0 -O 1 -E 1 -S " + seed + " -I 10 -W weka.classifiers.trees.DecisionStump\"\n" +
                    "\t\t\t\t},\n" +
                    "\t\t\t\t\"loadClassifierFileName\" : \"" + modelFilename + "\",\n" +
                    "\t\t\t\t\"name\" : \"Classifier\",\n" +
                    "\t\t\t\t\"resetIncrementalClassifier\" : false,\n" +
                    "\t\t\t\t\"updateIncrementalClassifier\" : true\n" +
                    "\t\t\t},\n" +
                    "\t\t\t\"connections\" : {\n" +
                    "\t\t\t\t\"batchClassifier\" : [\n" +
                    "\t\t\t\t\t\"PredictionAppender\"\n" +
                    "\t\t\t\t]\n" +
                    "\t\t\t},\n" +
                    "\t\t\t\"coordinates\" : \"506,697\"\n" +
                    "\t\t}";
        } else if (classifier.equals("smo")) {
            modelFilename += "_1_1_" + "SMO.model";
            classifierSpec = "{\n" +
                    "\t\t\t\"class\" : \"weka.knowledgeflow.steps.Classifier\",\n" +
                    "\t\t\t\"properties\" : {\n" +
                    "\t\t\t\t\"classifier\" : {\n" +
                    "\t\t\t\t\t\"type\" : \"optionHandler\",\n" +
                    "\t\t\t\t\t\"class\" : \"weka.classifiers.functions.SMO\",\n" +
                    "\t\t\t\t\t\"options\" : \"-C 1.0 -L 0.001 -P 1.0E-12 -N 0 -V -1 -W " + seed + " -K \\\"weka.classifiers.functions.supportVector.PolyKernel -E 1.0 -C 250007\\\" -calibrator \\\"weka.classifiers.functions.Logistic -R 1.0E-8 -M -1 -num-decimal-places 4\\\"\"\n" +
                    "\t\t\t\t},\n" +
                    "\t\t\t\t\"loadClassifierFileName\" : \"" + modelFilename + "\",\n" +
                    "\t\t\t\t\"name\" : \"Classifier\",\n" +
                    "\t\t\t\t\"resetIncrementalClassifier\" : false,\n" +
                    "\t\t\t\t\"updateIncrementalClassifier\" : true\n" +
                    "\t\t\t},\n" +
                    "\t\t\t\"connections\" : {\n" +
                    "\t\t\t\t\"batchClassifier\" : [\n" +
                    "\t\t\t\t\t\"PredictionAppender\"\n" +
                    "\t\t\t\t]\n" +
                    "\t\t\t},\n" +
                    "\t\t\t\"coordinates\" : \"506,697\"\n" +
                    "\t\t}";
        } else if (classifier.equals("randomsubspace")) {
            modelFilename += "_1_1_" + "RandomSubSpace.model";
            classifierSpec = "{\n" +
                    "\t\t\t\"class\" : \"weka.knowledgeflow.steps.Classifier\",\n" +
                    "\t\t\t\"properties\" : {\n" +
                    "\t\t\t\t\"classifier\" : {\n" +
                    "\t\t\t\t\t\"type\" : \"optionHandler\",\n" +
                    "\t\t\t\t\t\"class\" : \"weka.classifiers.meta.RandomSubSpace\",\n" +
                    "\t\t\t\t\t\"options\" : \"-P 0.5 -S " + seed + " -num-slots 1 -I 10 -W weka.classifiers.trees.REPTree -- -M 2 -V 0.001 -N 3 -S " + seed + " -L -1 -I 0.0\"\n" +
                    "\t\t\t\t},\n" +
                    "\t\t\t\t\"loadClassifierFileName\" : \"" + modelFilename + "\",\n" +
                    "\t\t\t\t\"name\" : \"Classifier\",\n" +
                    "\t\t\t\t\"resetIncrementalClassifier\" : false,\n" +
                    "\t\t\t\t\"updateIncrementalClassifier\" : true\n" +
                    "\t\t\t},\n" +
                    "\t\t\t\"connections\" : {\n" +
                    "\t\t\t\t\"batchClassifier\" : [\n" +
                    "\t\t\t\t\t\"PredictionAppender\"\n" +
                    "\t\t\t\t]\n" +
                    "\t\t\t},\n" +
                    "\t\t\t\"coordinates\" : \"506,697\"\n" +
                    "\t\t}";
        } else if (classifier.equals("kstar")) {
            modelFilename += "_1_1_" + "KStar.model";
            classifierSpec = "{\n" +
                    "\t\t\t\"class\" : \"weka.knowledgeflow.steps.Classifier\",\n" +
                    "\t\t\t\"properties\" : {\n" +
                    "\t\t\t\t\"classifier\" : {\n" +
                    "\t\t\t\t\t\"type\" : \"optionHandler\",\n" +
                    "\t\t\t\t\t\"class\" : \"weka.classifiers.lazy.KStar\",\n" +
                    "\t\t\t\t\t\"options\" : \"-B 20 -M a\"\n" +
                    "\t\t\t\t},\n" +
                    "\t\t\t\t\"loadClassifierFileName\" : \"" + modelFilename + "\",\n" +
                    "\t\t\t\t\"name\" : \"Classifier\",\n" +
                    "\t\t\t\t\"resetIncrementalClassifier\" : false,\n" +
                    "\t\t\t\t\"updateIncrementalClassifier\" : true\n" +
                    "\t\t\t},\n" +
                    "\t\t\t\"connections\" : {\n" +
                    "\t\t\t\t\"batchClassifier\" : [\n" +
                    "\t\t\t\t\t\"PredictionAppender\"\n" +
                    "\t\t\t\t]\n" +
                    "\t\t\t},\n" +
                    "\t\t\t\"coordinates\" : \"506,697\"\n" +
                    "\t\t}";
        } else if (classifier.equals("j48")) {
            modelFilename += "_1_1_" + "J48.model";
            classifierSpec = "{\n" +
                    "\t\t\t\"class\" : \"weka.knowledgeflow.steps.Classifier\",\n" +
                    "\t\t\t\"properties\" : {\n" +
                    "\t\t\t\t\"classifier\" : {\n" +
                    "\t\t\t\t\t\"type\" : \"optionHandler\",\n" +
                    "\t\t\t\t\t\"class\" : \"weka.classifiers.trees.J48\",\n" +
                    "\t\t\t\t\t\"options\" : \"-C 0.25 -M 2 -Q " + seed +"\"\n" +
                    "\t\t\t\t},\n" +
                    "\t\t\t\t\"loadClassifierFileName\" : \"" + modelFilename + "\",\n" +
                    "\t\t\t\t\"name\" : \"Classifier\",\n" +
                    "\t\t\t\t\"resetIncrementalClassifier\" : false,\n" +
                    "\t\t\t\t\"updateIncrementalClassifier\" : true\n" +
                    "\t\t\t},\n" +
                    "\t\t\t\"connections\" : {\n" +
                    "\t\t\t\t\"batchClassifier\" : [\n" +
                    "\t\t\t\t\t\"PredictionAppender\"\n" +
                    "\t\t\t\t]\n" +
                    "\t\t\t},\n" +
                    "\t\t\t\"coordinates\" : \"506,697\"\n" +
                    "\t\t}";
        } else if (classifier.equals("bagging")){
            modelFilename += "_1_1_" + "Bagging.model";
            classifierSpec = "{\n" +
                    "\t\t\t\"class\" : \"weka.knowledgeflow.steps.Classifier\",\n" +
                    "\t\t\t\"properties\" : {\n" +
                    "\t\t\t\t\"classifier\" : {\n" +
                    "\t\t\t\t\t\"type\" : \"optionHandler\",\n" +
                    "\t\t\t\t\t\"class\" : \"weka.classifiers.meta.Bagging\",\n" +
                    "\t\t\t\t\t\"options\" : \"-P 100 -S " + seed +" -num-slots 1 -I 10 -W weka.classifiers.trees.REPTree -- -M 2 -V 0.001 -N 3 -S 1 -L -1 -I 0.0\"\n" +
                    "\t\t\t\t},\n" +
                    "\t\t\t\t\"loadClassifierFileName\" : \"" + modelFilename + "\",\n" +
                    "\t\t\t\t\"name\" : \"Classifier\",\n" +
                    "\t\t\t\t\"resetIncrementalClassifier\" : false,\n" +
                    "\t\t\t\t\"updateIncrementalClassifier\" : true\n" +
                    "\t\t\t},\n" +
                    "\t\t\t\"connections\" : {\n" +
                    "\t\t\t\t\"batchClassifier\" : [\n" +
                    "\t\t\t\t\t\"PredictionAppender\"\n" +
                    "\t\t\t\t]\n" +
                    "\t\t\t},\n" +
                    "\t\t\t\"coordinates\" : \"506,697\"\n" +
                    "\t\t}";
        } else if (classifier.equals("neuralnetwork")) {
            modelFilename += "_1_1_" + "MultilayerPerceptron.model";
            classifierSpec = "{\n" +
                    "\t\t\t\"class\" : \"weka.knowledgeflow.steps.Classifier\",\n" +
                    "\t\t\t\"properties\" : {\n" +
                    "\t\t\t\t\"classifier\" : {\n" +
                    "\t\t\t\t\t\"type\" : \"optionHandler\",\n" +
                    "\t\t\t\t\t\"class\" : \"weka.classifiers.functions.MultilayerPerceptron\",\n" +
                    "\t\t\t\t\t\"options\" : \"-L 0.3 -M 0.2 -N 500 -V 0 -S " +  seed + " -E 20 -H a\"\n" +
                    "\t\t\t\t},\n" +
                    "\t\t\t\t\"loadClassifierFileName\" : \"" + modelFilename + "\",\n" +
                    "\t\t\t\t\"name\" : \"Classifier\",\n" +
                    "\t\t\t\t\"resetIncrementalClassifier\" : false,\n" +
                    "\t\t\t\t\"updateIncrementalClassifier\" : true\n" +
                    "\t\t\t},\n" +
                    "\t\t\t\"connections\" : {\n" +
                    "\t\t\t\t\"batchClassifier\" : [\n" +
                    "\t\t\t\t\t\"PredictionAppender\"\n" +
                    "\t\t\t\t]\n" +
                    "\t\t\t},\n" +
                    "\t\t\t\"coordinates\" : \"486,501\"\n" +
                    "\t\t}";
        } else if (classifier.equals("logistic")) {
            modelFilename += "_1_1_" + "Logistic.model";
            classifierSpec = "{\n" +
                    "\t\t\t\"class\" : \"weka.knowledgeflow.steps.Classifier\",\n" +
                    "\t\t\t\"properties\" : {\n" +
                    "\t\t\t\t\"classifier\" : {\n" +
                    "\t\t\t\t\t\"type\" : \"optionHandler\",\n" +
                    "\t\t\t\t\t\"class\" : \"weka.classifiers.functions.Logistic\",\n" +
                    "\t\t\t\t\t\"options\" : \"-R 1.0E-8 -M -1 -num-decimal-places 4 \"\n" +
                    "\t\t\t\t},\n" +
                    "\t\t\t\t\"loadClassifierFileName\" : \"" + modelFilename + "\",\n" +
                    "\t\t\t\t\"name\" : \"Classifier\",\n" +
                    "\t\t\t\t\"resetIncrementalClassifier\" : false,\n" +
                    "\t\t\t\t\"updateIncrementalClassifier\" : true\n" +
                    "\t\t\t},\n" +
                    "\t\t\t\"connections\" : {\n" +
                    "\t\t\t\t\"batchClassifier\" : [\n" +
                    "\t\t\t\t\t\"PredictionAppender\"\n" +
                    "\t\t\t\t]\n" +
                    "\t\t\t},\n" +
                    "\t\t\t\"coordinates\" : \"506,697\"\n" +
                    "\t\t}";
        } else if (classifier.equals("adaboostm1")){
            modelFilename += "_1_1_" + "AdaBoostM1.model";
            classifierSpec = "{\n" +
                    "\t\t\t\"class\" : \"weka.knowledgeflow.steps.Classifier\",\n" +
                    "\t\t\t\"properties\" : {\n" +
                    "\t\t\t\t\"classifier\" : {\n" +
                    "\t\t\t\t\t\"type\" : \"optionHandler\",\n" +
                    "\t\t\t\t\t\"class\" : \"weka.classifiers.meta.AdaBoostM1\",\n" +
                    "\t\t\t\t\t\"options\" : \"-P 100 -S " + seed +" -I 10 -W weka.classifiers.trees.DecisionStump\"\n" +
                    "\t\t\t\t},\n" +
                    "\t\t\t\t\"loadClassifierFileName\" : \"" + modelFilename + "\",\n" +
                    "\t\t\t\t\"name\" : \"Classifier\",\n" +
                    "\t\t\t\t\"resetIncrementalClassifier\" : false,\n" +
                    "\t\t\t\t\"updateIncrementalClassifier\" : true\n" +
                    "\t\t\t},\n" +
                    "\t\t\t\"connections\" : {\n" +
                    "\t\t\t\t\"batchClassifier\" : [\n" +
                    "\t\t\t\t\t\"PredictionAppender\"\n" +
                    "\t\t\t\t]\n" +
                    "\t\t\t},\n" +
                    "\t\t\t\"coordinates\" : \"486,501\"\n" +
                    "\t\t}";
        } else if (classifier.equals("ibk")){
            modelFilename += "_1_1_" + "IBk.model";
            classifierSpec = "{\n" +
                    "\t\t\t\"class\" : \"weka.knowledgeflow.steps.Classifier\",\n" +
                    "\t\t\t\"properties\" : {\n" +
                    "\t\t\t\t\"classifier\" : {\n" +
                    "\t\t\t\t\t\"type\" : \"optionHandler\",\n" +
                    "\t\t\t\t\t\"class\" : \"weka.classifiers.lazy.IBk\",\n" +
                    "\t\t\t\t\t\"options\" : \"-K 1 -W 0 -A \\\"weka.core.neighboursearch.LinearNNSearch -A \\\\\\\"weka.core.EuclideanDistance -R first-last\\\\\\\"\\\"\"" +
                    "\t\t\t\t},\n" +
                    "\t\t\t\t\"loadClassifierFileName\" : \"" + modelFilename + "\",\n" +
                    "\t\t\t\t\"name\" : \"Classifier\",\n" +
                    "\t\t\t\t\"resetIncrementalClassifier\" : false,\n" +
                    "\t\t\t\t\"updateIncrementalClassifier\" : true\n" +
                    "\t\t\t},\n" +
                    "\t\t\t\"connections\" : {\n" +
                    "\t\t\t\t\"batchClassifier\" : [\n" +
                    "\t\t\t\t\t\"PredictionAppender\"\n" +
                    "\t\t\t\t]\n" +
                    "\t\t\t},\n" +
                    "\t\t\t\"coordinates\" : \"506,697\"\n" +
                    "\t\t}";
        } else if (classifier.equals("stacking")) {
            modelFilename += "_1_1_" + "Stacking.model";
            classifierSpec = "{\n" +
                    "\t\t\t\"class\" : \"weka.knowledgeflow.steps.Classifier\",\n" +
                    "\t\t\t\"properties\" : {\n" +
                    "\t\t\t\t\"classifier\" : {\n" +
                    "\t\t\t\t\t\"type\" : \"optionHandler\",\n" +
                    "\t\t\t\t\t\"class\" : \"weka.classifiers.meta.Stacking\",\n" +
                    "\t\t\t\t\t\"options\" : \"-X 10 -M \\\"weka.classifiers.rules.ZeroR \\\" -S " + seed + " -num-slots 1 -B \\\"weka.classifiers.rules.ZeroR \\\"\"\n" +
                    "\t\t\t\t},\n" +
                    "\t\t\t\t\"loadClassifierFileName\" : \"" + modelFilename + "\",\n" +
                    "\t\t\t\t\"name\" : \"Classifier\",\n" +
                    "\t\t\t\t\"resetIncrementalClassifier\" : false,\n" +
                    "\t\t\t\t\"updateIncrementalClassifier\" : true\n" +
                    "\t\t\t},\n" +
                    "\t\t\t\"connections\" : {\n" +
                    "\t\t\t\t\"batchClassifier\" : [\n" +
                    "\t\t\t\t\t\"PredictionAppender\"\n" +
                    "\t\t\t\t]\n" +
                    "\t\t\t},\n" +
                    "\t\t\t\"coordinates\" : \"506,697\"\n" +
                    "\t\t}";
        } else if (classifier.equals("decisiontable")){
            modelFilename += "_1_1_" + "DecisionTable.model";
            classifierSpec = "{\n" +
                    "\t\t\t\"class\" : \"weka.knowledgeflow.steps.Classifier\",\n" +
                    "\t\t\t\"properties\" : {\n" +
                    "\t\t\t\t\"classifier\" : {\n" +
                    "\t\t\t\t\t\"type\" : \"optionHandler\",\n" +
                    "\t\t\t\t\t\"class\" : \"weka.classifiers.rules.DecisionTable\",\n" +
                    "\t\t\t\t\t\"options\" : \"-X 1 -E auc -S \\\"weka.attributeSelection.BestFirst -D 1 -N 5\\\"\"\n" +
                    "\t\t\t\t},\n" +
                    "\t\t\t\t\"loadClassifierFileName\" : \"" + modelFilename + "\",\n" +
                    "\t\t\t\t\"name\" : \"Classifier\",\n" +
                    "\t\t\t\t\"resetIncrementalClassifier\" : false,\n" +
                    "\t\t\t\t\"updateIncrementalClassifier\" : true\n" +
                    "\t\t\t},\n" +
                    "\t\t\t\"connections\" : {\n" +
                    "\t\t\t\t\"batchClassifier\" : [\n" +
                    "\t\t\t\t\t\"PredictionAppender\"\n" +
                    "\t\t\t\t]\n" +
                    "\t\t\t},\n" +
                    "\t\t\t\"coordinates\" : \"506,697\"\n" +
                    "\t\t}";
        } else if (classifier.equals("lwl")){
            modelFilename += "_1_1_" + "LWL.model";
            classifierSpec = "{\n" +
                    "\t\t\t\"class\" : \"weka.knowledgeflow.steps.Classifier\",\n" +
                    "\t\t\t\"properties\" : {\n" +
                    "\t\t\t\t\"classifier\" : {\n" +
                    "\t\t\t\t\t\"type\" : \"optionHandler\",\n" +
                    "\t\t\t\t\t\"class\" : \"weka.classifiers.lazy.LWL\",\n" +
                    "\t\t\t\t\t\"options\" : \"-U 0 -K -1 -A \\\"weka.core.neighboursearch.LinearNNSearch -A \\\\\\\"weka.core.EuclideanDistance -R first-last\\\\\\\"\\\" -W weka.classifiers.trees.DecisionStump\"\n" +
                    "\t\t\t\t},\n" +
                    "\t\t\t\t\"loadClassifierFileName\" : \"" + modelFilename + "\",\n" +
                    "\t\t\t\t\"name\" : \"Classifier\",\n" +
                    "\t\t\t\t\"resetIncrementalClassifier\" : false,\n" +
                    "\t\t\t\t\"updateIncrementalClassifier\" : true\n" +
                    "\t\t\t},\n" +
                    "\t\t\t\"connections\" : {\n" +
                    "\t\t\t\t\"batchClassifier\" : [\n" +
                    "\t\t\t\t\t\"PredictionAppender\"\n" +
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
                    "\t\t\t\t\t\"filePath\" : \"" + testFileNamePlaceholder + "\",\n" +
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
                    "\t\t\t\t\"classColumn\" : \"last\",\n" +
                    "\t\t\t\t\"name\" : \"ClassAssigner\"\n" +
                    "\t\t\t},\n" +
                    "\t\t\t\"connections\" : {\n" +
                    "\t\t\t\t\"dataSet\" : [\n" +
                    "\t\t\t\t\t\"TestSetMaker\"\n" +
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
                    "\t\t\t\"coordinates\" : \"1137,61\"\n" +
                    "\t\t},\n" +
                    "{\n" +
                    "\t\t\t\"class\" : \"weka.knowledgeflow.steps.PredictionAppender\",\n" +
                    "\t\t\t\"properties\" : {\n" +
                    "\t\t\t\t\"appendProbabilities\" : true,\n" +
                    "\t\t\t\t\"name\" : \"PredictionAppender\"\n" +
                    "\t\t\t},\n" +
                    "\t\t\t\"connections\" : {\n" +
                    "\t\t\t\t\"testSet\" : [\n" +
                    "\t\t\t\t\t\"TextViewer\"\n" +
                    "\t\t\t\t]\n" +
                    "\t\t\t},\n" +
                    "\t\t\t\"coordinates\" : \"904,98\"\n" +
                    "\t\t},"+
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
                    "\t\t\t\"coordinates\" : \"476,28\"\n" +
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
                    "\t\t\t\t\t\"filePath\" : \"" + testFileNamePlaceholder + "\",\n" +
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
                    "\t\t\t\t\"classColumn\" : \"last\",\n" +
                    "\t\t\t\t\"name\" : \"ClassAssigner\"\n" +
                    "\t\t\t},\n" +
                    "\t\t\t\"connections\" : {\n" +
                    "\t\t\t\t\"dataSet\" : [\n" +
                    "\t\t\t\t\t\"TestSetMaker\"\n" +
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
                    "\t\t\t\"coordinates\" : \"1137,61\"\n" +
                    "\t\t},\n" +
                    "{\n" +
                    "\t\t\t\"class\" : \"weka.knowledgeflow.steps.PredictionAppender\",\n" +
                    "\t\t\t\"properties\" : {\n" +
                    "\t\t\t\t\"appendProbabilities\" : true,\n" +
                    "\t\t\t\t\"name\" : \"PredictionAppender\"\n" +
                    "\t\t\t},\n" +
                    "\t\t\t\"connections\" : {\n" +
                    "\t\t\t\t\"testSet\" : [\n" +
                    "\t\t\t\t\t\"TextViewer\"\n" +
                    "\t\t\t\t]\n" +
                    "\t\t\t},\n" +
                    "\t\t\t\"coordinates\" : \"904,98\"\n" +
                    "\t\t},"+
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
                    "\t\t\t\"coordinates\" : \"476,28\"\n" +
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


        File f = new File(processFileName);
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
