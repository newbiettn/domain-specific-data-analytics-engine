package parsing;

import eu.mihosoft.vrl.workflow.VNode;
import javafx.util.Pair;

import java.util.ArrayList;

/**
 * Represents a node in the dsvl.
 *
 * @author Ngoc Tran
 * @since 2019-05-09
 */
public class Node {
    private String id;
    private VNode vNode;
    private ArrayList<Pair<String, Node>> children;

    public Node(VNode vNode){
        this.id = vNode.getId();
        this.vNode = vNode;
        this.children = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public VNode getVNode() {
        return vNode;
    }

    public void setvNode(VNode vNode) {
        this.vNode = vNode;
    }

    public ArrayList<Pair<String, Node>> getChildren() {
        return children;
    }

    public void setChildren(ArrayList<Pair<String, Node>> children) {
        this.children = children;
    }
}
