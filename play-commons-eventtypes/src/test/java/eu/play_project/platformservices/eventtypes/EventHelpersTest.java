package eu.play_project.platformservices.eventtypes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;

import org.junit.Test;
import org.ontoware.rdf2go.model.Model;
import org.ontoware.rdf2go.model.ModelSet;
import org.ontoware.rdf2go.model.node.impl.URIImpl;

import eu.play_project.play_commons.eventtypes.EventHelpers;

/**
 * Some tests for EventHelpers.
 * 
 * @author stuehmer
 * 
 */
public class EventHelpersTest {

	/**
	 * This test computes some sanity checks on
	 * {@linkplain EventHelpers#createEmptyModel(String)}.
	 */
	@Test
	public void testCreateEmptyModel() {
		String uri = "http://teststring";

		Model m = EventHelpers.createEmptyModel(uri);
		assertEquals(
				"The expected context URI was not properly added to the model.",
				uri, m.getContextURI().toString());
		assertTrue("Some namespaces should be defined for PLAY.", !m
				.getNamespaces().isEmpty());
		assertTrue("The new Model should be empty.", m.isEmpty());
		
		// Add at least one (dummy) statement to the model so that the graph is nonempty (might not be serialized otherwise)
		m.addStatement(new URIImpl("urn:something"), new URIImpl("urn:something"), new URIImpl("urn:something"));
		
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		EventHelpers.write(bout, m);
		String outputString = bout.toString();
		assertTrue("The expected context URI was not properly serialized to TriG syntax.", outputString.contains(uri));

		m.setNamespace("lala", "urn:lala");
		bout = new ByteArrayOutputStream();
		EventHelpers.write(bout, m);
		outputString = bout.toString();
		assertTrue("The expected namespace prefix was not properly serialized to TriG syntax.", outputString.contains("lala"));
	}

	/**
	 * This test computes some sanity checks on
	 * {@linkplain EventHelpers#createEmptyModelSet()}.
	 */
	@Test
	public void testCreateEmptyModelSet() {
		ModelSet m = EventHelpers.createEmptyModelSet();
		assertTrue("Some namespaces should be defined for PLAY.", !m
				.getNamespaces().isEmpty());
		assertTrue("The new ModelSet should be empty.", m.isEmpty());
		
		m.setNamespace("lala", "urn:lala");
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		EventHelpers.write(bout, m);
		assertTrue("The expected namespace prefix was not properly serialized to TriG syntax.", bout.toString().contains("lala"));
	}

}
