package eu.play_project.play_commons.constants.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import javax.xml.namespace.QName;

import org.junit.Test;

import eu.play_project.play_commons.constants.Stream;

public class StreamTest {
	
	@Test
	public void testStreamToTopicString() {
		String id1, id2;
		id1 = Stream.SituationalEventStream.getTopicUri();
		id2 = Stream.toTopicUri(Stream.SituationalEventStream.getUri());
		
		assertEquals("Check for consistent output.", id1, id2);
	}
	
	@Test
	public void testStreamToTopicQname() {
		QName id1, id2;
		id1 = Stream.SituationalEventStream.getTopicQName();
		id2 = Stream.toTopicQName((Stream.SituationalEventStream.getQName()));
		
		assertEquals("Check for consistent output.", id1, id2);
	}
	
	@Test
	public void testStreamUriSuffix() {
		String streamUri = "http://something/other#stream";

		assertTrue("Topic URIs must not contain the #stream suffix.", !Stream.toTopicUri(streamUri).contains(Stream.STREAM_ID_SUFFIX));
	}

	@Test
	public void testStreamQNameSuffix() {
		QName streamUri = new QName("http://something/", "other#stream", "sth");

		assertTrue("Topic URIs must not contain the #stream suffix.", !Stream.toTopicQName(streamUri).toString().contains(Stream.STREAM_ID_SUFFIX));
	}

}
