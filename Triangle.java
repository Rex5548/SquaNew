/*
 * Triangle.java 
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

import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

/**
 * SceneObject: Triangle
 * 
 * A triangle is bordered plane, defined by 3 points in space.
 * @author M S
 *
 */
public class Triangle extends SceneObject{
    
    /** First point of the triangle */
    private Point3d vertex1;
    
    /** Second point of the triangle*/
    private Point3d vertex2;
    
    /** Third point of the triangle */
    private Point3d vertex3;
    
    private Vector3d vector21;
    private Vector3d vector31;
    
    private Vector3d normal;
    /**
     * Constructs a triangle with the given 3 points. Note that
     * the surface normal of the triangle depends on the order
     * in that the point are submitted.
     * @param v1
     * @param v2
     * @param v3
     */
    public Triangle(Point3d v1, Point3d v2, Point3d v3) {
        this.vertex1 = v1;
        this.vertex2 = v2;
        this.vertex3 = v3;
        
        vector21 = new Vector3d();
        vector31 = new Vector3d();
        vector21.sub(vertex2, vertex1);
        vector31.sub(vertex3, vertex1);
        
        normal = new Vector3d();
        normal.cross(vector21, vector31);
        normal.normalize();
        
        bounds = computeBounds();
    }
    
    /**
     * Calculates the intersection point with a given ray and this
     * triangle. If there is an intersection point, the return value is the
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
    public double intersect( XRay ray) {
        
        Vector3d pvec = new Vector3d();
        Vector3d tvec = new Vector3d();
        Vector3d qvec = new Vector3d();
  
        pvec.cross(ray.getDirection(), vector31);
  
        double det = vector21.dot(pvec);
        
        if(det > -Scene.EPSILON && det < Scene.EPSILON) {
            return 0.0;
        }
  
        double inv_det = 1 / det;
  
        tvec.sub(ray.getOrigin(), vertex1);
  
        double u = tvec.dot(pvec) * inv_det;
  
        if(u < 0 || u > 1) {
            return 0.0;
        }
  
        qvec.cross(tvec, vector21);
        double v = ray.getDirection().dot(qvec) * inv_det;
  
        if(v < 0 || u + v > 1+Scene.EPSILON) {
            return 0.0;
        }
  
        double t = vector31.dot(qvec) * inv_det;
  
        return t;
    }
    
    /**
     * @return the bounding box that contains the triangle.
     */
    BoundingVolume computeBounds() {
        BoundingBox boundingBox;
        Point3d pos = new Point3d(vertex1);
        if(vertex2.x < pos.x) pos.x = vertex2.x;
        if(vertex3.x < pos.x) pos.x = vertex3.x;
        if(vertex2.y < pos.y) pos.y = vertex2.y;
        if(vertex3.y < pos.y) pos.y = vertex3.y;
        if(vertex2.z < pos.z) pos.z = vertex2.z;
        if(vertex3.z < pos.z) pos.z = vertex3.z;
        
        Vector3d sizeVector = new Vector3d(vertex1);
        if(vertex2.x > sizeVector.x) sizeVector.x = vertex2.x;
        if(vertex3.x > sizeVector.x) sizeVector.x = vertex3.x;
        if(vertex2.y > sizeVector.y) sizeVector.y = vertex2.y;
        if(vertex3.y > sizeVector.y) sizeVector.y = vertex3.y;
        if(vertex2.z > sizeVector.z) sizeVector.z = vertex2.z;
        if(vertex3.z > sizeVector.z) sizeVector.z = vertex3.z;
        
        sizeVector.sub(pos);
        
        boundingBox = new BoundingBox(pos, sizeVector);
        return boundingBox;
    }

    /**
     * @param pointOfIntersection The point for that the normal will be computed.
     * @return the surface normal. (depends on the vertex order)
     */
    public Vector3d getSurfaceNormal(Point3d pointOfIntersection) {
        return normal;
    }

    /**
     * @param cell The cell which will be checked on intersection with this scene object.
     * @return <code>true</code> if this scene object intersects the given cell
     * of the regular grid or <code>false</code> if not.
     */
    public boolean intersectsCell(Cell cell) { 
        //create rays from vertex to vertex and test if they intersect the cell
        
        //1st ray
        Point3d origin = new Point3d(vertex1);
        Vector3d direction = new Vector3d();
        direction.sub(vertex2, vertex1);
        XRay ray = new XRay(origin, direction, null);
        
        if(cell.intersect(ray) > Scene.EPSILON) {
            return true;
        }
        
        //2nd ray
        origin = new Point3d(vertex2);
        direction = new Vector3d();
        direction.sub(vertex3, vertex2);
        
        if(cell.intersect(ray) > Scene.EPSILON) {
            return true;
        }
        
        //3rd ray
        origin = new Point3d(vertex3);
        direction = new Vector3d();
        direction.sub(vertex1, vertex3);
        
        if(cell.intersect(ray) > Scene.EPSILON) {
            return true;
        }
        
        return false;
    }
}
