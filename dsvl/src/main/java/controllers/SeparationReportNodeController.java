package controllers;

import beans.AdmissionReportNodeBean;
import beans.SeparationReportNodeBean;
import eu.mihosoft.vrl.workflow.VNode;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

/**
 * Controller for sparql nodes.
 */
public class SeparationReportNodeController {
    private VNode node;
    private SeparationReportNodeBean separationReportNodeBean;

    @FXML
    private HBox nodeHboxContainer;

    @FXML
    private TextField variablePatientNode;

    public SeparationReportNodeController() {
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

    public SeparationReportNodeBean getSeparationReportNodeBean() {
        return separationReportNodeBean;
    }

    public void setSeparationReportNodeBean(SeparationReportNodeBean separationReportNodeBean) {
        this.separationReportNodeBean = separationReportNodeBean;
    }


}
