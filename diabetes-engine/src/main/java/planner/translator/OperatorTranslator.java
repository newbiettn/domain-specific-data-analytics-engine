package planner.translator;

import common.ProjectPropertiesGetter;
import model.DMComponentDecorator;
import model.DMOperator;
import model.DMParameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * The Translate Decorator designed specifically for Operators.
 *
 * @project diabetes-engine
 * @author newbiettn on 26/2/18
 *
 */
public class OperatorTranslator extends DMComponentDecorator{
    private static Logger logger = LoggerFactory.getLogger(OperatorTranslator.class);

    private DMOperator op;
    private int seed;
    private ProjectPropertiesGetter propGetter = ProjectPropertiesGetter.getSingleton();
    private String filePath;

    /**
     * Specific translation method for operators.
     *
     * @return
     */
    @Override
    public String translate(int seed, String filePath, boolean performOptimization) {
        super.translate(seed, filePath, performOptimization);
        this.op = (DMOperator) this.com;
        this.seed = seed;
        this.filePath = filePath;

        DMOperator.OperatorType operatorType = op.getType();
        //-- data loader
        if (operatorType == DMOperator.OperatorType.LOAD_CSV)
           return translateLoadCSVOperator();
        else if (operatorType == DMOperator.OperatorType.LOAD_ARFF)
            return translateLoadARFFOperator();

        //-- data shuffle
        else if (operatorType == DMOperator.OperatorType.RANDOMIZE)
            return translateRandomize();

        //-- attribute selection
        else if (operatorType == DMOperator.OperatorType.CFS_SUBSET_EVAL)
            return translateCfsSubsetEval();
        else if (operatorType == DMOperator.OperatorType.CORRELATION_ATTR_EVAL)
            return translateCorrelationAttrEval();
        else if (operatorType == DMOperator.OperatorType.GAIN_RATIO_ATTR_EVAL)
            return translateGainRatioAttributeEval();
        else if (operatorType == DMOperator.OperatorType.INFO_GAIN_ATTR_EVAL)
            return translateInfoGainAttributeEval();
        else if (operatorType == DMOperator.OperatorType.RELIEFF_ATTR_EVAL)
            return translateReliefFAttributeEval();
        else if (operatorType == DMOperator.OperatorType.SYMMETRICAL_UNCERT_ATTR_EVAL)
            return translateSymmetricalUncertAttributeEval();
        else if (operatorType == DMOperator.OperatorType.WRAPPER_SUBSET_EVAL_WITH_ADABOOST)
            return translateWrapperSubsetEvalWithAdaboost();
        else if (operatorType == DMOperator.OperatorType.WRAPPER_SUBSET_EVAL_WITH_RF)
            return translateWrapperSubsetEvalWithRandomforest();

        //-- Preprocessing
        else if (operatorType == DMOperator.OperatorType.STANDARDIZATION)
            return translateStandardization();
        else if (operatorType == DMOperator.OperatorType.CLASS_ASSIGNER)
            return translateClassAssigner();
        else if (operatorType == DMOperator.OperatorType.REMOVE_FEATURE)
            return translateRemoveFeatureOperator();
        else if (operatorType == DMOperator.OperatorType.REPLACE_MISSING_VALUE)
            return translateReplaceMissingValue();

        //-- evaluation
        else if (operatorType == DMOperator.OperatorType.CROSS_VALIDATION_FOLD_MAKER)
            return translateCrossValidationFoldMaker();

        //-- classifier
        else if (operatorType == DMOperator.OperatorType.J48)
            return translateClassifierJ48();
        else if (operatorType == DMOperator.OperatorType.NEURAL_NETWORK){
            if (!performOptimization)
                return translateClassifierNeuralnetwork();
            else
                return translateClassifierNeuralNetworkWithOptimization();
        } else if (operatorType == DMOperator.OperatorType.RANDOM_FOREST){
            if (!performOptimization)
                return translateClassifierRandomforest();
            else
                return translateClassifierRandomforestWithOptimization();

        }
        else if (operatorType == DMOperator.OperatorType.ADABOOST_M1)
            return translateClassifierAdaBoost();
        else if (operatorType == DMOperator.OperatorType.LOGISTIC)
            return translateClassifierLogistic();
        else if (operatorType == DMOperator.OperatorType.SMO)
            return translateClassifierSMO();
        else if (operatorType == DMOperator.OperatorType.KSTAR){
            if (!performOptimization)
                return translateClassifierKStar();
            else
                return translateClassifierKStarWithOptimization();
        }
        else if (operatorType == DMOperator.OperatorType.IBK)
            return translateClassifierIBk();
        else if (operatorType == DMOperator.OperatorType.LWL)
            return translateClassifierLWL();
        else if (operatorType == DMOperator.OperatorType.BAGGING)
            return translateClassifierBagging();
        else if (operatorType == DMOperator.OperatorType.LOGITBOOST)
            return translateClassifierLogitBoost();
        else if (operatorType == DMOperator.OperatorType.RANDOM_SUBSPACE)
            return translateClassifierRandomSubSpace();
        else if (operatorType == DMOperator.OperatorType.STACKING)
            return translateClassifierStacking();
        else if (operatorType == DMOperator.OperatorType.VOTE)
            return translateClassifierVote();
        else if (operatorType == DMOperator.OperatorType.DECISION_TABLE)
            return translateClassifierDecisionTable();

        //-- result gathering
        else if (operatorType == DMOperator.OperatorType.CLASSIFIER_PERFORMANCE_EVALUATOR)
            return translateClassifierPerformanceEvaluator();
        else if (operatorType == DMOperator.OperatorType.RESULT_VIEWER)
            return translateTextViewer();
        else
            return "";
    }

    /**
     * For LoadCSV
     *
     * @return a string of json
     */
    private String translateLoadCSVOperator(){
        String fileName = op.getParameters().get(0).getValue();
        String nextOpName = op.getNextOp().getName();
        String opName = this.op.getName();
        String json = "{\n" +
                "\t\t\t\"class\" : \"weka.knowledgeflow.steps.Loader\",\n" +
                "\t\t\t\"properties\" : {\n" +
                "\t\t\t\t\"loader\" : {\n" +
                "\t\t\t\t\t\"type\" : \"loader\",\n" +
                "\t\t\t\t\t\"class\" : \"weka.core.converters.CSVLoader\",\n" +
                "\t\t\t\t\t\"filePath\" : \"" + this.filePath + fileName +".arff\",\n" +
                "\t\t\t\t\t\"useRelativePath\" : false\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t\"name\" : \""+opName+"\"\n" +
                "\t\t\t},\n" +
                "\t\t\t\"connections\" : {\n" +
                "\t\t\t\t\"dataSet\" : [\n" +
                "\t\t\t\t\t\""+ nextOpName + "\"\n" +
                "\t\t\t\t]\n" +
                "\t\t\t},\n" +
                "\t\t\t\"coordinates\" : \"54,300\"\n" +
                "\t\t}";
        return json;
    }

    /**
     * LoadARFF
     *
     * @return a string of json
     */
    private String translateLoadARFFOperator(){
        String fileName = op.getParameters().get(0).getValue();
        String nextOpName = op.getNextOp().getName();
        String opName = this.op.getName();
        String json = "{\n" +
                "\t\t\t\"class\" : \"weka.knowledgeflow.steps.Loader\",\n" +
                "\t\t\t\"properties\" : {\n" +
                "\t\t\t\t\"loader\" : {\n" +
                "\t\t\t\t\t\"type\" : \"loader\",\n" +
                "\t\t\t\t\t\"class\" : \"weka.core.converters.ArffLoader\",\n" +
                "\t\t\t\t\t\"filePath\" : \"" + this.filePath + fileName +".arff\",\n" +
                "\t\t\t\t\t\"useRelativePath\" : false\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t\"name\" : \""+opName+"\"\n" +
                "\t\t\t},\n" +
                "\t\t\t\"connections\" : {\n" +
                "\t\t\t\t\"dataSet\" : [\n" +
                "\t\t\t\t\t\""+ nextOpName +"\"\n" +
                "\t\t\t\t]\n" +
                "\t\t\t},\n" +
                "\t\t\t\"coordinates\" : \"54,300\"\n" +
                "\t\t}";
        return json;

    }

    /**
     * ClassAssigner component
     *
     * @return a string of json.
     */
    private String translateClassAssigner(){
        String className = op.getParameters().get(0).getValue();
        String nextOpName = op.getNextOp().getName();
        String opName = this.op.getName();
        String json = "{\n" +
                "\t\t\t\"class\" : \"weka.knowledgeflow.steps.ClassAssigner\",\n" +
                "\t\t\t\"properties\" : {\n" +
                "\t\t\t\t\"classColumn\" : \""+ className +"\",\n" +
                "\t\t\t\t\"name\" : \""+opName+"\"\n" +
                "\t\t\t},\n" +
                "\t\t\t\"connections\" : {\n" +
                "\t\t\t\t\"dataSet\" : [\n" +
                "\t\t\t\t\t\"" + nextOpName +"\"\n" +
                "\t\t\t\t]\n" +
                "\t\t\t},\n" +
                "\t\t\t\"coordinates\" : \"68,439\"\n" +
                "\t\t}";
        return json;
    }

    /**
     * Shuffle dataset
     *
     * @return a string of json.
     */
    private String translateRandomize(){
        String opName = this.op.getName();
        String nextOpName = op.getNextOp().getName();
        String json = "\t\t{\n" +
                "\t\t\t\"class\" : \"weka.knowledgeflow.steps.Filter\",\n" +
                "\t\t\t\"properties\" : {\n" +
                "\t\t\t\t\"filter\" : {\n" +
                "\t\t\t\t\t\"type\" : \"optionHandler\",\n" +
                "\t\t\t\t\t\"class\" : \"weka.filters.unsupervised.instance.Randomize\",\n" +
                "\t\t\t\t\t\"options\" : \"-S "+ this.seed +"\"\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t\"name\" : \"" + opName +"\"\n" +
                "\t\t\t},\n" +
                "\t\t\t\"connections\" : {\n" +
                "\t\t\t\t\"dataSet\" : [\n" +
                "\t\t\t\t\t\"" + nextOpName +"\"\n" +
                "\t\t\t\t]\n" +
                "\t\t\t},\n" +
                "\t\t\t\"coordinates\" : \"154,381\"\n" +
                "\t\t}\n";
        return json;
    }

    /**
     * TextViewer
     *
     * @return a string of json.
     */
    private String translateTextViewer(){
        String opName = this.op.getName();
        String json = "{\n" +
                "\t\t\t\"class\" : \"weka.knowledgeflow.steps.TextViewer\",\n" +
                "\t\t\t\"properties\" : {\n" +
                "\t\t\t\t\"name\" : \""+opName+"\"\n" +
                "\t\t\t},\n" +
                "\t\t\t\"connections\" : {\n" +
                "\t\t\t},\n" +
                "\t\t\t\"coordinates\" : \"589,329\"\n" +
                "\t\t}";
        return json;
    }

    /**
     * Classifier Adaboost
     *
     * @return a string of json
     */
    private String translateClassifierAdaBoost(){
        String nextOpName = op.getNextOp().getName();
        String opName = op.getName();
        String json = "{\n" +
                "\t\t\t\"class\" : \"weka.knowledgeflow.steps.Classifier\",\n" +
                "\t\t\t\"properties\" : {\n" +
                "\t\t\t\t\"classifier\" : {\n" +
                "\t\t\t\t\t\"type\" : \"optionHandler\",\n" +
                "\t\t\t\t\t\"class\" : \"weka.classifiers.meta.AdaBoostM1\",\n" +
                "\t\t\t\t\t\"options\" : \"-P 100 -S " + this.seed +" -I 10 -W weka.classifiers.trees.DecisionStump\"\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t\"loadClassifierFileName\" : \"\",\n" +
                "\t\t\t\t\"name\" : \""+opName+"\",\n" +
                "\t\t\t\t\"resetIncrementalClassifier\" : false,\n" +
                "\t\t\t\t\"updateIncrementalClassifier\" : true\n" +
                "\t\t\t},\n" +
                "\t\t\t\"connections\" : {\n" +
                "\t\t\t\t\"batchClassifier\" : [\n" +
                "\t\t\t\t\t\""+ nextOpName+"\"\n" +
                "\t\t\t\t]\n" +
                "\t\t\t},\n" +
                "\t\t\t\"coordinates\" : \"486,501\"\n" +
                "\t\t}";
        return json;
    }
    /**
     * Classifier Neural Network
     *
     * @return a string of json
     */
    private String translateClassifierNeuralnetwork(){
        String nextOpName = op.getNextOp().getName();
        String opName = op.getName();
        String json = "{\n" +
                "\t\t\t\"class\" : \"weka.knowledgeflow.steps.Classifier\",\n" +
                "\t\t\t\"properties\" : {\n" +
                "\t\t\t\t\"classifier\" : {\n" +
                "\t\t\t\t\t\"type\" : \"optionHandler\",\n" +
                "\t\t\t\t\t\"class\" : \"weka.classifiers.functions.MultilayerPerceptron\",\n" +
                "\t\t\t\t\t\"options\" : \"-L 0.3 -M 0.2 -N 500 -V 0 -S " +  this.seed + " -E 20 -H a\"\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t\"loadClassifierFileName\" : \"\",\n" +
                "\t\t\t\t\"name\" : \""+opName+"\",\n" +
                "\t\t\t\t\"resetIncrementalClassifier\" : false,\n" +
                "\t\t\t\t\"updateIncrementalClassifier\" : true\n" +
                "\t\t\t},\n" +
                "\t\t\t\"connections\" : {\n" +
                "\t\t\t\t\"batchClassifier\" : [\n" +
                "\t\t\t\t\t\""+ nextOpName+"\"\n" +
                "\t\t\t\t]\n" +
                "\t\t\t},\n" +
                "\t\t\t\"coordinates\" : \"486,501\"\n" +
                "\t\t}";
        return json;
    }
    /**
     * Classifier Random Forest
     *
     * @return a string of json
     */
    private String translateClassifierRandomforest(){
        String nextOpName = op.getNextOp().getName();
        String opName = op.getName();
        String json = "{\n" +
                "\t\t\t\"class\" : \"weka.knowledgeflow.steps.Classifier\",\n" +
                "\t\t\t\"properties\" : {\n" +
                "\t\t\t\t\"classifier\" : {\n" +
                "\t\t\t\t\t\"type\" : \"optionHandler\",\n" +
                "\t\t\t\t\t\"class\" : \"weka.classifiers.trees.RandomForest\",\n" +
                "\t\t\t\t\t\"options\" : \"-P 100 -I 100 -num-slots 1 -K 0 -M 1.0 -V 0.001 -S " + this.seed + "\"\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t\"loadClassifierFileName\" : \"\",\n" +
                "\t\t\t\t\"name\" : \""+opName+"\",\n" +
                "\t\t\t\t\"resetIncrementalClassifier\" : false,\n" +
                "\t\t\t\t\"updateIncrementalClassifier\" : true\n" +
                "\t\t\t},\n" +
                "\t\t\t\"connections\" : {\n" +
                "\t\t\t\t\"batchClassifier\" : [\n" +
                "\t\t\t\t\t\""+ nextOpName+"\"\n" +
                "\t\t\t\t]\n" +
                "\t\t\t},\n" +
                "\t\t\t\"coordinates\" : \"486,501\"\n" +
                "\t\t}";
        return json;
    }
    /**
     * Classifier Random Forest
     *
     * @return a string of json
     */
    private String translateClassifierRandomforestWithOptimization(){
        String nextOpName = op.getNextOp().getName();
        String opName = op.getName();
        String json = "{\n" +
                "\t\t\t\"class\" : \"weka.knowledgeflow.steps.Classifier\",\n" +
                "\t\t\t\"properties\" : {\n" +
                "\t\t\t\t\"classifier\" : {\n" +
                "\t\t\t\t\t\"type\" : \"optionHandler\",\n" +
                "\t\t\t\t\t\"class\" : \"weka.classifiers.meta.CVParameterSelection\",\n" +
                "\t\t\t\t\t\"options\" : \"-P \\\"I 1.0 201.0 11.0\\\" -P \\\"K 2.0 32.0 11.0\\\" -X 10 -S 1 -W weka.classifiers.trees.RandomForest -- -P 100 -I 100 -num-slots 1 -K 0 -M 1.0 -V 0.001 -S " + this.seed + "\"\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t\"loadClassifierFileName\" : \"\",\n" +
                "\t\t\t\t\"name\" : \""+opName+"\",\n" +
                "\t\t\t\t\"resetIncrementalClassifier\" : false,\n" +
                "\t\t\t\t\"updateIncrementalClassifier\" : true\n" +
                "\t\t\t},\n" +
                "\t\t\t\"connections\" : {\n" +
                "\t\t\t\t\"batchClassifier\" : [\n" +
                "\t\t\t\t\t\""+ nextOpName+"\"\n" +
                "\t\t\t\t]\n" +
                "\t\t\t},\n" +
                "\t\t\t\"coordinates\" : \"486,501\"\n" +
                "\t\t}";
        return json;

    }

    /**
     * Classifier Random Forest
     *
     * @return a string of json
     */
    private String translateClassifierNeuralNetworkWithOptimization(){
        String nextOpName = op.getNextOp().getName();
        String opName = op.getName();
        String json = "{\n" +
                "\t\t\t\"class\" : \"weka.knowledgeflow.steps.Classifier\",\n" +
                "\t\t\t\"properties\" : {\n" +
                "\t\t\t\t\"classifier\" : {\n" +
                "\t\t\t\t\t\"type\" : \"optionHandler\",\n" +
                "\t\t\t\t\t\"class\" : \"weka.classifiers.meta.CVParameterSelection\",\n" +
                "\t\t\t\t\t\"options\" : \"-P \\\"M 0.0 1.0 10.0\\\" -P \\\"L 0.0 1.0 10.0\\\" -X 10 -S 1 -W weka.classifiers.functions.MultilayerPerceptron -- -L 0.3 -M 0.2 -N 500 -V 0 -S " +  this.seed + " -E 20 -H a\"\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t\"loadClassifierFileName\" : \"\",\n" +
                "\t\t\t\t\"name\" : \""+opName+"\",\n" +
                "\t\t\t\t\"resetIncrementalClassifier\" : false,\n" +
                "\t\t\t\t\"updateIncrementalClassifier\" : true\n" +
                "\t\t\t},\n" +
                "\t\t\t\"connections\" : {\n" +
                "\t\t\t\t\"batchClassifier\" : [\n" +
                "\t\t\t\t\t\""+ nextOpName+"\"\n" +
                "\t\t\t\t]\n" +
                "\t\t\t},\n" +
                "\t\t\t\"coordinates\" : \"486,501\"\n" +
                "\t\t}";
        return json;

    }



    /**
     * Classifier Logistic
     *
     * @return a string of json
     */
    private String translateClassifierLogistic(){
        String nextOpName = op.getNextOp().getName();
        String opName = op.getName();
        String json = "{\n" +
                "\t\t\t\"class\" : \"weka.knowledgeflow.steps.Classifier\",\n" +
                "\t\t\t\"properties\" : {\n" +
                "\t\t\t\t\"classifier\" : {\n" +
                "\t\t\t\t\t\"type\" : \"optionHandler\",\n" +
                "\t\t\t\t\t\"class\" : \"weka.classifiers.functions.Logistic\",\n" +
                "\t\t\t\t\t\"options\" : \"-R 1.0E-8 -M -1 -num-decimal-places 4 \"\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t\"loadClassifierFileName\" : \"\",\n" +
                "\t\t\t\t\"name\" : \""+opName+"\",\n" +
                "\t\t\t\t\"resetIncrementalClassifier\" : false,\n" +
                "\t\t\t\t\"updateIncrementalClassifier\" : true\n" +
                "\t\t\t},\n" +
                "\t\t\t\"connections\" : {\n" +
                "\t\t\t\t\"batchClassifier\" : [\n" +
                "\t\t\t\t\t\""+nextOpName+"\"\n" +
                "\t\t\t\t]\n" +
                "\t\t\t},\n" +
                "\t\t\t\"coordinates\" : \"506,697\"\n" +
                "\t\t}";
        return json;
    }

    /**
     * Classifier SMO
     *
     * @return a string of json
     */
    private String translateClassifierSMO(){
        String nextOpName = op.getNextOp().getName();
        String opName = op.getName();
        String json = "{\n" +
                "\t\t\t\"class\" : \"weka.knowledgeflow.steps.Classifier\",\n" +
                "\t\t\t\"properties\" : {\n" +
                "\t\t\t\t\"classifier\" : {\n" +
                "\t\t\t\t\t\"type\" : \"optionHandler\",\n" +
                "\t\t\t\t\t\"class\" : \"weka.classifiers.functions.SMO\",\n" +
                "\t\t\t\t\t\"options\" : \"-C 1.0 -L 0.001 -P 1.0E-12 -N 0 -V -1 -W " + this.seed + " -K \\\"weka.classifiers.functions.supportVector.PolyKernel -E 1.0 -C 250007\\\" -calibrator \\\"weka.classifiers.functions.Logistic -R 1.0E-8 -M -1 -num-decimal-places 4\\\"\"\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t\"loadClassifierFileName\" : \"\",\n" +
                "\t\t\t\t\"name\" : \""+opName+"\",\n" +
                "\t\t\t\t\"resetIncrementalClassifier\" : false,\n" +
                "\t\t\t\t\"updateIncrementalClassifier\" : true\n" +
                "\t\t\t},\n" +
                "\t\t\t\"connections\" : {\n" +
                "\t\t\t\t\"batchClassifier\" : [\n" +
                "\t\t\t\t\t\""+nextOpName+"\"\n" +
                "\t\t\t\t]\n" +
                "\t\t\t},\n" +
                "\t\t\t\"coordinates\" : \"506,697\"\n" +
                "\t\t}";
        return json;
    }

    /**
     * Classifier KStar
     *
     * @return a string of json
     */
    private String translateClassifierKStarWithOptimization(){
        String nextOpName = op.getNextOp().getName();
        String opName = op.getName();
        String json = "{\n" +
                "\t\t\t\"class\" : \"weka.knowledgeflow.steps.Classifier\",\n" +
                "\t\t\t\"properties\" : {\n" +
                "\t\t\t\t\"classifier\" : {\n" +
                "\t\t\t\t\t\"type\" : \"optionHandler\",\n" +
                "\t\t\t\t\t\"class\" : \"weka.classifiers.meta.CVParameterSelection\",\n" +
                "\t\t\t\t\t\"options\" : \"-P \\\"B 1.0 100.0 100.0\\\" -X 10 -S 1 -W weka.classifiers.lazy.KStar -- -B 20 -M a\"\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t\"loadClassifierFileName\" : \"\",\n" +
                "\t\t\t\t\"name\" : \""+opName+"\",\n" +
                "\t\t\t\t\"resetIncrementalClassifier\" : false,\n" +
                "\t\t\t\t\"updateIncrementalClassifier\" : true\n" +
                "\t\t\t},\n" +
                "\t\t\t\"connections\" : {\n" +
                "\t\t\t\t\"batchClassifier\" : [\n" +
                "\t\t\t\t\t\""+nextOpName+"\"\n" +
                "\t\t\t\t]\n" +
                "\t\t\t},\n" +
                "\t\t\t\"coordinates\" : \"506,697\"\n" +
                "\t\t}";
        return json;

    }

    /**
     * Classifier KStar
     *
     * @return a string of json
     */
    private String translateClassifierKStar(){
        String nextOpName = op.getNextOp().getName();
        String opName = op.getName();
        String json = "{\n" +
                "\t\t\t\"class\" : \"weka.knowledgeflow.steps.Classifier\",\n" +
                "\t\t\t\"properties\" : {\n" +
                "\t\t\t\t\"classifier\" : {\n" +
                "\t\t\t\t\t\"type\" : \"optionHandler\",\n" +
                "\t\t\t\t\t\"class\" : \"weka.classifiers.lazy.KStar\",\n" +
                "\t\t\t\t\t\"options\" : \"-B 20 -M a\"\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t\"loadClassifierFileName\" : \"\",\n" +
                "\t\t\t\t\"name\" : \""+opName+"\",\n" +
                "\t\t\t\t\"resetIncrementalClassifier\" : false,\n" +
                "\t\t\t\t\"updateIncrementalClassifier\" : true\n" +
                "\t\t\t},\n" +
                "\t\t\t\"connections\" : {\n" +
                "\t\t\t\t\"batchClassifier\" : [\n" +
                "\t\t\t\t\t\""+nextOpName+"\"\n" +
                "\t\t\t\t]\n" +
                "\t\t\t},\n" +
                "\t\t\t\"coordinates\" : \"506,697\"\n" +
                "\t\t}";
        return json;
    }


    /**
     * Classifier IBk
     *
     * @return a string of json
     */
    private String translateClassifierIBk(){
        String nextOpName = op.getNextOp().getName();
        String opName = op.getName();
        String json = "{\n" +
                "\t\t\t\"class\" : \"weka.knowledgeflow.steps.Classifier\",\n" +
                "\t\t\t\"properties\" : {\n" +
                "\t\t\t\t\"classifier\" : {\n" +
                "\t\t\t\t\t\"type\" : \"optionHandler\",\n" +
                "\t\t\t\t\t\"class\" : \"weka.classifiers.lazy.IBk\",\n" +
                "\t\t\t\t\t\"options\" : \"-K 1 -W 0 -A \\\"weka.core.neighboursearch.LinearNNSearch -A \\\\\\\"weka.core.EuclideanDistance -R first-last\\\\\\\"\\\"\"" +
                "\t\t\t\t},\n" +
                "\t\t\t\t\"loadClassifierFileName\" : \"\",\n" +
                "\t\t\t\t\"name\" : \""+opName+"\",\n" +
                "\t\t\t\t\"resetIncrementalClassifier\" : false,\n" +
                "\t\t\t\t\"updateIncrementalClassifier\" : true\n" +
                "\t\t\t},\n" +
                "\t\t\t\"connections\" : {\n" +
                "\t\t\t\t\"batchClassifier\" : [\n" +
                "\t\t\t\t\t\""+nextOpName+"\"\n" +
                "\t\t\t\t]\n" +
                "\t\t\t},\n" +
                "\t\t\t\"coordinates\" : \"506,697\"\n" +
                "\t\t}";
        return json;
    }

    /**
     * Classifier LWL
     *
     * @return a string of json
     */
    private String translateClassifierLWL(){
        String nextOpName = op.getNextOp().getName();
        String opName = op.getName();
        String json = "{\n" +
                "\t\t\t\"class\" : \"weka.knowledgeflow.steps.Classifier\",\n" +
                "\t\t\t\"properties\" : {\n" +
                "\t\t\t\t\"classifier\" : {\n" +
                "\t\t\t\t\t\"type\" : \"optionHandler\",\n" +
                "\t\t\t\t\t\"class\" : \"weka.classifiers.lazy.LWL\",\n" +
                "\t\t\t\t\t\"options\" : \"-U 0 -K -1 -A \\\"weka.core.neighboursearch.LinearNNSearch -A \\\\\\\"weka.core.EuclideanDistance -R first-last\\\\\\\"\\\" -W weka.classifiers.trees.DecisionStump\"\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t\"loadClassifierFileName\" : \"\",\n" +
                "\t\t\t\t\"name\" : \""+opName+"\",\n" +
                "\t\t\t\t\"resetIncrementalClassifier\" : false,\n" +
                "\t\t\t\t\"updateIncrementalClassifier\" : true\n" +
                "\t\t\t},\n" +
                "\t\t\t\"connections\" : {\n" +
                "\t\t\t\t\"batchClassifier\" : [\n" +
                "\t\t\t\t\t\""+nextOpName+"\"\n" +
                "\t\t\t\t]\n" +
                "\t\t\t},\n" +
                "\t\t\t\"coordinates\" : \"506,697\"\n" +
                "\t\t}";
        return json;
    }

    /**
     * Classifier Bagging
     *
     * @return a string of json
     */
    private String translateClassifierBagging(){
        String nextOpName = op.getNextOp().getName();
        String opName = op.getName();
        String json = "{\n" +
                "\t\t\t\"class\" : \"weka.knowledgeflow.steps.Classifier\",\n" +
                "\t\t\t\"properties\" : {\n" +
                "\t\t\t\t\"classifier\" : {\n" +
                "\t\t\t\t\t\"type\" : \"optionHandler\",\n" +
                "\t\t\t\t\t\"class\" : \"weka.classifiers.meta.Bagging\",\n" +
                "\t\t\t\t\t\"options\" : \"-P 100 -S " + this.seed +" -num-slots 1 -I 10 -W weka.classifiers.trees.REPTree -- -M 2 -V 0.001 -N 3 -S 1 -L -1 -I 0.0\"\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t\"loadClassifierFileName\" : \"\",\n" +
                "\t\t\t\t\"name\" : \""+opName+"\",\n" +
                "\t\t\t\t\"resetIncrementalClassifier\" : false,\n" +
                "\t\t\t\t\"updateIncrementalClassifier\" : true\n" +
                "\t\t\t},\n" +
                "\t\t\t\"connections\" : {\n" +
                "\t\t\t\t\"batchClassifier\" : [\n" +
                "\t\t\t\t\t\""+nextOpName+"\"\n" +
                "\t\t\t\t]\n" +
                "\t\t\t},\n" +
                "\t\t\t\"coordinates\" : \"506,697\"\n" +
                "\t\t}";
        return json;
    }

    /**
     * Classifier LogitBoost
     *
     * @return a string of json
     */
    private String translateClassifierLogitBoost(){
        String nextOpName = op.getNextOp().getName();
        String opName = op.getName();
        String json = "{\n" +
                "\t\t\t\"class\" : \"weka.knowledgeflow.steps.Classifier\",\n" +
                "\t\t\t\"properties\" : {\n" +
                "\t\t\t\t\"classifier\" : {\n" +
                "\t\t\t\t\t\"type\" : \"optionHandler\",\n" +
                "\t\t\t\t\t\"class\" : \"weka.classifiers.meta.LogitBoost\",\n" +
                "\t\t\t\t\t\"options\" : \"-P 100 -L -1.7976931348623157E308 -H 1.0 -Z 3.0 -O 1 -E 1 -S " + this.seed + " -I 10 -W weka.classifiers.trees.DecisionStump\"\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t\"loadClassifierFileName\" : \"\",\n" +
                "\t\t\t\t\"name\" : \""+opName+"\",\n" +
                "\t\t\t\t\"resetIncrementalClassifier\" : false,\n" +
                "\t\t\t\t\"updateIncrementalClassifier\" : true\n" +
                "\t\t\t},\n" +
                "\t\t\t\"connections\" : {\n" +
                "\t\t\t\t\"batchClassifier\" : [\n" +
                "\t\t\t\t\t\""+nextOpName+"\"\n" +
                "\t\t\t\t]\n" +
                "\t\t\t},\n" +
                "\t\t\t\"coordinates\" : \"506,697\"\n" +
                "\t\t}";
        return json;
    }

    /**
     * Classifier RandomSubSpace
     *
     * @return a string of json
     */
    private String translateClassifierRandomSubSpace(){
        String nextOpName = op.getNextOp().getName();
        String opName = op.getName();
        String json = "{\n" +
                "\t\t\t\"class\" : \"weka.knowledgeflow.steps.Classifier\",\n" +
                "\t\t\t\"properties\" : {\n" +
                "\t\t\t\t\"classifier\" : {\n" +
                "\t\t\t\t\t\"type\" : \"optionHandler\",\n" +
                "\t\t\t\t\t\"class\" : \"weka.classifiers.meta.RandomSubSpace\",\n" +
                "\t\t\t\t\t\"options\" : \"-P 0.5 -S " + this.seed + " -num-slots 1 -I 10 -W weka.classifiers.trees.REPTree -- -M 2 -V 0.001 -N 3 -S " + this.seed + " -L -1 -I 0.0\"\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t\"loadClassifierFileName\" : \"\",\n" +
                "\t\t\t\t\"name\" : \""+opName+"\",\n" +
                "\t\t\t\t\"resetIncrementalClassifier\" : false,\n" +
                "\t\t\t\t\"updateIncrementalClassifier\" : true\n" +
                "\t\t\t},\n" +
                "\t\t\t\"connections\" : {\n" +
                "\t\t\t\t\"batchClassifier\" : [\n" +
                "\t\t\t\t\t\""+nextOpName+"\"\n" +
                "\t\t\t\t]\n" +
                "\t\t\t},\n" +
                "\t\t\t\"coordinates\" : \"506,697\"\n" +
                "\t\t}";
        return json;
    }

    /**
     * Classifier Stacking
     *
     * @return a string of json
     */
    private String translateClassifierStacking(){
        String nextOpName = op.getNextOp().getName();
        String opName = op.getName();
        String json = "{\n" +
                "\t\t\t\"class\" : \"weka.knowledgeflow.steps.Classifier\",\n" +
                "\t\t\t\"properties\" : {\n" +
                "\t\t\t\t\"classifier\" : {\n" +
                "\t\t\t\t\t\"type\" : \"optionHandler\",\n" +
                "\t\t\t\t\t\"class\" : \"weka.classifiers.meta.Stacking\",\n" +
                "\t\t\t\t\t\"options\" : \"-X 10 -M \\\"weka.classifiers.rules.ZeroR \\\" -S " + this.seed + " -num-slots 1 -B \\\"weka.classifiers.rules.ZeroR \\\"\"\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t\"loadClassifierFileName\" : \"\",\n" +
                "\t\t\t\t\"name\" : \""+opName+"\",\n" +
                "\t\t\t\t\"resetIncrementalClassifier\" : false,\n" +
                "\t\t\t\t\"updateIncrementalClassifier\" : true\n" +
                "\t\t\t},\n" +
                "\t\t\t\"connections\" : {\n" +
                "\t\t\t\t\"batchClassifier\" : [\n" +
                "\t\t\t\t\t\""+nextOpName+"\"\n" +
                "\t\t\t\t]\n" +
                "\t\t\t},\n" +
                "\t\t\t\"coordinates\" : \"506,697\"\n" +
                "\t\t}";
        return json;
    }

    /**
     * Classifier Vote
     *
     * @return a string of json
     */
    private String translateClassifierVote(){
        String nextOpName = op.getNextOp().getName();
        String opName = op.getName();
        String json = "{\n" +
                "\t\t\t\"class\" : \"weka.knowledgeflow.steps.Classifier\",\n" +
                "\t\t\t\"properties\" : {\n" +
                "\t\t\t\t\"classifier\" : {\n" +
                "\t\t\t\t\t\"type\" : \"optionHandler\",\n" +
                "\t\t\t\t\t\"class\" : \"weka.classifiers.meta.Vote\",\n" +
                "\t\t\t\t\t\"options\" : \"-S " + this.seed + " -B \\\"weka.classifiers.rules.ZeroR \\\" -R AVG\"\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t\"loadClassifierFileName\" : \"\",\n" +
                "\t\t\t\t\"name\" : \""+opName+"\",\n" +
                "\t\t\t\t\"resetIncrementalClassifier\" : false,\n" +
                "\t\t\t\t\"updateIncrementalClassifier\" : true\n" +
                "\t\t\t},\n" +
                "\t\t\t\"connections\" : {\n" +
                "\t\t\t\t\"batchClassifier\" : [\n" +
                "\t\t\t\t\t\""+nextOpName+"\"\n" +
                "\t\t\t\t]\n" +
                "\t\t\t},\n" +
                "\t\t\t\"coordinates\" : \"506,697\"\n" +
                "\t\t}";
        return json;
    }

    /**
     * Classifier DecisionTable
     *
     * @return a string of json
     */
    private String translateClassifierDecisionTable(){
        String nextOpName = op.getNextOp().getName();
        String opName = op.getName();
        String json = "{\n" +
                "\t\t\t\"class\" : \"weka.knowledgeflow.steps.Classifier\",\n" +
                "\t\t\t\"properties\" : {\n" +
                "\t\t\t\t\"classifier\" : {\n" +
                "\t\t\t\t\t\"type\" : \"optionHandler\",\n" +
                "\t\t\t\t\t\"class\" : \"weka.classifiers.rules.DecisionTable\",\n" +
                "\t\t\t\t\t\"options\" : \"-X 1 -E auc -S \\\"weka.attributeSelection.BestFirst -D 1 -N 5\\\"\"\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t\"loadClassifierFileName\" : \"\",\n" +
                "\t\t\t\t\"name\" : \""+opName+"\",\n" +
                "\t\t\t\t\"resetIncrementalClassifier\" : false,\n" +
                "\t\t\t\t\"updateIncrementalClassifier\" : true\n" +
                "\t\t\t},\n" +
                "\t\t\t\"connections\" : {\n" +
                "\t\t\t\t\"batchClassifier\" : [\n" +
                "\t\t\t\t\t\""+nextOpName+"\"\n" +
                "\t\t\t\t]\n" +
                "\t\t\t},\n" +
                "\t\t\t\"coordinates\" : \"506,697\"\n" +
                "\t\t}";
        return json;
    }


    /**
     * Classifier J48
     *
     * @return a string of json
     */
    private String translateClassifierJ48(){
        String nextOpName = op.getNextOp().getName();
        String opName = op.getName();
        String json = "{\n" +
                "\t\t\t\"class\" : \"weka.knowledgeflow.steps.Classifier\",\n" +
                "\t\t\t\"properties\" : {\n" +
                "\t\t\t\t\"classifier\" : {\n" +
                "\t\t\t\t\t\"type\" : \"optionHandler\",\n" +
                "\t\t\t\t\t\"class\" : \"weka.classifiers.trees.J48\",\n" +
                "\t\t\t\t\t\"options\" : \"-C 0.25 -M 2 -Q " + this.seed +"\"\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t\"loadClassifierFileName\" : \"\",\n" +
                "\t\t\t\t\"name\" : \""+opName+"\",\n" +
                "\t\t\t\t\"resetIncrementalClassifier\" : false,\n" +
                "\t\t\t\t\"updateIncrementalClassifier\" : true\n" +
                "\t\t\t},\n" +
                "\t\t\t\"connections\" : {\n" +
                "\t\t\t\t\"batchClassifier\" : [\n" +
                "\t\t\t\t\t\""+nextOpName+"\"\n" +
                "\t\t\t\t]\n" +
                "\t\t\t},\n" +
                "\t\t\t\"coordinates\" : \"506,697\"\n" +
                "\t\t}";
        return json;
    }

    /**
     * CfsSubsetEval.
     *
     * @return a string of json
     */
    private String translateCfsSubsetEval(){
        String nextOpName = op.getNextOp().getName();
        String opName = op.getName();
        String json = "{\n" +
                "\t\t\t\"class\" : \"weka.knowledgeflow.steps.Filter\",\n" +
                "\t\t\t\"properties\" : {\n" +
                "\t\t\t\t\"filter\" : {\n" +
                "\t\t\t\t\t\"type\" : \"optionHandler\",\n" +
                "\t\t\t\t\t\"class\" : \"weka.filters.supervised.attribute.AttributeSelection\",\n" +
                "\t\t\t\t\t\"options\" : \"-E \\\"weka.attributeSelection.CfsSubsetEval -P 1 -E 1\\\" -S " +
                                        "\\\"weka.attributeSelection.BestFirst -D 1 -N 5\\\"\"\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t\"name\" : \""+opName+"\"\n" +
                "\t\t\t},\n" +
                "\t\t\t\"connections\" : {\n" +
                "\t\t\t\t\"dataSet\" : [\n" +
                "\t\t\t\t\t\""+ nextOpName+"\"\n" +
                "\t\t\t\t]\n" +
                "\t\t\t},\n" +
                "\t\t\t\"coordinates\" : \"211,484\"\n" +
                "\t\t}";
        return json;
    }

    /**
     * Attribute Selection.
     *
     * @return a string of json
     */
    private String translateCorrelationAttrEval(){
        String nextOpName = op.getNextOp().getName();
        String opName = op.getName();
        String json = "{\n" +
                "\t\t\t\"class\" : \"weka.knowledgeflow.steps.Filter\",\n" +
                "\t\t\t\"properties\" : {\n" +
                "\t\t\t\t\"filter\" : {\n" +
                "\t\t\t\t\t\"type\" : \"optionHandler\",\n" +
                "\t\t\t\t\t\"class\" : \"weka.filters.supervised.attribute.AttributeSelection\",\n" +
                "\t\t\t\t\t\"options\" : \"-E \\\"weka.attributeSelection.CorrelationAttributeEval \\\" -S " +
                "\\\"weka.attributeSelection.Ranker -T -1.7976931348623157E308 -N -1\\\"\"\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t\"name\" : \""+opName+"\"\n" +
                "\t\t\t},\n" +
                "\t\t\t\"connections\" : {\n" +
                "\t\t\t\t\"dataSet\" : [\n" +
                "\t\t\t\t\t\""+ nextOpName+"\"\n" +
                "\t\t\t\t]\n" +
                "\t\t\t},\n" +
                "\t\t\t\"coordinates\" : \"211,484\"\n" +
                "\t\t}";
        return json;
    }
    /**
     * GainRatioAttributeEval.
     *
     * @return a string of json
     */
    private String translateGainRatioAttributeEval(){
        String nextOpName = op.getNextOp().getName();
        String opName = op.getName();
        String json = "{\n" +
                "\t\t\t\"class\" : \"weka.knowledgeflow.steps.Filter\",\n" +
                "\t\t\t\"properties\" : {\n" +
                "\t\t\t\t\"filter\" : {\n" +
                "\t\t\t\t\t\"type\" : \"optionHandler\",\n" +
                "\t\t\t\t\t\"class\" : \"weka.filters.supervised.attribute.AttributeSelection\",\n" +
                "\t\t\t\t\t\"options\" : \"-E \\\"weka.attributeSelection.GainRatioAttributeEval \\\" -S " +
                "\\\"weka.attributeSelection.Ranker -T -1.7976931348623157E308 -N -1\\\"\"\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t\"name\" : \""+opName+"\"\n" +
                "\t\t\t},\n" +
                "\t\t\t\"connections\" : {\n" +
                "\t\t\t\t\"dataSet\" : [\n" +
                "\t\t\t\t\t\""+ nextOpName+"\"\n" +
                "\t\t\t\t]\n" +
                "\t\t\t},\n" +
                "\t\t\t\"coordinates\" : \"211,484\"\n" +
                "\t\t}";
        return json;
    }
    /**
     * InfoGain Attribute Eval.
     *
     * @return a string of json
     */
    private String translateInfoGainAttributeEval(){
        String nextOpName = op.getNextOp().getName();
        String opName = op.getName();
        String json = "{\n" +
                "\t\t\t\"class\" : \"weka.knowledgeflow.steps.Filter\",\n" +
                "\t\t\t\"properties\" : {\n" +
                "\t\t\t\t\"filter\" : {\n" +
                "\t\t\t\t\t\"type\" : \"optionHandler\",\n" +
                "\t\t\t\t\t\"class\" : \"weka.filters.supervised.attribute.AttributeSelection\",\n" +
                "\t\t\t\t\t\"options\" : \"-E \\\"weka.attributeSelection.InfoGainAttributeEval \\\" -S " +
                "\\\"weka.attributeSelection.Ranker -T -1.7976931348623157E308 -N -1\\\"\"\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t\"name\" : \""+opName+"\"\n" +
                "\t\t\t},\n" +
                "\t\t\t\"connections\" : {\n" +
                "\t\t\t\t\"dataSet\" : [\n" +
                "\t\t\t\t\t\""+ nextOpName+"\"\n" +
                "\t\t\t\t]\n" +
                "\t\t\t},\n" +
                "\t\t\t\"coordinates\" : \"211,484\"\n" +
                "\t\t}";
        return json;
    }
    /**
     * OneRAttributeEval.
     *
     * @return a string of json
     */
    private String translateOneRAttributeEval(){
        String nextOpName = op.getNextOp().getName();
        String opName = op.getName();
        String json = "{\n" +
                "\t\t\t\"class\" : \"weka.knowledgeflow.steps.Filter\",\n" +
                "\t\t\t\"properties\" : {\n" +
                "\t\t\t\t\"filter\" : {\n" +
                "\t\t\t\t\t\"type\" : \"optionHandler\",\n" +
                "\t\t\t\t\t\"class\" : \"weka.filters.supervised.attribute.AttributeSelection\",\n" +
                "\t\t\t\t\t\"options\" : \"-E \\\"weka.attributeSelection.OneRAttributeEval -S 1 -F 10 -B 6\\\" -S " +
                "\\\"weka.attributeSelection.Ranker -T -1.7976931348623157E308 -N -1\\\"\"\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t\"name\" : \""+opName+"\"\n" +
                "\t\t\t},\n" +
                "\t\t\t\"connections\" : {\n" +
                "\t\t\t\t\"dataSet\" : [\n" +
                "\t\t\t\t\t\""+ nextOpName+"\"\n" +
                "\t\t\t\t]\n" +
                "\t\t\t},\n" +
                "\t\t\t\"coordinates\" : \"211,484\"\n" +
                "\t\t}";
        return json;
    }
    /**
     * ReliefFAttributeEval.
     *
     * @return a string of json
     */
    private String translateReliefFAttributeEval(){
        String nextOpName = op.getNextOp().getName();
        String opName = op.getName();
        String json = "{\n" +
                "\t\t\t\"class\" : \"weka.knowledgeflow.steps.Filter\",\n" +
                "\t\t\t\"properties\" : {\n" +
                "\t\t\t\t\"filter\" : {\n" +
                "\t\t\t\t\t\"type\" : \"optionHandler\",\n" +
                "\t\t\t\t\t\"class\" : \"weka.filters.supervised.attribute.AttributeSelection\",\n" +
                "\t\t\t\t\t\"options\" : \"-E \\\"weka.attributeSelection.ReliefFAttributeEval -M -1 -D 1 -K 10\\\" -S " +
                "\\\"weka.attributeSelection.Ranker -T -1.7976931348623157E308 -N -1\\\"\"\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t\"name\" : \""+opName+"\"\n" +
                "\t\t\t},\n" +
                "\t\t\t\"connections\" : {\n" +
                "\t\t\t\t\"dataSet\" : [\n" +
                "\t\t\t\t\t\""+ nextOpName+"\"\n" +
                "\t\t\t\t]\n" +
                "\t\t\t},\n" +
                "\t\t\t\"coordinates\" : \"211,484\"\n" +
                "\t\t}";
        return json;
    }
    /**
     * SymmetricalUncertAttributeEval.
     *
     * @return a string of json
     */
    private String translateSymmetricalUncertAttributeEval(){
        String nextOpName = op.getNextOp().getName();
        String opName = op.getName();
        String json = "{\n" +
                "\t\t\t\"class\" : \"weka.knowledgeflow.steps.Filter\",\n" +
                "\t\t\t\"properties\" : {\n" +
                "\t\t\t\t\"filter\" : {\n" +
                "\t\t\t\t\t\"type\" : \"optionHandler\",\n" +
                "\t\t\t\t\t\"class\" : \"weka.filters.supervised.attribute.AttributeSelection\",\n" +
                "\t\t\t\t\t\"options\" : \"-E \\\"weka.attributeSelection.SymmetricalUncertAttributeEval \\\" -S " +
                "\\\"weka.attributeSelection.Ranker -T -1.7976931348623157E308 -N -1\\\"\"\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t\"name\" : \""+opName+"\"\n" +
                "\t\t\t},\n" +
                "\t\t\t\"connections\" : {\n" +
                "\t\t\t\t\"dataSet\" : [\n" +
                "\t\t\t\t\t\""+ nextOpName+"\"\n" +
                "\t\t\t\t]\n" +
                "\t\t\t},\n" +
                "\t\t\t\"coordinates\" : \"211,484\"\n" +
                "\t\t}";
        return json;
    }

    /**
     * WrapperSubsetEval.
     *
     * @return a string of json
     */
    private String translateWrapperSubsetEvalWithAdaboost(){
        String nextOpName = op.getNextOp().getName();
        String opName = op.getName();
        String json = "{\n" +
                "\t\t\t\"class\" : \"weka.knowledgeflow.steps.Filter\",\n" +
                "\t\t\t\"properties\" : {\n" +
                "\t\t\t\t\"filter\" : {\n" +
                "\t\t\t\t\t\"type\" : \"optionHandler\",\n" +
                "\t\t\t\t\t\"class\" : \"weka.filters.supervised.attribute.AttributeSelection\",\n" +
                "\t\t\t\t\t\"options\" : \"-E \\\"weka.attributeSelection.WrapperSubsetEval -B weka.classifiers.meta.AdaBoostM1 -F 5 -T 0.01 -R 1 -E AUC -- -P 100 -S " + this.seed + " -I 10 -W weka.classifiers.trees.DecisionStump\\\\\" -S " +
                "\\\"weka.attributeSelection.GreedyStepwise -T -1.7976931348623157E308 -N -1 -num-slots 1\\\"\"\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t\"name\" : \""+opName+"\"\n" +
                "\t\t\t},\n" +
                "\t\t\t\"connections\" : {\n" +
                "\t\t\t\t\"dataSet\" : [\n" +
                "\t\t\t\t\t\""+ nextOpName+"\"\n" +
                "\t\t\t\t]\n" +
                "\t\t\t},\n" +
                "\t\t\t\"coordinates\" : \"211,484\"\n" +
                "\t\t}";
        return json;
    }

    /**
     * WrapperSubsetEval.
     *
     * @return a string of json
     */
    private String translateWrapperSubsetEvalWithRandomforest(){
        String nextOpName = op.getNextOp().getName();
        String opName = op.getName();
        String json = "{\n" +
                "\t\t\t\"class\" : \"weka.knowledgeflow.steps.Filter\",\n" +
                "\t\t\t\"properties\" : {\n" +
                "\t\t\t\t\"filter\" : {\n" +
                "\t\t\t\t\t\"type\" : \"optionHandler\",\n" +
                "\t\t\t\t\t\"class\" : \"weka.filters.supervised.attribute.AttributeSelection\",\n" +
                "\t\t\t\t\t\"options\" : \"-E \\\"weka.attributeSelection.WrapperSubsetEval -B weka.classifiers.trees.RandomForest -F 5 -T 0.01 -R 1 -E AUC -- -P 100 -I 100 -num-slots 1 -K 0 -M 1.0 -V 0.001 -S " + this.seed + " \\\" -S " +
                "\\\"weka.attributeSelection.GreedyStepwise -T -1.7976931348623157E308 -N -1 -num-slots 1\\\"\"\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t\"name\" : \""+opName+"\"\n" +
                "\t\t\t},\n" +
                "\t\t\t\"connections\" : {\n" +
                "\t\t\t\t\"dataSet\" : [\n" +
                "\t\t\t\t\t\""+ nextOpName+"\"\n" +
                "\t\t\t\t]\n" +
                "\t\t\t},\n" +
                "\t\t\t\"coordinates\" : \"211,484\"\n" +
                "\t\t}";
        return json;
    }

    /**
     * Standardization.
     *
     * @return a string of json
     */
    private String translateStandardization(){
        String nextOpName = op.getNextOp().getName();
        String opName = op.getName();
        String json = "{\n" +
                "\t\t\t\"class\" : \"weka.knowledgeflow.steps.Filter\",\n" +
                "\t\t\t\"properties\" : {\n" +
                "\t\t\t\t\"filter\" : {\n" +
                "\t\t\t\t\t\"type\" : \"optionHandler\",\n" +
                "\t\t\t\t\t\"class\" : \"weka.filters.unsupervised.attribute.Standardize\",\n" +
                "\t\t\t\t\t\"options\" : \"\\\" -S " +
                "\\\"weka.attributeSelection.BestFirst -D 1 -N 5\\\"\"\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t\"name\" : \""+opName+"\"\n" +
                "\t\t\t},\n" +
                "\t\t\t\"connections\" : {\n" +
                "\t\t\t\t\"dataSet\" : [\n" +
                "\t\t\t\t\t\""+ nextOpName+"\"\n" +
                "\t\t\t\t]\n" +
                "\t\t\t},\n" +
                "\t\t\t\"coordinates\" : \"211,484\"\n" +
                "\t\t}";
        return json;
    }

    /**
     * Cross Validation Fold Maker
     *
     * @return a string of json
     */
    private String translateCrossValidationFoldMaker(){
        String nextOpName = op.getNextOp().getName();
        String opName = op.getName();
        String json = "{\n" +
                "\t\t\t\"class\" : \"weka.knowledgeflow.steps.CrossValidationFoldMaker\",\n" +
                "\t\t\t\"properties\" : {\n" +
                "\t\t\t\t\"name\" : \""+opName+"\",\n" +
                "\t\t\t\t\"numFolds\" : \"10\",\n" +
                "\t\t\t\t\"preserveOrder\" : false,\n" +
                "\t\t\t\t\"seed\" : \"" + this.seed +"\"\n" +
                "\t\t\t},\n" +
                "\t\t\t\"connections\" : {\n" +
                "\t\t\t\t\"trainingSet\" : [\n" +
                "\t\t\t\t\t\""+nextOpName+"\"\n" +
                "\t\t\t\t],\n" +
                "\t\t\t\t\"testSet\" : [\n" +
                "\t\t\t\t\t\""+nextOpName+"\"\n" +
                "\t\t\t\t]\n" +
                "\t\t\t},\n" +
                "\t\t\t\"coordinates\" : \"329,518\"\n" +
                "\t\t}";
        return json;
    }

    /**
     * Classifier Performance Evaluator
     *
     * @return a string of json
     */
    private String translateClassifierPerformanceEvaluator(){
        String nextOpName = op.getNextOp().getName();
        String opName = op.getName();
        String json = "{\n" +
                "\t\t\t\"class\" : \"weka.knowledgeflow.steps.ClassifierPerformanceEvaluator\",\n" +
                "\t\t\t\"properties\" : {\n" +
                "\t\t\t\t\"costMatrixString\" : \"\",\n" +
                "\t\t\t\t\"errorPlotPointSizeProportionalToMargin\" : false,\n" +
                "\t\t\t\t\"evaluateWithRespectToCosts\" : false,\n" +
                "\t\t\t\t\"evaluationMetricsToOutput\" : \"Correct,Incorrect,Kappa,Total cost,Average cost,KB relative," +
                                                        "KB information,Correlation,Complexity 0,Complexity scheme," +
                                                        "Complexity improvement,MAE,RMSE,RAE,RRSE,TP rate," +
                                                        "FP rate,Precision,Recall,F-measure,MCC,ROC area,PRC area\",\n" +
                "\t\t\t\t\"name\" : \""+opName+"\"\n" +
                "\t\t\t},\n" +
                "\t\t\t\"connections\" : {\n" +
                "\t\t\t\t\"text\" : [\n" +
                "\t\t\t\t\t\""+nextOpName+"\"\n" +
                "\t\t\t\t]\n" +
                "\t\t\t},\n" +
                "\t\t\t\"coordinates\" : \"643,464\"\n" +
                "\t\t}";
        return json;
    }

    /**
     * Remove feature
     *
     * @return a string of json
     */
    private String translateRemoveFeatureOperator(){
        List<String> paramString = new ArrayList<>();
        //-- convert feature indices to int
        for (DMParameter p : op.getParameters()){
            Double d = Double.parseDouble(p.getValue());
            int i = d.intValue();
            paramString.add(Integer.toString(i));
        }
        String nextOpName = op.getNextOp().getName();
        String classIndexes = String.join(",", paramString);
        String opName = this.op.getName();
        String json = "{\n" +
                "\t\t\t\"class\" : \"weka.knowledgeflow.steps.Filter\",\n" +
                "\t\t\t\"properties\" : {\n" +
                "\t\t\t\t\"filter\" : {\n" +
                "\t\t\t\t\t\"type\" : \"optionHandler\",\n" +
                "\t\t\t\t\t\"class\" : \"weka.filters.unsupervised.attribute.Remove\",\n" +
                "\t\t\t\t\t\"options\" : \"-R " + classIndexes + "\"\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t\"name\" : \""+opName+"\"\n" +
                "\t\t\t},\n" +
                "\t\t\t\"connections\" : {\n" +
                "\t\t\t\t\"dataSet\" : [\n" +
                "\t\t\t\t\t\"" + nextOpName +"\"\n" +
                "\t\t\t\t]\n" +
                "\t\t\t},\n" +
                "\t\t\t\"coordinates\" : \"240,333\"\n" +
                "\t\t}";
        return json;

    }

    /**
     * Replace Missing Value.
     *
     * @return a string of json
     */
    private String translateReplaceMissingValue(){
        String nextOpName = op.getNextOp().getName();
        String opName = op.getName();
        String json = "{\n" +
                "\t\t\t\"class\" : \"weka.knowledgeflow.steps.Filter\",\n" +
                "\t\t\t\"properties\" : {\n" +
                "\t\t\t\t\"filter\" : {\n" +
                "\t\t\t\t\t\"type\" : \"optionHandler\",\n" +
                "\t\t\t\t\t\"class\" : \"weka.filters.unsupervised.attribute.ReplaceMissingValues\",\n" +
                "\t\t\t\t\t\"options\" : \"\"\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t\"name\" : \""+opName+"\"\n" +
                "\t\t\t},\n" +
                "\t\t\t\"connections\" : {\n" +
                "\t\t\t\t\"dataSet\" : [\n" +
                "\t\t\t\t\t\""+nextOpName+"\"\n" +
                "\t\t\t\t]\n" +
                "\t\t\t},\n" +
                "\t\t\t\"coordinates\" : \"376,412\"\n" +
                "\t\t}";
        return json;

    }

}
