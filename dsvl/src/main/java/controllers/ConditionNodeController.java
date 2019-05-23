package controllers;

import beans.ConditionNodeBean;
import eu.mihosoft.vrl.workflow.VNode;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.TextUtils;

/**
 * Controller for sparql nodes.
 */
public class ConditionNodeController {
    private static Logger logger = LoggerFactory.getLogger(ConditionNodeController.class);
    private VNode node;
    private ConditionNodeBean conditionNodeBean;

    @FXML
    private HBox nodeHboxContainer;

    @FXML
    private BorderPane nodeBorderPaneContainer;

    @FXML
    private ChoiceBox<String> cbOperator;

    @FXML
    private ChoiceBox<String> cbValue;

    public ConditionNodeController() {
    }

    @FXML
    public void initialize() {
    }


    public VNode getNode() { return node; }
    public void setNode(VNode node) { this.node = node; }
    public ConditionNodeBean getConditionNodeBean() { return conditionNodeBean; }
    public void setConditionNodeBean(ConditionNodeBean conditionNodeBean) { this.conditionNodeBean = conditionNodeBean; }

    public ChoiceBox<String> getCbOperator() {
        return cbOperator;
    }

    public void setCbOperator(ChoiceBox<String> cbOperator) {
        this.cbOperator = cbOperator;
    }

    public ChoiceBox<String> getCbValue() {
        return cbValue;
    }

    public void setCbValue(ChoiceBox<String> cbValue) {
        this.cbValue = cbValue;
    }

}
