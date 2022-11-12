/*
 * Ray.java 
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
 * 
 */
package de.fhbingen.fpro.jaytracer;


/**
 * Deprecated Ray Class
 * @deprecated
 * @see de.fhbingen.fpro.jaytracer.XRay
 * @author P L
 */
public class Ray {
    
    /*private Point3d point;
    private Vector3d direction;
    private Scene rtScene;*/
    
   
    /*public Ray(Point3d p, Vector3d d, Scene rtScene) {
        point = p;
        d.normalize();
        direction = d;
        this.rtScene = rtScene;
    }

    public int trace() {
    	Color3f color = new Color3f(0.0f,0.0f,0.0f);
    	//SceneObject rtSceneObject = rtScene.getSceneObjectOfNearestIntersection(this);
    	
    	if (rtSceneObject == null) {
    		//System.out.println("null object");
    		return toInt(rtScene.getBackgroundColor());
    	}
    	
    	
    	color = addColor3f(color, shadeAmb(rtSceneObject));
    	
    	if (isInShadow(rtSceneObject) == false) {
    		color = addColor3f(color, shadeDif(rtSceneObject));
    		color = addColor3f(color, shadeSpec(rtSceneObject));
		}
    	color.clampMax(1.0f);
    	return toInt(color);
    }
    
    public int toInt(Color3f c) {
    	if (c == null)
    		return 0xFF000000;
    	int r, g, b, a;
    	a = 0xFF000000;
    	r = ((int) (c.x *255)) << 16;
    	g = ((int) (c.y *255)) << 8;
    	b = ((int) (c.z *255));
    	return a | r | g | b;
    }
    
    private Color3f shadeAmb(SceneObject rtSceneObject){
    	Color3f color = new Color3f(0.0f,0.0f,0.0f);
    	List<Light> rtLights = rtScene.getLights();

    	for (Light light : rtLights) {
    		color= addColor3f(color,light.getAmbient());
    	}
    	
    	return mulColor3f(rtSceneObject.getMaterial().getAmbient(), color);
    }
    
    private Color3f shadeDif(SceneObject rtSceneObject) {
    	Color3f color = new Color3f(0.0f,0.0f,0.0f);
    	List<Light> rtLights = rtScene.getLights();
    	Vector3d Nsurface;
    	Vector3d Nlight;
    	Color3f Msurface;
    	Color3f Mlight;
    	
    	float lambda;
    	Nsurface = rtSceneObject.getSurfaceNormal();
    	Msurface = rtSceneObject.getMaterial().getDiffuse();
    	Color3f lColor;
    	
    	for (Light light : rtLights) {
    		Nlight = rtSceneObject.getLightNormal(light);
    		lambda = (float) Nsurface.dot(Nlight);
    		if (lambda < 0.0f) lambda = 0.0f;
    		Mlight = light.getDiffuse();
    		lColor = mulColor3f(Mlight, Msurface);
    		lColor.scale(lambda);
    		color = addColor3f(color, lColor);
    	}
    	
    	return color;
    }
    
    private Color3f shadeSpec(SceneObject rtSceneObject) {
    	Color3f color = new Color3f(0.0f,0.0f,0.0f);
    	List<Light> rtLights = rtScene.getLights();
    	
    	Vector3d Nsurface;
    	Vector3d Vlight;
    	Vector3d Vreflect;
    	
    	Color3f Msurface;
    	Color3f Mlight;
        Color3f lColor;
        
    	Vector3d Vviewpoint = new Vector3d(this.direction);
    	Vviewpoint.scale(-1.0); //vector in viewpoint direction = opposite ray direction
    	Nsurface = rtSceneObject.getSurfaceNormal();
    	Msurface = rtSceneObject.getMaterial().getSpecular();
    	
    	for (Light light : rtLights) {
    		Vlight = rtSceneObject.getLightNormal(light);
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
    		lColor = mulColor3f(Mlight, Msurface);
    		
            lColor.scale((float)Math.pow(specFactor, rtSceneObject.getMaterial().getShininess()));
    	
    		color = addColor3f(color, lColor);
    	}
    	
    	return color;
    }
    
    private Color3f mulColor3f(Color3f x, Color3f y) {
    	return new Color3f(x.x*y.x, x.y*y.y, x.z*y.z);
    }
    
    private Color3f addColor3f(Color3f x, Color3f y) {
    	return new Color3f(x.x+y.x, x.y+y.y, x.z+y.z);
    }
    
    private boolean isInShadow(SceneObject rtSceneObject) {
    	Ray shadowRay;
    	SceneObject shadowObject;
    	double tToLight;
    	double tToObject;
    	
    	List<Light> rtLights = rtScene.getLights();
    	
    	for (Light light : rtLights) {
    		 Point3d point = new Point3d(rtSceneObject.pointOfIntersection); 
        	 Vector3d direction = new Vector3d(light.getPosition());
        	 direction.sub(rtSceneObject.pointOfIntersection);
    		 shadowRay = new Ray(point, direction, rtScene);
        	 shadowObject = rtScene.getSceneObjectOfNearestIntersection(shadowRay);
        	 
        	 
    	}
    	
    	
    	
    	
      return false;
    }*/
    
}
