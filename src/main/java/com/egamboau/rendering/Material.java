package com.egamboau.rendering;

import com.egamboau.objects.HitRecord;
import com.egamboau.utils.ColorVector;
import com.egamboau.utils.Ray;

public abstract class Material {
    
    public abstract ScatterData scatter(Ray in, HitRecord record);


    public class ScatterData {
        Ray scatteredRay;
        ColorVector albedo;
        boolean scattered;

        
        public Ray getScatteredRay() {
            return scatteredRay;
        }
        public void setScatteredRay(Ray scatteredRay) {
            this.scatteredRay = scatteredRay;
        }
        public ColorVector getAlbedo() {
            return albedo;
        }
        public void setAlbedo(ColorVector albedo) {
            this.albedo = albedo;
        }
        public boolean isScattered() {
            return scattered;
        }
        public void setScattered(boolean scattered) {
            this.scattered = scattered;
        }
        
        
    }
}
