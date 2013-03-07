package org.ow2.play.commons.accesscontrol.tests;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.ontoware.rdf2go.exception.ModelRuntimeException;
import org.ontoware.rdf2go.impl.jena.ModelImplJena;
import org.ontoware.rdf2go.model.Model;
import org.ontoware.rdf2go.model.Syntax;

import com.hp.hpl.jena.ontology.OntModelSpec;


public class AccessControlRdfJsonParserTest {

	/**
	 * Fetch and parse RDF/JSON from a specified URL. The RDF is reformatted to Turtle syntax and dumped on stdout.
	 * 
	 * @param args The first parameter {@code args[0]} can be a fully qualified URL to fetch some RDF/JSON.
	 * @throws ModelRuntimeException
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	public static void main(String[] args) throws ModelRuntimeException, MalformedURLException, IOException {
		String url;
		if (args[0] != null) {
			url = args[0];
		}
		else {
			url = "https://gist.github.com/chamerling/5108319/raw/776b5fc2d9badb3d440ac3dee959eade18b42031/stream-group.json";
		}
		Model m;
		m = getPermissions(new URL(url));
		m.dump();
	}

	
	private static Model getPermissions(URL url) throws ModelRuntimeException, IOException {

		Model permissions = new ModelImplJena(com.hp.hpl.jena.rdf.model.ModelFactory
				.createOntologyModel(OntModelSpec.OWL_DL_MEM_RULE_INF));
		permissions.open();

		permissions.readFrom(url.openStream(), Syntax.RdfJson);
	
		return permissions;
	}
}
