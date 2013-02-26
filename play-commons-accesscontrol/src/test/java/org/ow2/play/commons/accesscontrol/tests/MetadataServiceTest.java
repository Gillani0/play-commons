package org.ow2.play.commons.accesscontrol.tests;

import org.junit.Test;
import org.ow2.play.commons.accesscontrol.AccessImpl;
import org.ow2.play.commons.accesscontrol.api.Access;

public class MetadataServiceTest {

	@Test
	public void test() {
		Access aclImpl = new AccessImpl();
		aclImpl.check(null, null, null);
		
		//<urn:acl:1> acl:accessTo <http://streams.event-processing.org/ids/activityEvent>
		//<urn:acl:1> acl:mode acl:Read
		//<urn:acl:1> acl:agent <http://www.roland-stuehmer.de/foaf.rdf#me>
	}
}
