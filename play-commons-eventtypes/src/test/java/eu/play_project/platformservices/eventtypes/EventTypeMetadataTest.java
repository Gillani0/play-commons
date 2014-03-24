package eu.play_project.platformservices.eventtypes;

import static eu.play_project.play_commons.constants.Event.EVENT_ICON;
import static eu.play_project.play_commons.constants.Event.EVENT_ICON_DEFAULT;
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
import org.ontoware.rdf2go.model.node.URI;
import org.ontoware.rdf2go.model.node.impl.URIImpl;
import org.ontoware.rdf2go.vocabulary.RDF;

import eu.play_project.play_commons.constants.Stream;
import eu.play_project.play_commons.eventtypes.EventHelpers;
import eu.play_project.play_commons.eventtypes.EventTypeMetadata;

public class EventTypeMetadataTest {

	private final String eventId = EVENTS.getUri() + "1234";
	
	// Contexts
	private final URI GRAPHNAME = new URIImpl(eventId);
	
	// Subjects
	private final URI SUBJECT = new URIImpl(eventId + EVENT_ID_SUFFIX);
	private final URI OTHER_SUBJECT = new URIImpl(eventId + "bogus");
	
	// Predicates
	private final URI HAS_ICON = new URIImpl(EVENT_ICON);
	
	// Objects
	private final URI EVENT_TYPE_1 = new URIImpl(TYPES.getUri() + "Type1");
	private final URI EVENT_TYPE_2 = new URIImpl(TYPES.getUri() + "Type2");
	private final URI EVENT_TYPE_DEFAULT = Event.RDFS_CLASS;
	private final URI ICON = new URIImpl("http://domain.invalid/favicon.ico");
	private final URI OTHER_ICON = new URIImpl("http://domain.invalid/favicon2.ico");

	@Test
	public void testEventTypeMetadata() {
		String iconUrl = EventTypeMetadata.getIconForType(TwitterEvent.RDFS_CLASS);
		Assert.assertEquals(String.format("Got the wrong icon for TwitterEvent: '%s'", iconUrl), "http://twitter.com/favicon.ico", iconUrl);
	}

	@Test
	public void testGetEventTypeByClass() {
		String eventId = EventHelpers.createRandomEventId();
		TwitterEvent event = new TwitterEvent(EventHelpers.createEmptyModel(eventId),
				eventId + EVENT_ID_SUFFIX, true);
		
		String returnedType = EventTypeMetadata.getType(event);

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

		assertEquals(EVENT_TYPE_1.toString(), EventTypeMetadata.getType(rdf));
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
		
		assertEquals(EVENT_TYPE_2.toString(), EventTypeMetadata.getType(rdf));
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
		
		assertEquals(EVENT_TYPE_DEFAULT.toString(), EventTypeMetadata.getType(rdf));
	}
	
	@Test
	public void testGetEventId() {
		Event event;
		Model model;
		
		event = EventHelpers.builder(SUBJECT)
				.stream(Stream.TwitterFeed)
				.build();
		assertEquals(SUBJECT.toString(), EventTypeMetadata.getId(event));
		
		model = EventHelpers.createEmptyModel(SUBJECT);
		assertEquals(SUBJECT.toString() + EVENT_ID_SUFFIX, EventTypeMetadata.getId(model));
		
		model = RDF2Go.getModelFactory().createModel().open();
		model.addStatement(OTHER_SUBJECT, RDF.type, EVENT_TYPE_1);
		assertEquals(OTHER_SUBJECT.toString(), EventTypeMetadata.getId(model));

		model = RDF2Go.getModelFactory().createModel().open();
		// We expect the empty string
		assertEquals("", EventTypeMetadata.getId(model));

		
	}
	
	@Test
	public void testGetEventIcon() {
		Event event;
		
		event = EventHelpers.builder(SUBJECT)
				.stream(Stream.TwitterFeed)
				.addProperty(HAS_ICON, ICON)
				.addRdf(OTHER_SUBJECT, HAS_ICON, OTHER_ICON)
				.build();
		// We expect the specified icon:
		assertEquals(ICON.toString(), EventTypeMetadata.getIcon(event));
		
		event = EventHelpers.builder(SUBJECT)
				.stream(Stream.TwitterFeed)
				.addRdf(OTHER_SUBJECT, HAS_ICON, OTHER_ICON)
				.build();
		// We expect the type's (default) icon:
		assertEquals(EVENT_ICON_DEFAULT, EventTypeMetadata.getIcon(event));

		event = EventHelpers.builder(SUBJECT)
				.stream(Stream.TwitterFeed)
				.build();
		// We expect the type's (default) icon:
		assertEquals(EVENT_ICON_DEFAULT, EventTypeMetadata.getIcon(event));

	}
	
	/**
	 * Make sure some constants are in sync. Because of some maven dependencies
	 * these variables cannot always be reused but are redefined. We are trying
	 * to avoid this.
	 */
	@Test
	public void testStaticProperties() {
		assertEquals(Event.ICON.toString(), Stream.STREAM_ICON);
		assertEquals(Event.ICON.toString(), eu.play_project.play_commons.constants.Event.EVENT_ICON);
	}

}