package com.egamboau.rendering.materials;

import com.egamboau.objects.HitRecord;
import com.egamboau.rendering.Material;
import com.egamboau.utils.ColorVector;
import com.egamboau.utils.Ray;
import com.egamboau.utils.Vector3D;

public class Metal extends Material {

    private ColorVector albedo;
    private double fuzz;
    public Metal(ColorVector albedo, double fuzz){
        this.albedo = albedo;
        this.fuzz = fuzz < 1? fuzz: 1;
    }
    @Override
    public ScatterData scatter(Ray in, HitRecord record) {

        ScatterData result = new ScatterData();
        Vector3D reflected = Vector3D.reflect(in.getDirection().getUnitVector(), record.getNormal());
        result.setScatteredRay(new Ray(record.getP(), reflected.addVector(Vector3D.getRandomUnitVector().multiplyVectorByScalar(fuzz))));
        result.setAlbedo(albedo);
        result.setScattered(true);

        return result;
    }
    
}
