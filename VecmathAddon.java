/*
 * VecmathAddon.java 
 * 11/11/05
 * 
 * University of Applied Sciences
 * Bingen, Germany
 * 
 * FPro Project "Jaytracer"
 * 
 * Autor: 
 * M S
 * 
 */
package de.fhbingen.fpro.jaytracer;

import javax.vecmath.Color3f;
import javax.vecmath.Vector3d;

/**
 * Additional vecmath methods.
 * @author M S
 */
public class VecmathAddon {
    
    /**
     * @param v1 Vector A.
     * @param v2 Vector B.
     * @return The compontentwise multiplication of A and B.
     */
    public static Vector3d mulVector3d(Vector3d v1, Vector3d v2) {
        return new Vector3d(v1.x*v2.x, v1.y*v2.y, v1.z*v2.z);
    }
    
    /**
     * 
     * @param c1 Color A.
     * @param c2 Color B.
     * @return The compontentwise multiplication of A and B.
     */
    public static Color3f mulColor3f(Color3f c1, Color3f c2) {
        return new Color3f(c1.x*c2.x, c1.y*c2.y, c1.z*c2.z);
    }
}
