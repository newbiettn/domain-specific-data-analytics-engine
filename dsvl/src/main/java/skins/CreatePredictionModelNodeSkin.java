package skins;

import beans.CreatePredictionModelNodeBean;
import beans.SelectNodeBean;
import controllers.CreatePredictionModelNodeController;
import controllers.SelectNodeController;
import eu.mihosoft.vrl.workflow.VFlow;
import eu.mihosoft.vrl.workflow.VNode;
import eu.mihosoft.vrl.workflow.fx.FXSkinFactory;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
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
public class CreatePredictionModelNodeSkin extends CustomFlowNodeSkin {

    public CreatePredictionModelNodeSkin(FXSkinFactory skinFactory,
                                         VNode model, VFlow controller) {
        super(skinFactory, model, controller);
    }

    @Override
    protected Node createView() {
        FXMLLoader fxmlLoader = new FXMLLoader(this.getClass()
                .getClassLoader().getResource("fxml/CreatePredictionModelNode.fxml"));
        try {
            fxmlLoader.load();
        } catch (IOException ex) {
            Logger.getLogger(CreatePredictionModelNodeSkin.class.getName()).
                    log(Level.SEVERE, null, ex);
        }
        getModel().setTitle("Create Prediction Model Node");
        getNode().setPrefSize(30, 80);

        CreatePredictionModelNodeController controller = fxmlLoader.getController();
        controller.setNode(getModel());
        controller.setCreatePredictionModelNodeBean((CreatePredictionModelNodeBean) getModel().getValueObject().getValue());

        Pane root = (Pane) fxmlLoader.getRoot();
        return root;
    }


}