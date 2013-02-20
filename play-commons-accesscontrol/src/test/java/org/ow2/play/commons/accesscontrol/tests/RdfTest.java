package org.ow2.play.commons.accesscontrol.tests;

import java.io.IOException;

import org.junit.Test;
import org.ontoware.rdf2go.exception.ModelRuntimeException;
import org.ontoware.rdf2go.impl.jena29.ModelImplJena26;
import org.ontoware.rdf2go.model.Model;
import org.ontoware.rdf2go.model.QueryRow;
import org.ontoware.rdf2go.model.Syntax;

import com.hp.hpl.jena.ontology.OntModelSpec;

public class RdfTest {

	static final String PREFIXES = "prefix acl: <http://www.w3.org/ns/auth/acl#> " + 
			"prefix foaf: <http://xmlns.com/foaf/0.1/> " +
			"prefix rdfs:    <http://www.w3.org/2000/01/rdf-schema#>" +
			"prefix sioc: <http://rdfs.org/sioc/ns#> " +
			"prefix s:       <http://streams.event-processing.org/ids/> " +
			"prefix roland: <http://www.roland-stuehmer.de/foaf.rdf#> ";
	
	@Test
	public void testRdfFromFile() throws ModelRuntimeException, IOException {
		
		// Do some Jena magic because we need specific reasoning:
		Model rdf = new ModelImplJena26(com.hp.hpl.jena.rdf.model.ModelFactory
		        .createOntologyModel(OntModelSpec.OWL_DL_MEM_RULE_INF));
		rdf.open();
		
		rdf.readFrom(this.getClass().getClassLoader().getResourceAsStream("vocabularies/foaf.rdf"), Syntax.RdfXml);
		rdf.readFrom(this.getClass().getClassLoader().getResourceAsStream("vocabularies/sioc.rdf"), Syntax.RdfXml);
		rdf.readFrom(this.getClass().getClassLoader().getResourceAsStream("vocabularies/acl.rdf"), Syntax.RdfXml);
		rdf.readFrom(this.getClass().getClassLoader().getResourceAsStream("permissions.ttl"), Syntax.Turtle);
		
		System.out.println("Read:");
		for (QueryRow qr : rdf.sparqlSelect(PREFIXES + "SELECT * WHERE {[acl:accessTo <urn:card>; acl:mode acl:Read;  acl:agent ?s]}")) {
			System.out.format("%s %s%n", qr.getValue("s"), qr.getValue("p"));
		}
		
		System.out.println("Write:");
		for (QueryRow qr : rdf.sparqlSelect(PREFIXES + "SELECT * WHERE {[acl:accessTo <urn:card>; acl:mode acl:Write;  acl:agent ?s]}")) {
			System.out.format("%s %s%n", qr.getValue("s"), qr.getValue("p"));
		}
		
		System.out.println("Append:");
		for (QueryRow qr : rdf.sparqlSelect(PREFIXES + "SELECT * WHERE { {[acl:accessTo <urn:card>; acl:mode acl:Append;  acl:agent ?s]} UNION {?s2 rdfs:type ?c . [acl:accessTo <urn:card>; acl:mode acl:Append;  acl:agentClass ?c]}}")) {
			System.out.format("%s %s %s%n", qr.getValue("s"), qr.getValue("c"),  qr.getValue("s2"));
		}
		
		System.out.println("Append for group:");
		for (QueryRow qr : rdf.sparqlSelect(PREFIXES + "SELECT * WHERE {?s sioc:member_of ?s2 . [acl:accessTo <urn:card>; acl:mode acl:Append;  acl:agent ?s2]}")) {
			System.out.format("%s %s%n", qr.getValue("c"), qr.getValue("s"));
		}
		
		System.out.println("Trial:");
		for (QueryRow qr : rdf.sparqlSelect(PREFIXES + "SELECT * WHERE {?s sioc:has_member ?s2 }")) {
			System.out.format("%s %s%n", qr.getValue("s"), qr.getValue("s2"));
		}
	
	}
}
