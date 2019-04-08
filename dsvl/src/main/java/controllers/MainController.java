package controllers;

import beans.EpisodeNodeBean;
import beans.PatientNodeBean;

import beans.VariableNodeBean;
import eu.mihosoft.vrl.workflow.*;
import eu.mihosoft.vrl.workflow.fx.*;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import beans.SelectNodeBean;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Path;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import skins.*;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Main controller.
 *
 * @author newbiettn
 * @since 2019-03-1
 *
 */
public class MainController {
    private static Logger logger = LoggerFactory.getLogger(MainController.class);
    private FXValueSkinFactory skinFactory;
    private ObservableList<VNode> nodes;
    private VFlow flow;
    private VCanvas canvas;
    private Pane rootPane;
    private Path selectedPath;
    private Connection selectedConnection;
    private VFlowModel vFlowModel;

    @FXML
    private Pane contentPane;

    @FXML
    private Button selectBtn;

    @FXML
    private URL location;

    @FXML
    private ResourceBundle resourceBundle;

    @FXML
    private Accordion rightAccordion;

    @FXML
    private TitledPane propertiesTitledPane;

    @FXML
    private TextField connectionName;

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
        canvas.setTranslateToMinNodePos(false); // avoid panning
        canvas.setMaxScaleX(0.6);
        canvas.setMaxScaleY(0.6);
        Pane root = (Pane) canvas.getContent();
        contentPane.getChildren().add(canvas);
        rootPane = root;
        rootPane.getChildren().clear();

        // Node flow
        flow = FlowFactory.newFlow();
        flow.setVisible(true);

        // Create skin factory for flow visualization
        skinFactory = new CustomFXValueSkinFactory(canvas);
        skinFactory.addSkinClassForValueType(SelectNodeBean.class, SelectNodeSkin.class);
        skinFactory.addSkinClassForValueType(PatientNodeBean.class, PatientNodeSkin.class);
        skinFactory.addSkinClassForValueType(VariableNodeBean.class, VariableNodeSkin.class);
        skinFactory.addSkinClassForValueType(EpisodeNodeBean.class, EpisodeNodeSkin.class);
        flow.setSkinFactories(skinFactory);

        // config right accordion
        rightAccordion.setExpandedPane(propertiesTitledPane);

        // add event handler for connections
        Connections conns = flow.getConnections("query");
        conns.getConnections().addListener(new ListChangeListener<Connection>() {
            @Override
            public void onChanged(Change<? extends Connection> c) {
                while (c.next()){
                    if (c.wasAdded()){
                        List<? extends Connection> subList = c.getAddedSubList();
                        Connection conn = subList.get(0);
                        addEventHandlerForConn(conn);
                    }
                }
            }
        });

    }

    /**
     *
     * @param con
     */
    public void addEventHandlerForConn(Connection con){
            Path p = con.getConnectionPath();
            System.out.println(p);
            p.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    connectionName.setText(con.getName());
                    selectedPath = p;
                    selectedConnection = con;
                    System.out.println(con.getName());
                }
            });
        connectionName.addEventHandler(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode().equals(KeyCode.ENTER)){
                    System.out.println("connection name entered");
                    System.out.println(selectedConnection);
                    selectedConnection.setName(connectionName.getText());
                }
            }
        });
    }

    @FXML
    private void addSelectNode() {

        VNode n = flow.newNode();
        n.getValueObject().setValue(new SelectNodeBean());
//        n.setMainInput(n.addInput("data"))
//                .getVisualizationRequest()
//                .set(VisualizationRequest.KEY_CONNECTOR_AUTO_LAYOUT, true);
        n.setMainOutput(n.addOutput("query"))
                .getVisualizationRequest()
                .set(VisualizationRequest.KEY_CONNECTOR_AUTO_LAYOUT, true);
        flow.setSkinFactories(skinFactory);
    }

    @FXML
    private void addAskNode() {
//        n.getValueObject().setValue(new SelectNodeBean());
//        flow.newNode(n.getValueObject());
//        flow.getSkinFactories().clear();
//        flow.setSkinFactories(skinFactory);
    }

    @FXML
    private void addCreateMLModelNode() {
//        VNode n = copyFlow.newNode();
//        n.getValueObject().setValue(new SelectNodeBean());
//        flow.newNode(n.getValueObject());
    }

    @FXML
    private void addPatientNode() {
        VNode n = flow.newNode();
        n.getValueObject().setValue(new PatientNodeBean());
        n.setMainInput(n.addInput("query"))
                .getVisualizationRequest()
                .set(VisualizationRequest.KEY_CONNECTOR_AUTO_LAYOUT, true);

        // Output
        n.setMainOutput(n.addOutput("data"))
                .getVisualizationRequest()
                .set(VisualizationRequest.KEY_CONNECTOR_AUTO_LAYOUT, true);
//        n.setMainOutput(n.addOutput("hasEpisode"))
//                .getVisualizationRequest()
//                .set(VisualizationRequest.KEY_CONNECTOR_AUTO_LAYOUT, true);
        flow.setSkinFactories(skinFactory);

    }

    @FXML
    private void addVariableNode() {
        VNode n = flow.newNode();
        n.getValueObject().setValue(new VariableNodeBean());
        n.setMainInput(n.addInput("data"))
                .getVisualizationRequest()
                .set(VisualizationRequest.KEY_CONNECTOR_AUTO_LAYOUT, true);
        flow.getSkinFactories().clear();
    }

    @FXML
    private void addEpisodeNode() {
        VNode n = flow.newNode();
        n.getValueObject().setValue(new EpisodeNodeBean());
        n.setMainInput(n.addInput("data"))
                .getVisualizationRequest()
                .set(VisualizationRequest.KEY_CONNECTOR_AUTO_LAYOUT, true);
//        flow.setSkinFactories(skinFactory);
    }

}
