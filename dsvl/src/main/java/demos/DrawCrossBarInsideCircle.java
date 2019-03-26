package demos;

import javafx.animation.PathTransition;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * Describe class purpose here.
 *
 * @author Ngoc Tran
 * @since 2019-03-26
 */
public class DrawCrossBarInsideCircle extends Application{
    public static void main(String[] args) {
        Application.launch(args);
    }
    @Override
    public void start(Stage stage) {
        // Create the node
        Circle circle = new Circle(30);
        Group root = new Group(circle);
        Scene scene = new Scene(root, 200, 200);
        circle.radiusProperty().bind(Bindings.min(scene.widthProperty(),
                scene.heightProperty())
                .divide(2));
        circle.centerXProperty().bind(scene.widthProperty().divide(2));
        circle.centerYProperty().bind(scene.heightProperty().divide(2));
        circle.setFill(Color.WHITE);
        circle.setStroke(Color.BLACK);

        ContextMenu cm = new ContextMenu();
        MenuItem dummyItem1 = new MenuItem("Context menu is disabled");
        MenuItem dummyItem2 = new MenuItem("Context menu is disabled");
        cm.getItems().add(dummyItem1);
        cm.getItems().add(dummyItem2);
        EventHandler<MouseEvent> handler = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                cm.show(circle,
                        event.getScreenX(), event.getScreenY());
            }
        };
        circle.addEventFilter(MouseEvent.MOUSE_CLICKED, handler);


        // Create the path
        stage.setScene(scene);
        stage.setTitle("Path Transition");
        stage.show();


    }

//    public void showContextMenu(MouseEvent me) {
//        // Show menu only on right click
//        if (me.getButton() == MouseButton.SECONDARY) {
//            MenuItem rectItem = new MenuItem("Rectangle");
//            MenuItem circleItem = new MenuItem("Circle");
//            MenuItem ellipseItem = new MenuItem("Ellipse");
//            rectItem.setOnAction(e -> draw("Rectangle"));
//            circleItem.setOnAction(e -> draw("Circle"));
//            ellipseItem.setOnAction(e -> draw("Ellipse"));
//            ContextMenu ctxMenu =
//                    new ContextMenu(rectItem, circleItem, ellipseItem);
//            ctxMenu.show(canvas, me.getScreenX(), me.getScreenY());
//        } }
}
