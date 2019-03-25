package demos;

import javafx.animation.PathTransition;
import javafx.application.Application;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.DoubleProperty;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

public class TestPath extends Application {
    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage stage) {
        CubicCurve curve = new CubicCurve();
        curve.setStartX(350);
        curve.setStartY(150);
        curve.setControlX1(150);
        curve.setControlY1(300);
        curve.setControlX2(250);
        curve.setControlY2(50);
        curve.setEndX(50);
        curve.setEndY(200);
        curve.setStroke(Color.FORESTGREEN);
        curve.setStrokeWidth(4);
        curve.setStrokeLineCap(StrokeLineCap.ROUND);
        curve.setFill(null);

        Anchor start = new Anchor(Color.PALEGREEN, curve.startXProperty(), curve.startYProperty());
        Anchor control1 = new Anchor(Color.GOLD, curve.controlX1Property(), curve.controlY1Property());
        Anchor control2 = new Anchor(Color.GOLDENROD, curve.controlX2Property(), curve.controlY2Property());
        Anchor end = new Anchor(Color.TOMATO, curve.endXProperty(), curve.endYProperty());

        Text text = new Text("This is a text");
        double textX = (curve.getControlX2() - curve.getControlX1()) / 2 + curve.getControlX1();
        double textY = (curve.getControlY2() - curve.getControlY1()) / 2 + curve.getControlY1();
        text.setX(textX);
        text.setY(textY);

        DoubleBinding xBinding = new DoubleBinding() {
            DoubleProperty endX = curve.endXProperty();
            DoubleProperty startX = curve.startXProperty();

            {
                this.bind(endX, startX);
            }
            @Override
            protected double computeValue() {
                double x = 0;
                PathTransition pt = new PathTransition(Duration.ONE, curve, new Circle());
                pt.playFromStart(); // force initialization
                pt.stop();
                for (double frac = 0.0; frac <= 1.0; frac += 0.05) {
                    if (frac == 0.6) {
                    System.out.println(frac);
                        pt.interpolate(frac);
                        x = pt.getNode().getTranslateX();
                    }
                }
                return x;
//                return (endX.get() - startX.get())/2 + startX.get();
            }
        };
        DoubleBinding yBinding = new DoubleBinding() {
            DoubleProperty endY = curve.endYProperty();
            DoubleProperty startY = curve.startYProperty();
            {
                this.bind(endY, startY);
            }
            @Override
            protected double computeValue() {
                double y = 0;
                PathTransition pt = new PathTransition(Duration.ONE, curve, new Circle());
                pt.playFromStart(); // force initialization
                pt.stop();
                for (double frac = 0.0; frac <= 1.0; frac += 0.05) {
                    if (frac == 0.6) {
                        pt.interpolate(frac);
                        y = pt.getNode().getTranslateY();
                    }
                }
                return y;
            }
        };

        text.xProperty().bind(xBinding);
        text.yProperty().bind(yBinding);

        Group root = new Group(curve, text, start, control1, control2, end);
        Scene scene = new Scene(root, 800, 700);
        stage.setScene(scene);
        stage.setTitle("Using Paths");
        stage.show();
    }

    // a draggable anchor displayed around a point.
    class Anchor extends Circle {
        // records relative x and y co-ordinates.
        private class Delta {
            double x, y;
        }

        Anchor(Color color, DoubleProperty x, DoubleProperty y) {
            super(x.get(), y.get(), 10);
            setFill(color.deriveColor(1, 1, 1, 0.5));
            setStroke(color);
            setStrokeWidth(2);
            setStrokeType(StrokeType.OUTSIDE);

            x.bind(centerXProperty());
            y.bind(centerYProperty());
            enableDrag();
        }

        // make a node movable by dragging it around with the mouse.
        private void enableDrag() {
            final Delta dragDelta = new Delta();
            setOnMousePressed(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    // record a delta distance for the drag and drop operation.
                    dragDelta.x = getCenterX() - mouseEvent.getX();
                    dragDelta.y = getCenterY() - mouseEvent.getY();
                    getScene().setCursor(Cursor.MOVE);
                }
            });
            setOnMouseReleased(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    getScene().setCursor(Cursor.HAND);
                }
            });
            setOnMouseDragged(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    double newX = mouseEvent.getX() + dragDelta.x;
                    if (newX > 0 && newX < getScene().getWidth()) {
                        setCenterX(newX);
                    }
                    double newY = mouseEvent.getY() + dragDelta.y;
                    if (newY > 0 && newY < getScene().getHeight()) {
                        setCenterY(newY);
                    }
                }
            });
            setOnMouseEntered(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    if (!mouseEvent.isPrimaryButtonDown()) {
                        getScene().setCursor(Cursor.HAND);
                    }
                }
            });
            setOnMouseExited(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    if (!mouseEvent.isPrimaryButtonDown()) {
                        getScene().setCursor(Cursor.DEFAULT);
                    }
                }
            });
        }
    }
}
