package com.egamboau.objects;

import com.egamboau.utils.Ray;

public abstract class Hittable {
    
    public abstract boolean hit(Ray r, double rayTMin, double rayTMax, HitRecord record);
}
