/*
 * ImageOutput.java 
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

import javax.imageio.*;
import java.io.*;
import java.awt.image.*;

/**
 * A class for writing an integer array in an image.
 * At the moment i decided only to write in PNG format. 
 * Its a has no compression which could change the image.
 * @author P L
 */
public class ImageOutput {
	
    /**
	 * Writes an integer-array in a PNG image. 
	 * 
	 * @param pixelMap The pixelmap to write.
	 * @param name The filename.
	 * @throws IOException if an io error occurs.
	 */
	public void writePNG (int[][] pixelMap , String name) throws IOException {
		int width = pixelMap[0].length;
		int height = pixelMap.length;
		
        BufferedImage bImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        for(int y = 0; y < height; y++) {
        	for(int x = 0; x < width; x++) {
        		
        		bImage.setRGB(x,y, pixelMap[y][x]);
        	}
        }
        
		File file = new File(name + ".png");
		ImageIO.write(bImage,"png", file);
	}
	
	/*
	 * Testing this class and give information how to use it.
	 */
	/*public static void main(String[] args) throws IOException {
		int[][] pixel = new int[1200][1600];
		for(int x = 0;  x < pixel[0].length;  x++)
            for(int y = 0;  y < pixel.length;  y++) {
              	pixel[y][x] = (0xFF << 24) | (x << 16) | (y << 8);
            }
		ImageOutput ImWriter = new ImageOutput();
		ImWriter.writePNG(pixel, "pascal");
	}*/
}

