package org.ow2.play.commons.accesscontrol.tests;

import java.io.IOException;

import junit.framework.Assert;

import org.junit.Test;
import org.ontoware.rdf2go.exception.ModelRuntimeException;
import org.ontoware.rdf2go.impl.jena.ModelImplJena;
import org.ontoware.rdf2go.model.Model;
import org.ontoware.rdf2go.model.Syntax;
import org.ow2.play.commons.accesscontrol.AccessImpl;
import org.ow2.play.commons.accesscontrol.api.AccessManagementException;
import org.ow2.play.commons.accesscontrol.api.Permission;

import com.hp.hpl.jena.ontology.OntModelSpec;

public class AccessControlTest {

	@Test
	public void testCheckDirectPermission() throws ModelRuntimeException, IOException, AccessManagementException {
		AccessImpl access = new AccessImpl(getPermissions());
		
		Assert.assertTrue(access.checkDirectPermission("urn:account01", "urn:stream01", Permission.Write));
		Assert.assertFalse(access.checkDirectPermission("urn:account02", "urn:stream01", Permission.Write));
			
	}

	@Test
	public void testCheckViaGroupMembership() throws ModelRuntimeException, IOException, AccessManagementException {
		AccessImpl access = new AccessImpl(getPermissions());
		
		Assert.assertTrue(access.checkViaGroupMembership("urn:account03", "urn:stream03", Permission.Read));
		Assert.assertTrue(access.checkViaGroupMembership("urn:account04", "urn:stream03", Permission.Read));
		Assert.assertFalse(access.checkViaGroupMembership("urn:account04", "urn:stream03", Permission.Write));
			
	}

	@Test
	public void testCheckViaAgentClass() throws ModelRuntimeException, IOException, AccessManagementException {
		AccessImpl access = new AccessImpl(getPermissions());
		
		Assert.assertTrue(access.checkViaAgentClass("urn:account05", "urn:stream04", Permission.Notify));
		Assert.assertFalse(access.checkViaAgentClass("urn:account06", "urn:stream04", Permission.Notify));
	}

	private Model getPermissions() throws ModelRuntimeException, IOException {

		Model permissions = new ModelImplJena(com.hp.hpl.jena.rdf.model.ModelFactory
				.createOntologyModel(OntModelSpec.OWL_DL_MEM_RULE_INF));
		permissions.open();

		permissions.readFrom(this.getClass().getClassLoader().getResourceAsStream("permissions-test.ttl"), Syntax.Turtle);
	
		return permissions;
	}
}
