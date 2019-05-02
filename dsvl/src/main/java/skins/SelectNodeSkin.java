package skins;

import beans.SelectNodeBean;
import controllers.SelectNodeController;
import eu.mihosoft.vrl.workflow.VFlow;
import eu.mihosoft.vrl.workflow.VNode;
import eu.mihosoft.vrl.workflow.fx.FXSkinFactory;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import jfxtras.scene.control.window.CloseIcon;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * Custom flownode skin. In addition to the basic node visualization from
 * VWorkflows this skin adds custom visualization of value objects.
 *
 * @author Michael Hoffer &lt;info@michaelhoffer.de&gt;
 */
public class SelectNodeSkin extends CustomFlowNodeSkin {

    public SelectNodeSkin(FXSkinFactory skinFactory,
                          VNode model, VFlow controller) {
        super(skinFactory, model, controller);
    }

    @Override
    protected Node createView() {
        FXMLLoader fxmlLoader = new FXMLLoader(this.getClass()
                .getClassLoader().getResource("fxml/SelectNode.fxml"));
        try {
            fxmlLoader.load();
        } catch (IOException ex) {
            Logger.getLogger(SelectNodeSkin.class.getName()).
                    log(Level.SEVERE, null, ex);
        }
        getModel().setTitle("Select Node");
        getNode().setPrefSize(30, 80);

        SelectNodeController controller = fxmlLoader.getController();
        controller.setNode(getModel());
        controller.setSelectNodeBean((SelectNodeBean)getModel().getValueObject().getValue());

        Pane root = (Pane) fxmlLoader.getRoot();
        return root;
    }


}