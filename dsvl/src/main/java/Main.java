import controllers.MainController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class Main extends Application {
    private MainController controller;
    /**
     * The main() method is ignored in correctly deployed JavaFX application.
     * main() serves only as fallback in case the application can not be
     * launched through deployment artifacts, e.g., in IDEs with limited FX
     * support. NetBeans ignores main().
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage){
        URL fxmlUrl = this.getClass().getClassLoader().getResource("fxml/MainWindow.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(fxmlUrl);

        StackPane canvas = new StackPane();
        Scene scene = new Scene(canvas);

        try {
            fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        controller = fxmlLoader.getController();
        canvas.getChildren().add((Node) fxmlLoader.getRoot());
        scene.setCamera(new PerspectiveCamera());
        scene.getStylesheets().setAll("fxml/css/default.css");
        stage.setScene(scene);
        stage.setTitle("Domain Specific Visual Language for Diabetes Discovery");
        stage.show();
    }

}
