/*
 * Shader.java 
 * 05/12/01
 * 
 * University of Applied Sciences
 * Bingen, Germany
 * 
 * FPro Project "Jaytracer"
 * 
 * Authors:
 * P L
 *  
 */
package de.fhbingen.fpro.jaytracer;

import java.util.List;

import javax.vecmath.Color3f;
import javax.vecmath.Vector3d;

/**
 * This class provides methods to calculate the color at a specific intersection point.
 * @author P L
 */
public class Shader {
	
	/** The scene, which provides information for calculating the colors */
	private Scene scene;
	
    /** A list of all relevant lights */
    private List<Light> rtLights;
    
	/** The intersection for that the colors a calculated */
	private Intersection intersection;
	
	/** The sceneObject to that the intersection belongs */
	private SceneObject sceneObject;
    
    /**
     * Creates a shader object for the specified intersection and the given
     * scene information.
     * @param scene The raytracer scene.
     * @param intersection The intersection.
     */
	public Shader(Scene scene, Intersection intersection) {
		this.scene = scene;
		this.intersection = intersection;
		sceneObject = intersection.getIntersectedSceneObject();
        rtLights = scene.getHittingLights(intersection);
        for(Light light : rtLights) {
            light.setAttenuation(intersection.getPointOfIntersection());
        }   
	}
	
    /**
     * @return The color at the specified intersection.
     */
	public Color3f getColor() {
        
        float reflectionCoefficient = sceneObject.getMaterial().getReflectionCoefficient();
        float transparency = sceneObject.getMaterial().getTransparency();
        Color3f ambientColor;
        Color3f diffuseColor;
        Color3f specularColor;
        
        ambientColor = calcAmbientColor();
        diffuseColor = calcDiffuseColor();
        specularColor = calcSpecularColor();
        
        diffuseColor.scale(1 - reflectionCoefficient);
        diffuseColor.scale(1 - transparency);
        
        ambientColor.add(diffuseColor);
        
        ambientColor.add(specularColor);
        
        return ambientColor;
    }
    
    /**
     * @return the ambient color.
     */
    private Color3f calcAmbientColor() {
        return VecmathAddon.mulColor3f(scene.getAmbientLight(), sceneObject.getMaterial().getAmbient());
    }
    
	/**
	 * @return the diffuse color.
	 */
    private Color3f calcDiffuseColor() {
    	Color3f color = new Color3f(0.0f,0.0f,0.0f);
    	Vector3d Nsurface;
    	Vector3d Nlight;
    	Color3f Msurface;
    	Color3f Mlight;
    	
    	float lambda;
    	Nsurface = intersection.getSurfaceNormal();
    	Msurface = sceneObject.getMaterial().getDiffuse();
    	Color3f lColor;
    	
    	for (Light light : rtLights) {
    		Nlight = intersection.getLightNormal(light);
    		lambda = (float) Nsurface.dot(Nlight);
    		if (lambda <= 0.0f) continue;
    		Mlight = light.getDiffuse();
    		lColor = VecmathAddon.mulColor3f(Mlight, Msurface);
    		lColor.scale(lambda);
            lColor.scale(light.getAttenuation());
            color.add(lColor);  
    	}
    	return color;
	}
    
    /**
     * @return the specular color.
     */
    private Color3f calcSpecularColor() {
    	sceneObject = intersection.getIntersectedSceneObject();
    	Color3f color = new Color3f(0.0f,0.0f,0.0f);
    	
    	Vector3d Nsurface;
    	Vector3d Vlight;
    	Vector3d Vreflect;
    	
    	Color3f Msurface;
    	Color3f Mlight;
        Color3f lColor;
        
    	Vector3d Vviewpoint = new Vector3d(intersection.getXRay().getDirection());
    	Vviewpoint.negate(); //vector in viewpoint direction = opposite ray direction
    	Nsurface = intersection.getSurfaceNormal();
    	Msurface = sceneObject.getMaterial().getSpecular();
    	
    	for (Light light : rtLights) {
    		Vlight = intersection.getLightNormal(light);
    		Vreflect = new Vector3d(Nsurface);
    		Vreflect.scale(2*Vlight.dot(Nsurface));
    		Vreflect.sub(Vlight);
    		
            double specFactor =  Vreflect.dot(Vviewpoint);
            if(specFactor <= 0) {
                //angle between viewpoint vector and reflection vector is greater or equal 90 degree
                //continue with the next light
                continue; 
            }
  
    		//System.out.println(Nlight +"--" +Nsurface +"--" +Vreflect +" alp:" +alpha);
    		
    		Mlight = light.getSpecular();
    		lColor = VecmathAddon.mulColor3f(Mlight, Msurface);
    		
            lColor.scale((float)Math.pow(specFactor, sceneObject.getMaterial().getShininess()));
            lColor.scale(light.getAttenuation());
    		color.add(lColor);
    	}     
    	return color;
	}
}
