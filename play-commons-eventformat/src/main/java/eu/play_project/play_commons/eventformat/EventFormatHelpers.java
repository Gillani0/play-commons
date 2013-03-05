package eu.play_project.play_commons.eventformat;

import static eu.play_project.play_commons.constants.Event.WSN_MSG_DEFAULT_SYNTAX;
import static eu.play_project.play_commons.constants.Event.WSN_MSG_ELEMENT;
import static eu.play_project.play_commons.constants.Event.WSN_MSG_GRAPH_ATTRIBUTE;
import static eu.play_project.play_commons.constants.Event.WSN_MSG_SYNTAX_ATTRIBUTE;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringEscapeUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import eu.play_project.play_commons.constants.Namespace;
import eu.play_project.play_commons.eventformat.xml.DocumentBuilder;

/**
 * Shared methods to process RDF events in PLAY.
 * 
 * @author lpellegr
 * @author stuehmer
 * @author ialshaba
 */
public class EventFormatHelpers {

    private static Map<String, String> NS_PREFIX_MAP;
    private static String NS_PREFIX_ABREVIATE;

    // This instance must be only used to create XML elements
    // and no element must be appended to it
    private static Document DOCUMENT = DocumentBuilder.createDocument();

    private static String NOT_VALID_NATIVE_MESSAGE_MSG = "Specified element is not a valid native message element";
    
    private static Pattern UNWRAP_NATIVE_MESSAGE_PATTERN =
            Pattern.compile("<" + WSN_MSG_ELEMENT.getPrefix() + ":" + WSN_MSG_ELEMENT.getLocalPart()
                            + "[^<]*>(.*?)</" + WSN_MSG_ELEMENT.getPrefix() + ":"
                            + WSN_MSG_ELEMENT.getLocalPart() + ">", Pattern.MULTILINE | Pattern.DOTALL);

    /**
     * Get the prefix as an abbreviate String format like in TriG and Turtle.
     * 
     * @return prefix map as String.
     */
    public synchronized static String getNsPrefixAbbreviate() {
        if (NS_PREFIX_ABREVIATE == null) {
            Map<String, String> map = EventFormatHelpers.getNsPrefixMap();
            Iterator<Entry<String, String>> it = map.entrySet().iterator();
            StringBuilder sb = new StringBuilder();
            
            while (it.hasNext()) {
                Entry<String, String> pairs = it.next();
                sb.append("@prefix ");
                sb.append(pairs.getKey());
                sb.append(": <");
                sb.append(pairs.getValue());
                sb.append("> .\n");
            }
            
            NS_PREFIX_ABREVIATE = sb.toString();
        }
        
        return NS_PREFIX_ABREVIATE;
    }

    /**
     * Create a map of often used namespaces for events in PLAY.
     * 
     * @return a map of often used namespaces in PLAY for use with Jena.
     */
    public synchronized static Map<String, String> getNsPrefixMap() {
        if (NS_PREFIX_MAP == null) {
            NS_PREFIX_MAP = new HashMap<String, String>();
            for (Namespace ns : Namespace.values()) {
                NS_PREFIX_MAP.put(ns.getPrefix(), ns.getUri());
            }
        }

        return NS_PREFIX_MAP;
    }

    /**
     * Wraps in a native message XML element a semantic payload that uses
     * quadruples' serialization format (TriG). This method also
     * XML-escapes the string body.
     * 
     * @param semanticPayload
     *            the payload to wrap.
     * 
     * @return the wrapped payload.
     */
    public static String wrapWithNativeMessageElement(String semanticPayload) {
    	return wrapWithNativeMessageElement(semanticPayload, WSN_MSG_DEFAULT_SYNTAX, null);
	}

    /**
     * Wraps in a native message XML element a semantic payload that uses
     * quadruples' serialization format (e.g. TriG, NQuads, etc.). This method also
     * XML-escapes the string body.
     * 
     * @param semanticPayload
     *            the payload to wrap.
	 * @param syntax
	 *            the MIME type of the RDF serialization format used by the
	 *            payload. For possible values see
	 *            {@linkplain org.openrdf.rio.RDFFormat},
	 *            {@linkplain org.openjena.riot.WebContent} or
	 *            {@linkplain org.ontoware.rdf2go.model.Syntax}.
     * 
     * @return the wrapped payload.
     */
    public static String wrapWithNativeMessageElement(String semanticPayload,
                                                      String syntax) {
        return wrapWithNativeMessageElement(semanticPayload, syntax, null);
    }

    /**
	 * Wraps in a native message XML element a semantic payload that uses
	 * triples' serialization format (e.g. RDF/XML, N-Triple, Turtle, etc.). In
	 * that case a graph value has to be provided in order to indicate the
	 * source. This method also XML-escapes the string body.
	 * 
	 * @param semanticPayload
	 *            the payload to wrap.
	 * @param syntax
	 *            the MIME type of the RDF serialization format used by the
	 *            payload. For possible values see
	 *            {@linkplain org.openrdf.rio.RDFFormat},
	 *            {@linkplain org.openjena.riot.WebContent} or
	 *            {@linkplain org.ontoware.rdf2go.model.Syntax}.
	 * @param graphValue
	 *            if {@code semanticPayload} is in a triple format, the graph
	 *            name can be supplied here.
	 * 
	 * @return the wrapped payload.
	 */
    public static String wrapWithNativeMessageElement(String semanticPayload,
                                                      String syntax,
                                                      String graphValue) {
        StringBuilder result = new StringBuilder();
		result.append('<');
		result.append(WSN_MSG_ELEMENT.getPrefix());
		result.append(":");
		result.append(WSN_MSG_ELEMENT.getLocalPart());
		result.append(" xmlns:");
        result.append(WSN_MSG_ELEMENT.getPrefix());
        result.append("=\"");
        result.append(WSN_MSG_ELEMENT.getNamespaceURI());
        result.append("\" ");
        result.append(WSN_MSG_ELEMENT.getPrefix());
        result.append(":");
        result.append(WSN_MSG_SYNTAX_ATTRIBUTE);
        result.append("=\"");
        result.append(syntax);
        result.append("\"");

        if (graphValue != null) {
            result.append(' ');
            result.append(WSN_MSG_ELEMENT.getPrefix());
            result.append(":");
            result.append(WSN_MSG_GRAPH_ATTRIBUTE);
            result.append("=\"");
            result.append(graphValue);
            result.append("\"");
        }

        result.append(">\n");
        result.append("  ");
        result.append(StringEscapeUtils.escapeXml(semanticPayload.replaceAll("\n", "\n  ")));
        result.append("\n</");
        result.append(WSN_MSG_ELEMENT.getPrefix());
        result.append(":nativeMessage>");

        return result.toString();
    }
    
    /**
     * Wraps in a native message XML element a semantic payload that uses
     * quadruples' serialization format (TriG). This method also
     * XML-escapes the string body.
     * 
     * @param semanticPayload
     *            the payload to wrap.
     * 
     * @return the wrapped payload as an XML element.
     */
    public static Element wrapWithDomNativeMessageElement(String semanticPayload) {
        return wrapWithDomNativeMessageElement(semanticPayload, WSN_MSG_DEFAULT_SYNTAX, null);
    }

    /**
     * Wraps in a native message XML element a semantic payload that uses
     * quadruples' serialization format (e.g. TriG, NQuads, etc.). This method also
     * XML-escapes the string body.
     * 
     * @param semanticPayload
     *            the payload to wrap.
     * @param syntax
     *            the MIME type of the RDF serialization format used by the
     *            payload. For possible values see
     *            {@linkplain org.openrdf.rio.RDFFormat},
     *            {@linkplain org.openjena.riot.WebContent} or
     *            {@linkplain org.ontoware.rdf2go.model.Syntax}.
     * 
     * @return the wrapped payload as an XML element.
     */
    public static Element wrapWithDomNativeMessageElement(String semanticPayload,
                                                      String syntax) {
        return wrapWithDomNativeMessageElement(semanticPayload, syntax, null);
    }
    
    /**
     * Wraps in a native message XML element a semantic payload that uses
     * triples' serialization format (e.g. RDF/XML, N-Triple, Turtle, etc.). In
     * that case a graph value has to be provided in order to indicate the
     * source. This method also XML-escapes the string body.
     * 
     * @param semanticPayload
     *            the payload to wrap.
     * @param syntax
     *            the MIME type of the RDF serialization format used by the
     *            payload. For possible values see
     *            {@linkplain org.openrdf.rio.RDFFormat},
     *            {@linkplain org.openjena.riot.WebContent} or
     *            {@linkplain org.ontoware.rdf2go.model.Syntax}.
     * @param graphValue
     *            if {@code semanticPayload} is in a triple format, the graph
     *            name can be supplied here.
     * 
     * @return the wrapped payload as an XML element.
     */
    public static Element wrapWithDomNativeMessageElement(String semanticPayload,
                                                          String syntax,
                                                          String graphValue) {
        Element result =
                DOCUMENT.createElementNS(
                        WSN_MSG_ELEMENT.getNamespaceURI(),
                        WSN_MSG_ELEMENT.getPrefix() + ":"
                                + WSN_MSG_ELEMENT.getLocalPart());
        
        result.setTextContent(semanticPayload);
        result.setAttributeNS(
                WSN_MSG_ELEMENT.getNamespaceURI(),
                WSN_MSG_ELEMENT.getPrefix() + ":"
                        + WSN_MSG_SYNTAX_ATTRIBUTE, syntax);
        
        if (graphValue != null) {
            result.setAttributeNS(
                    WSN_MSG_ELEMENT.getNamespaceURI(),
                    WSN_MSG_ELEMENT.getPrefix() + ":"
                            + WSN_MSG_GRAPH_ATTRIBUTE, graphValue);
        }
        
        return result;
    }

    /**
     * Unwraps a native message element to return the string body of the message.
     * The body is also XML-unescaped.
     * 
     * @param xmlPayload the payload to unwrap.
     * 
     * @return the unwrapped and unescaped payload.
     */
    public static String unwrapFromNativeMessageElement(String xmlPayload) {
        Matcher m = UNWRAP_NATIVE_MESSAGE_PATTERN.matcher(xmlPayload);

        if (m.find()) {
        	return StringEscapeUtils.unescapeXml(m.group(1));
        } else {
        	return xmlPayload;
        }
    }
    
    /**
     * Unwraps a native message element to return the string body of the message.
     * The body is also XML-unescaped.
     * 
     * @param nativeMessageElt the payload to unwrap.
     * 
     * @return the unwrapped and unescaped payload.
     */
    public static String unwrapFromDomNativeMessageElement(Element nativeMessageElt) {
        if (nativeMessageElt.getNamespaceURI().equals(WSN_MSG_ELEMENT.getNamespaceURI())
                && nativeMessageElt.getLocalName().equals(WSN_MSG_ELEMENT.getLocalPart())
                && !nativeMessageElt.getAttributeNS(WSN_MSG_ELEMENT.getNamespaceURI(), WSN_MSG_SYNTAX_ATTRIBUTE).isEmpty()) {
            return StringEscapeUtils.unescapeXml(nativeMessageElt.getTextContent());
        }
        
        throw new IllegalArgumentException(NOT_VALID_NATIVE_MESSAGE_MSG);
    }
    
    /**
     * Extracts the syntax used by the given {@code nativeMessageElt}.
     * 
     * @param nativeMessageElt the native message element to parse.
     * 
     * @return the syntax used.
     */
    public static String getSyntaxFromDomNativeMessageElement(Element nativeMessageElt) {
        if (nativeMessageElt.getNamespaceURI().equals(WSN_MSG_ELEMENT.getNamespaceURI())
                && nativeMessageElt.getLocalName().equals(WSN_MSG_ELEMENT.getLocalPart())) {
            String syntax =
                    nativeMessageElt.getAttributeNS(
                            WSN_MSG_ELEMENT.getNamespaceURI(), WSN_MSG_SYNTAX_ATTRIBUTE);
            
            if (syntax != null) {
                return syntax;
            }
        }
        
        throw new IllegalArgumentException(NOT_VALID_NATIVE_MESSAGE_MSG);
    }
    
    /**
     * Extracts the graph value specified with {@code nativeMessageElt}.
     * 
     * @param nativeMessageElt the native message element to parse.
     * 
     * @return the graph value or {@code null}.
     */
    public static String getGraphFromDomNativeMessageElement(Element nativeMessageElt) {
        if (nativeMessageElt.getNamespaceURI().equals(WSN_MSG_ELEMENT.getNamespaceURI())
                && nativeMessageElt.getLocalName().equals(WSN_MSG_ELEMENT.getLocalPart())
                && !nativeMessageElt.getAttributeNS(WSN_MSG_ELEMENT.getNamespaceURI(), WSN_MSG_SYNTAX_ATTRIBUTE).isEmpty()) {
            String graphValue = nativeMessageElt.getAttributeNS(
                                    WSN_MSG_ELEMENT.getNamespaceURI(), WSN_MSG_GRAPH_ATTRIBUTE);
            
            if (graphValue.isEmpty()) {
                return null;
            }
            
            return graphValue;
        }
        
        throw new IllegalArgumentException(NOT_VALID_NATIVE_MESSAGE_MSG);
    }
}
