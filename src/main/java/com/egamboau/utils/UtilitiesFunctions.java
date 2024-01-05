package com.egamboau.utils;

import java.util.Random;

public class UtilitiesFunctions {

    private static Random randomGenerator = new Random();

    public static double degrees_to_radians(double degrees) {
        return degrees * Math.PI / 180.0;
    }

    public static double getRandomDouble() {
        return randomGenerator.nextDouble();
    }

    public static double getRandomDouble(double min, double max) {
        return min + (max - min) * randomGenerator.nextDouble();
    }
}
