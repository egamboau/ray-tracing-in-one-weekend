package com.egamboau;

import com.egamboau.objects.HittableList;
import com.egamboau.objects.Sphere;
import com.egamboau.redering.Camera;
import com.egamboau.utils.Vector3D;

import javafx.application.Application;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

/**
 * JavaFX App
 */
public class App extends Application {

    int gcScaleX = 1;
    int gcScaleY = 1;

    @Override
    public void start(Stage primaryStage) {

        Camera camera = new Camera();
        camera.setAspectRatio(16.0/9.0);
        camera.setImageWidth(400);
        camera.setSamplesPerPixel(100);

        HittableList world = new HittableList();
        world.add(new Sphere(new Vector3D(0,0,-1), 0.5));
        world.add(new Sphere(new Vector3D(0,-100.5,-1), 100));

        primaryStage.setTitle("Ray Tracing in a Weekend");
        Group root = new Group();
        Canvas canvas = new Canvas(camera.getImageWidth(), camera.getImageHeigth());
        GraphicsContext gc = canvas.getGraphicsContext2D();
        
        root.getChildren().add(canvas);
        Scene scene = new Scene(root, camera.getImageWidth(), camera.getImageHeigth());
        primaryStage.setScene(scene);


        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                // TODO Auto-generated method stub
                System.out.println(event.getText());
                System.out.println(canvas.getScaleX());
                System.out.println(canvas.getScaleY());
                switch (event.getText()) {
                    case "+":
                        canvas.setScaleX(canvas.getScaleX() + 1);
                        canvas.setScaleY(canvas.getScaleY() + 1);
                        break;  
                    case "-":
                        canvas.setScaleX(canvas.getScaleX() - 1);
                        canvas.setScaleY(canvas.getScaleY() - 1);        
                        break;
                    default:
                        break;
                }
                
                
            }
            
        });
        primaryStage.show();

        
        camera.render(world, gc);
    }


    public static void main(String[] args) {
        launch();
    }

}