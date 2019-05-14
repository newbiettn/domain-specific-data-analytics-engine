package parsing;

import beans.*;
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
    private final String NL = "\n";
    private final String TAB = "\t";
    private final String SPACE = " ";
    private final String DOT = ".";
    private Node root;
    private StringBuilder sparqlQuery;

    public ParseTree(){
        this.root = null;
        this.sparqlQuery = null;
    }

    /**
     * Interpret to SPARQL.
     *
     * @param node
     */
    private void interpret(Node node, int depth){
        if (node == null)
            return;

        /* first print data of node */
        VNode vNode = node.getVNode();
        ObjectBean objectBean = (ObjectBean) vNode.getValueObject().getValue();
        if (depth == 0){
            sparqlQuery.append(objectBean.getSparqlValue());
            ArrayList<String> variables = getVariables();
            for (String v : variables){
                sparqlQuery.append(SPACE)
                        .append(v)
                        .append(SPACE);
            }
        }
        if (depth > 1) {
            sparqlQuery.append(objectBean.getSparqlValue());
            sparqlQuery.append(DOT);
        }

        if (node.getChildren().size() > 0) {
            depth++;
            if (depth == 1){
                for(Pair<String, Node> p : node.getChildren()){
                    Node child = p.getValue();
                    sparqlQuery.append(SPACE);
                    interpret(child, depth);
                }
            } else {
                if (depth == 2){
                    sparqlQuery.append(SPACE)
                                .append("WHERE {")
                                .append(NL);
                    if (objectBean.getClass() == PatientNodeBean.class)
                        sparqlQuery.append(objectBean.getSparqlValue())
                                    .append(" rdf:type ").append("diab:Patient.");

                }
                for(Pair<String, Node> p : node.getChildren()){
                    Node child = p.getValue();
                    String connectionName = p.getKey();

                    sparqlQuery.append(NL);
                    sparqlQuery.append(objectBean.getSparqlValue());
                    sparqlQuery.append(SPACE)
                                .append(connectionName)
                                .append(SPACE);
                    interpret(child, depth);
                }

                if (depth == 2)
                    sparqlQuery.append(" } ");
            }
        }

    }

    /**
     * To interpret simple flows with only two nodes, such as: SELECT -> Patient
     *
     * @param node
     */
    private void interpret(Node node){
        if (node == null)
            return;

        /* first print data of node */
        VNode vNode = node.getVNode();
        ObjectBean objectBean = (ObjectBean) vNode.getValueObject().getValue();
        sparqlQuery.append(objectBean.getSparqlValue());
        if (node.getChildren().size() > 0) {
            for(Pair<String, Node> p : node.getChildren()) {
                Node child = p.getValue();
                ObjectBean ob = (ObjectBean) child.getVNode().getValueObject().getValue();
                sparqlQuery.append(ob.getSparqlValue()).append(" ");
            }
            sparqlQuery.append(" WHERE { ");
            for(Pair<String, Node> p : node.getChildren()) {
                    Node child = p.getValue();
                    ObjectBean ob = (ObjectBean) child.getVNode().getValueObject().getValue();
                    if (ob.getClass() == PatientNodeBean.class) {
                        sparqlQuery.append(ob.getSparqlValue())
                            .append(" rdf:type ").append("diab:Patient.");
                    } else if (ob.getClass() == EpisodeNodeBean.class) {
                        sparqlQuery.append(ob.getSparqlValue())
                            .append(" rdf:type ").append("diab:Episode.");
                    }
            }
            sparqlQuery.append("}");
        }

    }

    /**
     * Wrapper function for interpret.
     */
    public String interpret(){
        sparqlQuery = new StringBuilder();
        /* If not appropriate root to parse to SPARQL (e.g., SELECT, ASK, ...) */
        if (root == null) {
            logger.error("Require appropriate root nodes (SELECT, ASK, ...) to interpret to SPARQL");
            return null;
        }

        ObjectBean objectBean = (ObjectBean) root.getVNode().getValueObject().getValue();
        if (objectBean.getClass() != SelectNodeBean.class){
            logger.error("Require appropriate root nodes (SELECT, ASK, ...) to interpret to SPARQL");
            return null;
        }

        int treeDepth = getDepth();
        logger.info("Depth of the tree " + treeDepth);

        if (treeDepth > 1)
            interpret(root, 0);
        else
            interpret(root);

        logger.info("\n" + sparqlQuery.toString());
        return sparqlQuery.toString();
    }

    /**
     * Get all variables to display in the table result.
     * @return
     */
    private ArrayList<String> getVariables(ArrayList<String> variables, Node node, int depth){
        if (node == null)
            return variables;

        if (node.getChildren().size() > 0) {
            depth++;
            for(Pair<String, Node> p : node.getChildren()){
                Node child = p.getValue();
                ObjectBean ob = (ObjectBean) child.getVNode().getValueObject().getValue();
                if (ob.getClass() != ConditionNodeBean.class) {
                    String variableName = ob.getSparqlValue();
                    variables.add(variableName);
                } else {
                    ConditionNodeBean conditionNodeBean = (ConditionNodeBean) ob;
                    if (conditionNodeBean.getVariable().isEmpty()){
                        String variableName = ob.getSparqlValue();
                        variables.add(variableName);
                    }
                }
                variables = getVariables(variables, child, depth);
            }
        }
        return variables;

    }

    /**
     * Get all variables to display in the table result (a wrapper).
     * @return
     */
    private ArrayList<String> getVariables(){
        ArrayList<String> variables = new ArrayList<>();
        return getVariables(variables, root, 0);
    }

    /**
     * A quick tree travel to get the depth of the tree.
     *
     * @return
     */
    public int getDepth(Node node, int depth){
        if (node == null)
            return 0;

        if (node.getChildren().size() > 0) {
            depth++;
            for(Pair<String, Node> p : node.getChildren()){
                Node child = p.getValue();
                depth = getDepth(child, depth);
            }
        }
        return depth;
    }

    /**
     * A wrapper for getDepth.
     *
     * @return
     */
    public int getDepth(){
        return getDepth(root, 0);
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
