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
        Scene scene = new Scene(canvas, 1600, 800);

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

//    @Override
//    public void start(Stage stage){
//        VFlow flow = FlowFactory.newFlow();
//        flow.setVisible(true);
//
//        VNode functionNode = createInputNode(flow, "Input 11",
//                new FunctionInput("x*x+a*sin(x*3)", 80, 1));
//
//
//        VCanvas canvas = new VCanvas();
//        canvas.setTranslateToMinNodePos(false);
//        canvas.setMinScaleX(0.5);
//        canvas.setMinScaleY(0.5);
//        canvas.setMaxScaleX(1.0);
//        canvas.setMaxScaleY(1.0);
//
//        // define it as background (css class)
//        canvas.getStyleClass().setAll("vflow-background");
//
//        // create skin factory for flow visualization
//        FXValueSkinFactory fXSkinFactory = new FXValueSkinFactory(canvas);
//        fXSkinFactory.addSkinClassForValueType(FunctionInput.class, InputFunctionFlowNodeSkin.class);
//        flow.addSkinFactories(fXSkinFactory);
//
//        Scene scene = new Scene(canvas, 1024, 600);
//
//        // add css style
//        scene.getStylesheets().setAll("fxml/css/default.css");
//
//        stage.setTitle("AAAAAAAAAAAAA");
//        stage.setScene(scene);
//        stage.show();
//    }

}
