package com.egamboau.objects;

import java.util.ArrayList;
import java.util.List;

import com.egamboau.utils.Ray;

public class HittableList extends Hittable{

    private List<Hittable> objects;

    public HittableList() {
        this.objects = new ArrayList<>();
    }

    public HittableList(Hittable object) {
        this.objects = new ArrayList<>();
        objects.add(object);
    }

    public void clear() {
        objects.clear();
    }

    public void add(Hittable object) {
        objects.add(object);
    }

    @Override
    public boolean hit(Ray r, double rayTMin, double rayTMax, HitRecord record) {
        HitRecord temporal = new HitRecord();
        boolean hitAnything = false;
        double closest = rayTMax;

        for (Hittable object: this.objects) {
            if(object.hit(r,rayTMin,closest,temporal)) {
                hitAnything = true;
                closest = temporal.getT();
                record.setFrontFace(temporal.isFrontFace());
                record.setNormal(temporal.getNormal());
                record.setP(temporal.getP());
                record.setT(temporal.getT());
                record = temporal;
            }
        }
        
        return hitAnything;
    }

}
