package beans;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.util.Pair;

import java.util.ArrayList;

/**
 * Describe class purpose here.
 *
 * @author Ngoc Tran
 * @since 2019-04-08
 */
public class ObjectBean {
    protected String variable;
    protected ObservableList<Pair<String, Class>> outputs = FXCollections.observableArrayList();

    protected int maxOutputConn;
    protected int maxInputConn;
    protected int minOutputConn;
    protected int minInputConn;

    public ObjectBean(ArrayList<Pair<String, Class>> outputs){
        for (Pair<String, Class> o: outputs){
            this.outputs.add(o);
        }
    }

    public String getVariable() {
        return variable;
    }

    public void setVariable(String variable) {
        this.variable = variable;
    }

    public ObservableList<Pair<String, Class>> getOutputs() {
        return outputs;
    }

    public void setOutputs(ObservableList<Pair<String, Class>> outputs) {
        this.outputs = outputs;
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
