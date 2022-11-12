/*
 * SceneObject.java 
 * 05/10/23
 * 
 * University of Applied Sciences
 * Bingen, Germany
 * 
 * FPro Project "Jaytracer"
 * 
 * Author:
 * M S
 * 
 */

package de.fhbingen.fpro.jaytracer;

import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

/**
 * This is the super class for all scene objects. Every scene object which will be
 * implemented, is to derivate from this class.
 * 
 * @author M S
 * 
 */
abstract public class SceneObject {

    /** Bounding Volume */
    protected BoundingVolume bounds;
    
    /** A container for the scene object's material properties. */
    protected Material material;
    
    /**
     * @param m The scene object's material properties.
     */
    void setMaterial(Material m) {
        this.material = m;
    }

    /**
     * @return the scene object's material properties.
     */
    public Material getMaterial() {
        return this.material;
    }

    /**
     * Every scene object has to provide a method for calculating the
     * intersection point of the object with a given ray.
     * 
     * @param r
     * @return the t-parameter value of the straight line equation which is
     *         represented by the ray, where the ray intersects the object.
     */
    abstract public double intersect(XRay r);

    /**
     * Every scene object has to provide a method for calculate the surface
     * normal vector at the intersection point with a given vector.
     * 
     * @param pointOfIntersection The point for that the normal will be computed.
     * @return the normalized surface normal vector.
     */
    abstract public Vector3d getSurfaceNormal(Point3d pointOfIntersection);
    
    /**
     * @param cell The cell which will be checked on intersection with this scene object.
     * @return <code>true</code> if this scene object intersects the given cell
     * of the regular grid or <code>false</code> if not.
     */
    abstract public boolean intersectsCell(Cell cell);

    /**
     * @return Returns the bounds.
     */
    public BoundingVolume getBounds() {
        return bounds;
    }

    /**
     * @param bounds The bounds to set.
     */
    public void setBounds(BoundingVolume bounds) {
        this.bounds = bounds;
    }
    
    /**
     * 
     * @return the bounding volume for this object.
     */
    abstract BoundingVolume computeBounds();
}
