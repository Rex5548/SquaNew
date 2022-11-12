/*
 * Camera.java 
 * 05/10/30
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
 * This class represents a simple camera model. The camera is definded
 * by a point in space and two vectors, the up vector and the direction
 * of viewing.
 * 
 * @author M S
 */
public class Camera {

    /** The viewing Plane, trough which the rays are shot */
    private ViewingPlane viewingPlane;
    
    /** The camera's position */
    private Point3d position;
    
    /** The camera's up vector */
    private Vector3d up;
    
    /** The camera's direction of view */
    private Vector3d direction;
    
    /** Vertical field of view */
    private double vFov;
    
    /** Aspect ratio: width:height */
    private double aspect;
    
    /** The distance between the camera and the viewing plane */
    private double viewingPlaneDistance;
 
    /**
     * Creates a camera with the given parameters and computes the viewingplane.
     * @param pos The camera's position.
     * @param dir The camera's direction.
     * @param up The camera's up vector.
     * @param vFov The vertical field of view. (in degrees)
     * @param aspect The width-height aspect ratio.
     * @param distance The distance to the viewing plane.
     */
    public Camera(Point3d pos, Vector3d dir, Vector3d up, double vFov, double aspect, double distance) {
        this.position = pos;
        this.up = up;
        this.direction = dir;
        this.aspect = aspect;
        this.vFov = vFov * Math.PI/180;
        this.viewingPlaneDistance = distance;
        this.up.normalize();
        this.direction.normalize();
        this.updateViewingPlane();
    }

    /**
     * @return the camera's viewing direction.
     */
    public Vector3d getDirection() {
        return direction;
    }

    /**
     * Sets the camera's viewing direction.
     * @param direction
     */
    public void setDirection(Vector3d direction) {
        this.direction = direction;
        this.direction.normalize();
    }

    /**
     * @return the camera's position.
     */
    public Point3d getPosition() {
        return position;
    }

    /**
     * Sets the camera's position.
     * @param position
     */
    public void setPosition(Point3d position) {
        this.position = position;
    }

    /**
     * @return the camera's up vector.
     */
    public Vector3d getUp() {
        return up;
    }

    /**
     * Sets the camera's up vector.
     * @param up
     */
    public void setUp(Vector3d up) {
        this.up = up;
        this.up.normalize();
    }

	/**
	 * @return the viewingPlane.
	 */
	public ViewingPlane getViewingPlane() {
		return viewingPlane;
	}

	/**
	 * Updates the viewing plane and configures it according to the camera's settings.
	 */
    public void updateViewingPlane() {
        
        double width = 0;
        double height = 0;
        Point3d upperLeftPoint;
        Vector3d widthVector;
        Vector3d heightVector;
        Vector3d heightLot;
        Vector3d widthLot;
        
        //calculate the height and the width of the viewing plane
        height = 2 * viewingPlaneDistance * Math.tan(vFov/2);
        width = height * aspect;
        
        //calculate the width- and heightvector of the viewing plane
        heightVector = new Vector3d(up);
        heightVector.negate();
        widthVector = new Vector3d();
        widthVector.cross(direction, up);
        
        //calculate the upper left point of the viewing plane
        upperLeftPoint = new Point3d(this.direction);
        upperLeftPoint.scale(viewingPlaneDistance);
        upperLeftPoint.add(this.position);
        
        heightLot = new Vector3d(heightVector);
        widthLot = new Vector3d(widthVector);
        heightLot.negate();
        widthLot.negate();
        heightLot.scale(height/2);
        widthLot.scale(width/2);
        upperLeftPoint.add(heightLot);
        upperLeftPoint.add(widthLot);
        
        System.out.println("generated viewingplane:");
        System.out.println("point: "+upperLeftPoint);
        System.out.println("widthVec: "+widthVector);
        System.out.println("heightVec: "+heightVector);  
        System.out.println("width: "+width);
        System.out.println("height: "+height);   
       
        //instantiate a new viewing plane
        this.viewingPlane = new ViewingPlane(upperLeftPoint, widthVector, heightVector, width, height);  
    }

    /**
     * @return the vFov.
     */
    public double getVFov() {
        return vFov;
    }

    /**
     * @param fov The vFov to set. (in degrees)
     */
    public void setVFov(double fov) {
        vFov = fov * Math.PI/180;
    }

    /**
     * @return the viewingPlaneDistance.
     */
    public double getViewingPlaneDistance() {
        return viewingPlaneDistance;
    }

    /**
     * @param viewingPlaneDistance The viewingPlaneDistance to set.
     */
    public void setViewingPlaneDistance(double viewingPlaneDistance) {
        this.viewingPlaneDistance = viewingPlaneDistance;   
    }

    /**
     * @return the aspect.
     */
    public double getAspect() {
        return aspect;
    }

    /**
     * @param aspect The aspect to set.
     */
    public void setAspect(double aspect) {
        this.aspect = aspect;  
    }

}
