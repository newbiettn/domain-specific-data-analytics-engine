package planner.translator;
/*
 *
 * @project diabetes-engine
 * @author newbiettn on 26/2/18
 *
 */

import model.DMComponentDecorator;
import model.DMOperator;
import model.DMParameter;
import model.DMPlan;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PlanTranslator extends DMComponentDecorator{
    private DMPlan plan;
    private int seed;
    private String filePath;

    public PlanTranslator(int seed, String filePath) {
        this.seed = seed;
        this.filePath = filePath;
    }
    public PlanTranslator() {
    }

    /**
     * A general method to preprocess a raw plan before translating it.
     * 1. Group missing value handling operators.
     * 2. Set edge from current operators to their next operators.
     *
     * @param rawPlan a raw plan exported from JSHOP2
     * @return a preprocessed plan
     */
    public DMPlan preprocessPlan(DMPlan rawPlan){
        DMPlan preprocessedPlan = groupMissingValueHandlingOperators(rawPlan);
        preprocessedPlan = setEdgesBetweenOperators(preprocessedPlan);
        return preprocessedPlan;
    }
    /**
     * Before translate the plan, we need to preprocess plan in an order that can be easily output.
     * 1. Group replace-missing-value-operator of individual features into a single operator
     * 2. Group remove-feature-operators of individual features into a single operator
     * 3. Put grouped remove-feature-operator BEFORE replace-missing-value-operator in the action list.
     *
     * In order to do that, we have constraints them
     * 1. replace-missing-value-operator and remove-feature-operator must follow either of both.
     * 2. they are called only after class-assigner-operator, which would be once.
     *
     * @param originPlan the raw plan retrieved from JSHOP2
     * @return a preprocessed plan with correct order for translating
     */
    public DMPlan groupMissingValueHandlingOperators(DMPlan originPlan){
        DMPlan newPlan = new DMPlan();
        DMOperator groupedReplaceMissingValueOp = new DMOperator();
        DMOperator groupedRemoveMissingValueOp = new DMOperator();
        groupedReplaceMissingValueOp.setType(DMOperator.OperatorType.REPLACE_MISSING_VALUE);
        groupedReplaceMissingValueOp.setName("ReplaceMissingValues");
        groupedRemoveMissingValueOp.setType(DMOperator.OperatorType.REMOVE_FEATURE);
        groupedRemoveMissingValueOp.setName("Remove");
        List<DMOperator> operatorsToBeRemoved = new ArrayList<>();

        //-- grouping
        Iterator<DMOperator> iter = originPlan.getOperators().iterator();
        while (iter.hasNext()){
            DMOperator op = iter.next();
            if (op.getParameters().size() > 0){
                String className = op.getParameters().get(0).getValue();
                DMParameter param = new DMParameter("classname", className);
                //-- group replace-missing-value-operator
                if (op.getType() == DMOperator.OperatorType.REPLACE_MISSING_VALUE){
                    groupedReplaceMissingValueOp.addParameter(param);
                    operatorsToBeRemoved.add(op);
                    iter.remove();
                }
                //-- group remove-feature-operator
                else if (op.getType() == DMOperator.OperatorType.REMOVE_FEATURE){
                    groupedRemoveMissingValueOp.addParameter(param);
                    operatorsToBeRemoved.add(op);
                    iter.remove();
                }
            }
        }

        //-- forming a new preprocessed plan
        if ((groupedRemoveMissingValueOp.getParameters().size() > 0) ||
                (groupedReplaceMissingValueOp.getParameters().size() > 0)){
            for (DMOperator op : originPlan.getOperators()){
                newPlan.addOperator(op);
                //-- add grouped operators after class-assigner-operator
                if (op.getType() == DMOperator.OperatorType.CLASS_ASSIGNER){
                    if (groupedRemoveMissingValueOp.getParameters().size() > 0)
                        newPlan.addOperator(groupedRemoveMissingValueOp);
                    if (groupedReplaceMissingValueOp.getParameters().size() > 0)
                        newPlan.addOperator(groupedReplaceMissingValueOp);
                }
            }
            return newPlan;
        } else {
            return originPlan;
        }
    }

    /**
     * Connect operators with its next operators, except the last operator.
     *
     * @param plan plan without edges between operator
     * @return
     */
    public DMPlan setEdgesBetweenOperators(DMPlan plan){
        List<DMOperator> ops = plan.getOperators();
        //-- minus the last result-viewer-operator
        int size = ops.size() - 1;
        for (int i = 0; i < size; i++){
            DMOperator currentOp = ops.get(i);
            DMOperator nextOp = ops.get(i+1);
            currentOp.setNextOp(nextOp);
        }
//        plan.setOperators(ops);
        return plan;
    }

    /**
     * Translate plan from DMPlan to JSON
     *
     * @return a string of json
     */
    public String translate(){
        this.plan = (DMPlan) this.com;
        plan = preprocessPlan(plan);
        String json = "{\n" +
                "\t\"flow_name\" : \"ex1\",\n" +
                "\t\"steps\" : [\n\t\t";
        for (int i = 0; i < plan.getOperators().size(); i++){
            OperatorTranslator opTranslator = new OperatorTranslator();
            DMOperator op = plan.getOperators().get(i);
            opTranslator.setTheComponent(op);
            json += opTranslator.translate(this.seed, this.filePath, false);
            if (i < (plan.getOperators().size() - 1)) {
                json += ",\n\t\t";
            }
        }
        json += "\n\t]\n" +
                "}\n";
        return json;
    }
    /**
     * Translate plan from DMPlan to JSON
     *
     * @return a string of json
     */
    public String translateWithOptimization(){
        this.plan = (DMPlan) this.com;
        plan = preprocessPlan(plan);
        String json = "{\n" +
                "\t\"flow_name\" : \"ex1\",\n" +
                "\t\"steps\" : [\n\t\t";
        for (int i = 0; i < plan.getOperators().size(); i++){
            OperatorTranslator opTranslator = new OperatorTranslator();
            DMOperator op = plan.getOperators().get(i);
            opTranslator.setTheComponent(op);
            json += opTranslator.translate(this.seed, this.filePath, true);
            if (i < (plan.getOperators().size() - 1)) {
                json += ",\n\t\t";
            }
        }
        json += "\n\t]\n" +
                "}\n";
        return json;
    }
    public String translateMultiflowFooter(){
        String json = "\n\t]\n" +
                "}\n";
        return json;
    }
    public String translateMultiflowHeader(){
        String json = "{\n" +
                "\t\"flow_name\" : \"ex1\",\n" +
                "\t\"steps\" : [\n\t\t";
        return json;
    }
    public String translateMultiFlowBody(int flowIndex, int lastIndex){
        this.plan = (DMPlan) this.com;
        plan = preprocessPlan(plan);
        for (int i = 0; i < plan.getOperators().size(); i++){
            DMOperator op = plan.getOperators().get(i);
            String newOperatorName = op.getName() + flowIndex;
            op.setName(newOperatorName);
        }
        String json = "";
        for (int i = 0; i < plan.getOperators().size(); i++){
            OperatorTranslator opTranslator = new OperatorTranslator();
            DMOperator op = plan.getOperators().get(i);
            opTranslator.setTheComponent(op);
            json += opTranslator.translate(this.seed, this.filePath, false);
            if ((i == (plan.getOperators().size()- 1)) && flowIndex == lastIndex) {
                json += "\n\t\t";
            } else{
                json += ",\n\t\t";
            }
        }
        return json;
    }
}
