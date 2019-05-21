package controllers;

import beans.CreatePredictionModelNodeBean;
import beans.SelectNodeBean;
import eu.mihosoft.vrl.workflow.VNode;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

/**
 * Controller for sparql nodes.
 */
public class CreatePredictionModelNodeController {
    private VNode node;
    private CreatePredictionModelNodeBean createPredictionModelNodeBean;

    @FXML
    private HBox nodeHboxContainer;

    @FXML
    private TextField selectNodeTextField;

    public CreatePredictionModelNodeController() {
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

    public CreatePredictionModelNodeBean getCreatePredictionModelNodeBean() {
        return createPredictionModelNodeBean;
    }

    public void setCreatePredictionModelNodeBean(CreatePredictionModelNodeBean createPredictionModelNodeBean) {
        this.createPredictionModelNodeBean = createPredictionModelNodeBean;
    }


}
