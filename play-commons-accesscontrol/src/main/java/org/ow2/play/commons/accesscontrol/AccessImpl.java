package org.ow2.play.commons.accesscontrol;

import java.io.IOException;

import org.ontoware.rdf2go.exception.ModelRuntimeException;
import org.ontoware.rdf2go.impl.jena.ModelImplJena;
import org.ontoware.rdf2go.model.Model;
import org.ontoware.rdf2go.model.Syntax;
import org.ow2.play.commons.accesscontrol.api.Access;
import org.ow2.play.commons.accesscontrol.api.AccessManagementException;
import org.ow2.play.commons.accesscontrol.api.Permission;

import com.hp.hpl.jena.ontology.OntModelSpec;

import eu.play_project.play_commons.constants.Namespace;

/**
 * This class fetches permission data from a Web Service and validates
 * permissions based on such ACL information.
 * 
 * @author Roland Stühmer
 * 
 */
public class AccessImpl implements Access {

	private static String PREFIXES;
	private Model permissions;
	
	static {
		PREFIXES = "";
		for (Namespace ns : new Namespace[] { Namespace.RDF, Namespace.FOAF,
				Namespace.SIOC, Namespace.STREAMS, Namespace.GROUP,
				Namespace.PERMISSION, Namespace.ACL }) {
			PREFIXES += String.format("prefix %s: <%s> \n", ns.getPrefix(), ns.getUri());
		}
	}

	public AccessImpl() throws AccessManagementException {
		
		flushPermissions();
	}

	/**
	 * A contructor where a pre-exisiting {@linkplain Model} containing permissions
	 * can be supplied. This constructor does not connect to a PLAY service, useful
	 * e.g., in Unit Testing.
	 */
	public AccessImpl(Model permissions) throws AccessManagementException {

		flushPermissions(permissions);
	}

	
	@Override
	public boolean check(String agentUri, String documentUri, Permission permission) {

		// TODO stuehmer possibly introduce a check if permissions are old and flush
		
		return checkViaGroupMembership(agentUri, documentUri, permission)
				|| checkDirectPermission(agentUri, documentUri, permission)
				|| checkViaAgentClass(agentUri, documentUri, permission);
	}

	public boolean checkViaGroupMembership(String agentUri, String documentUri, Permission permission) {
		final String queryTemplate = "ASK WHERE {<%1$s> sioc:member_of ?group . [ acl:agent ?group ; acl:accessTo <%2$s> ; acl:mode <%3$s> ]}";
		String query = String.format(queryTemplate, agentUri, documentUri, permission.toString());
		return permissions.sparqlAsk(PREFIXES + query);
	}

	public boolean checkDirectPermission(String agentUri, String documentUri, Permission permission) {
		final String queryTemplate = "ASK WHERE {[ acl:agent <%1$s> ; acl:accessTo <%2$s> ; acl:mode <%3$s> ]}";
		String query = String.format(queryTemplate, agentUri, documentUri, permission.toString());
		return permissions.sparqlAsk(PREFIXES + query);
	}

	public boolean checkViaAgentClass(String agentUri, String documentUri, Permission permission) {
		final String queryTemplate = "ASK WHERE {<%1$s> rdf:type ?class . [ acl:accessTo <%2$s> ; acl:mode <%3$s> ; acl:agentClass ?class ]}";
		String query = String.format(queryTemplate, agentUri, documentUri, permission.toString());
		return permissions.sparqlAsk(PREFIXES + query);
	}

	@Override
	public void flushPermissions() throws AccessManagementException {
		/**
		 * We need some OWL inferencing here e.g., to infer inverse properties such as
		 * sioc group membership, infer superclasses of foaf agents and make use of owl:sameAs.
		 */
		Model permissions = new ModelImplJena(com.hp.hpl.jena.rdf.model.ModelFactory
				.createOntologyModel(OntModelSpec.OWL_DL_MEM_RULE_INF));
		permissions.open();

		// TODO stuehmer: load permissions from PLAY service
		
		flushPermissions(permissions);
	}

	private void flushPermissions(Model newPermissions) throws AccessManagementException {
		if (this.permissions != null) {
			this.permissions.close();
		}
		this.permissions = newPermissions;
		try {
			this.permissions.readFrom(this.getClass().getClassLoader().getResourceAsStream("vocabularies/foaf.rdf"), Syntax.RdfXml);
			this.permissions.readFrom(this.getClass().getClassLoader().getResourceAsStream("vocabularies/sioc.rdf"), Syntax.RdfXml);
			this.permissions.readFrom(this.getClass().getClassLoader().getResourceAsStream("vocabularies/acl.rdf"), Syntax.RdfXml);
			this.permissions.readFrom(this.getClass().getClassLoader().getResourceAsStream("permissions.ttl"), Syntax.Turtle);
		} catch (ModelRuntimeException e) {
			throw new AccessManagementException("Error while parsing permission information from storage.", e);
		} catch (IOException e) {
			throw new AccessManagementException("IO error while fetching permission information from storage.", e);
		}
	}
}
