package service;

import beans.*;
import eu.mihosoft.vrl.workflow.VNode;
import javafx.util.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import parsing.Node;
import java.util.ArrayList;

/**
 * Describe class purpose here.
 *
 * @author Ngoc Tran
 * @since 2019-05-17
 */
public class ParseTreeService {
    private static Logger logger = LoggerFactory.getLogger(ParseTreeService.class);
    private final String NL = "\n";
    private final String TAB = "\t";
    private final String SPACE = " ";
    private final String DOT = ".";
    private final String LPAREN = "(";
    private final String RPAREN = ")";
    private final String LCURLYBRACKET = "{";
    private final String RCURLYBRACKET = "}";
    private final String AND = "&&";
    private final String prolog = "PREFIX diab: <http://www.semanticweb.org/newbiettn/ontologies/2017/11/diabetes_inpatient_study#>\n" +
            "PREFIX : <http://localhost:2020/>\n" +
            "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
            "PREFIX owl: <http://www.w3.org/2002/07/owl#>";
    private StringBuilder sparqlQuery;

    public ParseTreeService(){
    }

    /**
     * Wrapper function for interpret.
     */
    public String interpret(Node root){
        sparqlQuery = new StringBuilder();
        sparqlQuery.append(prolog).append(NL);

        /* If not appropriate root to parse to SPARQL (e.g., SELECT, ASK, ...) */
        if (root == null) {
            logger.error("Require appropriate root nodes (SELECT, ASK, ...) to interpret to SPARQL");
            return null;
        }

        ObjectBean objectBean = (ObjectBean) root.getVNode().getValueObject().getValue();
        Class cl = objectBean.getClass();
        if (cl == SelectNodeBean.class){ // Select query
            int treeDepth = getDepth(root);
            logger.info("Depth of the tree " + treeDepth);
            if (treeDepth > 1)
                interpret(root, root, 0);
            else
                interpretSimple(root);
        }  else if (cl == PrevalenceNodeBean.class) { //Prevalence query
            interpretPrevalence(root, root, 0);
        } else if (cl == AskNodeBean.class){
            interpretAsk(root, root, 0);
        } else {
            logger.error("Require appropriate root nodes (SELECT, ASK, ...) to interpret to SPARQL");
            return null;
        }

        logger.info("Raw query: \n" + sparqlQuery.toString());
        return sparqlQuery.toString();
    }

    /**
     * Interpret ASK query.
     *
     */
    private void interpretAsk(Node root, Node node, int depth){
        if (node == null)
            return;

        VNode vNode = node.getVNode();
        ObjectBean objectBean = (ObjectBean) vNode.getValueObject().getValue();
        if (depth == 0){
            sparqlQuery.append(objectBean.getSparqlValue());
        }
        if (node.getChildren().size() > 0) {
            ++depth;
            Node child = node.getChildren().get(0).getValue();
            interpretWHERE(root, child, depth);
        }
    }


    /**
     * Query to calculate a group of poulation that satisfying users' conditions.
     *
     * @param node
     * @param depth
     */
    private void interpretPrevalence(Node root, Node node, int depth){
        if (node == null)
            return;

        if (depth == 0){
            sparqlQuery.append("SELECT ?prevalence  {");
            sparqlQuery.append(NL);
            sparqlQuery.append(LCURLYBRACKET);
            sparqlQuery.append("SELECT (COUNT(DISTINCT ?x) as ?population) WHERE {\n" +
                    "  ?x a diab:Patient\n" +
                    "}");
            sparqlQuery.append(NL);
            sparqlQuery.append(RCURLYBRACKET);
            sparqlQuery.append(LCURLYBRACKET);
            sparqlQuery.append("SELECT (COUNT(DISTINCT ");
            Node child = node.getChildren().get(0).getValue();
            ObjectBean ob = (ObjectBean) child.getVNode().getValueObject().getValue();
            sparqlQuery.append(ob.getSparqlValue());
            sparqlQuery.append(") as ?group)");
        }
        if (node.getChildren().size() > 0) {
            Node child = node.getChildren().get(0).getValue();
            interpretWHERE(root, child, ++depth);
        }
        sparqlQuery.append(RCURLYBRACKET)
                .append(NL)
                .append("BIND (?group/?population*100 as ?prevalence) ")
                .append(RCURLYBRACKET);
    }


    /**
     * Interpret to SPARQL.
     *
     * @param node
     */
    private void interpret(Node root, Node node, int depth){ //TODO: revise the code
        if (node == null)
            return;

        /* first print data of node */
        VNode vNode = node.getVNode();
        ObjectBean objectBean = (ObjectBean) vNode.getValueObject().getValue();
        if (depth == 0){
            sparqlQuery.append(objectBean.getSparqlValue());
            ArrayList<String> variables = getVariables(root);
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

//        if (node.getChildren().size() > 0) {
        depth++;
        if (depth == 1){
            for(Pair<String, Node> p : node.getChildren()){
                Node child = p.getValue();
                sparqlQuery.append(SPACE);
                interpret(root, child, depth);
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
                interpret(root, child, depth);
            }

            // last node of the tree
            if (depth == 2){ // TODO: not sure why depth == 2 works
                // filter condition
                ArrayList<Pair<String, String>> conditions = getConditions(root);
                if (conditions.size() > 0 ){
                    sparqlQuery.append(NL).append("FILTER").append(LPAREN);
                    for (int i = 0; i<conditions.size(); i++){
                        Pair<String, String> c = conditions.get(i);
                        sparqlQuery.append(c.getKey())
                                .append(SPACE)
                                .append(c.getValue());
                        if (i < conditions.size()-1) {
                            sparqlQuery.append(SPACE)
                                    .append(AND)
                                    .append(SPACE);
                        }
                    }
                    sparqlQuery.append(RPAREN);
                }
                sparqlQuery.append(" } ");
            }
        }
//        }

    }

    private void interpretWHERE(Node root, Node node, int depth){
        if (node == null)
            return;

        VNode vNode = node.getVNode();
        ObjectBean objectBean = (ObjectBean) vNode.getValueObject().getValue();
        if (depth == 1){
            sparqlQuery.append(LCURLYBRACKET);
            if (objectBean.getClass() == PatientNodeBean.class)
                sparqlQuery.append(objectBean.getSparqlValue())
                        .append(" rdf:type ").append("diab:Patient.");
            if (node.getChildren().size() > 0){
                for(Pair<String, Node> p : node.getChildren()){
                    Node child = p.getValue();
                    VNode vNodeChild = child.getVNode();
                    ObjectBean objectBeanChild = (ObjectBean) vNodeChild.getValueObject().getValue();
                    String connectionName = p.getKey();

                    sparqlQuery.append(NL);
                    sparqlQuery.append(objectBean.getSparqlValue());
                    sparqlQuery.append(SPACE)
                            .append(connectionName)
                            .append(SPACE)
                            .append(objectBeanChild.getSparqlValue())
                            .append(DOT);
                    interpretWHERE(root, child, ++depth);
                }
            }
            sparqlQuery.append(RCURLYBRACKET);
        } else {
            for(Pair<String, Node> p : node.getChildren()){
                Node child = p.getValue();
                VNode vNodeChild = child.getVNode();
                ObjectBean objectBeanChild = (ObjectBean) vNodeChild.getValueObject().getValue();
                String connectionName = p.getKey();
                sparqlQuery.append(NL);
                sparqlQuery.append(objectBean.getSparqlValue());
                sparqlQuery.append(SPACE)
                        .append(connectionName)
                        .append(SPACE)
                        .append(objectBeanChild.getSparqlValue())
                        .append(DOT);
                interpretWHERE(root, child, ++depth);
            }

            // last node of the tree
            if (depth == getDepth(root)){
                // filter condition
                ArrayList<Pair<String, String>> conditions = getConditions(root);
                if (conditions.size() > 0 ){
                    sparqlQuery.append(NL).append("FILTER").append(LPAREN);
                    for (int i = 0; i<conditions.size(); i++){
                        Pair<String, String> c = conditions.get(i);
                        sparqlQuery.append(c.getKey())
                                .append(SPACE)
                                .append(c.getValue());
                        if (i < conditions.size()-1) {
                            sparqlQuery.append(SPACE)
                                    .append(AND)
                                    .append(SPACE);
                        }
                    }
                    sparqlQuery.append(RPAREN);
                }
            }
        }
    }

    /**
     * To interpret simple flows with only two nodes, such as: SELECT -> Patient
     *
     * @param node
     */
    private void interpretSimple(Node node){
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
     * Traverse the tree to get all conditions in form of pairs (variableName, its condition)
     *
     * @param conditions
     * @param node
     * @param depth
     *
     * @return a list of condition pairs
     */
    private ArrayList<Pair<String, String>> getConditions(ArrayList<Pair<String, String>> conditions,
                                                          Node node,
                                                          int depth) {
        if (node == null)
            return conditions;

        if (node.getChildren().size() > 0) {
            depth++;
            for(Pair<String, Node> p : node.getChildren()){
                Node child = p.getValue();
                ObjectBean ob = (ObjectBean) child.getVNode().getValueObject().getValue();
                if (ob.getClass() == ConditionNodeBean.class){
                    ConditionNodeBean conditionNodeBean = (ConditionNodeBean) ob;
                    String c = conditionNodeBean.getCondition();
                    if (!c.isEmpty()) { // only get if the condition is specified
                        String v = conditionNodeBean.getSparqlValue();
                        conditions.add(new Pair<>(v, c));
                    }
                }
                conditions = getConditions(conditions, child, depth);
            }
        }
        return conditions;
    }

    /**
     * Wrapper for getConditions()
     *
     * @return
     */
    private ArrayList<Pair<String, String>> getConditions(Node root){
        ArrayList<Pair<String, String>> conditions = new ArrayList<>();
        return getConditions(conditions, root, 0);
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
                String variableName = ob.getSparqlValue();
                variables.add(variableName);
                variables = getVariables(variables, child, depth);
            }
        }
        return variables;

    }

    /**
     * Get all variables to display in the table result (a wrapper).
     * @return
     */
    private ArrayList<String> getVariables(Node root){
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
    public int getDepth(Node root){
        return getDepth(root, 0);
    }

}
