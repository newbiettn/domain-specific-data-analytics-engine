package beans;

import javafx.util.Pair;

import java.util.ArrayList;

/**
 * Represent the Variable node.
 *
 * @author Ngoc Tran
 * @since 2019-03-26
 */
public class VariableNodeBean extends ObjectBean{
    public VariableNodeBean() {
        this.variable = null;
        this.maxOutputConn = 0;
        this.maxInputConn = 1;
        this.minOutputConn = 0;
        this.minInputConn = 1;
    }
}
