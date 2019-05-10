package beans;

import javafx.util.Pair;

import java.util.ArrayList;

/**
 * Represents a SELECT node in DSVL.
 *
 * @author newbiettn
 * @since 2019-April-08
 */
public class SelectNodeBean extends ObjectBean{
    public SelectNodeBean(int id, ArrayList<Pair<String, Class>> outputs) {
        super(id, outputs);
        this.sparqlValue = "SELECT";
        this.maxOutputConn = 1;
        this.maxInputConn = 0;
        this.minOutputConn = 1;
        this.minInputConn = 0;
    }
}
