package controllers;

import beans.PatientNodeBean;

import eu.mihosoft.vrl.workflow.*;
import eu.mihosoft.vrl.workflow.fx.FXValueSkinFactory;
import eu.mihosoft.vrl.workflow.fx.VCanvas;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import beans.SelectNodeBean;
import javafx.scene.layout.Pane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import skins.PatientNodeSkin;
import skins.SelectNodeSkin;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Main controller.
 *
 */
public class MainController {
    private static Logger logger = LoggerFactory.getLogger(MainController.class);
    private FXValueSkinFactory skinFactory;
    private ObservableList<VNode> nodes;
    private VFlow flow;
    private VCanvas canvas;
    private Pane rootPane;

    @FXML
    private Pane contentPane;

    @FXML
    private Button selectBtn;

    @FXML
    private URL location;

    @FXML
    private ResourceBundle resourceBundle;

    public MainController(){}

    /**
     * Initialize the controller.
     */
    @FXML
    private void initialize() {
        logger.info("Initialize controllers.MainController...");
        logger.info("Location = " + location);
        logger.info("Resource = " + resourceBundle);

        canvas = new VCanvas();
        canvas.setMaxScaleX(1);
        canvas.setMaxScaleY(1);
        Pane root = (Pane) canvas.getContent();
        contentPane.getChildren().add(canvas);
        rootPane = root;
        rootPane.getChildren().clear();

        // Node flow
        flow = FlowFactory.newFlow();
        flow.setVisible(true);

        // Create skin factory for flow visualization
        skinFactory = new FXValueSkinFactory(canvas);
        skinFactory.addSkinClassForValueType(SelectNodeBean.class, SelectNodeSkin.class);
        skinFactory.addSkinClassForValueType(PatientNodeBean.class, PatientNodeSkin.class);
        flow.setSkinFactories(skinFactory);
    }

    @FXML
    private void addSELECTNode() {
        VNode n = flow.newNode();
        n.getValueObject().setValue(new SelectNodeBean());
        n.setMainInput(n.addInput("data"))
                .getVisualizationRequest()
                .set(VisualizationRequest.KEY_CONNECTOR_AUTO_LAYOUT, true);
        n.setMainOutput(n.addOutput("data"))
                .getVisualizationRequest()
                .set(VisualizationRequest.KEY_CONNECTOR_AUTO_LAYOUT, true);
//        flow.getSkinFactories().clear();
        flow.setSkinFactories(skinFactory);
    }

    @FXML
    private void addASKNode() {
//        n.getValueObject().setValue(new SelectNodeBean());
//        flow.newNode(n.getValueObject());
//        flow.getSkinFactories().clear();
//        flow.setSkinFactories(skinFactory);
    }

    @FXML
    private void addCREATEMLMODELNode() {
//        VNode n = copyFlow.newNode();
//        n.getValueObject().setValue(new SelectNodeBean());
//        flow.newNode(n.getValueObject());
    }

    @FXML
    private void addPATIENTNode() {
        VNode n = flow.newNode();
        n.getValueObject().setValue(new PatientNodeBean());
        n.setMainInput(n.addInput("data"))
                .getVisualizationRequest()
                .set(VisualizationRequest.KEY_CONNECTOR_AUTO_LAYOUT, true);
        n.setMainOutput(n.addOutput("data"))
                .getVisualizationRequest()
                .set(VisualizationRequest.KEY_CONNECTOR_AUTO_LAYOUT, true);
        flow.setSkinFactories(skinFactory);
    }
}
