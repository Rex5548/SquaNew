/*
 * Scene.java 
 * 05/10/23
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

import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

import org.xml.sax.SAXException;

/**
 * This class represents a complete scene. A scene contains
 * SceneObjects, a camera, lightsources and it has also a background color.<br/>
 * <br/>
 * The storage for scene objects and lights is implemented as
 * a linear list.<br/>
 * <br/>
 * The scene has also the ability to tell you, if a given ray intersects
 * a contained scene object or not. Further it tells you which is the
 * nearest intersected object.<br/>
 * <br/>
 * The scene can be loaded from a file, which contains a validate xml formed
 * representation of a scene.
 * 
 * @author M S
 */
public class Scene {

    /** Default scene bounds */
    public static final BoundingBox DEFAUL_BOUNDS = new BoundingBox(new Point3d(-50,-50,-50), new Vector3d(100,100,100));
    
    /** Epsilon that is used to compensate accuracy issues */
    public static final double EPSILON = 0.001;
    
    /** Container for all scene objects */
    protected List<SceneObject> sceneObjects;

    /** The scene's background color */
    protected Color3f backgroundColor;

    /** Container for all lightsources */
    protected List<Light> lights;

    /** The camera container */
    protected Camera camera;

    /** The scene's ambient light */
    protected Color3f ambientLight;

    /** The scene's bounding volume */
    protected BoundingBox sceneBounds;
    
    /**
     * Creates an empty scene with black background.
     * 
     */
    public Scene() {
        sceneObjects = new ArrayList<SceneObject>();
        lights = new ArrayList<Light>();
        backgroundColor = new Color3f(0.0f, 0.0f, 0.0f);
        ambientLight = new Color3f(0.0f, 0.0f, 0.0f);
        sceneBounds = DEFAUL_BOUNDS;
    }

    /**
     * @return the scene's ambient light.
     */
    public Color3f getAmbientLight() {
        return ambientLight;
    }

    /**
     * @return the scene's backgound color.
     */
    public Color3f getBackgroundColor() {
        return backgroundColor;
    }

    /**
     * Sets the scene's backgound color.
     * @param backgroundColor
     */
    public void setBackgroundColor(Color3f backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    /**
     * @return the scene's camara.
     */
    public Camera getCamera() {
        return camera;
    }

    /**
     * Sets the scene's camera.
     * @param camera
     */
    public void setCamera(Camera camera) {
        this.camera = camera;
    }

    /**
     * @return the list of all lights in the scene.
     */
    public List<Light> getLights() {
        return lights;
    }

    /**
     * @return the list of all scene objects.
     */
    public List<SceneObject> getSceneObjects() {
        return sceneObjects;
    }

    /**
     * Adds a new object to the scene.
     * @param newObject
     */
    public void addSceneObject(SceneObject newObject) {
        this.sceneObjects.add(newObject);
    }

    /**
     * Adds a new light to the scene.
     * @param newLight
     */
    public void addLight(Light newLight) {
        this.lights.add(newLight);
        this.ambientLight.add(newLight.getAmbient());
        this.ambientLight.clamp(0.0f, 1.0f);
    }
    
    /**
     * @return the number of scene objects.
     */
    public int getObjectCount() {
        return this.sceneObjects.size();
    }

    /**
     * @param ray The ray to intersect.
     * @return the nearest intersection of all scene objects with the given ray. Returns <code>null</code>
     * if there is no intersected object.
     */
    public Intersection getNearestIntersection(XRay ray) {
        double min_t = Double.POSITIVE_INFINITY;
       
        SceneObject nearestObject = null;
        
        for(SceneObject s : sceneObjects) {
            
            double t = s.intersect(ray);
      
            if(t > EPSILON) { // if(t <= epsilon) object is not intersected by the ray
            
                if(t < min_t) { //closer object found
                    min_t = t;
                    nearestObject = s;
            
                }
            }
        }
        if(nearestObject == null) {
        	return null;
        }
        else {
        	return new Intersection(nearestObject, min_t, ray);
        }
        
    }
    
    /**
     * @param intersection The intersecion for that the hitting lights will be computed.
     * @return a list of all lightsources in the scene, that throw light on the given intersection.
     */
    public List<Light> getHittingLights(Intersection intersection) {
        XRay shadowRay;
        List<Light> hittingLights = new ArrayList<Light>();
        //create a shadow ray for each lightsource and check if
        //there are objects between the current object and the lightsources
        for (Light light : lights) {
             Point3d point = new Point3d(intersection.getPointOfIntersection()); 
             Vector3d direction = new Vector3d(light.getPosition());
             direction.sub(point);
             shadowRay = new XRay(point, direction, this);   
             if(getShadowIntersection(shadowRay, light, intersection) == null) {
                 hittingLights.add(light);  
             } 
        }
        return hittingLights;
    }

    /**
     * Finds the nearest intersection between an intersection and a light source.
     * @param ray The shadow ray.
     * @param light The light source.
     * @param sourceIntersection The intersection for whicht the shadowintersection is computed.
     * @return an Intersection if there lies a sceneObject between the
     * source intersection and the lightsource, else <code>null</code>.
     */
    private Intersection getShadowIntersection(XRay ray, Light light, Intersection sourceIntersection) {
        Intersection shadowIntersection = getNearestIntersection(ray);  
        if(shadowIntersection != null) {
            double lightDistance = sourceIntersection.getPointOfIntersection().distance(light.getPosition()); 
            if(lightDistance <= shadowIntersection.getT()) {
                shadowIntersection = null;
            }
        }
        return shadowIntersection;
    }
    
    /**
     * Loads scene information from a file and add the objects, that are
     * represented in the file.
     *  
     * @param file Path to the scene file.
     * @throws IOException If file is not readable.
     * @throws SAXException If file is not a valid scene file.
     */
    public void load(String file) throws SAXException, IOException {
        new SceneFileLoader(this, file);
    }

    /**
     * @return the sceneBounds.
     */
    public BoundingBox getSceneBounds() {
        return sceneBounds;
    }

    /**
     * @param sceneBounds The sceneBounds to set.
     */
    public void setSceneBounds(BoundingBox sceneBounds) {
        this.sceneBounds = sceneBounds;
    }
}
