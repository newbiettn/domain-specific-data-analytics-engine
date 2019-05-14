package controllers;

import beans.*;

import common.ProjectPropertiesGetter;
import eu.mihosoft.vrl.workflow.*;
import eu.mihosoft.vrl.workflow.fx.*;
import io.CustomWorkflowIO;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Path;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import parsing.ParseTree;
import service.MainControllerService;
import skins.*;
import javafx.util.Pair;

import java.io.*;
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
// TODO: Limit connection number for each types only 1
public class MainController {
    static ProjectPropertiesGetter propGetter = ProjectPropertiesGetter.getSingleton();
    static String  filePath = propGetter.getProperty("sparqlml.tmp.data.filepath");
    static String trainingCsv = filePath + "sparql_data_tmp.csv";

    public static int nodeCount = 0;
    public static final String CONNECTION_NAME = "data";
    private static Logger logger = LoggerFactory.getLogger(MainController.class);
    private CustomFXValueSkinFactory skinFactory;
    private ObservableList<VNode> nodes;
    private VFlow flow;
    private VCanvas canvas;
    private Pane rootPane;
    private Connection selectedConnection;
    final TableView<ObservableList<StringProperty>> table = new TableView<>();
    private MainControllerService service;

    @FXML
    private Pane contentPane;

    @FXML
    private Pane tablePane;

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
    private ChoiceBox<String> connectionNameChoiceBox;

    public MainController(){
        service = new MainControllerService();
    }

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
        flow = CustomFlowFactory.newFlow();
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

        // add event handler for new connection when added
        Connections conns = flow.getConnections(CONNECTION_NAME);
        conns.getConnections().addListener(new ListChangeListener<Connection>() {
            @Override
            public void onChanged(Change<? extends Connection> c) {
                while (c.next()){
                    List<? extends Connection> subList = c.getList();
                    logger.info("Connection list has been added by " + subList.size());
                    for (int i = 0; i < subList.size(); i++){
                        Connection conn = subList.get(i);
                        addEventHandlerForConn(conn);
                    }
                }
            }
        });

        flow.getNodes().addListener(new ListChangeListener<VNode>() {
            @Override
            public void onChanged(Change<? extends VNode> c) {
                if (conns.getConnections().size() > 0){
                    Connection con = conns.getConnections().get(0);
                    conns.getConnections().remove(con);
                    conns.getConnections().add(con);
                }
            }
        });

        // Add table to tablePane
        // Fix top, bottom, left and right to 0 to ensure the table fills the whole pane
        AnchorPane.setTopAnchor(table, 0.0);
        AnchorPane.setBottomAnchor(table, 0.0);
        AnchorPane.setLeftAnchor(table, 0.0);
        AnchorPane.setRightAnchor(table, 0.0);
        tablePane.getChildren().add(table);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY); // No horizontal scroll

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
                    VNode receiver = con.getReceiver().getNode();
                    ObjectBean senderObj = (ObjectBean) sender.getValueObject().getValue();
                    ObjectBean receiverObj = (ObjectBean) receiver.getValueObject().getValue();
                    ObservableList<Pair<String, Class>> outputs = senderObj.getOutputs();
                    ObservableList<String> connNames = FXCollections.observableArrayList();
                    for (Pair<String, Class> o : outputs){
                        if (o.getValue() == receiverObj.getClass())
                            connNames.add(o.getKey());
                    }
                    connectionNameChoiceBox.setItems(connNames);
                }
            });
        connectionNameChoiceBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                    String newName = newValue;
                    selectedConnection.getConnectionText().setText(newName);
                    selectedConnection.setName(newName);
            }
        });
    }

    @FXML
    private void addSelectNode() {
        ArrayList<Pair<String, Class>> outputs = new ArrayList<>();
        Pair<String, Class> p1 = new Pair<>("", EpisodeNodeBean.class);
        Pair<String, Class> p2 = new Pair<>("", PatientNodeBean.class);
        outputs.add(p1);
        outputs.add(p2);

        VNode n = flow.newNode();
        n.getValueObject().setValue(new SelectNodeBean(++nodeCount, outputs));
        n.setMainOutput(n.addOutput(CONNECTION_NAME));

        flow.setSkinFactories(skinFactory);
    }

    @FXML
    private void addAskNode() {
    }

    @FXML
    private void addCreateMLModelNode() {
    }

    @FXML
    private void addPatientNode() {
        ArrayList<Pair<String, Class>> outputs = new ArrayList<>();
        Pair<String, Class> p1 = new Pair<>("diab:hasURN", VariableNodeBean.class);
        Pair<String, Class> p2 = new Pair<>("diab:hasEpisode", EpisodeNodeBean.class);
        outputs.add(p1);
        outputs.add(p2);

        VNode n = flow.newNode();
        n.getValueObject().setValue(new PatientNodeBean(++nodeCount, outputs));
        n.setMainInput(n.addInput(CONNECTION_NAME));
        n.setMainOutput(n.addOutput(CONNECTION_NAME));

        flow.setSkinFactories(skinFactory);
    }

    @FXML
    private void addEpisodeNode() {
        ArrayList<Pair<String, Class>> outputs = new ArrayList<>();
        Pair<String, Class> p1 = new Pair<>("diab:hasAge", VariableNodeBean.class);
        Pair<String, Class> p2 = new Pair<>("diab:hasDiabetesTestScore", VariableNodeBean.class);
        outputs.add(p1);
        outputs.add(p2);

        VNode n = flow.newNode();
        n.getValueObject().setValue(new EpisodeNodeBean(++nodeCount, outputs));
        n.setMainInput(n.addInput(CONNECTION_NAME));
        n.setMainOutput(n.addOutput(CONNECTION_NAME));
        n.getMainInput(CONNECTION_NAME).addClickEventListener(new EventHandler<ClickEvent>() {
            @Override
            public void handle(ClickEvent event) {
                System.out.println("AAAAAAAAAA");
            }
        });
        flow.setSkinFactories(skinFactory);
    }

    @FXML
    private void addVariableNode() {
        VNode n = flow.newNode();
        n.getValueObject().setValue(new VariableNodeBean(++nodeCount));
        n.setMainInput(n.addInput(CONNECTION_NAME));
        flow.setSkinFactories(skinFactory);
    }

    @FXML
    private void parseFlow(){
        try {
            CustomWorkflowIO.saveToXML(Paths.get("flow01.xml"), flow.getModel());
            logger.info("Parsed the flow");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void loadFlow() {
        System.out.print(" >> loading workflow from xml");

        try {
            flow = CustomWorkflowIO.loadFromXML(Paths.get("flow01.xml"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println(" [done]");
        updateUI();
    }

    private void updateUI() {
        rootPane.getChildren().clear();
        flow.getModel().setVisible(true);
        flow.setSkinFactories(skinFactory);
    }

    @FXML
    private void testFlow(){
        ParseTree pt = new ParseTree();
        pt.parse(flow); // Parse the flow to parse tree
        String sparqlQuery = pt.interpret(); // Interpret to appropriate SPARQL query
        if (pt.interpret() != null){
            if (service.executeQuery(sparqlQuery)) { // Run query to retrieve data
                logger.info("Populating the table...");
                service.populateTable(table, "",
                        true); // Populate the retrieved data to table
            } else {
                logger.info("Retrieved no data");
            }
        }
    }

}
