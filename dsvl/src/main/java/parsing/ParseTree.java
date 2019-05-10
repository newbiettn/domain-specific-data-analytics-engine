package parsing;

import beans.ObjectBean;
import beans.SelectNodeBean;
import controllers.MainController;
import eu.mihosoft.vrl.workflow.*;
import javafx.util.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Represents parse trees for dsvl.
 *
 * @author Ngoc Tran
 * @since 2019-05-09
 */
public class ParseTree {
    private static Logger logger = LoggerFactory.getLogger(ParseTree.class);
    private Node root;
    private StringBuilder sparqlQuery;

    public ParseTree(){
        this.root = null;
        this.sparqlQuery = null;
    }

    /**
     * Print to SPARQL format.
     *
     * @param node
     */
    public void parseToSPARQL(Node node, int depth){
        if (node == null)
            return;

        /* first print data of node */
        VNode vNode = node.getVNode();
        ObjectBean objectBean = (ObjectBean) vNode.getValueObject().getValue();
        sparqlQuery.append(objectBean.getSparqlValue());

        if (node.getChildren().size() > 0) {
            depth++;
            if (depth == 1){
                for(Pair<String, Node> p : node.getChildren()){
                    Node child = p.getValue();
                    sparqlQuery.append(" ");
                    parseToSPARQL(child, depth);
                }
            } else {
                if (depth == 2)
                    sparqlQuery.append(" WHERE { ");

                for(Pair<String, Node> p : node.getChildren()){
                    Node child = p.getValue();
                    VNode vChild = child.getVNode();
                    String connectionName = p.getKey();

                    sparqlQuery.append("\n");
                    sparqlQuery.append(objectBean.getSparqlValue());
                    sparqlQuery.append(" " + connectionName + " ");
                    parseToSPARQL(child, depth);
                }

                if (depth == 2)
                    sparqlQuery.append(" } ");
            }
        }

    }

    /**
     * Wrapper function for printSPARQL.
     */
    public boolean parseToSPARQL(){
        sparqlQuery = new StringBuilder();
        /* If not appropriate root to parse to SPARQL (e.g., SELECT, ASK, ...) */
        if (root == null) {
            logger.error("Require appropriate root nodes (SELECT, ASK, ...) to parse to SPARQL");
            return false;
        }

        ObjectBean objectBean = (ObjectBean) root.getVNode().getValueObject().getValue();
        if (objectBean.getClass() != SelectNodeBean.class){
            logger.error("Require appropriate root nodes (SELECT, ASK, ...) to parse to SPARQL");
            return false;
        }

        parseToSPARQL(root, 0);
        logger.info("\n" + sparqlQuery.toString());
        return true;
    }

    /**
     * Print tree in preorder.
     * @param node
     */
    public void printPreorder(Node node, int depth) {
        if (node == null)
            return;

        /* first print data of node */
        System.out.print(node.getId());

        if (node.getChildren().size() > 0) {
            System.out.print(" -> [");
            depth++;
            for(Pair<String, Node> p : node.getChildren()){
                Node child = p.getValue();
                String connectionName = p.getKey();
                System.out.print("\n");
                for (int i = 0; i<depth; i++){
                    System.out.print("\t");
                }
                System.out.print(connectionName + "->");
                printPreorder(child, depth);
            }
            System.out.print(" ] ");
        }
    }

    /**
     * Wrapper method for printing the parse tree.
     */
    public void printPreorder(){
        printPreorder(root, 0);
        System.out.print("\n");
    }

    /**
     * Parse the flow.
     * @param flow
     */
    public void parse(VFlow flow){
        VNode vRoot = null;
        for(VNode v : flow.getNodes()){
            if (v.getInputs().size() == 0){
                vRoot = v;
            }
        }
        /* If no head root*/
        if (vRoot == null)
            return;

        /* If not appropriate root (i.e., SELECT, ASK, ...) */
        ObjectBean objectBean = (ObjectBean) vRoot.getValueObject().getValue();
        if (objectBean.getClass() != SelectNodeBean.class)
            return;

        root = new Node(vRoot);
        Connections connections = flow.getConnections(MainController.CONNECTION_NAME);
        addChild(connections, root, vRoot);
    }

    /**
     * Recursively add children to node.
     *
     * @param connections
     * @param parent
     * @param vParent
     */
    private void addChild(Connections connections, Node parent, VNode vParent){
        Connector outputConnector = vParent.getMainOutput(MainController.CONNECTION_NAME);
        /* If reaching no output nodes */
        if (outputConnector == null)
            return;

        /* If reaching nodes having output connectors but no output connections */
        Collection<Connection> connectionCollection = connections.getAllWith(outputConnector);
        if (connectionCollection.size() == 0 || connectionCollection == null)
            return;

        ArrayList<Pair<String, Node>> children = new ArrayList<>();
        parent.setChildren(children);
        for (Connection c : connectionCollection){
            /* Add children*/
            VNode vChild = c.getReceiver().getNode();
            String connectionName = c.getName();
            Node child = new Node(vChild);
            addChild(connections, child, vChild);
            children.add(new Pair(connectionName, child));
        }
    }

}
