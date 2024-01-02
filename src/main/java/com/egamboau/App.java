package com.egamboau;

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

        drawImage(originLocation, pixelDeltaU, pixelDeltaV, cameraCenter);
    }

    private void drawImage(Vector3D originLocation, Vector3D pixelDeltaU, Vector3D pixelDeltaV, Vector3D cameraCenter) {
        PixelWriter pixelWriter = gc.getPixelWriter();
        for (int j = 0; j < imageHeigth; ++j) {

            System.err.println(String.format("\rScanlines remaining: %d", imageHeigth - j));
            for (int i = 0; i < imageWidth; ++i) {
                Vector3D deltaUPosition = pixelDeltaU.multiplyVectorByScalar(i);
                Vector3D deltaVPosition = pixelDeltaV.multiplyVectorByScalar(j);
                Vector3D pixelCenter = originLocation.addVector(deltaUPosition).addVector(deltaVPosition);

                Vector3D rayDirection = pixelCenter.substractVector(cameraCenter);
                Ray ray = new Ray(cameraCenter, rayDirection);

                ColorVector pixelColor = getRayColor(ray);
                Color currentColor = ColorVector.generateColor(pixelColor);

                pixelWriter.setColor(i, j, currentColor);
            }

        }
    }

    private ColorVector getRayColor(Ray ray) {
        if (this.rayHitSphere(new Vector3D(0,0,10), 0.5, ray)) {
            return new ColorVector(1, 0, 0);
        }
        Vector3D unitVector = ray.getDirection().getUnitVector();
        double a = 0.5*(unitVector.getY()+ 1.0) ;
        ColorVector whiteValue = new ColorVector(1,1,1).multiplyVectorByScalar(1.0-a);
        ColorVector blueValue = new ColorVector(0.5,0.7,1).multiplyVectorByScalar(a);

        return whiteValue.addVector(blueValue);
    }


    private boolean rayHitSphere(Vector3D center, double radius, Ray ray) {
        Vector3D oc = ray.getOrigin().substractVector(center);
        double a = ray.getDirection().dotProduct(ray.getDirection());
        double b = 2.0 * oc.dotProduct(ray.getDirection());
        double c = oc.dotProduct(oc) - (radius * radius);
        double discriminant = b*b - 4*a*c;
        return discriminant >= 0;

    }

    public static void main(String[] args) {
        launch();
    }

}