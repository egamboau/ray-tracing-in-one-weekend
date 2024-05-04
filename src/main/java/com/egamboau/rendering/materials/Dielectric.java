package com.egamboau.rendering.materials;

import com.egamboau.objects.HitRecord;
import com.egamboau.rendering.Material;
import com.egamboau.utils.ColorVector;
import com.egamboau.utils.Ray;
import com.egamboau.utils.UtilitiesFunctions;
import com.egamboau.utils.Vector3D;

public class Dielectric extends Material {


    private double index_of_refraction;

    public Dielectric(double index_of_refraction) {
        this.index_of_refraction = index_of_refraction;
    }

    @Override
    public ScatterData scatter(Ray in, HitRecord record) {

        ScatterData result = new ScatterData();
        ColorVector attenuation = new ColorVector(1.0,1.0,1.0);

        double refractionRatio = record.isFrontFace() ? 1/index_of_refraction: index_of_refraction;

        Vector3D unitDirection = in.getDirection().getUnitVector();
        double cos_theta = Math.min(unitDirection.getNegatedVector().dotProduct(record.getNormal()), 1.0);
        double sin_theta = Math.sqrt(1.0 - Math.pow(cos_theta, 2));

        boolean can_not_refract = refractionRatio * sin_theta > 1;

        Vector3D direction = null;
        if (can_not_refract || reflectance(cos_theta, refractionRatio) > UtilitiesFunctions.getRandomDouble())
            direction = Vector3D.reflect(unitDirection, record.getNormal());
        else
            direction = Vector3D.refract(unitDirection, record.getNormal(), refractionRatio);

        result.setScatteredRay(new Ray(record.getP(), direction));
        result.setScattered(true);
        result.setAlbedo(attenuation);

        return result;
    }
    

    private double reflectance(double cosine, double refraction_index) {
        // Use Schlick's approximation for reflectance.
        double r0 = (1 - refraction_index) / (1 + refraction_index);
        r0 = r0*r0;
        return r0 + (1-r0)*Math.pow((1 - cosine),5);
    }
}
