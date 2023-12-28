package com.egamboau.utils;

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
                this.vector[0] + scalar,
                this.vector[1] + scalar,
                this.vector[2] + scalar);
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

    @Override
    public String toString() {
        return String.format("%f %f %f", this.vector[0], this.vector[1], this.vector[2]);
    }
}
