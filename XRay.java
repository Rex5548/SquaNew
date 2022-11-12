/*
 * XRay.java 
 * 05/10/30
 * 
 * University of Applied Sciences
 * Bingen, Germany
 * 
 * FPro Project "Jaytracer"
 * 
 * Author:
 * P L
 * 
 */
package de.fhbingen.fpro.jaytracer;

import java.util.Stack;
import javax.vecmath.*;

/**
 * This class represents a Ray, that is used to find the
 * intersections with the scene object.
 * 
 * @author P L
 */
public class XRay {
    
    /** The ray's origin */
    private Point3d origin;
    
    /** The ray's direction */
    private Vector3d direction;
    
    /** The tracing scene */
    private Scene rtScene;
    
    /** The current intersected scenebject */
    private SceneObject rtSceneObject;
    
    /** The current intersection object */
    private Intersection rtIntersection;
    
    /** Refractionindices: top is the current medium, next is the surrounding one, and so one...*/
    private Stack<Float> refractionIndices;
    
    /**
     * Creates a new Ray.
     * @param origin The ray's origin.
     * @param direction The ray's direction.
     * @param rtScene The tracing scene.
     */
    public XRay(Point3d origin, Vector3d direction, Scene rtScene) {
        this.origin = origin;
        direction.normalize();
        this.direction = direction;
        this.rtScene = rtScene;
        refractionIndices = new Stack<Float>();
        //default medium: vacuum
        refractionIndices.push(1.0f);
    }
    
    /**
     * Only used when rays are reflected or refracted.
     * @param origin The ray's origin.
     * @param direction The ray's direction.
     * @param rtScene The tracing scene.
     * @param refractionIndices The stack of encountered media.
     */
    private XRay(Point3d origin, Vector3d direction, Scene rtScene, Stack<Float> refractionIndices) {
        this.origin = origin;
        direction.normalize();
        this.direction = direction;
        this.rtScene = rtScene;
        this.refractionIndices = refractionIndices;
    }
   
    /**
     * Traces the ray through the scene and find the nearest intersection. It also traces the reflections and
     * the refractions of this ray recursively. A shader object is used to calculate the colors at the intersection
     * point with the scene objects.
     * @param ttl The recursive depth.
     * @return the color that this ray tracing has resulted. If the ray hits no object, the background color is returned.
     */
    public Color3f recursiveTrace(int ttl) {
    	Color3f color = new Color3f();
    	Color3f reflectedColor = new Color3f();
    	Color3f refractedColor = new Color3f();
        Shader shader;
    	
    	rtIntersection = rtScene.getNearestIntersection(this);
    	
    	if (rtIntersection != null) {
    		this.rtSceneObject = rtIntersection.getIntersectedSceneObject();
            shader = new Shader(rtScene, rtIntersection);
            color = shader.getColor();
    		
    		if(ttl > 0) {
    			//reflection
                if(rtSceneObject.getMaterial().getReflectionCoefficient() > 0.0) {
    				float reflectionCoefficient = (float) rtSceneObject.getMaterial().getReflectionCoefficient();
                    XRay reflectedRay = generateReflectedRay();	
                    reflectedColor = reflectedRay.recursiveTrace(ttl-1);    	
                    reflectedColor.scale(reflectionCoefficient);
                    Color3f diffusePart = new Color3f(rtSceneObject.getMaterial().getDiffuse());
                    diffusePart.scale(reflectionCoefficient);
                    color.add(diffusePart);
                    color.add(reflectedColor);
    	    	}
    			//refraction
    			if(rtSceneObject.getMaterial().getTransparency() > 0.0) {
    				float transparency = (float) rtSceneObject.getMaterial().getTransparency();
                    XRay refractedRay = generateRefractedRay();
    	    		refractedColor = refractedRay.recursiveTrace(ttl-1);    	
                    refractedColor.scale(transparency);
                    Color3f diffusePart = new Color3f(rtSceneObject.getMaterial().getDiffuse());
                    diffusePart.scale(transparency);
                    color.add(diffusePart);
                    color.add(refractedColor);
    			}
    		}
            
    		color.clamp(0.0f, 1.0f);
            return color;
    	}
    	
    	else {
    		return new Color3f( rtScene.getBackgroundColor());
    	}  	
    }
    
    /**
     * Creates the reflected ray.
     * @return the reflected ray.
     */
    private XRay generateReflectedRay() {
        // r = i - 2(n*i)n   n=surfacenormal   i=incoming direction
        Point3d reflectedRayPos = new Point3d(rtIntersection.getPointOfIntersection());
    	Vector3d reflectedRayDir = new Vector3d(this.direction); //incoming ray dir
        Vector3d normal = new Vector3d(rtIntersection.getSurfaceNormal());
        Double dot = reflectedRayDir.dot(normal);
        normal.scale(2 * dot);
        reflectedRayDir.sub(normal);
    	
        return new XRay(reflectedRayPos, reflectedRayDir, rtScene);
    }
    
    /**
     * Creates the refracted ray.
     * @return the refracted ray.
     */
    private XRay generateRefractedRay() {
        //n = n1 / n2                             
        //c1 = -I.N                             
        //c2 = sqrt(1 - n * n * (1 - c1 * c1))     
        //T = n * I + (n * c1 - c2) * N
        
        Vector3d N = new Vector3d(rtIntersection.getSurfaceNormal());
        Vector3d I = new Vector3d(direction);
        
        double n;
        
        if(direction.dot(N) > 0) { //OUT - leave a medium
            //n = rtSceneObject.getMaterial().getRefractionIndex();
            if(refractionIndices.size() > 1) { 
                n = refractionIndices.pop() / refractionIndices.peek();
            } else {
                //safety enquiry - this can be reached through calculation inaccuracies
                n = refractionIndices.peek();
            }
        } else { //IN - penetrate another medium
            //n = 1/rtSceneObject.getMaterial().getRefractionIndex();
            float enteringMedium = rtSceneObject.getMaterial().getRefractionIndex();
            n = refractionIndices.peek() / enteringMedium;
            refractionIndices.push(enteringMedium);
        }
        
        double c1 = -I.dot(N);
        
        double discriminant = 1 - n * n * (1 - c1 * c1);
        
        if(discriminant < 0) { //total reflection
            return generateReflectedRay();
        }
        
        double c2 = Math.sqrt(discriminant);
        
        Vector3d T = new Vector3d(I);
        T.scale(n);
        T.scaleAdd(n * c1 - c2, N);
        T.negate();
        
        return new XRay(new Point3d(rtIntersection.getPointOfIntersection()), T, this.rtScene, copyRefractionIndices());
    }
    
    /**
     * @return a copy of the refractionindices stack.
     */
    private Stack<Float> copyRefractionIndices() {
        Stack<Float> copyOfRefractionIndeces = new Stack<Float>();
        for(Float f : refractionIndices) {
            copyOfRefractionIndeces.push(f);
        }
        return copyOfRefractionIndeces;
    }

    /**
     * @return Returns the direction.
     */
    public Vector3d getDirection() {
        return direction;
    }

    /**
     * @return Returns the origin.
     */
    public Point3d getOrigin() {
        return origin;
    }
}
