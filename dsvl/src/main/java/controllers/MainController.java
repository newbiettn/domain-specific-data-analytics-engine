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
import service.ParseTreeService;
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
        skinFactory.addSkinClassForValueType(CreatePredictionModelNodeBean.class, CreatePredictionModelNodeSkin.class);
        skinFactory.addSkinClassForValueType(TargetNodeBean.class, TargetNodeSkin.class);
        skinFactory.addSkinClassForValueType(FeatureNodeBean.class, FeatureNodeSkin.class);
        skinFactory.addSkinClassForValueType(PrevalenceNodeBean.class, PrevalenceNodeSkin.class);
        skinFactory.addSkinClassForValueType(AskNodeBean.class, AskNodeSkin.class);
        skinFactory.addSkinClassForValueType(PatientNodeBean.class, PatientNodeSkin.class);
        skinFactory.addSkinClassForValueType(ConditionNodeBean.class, ConditionNodeSkin.class);
        skinFactory.addSkinClassForValueType(EpisodeNodeBean.class, EpisodeNodeSkin.class);
        skinFactory.addSkinClassForValueType(SeparationReportNodeBean.class, SeparationReportNodeSkin.class);
        skinFactory.addSkinClassForValueType(AdmissionReportNodeBean.class, AdmissionReportNodeSkin.class);

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
     * Event handler for connections (i.e., allow to select name for them).
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

                    // Automatically generate variables names based on connection name
                    // For example, hasURN -> ?urn
                    VNode receiver = selectedConnection.getReceiver().getNode();
                    ObjectBean receiverObj = (ObjectBean) receiver.getValueObject().getValue();
                    String pattern = "(diab:has)(\\w+)";
                    String v = newName.replaceAll(pattern, "$2");
                    receiverObj.setSparqlValue(v);
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
        n.getValueObject().setValue(new SelectNodeBean(outputs));
        n.setMainOutput(n.addOutput(CONNECTION_NAME));

        flow.setSkinFactories(skinFactory);
    }

    @FXML
    private void addPrevalenceNode() {
        ArrayList<Pair<String, Class>> outputs = new ArrayList<>();
        outputs.add(new Pair<>("", PatientNodeBean.class));

        VNode n = flow.newNode();
        n.getValueObject().setValue(new PrevalenceNodeBean(outputs));
        n.setMainOutput(n.addOutput(CONNECTION_NAME));

        flow.setSkinFactories(skinFactory);
    }

    @FXML
    private void addAskNode() {
        ArrayList<Pair<String, Class>> outputs = new ArrayList<>();
        outputs.add(new Pair<>("", PatientNodeBean.class));
        outputs.add(new Pair<>("", EpisodeNodeBean.class));

        VNode n = flow.newNode();
        n.getValueObject().setValue(new AskNodeBean(outputs));
        n.setMainOutput(n.addOutput(CONNECTION_NAME));

        flow.setSkinFactories(skinFactory);
    }

    @FXML
    private void addCreateMLModelNode() {
        ArrayList<Pair<String, Class>> outputs = new ArrayList<>();
        outputs.add(new Pair<>("", PatientNodeBean.class));
        outputs.add(new Pair<>("", EpisodeNodeBean.class));
        outputs.add(new Pair<>("", TargetNodeBean.class));
        outputs.add(new Pair<>("", FeatureNodeBean.class));

        VNode n = flow.newNode();
        n.getValueObject().setValue(new CreatePredictionModelNodeBean(outputs));
        n.setMainOutput(n.addOutput(CONNECTION_NAME));

        flow.setSkinFactories(skinFactory);
    }

    @FXML
    private void addTargetNode() {
        ArrayList<Pair<String, Class>> outputs = new ArrayList<>();
        outputs.add(new Pair<>("", ConditionNodeBean.class));

        VNode n = flow.newNode();
        n.getValueObject().setValue(new TargetNodeBean(outputs));
        n.setMainOutput(n.addOutput(CONNECTION_NAME));
        n.setMainInput(n.addInput(CONNECTION_NAME));

        flow.setSkinFactories(skinFactory);
    }

    @FXML
    private void addFeatureNode() {
        ArrayList<Pair<String, Class>> outputs = new ArrayList<>();
        outputs.add(new Pair<>("", ConditionNodeBean.class));

        VNode n = flow.newNode();
        n.getValueObject().setValue(new FeatureNodeBean(outputs));
        n.setMainOutput(n.addOutput(CONNECTION_NAME));
        n.setMainInput(n.addInput(CONNECTION_NAME));

        flow.setSkinFactories(skinFactory);
    }

    @FXML
    private void addPatientNode() {
        ArrayList<Pair<String, Class>> outputs = new ArrayList<>();
        outputs.add(new Pair<>("diab:hasURN", ConditionNodeBean.class));
        outputs.add(new Pair<>("diab:hasEpisode", EpisodeNodeBean.class));

        VNode n = flow.newNode();
        n.getValueObject().setValue(new PatientNodeBean(outputs));
        n.setMainInput(n.addInput(CONNECTION_NAME));
        n.setMainOutput(n.addOutput(CONNECTION_NAME));

        flow.setSkinFactories(skinFactory);
    }

    @FXML
    private void addEpisodeNode() {
        ArrayList<Pair<String, Class>> outputs = new ArrayList<>();
        outputs.add(new Pair<>("diab:hasAdmissionReport", AdmissionReportNodeBean.class));
        outputs.add(new Pair<>("diab:hasAge", ConditionNodeBean.class));
        outputs.add(new Pair<>("diab:hasCountryOfBirth", ConditionNodeBean.class));
        outputs.add(new Pair<>("diab:hasCountryOfBirthCode", ConditionNodeBean.class));
        outputs.add(new Pair<>("diab:hasDeceased", ConditionNodeBean.class));
        outputs.add(new Pair<>("diab:hasEpisodeKey", ConditionNodeBean.class));
        outputs.add(new Pair<>("diab:hasGender", ConditionNodeBean.class));
        outputs.add(new Pair<>("diab:hasGp", ConditionNodeBean.class));
        outputs.add(new Pair<>("diab:hasHbA1c", ConditionNodeBean.class));
        outputs.add(new Pair<>("diab:hasHbA1cTestRequestEvent", ConditionNodeBean.class));
        outputs.add(new Pair<>("diab:hasHbA1cTestResult", ConditionNodeBean.class));
        outputs.add(new Pair<>("diab:hasInterpreterRequired", ConditionNodeBean.class));
        outputs.add(new Pair<>("diab:hasLengthOfStay", ConditionNodeBean.class));
        outputs.add(new Pair<>("diab:hasLengthOfStayType", ConditionNodeBean.class));
        outputs.add(new Pair<>("diab:hasMaritalStatus", ConditionNodeBean.class));
        outputs.add(new Pair<>("diab:hasPreferredLanguage", ConditionNodeBean.class));
        outputs.add(new Pair<>("diab:hasReligion", ConditionNodeBean.class));
        outputs.add(new Pair<>("diab:hasSeparationReport", SeparationReportNodeBean.class));

        VNode n = flow.newNode();
        n.getValueObject().setValue(new EpisodeNodeBean(outputs));
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
    private void addAdmissionReportNode() {
        ArrayList<Pair<String, Class>> outputs = new ArrayList<>();
        outputs.add(new Pair<>("diab:hasAdmissionSource", ConditionNodeBean.class));
        outputs.add(new Pair<>("diab:hasAdmissionType", ConditionNodeBean.class));
        outputs.add(new Pair<>("diab:hasAdmissionUnit", ConditionNodeBean.class));
        outputs.add(new Pair<>("diab:hasAdmissionWard", ConditionNodeBean.class));
        outputs.add(new Pair<>("diab:hasTimestamp", ConditionNodeBean.class));

        VNode n = flow.newNode();
        n.getValueObject().setValue(new AdmissionReportNodeBean(outputs));
        n.setMainInput(n.addInput(CONNECTION_NAME));
        n.setMainOutput(n.addOutput(CONNECTION_NAME));

        flow.setSkinFactories(skinFactory);
    }

    @FXML
    private void addSeparationReportNode() {
        ArrayList<Pair<String, Class>> outputs = new ArrayList<>();
        outputs.add(new Pair<>("diab:hasSeparationMode", ConditionNodeBean.class));
        outputs.add(new Pair<>("diab:hasSeparationUnit", ConditionNodeBean.class));
        outputs.add(new Pair<>("diab:hasSeparationWard", ConditionNodeBean.class));

        VNode n = flow.newNode();
        n.getValueObject().setValue(new SeparationReportNodeBean(outputs));
        n.setMainInput(n.addInput(CONNECTION_NAME));
        n.setMainOutput(n.addOutput(CONNECTION_NAME));

        flow.setSkinFactories(skinFactory);
    }

    @FXML
    private void addVariableNode() {
        VNode n = flow.newNode();
        n.getValueObject().setValue(new ConditionNodeBean());
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
        int type = pt.parse(flow); // Parse the flow to parse tree
        if (type != ParseTree.INVALID_TREE){
            String sparqlQuery = pt.interpret(); // Interpret to appropriate SPARQL query
            if (pt.interpret() != null){
                boolean execResult = false;
                if (type == ParseTree.SELECT_TREE)
                    execResult = service.execSelectQuery(sparqlQuery);
                else if (type == ParseTree.PREVALENCE_TREE)
                    execResult = service.execSelectQuery(sparqlQuery);
                else if (type == ParseTree.ASK_TREE)
                    execResult = service.execAskQuery(sparqlQuery);
                else if (type == ParseTree.CREATEPREDICTIONMODEL_TREE)
                    execResult = service.execCreatePredictionModelQuery(sparqlQuery);
                if (execResult) { // Run query to retrieve data
                    logger.info("Populating the table...");
                    service.populateTable(table, "",
                            true); // Populate the retrieved data to table
                } else {
                    logger.info("Retrieved no data");
                }
            }
        } else {
            logger.info("Invalid parsing tree");
        }
    }

}
