package com.egamboau.objects;

import com.egamboau.utils.Interval;
import com.egamboau.utils.Ray;
import com.egamboau.utils.Vector3D;

public class Sphere extends Hittable{

    private Vector3D center;
    private double radius;

    public Sphere(Vector3D center, double radius) {
        this.center = center;
        this.radius = radius;
    }

    @Override
    public boolean hit(Ray ray, Interval rayT, HitRecord record) {
        Vector3D oc = ray.getOrigin().substractVector(center);
        double a = ray.getDirection().getLengthSquared();
        double halfB = oc.dotProduct(ray.getDirection());
        double c = oc.getLengthSquared() - (radius * radius);
        double discriminant = halfB*halfB - a*c;

        if(discriminant < 0 ) {
            return false;
        }

        double discriminantSqrtd = Math.sqrt(discriminant);
        // Find the nearest root that lies in the acceptable range.
        double root = (-halfB - discriminantSqrtd) / a;

        if (!rayT.surrounds(root)) {
            root = (-halfB + discriminantSqrtd) / a;
            if (!rayT.surrounds(root))
                return false;
        }

        record.setT(root);
        record.setP(ray.movePointToPositionInRay(record.getT()));

        Vector3D outwardNormal = (record.getP().substractVector(center)).divideVectorByScalar(radius);
        record.setFaceNormal(ray, outwardNormal);
        

        return true;
    }
    
}
