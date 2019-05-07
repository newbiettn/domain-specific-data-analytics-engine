package controllers;

import eu.mihosoft.vrl.workflow.Connection;
import eu.mihosoft.vrl.workflow.ConnectionResult;
import eu.mihosoft.vrl.workflow.VFlow;
import eu.mihosoft.vrl.workflow.VNode;
import eu.mihosoft.vrl.workflow.fx.*;
import eu.mihosoft.vrl.workflow.skin.VNodeSkin;
import javafx.scene.Node;

import java.util.List;

/**
 * Describe class purpose here.
 *
 * @author Ngoc Tran
 * @since 2019-05-04
 */
public class CustomConnectionListener implements ConnectionListener {
    private final Node receiverConnectorUI;
    private final FXSkinFactory skinFactory;
    private final VFlow flowController;

    public CustomConnectionListener(FXSkinFactory skinFactory, VFlow vflow, Node receiverConnectorUI) {
        this.skinFactory = skinFactory;
        this.flowController = vflow;
        this.receiverConnectorUI = receiverConnectorUI;
    }

    @Override
    public void onConnectionCompatible(Node n) {
        FXConnectorUtil.connectAnim(receiverConnectorUI, n);
//        System.out.println("onConnectionCompatible");
    }

    @Override
    public void onConnectionCompatibleReleased(Node n) {
//        System.out.println("onConnectionCompatibleReleased");
        FXConnectorUtil.connectAnim(receiverConnectorUI, n);
    }

    @Override
    public void onConnectionIncompatible() {
//        System.out.println("onConnectionIncompatible");
        FXConnectorUtil.incompatibleAnim(receiverConnectorUI);
    }

    @Override
    public void onConnectionIncompatibleReleased(Node n) {
//        System.out.println("onConnectionIncompatibleReleased");
    }

    @Override
    public void onCreateNewConnectionReleased(ConnectionResult connResult) {
//        System.out.println("onCreateNewConnectionReleased");
        newConnectionAnim(connResult);
    }

    @Override
    public void onCreateNewConnectionReverseReleased(ConnectionResult connResult) {
//        System.out.println("onCreateNewConnectionReverseReleased");

    }

    @Override
    public void onNoConnection(Node n) {
//        System.out.println("onNoConnection");
    }

    @Override
    public void onRemoveConnectionReleased() {
//        System.out.println("onRemoveConnectionReleased");
    }
    private void newConnectionAnim(ConnectionResult connResult) {
        if (connResult.getConnection() != null) {
            FXConnectionSkin connectionSkin
                    = (FXConnectionSkin) flowController.getNodeSkinLookup().getById(
                    skinFactory, connResult.getConnection());
            FXConnectorUtil.connnectionEstablishedAnim(connectionSkin.getReceiverUI());
        }
    }

    private void newConnectionReverseAnim(ConnectionResult connResult) {
        // System.out.println("new-connection anim (reverse)");
        if (connResult.getConnection() != null) {
            FXConnectionSkin connectionSkin
                    = (FXConnectionSkin) flowController.getNodeSkinLookup().getById(
                    skinFactory, connResult.getConnection());

            FXConnectorUtil.connnectionEstablishedAnim(connectionSkin.getSenderShape().getNode());
        }
    }

}
