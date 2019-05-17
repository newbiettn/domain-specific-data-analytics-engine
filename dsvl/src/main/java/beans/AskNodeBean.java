package beans;

import javafx.util.Pair;

import java.util.ArrayList;

/**
 * Represents a ASK node in DSVL.
 *
 * @author Ngoc Tran
 * @since 2019-05-16
 */
public class AskNodeBean extends ObjectBean{
    public static int count = 0;
    public AskNodeBean(ArrayList<Pair<String, Class>> outputs) {
        super(count++, outputs);
        this.sparqlValue = "ASK";
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
