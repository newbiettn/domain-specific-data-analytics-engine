package service;

import beans.*;
import eu.mihosoft.vrl.workflow.VNode;
import javafx.util.Pair;
import org.apache.jena.sparql.util.Context;
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
    private final String RDFTYPE = "rdf:type";
    private final String WHERE = "WHERE";
    private final String FEATURE = "FEATURE";
    private final String FILTER = "FILTER";
    private StringBuilder sparqlQuery;
    private final String prolog = "PREFIX owl: <http://www.w3.org/2002/07/owl#>\n" +
            "PREFIX diab: <http://localhost:2020/resource/>\n" +
            "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n";


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
            interpretSELECT(root, root, 0);
        }  else if (cl == PrevalenceNodeBean.class) { //Prevalence query
            interpretPrevalence(root, root, 0);
        } else if (cl == AskNodeBean.class) {
            interpretAsk(root, root, 0);
        } else if (cl == CreatePredictionModelNodeBean.class) {
            interpretCreatePredictionModel(root, root, 0);
        }else if (cl == PredictNodeBean.class){
            interpretPredict(root, 0);
        } else {
            logger.error("Require appropriate root nodes (SELECT, ASK, ...) to interpret to SPARQL");
            return null;
        }

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
            Node child = node.getChildren().get(0).getValue();
            interpretWHERE(root, child, ++depth);
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
     * Interpret SELECT query.
     *
     * @param root
     * @param node
     * @param depth
     */
    private void interpretSELECT(Node root, Node node, int depth){
        if (node == null)
            return;
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
        if (node.getChildren().size() > 0) {
            Node child = node.getChildren().get(0).getValue();
            interpretWHERE(root, child, ++depth);
        }
    }

    /**
     * JG does not want to have a separate query to create ML model to make PREDICT query.
     * He just wants to make CreateMLModel be executed behind the scence for PREDICT query.
     * At the moment, I do not think it is a good idea as it will slow down PREDICT query because
     * we have to learn a model for every PREDICT query. But let's see...
     *
     * Thus, I have to `fabricate` CPM query and execute it behind the scene.
     */
    public String fabricateCreatePredictionModelQuery(Node root, int depth){
        sparqlQuery = new StringBuilder();
        sparqlQuery.append(prolog).append(NL);
        if (depth == 0){
            sparqlQuery.append("CREATE PREDICTION MODEL");
            sparqlQuery.append(NL);
        }

        if (root.getChildren().size() == 3) {
            Node targetNode = findNode(root, TargetNodeBean.class);
            if (targetNode != null) {
                interpretTargetNode(root, targetNode, depth + 1);
                sparqlQuery.append(NL);
            } else {
                logger.warn("Require TARGET node");
            }

            Node featureNode = findNode(root, FeatureNodeBean.class);
            if (featureNode != null){
                interpretFeature(root, featureNode, depth + 1);
                sparqlQuery.append(NL);
            } else {
                logger.warn("Require FEATURE node");
            }

            Node contextNode = findNode(root, ContextNodeBean.class);
            Node contextChildrenNode =contextNode.getChildren().get(0).getValue();
            if (contextNode != null) {
                sparqlQuery.append(WHERE).append(SPACE).append(NL);
                interpretWHERE(root, contextChildrenNode, depth + 1);
            } else {
                logger.info("Require domain-specific objects to predict");
            }

            sparqlQuery.append(NL).append("SAVE MODEL 'tmp'");
            return sparqlQuery.toString();
        } else {
            logger.warn("Require FEATURE/TARGET/OBJECT/USE PREDICTIVE MODEL nodes");
        }
        return null;
    }


    /**
     * Interpret PREDICT query.
     *
     */
    private void interpretPredict(Node root, int depth){
        VNode vNode = root.getVNode();
        ObjectBean objectBean = (ObjectBean) vNode.getValueObject().getValue();
        if (depth == 0){
            sparqlQuery.append(objectBean.getSparqlValue());
            sparqlQuery.append(NL);
        }
        if (root.getChildren().size() == 3) {
            Node targetNode = findNode(root, TargetNodeBean.class);
            if (targetNode != null) {
                interpretTargetNode(root, targetNode, depth + 1);
                sparqlQuery.append(NL);
            } else {
                logger.warn("Require TARGET node");
            }

            Node featureNode = findNode(root, FeatureNodeBean.class);
            if (featureNode != null){
                interpretFeature(root, featureNode, depth + 1);
                sparqlQuery.append(NL);
            } else {
                logger.warn("Require FEATURE node");
            }

            Node contextNode = findNode(root, ContextNodeBean.class);
            Node contextChildrenNode =contextNode.getChildren().get(0).getValue();
            if (contextNode != null) {
                sparqlQuery.append(WHERE).append(SPACE).append(NL);
                interpretWHERE(root, contextChildrenNode, depth + 1);
            } else {
                logger.info("Require domain-specific objects to predict");
            }

            sparqlQuery.append(NL).append("USE MODEL 'tmp'");

        } else {
            logger.warn("Require FEATURE/TARGET/OBJECT/USE PREDICTIVE MODEL nodes");
        }
    }

    /**
     * Interpret CreatePredictionModel query.
     *
     */
    private void interpretCreatePredictionModel(Node root, Node node, int depth){
        if (node == null)
            return;

        VNode vNode = node.getVNode();
        ObjectBean objectBean = (ObjectBean) vNode.getValueObject().getValue();
        if (depth == 0){
            sparqlQuery.append(objectBean.getSparqlValue());
            sparqlQuery.append(NL);
        }
        if (root.getChildren().size() == 4) {
            Node targetNode = findNode(root, TargetNodeBean.class);
            if (targetNode != null) {
                interpretTargetNode(root, targetNode, ++depth);
                sparqlQuery.append(NL);
            } else {
                logger.warn("Require TARGET node");
            }

            Node featureNode = findNode(root, FeatureNodeBean.class);
            if (featureNode != null){
                interpretFeature(root, featureNode, ++depth);
                sparqlQuery.append(NL);
            } else {
                logger.warn("Require FEATURE node");
            }

            Node whereNode = null;
            if (findNode(root, PatientNodeBean.class) != null){
                whereNode = findNode(root, PatientNodeBean.class);
            } else if (findNode(root, EpisodeNodeBean.class) != null){
                whereNode = findNode(root, EpisodeNodeBean.class);
            }
            if (whereNode != null) {
                sparqlQuery.append(WHERE).append(SPACE).append(LCURLYBRACKET);
                interpretWHERE(root, whereNode, ++depth);
                sparqlQuery.append(RCURLYBRACKET);
            } else {
                logger.info("Require domain-specific objects to predict");

            }

            // SAVE PREDICTIVE MODEL node
            Node savePredictiveModelNode = findNode(root, SavePredictiveModelBean.class);
            if (savePredictiveModelNode != null){
                interpretSavePredictiveModel(root, savePredictiveModelNode, ++depth);
            } else {
                logger.warn("Require SAVE PREDICTIVE model");
            }

        } else {
            logger.warn("Require FEATURE/TARGET/OBJECT/SAVE PREDICTIVE MODEL nodes");
        }
    }

    /**
     * Interpret TARGET clause.
     *
     * @param root
     * @param node
     * @param depth
     */
    private void interpretTargetNode(Node root, Node node, int depth) {
        TargetNodeBean targetNodeBean = (TargetNodeBean) node.getVNode().getValueObject().getValue();
        sparqlQuery.append(targetNodeBean.getSparqlValue());

        if (node.getChildren().size() == 1) {
            for(Pair<String, Node> p : node.getChildren()){
                Node child = p.getValue();
                ConditionNodeBean conditionNodeBean = (ConditionNodeBean) child.getVNode().getValueObject().getValue();
                sparqlQuery.append(SPACE).append(conditionNodeBean.getSparqlValue());
            }
        } else {

        }
    }

    /**
     * Intepret SAVE PREDICTVE MODEL node
     * @param root
     * @param node
     * @param depth
     */
    private void interpretSavePredictiveModel(Node root, Node node, int depth){
        if (node == null)
            return;
        SavePredictiveModelBean savePredictiveModelBean = (SavePredictiveModelBean) node.getVNode().getValueObject().getValue();
        sparqlQuery.append(NL);
        sparqlQuery.append(savePredictiveModelBean.getSparqlValue());
    }

    /**
     * Intepret USE PREDICTVE MODEL node
     * @param root
     * @param node
     * @param depth
     */
    private void interpretUsePredictiveModel(Node root, Node node, int depth){
        if (node == null)
            return;

        UsePredictiveModelBean usePredictiveModelBean = (UsePredictiveModelBean) node.getVNode().getValueObject().getValue();
        sparqlQuery.append(NL);
        sparqlQuery.append(usePredictiveModelBean.getSparqlValue());
    }

    /**
     * Interpret FEATURE clause.
     *
     * @param root
     * @param node
     * @param depth
     */
    private void interpretFeature(Node root, Node node, int depth) {
        FeatureNodeBean featureNodeBean = (FeatureNodeBean) node.getVNode().getValueObject().getValue();
        sparqlQuery.append(featureNodeBean.getSparqlValue());
        sparqlQuery.append(SPACE);
        sparqlQuery.append(LCURLYBRACKET);
        sparqlQuery.append(NL);

        if (node.getChildren().size() > 0) {
            int count = 0;
            for(Pair<String, Node> p : node.getChildren()){
                Node child = p.getValue();
                ConditionNodeBean conditionNodeBean = (ConditionNodeBean) child.getVNode().getValueObject().getValue();
                sparqlQuery.append(FEATURE)
                        .append(SPACE)
                        .append(conditionNodeBean.getSparqlValue());
                if (count < node.getChildren().size() - 1)
                    sparqlQuery.append(DOT);
                sparqlQuery.append(NL);
                count++;
            }
        }
        sparqlQuery.append(RCURLYBRACKET);
    }

    /**
     * Interpret WHERE clause.
     *
     * @param root
     * @param node
     * @param depth
     */
    private void interpretWHERE(Node root, Node node, int depth){
        if (node == null)
            return;

        VNode vNode = node.getVNode();
        ObjectBean objectBean = (ObjectBean) vNode.getValueObject().getValue();
        if (depth == 1){
            sparqlQuery.append(LCURLYBRACKET);
            sparqlQuery.append(objectBean.getSparqlValue())
                    .append(SPACE)
                    .append(RDFTYPE)
                    .append(SPACE)
                    .append(objectBean.getSPARQLClass())
                    .append(DOT);
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
                    sparqlQuery.append(NL).append(FILTER).append(LPAREN);
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

    /**
     * Find node by its class, return the first found node.
     *
     * @param root
     * @param cl
     * @return
     */
    private Node findNode(Node root, Class cl){
        if (root.getChildren().size() > 0) {
            for (Pair<String, Node> pair : root.getChildren()){
                Node child = pair.getValue();
                ObjectBean objectBean = (ObjectBean) child.getVNode().getValueObject().getValue();
                if (objectBean.getClass() == cl)
                    return child;
            }
        }
        return null;
    }
}
