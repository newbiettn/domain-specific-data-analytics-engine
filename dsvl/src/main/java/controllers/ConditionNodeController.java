package controllers;

import beans.ConditionNodeBean;
import config.DataType;
import eu.mihosoft.vrl.workflow.VNode;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.TextUtils;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller for sparql nodes.
 */
public class ConditionNodeController implements Initializable {
    private static Logger logger = LoggerFactory.getLogger(ConditionNodeController.class);
    private VNode node;
    private ConditionNodeBean conditionNodeBean;

    private HBox root;

    private BorderPane conditionNodeBorderPane;

    private ChoiceBox<String> cbOperator;

    private ChoiceBox<String> cbValue;

    private TextField textFieldValue ;

    public ConditionNodeController() {
        root = new HBox();
        conditionNodeBorderPane = new BorderPane();
        HBox.setHgrow(conditionNodeBorderPane, Priority.ALWAYS);
        conditionNodeBorderPane.setPrefWidth(50);
        cbOperator = new ChoiceBox<>();
        cbOperator.setPrefWidth(27);
        cbOperator.setPrefWidth(75);
        cbOperator.setVisible(false);
        cbValue = new ChoiceBox<>();
        textFieldValue = new TextField();

        conditionNodeBorderPane.setLeft(cbOperator);
        root.getChildren().setAll(conditionNodeBorderPane);
        conditionNodeBorderPane.setPadding(new Insets(10, 0, 0, 0));
    }

    @FXML
    public void initialize() {
        cbOperator.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (newValue != null) {
                    DataType.Type type = conditionNodeBean.getDataType().getType();
                    if (type == DataType.Type.NUMERIC)
                        conditionNodeBean.setCondition(newValue + " " + cbValue.getValue());
                    else if (type == DataType.Type.CATEGORY)
                        conditionNodeBean.setCondition(newValue + " \'" + cbValue.getValue() + "\'");
                }
            }
        });
        cbValue.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (newValue != null) {
                    DataType.Type type = conditionNodeBean.getDataType().getType();
                    if (type == DataType.Type.NUMERIC)
                        conditionNodeBean.setCondition(cbOperator.getValue() + " " + newValue);
                    else if (type == DataType.Type.CATEGORY)
                        conditionNodeBean.setCondition(cbOperator.getValue() + " \'" + newValue + "\'");
                }
            }
        });
        textFieldValue.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (newValue) {
                    conditionNodeBean.setCondition(textFieldValue.getText());
                }
            }
        });
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

    public TextField getTextFieldValue() {
        return textFieldValue;
    }

    public void setTextFieldValue(TextField textFieldValue) {
        this.textFieldValue = textFieldValue;
    }

    public BorderPane getConditionNodeBorderPane() {
        return conditionNodeBorderPane;
    }

    public void setConditionNodeBorderPane(BorderPane conditionNodeBorderPane) {
        this.conditionNodeBorderPane = conditionNodeBorderPane;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
