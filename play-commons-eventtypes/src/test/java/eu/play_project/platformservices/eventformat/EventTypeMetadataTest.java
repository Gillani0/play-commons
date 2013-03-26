package eu.play_project.platformservices.eventformat;

import static eu.play_project.play_commons.constants.Event.EVENT_ID_SUFFIX;
import static eu.play_project.play_commons.constants.Namespace.EVENTS;
import static eu.play_project.play_commons.constants.Namespace.TYPES;
import static org.junit.Assert.assertEquals;

import org.event_processing.events.types.Event;
import org.event_processing.events.types.TwitterEvent;
import org.junit.Assert;
import org.junit.Test;
import org.ontoware.rdf2go.RDF2Go;
import org.ontoware.rdf2go.model.Model;
import org.ontoware.rdf2go.model.node.Resource;
import org.ontoware.rdf2go.model.node.URI;
import org.ontoware.rdf2go.model.node.impl.URIImpl;
import org.ontoware.rdf2go.vocabulary.RDF;

import eu.play_project.play_commons.eventtypes.EventHelpers;
import eu.play_project.play_commons.eventtypes.EventTypeMetadata;

public class EventTypeMetadataTest {

	private final String eventId = EVENTS.getUri() + "1234";
	private final URI GRAPHNAME = new URIImpl(eventId);
	// Subjects
	private final Resource SUBJECT = new URIImpl(eventId + EVENT_ID_SUFFIX);
	private final Resource OTHER_SUBJECT = new URIImpl(eventId + "bogus");
	
	// Objects
	private final Resource EVENT_TYPE_1 = new URIImpl(TYPES.getUri() + "Type1");
	private final Resource EVENT_TYPE_2 = new URIImpl(TYPES.getUri() + "Type2");
	private final Resource EVENT_TYPE_DEFAULT = Event.RDFS_CLASS;

	@Test
	public void testEventTypeMetadata() {
		String iconUrl = EventTypeMetadata.getEventTypeIcon(TwitterEvent.RDFS_CLASS);
		Assert.assertEquals(String.format("Got the wrong icon for TwitterEvent: '%s'", iconUrl), "http://twitter.com/favicon.ico", iconUrl);
	}

	@Test
	public void testGetEventTypeByClass() {
		String eventId = EventHelpers.createRandomEventId();
		TwitterEvent event = new TwitterEvent(EventHelpers.createEmptyModel(eventId),
				eventId + EVENT_ID_SUFFIX, true);
		
		String returnedType = EventTypeMetadata.getEventType(event);

		Assert.assertEquals(String.format("Got the wrong type for TwitterEvent: '%s'", returnedType), TwitterEvent.RDFS_CLASS.toString(), returnedType);
	}
	
	/**
	 * Test an event with proper subject.
	 */
	@Test
	public void testGetEventTypeWithSubject() {
		Model rdf = RDF2Go.getModelFactory().createModel(GRAPHNAME).open();

		rdf.addStatement(
				SUBJECT,
				RDF.type,
				EVENT_TYPE_1);

		rdf.addStatement(
				OTHER_SUBJECT,
				RDF.type,
				EVENT_TYPE_2);

		assertEquals(EVENT_TYPE_1.toString(), EventTypeMetadata.getEventType(rdf));
	}
	
	/**
	 * Test an event with no proper subject.
	 */
	@Test
	public void testGetEventTypeWithoutSubject() {
		Model rdf = RDF2Go.getModelFactory().createModel(GRAPHNAME).open();

		rdf.addStatement(
				OTHER_SUBJECT,
				RDF.type,
				EVENT_TYPE_2);
		
		assertEquals(EVENT_TYPE_2.toString(), EventTypeMetadata.getEventType(rdf));
	}

	/**
	 * Test an event with no type declaration whatsoever: the default event type
	 * should be returned.
	 */
	@Test
	public void testGetEventTypeWithoutType() {
		Model rdf = RDF2Go.getModelFactory().createModel(GRAPHNAME).open();

		rdf.addStatement(
				OTHER_SUBJECT,
				RDF.first, // arbitrary predicate
				EVENT_TYPE_2);
		
		assertEquals(EVENT_TYPE_DEFAULT.toString(), EventTypeMetadata.getEventType(rdf));
	}

}