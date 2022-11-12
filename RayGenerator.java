/*
 * RayGenerator.java 
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

import javax.vecmath.*;
import java.util.Date;

/**
 * This class is the ray generator. It creates rays according the given
 * resolution and the viewingplane, that is defined by the scene's camera configuration.
 * All rays are traced and the calculated colors will be written to a pixelmap.
 * @author P L
 */
public class RayGenerator extends Thread {
    
    /** Constant for no supersampling */
    public static final int NO_SUPERSAMPLING = 0;
    
    /** Constant for supersampling with a rotated grid */
    public static final int ROTATED_GRID = 1;
    
    /** Constant for supersampling with an ordered grid */
    public static final int ORDERD_GRID = 2;
	
	/** Generator vars */
	private int firstX;
	private int firstY;
    private int width;
    private int height;
    private Scene rtScene;
    private int supersampling;
    private int recursiveDepth;
    private int[][] pixelMap;
    private Point3d eyePos;
    private Vector3d[][] ssGrid; 
	private Point3d origin;
	private Vector3d viewingPlaneX;
	private Vector3d viewingPlaneY;
	private Vector3d eyeVec;
	private Vector3d rasterOrigin;
	private double pointsInWidth;
	private double pointsInHeight;
	
	/**
     * Creates a RayGenerator.
     * @param rtScene The scene to ray trace.
     * @param width The horizontal stepcount.
     * @param height The vertical stepcount.
     * @param recursiveDepth The recursive depth for reflected and refracted rays.
     * @param supersampling The supersampling mode.
	 */
    public RayGenerator(Scene rtScene, int width, int height, int recursiveDepth, int supersampling) {
    	this.rtScene = rtScene;
    	this.firstX = 0;
    	this.firstY = 0;
    	this.width = width;
    	this.height = height;
    	this.supersampling = supersampling;
        this.recursiveDepth = recursiveDepth;
    	pixelMap = new int[height][width];

		this.pointsInWidth = width- firstX;
		this.pointsInHeight = height- firstY;
		this.viewingPlaneX = new Vector3d(rtScene.getCamera().getViewingPlane().getWidthVector());
		this.viewingPlaneX.scale(rtScene.getCamera().getViewingPlane().getWidth());
		this.viewingPlaneY = new Vector3d(rtScene.getCamera().getViewingPlane().getHeightVector());
		this.viewingPlaneY.scale(rtScene.getCamera().getViewingPlane().getHeight());

		eyePos = new Point3d(rtScene.getCamera().getPosition());
		this.eyeVec = new Vector3d(eyePos.x, eyePos.y, eyePos.z);
		this.origin = new Point3d(rtScene.getCamera().getViewingPlane().getUpperLeftPoint());
		this.rasterOrigin = new Vector3d(origin.x, origin.y, origin.z);
		
		// supersampling
		ssGrid = new Vector3d[2][4];
		// rotetad grid
		ssGrid[0][0] = new Vector3d(0.360/pointsInWidth, 0.8198/pointsInHeight, 0.0);
		ssGrid[0][1] = new Vector3d(0.810/pointsInWidth, 0.6396/pointsInHeight, 0.0);
		ssGrid[0][2] = new Vector3d(0.630/pointsInWidth, 0.1891/pointsInHeight, 0.0);
		ssGrid[0][3] = new Vector3d(0.180/pointsInWidth, 0.3693/pointsInHeight, 0.0);
		// orderd grid
		ssGrid[1][0] = new Vector3d(0.25/pointsInWidth, 0.75/pointsInHeight, 0.0);
		ssGrid[1][1] = new Vector3d(0.75/pointsInWidth, 0.75/pointsInHeight, 0.0);
		ssGrid[1][2] = new Vector3d(0.25/pointsInWidth, 0.25/pointsInHeight, 0.0);
		ssGrid[1][3] = new Vector3d(0.25/pointsInWidth, 0.25/pointsInHeight, 0.0);	
    }    
    
    /**
     * Starts this RayGeneator.
     */
    public void run() {
    	
    	XRay rtRay;
    	XRay rtRaySS1;
    	XRay rtRaySS2;
    	XRay rtRaySS3;
    	XRay rtRaySS4;
    	Vector3d vecSS1;
    	Vector3d vecSS2;
    	Vector3d vecSS3;
    	Vector3d vecSS4;
    	
    	//Timer jtTimer = new Timer();
    	Date actlDate = new Date();
    	long startSec = actlDate.getTime();
    	long holeSec;
    	Color3f color;
    	
    	Point3d rayPoint = new Point3d(rtScene.getCamera().getPosition());
        for(int j = firstY; j < height; ++j) {
            for(int i = firstX; i < width; ++i) {
            	if (this.supersampling == 0) {
            		rtRay = new XRay(rayPoint, getVec(j,i), rtScene);
            		color = rtRay.recursiveTrace(recursiveDepth);
            		
            	} else {
            	//rtRay = new XRay(rayPoint, getVec(j,i), rtScene);
            	//generate sumpersampling rays
            	vecSS1 = getVec(j,i);
            	
            	vecSS1.normalize();
            	vecSS2 = new Vector3d(vecSS1);
            	vecSS3 = new Vector3d(vecSS1);
            	vecSS4 = new Vector3d(vecSS1);
            	
            	vecSS1.add(this.ssGrid[this.supersampling-1][0]);
            	vecSS2.add(this.ssGrid[this.supersampling-1][1]);
            	vecSS3.add(this.ssGrid[this.supersampling-1][2]);
            	vecSS4.add(this.ssGrid[this.supersampling-1][3]);
            	
            	rtRaySS1 = new XRay(rayPoint, vecSS1, rtScene);
            	rtRaySS2 = new XRay(rayPoint, vecSS2, rtScene);
            	rtRaySS3 = new XRay(rayPoint, vecSS3, rtScene);
            	rtRaySS4 = new XRay(rayPoint, vecSS4, rtScene);
            	
            	color = rtRaySS1.recursiveTrace(100);
            	color.add(rtRaySS2.recursiveTrace(100));
            	color.add(rtRaySS3.recursiveTrace(100));
            	color.add(rtRaySS4.recursiveTrace(100));
            	
            	color.scale(0.25f);
            	}
            	
            	//color.clampMax(1.0f);
            	pixelMap[j][i] = toInt(color);
            }
            	
            if(j % (height/10) == 0) {
            	System.out.print("Trace Status: " + ((float) j/height) +"  (" +j +" / " +height +") ");
            	actlDate = new Date();
            	holeSec = actlDate.getTime() - startSec;
            	System.out.println("Time: " +holeSec/1000 +" s over");
            }
        }
        System.out.print("Trace Status: " +1.0  +"  (" +height +" / " +height +") ");
    	actlDate = new Date();
    	holeSec = actlDate.getTime() - startSec;
    	System.out.println("Time: " +holeSec/1000 +" s over");
    }    
    
    /**
     * Prints the pixel map to the standard output stream.
     */
    public void printPixelMap() {
    	for (int y = 0; y < height; y++) {
    		for (int x = 0; x < width; x++) {
    			System.out.print("[" +this.pixelMap[y][x] +"] ");
    			//System.out.print(" " + (x) +"/" +(rasterOrigin.y-(y+1)*heightRatio) +" ");
    		}
    		System.out.println();
    	}
    }
    
    /**
     * @return the pixmelmap, which contains the raytraced image.
     */
    public int[][] getPixelMap() {
    	return pixelMap;
    }
    
    /**
     * Converts a color3f object into an int.
     * @param c The color to convert
     * @return the int that represents the color.
     */
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
    
    /**
     * Creates the ray for the current grid part.
     * @param j
     * @param i
     * @return the created ray
     */
    private Vector3d getVec(int j, int i) { 	
		Vector3d newVecX = new Vector3d(this.viewingPlaneX);
		Vector3d newVecY = new Vector3d(this.viewingPlaneY);
		newVecX.scale((i+0.5)/ (double) (this.pointsInWidth));
		newVecY.scale((j+0.5)/ (double) (this.pointsInHeight));
    	Vector3d rayVec = new Vector3d(newVecX);
    	rayVec.add(newVecY);
    	rayVec.add(this.rasterOrigin);
    	rayVec.sub(this.eyeVec);
    	return rayVec;
    }
}
