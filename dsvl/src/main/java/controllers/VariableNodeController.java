package controllers;

import beans.VariableNodeBean;
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
public class VariableNodeController {
    private static Logger logger = LoggerFactory.getLogger(VariableNodeController.class);
    private VNode node;
    private VariableNodeBean variableNodeBean;

    @FXML
    private HBox nodeHboxContainer;

    @FXML
    private BorderPane nodeBorderPaneContainer;

    @FXML
    private TextField variableNodeTextField;

    public VariableNodeController() {
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
            variableNodeBean.setVariable(variable.toString());
            logger.info(variable.toString());
        };
        variableNodeTextField.addEventFilter(KeyEvent.KEY_PRESSED, typingHandler);
    }

    public void reloadVariable(){
        variableNodeTextField.setText(variableNodeBean.getVariable());

    }

    public VNode getNode() { return node; }
    public void setNode(VNode node) { this.node = node; }
    public VariableNodeBean getVariableNodeBean() { return variableNodeBean; }
    public void setVariableNodeBean(VariableNodeBean variableNodeBean) { this.variableNodeBean = variableNodeBean; }

}
