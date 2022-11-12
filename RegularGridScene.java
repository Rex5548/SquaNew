/*
 * RegularGridScene.java 
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

import java.util.List;

import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

/**
 * Regular grid scene</br>
 * This scene orders the scene objects in a regular grid, to reduce
 * the intersection test count.<br/>
 * The grids size is defined by the scene's bounding box. The count of
 * grid cells in every dimension can be set by the x,y and z parameter of
 * the constructor.<br/>
 * All scene objects, that are not contained by the grid, are put in an
 * linear list, similar to the standard scene hierarchy. These objects will
 * also be relevant for the intersection tests.
 * 
 * @author M S
 *
 */
public class RegularGridScene extends Scene {
    
    /** The grid's lower point (back-lower-left point)*/
    private Point3d lower;
    
    /** The number of cells in every dimension */
    private int cellCount;
    
    /** The grid */
    private Cell[] grid;
    
    /** The size of each cell */
    private Vector3d cellSize;
    
    /** Reciprocal of gridBound.size divided by grid size */
    private Vector3d scaleCountR;
    
    /** Number of scene objects that are added to the grid */
    private int numberOfObjectsInGrid = 0;
    
    /**
     * Creates a scene width a regular grid sceneobject hierarchy.
     * @param lower The grids lower point (left-lower-back point).
     * @param size The size of the grid.
     * @param cellCount The number of cells in each direction.
     */
    public RegularGridScene(Point3d lower, Vector3d size, int cellCount) {
        
        sceneBounds = new BoundingBox(lower, size);
        this.cellCount = cellCount;
        this.lower = new Point3d();
        cellSize = new Vector3d();
        scaleCountR = new Vector3d();
        
        this.lower.set(lower);
        grid = new Cell[cellCount*cellCount*cellCount]; 
        cellSize.x = sceneBounds.size.x / cellCount;
        cellSize.y = sceneBounds.size.y / cellCount;
        cellSize.z = sceneBounds.size.z / cellCount; 
        scaleCountR.x = cellCount / sceneBounds.size.x;
        scaleCountR.y = cellCount / sceneBounds.size.y;
        scaleCountR.z = cellCount / sceneBounds.size.z;  
        
        createGrid();   
    }
    
    /**
     * Initializes the grid.
     */
    private void createGrid() {
        
        Point3d cellPos = new Point3d(lower);
        
        for(int x = 0; x < cellCount; ++x) {  
            cellPos.y = lower.y;
            for(int y = 0; y < cellCount; ++y) {
                cellPos.z = lower.z;
                for(int z = 0; z < cellCount; ++z) {     
                    grid[x + y * cellCount + z * cellCount * cellCount] = new Cell(new Point3d(cellPos), cellSize);
                    cellPos.z += cellSize.z;
                }
                cellPos.y += cellSize.y;
            }
            cellPos.x += cellSize.x;
        } 
    }
    
    /**
     * Adds a scene object to the scene. If the scene object lies in the grid's bounds, it will
     * be added to the grid, else to the standard object list.
     * @param sceneObject The sceneObject to add.
     */
    public void addSceneObject(SceneObject sceneObject) {
        
        boolean addedToGrid = false;
        
        if(sceneBounds.containsBoundingVolume(sceneObject.getBounds())) {
            for(Cell c : grid) {
                if(sceneObject.getBounds().intersectsBoundingVolume(c)) {
                    if(sceneObject.intersectsCell(c)) {
                        c.add(sceneObject);
                        addedToGrid = true;
                        numberOfObjectsInGrid++;
                    } 
                }
            }  
        }
        
        if(!addedToGrid) { 
            sceneObjects.add(sceneObject);
        }
    }
    
    /**
     * @param ray The ray to intersect.
     * @return The nearest intersection of the given ray with the
     * grid's scene objects. Returns <code>null</code> if there is no object intersected.
     */
    private Intersection getNearestIntersectionFromGrid(XRay ray) {
        
        Vector3d raydir, curpos;
        BoundingBox e = this.sceneBounds;
        raydir = new Vector3d(ray.getDirection());
        
        // setup 3DDDA (double check reusability of primary ray data)
        Vector3d cb = new Vector3d();
        Vector3d tmax = new Vector3d();
        Vector3d tdelta = new Vector3d();
 
        //find start point for grid traversal
        if(!sceneBounds.containsPoint(ray.getOrigin())) {
            double test = sceneBounds.intersect(ray);
            if(test > EPSILON) {
                Intersection i = new Intersection(sceneBounds, test, ray);
                curpos = new Vector3d(i.getPointOfIntersection());
            } else {
                return null;
            }
        } else {
            curpos = new Vector3d(ray.getOrigin());
        }
        Vector3d cell = new Vector3d(curpos);
        cell.sub(e.lower);
        cell = VecmathAddon.mulVector3d(cell, scaleCountR);
        
        int stepX, outX, X = (int)cell.x;
        int stepY, outY, Y = (int)cell.y;
        int stepZ, outZ, Z = (int)cell.z;
        
        if ((X < 0) || (X >= cellCount) || (Y < 0) || (Y >= cellCount) || (Z < 0) || (Z >= cellCount)) { 
            return null;
        }
        
        if (raydir.x > 0)
        {
            stepX = 1;
            outX = cellCount;
            cb.x = e.lower.x + (X + 1) * cellSize.x;
        }
        else 
        {
            stepX = -1;
            outX = -1;
            cb.x = e.lower.x + X * cellSize.x;
        }
        if (raydir.y > 0.0)
        {
            stepY = 1;
            outY = cellCount;
            cb.y = e.lower.y + (Y + 1) * cellSize.y; 
        }
        else 
        {
            stepY = -1;
            outY = -1;
            cb.y = e.lower.y + Y * cellSize.y;
        }
        if (raydir.z > 0.0)
        {
            stepZ = 1;
            outZ = cellCount;
            cb.z = e.lower.z + (Z + 1) * cellSize.z;
        }
        else 
        {
            stepZ = -1;
            outZ = -1;
            cb.z = e.lower.z + Z * cellSize.z;
        }
        double rxr, ryr, rzr;
        if (raydir.x != 0)
        {
            rxr = 1.0f / raydir.x;
            tmax.x = (cb.x - curpos.x) * rxr; 
            tdelta.x = cellSize.x * stepX * rxr;
        }
        else tmax.x = Double.POSITIVE_INFINITY;
        if (raydir.y != 0)
        {
            ryr = 1.0f / raydir.y;
            tmax.y = (cb.y - curpos.y) * ryr; 
            tdelta.y = cellSize.y * stepY * ryr;
        }
        else tmax.y = Double.POSITIVE_INFINITY;
        if (raydir.z != 0)
        {
            rzr = 1.0f / raydir.z;
            tmax.z = (cb.z - curpos.z) * rzr; 
            tdelta.z = cellSize.z * stepZ * rzr;
        }
        else tmax.z = Double.POSITIVE_INFINITY;
        
        // start stepping
        List<SceneObject> list;
        SceneObject nearestHitObject = null;
        
        while (true)
        {
            Cell currentCell = grid[X + (Y * cellCount) + (Z * cellCount * cellCount)];
            list = currentCell.getSceneObjects();
            double t = Double.POSITIVE_INFINITY;
            for(SceneObject s : list)
            { 
                double result = s.intersect( ray );
                if (result > EPSILON && result < t) 
                {
                    t = result; 
                    nearestHitObject = s;
                }
            }
            
            //if found intersection belongs to the current cell, it is the nearest
            //else go on...
            if(nearestHitObject != null) {
                Intersection intersection = new Intersection(nearestHitObject, t, ray);
                if(currentCell.containsPoint(intersection.getPointOfIntersection())) {
                    return intersection;
                }
            }
            
            //next step
            if (tmax.x < tmax.y)
            {
                if (tmax.x < tmax.z)
                {
                    X = X + stepX;
                    if (X == outX) return null;
                    tmax.x += tdelta.x;
                }
                else
                {
                    Z = Z + stepZ;
                    if (Z == outZ) return null;
                    tmax.z += tdelta.z;
                }
            }
            else
            {
                if (tmax.y < tmax.z)
                {
                    Y = Y + stepY;
                    if (Y == outY) return null;
                    tmax.y += tdelta.y;
                }
                else
                {
                    Z = Z + stepZ;
                    if (Z == outZ) return null;
                    tmax.z += tdelta.z;
                }
            }     
        }     
    }
  
    /**
     * @param ray The ray to intersect.
     * @return the nearest intersection of all scene objects with the given ray. Returns <code>null</code>
     * if there is no intersected object.
     */
    public Intersection getNearestIntersection(XRay ray) {
        Intersection gridIntersection = this.getNearestIntersectionFromGrid(ray);
        //intersection from objects that are not in the grid
        Intersection listIntersection = super.getNearestIntersection(ray);
        
        if(gridIntersection == null) {
            return listIntersection;
        }
        
        if(listIntersection == null) {
            return gridIntersection;
        }
        
        if(gridIntersection.getT() < listIntersection.getT()) {
            return gridIntersection;
        } else {
            return listIntersection;
        }
    }
    
    /**
     * Prints the number of objects in and out of the grid.
     */
    public void printInformation() {
        System.out.println("objects in grid: "+numberOfObjectsInGrid+" (notice: one object can appear multiple times)");
        System.out.println("objects not in grid: "+sceneObjects.size());
    }
}
