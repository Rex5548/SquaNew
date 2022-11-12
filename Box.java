/*
 * Box.java 
 * 05/12/09
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
 * SceneObject: axis aligned box
 * @author M S
 */
public class Box extends SceneObject {

    /** The box's side length */
    protected Vector3d size;
    
    /** The lower point of the box */
    protected Point3d lower;
    
    /** The upper point of the box (lower+size) */
    protected Point3d upper;
    
    /**
     * Creates a axis aligned box
     * @param lower The box's lower point.
     * @param size The box's size vector.
     */
    public Box(Point3d lower, Vector3d size) {
        this.lower = lower;
        this.size = size;
        this.upper = new Point3d();
        this.upper.add(lower, size);
        bounds = computeBounds();
    }
    
    /**
     * Constructor for bounding box use.
     */
    protected Box() { 
    }
    
    /**
     * Calculates the intersection point with a given ray and this
     * bounding box. If there is an intersection point, the return value is the
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
        
        double[] dist = new double[6];
        Vector3d d = new Vector3d(ray.getDirection()), o = new Vector3d(ray.getOrigin());
        Vector3d[] ip = new Vector3d[6];
        
        double t = Double.POSITIVE_INFINITY;
        for ( int i = 0; i < 6; i++ ) dist[i] = -1;
        Vector3d v1 = new Vector3d(lower);
        Vector3d v2 = new Vector3d(v1);
        v2.add(size);
        if (d.x != 0) 
        {
            double rc = 1.0f / d.x;
            dist[0] = (v1.x - o.x) * rc;
            dist[3] = (v2.x - o.x) * rc;
        }
        if (d.y != 0) 
        {
            double rc = 1.0f / d.y;
            dist[1] = (v1.y - o.y) * rc;
            dist[4] = (v2.y - o.y) * rc;
        }
        if (d.z != 0) 
        {
            double rc = 1.0f / d.z;
            dist[2] = (v1.z - o.z) * rc;
            dist[5] = (v2.z - o.z) * rc;
        }
        for ( int i = 0; i < 6; i++ ) if (dist[i] > 0)
        {
            //ip[i] = o + dist[i] * d;
            Vector3d tmp = new Vector3d(d);
            tmp.scale(dist[i]);
            tmp.add(o);
            ip[i] = tmp;
            if ((ip[i].x > (v1.x - Scene.EPSILON)) && (ip[i].x < (v2.x + Scene.EPSILON)) && 
                (ip[i].y > (v1.y - Scene.EPSILON)) && (ip[i].y < (v2.y + Scene.EPSILON)) &&
                (ip[i].z > (v1.z - Scene.EPSILON)) && (ip[i].z < (v2.z + Scene.EPSILON)))
            {
                if (dist[i] < t) 
                {
                    t = dist[i];    
                }
            }
        }
        return t;
       
    }
    
    /**
     * @param cell The cell which will be checked on intersection with this bounding box.
     * @return <code>true</code> if this box intersects the given cell
     * of the regular grid or <code>false</code> if not.
     */
    public boolean intersectsCell(Cell cell) {
        
        if( upper.x >= cell.lower.x && cell.upper.x >= lower.x &&
                upper.y >= cell.lower.y && cell.upper.y >= lower.y &&
                upper.z >= cell.lower.z && cell.upper.z >= lower.z ) {
                return true;
        } else {
            return false;
        }
    }
    
    /**
     * @param pointOfIntersection
     * @return the surface normal at the point of intersection.
     */
    public Vector3d getSurfaceNormal(Point3d pointOfIntersection) {
        
        if(pointOfIntersection.x < lower.x + Scene.EPSILON && 
                pointOfIntersection.x > lower.x - Scene.EPSILON) {
            return new Vector3d(-1.0,0.0,0.0);
        }
        if(pointOfIntersection.x < upper.x + Scene.EPSILON && 
                pointOfIntersection.x > upper.x - Scene.EPSILON) {
            return new Vector3d(1.0,0.0,0.0);
        }
        
        if(pointOfIntersection.y < lower.y + Scene.EPSILON && 
                pointOfIntersection.y > lower.y - Scene.EPSILON) {
            return new Vector3d(0.0,-1.0,0.0);
        }
        if(pointOfIntersection.y < upper.y + Scene.EPSILON && 
                pointOfIntersection.y > upper.y - Scene.EPSILON) {
            return new Vector3d(0.0,1.0,0.0);
        }
        
        if(pointOfIntersection.z < lower.z + Scene.EPSILON && 
                pointOfIntersection.z > lower.z - Scene.EPSILON) {
            return new Vector3d(0.0,0.0,-1.0);
        }
        else {  //if(pointOfIntersection.z < upper.z + Scene.EPSILON && 
                //pointOfIntersection.z > upper.z - Scene.EPSILON)
            return new Vector3d(0.0,0.0,1.0);
        }  
    }

    /**
     * @return a bounding box which contains this box.
     */
    BoundingVolume computeBounds() {
        return new BoundingBox(lower, size);
    }

    /**
     * @return Returns the lower point.
     */
    public Point3d getLower() {
        return lower;
    }

    /**
     * @return Returns the size vector.
     */
    public Vector3d getSize() {
        return size;
    }

    /**
     * @return Returns the upper point.
     */
    public Point3d getUpper() {
        return upper;
    }
}
