package beans;

/**
 * Represents a SELECT node in DSVL.
 *
 * @author newbiettn
 * @since 2019-April-08
 */
public class SelectNodeBean {

    private String value;
    private int maxOutputConn = 1;
    private int maxInputConn = 0;
    private int minOutputConn = 1;
    private int minInputConn = 0;

    public SelectNodeBean(){
        value = "";
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getMaxOutputConn() {
        return maxOutputConn;
    }

    public void setMaxOutputConn(int maxOutputConn) {
        this.maxOutputConn = maxOutputConn;
    }

    public int getMaxInputConn() {
        return maxInputConn;
    }

    public void setMaxInputConn(int maxInputConn) {
        this.maxInputConn = maxInputConn;
    }

    public int getMinOutputConn() {
        return minOutputConn;
    }

    public void setMinOutputConn(int minOutputConn) {
        this.minOutputConn = minOutputConn;
    }

    public int getMinInputConn() {
        return minInputConn;
    }

    public void setMinInputConn(int minInputConn) {
        this.minInputConn = minInputConn;
    }
}
