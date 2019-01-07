package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Class of DM plans.
 *
 * @project diabetes-engine
 * @author newbiettn on 26/2/18
 *
 */
public class DMPlan extends DMComponent{
    //-- variables
    private String name;
    private List<DMOperator> operators = new ArrayList<DMOperator>();

    //-- getters & setters
    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setOperators(List<DMOperator> operators) {
        List<DMOperator> operatorClone = new ArrayList<DMOperator>(this.getOperators());
        for (DMOperator op : operatorClone) {
            removeOperator(op);
        }
        for (DMOperator op: operators) {
            addOperator(op);
        }
    }
    public List<DMOperator> getOperators() {
        return operators;
    }
    public void addOperator(DMOperator op) {
        this.operators.add(op);
        op.setPlan(this);
    }

    public void removeOperator(DMOperator op) {
        this.operators.remove(op);
        op.setPlan(null);
    }

    @Override
    public String translate(int seed, String filePath, boolean performOptimization) {
        return null;
    }
}
