package controllers;

import beans.PatientNodeBean;
import eu.mihosoft.vrl.workflow.VNode;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

/**
 * Controller for sparql nodes.
 */
public class PatientNodeController {
    private VNode node;
    private PatientNodeBean patientNodeBean;

    @FXML
    private HBox nodeHboxContainer;

    @FXML
    private TextField variablePatientNode;

    public PatientNodeController() {
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

    public PatientNodeBean getSelectNodeBean() {
        return patientNodeBean;
    }

    public void setSelectNodeBean(PatientNodeBean patientNodeBean) {
        this.patientNodeBean = patientNodeBean;
    }

}
