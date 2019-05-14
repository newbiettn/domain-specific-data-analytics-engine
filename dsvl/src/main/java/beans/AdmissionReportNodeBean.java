package beans;

import javafx.util.Pair;

import java.util.ArrayList;

/**
 * Describe class purpose here.
 *
 * @author Ngoc Tran
 * @since 2019-05-14
 */
public class AdmissionReportNodeBean extends ObjectBean{
    private static int count = 0;
    public AdmissionReportNodeBean(ArrayList<Pair<String, Class>> outputs) {
        super(count++, outputs);
        this.sparqlValue = "admissionReport";
        this.maxOutputConn = 1;
        this.maxInputConn = 0;
        this.minOutputConn = 1;
        this.minInputConn = 0;
    }
}
