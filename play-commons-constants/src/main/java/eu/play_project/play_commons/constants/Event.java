package eu.play_project.play_commons.constants;

import javax.xml.namespace.QName;

public class Event {
	
	/**
	 * Event IDs usually end in this special suffix (URI fragment identifier). 
	 */
	public static final String EVENT_ID_SUFFIX = "#event";

	/**
	 * In EP-SPARQL queries the ID of an unfinished complex event is depicted by
	 * this string before it is replaced by a unique identifyer.
	 */
	public static final String EVENT_ID_PLACEHOLDER = Namespace.TYPES.getUri() + "e";
	
    /*
     * Some constants for wrapping the PLAY events in WS-Notification:
     */
	public static final String WSN_MSG_NS = "http://www.event-processing.org/wsn/msgtype/";
    public static final QName WSN_MSG_ELEMENT = new QName(WSN_MSG_NS, "nativeMessage", "mt");    
    public static final String WSN_MSG_GRAPH_ATTRIBUTE = "graph";
    public static final String WSN_MSG_SYNTAX_ATTRIBUTE = "syntax";
    public static final String WSN_MSG_DEFAULT_SYNTAX = "application/x-trig";
    
    /**
     * Date format to be used with UTC information in events with {@code xsd:date}.
     */
	public static final String DATE_FORMAT_8601 = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";


}
