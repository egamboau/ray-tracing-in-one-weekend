package com.egamboau.utils;

import javafx.scene.paint.Color;

public class ColorVector extends Vector3D {

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

    public static Color generateColor(ColorVector pixelColor) {

        double r = pixelColor.getR();
        double g = pixelColor.getG();
        double b = pixelColor.getB();

        Color currentColor = new Color(r, g, b, 1);
        return currentColor;
    }

    private ColorVector convert3dVectorToColorVector(Vector3D vector) {
        return new ColorVector(vector.getX(), vector.getY(), vector.getZ());
    }


    public ColorVector multiplyVectorByScalar(double scalar) {
        Vector3D operationResult = super.multiplyVectorByScalar(scalar);
        return this.convert3dVectorToColorVector(operationResult);
    }

    public ColorVector addVector(ColorVector other) {
        Vector3D operationResult = super.addVector(other);
        return this.convert3dVectorToColorVector(operationResult);
    }
}
