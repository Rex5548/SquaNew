/*
 * Light.java 
 * 05/10/29
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

import javax.vecmath.Color3f;
import javax.vecmath.Point3d;

/**
 * This class represents a simple light model. The light has a position in the space,
 * several light properties like ambient, diffuse and specular color, etc.
 * The light has also a normal vector, which is used by the shading model.
 * @author M S
 */
public class Light {

    /** Default light position */
    public final static Point3d DEFAULT_POSITION = new Point3d(0.0,0.0,0.0);
    
    /** Default ambient color */
    public final static Color3f DEFAULT_AMBIENT = new Color3f(0.0f,0.0f,0.0f);
    
    /** Default diffuse color */
    public final static Color3f DEFAULT_DIFFUSE = new Color3f(0.0f,0.0f,0.0f);
    
    /** Default specular color */
    public final static Color3f DEFAULT_SPECULAR = new Color3f(0.0f,0.0f,0.0f);
    
    /** Default light intendity */
    public final static float DEFAULT_INTENSITY = 1f;
    
    /** The light's position */
    private Point3d position;
    
    /** The light's ambient color */
    private Color3f ambient;
    
    /** The light's diffuse color */
    private Color3f diffuse;
    
    /** The light's specular color */
    private Color3f specular;
    
    /** The light's intensity */
    private float intensity = 1;
    
    /** The light's attenuation factor */
    private float attenuation;
    
    /**
     * Creates a light at default postition and default properties.
     */
    public Light() {
        this.position = Light.DEFAULT_POSITION;
        this.ambient = Light.DEFAULT_AMBIENT;
        this.diffuse = Light.DEFAULT_DIFFUSE;
        this.specular = Light.DEFAULT_SPECULAR;
        this.intensity = Light.DEFAULT_INTENSITY;
    }

    /**
     * Creates a light at the given postition and with the specified intensity.
     * @param position The light's position.
     */
    public Light(Point3d position) {
        this(); //default constructor call
        this.position = new Point3d(position);
    }

    /**
     * Sets the light's position.
     * @param position The new position.
     */
    public void setPosition(Point3d position) {
        this.position = position;
    }

    /**
     * @return the light's actual position.
     */
    public Point3d getPosition() {
        return position;
    }

    /**
     * @return the light's ambient color.
     */
	public Color3f getAmbient() {
		return ambient;
	}

	/**
     * Sets the light's ambient color.
	 * @param ambient The ambient color.
	 */
	public void setAmbient(Color3f ambient) {
		this.ambient = ambient;
	}

	/**
	 * @return the light's diffuse color.
	 */
	public Color3f getDiffuse() {
		return diffuse;
	}

	/**
	 * Sets the light's diffuse color.
	 * @param diffuse The diffuse color.
	 */
	public void setDiffuse(Color3f diffuse) {
		this.diffuse = diffuse;
	}

	/**
	 * @return the light's specular color.
	 */
	public Color3f getSpecular() {
		return specular;
	}

	/**
	 * Sets the light's specular color.
	 * @param specular The specular color.
	 */
	public void setSpecular(Color3f specular) {
		this.specular = specular;
	}

    /**
     * @return the intensity.
     */
    public float getIntensity() {
        return intensity;
    }

    /**
     * @param intensity The intensity to set.
     */
    public void setIntensity(float intensity) {
        this.intensity = intensity;
    }
    
    /**
     * Computes the light's attenuation according to the distance to the given point. The light
     * attenuation is linear.
     * @param pointOfIntersection The point.
     */
    public void setAttenuation(Point3d pointOfIntersection) {
        attenuation = intensity / (float) position.distance(pointOfIntersection);
    }

    /**
     * @return the light attenuation.
     */
    public float getAttenuation() {
        return attenuation;
    }
}
