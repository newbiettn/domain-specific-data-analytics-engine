package beans;

import javafx.util.Pair;

import java.util.ArrayList;

/**
 * Represents a CONEXT node in DSVL.
 *
 * @author newbiettn
 * @since 2019-April-08
 */
public class ContextNodeBean extends ObjectBean{
    public static int count = 0;
    public ContextNodeBean(ArrayList<Pair<String, Class>> outputs) {
        super(count++, outputs);
        this.sparqlValue = "CONTEXT";
        this.maxOutputConn = 1;
        this.maxInputConn = 0;
        this.minOutputConn = 1;
        this.minInputConn = 0;
    }

    @Override
    public String getSparqlValue() {
        return this.sparqlValue;
    }
}
