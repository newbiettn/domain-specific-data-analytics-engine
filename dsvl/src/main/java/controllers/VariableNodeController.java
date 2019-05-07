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

/**
 * Controller for sparql nodes.
 */
public class VariableNodeController {
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
        EventHandler<KeyEvent> typingHandler = event -> {
            double desireWidth = TextUtils.computeTextWidth(variableNodeTextField.getFont(),
                    variableNodeTextField.getText(), 0.0D) + 25;
            variableNodeTextField.setPrefWidth(desireWidth);
            System.out.println(node != null);
            node.setWidth(desireWidth+30);
            nodeHboxContainer.setPrefWidth(desireWidth+20);
            nodeBorderPaneContainer.setPrefWidth(desireWidth);
            variableNodeBean.setVariable(variableNodeTextField.getText());
        };
        variableNodeTextField.addEventFilter(KeyEvent.KEY_PRESSED, typingHandler);
    }

    public VNode getNode() { return node; }
    public void setNode(VNode node) { this.node = node; }
    public VariableNodeBean getVariableNodeBean() { return variableNodeBean; }
    public void setVariableNodeBean(VariableNodeBean variableNodeBean) { this.variableNodeBean = variableNodeBean; }

}
