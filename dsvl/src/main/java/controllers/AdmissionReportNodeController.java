package controllers;

import beans.AdmissionReportNodeBean;
import beans.PatientNodeBean;
import eu.mihosoft.vrl.workflow.VNode;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

/**
 * Controller for sparql nodes.
 */
public class AdmissionReportNodeController {
    private VNode node;
    private AdmissionReportNodeBean admissionReportNodeBean;

    @FXML
    private HBox nodeHboxContainer;

    @FXML
    private TextField variablePatientNode;

    public AdmissionReportNodeController() {
    }

    @FXML
    public void initialize() {
    }

    public VNode getNode() {
        return node;
    }

    public void setNode(VNode node) {
        this.node = node;
    }

    public AdmissionReportNodeBean getAdmissionReportNodeBean() {
        return admissionReportNodeBean;
    }

    public void setAdmissionReportNodeBean(AdmissionReportNodeBean admissionReportNodeBean) {
        this.admissionReportNodeBean = admissionReportNodeBean;
    }

}
