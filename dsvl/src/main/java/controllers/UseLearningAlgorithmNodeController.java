package controllers;
import beans.UseLearningAlgorithmBean;
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
public class UseLearningAlgorithmNodeController {
    private ProjectPropertiesGetter propGetter = ProjectPropertiesGetter.getSingleton();
    private VNode node;

    private UseLearningAlgorithmBean useLearningAlgorithmBean;

    private HBox root;

    private BorderPane borderPane;

    private ChoiceBox<String> cbLearningAlgorithmNames;

    public UseLearningAlgorithmNodeController() {
        root = new HBox();
        borderPane = new BorderPane();
        HBox.setHgrow(borderPane, Priority.ALWAYS);

        Text title = new Text("WITH LEARNING ALGORITHM");
        borderPane.setCenter(title);
        BorderPane.setAlignment(title, Pos.CENTER);

        cbLearningAlgorithmNames = new ChoiceBox<>();
        cbLearningAlgorithmNames.setPrefWidth(220);
        borderPane.setBottom(cbLearningAlgorithmNames);
        BorderPane.setAlignment(cbLearningAlgorithmNames, Pos.CENTER);

        borderPane.setPadding(new Insets(0, 5, 10, 5));

        cbLearningAlgorithmNames.setItems(FXCollections.observableArrayList(listLearningAlgorithmNames()));
        cbLearningAlgorithmNames.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (newValue != null) {
                    useLearningAlgorithmBean.setSparqlValue("WITH LEARNING ALGORITHM '" + newValue + "'");
                }
            }
        });
    }

    @FXML
    public void initialize() {
    }

    /**
     * List all learning algorithm names.
     *
     * @return
     */
    public ArrayList<String> listLearningAlgorithmNames(){
        ArrayList<String> learningAlgorithmNames = new ArrayList<>();
        learningAlgorithmNames.add("j48");
        learningAlgorithmNames.add("adaboostm1");
        learningAlgorithmNames.add("randomforest");
        learningAlgorithmNames.add("neuralnetwork");
        learningAlgorithmNames.add("logistic");
        learningAlgorithmNames.add("smo");
        learningAlgorithmNames.add("kstar");
        learningAlgorithmNames.add("ibk");
        learningAlgorithmNames.add("lwl");
        learningAlgorithmNames.add("bagging");
        learningAlgorithmNames.add("logitboost");
        learningAlgorithmNames.add("randomsubspace");
        learningAlgorithmNames.add("stacking");
        learningAlgorithmNames.add("vote");
        learningAlgorithmNames.add("decisiontable");

        return learningAlgorithmNames;
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

    public UseLearningAlgorithmBean getUseLearningAlgorithmBean() {
        return useLearningAlgorithmBean;
    }

    public void setUseLearningAlgorithmBean(UseLearningAlgorithmBean useLearningAlgorithmBean) {
        this.useLearningAlgorithmBean = useLearningAlgorithmBean;
    }

}
