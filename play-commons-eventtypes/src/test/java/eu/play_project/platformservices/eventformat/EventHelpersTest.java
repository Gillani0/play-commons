package eu.play_project.platformservices.eventformat;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.ontoware.rdf2go.model.Model;
import org.ontoware.rdf2go.model.ModelSet;

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
		System.out.println("context:" + m.getContextURI() + uri);
		assertEquals(
				"The expected context URI was not properly added to the model.",
				uri, m.getContextURI().toString());
		assertTrue("Some namespaces should be defined for PLAY.", !m
				.getNamespaces().isEmpty());
		assertTrue("The new Model should be empty.", m.isEmpty());
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
	}

}
