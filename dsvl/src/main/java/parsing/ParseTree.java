package parsing;

import controllers.MainController;
import eu.mihosoft.vrl.workflow.*;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Represents parse trees for dsvl.
 *
 * @author Ngoc Tran
 * @since 2019-05-09
 */
public class ParseTree {
    Node root;
    public ParseTree(){
        root = null;
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
            for(Node each : node.getChildren()){
                System.out.print("\n");
                for (int i = 0; i<depth; i++){
                    System.out.print("\t");
                }
                printPreorder(each, depth);
            }
            System.out.print(" ] ");
        }
    }

    /**
     * Wrapper method for printing the parse tree.
     */
    public void printPreorder(){
        printPreorder(root, 0);
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

        if (vRoot == null)
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
        if (outputConnector == null)
            return;

        Collection<Connection> connectionCollection = connections.getAllWith(outputConnector);
        if (connectionCollection.size() == 0 || connectionCollection == null)
            return;

        ArrayList<Node> children = new ArrayList<>();
        parent.setChildren(children);
        for (Connection c : connectionCollection){
            /* Add children*/
            VNode vChild = c.getReceiver().getNode();
            Node child = new Node(vChild);
            addChild(connections, child, vChild);
            children.add(child);
        }
    }

}
