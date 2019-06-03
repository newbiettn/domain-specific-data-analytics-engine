package controllers;
import beans.SavePredictiveModelBean;
import beans.UsePredictiveModelBean;
import common.ProjectPropertiesGetter;
import eu.mihosoft.vrl.workflow.VNode;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.text.Text;

import java.io.File;
import java.util.ArrayList;

/**
 * Controller for sparql nodes.
 */
public class SavePredictiveModelNodeController {
    private ProjectPropertiesGetter propGetter = ProjectPropertiesGetter.getSingleton();
    private String modelStorePath =  propGetter.getProperty("sparqlml.dm.model.filepath");
    private VNode node;

    private SavePredictiveModelBean savePredictiveModelBean;

    private HBox root;

    private BorderPane borderPane;

    private TextField textFieldModelName;

    public SavePredictiveModelNodeController() {
        root = new HBox();
        borderPane = new BorderPane();
        HBox.setHgrow(borderPane, Priority.ALWAYS);

        Text title = new Text("SAVE PREDICTIVE MODEL");
        borderPane.setCenter(title);
        BorderPane.setAlignment(title, Pos.CENTER);

        textFieldModelName = new TextField();
        textFieldModelName.setPrefWidth(220);
        borderPane.setBottom(textFieldModelName);
        BorderPane.setAlignment(textFieldModelName, Pos.CENTER);

        borderPane.setPadding(new Insets(0, 5, 10, 5));

        textFieldModelName.addEventHandler(KeyEvent.KEY_RELEASED, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                savePredictiveModelBean.setSparqlValue("SAVE MODEL '" + textFieldModelName.getText() + "'");
            }

        });
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

    public BorderPane getBorderPane() { return borderPane; }

    public void setBorderPane(BorderPane borderPane) {
        this.borderPane = borderPane;
    }

    public SavePredictiveModelBean getSavePredictiveModelBean() {
        return savePredictiveModelBean;
    }

    public void setSavePredictiveModelBean(SavePredictiveModelBean savePredictiveModelBean) {
        this.savePredictiveModelBean = savePredictiveModelBean;
    }

}
