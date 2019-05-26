package controllers;

import beans.*;

import config.Condition;
import config.DataType;
import config.Operator;
import eu.mihosoft.vrl.workflow.*;
import eu.mihosoft.vrl.workflow.fx.*;
import io.CustomWorkflowIO;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Path;
import javafx.stage.StageStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.MainControllerService;
import skins.*;
import javafx.util.Pair;

import java.io.*;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.*;

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
    private TableView<ObservableList<StringProperty>> table = new TableView<>();
    private MainControllerService service;
    private ExecutorService executor;


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
        executor = Executors.newSingleThreadExecutor();
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
        canvas.setMaxScaleX(0.7);
        canvas.setMaxScaleY(0.7);
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
        skinFactory.addSkinClassForValueType(PredictNodeBean.class, PredictNodeSkin.class);
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
                if (newValue != null) {
                    String newName = newValue;
                    selectedConnection.getConnectionText().setText(newName);
                    selectedConnection.setName(newName);

                    // Automatically generate variables names based on connection name
                    // For example, hasURN -> ?urn
                    VNode receiver = selectedConnection.getReceiver().getNode();
                    ObjectBean receiverObj = (ObjectBean) receiver.getValueObject().getValue();
                    String pattern = "(diab:has)(\\w+)";
                    String v = newName.replaceAll(pattern, "$2");
                    v = v.toLowerCase();
                    receiverObj.setSparqlValue(v);

                    // Set title node
                    receiver.setTitle(v.substring(0,1).toUpperCase() + v.substring(1).toLowerCase());

                    // Generate operators and values for condition nodes
                    if (receiverObj.getClass() == ConditionNodeBean.class){
                        ConditionNodeBean conditionBean = (ConditionNodeBean) receiverObj;
                        ConditionNodeController controller = (ConditionNodeController) receiver.getController();
                        ChoiceBox<String> cbOperator = controller.getCbOperator();
                        ChoiceBox<String> cbValue = controller.getCbValue();
                        TextField textFieldValue = controller.getTextFieldValue();

                        BorderPane borderPane = controller.getConditionNodeBorderPane();

                        // Load predefined operators & data types
                        Condition c = Configuration.getSingleton().getProject().getConditionByName(v);
                        if (c != null){
                            // Data type
                            DataType dataType = c.getAllowedDataTypes();
                            conditionBean.setDataType(c.getAllowedDataTypes());
                            System.out.println(dataType.getType());
                            // Operators
                            ObservableList<String> operatorItems = FXCollections.observableArrayList();
                            ArrayList<Operator> allowedOperators = c.getAllowedOperators();
                            if (allowedOperators.size() > 0) {
                                for (Operator op : c.getAllowedOperators()){
                                    operatorItems.add(op.getValue());
                                }
                                cbOperator.setItems(operatorItems);
                                cbOperator.setVisible(true);
                            }

                            // Value
                            ObservableList<String> valueItems = FXCollections.observableArrayList();
                            if (dataType.getType() == DataType.Type.CATEGORY) {
                                borderPane.setCenter(cbValue);
                                ArrayList<String> values = dataType.getValues();
                                if (values.size() > 0){
                                    for (String val : dataType.getValues()){
                                        valueItems.add(val);
                                    }
                                    cbValue.setItems(valueItems);
                                }
                            } if (dataType.getType() == DataType.Type.NUMERIC) {
                                borderPane.setCenter(textFieldValue);
                            }
                        }
                    }

                }
            }
        });
    }

    @FXML
    private void addSelectNode() {
        ArrayList<Pair<String, Class>> outputs = new ArrayList<>();
        outputs.add(new Pair<>("", EpisodeNodeBean.class));
        outputs.add(new Pair<>("", PatientNodeBean.class));
        outputs.add(new Pair<>("", AdmissionReportNodeBean.class));
        outputs.add(new Pair<>("", SeparationReportNodeBean.class));

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
    private void addPredictNode() {
        ArrayList<Pair<String, Class>> outputs = new ArrayList<>();
        outputs.add(new Pair<>("", ConditionNodeBean.class));
        outputs.add(new Pair<>("", TargetNodeBean.class));
        outputs.add(new Pair<>("", FeatureNodeBean.class));

        VNode n = flow.newNode();
        n.getValueObject().setValue(new PredictNodeBean(outputs));
        n.setMainOutput(n.addOutput(CONNECTION_NAME));

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
        outputs.add(new Pair<>("diab:hasDeceased", ConditionNodeBean.class));
        outputs.add(new Pair<>("diab:hasGender", ConditionNodeBean.class));
        outputs.add(new Pair<>("diab:hasGp", ConditionNodeBean.class));
        outputs.add(new Pair<>("diab:hasHbA1c", ConditionNodeBean.class));
//        outputs.add(new Pair<>("diab:hasHbA1cTestRequestEvent", ConditionNodeBean.class));
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
    private void testFlow() throws ExecutionException, InterruptedException {
        service.setFlow(flow);

        // Loading dialog
        ProgressBar p = new ProgressBar();
        p.setPrefWidth(300);
        Dialog<Boolean> loadingDialog = new Dialog<>();
        loadingDialog.getDialogPane().setContent(p);
//        loadingDialog.setHeaderText("Loading");
        loadingDialog.setGraphic(null);
        loadingDialog.resizableProperty().set(false);
        loadingDialog.initStyle(StageStyle.UNDECORATED);

        // here runs the JavaFX thread
        // Boolean as generic parameter since you want to return it
        Task<Boolean> task = new Task<Boolean>() {
            @Override public Boolean call() {
                // do your operation in here
                return (service.execQuery());
            }
        };

        task.setOnRunning((e) -> loadingDialog.show());
        task.setOnSucceeded((e) -> {
            loadingDialog.setResult(Boolean.TRUE);
            loadingDialog.hide();
            try {
                if (task.get())
                    service.populateTable(table, "", true);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            } catch (ExecutionException ex) {
                ex.printStackTrace();
            }
            // process return value again in JavaFX thread
        });
        task.setOnFailed((e) -> {
            // eventual error handling by catching exceptions from task.get()
        });
        new Thread(task).start();
    }
}
