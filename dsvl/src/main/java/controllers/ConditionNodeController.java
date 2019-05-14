package controllers;

import beans.ConditionNodeBean;
import eu.mihosoft.vrl.workflow.VNode;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import utils.TextUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    private TextField variableNodeTextField;

    public ConditionNodeController() {
    }

    @FXML
    public void initialize() {
        StringBuilder variable = new StringBuilder();
        EventHandler<KeyEvent> typingHandler = event -> {
            double desireWidth = TextUtils.computeTextWidth(variableNodeTextField.getFont(),
                    variableNodeTextField.getText(), 0.0D) + 25;
            variableNodeTextField.setPrefWidth(desireWidth);
            node.setWidth(desireWidth+30);
            nodeHboxContainer.setPrefWidth(desireWidth+20);
            nodeBorderPaneContainer.setPrefWidth(desireWidth);
            variable.append(event.getText());
            conditionNodeBean.setVariable(variable.toString());
            logger.info(variable.toString());
        };
        variableNodeTextField.addEventFilter(KeyEvent.KEY_PRESSED, typingHandler);
    }

    public void reloadVariable(){
        variableNodeTextField.setText(conditionNodeBean.getVariable());

    }

    public VNode getNode() { return node; }
    public void setNode(VNode node) { this.node = node; }
    public ConditionNodeBean getConditionNodeBean() { return conditionNodeBean; }
    public void setConditionNodeBean(ConditionNodeBean conditionNodeBean) { this.conditionNodeBean = conditionNodeBean; }

}
