/*
 * Material.java 
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

/**
 * Container for material properties. The material properties are:
 * <ul>
 *   <li>ambient color</li>
 *   <li>diffuse color</li>
 *   <li>specular color</li>
 *   <li>shininess exponent</li>
 *   <li>emission color</li>
 *   <li>transparency</li>
 *   <li>refraction index</li>
 * </ul>
 * 
 * @author M S
 */
public class Material {

    /** Default material transparency */
    public static final float DEFAULT_TRANSPARENCY = 0.0f;
    
    /** Default material reflectionCoefficient */
    public static final float DEFAULT_REFLECTION_COEFFICIENT = 0.0f;

    /** Default material ambient color */
    public static final Color3f DEFAULT_AMBIENT = new Color3f(0.0f,0.0f,0.0f);

    /** Default material diffuse color */
    public static final Color3f DEFAULT_DIFFUSE = new Color3f(0.0f,0.0f,0.0f);

    /** Default material specular color */
    public static final Color3f DEFAULT_SPECULAR = new Color3f(0.0f,0.0f,0.0f);
    
    /** Default material emission color */
    public static final Color3f DEFAULT_EMISSION = new Color3f(0.0f,0.0f,0.0f);

    /** Default material shininess exponent */
    public static final float DEFAULT_SHININESS = 50.0f;

    /** Default material refraction index */
    public static final float DEFAULT_REFRACTION_INDEX = 1.0f;

    /** The material's transparency */
    private float transparency;

    /** The material's ambient color */
    private Color3f ambient;

    /** The material's diffuse color */
    private Color3f diffuse;

    /** The material's specular color */
    private Color3f specular;
    
    /** The material's emission color */
    private Color3f emission;
    
    /** Factor of the specular's brilliance */
    private float shininess;

    /** Index of refraction */
    private float refractionIndex;
    
    /** Coefficient of reflection */
    private float reflectionCoefficient;

    /**
     * Creates a material property container with default values.
     */
    public Material() {
        transparency = Material.DEFAULT_TRANSPARENCY;
        ambient = Material.DEFAULT_AMBIENT;
        diffuse = Material.DEFAULT_DIFFUSE;
        specular = Material.DEFAULT_SPECULAR;
        shininess = Material.DEFAULT_SHININESS;
        emission = Material.DEFAULT_EMISSION;
        refractionIndex = Material.DEFAULT_REFRACTION_INDEX;
        reflectionCoefficient = Material.DEFAULT_REFLECTION_COEFFICIENT;
    }

    /**
     * @return the material's ambient color.
     */
	public Color3f getAmbient() {
		return ambient;
	}

	/**
	 * Sets the material's ambient color.
	 * @param ambient
	 */
	public void setAmbient(Color3f ambient) {
		this.ambient = ambient;
	}

	/**
	 * @return the material's diffuse color.
	 */
	public Color3f getDiffuse() {
		return diffuse;
	}

	/**
	 * Sets the material's diffuse color.
	 * @param diffuse
	 */
	public void setDiffuse(Color3f diffuse) {
		this.diffuse = diffuse;
	}

	/**
	 * @return the material's emission color.
	 */
	public Color3f getEmission() {
		return emission;
	}

	/**
	 * Sets the material's emission color.
	 * @param emission
	 */
	public void setEmission(Color3f emission) {
		this.emission = emission;
	}

	/**
	 * @return the material's refraction index.
	 */
	public float getRefractionIndex() {
		return refractionIndex;
	}

	/**
	 * Sets the material's refraction index.
	 * @param refractionIndex
	 */
	public void setRefractionIndex(float refractionIndex) {
		this.refractionIndex = refractionIndex;
	}

	/**
	 * @return The material's shininess exponent.
	 */
	public float getShininess() {
		return shininess;
	}

	/**
	 * Sets the material's shininess exponent.
	 * @param shininess
	 */
	public void setShininess(float shininess) {
		this.shininess = shininess;
	}

	/**
	 * @return the material's specular color.
	 */
	public Color3f getSpecular() {
		return specular;
	}

	/**
	 * Sets the material's specular color.
	 * @param specular
	 */
	public void setSpecular(Color3f specular) {
		this.specular = specular;
	}

	/**
	 * @return The material's transparency.
	 */
	public float getTransparency() {
		return transparency;
	}

	/**
     * Sets the material's transparency.
	 * @param transparency
	 */
	public void setTransparency(float transparency) {
		this.transparency = transparency;
	}

	/**
	 * @return Returns the reflectionCoefficient.
	 */
	public float getReflectionCoefficient() {
		return reflectionCoefficient;
	}

	/**
	 * @param reflectionCoefficient The reflectionCoefficient to set.
	 */
	public void setReflectionCoefficient(float reflectionCoefficient) {
		this.reflectionCoefficient = reflectionCoefficient;
	}

    
}
