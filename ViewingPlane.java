/*
 * ViewingPlane.java 
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
 */

package de.fhbingen.fpro.jaytracer;

import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

/**
 * The viewing plane, that represents the plane to that the 3D scene is projected.
 * @author M S
 */
public class ViewingPlane {
    
    /** The upper left point of the viewing plane */
    private Point3d upperLeftPoint;
    
    /** The width vector of the viewing plane */
    private Vector3d widthVector;
    
    /** The height vector of the viewing plane  */
    private Vector3d heightVector;
    
    /** The width of the viewing plane */
    private double width;
    
    /** The height of the viewing plane */
    private double height;
    
    public ViewingPlane(Point3d p, Vector3d wVec, Vector3d hVec, double w, double h) {
        this.upperLeftPoint = p;
        this.widthVector = wVec;
        this.heightVector = hVec;
        this.height = h;
        this.width = w;
    }
    
    /**
     * 
     * @param heightVector The heightVector to set.
     */
    public void setHeightVector(Vector3d heightVector) {
        this.heightVector = heightVector;
        this.heightVector.normalize();
    }

    /**
     * 
     * @param widthVector The widthVector to set.
     */
    public void setWidthVector(Vector3d widthVector) {
        this.widthVector = widthVector;
        this.widthVector.normalize();
    }

	/**
	 * @return Returns the height.
	 */
	public double getHeight() {
		return height;
	}

	/**
	 * @param height The height to set.
	 */
	public void setHeight(double height) {
		this.height = height;
	}

	/**
	 * @return Returns the upperLeftPoint.
	 */
	public Point3d getUpperLeftPoint() {
		return upperLeftPoint;
	}

	/**
	 * @param upperLeftPoint The upperLeftPoint to set.
	 */
	public void setUpperLeftPoint(Point3d upperLeftPoint) {
		this.upperLeftPoint = upperLeftPoint;
	}

	/**
	 * @return Returns the width.
	 */
	public double getWidth() {
		return width;
	}

	/**
	 * @param width The width to set.
	 */
	public void setWidth(double width) {
		this.width = width;
	}

	/**
	 * @return Returns the heightVector.
	 */
	public Vector3d getHeightVector() {
		return heightVector;
	}

	/**
	 * @return Returns the widthVector.
	 */
	public Vector3d getWidthVector() {
		return widthVector;
	}
    
}
