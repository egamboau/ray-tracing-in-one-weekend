package com.egamboau;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.PixelWriter;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * JavaFX App
 */
public class App extends Application {

    int imageWidth = 256;
    int imageHeigt = 256;

    private GraphicsContext gc;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Ray Tracing in a Weekend");
        Group root = new Group();
        Canvas canvas = new Canvas(imageWidth, imageHeigt);
        gc = canvas.getGraphicsContext2D();
        root.getChildren().add(canvas);
        primaryStage.setScene(new Scene(root, imageWidth, imageHeigt));
        primaryStage.show();

        drawImage();
    }

    private void drawImage(){
        PixelWriter pixelWriter = gc.getPixelWriter();

        for (int j = 0; j < imageHeigt; ++j) {
            for (int i = 0; i < imageWidth; ++i) {
               double r = (double)i / (imageWidth-1);
               double g = (double) j / (imageHeigt-1);
               double b = 0;

               Color currentColor = new Color(r, g, b, 1);
               pixelWriter.setColor(i,j,currentColor);
            }
            
        }


    }

    public static void main(String[] args) {
        launch();
    }

}