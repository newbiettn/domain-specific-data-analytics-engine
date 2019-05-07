package skins;

import com.sun.javafx.css.StyleClassSet;
import controllers.MainController;
import eu.mihosoft.vrl.workflow.VFlow;
import eu.mihosoft.vrl.workflow.VFlowModel;
import eu.mihosoft.vrl.workflow.VNode;
import eu.mihosoft.vrl.workflow.fx.FXSkinFactory;
import eu.mihosoft.vrl.workflow.fx.FXFlowNodeSkinBase;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import jfxtras.scene.control.window.CloseIcon;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public abstract class CustomFlowNodeSkin extends CustomFXFlowNodeSkinBase {

    private static Logger logger = LoggerFactory.getLogger(CustomFlowNodeSkin.class);
    public CustomFlowNodeSkin(FXSkinFactory skinFactory, VNode model, VFlow controller) {
        super(skinFactory, model, controller);
    }

    protected abstract Node createView();

    /**
     * Will be called once for each value change.
     */
    @Override
    public void updateView() {

//        getNode().getLeftIcons().add(new MinimizeIcon(getNode()));
        getNode().getLeftIcons().clear();
        getNode().getLeftIcons().add(new CloseIcon(getNode()));
//        getNode().setPrefWidth(15);

        super.updateView();

        // we don't create custom view for flows
        if (getModel() instanceof VFlowModel) {
            return;
        }

        // we don't create a custom view if no value has been defined
        if (getModel().getValueObject().getValue() == null) {
            return;
        }

        // create the view
        Node view = createView();

        // add the view to scalable content pane
        if (view != null) {
            AnchorPane nodePane = new AnchorPane();
            nodePane.getChildren().add(view);

            AnchorPane.setTopAnchor(view, 0.0);
            AnchorPane.setBottomAnchor(view, 0.0);
            AnchorPane.setLeftAnchor(view, 0.0);
            AnchorPane.setRightAnchor(view, 0.0);

            getNode().setContentPane(nodePane);

        }
    }
}
