package skins;

import eu.mihosoft.vrl.workflow.Connection;
import eu.mihosoft.vrl.workflow.VFlow;
import eu.mihosoft.vrl.workflow.fx.DefaultFXConnectionSkin;
import eu.mihosoft.vrl.workflow.fx.FXSkinFactory;
import javafx.collections.MapChangeListener;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Path;

import javax.swing.event.ChangeEvent;

/**
 * Custom connection skin.
 *
 * @author Ngoc Tran
 * @since 2019-04-02
 */
public class CustomFXConnectionSkin extends DefaultFXConnectionSkin {
    private MapChangeListener<String, Object> vReqLister;
    private Path selectedPath;

    public CustomFXConnectionSkin(FXSkinFactory skinFactory,
                                  Parent parent,
                                  Connection connection,
                                  VFlow flow,
                                  String type) {
        super(skinFactory, parent, connection, flow, type);
    }

    @Override
    protected void initMouseEventHandler() {
        EventHandler<MouseEvent> contextMenuHandler = createContextMenuHandler(createContextMenu());
//        connectionPath.addEventHandler(MouseEvent.MOUSE_CLICKED, contextMenuHandler);
        getReceiverUI().addEventHandler(MouseEvent.MOUSE_CLICKED, contextMenuHandler);
    } // end init

    @Override
    protected ContextMenu createContextMenu() {
        ContextMenu contextMenu = super.createContextMenu();
        contextMenu.getItems().addAll(createMenuItem("Foo"));
        contextMenu.getItems().addAll(createMenuItem("Bar"));
        return contextMenu;
    }

    private MenuItem createMenuItem(String title) {
        MenuItem item = new MenuItem(title);
        item.setOnAction(event -> System.out.println(title));
        return item;
    }

    @Override
    protected EventHandler<MouseEvent> createContextMenuHandler(ContextMenu contextMenu) {
        return new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                contextMenu.show(getConnectionPath(), event.getScreenX(), event.getScreenY());
            }
        };
    }
}
