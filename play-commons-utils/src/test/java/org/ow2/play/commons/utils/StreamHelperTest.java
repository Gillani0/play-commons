/**
 * 
 */
package org.ow2.play.commons.utils;

import javax.xml.namespace.QName;

import junit.framework.TestCase;

/**
 * @author chamerling
 * 
 */
public class StreamHelperTest extends TestCase {

	public void testGetStreamNameFromNull() throws Exception {
		assertEquals("", StreamHelper.getStreamName(null));
	}
	
	public void testGetStreamNameNoTrailingSlash() throws Exception {
		assertEquals("http://play.eu/Stream", StreamHelper.getStreamName(QName.valueOf("{http://play.eu}Stream")));
	}
	
	public void testGetStreamNameWithTrailingSlash() throws Exception {
		assertEquals("http://play.eu/Stream", StreamHelper.getStreamName(QName.valueOf("{http://play.eu/}Stream")));
	}

}
