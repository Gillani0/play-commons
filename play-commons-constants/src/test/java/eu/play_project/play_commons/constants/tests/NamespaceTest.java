package eu.play_project.play_commons.constants.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.net.URI;
import java.net.URISyntaxException;

import org.junit.Test;

import eu.play_project.play_commons.constants.Stream;

public class NamespaceTest {

	/**
	 * This test checks if there are sane stream IDs defined.
	 * 
	 * @throws URISyntaxException
	 */
	@Test
	public void testGetStreamIds() throws URISyntaxException {
		assertEquals("Check if the '#stream' suffix is not lost in the QName.",
				"FacebookCepResults#stream", Stream.FacebookCepResults
						.getQName().getLocalPart());
		// Test if no syntax exceptions are thrown:
		for (Stream s : Stream.values()) {
			new URI(s.getUri());
		}
	}

	/**
	 * Check if the topic IDs are properly created from the streams.
	 */
	@Test
	public void testGetTopicIds() {
		for (Stream s : Stream.values()) {
			String longer = s.getQName().toString();
			String shorter = s.getTopicQName().toString();
			assertTrue(longer.startsWith(shorter));
		}
	}

}
