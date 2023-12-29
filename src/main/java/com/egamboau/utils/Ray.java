package com.egamboau.utils;

public class Ray {

    private Vector3D origin;
    private Vector3D direction;


    public Vector3D getOrigin() {
        return origin;
    }

    public Vector3D getDirection() {
        return direction;
    }

    public Ray() {
        this(new Vector3D(), new Vector3D());
    }

    public Ray(Vector3D origin, Vector3D direction) {
        this.origin = origin;
        this.direction = direction;
    }

    Vector3D movePointToPositionInRay(double newPosition) {
        return this.origin.addVector(direction.multiplyVectorByScalar(newPosition));
    }


}
