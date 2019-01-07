package model;
/**
 * Class of parameters for DM operators.
 *
 * @project diabetes-engine
 * @author newbiettn on 26/2/18
 *
 */

public class DMParameter {
    //-- variables
    private String name;
    private String value;
    private DMOperator operator;

    //-- constructors
    public DMParameter(String name, String value) {
        this.name = name;
        this.value = value;
    }

    //-- getters & setters
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setOperator(DMOperator operator) {
        this.operator = operator;
    }

    public DMOperator getOperator() {
        return operator;
    }
}
