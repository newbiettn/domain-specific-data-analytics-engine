package controllers;

import beans.SelectNodeBean;
import beans.TargetNodeBean;
import eu.mihosoft.vrl.workflow.VNode;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

/**
 * Controller for sparql nodes.
 */
public class TargetNodeController {
    private VNode node;
    private TargetNodeBean targetNodeBean;

    @FXML
    private HBox nodeHboxContainer;

    @FXML
    private TextField selectNodeTextField;

    public TargetNodeController() {
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

    public TargetNodeBean getTargetNodeBean() {
        return targetNodeBean;
    }

    public void setTargetNodeBean(TargetNodeBean targetNodeBean) {
        this.targetNodeBean = targetNodeBean;
    }


}
