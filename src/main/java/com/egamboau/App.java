package com.egamboau;

import com.egamboau.objects.HittableList;
import com.egamboau.objects.Sphere;
import com.egamboau.redering.Camera;
import com.egamboau.utils.Vector3D;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.stage.Stage;

/**
 * JavaFX App
 */
public class App extends Application {

    @Override
    public void start(Stage primaryStage) {

        Camera camera = new Camera();
        camera.setAspectRatio(16.0/9.0);
        camera.setImageWidth(400);

        HittableList world = new HittableList();
        world.add(new Sphere(new Vector3D(0,0,-1), 0.5));
        world.add(new Sphere(new Vector3D(0,-100.5,-1), 100));

        primaryStage.setTitle("Ray Tracing in a Weekend");
        Group root = new Group();
        Canvas canvas = new Canvas(camera.getImageWidth(), camera.getImageHeigth());
        GraphicsContext gc = canvas.getGraphicsContext2D();
        root.getChildren().add(canvas);
        primaryStage.setScene(new Scene(root, camera.getImageWidth(), camera.getImageHeigth()));
        primaryStage.show();

        
        camera.render(world, gc);
    }


    public static void main(String[] args) {
        launch();
    }

}