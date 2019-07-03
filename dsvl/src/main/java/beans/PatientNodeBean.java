package beans;


import javafx.util.Pair;

import java.util.ArrayList;

/**
 * Represent a Patient node in DSVL.
 *
 * @author newbiettn
 * @since 2019-March-01
 */
public class PatientNodeBean extends ObjectBean{
    public static int count = 0;
    public PatientNodeBean(ArrayList<Pair<String, Class>> outputs) {
        super(count++, outputs);
        this.sparqlValue = "patient";
        this.SPARQLClass = "diab:Patient";
        this.maxOutputConn = 1;
        this.maxInputConn = 0;
        this.minOutputConn = 1;
        this.minInputConn = 0;
    }
}
