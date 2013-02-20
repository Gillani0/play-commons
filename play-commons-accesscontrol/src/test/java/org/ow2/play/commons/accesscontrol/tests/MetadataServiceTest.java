package org.ow2.play.commons.accesscontrol.tests;

import org.junit.Test;
import org.ow2.play.commons.accesscontrol.Acl;

public class MetadataServiceTest {

	@Test
	public void test() {
		Acl acl = new Acl();
		acl.check(null, null, null);
		
		//<urn:acl:1> acl:accessTo <http://streams.event-processing.org/ids/activityEvent>
		//<urn:acl:1> acl:mode acl:Read
		//<urn:acl:1> acl:agent <http://www.roland-stuehmer.de/foaf.rdf#me>
	}
}
