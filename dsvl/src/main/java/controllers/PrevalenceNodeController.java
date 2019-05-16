package controllers;

import beans.PrevalenceNodeBean;
import beans.SelectNodeBean;
import eu.mihosoft.vrl.workflow.VNode;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

/**
 * Controller for sparql nodes.
 */
public class PrevalenceNodeController {
    private VNode node;
    private PrevalenceNodeBean prevalenceNodeBean;

    @FXML
    private HBox nodeHboxContainer;

    @FXML
    private TextField selectNodeTextField;

    public PrevalenceNodeController() {
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

    public PrevalenceNodeBean getPrevalenceNodeBean() {
        return prevalenceNodeBean;
    }

    public void setPrevalenceNodeBean(PrevalenceNodeBean prevalenceNodeBean) {
        this.prevalenceNodeBean = prevalenceNodeBean;
    }

}
