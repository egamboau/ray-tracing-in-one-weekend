package com.egamboau;

import com.egamboau.objects.HittableList;
import com.egamboau.objects.Sphere;
import com.egamboau.redering.Camera;
import com.egamboau.utils.Interval;
import com.egamboau.utils.Vector3D;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

/**
 * JavaFX App
 */
public class App extends Application {

    int gcScaleX = 1;
    int gcScaleY = 1;

    double zoomScale = 0.1;

    @Override
    public void start(Stage primaryStage) {

        Camera camera = new Camera();
        camera.setAspectRatio(16.0/9.0);
        camera.setImageWidth(400);
        camera.setSamplesPerPixel(100);
        camera.setMaxDepth(50);

        HittableList world = new HittableList();
        world.add(new Sphere(new Vector3D(0,0,-1), 0.5));
        world.add(new Sphere(new Vector3D(0,-100.5,-1), 100));

        primaryStage.setTitle("Ray Tracing in a Weekend");
        Group root = new Group();
        ImageView canvas = new ImageView();
        canvas.setFitWidth(camera.getImageWidth());
        canvas.setFitHeight(camera.getImageHeigth());
        canvas.setCache(true);
        canvas.setViewport(new Rectangle2D(0, 0, camera.getImageWidth(), camera.getImageHeigth()));
        
        WritableImage writtableImage = new WritableImage(camera.getImageWidth(), camera.getImageHeigth());
        canvas.setImage(writtableImage);
        PixelWriter pixelWriter = writtableImage.getPixelWriter();
        
        root.getChildren().add(canvas);
        Scene scene = new Scene(root, camera.getImageWidth(), camera.getImageHeigth());
        primaryStage.setScene(scene);

        double deltaX = (canvas.getFitWidth() * zoomScale);
        double deltaY = (canvas.getFitHeight() * zoomScale);

        Interval xPositionInterval = new Interval(0, canvas.getFitWidth()-deltaX);
        Interval yPositionInterval = new Interval(0, canvas.getFitHeight()-deltaY);

        Interval heightInterval = new Interval(deltaY,canvas.getFitHeight());
        Interval widthInterval = new Interval(deltaX,canvas.getFitWidth());

        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {

            @Override
            public void handle(KeyEvent event) {

                Rectangle2D viewport = canvas.getViewport();

                double newMinX = viewport.getMinX();
                double newMinY = viewport.getMinY();

                switch (event.getCode()) {
                    case RIGHT:
                        newMinX += 10;
                        break;
                    case LEFT:
                        newMinX -= 10;
                        break;
                    case UP:
                        newMinY -= 10;
                        break;
                    case DOWN:
                        newMinY += 10;
                        break;
                    default:
                        break;
                }
                Rectangle2D newViewport = new Rectangle2D(xPositionInterval.clamp(newMinX), 
                    yPositionInterval.clamp(newMinY), 
                    viewport.getWidth(), 
                    viewport.getHeight());

                

                //if the image will not fit after moving, the avoid changing the viewport
                if (newViewport.getMaxX() <= canvas.getFitWidth() && newViewport.getMaxY() <= canvas.getFitHeight()) {
                    canvas.setViewport(newViewport);
                }
                
            }
            
        });


        scene.setOnKeyTyped(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                Rectangle2D viewport = canvas.getViewport();

                double newMinX = viewport.getMinX();
                double newMinY = viewport.getMinY();
                double newWidth = viewport.getWidth();
                double newHeigth = viewport.getHeight();
                
                switch (event.getCharacter()) {
                    case "+":
                        newMinX += deltaX/2;
                        newMinY += deltaY/2;
                        newWidth -=deltaX;
                        newHeigth -=deltaY;
                        break;  
                    case "-":
                        newMinX -= deltaX/2;
                        newMinY -= deltaY/2;
                        newWidth +=deltaX;
                        newHeigth +=deltaY;
                        break;
                    default:
                        break;
                }

                Rectangle2D newViewport = new Rectangle2D(xPositionInterval.clamp(newMinX), 
                    yPositionInterval.clamp(newMinY), 
                    widthInterval.clamp(newWidth), 
                    heightInterval.clamp(newHeigth));
                canvas.setViewport(newViewport);
                
            }
            
        });
        primaryStage.show();

        
        camera.render(world, pixelWriter);
    }


    public static void main(String[] args) {
        launch();
    }

}