/*
 * SceneFileException.java 
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

import org.xml.sax.SAXException;

/**
 * This exception is used when errors with an read
 * scenefile occure.
 * 
 * @author M S
 */
public class SceneFileException extends SAXException {
    
    /**
     * Creates a SceneFileException. 
     *
     */
    public SceneFileException()
    {
        super();
    }
    
    /**
     * Creates a SceneFileException and stores the given
     * error message.
     * @param s
     */
    public SceneFileException( String s )
    {
        super( s );
    }
}
