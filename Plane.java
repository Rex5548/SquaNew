/*
 * Plane.java 
 * 05/11/05
 * 
 * University of Applied Sciences
 * Bingen, Germany
 * 
 * FPro Project "Jaytracer"
 * 
 * Autor: 
 * M S
 * 
 * 
 */

package de.fhbingen.fpro.jaytracer;

import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

/**
 * SceneObject: Plane<br/>
 * A plane that is defined by a point in space and a normal vector.
 * @author M S
 *
 */
public class Plane extends SceneObject {

    /** The plane's normal vector */
    private Vector3d surfaceNormal;
    
    /** A point on the plane */
    private Point3d point;
    
    /**
     * Creates a plane that intersects the given point and that has the given normal vector.
     * @param point A point, that lies on the plane.
     * @param normal The plane's normal.
     */
    public Plane(Point3d point, Vector3d normal) {
        this.point = point;
        normal.normalize();
        this.surfaceNormal = normal;
        bounds = computeBounds();
    }
    
    /**
     * Calculates the intersection point with a given ray and this
     * plane. If there is an intersection point, the return value is the
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
    public double intersect(XRay ray) {

        Vector3d rayDir = new Vector3d(ray.getDirection());
        Vector3d planeNormal = new Vector3d(surfaceNormal);
        Vector3d rayStart = new Vector3d(ray.getOrigin());
        Vector3d planePoint = new Vector3d(point);
        
        double base = planeNormal.dot(rayDir);
        
        if(base == 0.0) {
            //planeNormal and rayDir are orthogonal
            return 0.0;
        } 
        
        double t;
        rayStart.sub(planePoint);
        t = -(planeNormal.dot(rayStart)) / base;
        if(t > Scene.EPSILON) {
            return t;
        } else {
            return 0.0;
        }
        
    }
    
    /**
     * @param pointOfIntersection The point for that the normal will be computed.
     * @return the normal of the plane.
     */
    public Vector3d getSurfaceNormal(Point3d pointOfIntersection) {
        return this.surfaceNormal;
    }

    /**
     * @param normal The normal to set, that defines the plane.
     */
    public void setSurfaceNormal(Vector3d normal) {
        normal.normalize();
        this.surfaceNormal = normal;
    }

    /**
     * @return Returns the point.
     */
    public Point3d getPoint() {
        return point;
    }

    /**
     * @param point The point to set, which defines the plane.
     */
    public void setPoint(Point3d point) {
        this.point = point;
    }

    /**
     * @return The bounding volume of this plane. (inifinitesimal)
     */
    BoundingVolume computeBounds() {
        Point3d lower = new Point3d(Double.NaN , Double.NaN , Double.NaN);
        Vector3d size = new Vector3d(Double.NaN, Double.NaN, Double.NaN);
        return new BoundingBox(lower, size); 
    }

    /**
     * @param cell The cell which will be checked on intersection with this scene object.
     * @return <code>true</code> if this scene object intersects the given cell
     * of the regular grid or <code>false</code> if not.
     */
    public boolean intersectsCell(Cell cell) {
        return bounds.intersectsBoundingVolume(cell);
    }
}
