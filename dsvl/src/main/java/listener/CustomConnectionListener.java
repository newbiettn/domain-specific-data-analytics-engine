package listener;

import beans.ConditionNodeBean;
import beans.ObjectBean;
import controllers.ConditionNodeController;
import eu.mihosoft.vrl.workflow.Connection;
import eu.mihosoft.vrl.workflow.ConnectionResult;
import eu.mihosoft.vrl.workflow.VFlow;
import eu.mihosoft.vrl.workflow.VNode;
import eu.mihosoft.vrl.workflow.fx.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.Glow;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Path;
import javafx.util.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Describe class purpose here.
 *
 * @author Ngoc Tran
 * @since 2019-05-04
 */
public class CustomConnectionListener implements ConnectionListener {
    private static Logger logger = LoggerFactory.getLogger(CustomConnectionListener.class);
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
        logger.info("onConnectionCompatible");
        FXConnectorUtil.connectAnim(receiverConnectorUI, n);
    }

    @Override
    public void onConnectionCompatibleReleased(Node n) {
        logger.info("onConnectionCompatibleReleased");
        FXConnectorUtil.connectAnim(receiverConnectorUI, n);
    }

    @Override
    public void onConnectionIncompatible() {
        logger.info("onConnectionIncompatible");
        FXConnectorUtil.incompatibleAnim(receiverConnectorUI);
    }

    @Override
    public void onConnectionIncompatibleReleased(Node n) {
        logger.info("onConnectionIncompatibleReleased");
    }

    @Override
    public void onCreateNewConnectionReleased(ConnectionResult connResult) {
        logger.info("onCreateNewConnectionReleased");
        newConnectionAnim(connResult);
        updateConnectionName(connResult.getConnection());
    }

    @Override
    public void onCreateNewConnectionReverseReleased(ConnectionResult connResult) {
        logger.info("onCreateNewConnectionReverseReleased");
        newConnectionReverseAnim(connResult);
    }

    @Override
    public void onNoConnection(Node n) {
        logger.info("onNoConnection");
        FXConnectorUtil.unconnectAnim(receiverConnectorUI);
    }

    @Override
    public void onRemoveConnectionReleased() {
        logger.info("onRemoveConnectionReleased");
        FXConnectorUtil.unconnectAnim(receiverConnectorUI);
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

    /**
     * Automatically update connection name if there is only 1 name choice for the connection.
     *
     * @param conn
     */
    private void updateConnectionName(Connection conn){
        VNode sender = conn.getSender().getNode();
        VNode receiver = conn.getReceiver().getNode();
        ObjectBean senderObj = (ObjectBean) sender.getValueObject().getValue();
        ObjectBean receiverObj = (ObjectBean) receiver.getValueObject().getValue();
        ObservableList<Pair<String, Class>> outputs = senderObj.getOutputs();
        ObservableList<String> connNames = FXCollections.observableArrayList();
        for (Pair<String, Class> o : outputs){
            if (o.getValue() == receiverObj.getClass())
                connNames.add(o.getKey());
        }
        if (connNames.size() == 1){
            conn.getConnectionText().setText(connNames.get(0));
            conn.setName(connNames.get(0));
        }
    }

    /**
     *
     * @param conn
     */
    private void updateChoiceBox(Connection conn){
        VNode sender = conn.getSender().getNode();
        VNode receiver = conn.getReceiver().getNode();
        ObjectBean senderObj = (ObjectBean) sender.getValueObject().getValue();
        ObjectBean receiverObj = (ObjectBean) receiver.getValueObject().getValue();
        ObservableList<Pair<String, Class>> outputs = senderObj.getOutputs();
        ObservableList<String> connNames = FXCollections.observableArrayList();

        if (receiverObj.getClass() == ConditionNodeBean.class){
            ConditionNodeController controller = (ConditionNodeController) receiver.getController();
            ChoiceBox<String> cbOperator = controller.getCbOperator();
            ObservableList<String> operators = FXCollections.observableArrayList();

            cbOperator.setItems(operators);

        }
    }
}
