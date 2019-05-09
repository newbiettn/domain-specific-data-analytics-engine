package demos;

import java.util.ArrayList;

/**
 * Describe class purpose here.
 *
 * @author Ngoc Tran
 * @since 2019-05-09
 */
public class Node {
    int key;
    ArrayList<Node> child;
    public Node(int item){
        key = item;
        child = new ArrayList<>();
    }
}
