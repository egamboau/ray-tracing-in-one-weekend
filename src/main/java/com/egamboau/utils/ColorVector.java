package com.egamboau.utils;

import javafx.scene.paint.Color;

public class ColorVector extends Vector3D {

    private static Interval intervalIntensity = new Interval(0, 0.999);

    public ColorVector(){
        super();
    }

    public ColorVector(double r,double g,double b) {
        super(r,g,b);
    }
    
    private double getB() {
        return getZ();
    }

    private double getG() {
        return getY();
    }

    private double getR() {
        return getX();
    }

    private static double linearToGamma(double linearComponent) {
        return Math.sqrt(linearComponent);
    }

    public static Color generateColor(ColorVector pixelColor, int samplesPerPixel) {

        double r = pixelColor.getR();
        double g = pixelColor.getG();
        double b = pixelColor.getB();

        double scale = 1.0D/samplesPerPixel;
        r*=scale;
        g*=scale;
        b*=scale;

        r = linearToGamma(r);
        g = linearToGamma(g);
        b = linearToGamma(b);

    
        Color currentColor = new Color(intervalIntensity.clamp(r), intervalIntensity.clamp(g), intervalIntensity.clamp(b), 1);
        return currentColor;
    }

    private static ColorVector convert3dVectorToColorVector(Vector3D vector) {
        return new ColorVector(vector.getX(), vector.getY(), vector.getZ());
    }


    public ColorVector multiplyVectorByScalar(double scalar) {
        Vector3D operationResult = super.multiplyVectorByScalar(scalar);
        return ColorVector.convert3dVectorToColorVector(operationResult);
    }

    public ColorVector addVector(ColorVector other) {
        Vector3D operationResult = super.addVector(other);
        return ColorVector.convert3dVectorToColorVector(operationResult);
    }

    public ColorVector multiplyVectorByVector(ColorVector other) {
        Vector3D operationResult = super.multiplyVectorByVector(other);
        return ColorVector.convert3dVectorToColorVector(operationResult);
    }

    public static ColorVector getRandomColorVector(){
        Vector3D operationResult = getRandomVector();
        return convert3dVectorToColorVector(operationResult);
    }

    public static ColorVector getRandomColorVector(double min, double max){
        Vector3D operationResult = getRandomVector(min, max);
        return convert3dVectorToColorVector(operationResult);
    }
}
