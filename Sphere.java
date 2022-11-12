/*
 * Sphere.java 
 * 05/10/27
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
 * Scene Object: Sphere<br/>
 * A sphere that is defined by its center point in the space and its radius.
 * @author M S
 */
public class Sphere extends SceneObject {
    
    /** The sphere's position in the scene */
    private Point3d center;

    /** The sphere's radius */
    private double radius;
    
    /** The square of the sphere's radius */
    private double squareRadius;
    
    /**
     * Creates a sphere at a given location and with a specific radius.
     * 
     * @param center
     * @param radius
     */
    public Sphere(Point3d center, double radius) {
        this.center = center;
        this.radius = radius; 
        this.squareRadius = radius * radius;
        bounds = computeBounds();
    }

    /**
     * Returns the sphere's position.
     * @return The sphere's position.
     */
    public Point3d getCenter() {
        return center;
    }
    
    /**
     * Sets the sphere's position.
     * @param position The new position.
     */
    public void setCenter(Point3d position) {
        this.center = position;  
    }

    /**
     * Returns the sphere's radius.
     * @return The sphere's radius.
     */
    public double getRadius() {
        return radius;
    }

    /**
     * Sets the sphere's radius.
     * @param radius The new radius.
     */
    public void setRadius(double radius) {
        this.radius = radius;   
    }

    /**
     * Calculates the intersection point with a given ray and this
     * sphere. If there is an intersection point, the return value is the
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

        double b = 0.0;
        double c = 0.0;
        double t0 = 0.0;
        double t1 = 0.0;
        double discriminant = 0.0;

        b = 2 * (ray.getDirection().x * (ray.getOrigin().x - center.x)
                + ray.getDirection().y * (ray.getOrigin().y - center.y) + ray.getDirection().z
                * (ray.getOrigin().z - center.z));

        c = (ray.getOrigin().x - center.x) * (ray.getOrigin().x - center.x)
                + (ray.getOrigin().y - center.y) * (ray.getOrigin().y - center.y)
                + (ray.getOrigin().z - center.z) * (ray.getOrigin().z - center.z)
                - squareRadius;

        discriminant = b * b - 4 * c;
        // HINT: Normally, the discriminant would be "b^2 - 4ac"
        // In this case we can remove "a" from the calculation because "a" is
        // always 1
        // because "a = dx^2 + dy^2 + dz^2".
        // Reason: the ray direction vector is always normalized

        if (discriminant < 0)
            return 0.0;

        else {
            double root = Math.sqrt(discriminant);
            
            t0 = (-b - root) * 0.5;
            
            if(t0 > Scene.EPSILON) return t0;
            
            t1 = (-b + root) * 0.5;

            if(t1 > Scene.EPSILON) return t1;
            
            return 0.0;
        }

    }

    /**
     * Calculates the normalized normal vector of the surface point where the
     * given vector v hits the sphere.
     * 
     * @param pointOfIntersection The point for that the normal will be computed.
     * @return The normalized normal vector of the surface.
     */
    public Vector3d getSurfaceNormal(Point3d pointOfIntersection) {
        Vector3d normal = new Vector3d();
        normal.sub(pointOfIntersection, this.center);
        normal.normalize();
        return normal;
    }

    /**
     * @return the bounding box that contains the sphere.
     */
    BoundingVolume computeBounds() {
        BoundingBox boundingBox;
        Point3d pos = new Point3d(center);
        Vector3d size = new Vector3d(2*radius, 2*radius, 2*radius);
        pos.sub(new Vector3d(radius, radius, radius));
        boundingBox = new BoundingBox(pos, size);
        return boundingBox;
    }

    /**
     * @param cell The cell which will be checked on intersection with this scene object.
     * @return <code>true</code> if this scene object intersects the given cell
     * of the regular grid or <code>false</code> if not.
     */
    public boolean intersectsCell(Cell cell) {
        
        double distance = 0.0;
        
        // find the corner closest to the center of sphere

        if( center.x < cell.lower.x ) {
            distance = (center.x-cell.lower.x)*(center.x-cell.lower.x);
        }
        else if( center.x > cell.upper.x ) {
            distance = (center.x-cell.upper.x)*(center.x-cell.upper.x);
        }
        if( center.y < cell.lower.y ) {
            distance += (center.y-cell.lower.y)*(center.y-cell.lower.y);
        }
        else if( center.y > cell.upper.y ) {
            distance += (center.y-cell.upper.y)*(center.y-cell.upper.y);
        }
        if( center.z < cell.lower.z ) {
            distance += (center.z-cell.lower.z)*(center.z-cell.lower.z);
        }
        else if( center.z > cell.upper.z ) {
            distance += (center.z-cell.upper.z)*(center.z-cell.upper.z);
        }
        
        return ( distance <= this.squareRadius ); 
    }
}
