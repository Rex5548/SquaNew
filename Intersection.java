/*
 * Intersection.java 
 * 05/11/16
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
 * An intersection is a container for alle needed information for
 * an intersection of a scene object with a ray.
 * @author M S
 *
 */
public class Intersection {
	
	/** The intersected scene object */
	private SceneObject intersectedSceneObject;
	
	/** The t parameter */
	private double t;
	
	/** The point of intersection */
	private Point3d pointOfIntersection;
	
	/** The surface normal at the intersection point */
	private Vector3d surfaceNormal;
	
	/** The ray that caused the intersection */
	private XRay xRay;
    
   
	/**
	 * Creates an intersection of the given parameters.
	 * @param sceneObject
	 * @param t
	 * @param ray
	 */
	public Intersection(SceneObject sceneObject, double t, XRay ray) {
		this.xRay = ray;
        this.t = t;
		this.pointOfIntersection = this.calculatePointOfIntersection(ray, t);
		this.surfaceNormal = sceneObject.getSurfaceNormal(this.pointOfIntersection);
		this.intersectedSceneObject = sceneObject;
       
	}
	
	/**
     * Calculates and sets the point of intersection with the given ray and the former
     * calculated parameter t, that tells where on the ray the intersection lies.
     * @param ray
     * @param t
     */
    private Point3d calculatePointOfIntersection(XRay ray, double t) {
     	Vector3d dir = new Vector3d(ray.getDirection());
    	Point3d poi = new Point3d(ray.getOrigin());
    	dir.scale(t);
    	poi.add(dir);
    	return poi;
    }

	/**
	 * @return Returns the intersectedSceneObject.
	 */
	public SceneObject getIntersectedSceneObject() {
		return intersectedSceneObject;
	}

	/**
	 * @return Returns the pointOfIntersection.
	 */
	public Point3d getPointOfIntersection() {
		return pointOfIntersection;
	}

	/**
	 * @return Returns the surfaceNormal.
	 */
	public Vector3d getSurfaceNormal() {
		return surfaceNormal;
	}

	/**
	 * @return Returns the t.
	 */
	public double getT() {
		return t;
	}
    
    /**
     * @param l
     * @return a normalized vector which points from the point of intersection
     * to the given lightsource.
     */
    public Vector3d getLightNormal(Light l) {
        Vector3d lightNormal = new Vector3d(l.getPosition());
        lightNormal.sub(this.pointOfIntersection);
        lightNormal.normalize();
        return lightNormal;
    }

	/**
	 * @return Returns the xray.
	 */
	public XRay getXRay() {
		return xRay;
	}
}
