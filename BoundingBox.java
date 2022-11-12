/*
 * BoundingBox.java 
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
 * 
 */

package de.fhbingen.fpro.jaytracer;

import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

/**
 * Bounding Volume: Axis aligned bounding box (AABB)
 * @author M S
 */
public class BoundingBox extends Box implements BoundingVolume{

    /** If bounding box is infinite */
    private boolean boundingBoxIsInfinite;
    
    /** If bounding box is infinitesimal */
    private boolean boundingBoxIsInfinitesimal;
    
    /**
     * Creates a axis aligned bounding box.
     * @param lower The box's lower point.
     * @param size The box's size vector.
     */
    public BoundingBox(Point3d lower, Vector3d size) {
        this.lower = lower;
        this.size = size;
        this.upper = new Point3d();
        this.upper.add(lower, size);
        
        if((lower.x == Double.NEGATIVE_INFINITY) &&
               (lower.y == Double.NEGATIVE_INFINITY) &&
               (lower.z == Double.NEGATIVE_INFINITY) &&
               (upper.x == Double.POSITIVE_INFINITY) &&
               (upper.y == Double.POSITIVE_INFINITY) &&
               (upper.z == Double.POSITIVE_INFINITY)) {
            boundingBoxIsInfinite = true;
            boundingBoxIsInfinitesimal = false;
                
        } else if (Double.isNaN(lower.x+lower.y+lower.z+upper.x+upper.y+upper.z)) {
            boundingBoxIsInfinite = false;
            boundingBoxIsInfinitesimal = true;
        } 
    }

    /**
     * @param point
     * @return <code>true</code> if the given point is contained by this bounding box, else <code>false</code>.
     */    
    public boolean containsPoint(Point3d point) {
        return ((point.x > (lower.x - Scene.EPSILON)) && (point.x < (upper.x + Scene.EPSILON)) &&
                (point.y > (lower.y - Scene.EPSILON)) && (point.y < (upper.y + Scene.EPSILON)) &&
                (point.z > (lower.z - Scene.EPSILON)) && (point.z < (upper.z + Scene.EPSILON)));
    }

    /**
     * @param boundingVolume
     * @return <code>true</code>, if this bounding box contains the given bounging volume, else <code>false</code>.
     */
    public boolean containsBoundingVolume(BoundingVolume boundingVolume) {
        
        if(boundingVolume == null) {
            return false;
        }
        
        if(this.isInfinitesimal() || boundingVolume.isInfinitesimal()) {
            return false;
        }
        
        if(this.isInfinite() || boundingVolume.isInfinite()) {
            return false;
        }
        
        if(boundingVolume instanceof BoundingBox) {
            BoundingBox box = (BoundingBox) boundingVolume;
            if(containsPoint(box.lower) && containsPoint(box.upper)) {
                return true;
            } else {
                return false;
            }
        } else {
            throw new IllegalArgumentException("Unknown BoundingVolume type");
        }
    }
    
    /**
     * @param boundingVolume The bounding volume which will be checked on intersection with this bounding box.
     * @return <code>true</code> if this box intersects the given cell
     * of the regular grid or <code>false</code> if not.
     */
    public boolean intersectsBoundingVolume(BoundingVolume boundingVolume) {
        
        if(boundingVolume == null) {
            return false;
        }
        
        if(this.isInfinitesimal() || boundingVolume.isInfinitesimal()) {
            return false;
        }
        
        if(this.isInfinite() || boundingVolume.isInfinite()) {
            return true;
        }
        
        if(boundingVolume instanceof BoundingBox) {
            BoundingBox box = (BoundingBox) boundingVolume; 
            if( upper.x >= box.lower.x && box.upper.x >= lower.x &&
                    upper.y >= box.lower.y && box.upper.y >= lower.y &&
                    upper.z >= box.lower.z && box.upper.z >= lower.z ) {
                    return true;
            } else {
                return false;
            }
        } else {
            throw new IllegalArgumentException("Unknown BoundingVolume type");
        }
    }

    /**
     * @return <code>true</code>, if the bounding volumes size is infinite.
     */
    public boolean isInfinite() {
        return boundingBoxIsInfinite;
    }

    /**
     * @return <code>true</code>, if the bounding volumes size inifinitesimal.
     */
    public boolean isInfinitesimal() {
        return boundingBoxIsInfinitesimal;
    }
}
