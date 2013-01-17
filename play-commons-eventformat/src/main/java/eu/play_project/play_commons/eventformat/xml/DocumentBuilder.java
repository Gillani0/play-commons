package eu.play_project.play_commons.eventformat.xml;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;

/**
 * Class provided to ease the creation of {@link Document}.
 * 
 * @author lpellegr
 */
public class DocumentBuilder {

    public static Document createDocument() {
        DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();
        javax.xml.parsers.DocumentBuilder docBuilder = null;

        try {
            docBuilder = dbfac.newDocumentBuilder();
            return docBuilder.newDocument();
        } catch (ParserConfigurationException e) {
            throw new IllegalStateException(e);
        }
    }

}
