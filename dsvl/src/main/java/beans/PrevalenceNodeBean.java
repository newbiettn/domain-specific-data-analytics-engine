package beans;

import javafx.util.Pair;

import java.util.ArrayList;

/**
 * Describe class purpose here.
 *
 * @author Ngoc Tran
 * @since 2019-05-16
 */
public class PrevalenceNodeBean extends ObjectBean{
    public static int count = 0;
    public PrevalenceNodeBean(ArrayList<Pair<String, Class>> outputs) {
        super(count++, outputs);
        this.sparqlValue = "PREVALENCE";
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
