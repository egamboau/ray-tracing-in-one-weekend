package com.egamboau.utils;

import java.util.Arrays;

public class Vector3D {

    private double[] vector;

    public Vector3D() {
        this(0, 0, 0);
    }

    public Vector3D(double x, double y, double z) {
        this.vector = new double[] { x, y, z };
    }

    public double getX() {
        return vector[0];
    }

    public double getY() {
        return vector[1];
    }

    public double getZ() {
        return vector[2];
    }

    public double getAt(int index) {
        return vector[index];
    }

    public Vector3D addVector(Vector3D other) {
        return new Vector3D(
                this.vector[0] + other.vector[0],
                this.vector[1] + other.vector[1],
                this.vector[2] + other.vector[2]);
    }

    public Vector3D substractVector(Vector3D other) {

        return new Vector3D(
                this.vector[0] - other.vector[0],
                this.vector[1] - other.vector[1],
                this.vector[2] - other.vector[2]);
    }

    public Vector3D multiplyVectorByVector(Vector3D other) {

        return new Vector3D(
                this.vector[0] * other.vector[0],
                this.vector[1] * other.vector[1],
                this.vector[2] * other.vector[2]);
    }

    public Vector3D multiplyVectorByScalar(double scalar) {
        return new Vector3D(
                this.vector[0] * scalar,
                this.vector[1] * scalar,
                this.vector[2] * scalar);
    }

    public Vector3D divideVectorByScalar(double scalar) {
        return this.multiplyVectorByScalar(1 / scalar);
    }

    public double getLengthSquared() {
        double result = 0;
        for (double element : vector) {
            result += element * element;
        }
        return result;
    }

    public double getLength() {
        return Math.sqrt(getLengthSquared());
    }

    public double dotProduct(Vector3D another) {
        double result = 0;
        for (int i = 0; i < this.vector.length; i++) {
            result += vector[i] * another.vector[i];
        }
        return result;
    }

    public Vector3D crossProduct(Vector3D another) {
        return new Vector3D(
                this.vector[1] * another.vector[2] - this.vector[2] * another.vector[1],
                this.vector[2] * another.vector[0] - this.vector[0] * another.vector[2],
                this.vector[0] * another.vector[1] - this.vector[1] * another.vector[0]);
    }

    public Vector3D getUnitVector() {
        return this.divideVectorByScalar(this.getLength());
    }

    public boolean isNearZero() {
        double s = 1e-18;
        return (Math.abs(getX()) < s) && (Math.abs(getY()) < s) && (Math.abs(getZ()) < s);
    }

    

    @Override
    public String toString() {
        return "Vector3D [vector=" + Arrays.toString(vector) + "]";
    }

    public Vector3D getNegatedVector() {
        return new Vector3D(-getX(), -getY(), -getZ());
    }

    public static Vector3D getRandomVector() {
        return new Vector3D(UtilitiesFunctions.getRandomDouble(), UtilitiesFunctions.getRandomDouble(), UtilitiesFunctions.getRandomDouble());
    }

    public static Vector3D getRandomVector(double min, double max) {
        return new Vector3D(UtilitiesFunctions.getRandomDouble(min, max), UtilitiesFunctions.getRandomDouble(min, max), UtilitiesFunctions.getRandomDouble(min, max));
    }

    public static Vector3D getRandomVectorInUnitSphere() {
        Vector3D result = null;
        while (result == null) {
            Vector3D p = Vector3D.getRandomVector(-1, 1);
            if (p.getLengthSquared() < 1) {
                result = p;
            }
        }
        return result;
    }

    public static Vector3D getRandomVectorInUnitDisk() {
        while (true) {
            Vector3D p = new Vector3D(UtilitiesFunctions.getRandomDouble(-1,1), UtilitiesFunctions.getRandomDouble(-1,1), 0.0);
            if (p.getLengthSquared() < 1)
                return p;
        }
    }

    public static Vector3D reflect(Vector3D v, Vector3D n) {
        double bLength = v.dotProduct(n);
        Vector3D b = n.multiplyVectorByScalar(bLength);
        Vector3D bTimes2 = b.multiplyVectorByScalar(2);
        return v.substractVector(bTimes2);
    }

    public static Vector3D getRandomUnitVector() {
        return getRandomVectorInUnitSphere().getUnitVector();
    }

    public static Vector3D randomVectorOnHemisphere(Vector3D normal) {
        Vector3D vectorOnUnitSphere = Vector3D.getRandomUnitVector();
        if(vectorOnUnitSphere.dotProduct(normal) > 0.0) {
            return vectorOnUnitSphere;
        } else {
            return vectorOnUnitSphere.getNegatedVector();
        }
    }

    public static Vector3D refract(Vector3D uv, Vector3D n, double etai_over_etat) {
        double cos_theta = Math.min(uv.getNegatedVector().dotProduct(n), 1.0);
        Vector3D r_out_perpendicular = (uv.addVector(n.multiplyVectorByScalar(cos_theta))).multiplyVectorByScalar(etai_over_etat);
        Vector3D r_out_parallel = n.multiplyVectorByScalar(-Math.sqrt(Math.abs(1.0 - r_out_perpendicular.getLengthSquared())));
        return r_out_perpendicular.addVector(r_out_parallel);
    }
} 
