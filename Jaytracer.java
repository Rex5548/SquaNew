/*
 * Jaytracer.java 
 * 06/01/05
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

import java.io.IOException;

import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

import org.xml.sax.SAXException;

/**
 * The Main Class.
 * @author M S
 */
public class Jaytracer {

    /**
     * Mainroutine
     * @param args
     */
    public static void main(String[] args) {
        
        if(args.length == 0) {
            printUsage();
            return;
        }
        
        if(args.length < 2) {
            System.out.println("Error: not enough parameters");
            return;
        }   
        else if(args.length == 2) {
            jaytrace(args[0], args[1], 800, 800, 5, 0); 
        }
        else {
            
            if((args.length - 2) % 2 != 0) {
                System.out.println("Error: malformed parameter");
                return;
            }
            
            String supSamp;
            int ss = 0;
            int x = 800;
            int y = 800;
            int ttl = 5;
            Point3d lower = null;
            Vector3d size = null;
            int cells = 0;
            
            try {
            
                for(int i = 2; i < args.length; ++i) {
                    
                    if(args[i].equals("-R")) {
                        String res = args[i+1];
                        String[] tmp = res.split("x");
                        if(tmp.length != 2) {
                            System.out.println("Error: malformed -R parameter");
                            return;
                        }
                        x = Integer.parseInt(tmp[0]);
                        y = Integer.parseInt(tmp[1]);
                    }
                    else if(args[i].equals("-TTL")) {
                        ttl = Integer.parseInt(args[i+1]);
                        if(ttl < 0) {
                            System.out.println("Error: malformed -TTL parameter");
                            return;
                        }
                    }
                    else if(args[i].equals("-S")) {
                        supSamp = args[i+1];
                        if(supSamp.equals("OFF")) {
                            ss = RayGenerator.NO_SUPERSAMPLING;
                        } else if(supSamp.equals("ROTATED")) {
                            ss = RayGenerator.ROTATED_GRID;
                        } else if(supSamp.equals("ORDERED")) {
                            ss = RayGenerator.ORDERD_GRID;
                        }
                        else {
                            System.out.println("Error: malformed -S parameter");
                            return;
                        }
                    }
                    else if(args[i].equals("-G")) {
                        String grid = args[i+1];
                        String tmp[] = grid.split("x"); 
                        if(tmp.length != 3) {
                            System.out.println("Error: malformed -G parameter");  
                            return;
                        }
                        lower = getPoint3dString(tmp[0]);
                        size = new Vector3d(getPoint3dString(tmp[1]));
                        cells = Integer.parseInt(tmp[2]);
                        if(cells < 1) {
                            System.out.println("Error: malformed -G parameter cells");
                            return;
                        }
                    }
                    else {
                        System.out.println("Error: unknown parameter");
                        return;
                    }
                    i++;
                }
            } catch (NumberFormatException e) {
                System.out.println("Error: malformed parameter value");
                return;
            }
            
            if(lower != null && size != null) {
                jaytrace(args[0], args[1], x, y, ttl, ss, lower, size, cells); 
            }
            else {
                jaytrace(args[0], args[1], x, y, ttl, ss);
            }
        }
        
        
    }

    /**
     * Starts raytracing.
     * @param src Scenefile path.
     * @param tgt Target image path.
     * @param x X resolution.
     * @param y Y resolution.
     * @param ss Super sampling mode.
     * @param rec Recursion depth.
     */
    private static void jaytrace(String src, String tgt, int x, int y, int rec, int ss) {
        System.out.println("scene_file: "+src);
        System.out.println("image_File: "+tgt);
        System.out.println("resolution: "+x+"x"+y);
        System.out.println("recursion_depth: "+rec);
        if(ss == RayGenerator.NO_SUPERSAMPLING) {
            System.out.println("super_sampling: off");
        } else if(ss == RayGenerator.ROTATED_GRID) {
            System.out.println("super_sampling: rotated grid");
        } else {
            System.out.println("super_sampling: ordered grid");
        }
        
        Scene rtScene = new Scene();
        ImageOutput rtImageOutput = new ImageOutput();
        try {
            rtScene.load(src);
            RayGenerator rtRayGenerator = new RayGenerator(rtScene, x ,y , rec, ss);
            rtRayGenerator.start();
            rtRayGenerator.join();
            rtImageOutput.writePNG(rtRayGenerator.getPixelMap(), tgt);
        } catch (SAXException e) {
            System.out.println("Malformed Scenefile: "+e.getMessage());
        } catch (IOException e) {
            System.out.println("I/O error: "+e.getMessage());
        } catch (InterruptedException e) {
            System.out.println("Raytracer error: "+e.getMessage());
        }
    }
    
    /**
     * Starts raytracing with a regular grid.
     * @param src Scenefile path.
     * @param tgt Target image path.
     * @param x X resolution.
     * @param y Y resolution.
     * @param ss Super sampling mode.
     * @param rec Recursion depth.
     * @param lower The grid's lower point.
     * @param size The grid's size.
     * @param cells The number of cells in each direction.
     */
    private static void jaytrace(String src, String tgt, int x, int y, int rec, int ss, Point3d lower, Vector3d size, int cells) {
        System.out.println("scene_file: "+src);
        System.out.println("image_File: "+tgt);
        System.out.println("resolution: "+x+"x"+y);
        System.out.println("recursion_depth: "+rec);
        if(ss == RayGenerator.NO_SUPERSAMPLING) {
            System.out.println("super_sampling: off");
        } else if(ss == RayGenerator.ROTATED_GRID) {
            System.out.println("super_sampling: rotated grid");
        } else {
            System.out.println("super_sampling: ordered grid");
        }
        System.out.println("regular_grid_lower: "+lower);
        System.out.println("regular_grid_upper: "+size);
        System.out.println("cell_count: "+cells);
        RegularGridScene rtScene = new RegularGridScene(lower, size, cells);
        ImageOutput rtImageOutput = new ImageOutput();
        try {
            rtScene.load(src);
            rtScene.printInformation();
            RayGenerator rtRayGenerator = new RayGenerator(rtScene ,x ,y , rec, ss);
            rtRayGenerator.start();
            rtRayGenerator.join();
            rtImageOutput.writePNG(rtRayGenerator.getPixelMap(), tgt);
        } catch (SAXException e) {
            System.out.println("Malformed Scenefile: "+e.getMessage());
        } catch (IOException e) {
            System.out.println("I/O error: "+e.getMessage());
        } catch (InterruptedException e) {
            System.out.println("Raytracer error: "+e.getMessage());
        }
    }
    
    /**
     * Prints the usage information.
     */
    private static void printUsage() {
        System.out.println("Usage:");
        System.out.println("Jaytracer <scenefilepath> <outputpath>");         
        System.out.println("options:");
        System.out.println("\t-R WIDTHxHEIGHT (resolution, e.g. 500x500)");
        System.out.println("\t-TTL x (recursion depth, 0,...)");
        System.out.println("\t-S [OFF, ORDERED, ROTATED] (supersampling mode)");
        System.out.println("\t-G LOWERxSIZExCELLS (regular grid, e.g \"-50,-50,-50x100,100,100x10\")");
    }
    
    /**
     * Creates a Point3d object from a given string.
     * @param string The string to convert.
     * @return the point3d object.
     */
    private static Point3d getPoint3dString(String string) {
        String[] point3dString = string.split(",");

        if (point3dString.length != 3)
            throw new NumberFormatException();

        double x, y, z;

        x = Double.valueOf(point3dString[0]);
        y = Double.valueOf(point3dString[1]);
        z = Double.valueOf(point3dString[2]);

        return new Point3d(x, y, z);
    }
}
