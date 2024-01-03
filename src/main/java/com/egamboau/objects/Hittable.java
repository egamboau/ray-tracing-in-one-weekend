package com.egamboau.objects;

import com.egamboau.utils.Interval;
import com.egamboau.utils.Ray;

public abstract class Hittable {
    
    public abstract boolean hit(Ray r, Interval rayT, HitRecord record);
}
