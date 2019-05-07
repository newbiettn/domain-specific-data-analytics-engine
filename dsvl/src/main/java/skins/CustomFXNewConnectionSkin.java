package skins;

import beans.ObjectBean;
import controllers.CustomConnectionListener;
import eu.mihosoft.vrl.workflow.*;
import eu.mihosoft.vrl.workflow.fx.*;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;
import javafx.scene.shape.CubicCurveTo;
import jfxtras.labs.util.event.MouseControlUtil;

/**
 * Describe class purpose here.
 *
 * @author Ngoc Tran
 * @since 2019-05-06
 */
public class CustomFXNewConnectionSkin extends AbstractFXConnectionSkin {
    private CubicCurveTo curveTo = new CubicCurveTo();
    private ConnectorShape senderConnectorUI;
    private VFlowModel flow;
    private Node lastNode;

    public CustomFXNewConnectionSkin(FXSkinFactory skinFactory,
                                     Parent parent, Connector sender, VFlow controller, String type) {
        super(skinFactory, parent, controller, type);
        setSender(sender);
        flow = controller.getModel();
    }

    protected void initStyle() {
        connectionPath.getStyleClass().setAll(
                "vnode-new-connection",
                "vnode-new-connection-" + getSender().getType());
        receiverConnectorUI.getStyleClass().setAll(
                "vnode-new-connection-receiver",
                "vnode-new-connection-receiver-" + getSender().getType());

        receiverConnectorUI.setStrokeWidth(3);
        //
    }

    @Override
    protected void initConnectionListener() {
        System.out.println("initConnectionListener");
        connectionListener
                = new CustomConnectionListener(
                skinFactory, controller, receiverConnectorUI);
    }

    protected void initSenderAndReceiver() {
        receiverConnectorUI = new Circle(15);
        final VNode sender = getSender().getNode();
        final CustomFXFlowNodeSkin senderSkin = (CustomFXFlowNodeSkin) getController().
                getNodeSkinLookup().getById(skinFactory, sender.getId());

        senderShape = senderSkin.getConnectorShape(getSender());
        final Node senderNode = senderShape.getNode();

        senderConnectorUI = senderShape;

        receiverConnectorUI.setLayoutX(senderNode.getLayoutX()
                + receiverConnectorUI.getRadius());
        receiverConnectorUI.setLayoutY(senderNode.getLayoutY()
                + receiverConnectorUI.getRadius());
    }

    protected void makeDraggable() {
        connectionPath.toFront();
        receiverConnectorUI.toFront();

        MouseControlUtil.makeDraggable(receiverConnectorUI, (MouseEvent t) -> {

            if (lastNode != null) {
//                    lastNode.setEffect(null);
                lastNode = null;
            }

            SelectedConnector selConnector = null;

            if (getSender().isOutput()) {
                selConnector = FXConnectorUtil.getSelectedInputConnector(
                        getSender().getNode(), getParent(), type, t);
            } else {
                selConnector = FXConnectorUtil.getSelectedOutputConnector(
                        getSender().getNode(), getParent(), type, t);
            }

            // reject connection if no main input defined for current node
            if (selConnector != null
                    && selConnector.getNode() != null
                    && selConnector.getConnector() == null) {
//                    DropShadow shadow = new DropShadow(20, Color.RED);
//                    Glow effect = new Glow(0.8);
//                    effect.setInput(shadow);
//                    selConnector.getNode().setEffect(effect);

                //onConnectionIncompatible();
                connectionListener.onNoConnection(selConnector.getNode());

                lastNode = selConnector.getNode();
            }

            if (selConnector != null
                    && selConnector.getNode() != null
                    && selConnector.getConnector() != null) {

                Connector receiverConnectorModel = selConnector.getConnector();
                Node n = selConnector.getNode();
                n.toFront();

                VNode model = selConnector.getConnector().getNode();

//                    // we cannot create a connection from us to us
//                    if (model == getSender()) {
//                        return;
//                    }
                ConnectionResult connResult = null;

                if (getSender().isInput() && receiverConnectorModel.isOutput()) {

                    connResult = flow.tryConnect(
                            receiverConnectorModel, getSender(), getName());
                } else {
                    connResult = flow.tryConnect(
                            getSender(), receiverConnectorModel, getName());
                }

                if (connResult.getStatus().isCompatible()) {
                    if (lastNode != n) {
                        connectionListener.onConnectionCompatible(n);
                    }

                } else {
//                        DropShadow shadow = new DropShadow(20, Color.RED);
//                        Glow effect = new Glow(0.8);
//                        effect.setInput(shadow);
//                        n.setEffect(effect);
                    connectionListener.onConnectionIncompatible();
                }

                receiverConnectorUI.toFront();

                lastNode = n;
            } else {
                if (lastNode == null) {
                    connectionListener.onNoConnection(receiverConnectorUI);
                }
            }
        }, (MouseEvent event) -> {
            receiverConnectorUI.layoutXProperty().unbind();
            receiverConnectorUI.layoutYProperty().unbind();
        }, true);

        receiverConnectorUI.onMouseReleasedProperty().set(
                (EventHandler<MouseEvent>) (MouseEvent t) -> {
                    receiverConnectorUI.toBack();
                    connectionPath.toBack();

                    if (lastNode != null) {
                        lastNode = null;
                    }

                    SelectedConnector selConnector = null;

                    if (getSender().isOutput()) {
                        selConnector = FXConnectorUtil.getSelectedInputConnector(
                                getSender().getNode(), getParent(), type, t);
                    } else {
                        selConnector = FXConnectorUtil.getSelectedOutputConnector(
                                getSender().getNode(), getParent(), type, t);
                    }

                    if (selConnector != null
                            && selConnector.getNode() != null
                            && selConnector.getConnector() != null) {

                        Node n = selConnector.getNode();

                        n.toFront();

                        Connector receiverConnector = selConnector.getConnector();

                        ConnectionResult connResult = null;

                        if (getSender().isInput() && receiverConnector.isOutput()) {
                            connResult = flow.connect(receiverConnector, getSender(), getName());
                            connectionListener.
                                    onCreateNewConnectionReverseReleased(connResult);

                        } else {
                            connResult = flow.connect(getSender(), receiverConnector, getName());
                            connectionListener.onCreateNewConnectionReleased(connResult);
                        }

                        if (!connResult.getStatus().isCompatible()) {
                            connectionListener.onConnectionIncompatibleReleased(n);
                        }
                    }

                    remove();
                });
    }

    @Override
    public final void setSender(Connector n) {

        if (n == null) {
            throw new IllegalArgumentException("Sender 'null' not supported.");
        }

        senderProperty.set(n);
    }

    @Override
    public void setController(VFlow flow) {
        super.setController(flow);
        this.flow = flow.getModel();
    }
}