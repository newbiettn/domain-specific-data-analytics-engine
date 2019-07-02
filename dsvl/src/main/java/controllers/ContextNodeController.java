package controllers;

import beans.ContextNodeBean;
import beans.TargetNodeBean;
import eu.mihosoft.vrl.workflow.VNode;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

/**
 * Controller for sparql nodes.
 */
public class ContextNodeController {
    private VNode node;
    private ContextNodeBean contextNodeBean;

    @FXML
    private HBox nodeHboxContainer;


    public ContextNodeController() {
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

    public ContextNodeBean getContextNodeBean() {
        return contextNodeBean;
    }

    public void setContextNodeBean(ContextNodeBean contextNodeBean) {
        this.contextNodeBean = contextNodeBean;
    }


}
