package skins;

import eu.mihosoft.vrl.workflow.Connection;
import eu.mihosoft.vrl.workflow.VFlow;
import eu.mihosoft.vrl.workflow.fx.FXValueSkinFactory;
import eu.mihosoft.vrl.workflow.skin.ConnectionSkin;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Path;


/**
 * Custom skins for connection as well as node.
 *
 * @author Ngoc Tran
 * @since 2019-04-02
 */
public class CustomFXValueSkinFactory extends FXValueSkinFactory {
    private ConnectionSkin customConnectionSkin;
    public CustomFXValueSkinFactory(Parent parent) {
        super(parent);
    }

    @Override
    public ConnectionSkin createSkin(Connection c, VFlow flow, String type) {
        customConnectionSkin = new CustomFXConnectionSkin(this, getFxParent(), c, flow, type).init();
        return customConnectionSkin;
    }

    public ConnectionSkin getCustomConnectionSkin() {
        return customConnectionSkin;
    }

}
