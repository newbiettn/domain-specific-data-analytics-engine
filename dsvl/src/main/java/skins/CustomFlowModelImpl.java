package skins;

import beans.ObjectBean;
import eu.mihosoft.vrl.workflow.*;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.util.Pair;

import java.lang.reflect.Constructor;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Describe class purpose here.
 *
 * @author Ngoc Tran
 * @since 2019-05-06
 */
public class CustomFlowModelImpl implements FlowModel {

    private final ObservableMap<String, Connections> connections
            = FXCollections.observableHashMap();
    private final ObservableMap<String, Connections> readOnlyObservableConnections
            = FXCollections.unmodifiableObservableMap(connections);
    private final ObservableList<VNode> observableNodes
            = FXCollections.observableArrayList();
    private final ObservableList<VNode> readOnlyObservableNodes
            = FXCollections.unmodifiableObservableList(observableNodes);
    private final Map<String, VNode> nodes = new HashMap<>();
    private Class<? extends VNode> flowNodeClass = VNodeImpl.class;
    private final BooleanProperty visibleProperty = new SimpleBooleanProperty();
    private IdGenerator idGenerator;
    private NodeLookup nodeLookup;

    @Override
    public BooleanProperty visibleProperty() {
        return visibleProperty;
    }

    @Override
    public void setVisible(boolean b) {
        visibleProperty.set(b);
    }

    @Override
    public boolean isVisible() {
        return visibleProperty.get();
    }

    // TODO duplicated code
    private static String connectionId(String id, String s, String r) {
        return "id=" + id + ";[" + s + "]->[" + r + "]";
    }

    // TODO duplicated code
    private static String connectionId(Connection c) {
        return connectionId(c.getId(), c.getSender().getId(), c.getReceiver().getId());
    }

    @Override
    public ConnectionResult tryConnect(VNode s, VNode r, String type, String name) {

        ValueObject senderValObj = new NoDefaultConnectorValueObject(s);
        ValueObject receiverValObj = new NoDefaultConnectorValueObject(r);

        if (s.getMainOutput(type) != null) {
            senderValObj = s.getMainOutput(type).getValueObject();
        }

        if (r.getMainInput(type) != null) {
            receiverValObj = r.getMainInput(type).getValueObject();
        }

        CompatibilityResult result = receiverValObj.
                compatible(senderValObj, type);

        return new ConnectionResultImpl(result, null);
    }

    @Override
    public ConnectionResult connect(VNode s, VNode r, String type, String name) {

        ConnectionResult result = tryConnect(s, r, type, name);

        if (!result.getStatus().isCompatible()) {
            return result;
        }

        Connector sender = null;
        Connector receiver = null;

        if (s.getMainOutput(type) != null) {
            sender = s.getMainOutput(type);
        }

        if (r.getMainInput(type) != null) {
            receiver = r.getMainInput(type);
        }

//        System.out.println("ADD: " + sender + ", " + receiver);
        Connection connection = getConnections(type).add(sender, receiver, name);



        return new ConnectionResultImpl(result.getStatus(), connection);
    }

    @Override
    public ConnectionResult tryConnect(Connector s, Connector r, String name) {
        CompatibilityResult result = r.getValueObject().
                compatible(s.getValueObject(), s.getType());

        CompatibilityResult customCompatibleResult = new CompatibilityResult() {
            @Override
            public boolean isCompatible() {
                boolean isReceiverCompatible = false;
                ObjectBean senderBean = (ObjectBean) s.getNode().getValueObject().getValue();
                ObjectBean receiverBean = (ObjectBean) r.getNode().getValueObject().getValue();
                ObservableList<Pair<String, Class>> senderOutputs = senderBean.getOutputs();
                for (Pair<String, Class> o : senderOutputs){
                    if (o.getValue() == receiverBean.getClass()){
                        isReceiverCompatible = true;
                    }
                }
                if (isReceiverCompatible && result.isCompatible())
                    return true;
                else
                    return false;
            }

            @Override
            public String getMessage() {
                return null;
            }

            @Override
            public String getStatus() {
                return null;
            }
        };

        return new ConnectionResultImpl(customCompatibleResult, null);
    }

    @Override
    public ConnectionResult connect(Connector s, Connector r, String name) {
        ConnectionResult result = tryConnect(s, r, name);

        if (!result.getStatus().isCompatible()) {
            return result;
        }

        Connection connection = getConnections(s.getType()).add(s, r, name);

        return new ConnectionResultImpl(result.getStatus(), connection);
    }

    @Override
    public ObservableList<VNode> getNodes() {
        return readOnlyObservableNodes;
    }

    @Override
    public void clear() {
        List<VNode> delList = new ArrayList<>(observableNodes);

        for (VNode n : delList) {
            remove(n);
        }
    }

    @Override
    public VNode remove(VNode n) {

//        if (n instanceof FlowModel) {
//            ((FlowModel)n).clear();
//        }
        VNode result = nodes.remove(n.getId());
        observableNodes.remove(n);

//        removeNodeSkin(n);
        for (Connections cns : getAllConnections().values()) {

            Collection<Connection> connectionsToRemove
                    = cns.getAllWithNode(n);

            for (Connection c : connectionsToRemove) {
                cns.remove(c);
//                removeConnectionSkin(c);
            }
        }

        return result;
    }

    @Override
    public ObservableMap<String, Connections> getAllConnections() {
        return readOnlyObservableConnections;
    }

    //TODO unmodifiable connection object?
    @Override
    public Connections getConnections(String type) {
        Connections result = connections.get(type);

        if (result == null) {
            addConnections(VConnections.newConnections(type), type);
            result = connections.get(type);
        }

        return result;
    }

    @Override
    public VNode getSender(Connection c) {
        return getNodeLookup().getById(c.getSender().getId());
    }

    @Override
    public VNode getReceiver(Connection c) {
        return getNodeLookup().getById(c.getReceiver().getId());
    }

    @Override
    public void setFlowNodeClass(Class<? extends VNode> cls) {
        try {
            Constructor constructor = cls.getConstructor(FlowModel.class);
            throw new IllegalArgumentException("constructor missing: (String, String)");
        } catch (NoSuchMethodException | SecurityException ex) {
            Logger.getLogger(ConnectionsImpl.class.getName()).log(Level.SEVERE, null, ex);
        }

        this.flowNodeClass = cls;
    }

    @Override
    public Class<? extends VNode> getFlowNodeClass() {
        return flowNodeClass;
    }

    VNode newNode(VNode result, ValueObject obj, String parentId) {

        result.setValueObject(obj);

        if (getIdGenerator() == null) {
            throw new IllegalStateException("Please define an idgenerator before creating nodes!");
        }

        String id = getIdGenerator().newId(parentId+":");

        result.setId(id);

        nodes.put(id, result);
        observableNodes.add(result);

        return result;
    }

    @Override
    public void addConnections(Connections connections, String flowType) {
        this.connections.put(flowType, connections);
    }

    @Override
    public VisualizationRequest getVisualizationRequest() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setVisualizationRequest(VisualizationRequest vReq) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * @return the idGenerator
     */
    @Override
    public IdGenerator getIdGenerator() {
        return idGenerator;
    }

    /**
     * @param idGenerator the idGenerator to set
     */
    @Override
    public void setIdGenerator(IdGenerator idGenerator) {
        this.idGenerator = idGenerator;
    }

    /**
     * @return the nodeLookup
     */
    @Override
    public NodeLookup getNodeLookup() {
        return nodeLookup;
    }

    /**
     * @param nodeLookup the nodeLookup to set
     */
    @Override
    public void setNodeLookup(NodeLookup nodeLookup) {
        this.nodeLookup = nodeLookup;
    }

    @Override
    public ReadOnlyProperty<VisualizationRequest> visualizationRequestProperty() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isVisualizationRequestInitialized() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
