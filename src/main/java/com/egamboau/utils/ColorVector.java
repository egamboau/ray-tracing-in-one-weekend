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

    
    
}
