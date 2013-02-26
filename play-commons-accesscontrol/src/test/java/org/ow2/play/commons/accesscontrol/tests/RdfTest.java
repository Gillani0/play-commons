package org.ow2.play.commons.accesscontrol.tests;

import java.io.FileOutputStream;
import java.io.IOException;

import org.junit.Test;
import org.ontoware.rdf2go.RDF2Go;
import org.ontoware.rdf2go.exception.ModelRuntimeException;
import org.ontoware.rdf2go.impl.jena.ModelImplJena;
import org.ontoware.rdf2go.model.Model;
import org.ontoware.rdf2go.model.Syntax;

import com.hp.hpl.jena.ontology.OntModelSpec;

public class RdfTest {

	static final String PREFIXES = "prefix acl: <http://www.w3.org/ns/auth/acl#> " +
			"prefix foaf: <http://xmlns.com/foaf/0.1/> " +
			"prefix rdfs:    <http://www.w3.org/2000/01/rdf-schema#>" +
			"prefix sioc: <http://rdfs.org/sioc/ns#> " +
			"prefix s:       <http://streams.event-processing.org/ids/> " +
			"prefix roland: <http://www.roland-stuehmer.de/foaf.rdf#> " +
			"prefix notify:  <http://docs.oasis-open.org/wsn/bw-2/NotificationConsumer/> " +
			"prefix subscribe: <http://docs.oasis-open.org/wsn/bw-2/NotificationProducer/> ";

	@Test
	public void testCheckPermissions() throws ModelRuntimeException, IOException {
		Model permissions = new ModelImplJena(com.hp.hpl.jena.rdf.model.ModelFactory
				.createOntologyModel(OntModelSpec.OWL_DL_MEM_RULE_INF));
		permissions.open();

		permissions.readFrom(this.getClass().getClassLoader().getResourceAsStream("permissions-test.ttl"), Syntax.Turtle);

	}

	@Test
	public void testRdfFromFile() throws ModelRuntimeException, IOException {

		// Do some Jena magic because we need specific reasoning:
		Model permissions = new ModelImplJena(com.hp.hpl.jena.rdf.model.ModelFactory
				.createOntologyModel(OntModelSpec.OWL_DL_MEM_RULE_INF));
		permissions.open();

		permissions.readFrom(this.getClass().getClassLoader().getResourceAsStream("vocabularies/foaf.rdf"), Syntax.RdfXml);
		permissions.readFrom(this.getClass().getClassLoader().getResourceAsStream("vocabularies/sioc.rdf"), Syntax.RdfXml);
		permissions.readFrom(this.getClass().getClassLoader().getResourceAsStream("vocabularies/acl.rdf"), Syntax.RdfXml);
		permissions.readFrom(this.getClass().getClassLoader().getResourceAsStream("permissions.ttl"), Syntax.Turtle);

		Model test = RDF2Go.getModelFactory().createModel().open();
		test.readFrom(this.getClass().getClassLoader().getResourceAsStream("foaf.rdf"), Syntax.RdfXml);
		test.writeTo(new FileOutputStream(System.getProperty("java.io.tmpdir") + "foaf.json"), Syntax.RdfJson);


		//		System.out.println("Read:");
		//		for (QueryRow qr : rdf.sparqlSelect(PREFIXES + "SELECT * WHERE {[acl:accessTo <urn:card>; acl:mode acl:Read;  acl:agent ?s]}")) {
		//			System.out.format("%s %s%n", qr.getValue("s"), qr.getValue("p"));
		//		}
		//
		//		System.out.println("Write:");
		//		for (QueryRow qr : rdf.sparqlSelect(PREFIXES + "SELECT * WHERE {[acl:accessTo <urn:card>; acl:mode acl:Write;  acl:agent ?s]}")) {
		//			System.out.format("%s %s%n", qr.getValue("s"), qr.getValue("p"));
		//		}
		//
		//		System.out.println("Append:");
		//		for (QueryRow qr : rdf.sparqlSelect(PREFIXES + "SELECT * WHERE { {[acl:accessTo <urn:card>; acl:mode acl:Append;  acl:agent ?s]} UNION {?s2 rdfs:type ?c . [acl:accessTo <urn:card>; acl:mode acl:Append;  acl:agentClass ?c]}}")) {
		//			System.out.format("%s %s %s%n", qr.getValue("s"), qr.getValue("c"),  qr.getValue("s2"));
		//		}
		//
		//		System.out.println("Append for group:");
		//		for (QueryRow qr : rdf.sparqlSelect(PREFIXES + "SELECT * WHERE {?s sioc:member_of ?group . [acl:accessTo <urn:card>; acl:mode acl:Append;  acl:agent ?group]}")) {
		//			System.out.format("%s %s%n", qr.getValue("c"), qr.getValue("s"));
		//		}

		final String agent = "roland:me"; // %1$s
		final String document = "s:PersonalMonitoring"; // %2$s
		final String mode = "notify:Notify"; // %3$s
		System.out.println("Trial:");
		System.out.println(permissions.sparqlAsk(PREFIXES + String.format("ASK WHERE {%1$s sioc:member_of ?group . [ acl:accessTo %2$s ; acl:mode %3$s; acl:agent ?group ] }", agent, document, mode)));

	}
}
