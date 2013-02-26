package org.ow2.play.commons.accesscontrol;

import java.io.IOException;

import org.ontoware.rdf2go.exception.ModelRuntimeException;
import org.ontoware.rdf2go.impl.jena.ModelImplJena;
import org.ontoware.rdf2go.model.Model;
import org.ontoware.rdf2go.model.Syntax;
import org.ow2.play.commons.accesscontrol.api.Access;
import org.ow2.play.commons.accesscontrol.api.AccessManagementException;
import org.ow2.play.commons.accesscontrol.api.Permission;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hp.hpl.jena.ontology.OntModelSpec;

import eu.play_project.play_commons.constants.Constants;
import eu.play_project.play_commons.constants.Namespace;

public class AccessImpl implements Access {

	private static String METADATA_SERVICE =
			Constants.getProperties().getProperty("governance.metadataservice.endpoint");
	private String PREFIXES;
	private Model permissions;
	private final Logger logger;

	public AccessImpl() {
		this.logger = LoggerFactory.getLogger(this.getClass());
		this.PREFIXES = "";
		for (Namespace ns : new Namespace[] { Namespace.RDFS, Namespace.FOAF,
				Namespace.SIOC, Namespace.STREAMS, Namespace.GROUP,
				Namespace.PERMISSION, Namespace.ACL }) {
			this.PREFIXES += String.format("@prefix %s: <%s> . \n", ns.getPrefix(), ns.getUri());
		}

	}

	@Override
	public boolean check(String agentUri, String documentUri, Permission permission) {

		return checkViaGroupMembership(agentUri, documentUri, permission)
				|| checkDirectPermission(agentUri, documentUri, permission)
				|| checkViaClassMembership(agentUri, documentUri, permission)
				|| checkViaGroupClassMembership(agentUri, documentUri, permission);
	}

	private boolean checkViaGroupMembership(String agentUri, String documentUri, Permission permission) {
		final String queryTemplate = "ASK WHERE {<%1$s> sioc:member_of ?group . [ acl:agent ?group ; acl:accessTo <%2$s> ; acl:mode <%3$s> ]}";
		String query = String.format(queryTemplate, agentUri, documentUri, permission.toString());
		return permissions.sparqlAsk(PREFIXES + query);
	}

	private boolean checkDirectPermission(String agentUri, String documentUri, Permission permission) {
		return false;
	}

	private boolean checkViaClassMembership(String agentUri, String documentUri, Permission permission) {
		return false;
	}

	private boolean checkViaGroupClassMembership(String agentUri, String documentUri, Permission permission) {
		return false;
	}

	@Override
	public void flushPermissions() throws AccessManagementException {
		// Do some Jena magic because we need specific reasoning:

		/**
		 * We need some OWL inferencing here e.g., to infer inverse properties such as
		 * sioc group membership and infer superclasses of foaf agents.
		 */
		Model permissions = new ModelImplJena(com.hp.hpl.jena.rdf.model.ModelFactory
				.createOntologyModel(OntModelSpec.OWL_DL_MEM_RULE_INF));
		permissions.open();

		flushPermissions(permissions);
	}

	/**
	 * A variant where a pre-exisiting {@linkplain Model} containing permissions can
	 * be supplied. Useful e.g., in Unit Testing.
	 */
	public void flushPermissions(Model permissions) throws AccessManagementException {
		if (this.permissions != null) {
			this.permissions.close();
		}
		this.permissions = permissions;
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
