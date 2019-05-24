package beans;

import config.DataType;

/**
 * Represent the Variable node.
 *
 * @author Ngoc Tran
 * @since 2019-03-26
 */
public class ConditionNodeBean extends ObjectBean{
    public static int count = 0;
    private String condition;
    private DataType dataType;

    public ConditionNodeBean() {
        this.id = count++;
        this.condition = "";
        this.outputs = null;
        this.sparqlValue = "condition";
        this.maxOutputConn = 0;
        this.maxInputConn = 1;
        this.minOutputConn = 0;
        this.minInputConn = 1;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public DataType getDataType() {
        return dataType;
    }

    public void setDataType(DataType dataType) {
        this.dataType = dataType;
    }
}
