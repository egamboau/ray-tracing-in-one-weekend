package com.egamboau;

import java.util.concurrent.ExecutionException;

import com.egamboau.objects.HittableList;
import com.egamboau.objects.Sphere;
import com.egamboau.rendering.materials.Dielectric;
import com.egamboau.rendering.materials.Lambertian;
import com.egamboau.rendering.materials.Metal;
import com.egamboau.rendering.Camera;
import com.egamboau.rendering.Material;
import com.egamboau.utils.ColorVector;
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
import javafx.stage.WindowEvent;

/**
 * JavaFX App
 */
public class App extends Application {

    int gcScaleX = 1;
    int gcScaleY = 1;

    double zoomScale = 0.1;

    @Override
    public void start(Stage primaryStage) throws InterruptedException, ExecutionException {

        Camera camera = new Camera();
        camera.setAspectRatio(16.0/9.0);
        camera.setImageWidth(400);
        camera.setSamplesPerPixel(100);
        camera.setMaxDepth(50);

        camera.setVfov(20);
        camera.setLookfrom(new Vector3D(-2,2,1));
        camera.setLookat(new Vector3D(0,0,-1));
        camera.setVup(new Vector3D(0,1,0));

        camera.setDefocusAngle(10.0);
        camera.setFocusDist(3.4);

        HittableList world = new HittableList();
        Material materialGround = new Lambertian(new ColorVector(0.8, 0.8, 0.0));
        Material materialCenter = new Lambertian(new ColorVector(0.1,0.2,0.5));
        Material materialBubble = new Dielectric(1.00 / 1.50);
        Material materialRight  = new Metal(new ColorVector(0.8, 0.6, 0.2), 0.0);

        world.add(new Sphere(new Vector3D( 0.0, -100.5, -1.0), 100.0, materialGround));
        world.add(new Sphere(new Vector3D( 0.0,    0.0, -1.0),   0.5, materialCenter));
        //world.add(new Sphere(new Vector3D(-1.0,    0.0, -1.0),   0.5, materialLeft));
        world.add(new Sphere(new Vector3D(-1.0,    0.0, -1.0),   0.5, materialBubble));

        world.add(new Sphere(new Vector3D( 1.0,    0.0, -1.0),   0.5, materialRight));

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
        primaryStage.setOnShown(new EventHandler<WindowEvent>() {

            @Override
            public void handle(WindowEvent event) {
                camera.render(world, pixelWriter);
            }
        });

        primaryStage.show();
    }


    public static void main(String[] args) {
        launch();
    }

}