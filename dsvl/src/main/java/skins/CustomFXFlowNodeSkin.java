package skins;

import eu.mihosoft.vrl.workflow.*;
import eu.mihosoft.vrl.workflow.fx.ConnectorShape;
import eu.mihosoft.vrl.workflow.fx.FXFlowNodeSkin;
import eu.mihosoft.vrl.workflow.fx.FXSkinFactory;
import eu.mihosoft.vrl.workflow.fx.NodeUtil;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;

import java.util.Optional;

/**
 * Describe class purpose here.
 *
 * @author Ngoc Tran
 * @since 2019-05-06
 */
public class CustomFXFlowNodeSkin extends FXFlowNodeSkin {
    public CustomFXFlowNodeSkin(FXSkinFactory skinFactory, Parent parent, VNode model, VFlow controller) {
        super(skinFactory, parent, model, controller);
    }

    protected void addConnector(final Connector connector) {
        connectorList.add(connector);
        ConnectorShape connectorShape = createConnectorShape(connector);

        final Node connectorNode = connectorShape.getNode();
        connectorNode.setManaged(false);

        connectors.put(connector, connectorShape);
//--------------------B
        Optional<Boolean> preferTD = connector.getVisualizationRequest().
                get(VisualizationRequest.KEY_CONNECTOR_PREFER_TOP_DOWN);
        boolean preferTopDown = preferTD.orElse(false);
        int inputDefault = preferTopDown ? TOP : LEFT;
        int outputDefault = preferTopDown ? BOTTOM : RIGHT;
//--------------------E
        if (connector.isInput()) {
//            inputList.add(connectorNode);
            shapeLists.get(inputDefault).add(connectorShape);
            connectorToIndexMap.put(connector, inputDefault);
        } else if (connector.isOutput()) {
//            outputList.add(connectorNode);
            shapeLists.get(outputDefault).add(connectorShape);
            connectorToIndexMap.put(connector, outputDefault);
        }

        node.boundsInLocalProperty().addListener((ov, oldValue, newValue) -> {
            computeConnectorSizes();
            adjustConnectorSize();
        });

        NodeUtil.addToParent(getParent(), connectorNode);

        connectorNode.onMouseEnteredProperty().set(
                (EventHandler<MouseEvent>) (MouseEvent t) -> {
                    connectorNode.toFront();
                });

        connectorNode.onMousePressedProperty().set(
                (EventHandler<MouseEvent>) (MouseEvent t) -> {
                    // we are already connected and manipulate the existing connection
                    // rather than creating a new one
                    if (controller.getConnections(connector.getType()).
                            isInputConnected(connector)) {
                        return;
                    }

                    t.consume();
                    newConnectionPressEvent = t;
                });

        connectorNode.onMouseDraggedProperty().set(
                (EventHandler<MouseEvent>) (MouseEvent t) -> {
                    if (connectorNode.isMouseTransparent()) {
                        return;
                    }

                    // we are already connected and manipulate the existing connection
                    // rather than creating a new one
                    if (controller.getConnections(connector.getType()).
                            isInputConnected(connector)) {
                        return;
                    }

                    int numOfExistingConnections = connector.getNode().getFlow().
                            getConnections(connector.getType()).
                            getAllWith(connector).size();

                    if (numOfExistingConnections < connector.
                            getMaxNumberOfConnections()) {

                        if (newConnectionSkin == null) {
                            newConnectionSkin
                                    = new CustomFXNewConnectionSkin(getSkinFactory(),
                                    getParent(), connector,
                                    getController(), connector.getType()).init();

                            newConnectionSkin.add();

                            MouseEvent.fireEvent(
                                    newConnectionSkin.getReceiverUI(),
                                    newConnectionPressEvent);
                        }

                        t.consume();
                        MouseEvent.fireEvent(
                                newConnectionSkin.getReceiverUI(), t);

                        t.consume();
                        MouseEvent.fireEvent(
                                newConnectionSkin.getReceiverUI(), t);
                    }
                });

        connectorNode.onMouseReleasedProperty().set(
                (EventHandler<MouseEvent>) (MouseEvent t) -> {
                    if (connectorNode.isMouseTransparent()) {
                        return;
                    }

                    connector.click(NodeUtil.mouseBtnFromEvent(t), t);

                    // we are already connected and manipulate the existing connection
                    // rather than creating a new one
                    if (controller.getConnections(connector.getType()).
                            isInputConnected(connector)) {
                        return;
                    }

                    t.consume();
                    try {
                        MouseEvent.fireEvent(
                                newConnectionSkin.getReceiverUI(), t);
                    } catch (Exception ex) {
                        // TODO exception is not critical here (node already removed)
                    }

                    newConnectionSkin = null;
                });
    }
}
