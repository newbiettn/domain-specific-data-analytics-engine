package demos;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Describe class purpose here.
 *
 * @author Ngoc Tran
 * @since 2019-05-09
 */
public class BinaryTree {
    Node root;
    BinaryTree(){
        root = null;
    }
    void printPreorder(Node node) {
        if (node == null)
            return;

        /* first print data of node */
        System.out.print(node.key + " ");

        for(Node each : node.child){
            printPreorder(each);
        }
    }
    public static void main(String[] args){
        BinaryTree tree = new BinaryTree();
        tree.root = new Node(1);
        tree.root.child = new ArrayList<>(Arrays.asList(new Node(2), new Node(6)));
        tree.root.child.get(0).child = new ArrayList<>(Arrays.asList(new Node(3), new Node(4), new Node(5)));
        tree.root.child.get(1).child = new ArrayList<>(Arrays.asList(new Node(7), new Node(8)));

        System.out.println("Preorder traversal of binary tree is ");
        tree.printPreorder(tree.root);
    }
}
