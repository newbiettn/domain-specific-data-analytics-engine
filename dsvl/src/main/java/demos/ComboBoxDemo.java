package demos;

import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

/**
 * Describe class purpose here.
 *
 * @author Ngoc Tran
 * @since 2019-03-26
 */
public class ComboBoxDemo extends Application{
    public static void main(String[] args) {
        Application.launch(args);
    }
    @Override
    public void start(Stage stage) {
        ComboBox<String> seasons = new ComboBox<>();
        HBox root = new HBox(seasons);
        Scene scene = new Scene(root, 200, 200);
        seasons.getItems().addAll("Spring", "Summer", "Fall", "Winter");


        // Create the path
        stage.setScene(scene);
        stage.setTitle("Path Transition");
        stage.show();


    }
}
