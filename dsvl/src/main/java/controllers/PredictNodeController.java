package controllers;

import beans.PredictNodeBean;
import beans.SelectNodeBean;
import eu.mihosoft.vrl.workflow.VNode;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

/**
 * Controller for sparql nodes.
 */
public class PredictNodeController {
    private VNode node;
    private PredictNodeBean predictNodeBean;

    @FXML
    private HBox nodeHboxContainer;

    @FXML
    private TextField selectNodeTextField;

    public PredictNodeController() {
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

    public PredictNodeBean getPredictNodeBean() {
        return predictNodeBean;
    }

    public void setPredictNodeBean(PredictNodeBean predictNodeBean) {
        this.predictNodeBean = predictNodeBean;
    }

}
