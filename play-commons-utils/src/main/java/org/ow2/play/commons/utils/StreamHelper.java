/**
 * 
 */
package org.ow2.play.commons.utils;

import javax.xml.namespace.QName;

/**
 * @author chamerling
 * 
 */
public class StreamHelper {

	/**
	 * Get stream name from a topic name (QName)
	 * 
	 * @param topic
	 * @return
	 */
	public static final String getStreamName(QName topic) {
		if (topic == null) {
			return "";
		}
		
		String ns = topic.getNamespaceURI();
		if (ns == null) {
			ns = "";
		}
		if (!ns.endsWith("/") && ns.length() > 0) {
			ns = ns + "/";
		}
		return ns + topic.getLocalPart();
	}
}
