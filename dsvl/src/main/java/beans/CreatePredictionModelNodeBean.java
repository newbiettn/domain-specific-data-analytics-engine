package beans;

import javafx.util.Pair;

import java.util.ArrayList;

/**
 * Represents a SELECT node in DSVL.
 *
 * @author newbiettn
 * @since 2019-April-08
 */
public class CreatePredictionModelNodeBean extends ObjectBean{
    public static int count = 0;
    public CreatePredictionModelNodeBean(ArrayList<Pair<String, Class>> outputs) {
        super(count++, outputs);
        this.sparqlValue = "CREATE PREDICTION MODEL";
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
