package beans;

/**
 * Represent the Variable node.
 *
 * @author Ngoc Tran
 * @since 2019-03-26
 */
public class ConditionNodeBean extends ObjectBean{
    public static int count = 0;
    private String variable;

    public ConditionNodeBean() {
        this.id = count++;
        this.variable = "";
        this.outputs = null;
        this.sparqlValue = "condition";
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
