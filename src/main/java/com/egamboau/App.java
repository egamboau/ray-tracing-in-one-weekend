package com.egamboau;

import com.egamboau.objects.HitRecord;
import com.egamboau.objects.Hittable;
import com.egamboau.objects.HittableList;
import com.egamboau.objects.Sphere;
import com.egamboau.utils.ColorVector;
import com.egamboau.utils.Ray;
import com.egamboau.utils.Vector3D;

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

    private GraphicsContext gc;
    private int imageHeigth;
    private int imageWidth;

    @Override
    public void start(Stage primaryStage) {

        double ASPECT_RATIO = (double) 16 / 9;
        imageWidth = 400;

        // Calculate the image height, and ensure that it's at least 1.
        imageHeigth = (int) (imageWidth / ASPECT_RATIO);
        imageHeigth = (imageHeigth < 1) ? 1 : imageHeigth;

        HittableList world = new HittableList();
        world.add(new Sphere(new Vector3D(0,0,-1), 0.5));
        world.add(new Sphere(new Vector3D(0,-100.5,-1), 100));

        double focal_length = 1.0;
        double viewportHeight = 2.0;
        double viewportWidth = viewportHeight * ((double) imageWidth / imageHeigth);
        Vector3D cameraCenter = new Vector3D(0, 0, 0);

        // Calculate the vectors across the horizontal and down the vertical viewport
        // edges.
        Vector3D viewport_u = new Vector3D(viewportWidth, 0, 0);
        Vector3D viewport_v = new Vector3D(0, -viewportHeight, 0);

        // Calculate the horizontal and vertical delta vectors from pixel to pixel.
        Vector3D pixelDeltaU = viewport_u.divideVectorByScalar(imageWidth);
        Vector3D pixelDeltaV = viewport_v.divideVectorByScalar(imageHeigth);

        // Calculate the location of the upper left pixel.
        Vector3D viewportUpperLeft = cameraCenter
                .substractVector(new Vector3D(0, 0, focal_length))
                .substractVector(viewport_u.divideVectorByScalar(2))
                .substractVector(viewport_v.divideVectorByScalar(2));

        Vector3D deltaAdition = pixelDeltaU.addVector(pixelDeltaV);
        Vector3D originLocation = viewportUpperLeft.addVector(deltaAdition.multiplyVectorByScalar(0.5));

        primaryStage.setTitle("Ray Tracing in a Weekend");
        Group root = new Group();
        Canvas canvas = new Canvas(imageWidth, imageHeigth);
        gc = canvas.getGraphicsContext2D();
        root.getChildren().add(canvas);
        primaryStage.setScene(new Scene(root, imageWidth, imageHeigth));
        primaryStage.show();

        drawImage(originLocation, pixelDeltaU, pixelDeltaV, cameraCenter, world);
    }

    private void drawImage(Vector3D originLocation, Vector3D pixelDeltaU, Vector3D pixelDeltaV, Vector3D cameraCenter, Hittable world) {
        PixelWriter pixelWriter = gc.getPixelWriter();
        for (int j = 0; j < imageHeigth; ++j) {

            System.err.println(String.format("\rScanlines remaining: %d", imageHeigth - j));
            for (int i = 0; i < imageWidth; ++i) {
                Vector3D deltaUPosition = pixelDeltaU.multiplyVectorByScalar(i);
                Vector3D deltaVPosition = pixelDeltaV.multiplyVectorByScalar(j);
                Vector3D pixelCenter = originLocation.addVector(deltaUPosition).addVector(deltaVPosition);

                Vector3D rayDirection = pixelCenter.substractVector(cameraCenter);
                Ray ray = new Ray(cameraCenter, rayDirection);

                ColorVector pixelColor = getRayColor(ray, world);
                Color currentColor = ColorVector.generateColor(pixelColor);

                pixelWriter.setColor(i, j, currentColor);
            }

        }
    }

    private ColorVector getRayColor(Ray ray, Hittable world) {
        HitRecord record = new HitRecord();

        if (world.hit(ray, 0, Double.POSITIVE_INFINITY, record)) {
            Vector3D result =(record.getNormal().addVector(new ColorVector(1,1,1))).multiplyVectorByScalar(0.5);
            return new ColorVector(result.getX(), result.getY(), result.getZ());
        }
        
        Vector3D unitVector = ray.getDirection().getUnitVector();
        double a = 0.5*(unitVector.getY()+ 1.0) ;
        ColorVector whiteValue = new ColorVector(1,1,1).multiplyVectorByScalar(1.0-a);
        ColorVector blueValue = new ColorVector(0.5,0.7,1).multiplyVectorByScalar(a);

        return whiteValue.addVector(blueValue);
    }

    public static void main(String[] args) {
        launch();
    }

}