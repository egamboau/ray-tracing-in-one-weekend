package com.egamboau.utils;

public class Interval {

    public static Interval empty = new Interval();
    public static Interval universe = new Interval(Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY);

    
    private double min;
    private double max;

    public Interval() {
        this(Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY);
    }

    public Interval(double minValue, double maxValue) {
        this.min = minValue;
        this.max = maxValue;
    }

    public boolean contains(double value) {
        return min <= value && value <= max;
    }

    public boolean surrounds(double value) {
        return min < value && value < max;
    }

    public double clamp(double value) {
        if (value < min) {
            return min;
        }

        if(value > max) {
            return max;
        }

        return value;
    }

    public double getMin() {
        return min;
    }

    public double getMax() {
        return max;
    }
}
