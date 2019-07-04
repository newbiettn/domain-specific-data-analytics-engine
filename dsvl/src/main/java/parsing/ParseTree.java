package parsing;

import beans.*;
import controllers.MainController;
import eu.mihosoft.vrl.workflow.*;
import javafx.util.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.ParseTreeService;

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
    private ParseTreeService parseTreeService;
    public static int INVALID_TREE = -1;
    public static int SELECT_TREE = 1;
    public static int PREVALENCE_TREE = 2;
    public static int ASK_TREE = 3;
    public static int CREATEPREDICTIONMODEL_TREE = 4;
    public static int PREDICT_TREE = 5;

    public ParseTree(){
        this.root = null;
        this.parseTreeService  = new ParseTreeService();
    }

    public String interpret(){
        return parseTreeService.interpret(root);
    }

    public String fabricateInterpretingCPM(){
        return parseTreeService.fabricateCreatePredictionModelQuery(root, 0);
    }

    /**
     * Parse the flow.
     * @param flow
     */
    public int parse(VFlow flow){
        VNode vRoot = null;
        for(VNode v : flow.getNodes()){
            if (v.getInputs().size() == 0){
                vRoot = v;
            }
        }
        /* If no head root*/
        if (vRoot == null)
            return INVALID_TREE;

        /* If not appropriate root (i.e., SELECT, ASK, ...) */
        ObjectBean objectBean = (ObjectBean) vRoot.getValueObject().getValue();
        Class cl = objectBean.getClass();
        if (cl != SelectNodeBean.class &&
                cl != PrevalenceNodeBean.class &&
                cl != AskNodeBean.class &&
                cl != CreatePredictionModelNodeBean.class &&
                cl != PredictNodeBean.class)
            return INVALID_TREE;

        root = new Node(vRoot);
        Connections connections = flow.getConnections(MainController.CONNECTION_NAME);
        addChild(connections, root, vRoot);
        if (cl == SelectNodeBean.class){
            return SELECT_TREE;
        } else if (cl == PrevalenceNodeBean.class) {
            return PREVALENCE_TREE;
        } else if (cl == AskNodeBean.class) {
            return ASK_TREE;
        } else if (cl == CreatePredictionModelNodeBean.class) {
            return CREATEPREDICTIONMODEL_TREE;
        } else if (cl == PredictNodeBean.class) {
            return PREDICT_TREE;
        } else {
            return INVALID_TREE;
        }
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

}
