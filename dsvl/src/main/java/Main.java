import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.URL;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        URL fxmlUrl = this.getClass().getClassLoader().getResource("fxml/MainWindow.fxml");
        VBox root = FXMLLoader.load(fxmlUrl);
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Domain Specific Visual Language for Diabetes Discovery");
        stage.show();
    }
}
