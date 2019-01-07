package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Class of operators in DM plans.
 *
 * @project diabetes-engine
 * @author newbiettn on 26/2/18
 *
 */
public class DMOperator extends DMComponent{
    //-- variables
    OperatorType type;
    private List<DMParameter> parameters = new ArrayList<>();
    private DMPlan plan;
    private DMOperator nextOp;
    private String name;

    @Override
    public String translate(int seed, String filePath, boolean performOptimization) {
        return "";
    }

    //-- enum
    public enum OperatorType {
        //-- data loader
        LOAD_CSV("load-csv-operator"),
        LOAD_ARFF("load-arff-operator"),

        //-- shuffle data
        RANDOMIZE("randomize-operator"),

        //-- class assigner
        CLASS_ASSIGNER("class-assigner-operator"),

        //-- preprocessing
        REPLACE_MISSING_VALUE("replace-missing-value-operator"),
        REMOVE_FEATURE("remove-feature-operator"),

        //-- attribute selection
        ATTRIBUTE_SELECTION("attribute-selection-operator"),
        CFS_SUBSET_EVAL("cfs-subset-eval-operator"),
        CORRELATION_ATTR_EVAL("correlation-attribute-eval-operator"),
        GAIN_RATIO_ATTR_EVAL("gain-ratio-attribute-eval-operator"),
        INFO_GAIN_ATTR_EVAL("info-gain-attribute-eval-operator"),
        RELIEFF_ATTR_EVAL("relieff-attribute-eval-operator"),
        SYMMETRICAL_UNCERT_ATTR_EVAL("symmetrical-uncert-attribute-eval-operator"),
        WRAPPER_SUBSET_EVAL_WITH_RF("wrapper-subset-eval-with-rf-operator"),
        WRAPPER_SUBSET_EVAL_WITH_ADABOOST("wrapper-subset-eval-with-adaboost-operator"),

        //-- standardization
        STANDARDIZATION("standardization-operator"),

        //-- evaluation
        CROSS_VALIDATION_FOLD_MAKER("cross-validation-fold-maker-operator"),

        //-- classifier
        J48("j48-operator"),
        RANDOM_FOREST("randomforest-operator"),
        NEURAL_NETWORK("neuralnetwork-operator"),
        ADABOOST_M1("adaboostm1-operator"),
        LOGISTIC("logistic-operator"),
        SMO("smo-operator"),
        KSTAR("kstar-operator"),
        IBK("ibk-operator"),
        LWL("lwl-operator"),
        BAGGING("bagging-operator"),
        LOGITBOOST("logitboost-operator"),
        RANDOM_SUBSPACE("randomsubspace-operator"),
        STACKING("stacking-operator"),
        VOTE("vote-operator"),
        DECISION_TABLE("decisiontable-operator"),

        //-- results
        RESULT_VIEWER("result-viewer-operator"),
        CLASSIFIER_PERFORMANCE_EVALUATOR("classifier-performance-evaluator-operator");

        private String value;

        OperatorType(String value) {
            this.value = value;
        }

        public String toString() {
            return value;
        }
    }

    //-- getters & setters
    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public DMOperator getNextOp() { return nextOp; }

    public void setNextOp(DMOperator nextOp) { this.nextOp = nextOp; }

    public void setType(OperatorType type) { this.type = type; }

    public OperatorType getType() { return type; }

    public DMPlan getPlan() { return plan; }

    public void setPlan(DMPlan plan) { this.plan = plan; }

    public void setParameters(List<DMParameter> parameters) {
        List<DMParameter> parametersClone = new ArrayList<DMParameter>(this.getParameters());
        for (DMParameter parameter : parametersClone) {
            removeParameter(parameter);
        }
        for (DMParameter parameter : parameters) {
            addParameter(parameter);
        }
    }
    public List<DMParameter> getParameters() {
        return parameters;
    }
    public void addParameter(DMParameter parameter) {
        parameters.add(parameter);
        parameter.setOperator(this);
    }

    public void removeParameter(DMParameter parameter) {
        parameters.remove(parameter);
        parameter.setOperator(null);
    }
}
