/*
 * SceneFileLoader.java 
 * 05/11/03
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

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.SAXException;

/**
 * Parses xml scenefiles by using the SAX xml parser.
 * 
 * @author M S
 *
 */
public class SceneFileLoader {

    /**
     * After the creation of the scene file loader object the
     * parser tarts immediately. The information in the 
     * scene file will be add to the given scene.
     * 
     * @param scene Scene, to which the information from the file shell be added.
     * @param file Path to the scene file.
     * @throws IOException If file is not readable.
     * @throws SAXException If file format is not valid.
     */
    public SceneFileLoader(Scene scene, String file) throws SAXException, IOException {
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser;
            saxParser = factory.newSAXParser();
            saxParser.parse(file, new SceneFileContentHandler(scene));
        } catch (ParserConfigurationException e) {
            System.err.println("An internal error has occured");
            e.printStackTrace();
        }
        
    }
    
}

