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
import com.egamboau.utils.UtilitiesFunctions;
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
public class FinalRenderApp extends Application {

    int gcScaleX = 1;
    int gcScaleY = 1;

    double zoomScale = 0.1;

    @Override
    public void start(Stage primaryStage) throws InterruptedException, ExecutionException {

        Camera camera = new Camera();
        camera.setAspectRatio(16.0/9.0);
        camera.setImageWidth(1200);
        camera.setSamplesPerPixel(10);
        camera.setMaxDepth(50);

        camera.setVfov(20);
        camera.setLookfrom(new Vector3D(13,2,3));
        camera.setLookat(new Vector3D(0,0,0));
        camera.setVup(new Vector3D(0,1,0));

        camera.setDefocusAngle(0.6);
        camera.setFocusDist(10);

        HittableList world = new HittableList();
        Material materialGround = new Lambertian(new ColorVector(0.5, 0.5, 0.5));
        world.add(new Sphere(new Vector3D( 0,-1000,0), 1000.0, materialGround));

        for (int a = -11; a < 11; a++) {
            for (int b = -11; b < 11; b++) {
                double chooseMaterial = UtilitiesFunctions.getRandomDouble();
                Vector3D center = new Vector3D(a + 0.9*UtilitiesFunctions.getRandomDouble(), 0.2, b + 0.9*UtilitiesFunctions.getRandomDouble());

                if (center.substractVector(new Vector3D(4, 0.2, 0)).getLength() > 0.9) {
                    Material sphereMaterial = null;
                    
                    if (chooseMaterial < 0.8) {
                        // diffuse
                        ColorVector albedo = ColorVector.getRandomColorVector().multiplyVectorByVector(ColorVector.getRandomColorVector());
                        sphereMaterial = new Lambertian(albedo);
                        world.add(new Sphere(center, 0.2, sphereMaterial));
                    } else if (chooseMaterial < 0.95) {
                        // metal
                        ColorVector albedo = ColorVector.getRandomColorVector(0.5, 1);
                        double fuzz = UtilitiesFunctions.getRandomDouble(0,0.5);
                        sphereMaterial = new Metal(albedo, fuzz);
                        world.add(new Sphere(center, 0.2, sphereMaterial));
                    } else {
                        // glass
                        sphereMaterial = new Dielectric(1.5);
                        world.add(new Sphere(center, 0.2, sphereMaterial));
                    }

                }
            }
        }

        Material material1 = new Dielectric(1.5);
        world.add(new Sphere(new Vector3D(0, 1, 0), 1.0, material1));

        Material material2 = new Lambertian(new ColorVector(0.4, 0.2, 0.1));
        world.add(new Sphere(new Vector3D(-4, 1, 0), 1.0, material2));

        Material material3 = new Metal(new ColorVector(0.7, 0.6, 0.5), 0.0);
        world.add(new Sphere(new Vector3D(4, 1, 0), 1.0, material3));


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