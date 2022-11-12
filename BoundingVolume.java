/*
 * BoundingVolume.java 
 * 05/12/18
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

/**
 * Bounding Volume base class.
 * @author M S
 */
interface BoundingVolume {
    
    /**
     * Calculates the intersection point with a given ray and this
     * bounding volume. If there is an intersection point, the return value is the
     * t-parameter value in the parametric notation of the straight line that is
     * defined by the ray. (example straight line equation: P=A+t*V, with P, A
     * and V as vectors, P=intersection point, A=ray startpoint, V=ray
     * direction) If there is no intersection Point with the ray, the return
     * value will be 0.
     * 
     * @param ray
     * @return The t-parameter of the straight line equation the ray represents,
     *         where the ray intersects the sphere. 0, if the sphere is not
     *         intersected.
     */
    abstract public double intersect(XRay ray);
    
    /**
     * @param boundingVolume The bounding volume which will be checked on intersection with this bounding volume.
     * @return <code>true</code> if this box intersects the given cell
     * of the regular grid or <code>false</code> if not.
     */
    abstract public boolean intersectsBoundingVolume(BoundingVolume boundingVolume);
    
    /**
     * @param point
     * @return <code>true</code> if the given point is contained by this bounding volume, else <code>false</code>.
     */
    abstract public boolean containsPoint(Point3d point);
    
    /**
     * @param boundingVolume
     * @return <code>true</code>, if this bounding volume contains the given one, else <code>false</code>.
     */
    abstract public boolean containsBoundingVolume(BoundingVolume boundingVolume);
    
    /**
     * @return <code>true</code>, if the bounding volumes size is infinite.
     */
    abstract public boolean isInfinite();
    
    /**
     * @return <code>true</code>, if the bounding volumes size inifinitesimal.
     */
    abstract public boolean isInfinitesimal();
}
