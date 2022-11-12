/*
 * Cell.java 
 * 05/12/09
 * 
 * University of Applied Sciences
 * Bingen, Germany
 * 
 * FPro Project "Jaytracer"
 * 
 * Autor: 
 * M S
 * 
 * 
 */
package de.fhbingen.fpro.jaytracer;

import java.util.ArrayList;
import java.util.List;

import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

/**
 * Cell of the regular grid.
 * @author M S
 */
public class Cell extends BoundingBox {
    
    /** The list of all sceneobjects that lie in this cell. */
    private List<SceneObject> sceneObjects;
    
    /**
     * Creates a grid cell.
     * @param lower The cell's lower point.
     * @param size The cell's size vector.
     */
    public Cell(Point3d lower, Vector3d size) {
        super(lower, size);
    	sceneObjects = new ArrayList<SceneObject>();
    }
    
    /**
     * Adds a scene object to this cell.
     * @param sceneObject
     */
    public void add(SceneObject sceneObject) {
        sceneObjects.add(sceneObject);
    }

    /**
     * @return Returns the sceneObjects.
     */
    public List<SceneObject> getSceneObjects() {
        return sceneObjects;
    }
}
