package com.egamboau.objects;

import com.egamboau.redering.Material;
import com.egamboau.utils.Ray;
import com.egamboau.utils.Vector3D;

public class HitRecord {

    private Vector3D p;
    private Vector3D normal;
    private double t;
    private boolean frontFace;
    private Material material;


    public Material getMaterial() {
        return material;
    }
    public void setMaterial(Material material) {
        this.material = material;
    }
    public boolean isFrontFace() {
        return frontFace;
    }
    public void setFrontFace(boolean frontFace) {
        this.frontFace = frontFace;
    }
    public Vector3D getP() {
        return p;
    }
    public void setP(Vector3D p) {
        this.p = p;
    }
    public Vector3D getNormal() {
        return normal;
    }
    public void setNormal(Vector3D normal) {
        this.normal = normal;
    }
    public double getT() {
        return t;
    }
    public void setT(double t) {
        this.t = t;
    }

    public void setFaceNormal(Ray ray, Vector3D outwardNormal){

        frontFace = ray.getDirection().dotProduct(outwardNormal) < 0;
        normal = frontFace? outwardNormal: outwardNormal.getNegatedVector();
    }
}
