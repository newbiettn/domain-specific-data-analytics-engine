package controllers;

import eu.mihosoft.vrl.workflow.VNode;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import beans.SelectNodeBean;

/**
 * Controller for sparql nodes.
 */
public class SelectNodeController {
    private VNode node;
    private SelectNodeBean selectNodeBean;

    @FXML
    private HBox nodeHboxContainer;

    @FXML
    private TextField selectNodeTextField;

    public SelectNodeController() {
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

    public SelectNodeBean getSelectNodeBean() {
        return selectNodeBean;
    }

    public void setSelectNodeBean(SelectNodeBean selectNodeBean) {
        this.selectNodeBean = selectNodeBean;
    }

}
