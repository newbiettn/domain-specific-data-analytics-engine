package controllers;
import beans.UsePredictiveModelBean;
import common.ProjectPropertiesGetter;
import eu.mihosoft.vrl.workflow.VNode;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.text.Text;

import java.io.File;
import java.util.ArrayList;

/**
 * Controller for sparql nodes.
 */
public class UsePredictiveModelNodeController {
    private ProjectPropertiesGetter propGetter = ProjectPropertiesGetter.getSingleton();
    private String processStorePath =  propGetter.getProperty("sparqlml.dm.process.filepath");
    private VNode node;

    private UsePredictiveModelBean usePredictiveModelBean;

    private HBox root;

    private BorderPane borderPane;

    private ChoiceBox<String> cbModelName;

    public UsePredictiveModelNodeController() {
        root = new HBox();
        borderPane = new BorderPane();
        HBox.setHgrow(borderPane, Priority.ALWAYS);

        Text title = new Text("USE PREDICTIVE MODEL");
        borderPane.setCenter(title);
        BorderPane.setAlignment(title, Pos.CENTER);

        cbModelName = new ChoiceBox<>();
        cbModelName.setPrefWidth(220);
        borderPane.setBottom(cbModelName);
        BorderPane.setAlignment(cbModelName, Pos.CENTER);

        borderPane.setPadding(new Insets(0, 5, 10, 5));

        ArrayList<String> modelNames = listSavedModels(processStorePath);
        cbModelName.setItems(FXCollections.observableArrayList(modelNames));
        cbModelName.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (newValue != null) {
                    usePredictiveModelBean.setSparqlValue("USE MODEL '" + newValue + "'");
                }
            }
        });
    }

    @FXML
    public void initialize() {
    }

    /**
     * List all saved models for further selection.
     *
     * @param folderPath
     * @return
     */
    public ArrayList<String> listSavedModels(String folderPath){
        File f = null;
        String[] paths;
        ArrayList<String> modelNames = new ArrayList<>();

        try {
            f = new File(folderPath);
            paths = f.list();
            for(String path:paths) {
                String pattern = "(\\w+)(_)(\\w+)(.kf)";
                String modelName = path.replaceAll(pattern, "$3");
                modelNames.add(modelName);
            }

        } catch(Exception e) {
            // if any error occurs
            e.printStackTrace();
        }
        return modelNames;
    }

    public VNode getNode() {
        return node;
    }

    public void setNode(VNode node) {
        this.node = node;
    }

    public UsePredictiveModelBean getUsePredictiveModelBean() {
        return usePredictiveModelBean;
    }

    public void setUsePredictiveModelBean(UsePredictiveModelBean usePredictiveModelBean) {
        this.usePredictiveModelBean = usePredictiveModelBean;
    }

    public BorderPane getBorderPane() { return borderPane; }

    public void setBorderPane(BorderPane borderPane) {
        this.borderPane = borderPane;
    }

}
