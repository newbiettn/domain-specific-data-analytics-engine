package controllers;

import beans.PatientNodeBean;
import eu.mihosoft.vrl.workflow.FlowFactory;
import eu.mihosoft.vrl.workflow.VFlow;
import eu.mihosoft.vrl.workflow.VNode;
import eu.mihosoft.vrl.workflow.fx.FXValueSkinFactory;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import beans.SelectNodeBean;
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
    private VFlow copyFlow;

    @FXML
    private AnchorPane canvasPane;

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

        // Node flow
        flow = FlowFactory.newFlow();
        copyFlow = FlowFactory.newFlow();
        nodes = flow.getNodes();
        flow.setVisible(true);
        copyFlow.setVisible(false);

        // Create skin factory for flow visualization
        skinFactory = new FXValueSkinFactory(canvasPane);
        skinFactory.addSkinClassForValueType(SelectNodeBean.class, SelectNodeSkin.class);
        skinFactory.addSkinClassForValueType(PatientNodeBean.class, PatientNodeSkin.class);
        flow.setSkinFactories(skinFactory);
        copyFlow.setSkinFactories(skinFactory);
    }

    @FXML
    private void addSELECTNode() {
        VNode n = copyFlow.newNode();
        n.getValueObject().setValue(new SelectNodeBean());
        flow.newNode(n.getValueObject());
    }

    @FXML
    private void addASKNode() {
        VNode n = copyFlow.newNode();
        n.getValueObject().setValue(new SelectNodeBean());
        flow.newNode(n.getValueObject());
    }

    @FXML
    private void addCREATEMLMODELNode() {
        VNode n = copyFlow.newNode();
        n.getValueObject().setValue(new SelectNodeBean());
        flow.newNode(n.getValueObject());
    }

    @FXML
    private void addPATIENTNode() {
        VNode n = copyFlow.newNode();
        n.getValueObject().setValue(new PatientNodeBean());
        flow.newNode(n.getValueObject());
    }


}
