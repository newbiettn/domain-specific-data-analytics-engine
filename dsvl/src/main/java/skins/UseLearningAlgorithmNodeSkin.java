package skins;

import beans.UsePredictiveModelBean;
import controllers.UseLearningAlgorithmNodeController;
import eu.mihosoft.vrl.workflow.VFlow;
import eu.mihosoft.vrl.workflow.VNode;
import eu.mihosoft.vrl.workflow.fx.FXSkinFactory;
import javafx.scene.Node;
import javafx.scene.layout.Pane;

/**
 * Custom flownode skin. In addition to the basic node visualization from
 * VWorkflows this skin adds custom visualization of value objects.
 *
 * @author Michael Hoffer &lt;info@michaelhoffer.de&gt;
 */
public class UseLearningAlgorithmNodeSkin extends CustomFlowNodeSkin {

    public UseLearningAlgorithmNodeSkin(FXSkinFactory skinFactory,
                                        VNode model, VFlow controller) {
        super(skinFactory, model, controller);
    }

    @Override
    protected Node createView() {
        String title = getModel().getTitle();
        if (title.equals("Node"))
            getModel().setTitle("Use Learning Algorithm Node");
        getNode().setPrefSize(270, 100);

        UseLearningAlgorithmNodeController controller = null;
        if (getModel().getController() == null) {
            controller= new UseLearningAlgorithmNodeController();
            controller.initialize();
            getModel().setController(controller);
            controller.setNode(getModel());
            controller.setUsePredictiveModelBean((UsePredictiveModelBean) getModel().getValueObject().getValue());

        } else {
            controller = (UseLearningAlgorithmNodeController) getModel().getController();
        }
        Pane root = (Pane) controller.getBorderPane();

        return root;
    }
}