package skins;

import beans.ConditionNodeBean;
import beans.ObjectBean;
import config.Condition;
import config.DataType;
import config.Operator;
import controllers.ConditionNodeController;
import controllers.Configuration;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.shape.Path;
import javafx.stage.WindowEvent;
import javafx.util.Pair;
import listener.CustomConnectionListener;
import eu.mihosoft.vrl.workflow.*;
import eu.mihosoft.vrl.workflow.fx.*;
import javafx.beans.binding.DoubleBinding;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;
import jfxtras.labs.util.event.MouseControlUtil;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Custom connection skin.
 *
 * @author Ngoc Tran
 * @since 2019-04-02
 */
public class CustomFXConnectionSkin extends DefaultFXConnectionSkin {

    public CustomFXConnectionSkin(FXSkinFactory skinFactory,
                                  Parent parent,
                                  Connection connection,
                                  VFlow flow,
                                  String type) {
        super(skinFactory, parent, connection, flow, type);
    }

    protected void initSenderAndReceiver() {
        receiverConnectorUI = new Circle(15);

        // find the sender skin via lookup
        // TODO: replace lookup by direct reference?
        final CustomFXFlowNodeSkin senderSkin = (CustomFXFlowNodeSkin) getController().
                getNodeSkinLookup().getById(skinFactory,
                connection.getSender().getId());

        // retrieve the sender node from its skin
        senderShape = senderSkin.getConnectorShape(connection.getSender());

        // find the receiver skin via lookup
        // TODO: replace lookup by direct reference?
        CustomFXFlowNodeSkin receiverSkin = (CustomFXFlowNodeSkin) getController().
                getNodeSkinLookup().getById(skinFactory,
                connection.getReceiver().getId());

        // retrieve the receiver node from its skin
        receiverShape = receiverSkin.getConnectorShape(connection.getReceiver());

        // if we establish a connection between different flows
        // we have to create intermediate connections
        if (receiverShape.getNode().getParent() != senderShape.getNode().getParent()) {
            createIntermediateConnection(senderShape, receiverShape, connection);
        }

        setSender(getController().getNodeLookup().getConnectorById(
                connection.getSender().getId()));
        setReceiver(getController().getNodeLookup().getConnectorById(
                connection.getReceiver().getId()));
        setName(connection.getName());
    }

    @Override
    protected void initConnectionListener() {
        connectionListener
                = new CustomConnectionListener(
                skinFactory, controller, receiverConnectorUI);
    }

    @Override
    protected void initMouseEventHandler() {
        EventHandler<MouseEvent> contextMenuHandler = createContextMenuHandler(createContextMenu());
        connectionPath.addEventHandler(MouseEvent.MOUSE_CLICKED, contextMenuHandler);
        connectionPath.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                connectionPath.getStyleClass().setAll("selected-connection");
            }
        });
//        getReceiverUI().addEventHandler(MouseEvent.MOUSE_CLICKED, contextMenuHandler);

    } // end init

    @Override
    protected ContextMenu createContextMenu() {
        ContextMenu contextMenu = super.createContextMenu();
        contextMenu.setOnHiding(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                connectionPath.getStyleClass().setAll("vnode-connection");
            }
        });
        VNode sender = this.connection.getSender().getNode();
        VNode receiver = this.connection.getReceiver().getNode();
        ObjectBean senderObj = (ObjectBean) sender.getValueObject().getValue();
        ObjectBean receiverObj = (ObjectBean) receiver.getValueObject().getValue();
        ObservableList<Pair<String, Class>> outputs = senderObj.getOutputs();
        ObservableList<String> connNames = FXCollections.observableArrayList();
        for (Pair<String, Class> o : outputs){
            if (o.getValue() == receiverObj.getClass()){
                String connName = o.getKey();
                connNames.add(o.getKey());
                MenuItem item = new MenuItem(connName);
                item.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        connection.getConnectionText().setText(connName);
                        connection.setName(connName);

                        // Automatically generate variables names based on connection name
                        // For example, hasURN -> ?urn

                        // Generate operators and values for condition nodes
                        if (receiverObj.getClass() == ConditionNodeBean.class){
                            String pattern = "(diab:has)(\\w+)";
                            String v = connName.replaceAll(pattern, "$2");
                            v = v.toLowerCase();
                            receiverObj.setSparqlValue(v);

                            // Set title node
                            receiver.setTitle(v.substring(0,1).toUpperCase() + v.substring(1).toLowerCase());

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
                                if (dataType.getType() == DataType.Type.CATEGORY || dataType.getType() == DataType.Type.BOOLEAN) {
                                    cbValue.setVisible(true);
                                    textFieldValue.setVisible(false);
                                    borderPane.setCenter(cbValue);
                                    ArrayList<String> values = dataType.getValues();
                                    if (values.size() > 0){
                                        for (String val : dataType.getValues()){
                                            valueItems.add(val);
                                        }
                                        cbValue.setItems(valueItems);
                                    }
                                } if (dataType.getType() == DataType.Type.NUMERIC) {
                                    cbValue.setVisible(false);
                                    textFieldValue.setVisible(true);
                                    borderPane.setCenter(textFieldValue);
                                }
                            }
                        }
                    }
                });
                contextMenu.getItems().add(item);
            }
        }
        return contextMenu;
    }

    private MenuItem createMenuItem(String title) {
        MenuItem item = new MenuItem(title);
        item.setOnAction(event -> System.out.println(title));
        return item;
    }

    @Override
    protected EventHandler<MouseEvent> createContextMenuHandler(ContextMenu contextMenu) {
        return new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                contextMenu.show(getConnectionPath(), event.getScreenX(), event.getScreenY());
            }
        };
    }

    @Override
    protected void makeDraggable() {
        Logger.getLogger(DefaultFXConnectionSkin.class.getName()).
                log(Level.INFO, "makeDraggable");
        final DoubleBinding receiveXBinding = new DoubleBinding() {
            {
                super.bind(getReceiverShape().getNode().layoutXProperty(),
                        getReceiverShape().getNode().translateXProperty(),
                        getReceiverShape().radiusProperty());
            }

            @Override
            protected double computeValue() {

                return getReceiverShape().getNode().layoutXProperty().get()
                        + getReceiverShape().getNode().getTranslateX()
                        + getReceiverShape().getRadius();
            }
        };

        final DoubleBinding receiveYBinding = new DoubleBinding() {
            {
                super.bind(getReceiverShape().getNode().layoutYProperty(),
                        getReceiverShape().getNode().translateYProperty(),
                        getReceiverShape().radiusProperty());
            }

            @Override
            protected double computeValue() {

                return getReceiverShape().getNode().layoutYProperty().get()
                        + getReceiverShape().getNode().getTranslateY()
                        + getReceiverShape().getRadius();
            }
        };

        connectionPath.toFront();
        getReceiverUI().toFront();

        MouseControlUtil.makeDraggable(getReceiverUI(), (MouseEvent t) -> {
            receiverDraggingStarted = true;

            if (lastNode != null) {
//                    lastNode.setEffect(null);
                lastNode = null;
            }

            SelectedConnector selConnector
                    = FXConnectorUtil.getSelectedInputConnector(
                    getSender().getNode(),
                    getParent(), type, t);

            // reject connection if no main input defined for current node
            if (selConnector != null
                    && selConnector.getNode() != null
                    && selConnector.getConnector() == null) {
//                    DropShadow shadow = new DropShadow(20, Color.RED);
//                    Glow effect = new Glow(0.8);
//                    effect.setInput(shadow);
//                    selConnector.getNode().setEffect(effect);
                connectionListener.onNoConnection(selConnector.getNode());
                lastNode = selConnector.getNode();
            }

            if (selConnector != null
                    && selConnector.getNode() != null
                    && selConnector.getConnector() != null) {

                Node n = selConnector.getNode();
                n.toFront();
                Connector receiver = selConnector.getConnector();

                ConnectionResult connResult
                        = getSender().getNode().getFlow().tryConnect(
                        getSender(), receiver, getName());

                Connector receiverConnector = selConnector.getConnector();
                boolean isSameConnection = receiverConnector.equals(getReceiver());

                if (connResult.getStatus().isCompatible() || isSameConnection) {

//                        DropShadow shadow = new DropShadow(20, Color.WHITE);
//                        Glow effect = new Glow(0.5);
//                        shadow.setInput(effect);
//                        n.setEffect(shadow);
                    getReceiverUI().toFront();

                    if (lastNode != n) {
                        receiverConnectorUI.radiusProperty().unbind();
                        connectionListener.onConnectionCompatible(n);
                    }

                } else {

//                        DropShadow shadow = new DropShadow(20, Color.RED);
//                        Glow effect = new Glow(0.8);
//                        effect.setInput(shadow);
//                        n.setEffect(effect);
                    connectionListener.onConnectionIncompatible();
                }

                getReceiverUI().toFront();

                lastNode = n;

            } else if (lastNode == null) {
                receiverConnectorUI.radiusProperty().unbind();
                connectionListener.onNoConnection(receiverConnectorUI);
            }
        }, (MouseEvent event) -> {
            if (event.getButton() == javafx.scene.input.MouseButton.PRIMARY) {
                getReceiverUI().layoutXProperty().unbind();
                getReceiverUI().layoutYProperty().unbind();
                receiverConnectorUI.radiusProperty().unbind();
            }
            connection.getReceiver().click(NodeUtil.mouseBtnFromEvent(event), event);
            receiverDraggingStarted = false;
        });

        getReceiverUI().layoutXProperty().bind(receiveXBinding);
        getReceiverUI().layoutYProperty().bind(receiveYBinding);

        getReceiverUI().onMouseReleasedProperty().set(
                (EventHandler<MouseEvent>) (MouseEvent t) -> {
                    if (!receiverDraggingStarted) {
                        return;
                    }

                    if (lastNode != null) {
//                    lastNode.setEffect(null);
                        lastNode = null;
                    }

                    getReceiverUI().toFront();
                    connectionPath.toBack();

                    getReceiverUI().layoutXProperty().bind(receiveXBinding);
                    getReceiverUI().layoutYProperty().bind(receiveYBinding);

                    SelectedConnector selConnector
                            = FXConnectorUtil.getSelectedInputConnector(
                            getSender().getNode(), getParent(), type, t);

                    boolean isSameConnection = false;

                    if (selConnector != null
                            && selConnector.getNode() != null
                            && selConnector.getConnector() != null) {

                        Node n = selConnector.getNode();
                        n.toFront();
                        Connector receiverConnector = selConnector.getConnector();

                        isSameConnection = receiverConnector.equals(getReceiver());

                        if (!isSameConnection) {

                            ConnectionResult connResult = controller.connect(
                                    getSender(), receiverConnector);

                            if (connResult.getStatus().isCompatible()) {
                                connectionListener.onCreateNewConnectionReleased(connResult);
                            } else {
                                connectionListener.onConnectionIncompatibleReleased(n);
                            }
                        }

                    }

                    if (!isSameConnection) {

                        // remove error notification etc.
                        if (controller.getConnections(type).contains(connection.getSender(),
                                connection.getReceiver())) {
                            connectionListener.onNoConnection(receiverConnectorUI);
                        }

                        remove();
                        connection.getConnections().remove(connection);
                    } else if (getReceiverShape() instanceof ConnectorShape) {
                        ConnectorShape recConnNode = getReceiverShape();

                        if (getReceiverUI() instanceof Circle) {
                            ((Circle) getReceiverUI()).radiusProperty().unbind();
                            FXConnectorUtil.stopTimeLine();
                            ((Circle) getReceiverUI()).radiusProperty().
                                    bind(recConnNode.radiusProperty());
                            initStyle();
                        }
                    }
                });
    }

    private void createIntermediateConnection(ConnectorShape senderNode, ConnectorShape receiverNode, Connection connection) {
        VNode sender = connection.getSender().getNode();
        VNode receiver = connection.getReceiver().getNode();

        throw new UnsupportedOperationException("Cannot visualize connection with different parent flows!");
    }

}
