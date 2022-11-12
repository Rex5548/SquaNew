/*
 * SceneFileContentHandler.java 
 * 05/11/04
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

import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

/**
 * This is class extends the DefaultHandler class, which is used by the SAX XML
 * parser. It handles all the content of a "Jaytracer XML Scenefile" an creates
 * a scene from it.
 * 
 * @author M S
 */
public class SceneFileContentHandler extends DefaultHandler {

    /** Separator, that is used to divide part of strings, that represent vector or color components */
    private static final String SEPARATOR = ",";

    /** The scene that is build up from the file */
    private Scene scene;

    /** Last encountered scene object - the next material will be assigned to it */
    private SceneObject currentSceneObject = null;
    
    /**
     * Creates a SceneFileContentHandler that will add
     * the new builded scene to the given reference.
     * @param scene
     */
    public SceneFileContentHandler(Scene scene) {
        this.scene = scene;
    }

    /**
     * Handels start tags and create the appropriate new scene part.
     * @throws SceneFileException if there is a malformed part is encountered in the scene file.
     */
    public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SceneFileException {

        // <Scene>
        if (qName.equals("scene")) {
            for (int i = 0; i < atts.getLength(); i++) {
                if (atts.getQName(i).equals("backgroundcolor")) {
                    scene.setBackgroundColor(getColor3fFromAttr(atts.getValue(i)));
                }
                else {
                    throw new SceneFileException("unknown scene attribute: "+atts.getQName(i));
                }
            }
        }

        // <camera>
        else if (qName.equals("camera")) {

            if (scene.getCamera() != null) {
                throw new SceneFileException(
                        "multiple cameras - only one is allowed");
            } else {
                Camera camera;
                Point3d pos = null;
                Vector3d up = null, dir = null;
                double aspect = Double.NaN, vfov = Double.NaN, vdistance = Double.NaN;
                for (int i = 0; i < atts.getLength(); i++) {
                    if (atts.getQName(i).equals("position")) {
                        pos = getPoint3dFromAttr(atts.getValue(i));
                    }
                    else if (atts.getQName(i).equals("direction")) {
                        dir = getVector3dFromAttr(atts.getValue(i));
                    }
                    else if (atts.getQName(i).equals("up")) {
                        up = getVector3dFromAttr(atts.getValue(i));
                    }
                    else if (atts.getQName(i).equals("aspect")) {
                        aspect = getDoubleFromAttr(atts.getValue(i));
                    }
                    else if (atts.getQName(i).equals("vfov")) {
                        vfov = getDoubleFromAttr(atts.getValue(i));
                    }
                    else if (atts.getQName(i).equals("viewingplanedistance")) {
                        vdistance = getDoubleFromAttr(atts.getValue(i));
                    }
                    else {
                        throw new SceneFileException("unknown camera attribute: "+atts.getQName(i));
                    }
                }
                
                if(pos == null) {
                    throw new SceneFileException("missing camera position");
                }
                if(up == null) {
                    throw new SceneFileException("missing camera up");
                }
                if(dir == null) {
                    throw new SceneFileException("missing camera direction");
                }
                if(Double.isNaN(aspect)) {
                    throw new SceneFileException("missing camera aspect");
                }
                if(Double.isNaN(vfov)) {       
                    throw new SceneFileException("missing camera vfov");
                }
                if(Double.isNaN(vdistance)) {
                    throw new SceneFileException("missing camera viewingplanedistance");
                }
                
                camera = new Camera(pos, dir, up, vfov, aspect, vdistance);
                scene.setCamera(camera);   
                
            }
        }
        
        // <light>
        else if (qName.equals("light")) {
            Light light = new Light();
            for (int i = 0; i < atts.getLength(); i++) {
                if (atts.getQName(i).equals("position")) {
                    light.setPosition(getPoint3dFromAttr(atts.getValue(i)));
                }
                else if (atts.getQName(i).equals("ambient")) {
                    light.setAmbient(getColor3fFromAttr(atts.getValue(i)));
                }
                else if (atts.getQName(i).equals("diffuse")) {
                    light.setDiffuse(getColor3fFromAttr(atts.getValue(i)));
                }
                else if (atts.getQName(i).equals("specular")) {
                    light.setSpecular(getColor3fFromAttr(atts.getValue(i)));
                }  
                else if (atts.getQName(i).equals("intensity")) {
                    light.setIntensity(getFloatFromAttr(atts.getValue(i)));
                }  
                else {
                    throw new SceneFileException("unknown light attribute: "+atts.getQName(i));
                }
            }
            scene.addLight(light);
        }

        // <sphere>
        else if (qName.equals("sphere")) {
            Sphere sphere;
            double rad = Double.NaN;
            Point3d pos = null;
            for (int i = 0; i < atts.getLength(); i++) {
                if (atts.getQName(i).equals("position")) {
                    pos = getPoint3dFromAttr(atts.getValue(i));
                }
                else if (atts.getQName(i).equals("radius")) {
                    rad = getDoubleFromAttr(atts.getValue(i));
                }
                else {
                    throw new SceneFileException("unknown sphere attribute: "+atts.getQName(i));
                }
            }
            if(pos == null) {
                throw new SceneFileException("missing sphere position");
            }
            if(Double.isNaN(rad)) {
                throw new SceneFileException("missing sphere radius");
            }
            sphere = new Sphere(pos, rad);
            currentSceneObject = sphere;
            scene.addSceneObject(sphere); 
        }

        // <plane>
        else if (qName.equals("plane")) {
            Plane plane;
            Point3d point = null;
            Vector3d normal = null;
            for (int i = 0; i < atts.getLength(); i++) {
                if (atts.getQName(i).equals("point")) {
                    point = getPoint3dFromAttr(atts.getValue(i));
                }
                else if (atts.getQName(i).equals("normal")) {
                    normal = getVector3dFromAttr(atts.getValue(i));
                }
                else {
                    throw new SceneFileException("unknown plane attribute: "+atts.getQName(i));
                }
            }
            if(point == null) {
                throw new SceneFileException("missing plane point");
            }
            if(normal == null) {
                throw new SceneFileException("missing plane normal");
            } 
            plane = new Plane(point, normal);
            currentSceneObject = plane;
            scene.addSceneObject(plane); 
        }
        
        // <triangle>
        else if (qName.equals("triangle")) {
            Point3d v1 = null, v2 = null, v3 = null;
            for (int i = 0; i < atts.getLength(); i++) {
                if (atts.getQName(i).equals("vertex1")) {
                    v1 = getPoint3dFromAttr(atts.getValue(i));
                }
                else if (atts.getQName(i).equals("vertex2")) {
                    v2 = getPoint3dFromAttr(atts.getValue(i));
                }
                else if (atts.getQName(i).equals("vertex3")) {
                    v3 = getPoint3dFromAttr(atts.getValue(i));
                }
                else {
                    throw new SceneFileException("unknown triangle attribute: "+atts.getQName(i));
                }
            }
            if(v1 == null || v2 == null || v3 == null) {
                throw new SceneFileException("missing triangle vertex");
            }
            Triangle triangle = new Triangle(v1, v2, v3);
            currentSceneObject = triangle;
            scene.addSceneObject(triangle);
        }
        
        //<box>
        else if(qName.equals("box")) {
           
            Vector3d size = null;
            Point3d lower = null;
            for(int i = 0; i < atts.getLength(); ++i) {
                if(atts.getQName(i).equals("lower")) {
                    lower = getPoint3dFromAttr(atts.getValue(i));
                }
                else if(atts.getQName(i).equals("size")) {
                    size = getVector3dFromAttr(atts.getValue(i));
                }
                else {
                    throw new SceneFileException("unknown box attribute: "+atts.getQName(i));
                }
            }
            if(lower == null) {
                throw new SceneFileException("missing box lower");
            }
            if(size == null) {
                throw new SceneFileException("missing box size");
            } 
            Box box = new BoundingBox(lower, size);
            currentSceneObject = box;
            scene.addSceneObject(box);    
        }
        
        // <material>
        else if (qName.equals("material")) {
            if (currentSceneObject == null) {
                throw new SceneFileException(
                        "Material outside of a SceneObject is not allowed.");
            } else {
                Material material = new Material();
                for (int i = 0; i < atts.getLength(); i++) {
                    if (atts.getQName(i).equals("ambient")) {
                        material.setAmbient(getColor3fFromAttr(atts.getValue(i)));
                    }
                    else if (atts.getQName(i).equals("diffuse")) {
                        material.setDiffuse(getColor3fFromAttr(atts.getValue(i)));
                    }
                    else if (atts.getQName(i).equals("specular")) {
                        material.setSpecular(getColor3fFromAttr(atts.getValue(i)));
                    }
                    else if (atts.getQName(i).equals("shininess")) {
                        material.setShininess(getFloatFromAttr(atts.getValue(i)));
                    }
                    else if (atts.getQName(i).equals("transparency")) {
                        material.setTransparency(getFloatFromAttr(atts.getValue(i)));
                    }
                    else if (atts.getQName(i).equals("refractionindex")) {
                        material.setRefractionIndex(getFloatFromAttr(atts.getValue(i)));
                    }
                    else if (atts.getQName(i).equals("reflectioncoefficient")) {
                        material.setReflectionCoefficient(getFloatFromAttr(atts.getValue(i)));
                    }
                    else {
                        throw new SceneFileException("unknown material attribute: "+atts.getQName(i));
                    }
                }
                currentSceneObject.setMaterial(material);
            }
        }

        // if there is an unknown start tag, an exception will be thrown
        else {
            throw new SceneFileException("Unknown Tag: " + qName);
        }

    }

    /**
     * Detects missplaced characters in the scene file.
     * @throws SceneFileException if a missplaced character is encountered.
     */
    public void characters(char[] ch, int start, int length) throws SceneFileException {

        // detect missplaced characters
        String tmp = "";
        int end = start + length;
        for (int i = start; i < end; i++) {
            tmp += ch[i];
        }
        tmp = tmp.trim();
        if (!tmp.equals(""))
            throw new SceneFileException("missplaced characters: " + tmp);

    }

    /**
     * Handels end tags. This method has only the job to set the
     * currentSceneObject to null, if the sceneobject is passed.
     */
    public void endElement(String namespaceURI, String localName, String qName) {
        if (qName.equals("sphere") || qName.equals("triangle") || qName.equals("plane")) {
            currentSceneObject = null;
        } 
    }

    /**
     * Creates a Color3f container from a given string.
     * @param attr
     * @return a color.
     * @throws SceneFileException if the string does not represent a color.
     */
    private Color3f getColor3fFromAttr(String attr) throws SceneFileException {
        String[] colorString = attr.split(SEPARATOR);

        if (colorString.length != 3) {
            throw new SceneFileException("malformed rgb3d attribute");
        } else {
            float red, green, blue;
            try {
                red = Float.valueOf(colorString[0]);
                green = Float.valueOf(colorString[1]);
                blue = Float.valueOf(colorString[2]);
            } catch (NumberFormatException e) {
                throw new SceneFileException("malformed rgb3d attribute");
            }
            return new Color3f(red, green, blue);
        }
    }

    /**
     * Creates a Point3d container from a given string.
     * @param attr
     * @return the point3d.
     * @throws SceneFileException if the string does not represent a point3d.
     */
    private Point3d getPoint3dFromAttr(String attr) throws SceneFileException {
        String[] colorString = attr.split(SEPARATOR);

        if (colorString.length != 3)
            throw new SceneFileException("malformed vector3d attribute");

        double x, y, z;
        try {
            x = Double.valueOf(colorString[0]);
            y = Double.valueOf(colorString[1]);
            z = Double.valueOf(colorString[2]);
        } catch (NumberFormatException e) {
            throw new SceneFileException("malformed vector3d attribute");
        }
        return new Point3d(x, y, z);
    }
    
    /**
     * Creates a Vector3d container from a given string.
     * @param attr
     * @return the vector3d.
     * @throws SceneFileException if the string does not represent a vector3d.
     */
    private Vector3d getVector3dFromAttr(String attr) throws SceneFileException {
        return new Vector3d(getPoint3dFromAttr(attr));
    }
    
    /**
     * Parses a double value from a given string.
     * @param attr
     * @return the double value
     * @throws SceneFileException if the string does not represent a double.
     */
    private double getDoubleFromAttr(String attr) throws SceneFileException {
        double x;
        try {
            x = Double.parseDouble(attr);
        } catch (NumberFormatException e) {
            throw new SceneFileException("malformed double attribute");
        }
        return x;
    }
    
    /**
     * Parses a float value from a given string.
     * @param attr
     * @return the float value
     * @throws SceneFileException if the string does not represent a float.
     */
    private float getFloatFromAttr(String attr) throws SceneFileException {
        float x;
        try {
            x = Float.parseFloat(attr);
        } catch (NumberFormatException e) {
            throw new SceneFileException("malformed double attribute");
        }
        return x;
    }

}
