package controllers;

import beans.EpisodeNodeBean;
import beans.PatientNodeBean;

import beans.VariableNodeBean;
import com.sun.org.apache.bcel.internal.generic.Select;
import eu.mihosoft.vrl.workflow.*;
import eu.mihosoft.vrl.workflow.fx.*;
import eu.mihosoft.vrl.workflow.io.WorkflowIO;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import beans.SelectNodeBean;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Path;
import javafx.scene.text.Text;
import javafx.util.StringConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import skins.*;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ArrayList;
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
    private Connection selectedConnection;
    private VFlow cloneFlow;

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
    private ChoiceBox<String> connectionName;

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
        cloneFlow = FlowFactory.newFlow();
        cloneFlow.setVisible(false);

        // Create skin factory for flow visualization
        skinFactory = new CustomFXValueSkinFactory(canvas);
        skinFactory.addSkinClassForValueType(SelectNodeBean.class, SelectNodeSkin.class);
        skinFactory.addSkinClassForValueType(PatientNodeBean.class, PatientNodeSkin.class);
        skinFactory.addSkinClassForValueType(VariableNodeBean.class, VariableNodeSkin.class);
        skinFactory.addSkinClassForValueType(EpisodeNodeBean.class, EpisodeNodeSkin.class);
        flow.setSkinFactories(skinFactory);

        // config right accordion
        rightAccordion.setExpandedPane(propertiesTitledPane);

        // add event handler for new connection when added
        Connections conns = flow.getConnections("data");
        conns.getConnections().addListener(new ListChangeListener<Connection>() {
            @Override
            public void onChanged(Change<? extends Connection> c) {
                while (c.next()){
                    if (c.wasAdded()){
                        List<? extends Connection> subList = c.getAddedSubList();
                        logger.info("Connection list has been added by " + subList.size());
                        for (int i = 0; i < subList.size(); i++){
                            Connection conn = subList.get(i);
                            addEventHandlerForConn(conn);
                        }
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
            p.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    selectedConnection = con;
                    String t = selectedConnection.getConnectionText().getText();
                    logger.info("Click on:" + con);

                    VNode sender = con.getSender().getNode();
                    Object obj = sender.getValueObject().getValue();
                    if (obj.getClass().equals(PatientNodeBean.class)){
                        PatientNodeBean bean = (PatientNodeBean) obj;
                        connectionName.setItems(bean.getConnNames());
                        logger.info(t);
                        if (!t.isEmpty()){
                            connectionName.getSelectionModel().select(t);
                        }

                    } else if (obj.getClass().equals(EpisodeNodeBean.class)){
                        EpisodeNodeBean bean = (EpisodeNodeBean) obj;
                        connectionName.setItems(bean.getConnNames());
                        logger.info(t);
                        if (!t.isEmpty()){
                            connectionName.getSelectionModel().select(t);
                        }
                    }

                }
            });
        connectionName.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                    String newName = newValue;
                    selectedConnection.getConnectionText().setText(newName);
                    selectedConnection.setName(newName);
                    logger.info(newValue);
            }
        });
    }

    @FXML
    private void addSelectNode() {
        VNode n = cloneFlow.newNode();
        n.getValueObject().setValue(new SelectNodeBean());
        n.addOutput("data");
        flow.newNode(n);
    }

    @FXML
    private void addAskNode() {
    }

    @FXML
    private void addCreateMLModelNode() {
    }

    @FXML
    private void addPatientNode() {
        ArrayList<String> cns = new ArrayList<>();
        cns.add("hasURN");
        cns.add("hasEpisode");

        VNode n = cloneFlow.newNode();
        n.getValueObject().setValue(new PatientNodeBean(cns));
        n.addInput("data");
        n.addOutput("data");
        flow.newNode(n);
    }

    @FXML
    private void addEpisodeNode() {
        ArrayList<String> cns = new ArrayList<>();
        cns.add("hasAge");
        cns.add("hasDiabetesTestScore");

        VNode n = cloneFlow.newNode();
        n.getValueObject().setValue(new EpisodeNodeBean(cns));
        n.addInput("data");
        n.addOutput("data");
        flow.newNode(n);
    }

    @FXML
    private void addVariableNode() {
        VNode n = cloneFlow.newNode();
        n.getValueObject().setValue(new VariableNodeBean());
        n.addInput("data");
        flow.newNode(n);
    }

    @FXML
    private void parseFlow(){
        try {
            WorkflowIO.saveToXML(Paths.get("flow01.xml"), flow.getModel());
            logger.info("Parsed the flow");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
