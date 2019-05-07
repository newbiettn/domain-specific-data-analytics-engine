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
    public PatientNodeBean(ArrayList<Pair<String, Class>> outputs) {
        super(outputs);
        this.variable = null;
        this.maxOutputConn = 1;
        this.maxInputConn = 0;
        this.minOutputConn = 1;
        this.minInputConn = 0;
    }
}
