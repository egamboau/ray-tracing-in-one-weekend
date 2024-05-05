package com.egamboau.rendering;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

import com.egamboau.objects.HitRecord;
import com.egamboau.objects.Hittable;
import com.egamboau.rendering.Material.ScatterData;
import com.egamboau.utils.ColorVector;
import com.egamboau.utils.Interval;
import com.egamboau.utils.Ray;
import com.egamboau.utils.UtilitiesFunctions;
import com.egamboau.utils.Vector3D;

import javafx.application.Platform;
import javafx.scene.image.PixelWriter;
import javafx.scene.paint.Color;

public class Camera {

    private double aspectRatio = 1.0D;
    private int imageWidth = 100;
    private int samplesPerPixel = 10;
    private int imageHeigth;

    private double vfov = 90;

    private Vector3D lookfrom = new Vector3D(0,0,0);   // Point camera is looking from
    private Vector3D lookat = new Vector3D(0,0,-1);  // Point camera is looking at
    private Vector3D vup = new Vector3D(0,1,0);     // Camera-relative "up" direction


    private double defocusAngle = 0;
    private double focusDist = 10;


    private Vector3D originLocation;
    private Vector3D pixelDeltaU;
    private Vector3D pixelDeltaV;

    private Vector3D cameraCenter;

    private int maxDepth = 10;
    private Vector3D w;
    private Vector3D u;
    private Vector3D v;
    private Vector3D defocus_disk_u;
    private Vector3D defocus_disk_v;

    

    public void render(Hittable world, PixelWriter pixelWriter) {
        initialize();
        ExecutorService service = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        int imageHeigthDist = (imageHeigth)/10;
        int imageWidthDist = (imageWidth)/10;
        
        IntStream.iterate(0, boundaryMinHeight -> boundaryMinHeight < imageHeigth, boundaryMinHeight -> boundaryMinHeight + imageHeigthDist).forEach(boundaryMinHeight ->  {
            IntStream.iterate(0, boundaryMinWidth -> boundaryMinWidth < imageWidth, boundaryMinWidth -> boundaryMinWidth + imageWidthDist).forEach(boundaryMinWidth -> {
                service.execute(() -> {
                    IntStream.range(boundaryMinHeight, boundaryMinHeight + imageHeigthDist).filter(j -> j < imageHeigth).forEach(j -> {
                        IntStream.range(boundaryMinWidth, boundaryMinWidth + imageWidthDist).filter(i -> i < imageWidth).forEach(i -> {
                            System.err.println(String.format("\rworking pixel %d, %d", i, j));
                            ColorVector pixelColor = new ColorVector(0,0,0);
                            for (int sample = 0; sample < this.samplesPerPixel; sample++){
                                Ray r = this.getRay(i,j);
                                pixelColor = pixelColor.addVector(this.getRayColor(r,world, this.maxDepth));
                            }

                            Color currentColor = ColorVector.generateColor(pixelColor,samplesPerPixel);
                            Platform.runLater(()->pixelWriter.setColor(i,j,currentColor));
                        });
                    });
                });
        });
    });
}

    private Ray getRay(int i, int j) {
        // Get a randomly sampled camera ray for the pixel at location i,j.
        Vector3D deltaULocation = pixelDeltaU.multiplyVectorByScalar(i);
        Vector3D deltaVLocation = pixelDeltaV.multiplyVectorByScalar(j);
        Vector3D pixelCenter = originLocation.addVector(deltaULocation).addVector(deltaVLocation);

        Vector3D pixelSample = pixelCenter.addVector(this.pixelSampleSquare());
        Vector3D rayOrigin = (defocusAngle <= 0) ? cameraCenter : defocusDiskSample();;
        Vector3D rayDirection = pixelSample.substractVector(rayOrigin);
        return new Ray(rayOrigin, rayDirection);

    }

    private Vector3D defocusDiskSample() {
        // Returns a random point in the camera defocus disk.
        Vector3D p = Vector3D.getRandomVectorInUnitDisk();
        return cameraCenter.addVector(defocus_disk_u.multiplyVectorByScalar(p.getAt(0))).addVector(defocus_disk_v.multiplyVectorByScalar(p.getAt(1)));
    }

    private Vector3D pixelSampleSquare() {
        double px = -0.5 + UtilitiesFunctions.getRandomDouble();
        double py = -0.5 + UtilitiesFunctions.getRandomDouble();

        Vector3D deltaUValue = pixelDeltaU.multiplyVectorByScalar(px);
        Vector3D deltaVValue = pixelDeltaV.multiplyVectorByScalar(py);
        return deltaUValue.addVector(deltaVValue);
    }



    public void initialize() {
        // Calculate the image height, and ensure that it's at least 1.
        imageHeigth = (int) (imageWidth / aspectRatio);
        imageHeigth = (imageHeigth < 1) ? 1 : imageHeigth;

        cameraCenter = lookfrom;
        
        

        double theta = UtilitiesFunctions.degreesToRadians(vfov);
        double h = Math.tan(theta/2);
        double viewportHeight = 2 * h * focusDist;
        double viewportWidth = viewportHeight * ((double) imageWidth / imageHeigth);

        w = lookfrom.substractVector(lookat).getUnitVector();
        u = vup.crossProduct(w).getUnitVector();
        v = w.crossProduct(u).getUnitVector();

        // Calculate the vectors across the horizontal and down the vertical viewport edges.
        Vector3D viewport_u = u.multiplyVectorByScalar(viewportWidth);
        Vector3D viewport_v = v.multiplyVectorByScalar(-viewportHeight);

        // Calculate the horizontal and vertical delta vectors from pixel to pixel.
        pixelDeltaU = viewport_u.divideVectorByScalar(imageWidth);
        pixelDeltaV = viewport_v.divideVectorByScalar(imageHeigth);

        // Calculate the location of the upper left pixel.
        Vector3D viewportUpperLeft = cameraCenter
                .substractVector(w.multiplyVectorByScalar(focusDist))
                .substractVector(viewport_u.divideVectorByScalar(2))
                .substractVector(viewport_v.divideVectorByScalar(2));
        Vector3D deltaAdition = pixelDeltaU.addVector(pixelDeltaV);
        originLocation = viewportUpperLeft.addVector(deltaAdition.multiplyVectorByScalar(0.5));

        double defocus_radius = focusDist * Math.tan(UtilitiesFunctions.degreesToRadians(defocusAngle / 2));
        defocus_disk_u = u.multiplyVectorByScalar(defocus_radius);
        defocus_disk_v = v.multiplyVectorByScalar(defocus_radius);
                
    }

    private ColorVector getRayColor(Ray ray, Hittable world, int depth) {

        if (depth <= 0) {
            return new ColorVector(0,0,0);
        }

        HitRecord record = new HitRecord();

        if (world.hit(ray, new Interval(0.001, Double.POSITIVE_INFINITY), record)) {

            ScatterData scatterData = record.getMaterial().scatter(ray, record);
            if(scatterData.isScattered()) {
                return scatterData.getAlbedo().multiplyVectorByVector(getRayColor(scatterData.getScatteredRay(), world, depth-1));
            } else {
                return new ColorVector(0,0,0);
            }
        }
        
        Vector3D unitVector = ray.getDirection().getUnitVector();
        double a = 0.5*(unitVector.getY()+ 1.0) ;
        ColorVector whiteValue = new ColorVector(1,1,1).multiplyVectorByScalar(1.0-a);
        ColorVector blueValue = new ColorVector(0.5,0.7,1).multiplyVectorByScalar(a);

        return whiteValue.addVector(blueValue);
    }

    public double getAspectRatio() {
        return aspectRatio;
    }

    public void setAspectRatio(double aspectRatio) {
        this.aspectRatio = aspectRatio;
    }

    public int getImageWidth() {
        return imageWidth;
    }

    public void setImageWidth(int imageWidth) {
        this.imageWidth = imageWidth;
    }

    public int getImageHeigth() {
        if (imageHeigth == 0) {
            imageHeigth = (int) (imageWidth / aspectRatio);
            imageHeigth = (imageHeigth < 1) ? 1 : imageHeigth;
        }
        return imageHeigth;
    }

    public void setImageHeigth(int imageHeigth) {
        this.imageHeigth = imageHeigth;
    }

    public Vector3D getOriginLocation() {
        return originLocation;
    }

    public void setOriginLocation(Vector3D originLocation) {
        this.originLocation = originLocation;
    }

    public Vector3D getPixelDeltaU() {
        return pixelDeltaU;
    }

    public void setPixelDeltaU(Vector3D pixelDeltaU) {
        this.pixelDeltaU = pixelDeltaU;
    }

    public Vector3D getPixelDeltaV() {
        return pixelDeltaV;
    }

    public void setPixelDeltaV(Vector3D pixelDeltaV) {
        this.pixelDeltaV = pixelDeltaV;
    }

    public Vector3D getCameraCenter() {
        return cameraCenter;
    }

    public void setCameraCenter(Vector3D cameraCenter) {
        this.cameraCenter = cameraCenter;
    }

    public int getSamplesPerPixel() {
        return samplesPerPixel;
    }

    public void setSamplesPerPixel(int samplesPerPixel) {
        this.samplesPerPixel = samplesPerPixel;
    }

    public int getMaxDepth() {
        return maxDepth;
    }

    public void setMaxDepth(int max_depth) {
        this.maxDepth = max_depth;
    }

    public void setVfov(double vfov) {
        this.vfov = vfov;
    }


    public void setLookfrom(Vector3D lookfrom) {
        this.lookfrom = lookfrom;
    }

    public void setLookat(Vector3D lookat) {
        this.lookat = lookat;
    }

    public void setVup(Vector3D vup) {
        this.vup = vup;
    }

    public void setDefocusAngle(double defocusAngle) {
        this.defocusAngle = defocusAngle;
    }

    public void setFocusDist(double focusDist) {
        this.focusDist = focusDist;
    }
}
