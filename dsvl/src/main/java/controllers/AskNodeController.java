package controllers;

import beans.AskNodeBean;
import beans.SelectNodeBean;
import eu.mihosoft.vrl.workflow.VNode;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

/**
 * Controller for sparql nodes.
 */
public class AskNodeController {
    private VNode node;
    private AskNodeBean askNodeBean;

    @FXML
    private HBox nodeHboxContainer;

    @FXML
    private TextField selectNodeTextField;

    public AskNodeController() {
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

    public AskNodeBean getAskNodeBean() {
        return askNodeBean;
    }

    public void setAskNodeBean(AskNodeBean askNodeBean) {
        this.askNodeBean = askNodeBean;
    }

}
