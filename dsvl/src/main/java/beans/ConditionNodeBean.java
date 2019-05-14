package beans;

/**
 * Represent the Variable node.
 *
 * @author Ngoc Tran
 * @since 2019-03-26
 */
public class ConditionNodeBean extends ObjectBean{
    private String variable;

    public ConditionNodeBean(int id) {
        this.id = id;
        this.variable = "";
        this.outputs = null;
        this.sparqlValue = null;
        this.maxOutputConn = 0;
        this.maxInputConn = 1;
        this.minOutputConn = 0;
        this.minInputConn = 1;
    }

    public String getVariable() {
        return variable;
    }

    public void setVariable(String variable) {
        this.variable = variable;
        this.sparqlValue = variable;
    }
}
