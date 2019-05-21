package controllers;

import beans.FeatureNodeBean;
import beans.TargetNodeBean;
import eu.mihosoft.vrl.workflow.VNode;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

/**
 * Controller for sparql nodes.
 */
public class FeatureNodeController {
    private VNode node;
    private FeatureNodeBean featureNodeBean;

    @FXML
    private HBox nodeHboxContainer;

    @FXML
    private TextField selectNodeTextField;

    public FeatureNodeController() {
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

    public FeatureNodeBean getFeatureNodeBean() {
        return featureNodeBean;
    }

    public void setFeatureNodeBean(FeatureNodeBean featureNodeBean) {
        this.featureNodeBean = featureNodeBean;
    }

}
