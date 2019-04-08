package beans;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;

/**
 * Describe class purpose here.
 *
 * @author Ngoc Tran
 * @since 2019-04-08
 */
public class ObjectBean {
    private String variable;
    private ObservableList<String> connNames = FXCollections.observableArrayList();
    private int maxOutputConn;
    private int maxInputConn;
    private int minOutputConn;
    private int minInputConn;

    public ObjectBean(ArrayList<String> cns){
        for (String cn : cns){
            connNames.add(cn);
        }
    }

    public String getVariable() {
        return variable;
    }

    public void setVariable(String variable) {
        this.variable = variable;
    }

    public ObservableList<String> getConnNames() {
        return connNames;
    }

    public void setConnNames(ObservableList<String> connNames) {
        this.connNames = connNames;
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
