package com.egamboau.redering.materials;

import com.egamboau.objects.HitRecord;
import com.egamboau.redering.Material;
import com.egamboau.utils.ColorVector;
import com.egamboau.utils.Ray;
import com.egamboau.utils.Vector3D;

public class Lambertian extends Material{

    private ColorVector albedo;

    public Lambertian(ColorVector color) {
        this.albedo = color;
    }

    @Override
    public ScatterData scatter(Ray in, HitRecord record) {

        ScatterData result = new ScatterData();
        Vector3D scatterDirection = record.getNormal().addVector(Vector3D.getRandomUnitVector());

        if (scatterDirection.isNearZero()) {
            scatterDirection = record.getNormal();
        }
        result.setScatteredRay(new Ray(record.getP(), scatterDirection));
        result.setAlbedo(albedo);
        result.setScattered(true);
        
        return result;
    }
    
}
