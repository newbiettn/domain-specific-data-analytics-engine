package skins;

import beans.ConditionNodeBean;
import controllers.ConditionNodeController;
import eu.mihosoft.vrl.workflow.VFlow;
import eu.mihosoft.vrl.workflow.VNode;
import eu.mihosoft.vrl.workflow.fx.FXSkinFactory;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * Custom flownode skin. In addition to the basic node visualization from
 * VWorkflows this skin adds custom visualization of value objects.
 *
 * @author Michael Hoffer &lt;info@michaelhoffer.de&gt;
 */
public class ConditionNodeSkin extends CustomFlowNodeSkin { //TODO: fix condition nodes after refreshing

    public ConditionNodeSkin(FXSkinFactory skinFactory,
                             VNode model, VFlow controller) {
        super(skinFactory, model, controller);
    }

    @Override
    protected Node createView() {
        String title = getModel().getTitle();
        if (title.equals("Node"))
            getModel().setTitle("Condition Node");
        getNode().setPrefSize(150, 100);

        ConditionNodeController controller = null;
        if (getModel().getController() == null) {
            controller= new ConditionNodeController();
            controller.initialize();
            getModel().setController(controller);
            controller.setNode(getModel());
            controller.setConditionNodeBean((ConditionNodeBean) getModel().getValueObject().getValue());

        } else {
            controller = (ConditionNodeController) getModel().getController();
        }
        Pane root = (Pane) controller.getConditionNodeBorderPane();

        return root;
    }


}