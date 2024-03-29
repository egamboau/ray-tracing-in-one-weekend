package com.egamboau.redering;

import com.egamboau.objects.HitRecord;
import com.egamboau.objects.Hittable;
import com.egamboau.redering.Material.ScatterData;
import com.egamboau.utils.ColorVector;
import com.egamboau.utils.Interval;
import com.egamboau.utils.Ray;
import com.egamboau.utils.UtilitiesFunctions;
import com.egamboau.utils.Vector3D;
import javafx.scene.image.PixelWriter;
import javafx.scene.paint.Color;

public class Camera {

    private double aspectRatio = 1.0D;
    private int imageWidth = 100;
    private int samplesPerPixel = 10;
    private int imageHeigth;

    private Vector3D originLocation;
    private Vector3D pixelDeltaU;
    private Vector3D pixelDeltaV;

    private Vector3D cameraCenter;

    private int maxDepth = 10;

    

    public void render(Hittable world, PixelWriter pixelWriter) {
        initialize();
        for (int j = 0; j < imageHeigth; ++j) {

            System.err.println(String.format("\rScanlines remaining: %d", imageHeigth - j));
            for (int i = 0; i < imageWidth; ++i) {
                ColorVector pixelColor = new ColorVector(0,0,0);

                for (int sample = 0; sample < this.samplesPerPixel; sample++){
                    Ray r = this.getRay(i,j);
                    pixelColor = pixelColor.addVector(this.getRayColor(r,world, this.maxDepth));
                }
                
                //ColorVector pixelColor = getRayColor(ray, world);
                Color currentColor = ColorVector.generateColor(pixelColor,samplesPerPixel);

                pixelWriter.setColor(i, j, currentColor);
            }

        }
    }

    private Ray getRay(int i, int j) {
        // Get a randomly sampled camera ray for the pixel at location i,j.
        Vector3D deltaULocation = pixelDeltaU.multiplyVectorByScalar(i);
        Vector3D deltaVLocation = pixelDeltaV.multiplyVectorByScalar(j);
        Vector3D pixelCenter = originLocation.addVector(deltaULocation).addVector(deltaVLocation);

        Vector3D pixelSample = pixelCenter.addVector(this.pixelSampleSquare());
        Vector3D rayOrigin = cameraCenter;
        Vector3D rayDirection = pixelSample.substractVector(rayOrigin);
        return new Ray(rayOrigin, rayDirection);

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

        cameraCenter = new Vector3D(0, 0, 0);

        // Determine viewport dimensions.
        double focal_length = 1.0;
        double viewportHeight = 2.0;
        double viewportWidth = viewportHeight * ((double) imageWidth / imageHeigth);

        // Calculate the vectors across the horizontal and down the vertical viewport edges.
        Vector3D viewport_u = new Vector3D(viewportWidth, 0, 0);
        Vector3D viewport_v = new Vector3D(0, -viewportHeight, 0);

        // Calculate the horizontal and vertical delta vectors from pixel to pixel.
        pixelDeltaU = viewport_u.divideVectorByScalar(imageWidth);
        pixelDeltaV = viewport_v.divideVectorByScalar(imageHeigth);

        // Calculate the location of the upper left pixel.
        Vector3D viewportUpperLeft = cameraCenter
                .substractVector(new Vector3D(0, 0, focal_length))
                .substractVector(viewport_u.divideVectorByScalar(2))
                .substractVector(viewport_v.divideVectorByScalar(2));
        Vector3D deltaAdition = pixelDeltaU.addVector(pixelDeltaV);
        originLocation = viewportUpperLeft.addVector(deltaAdition.multiplyVectorByScalar(0.5));
                
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
    
}
