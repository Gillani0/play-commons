package eu.play_project.platformservices.eventformat;

import static eu.play_project.play_commons.constants.Event.EVENT_ID_SUFFIX;
import static eu.play_project.play_commons.constants.Namespace.EVENTS;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import org.event_processing.events.types.FacebookCepResult;
import org.event_processing.events.types.FacebookStatusFeedEvent;
import org.event_processing.events.types.TwitterEvent;
import org.junit.Test;
import org.ontoware.rdf2go.exception.ModelRuntimeException;
import org.ontoware.rdf2go.model.Syntax;
import org.ontoware.rdf2go.model.node.impl.URIImpl;
import org.ontoware.rdfreactor.runtime.CardinalityException;

import eu.play_project.play_commons.constants.Stream;
import eu.play_project.play_commons.eventtypes.EventHelpers;

public class EventTypesTest {
	
	private static Random random = new Random();
	
	/**
	 * An experiment with RDFReactor
	 */
	@Test
	public void testFacebookStatusFeedEvent() throws ModelRuntimeException, IOException {
		// Create an event ID used in RDF context and RDF subject
		String eventId = EVENTS.getUri() + "e" + Math.abs(random.nextLong());
		// TODO stuehmer: devise safer model for uniqueness of event IDs, e.g. incorporating process ID and hostname

		FacebookStatusFeedEvent event = new FacebookStatusFeedEvent(
				// set the RDF context part
				EventHelpers.createEmptyModel(eventId),
				// set the RDF subject
				eventId + EVENT_ID_SUFFIX,
				// automatically write the rdf:type statement
				true);
		
		// Run some setters of the event
		event.setFacebookName("Roland Stühmer");
		event.setFacebookId("100000058455726");
		event.setFacebookLink(new URIImpl("http://graph.facebook.com/roland.stuehmer#"));
		event.setStatus("I bought some JEANS this morning");
		event.setFacebookLocation("Karlsruhe, Germany");
		// Create a Calendar for the current date and time
		event.setEndTime(Calendar.getInstance());
		event.setStream(new URIImpl(Stream.FacebookStatusFeed.getUri()));
		event.setSource(new URIImpl("http://sources.event-processing.org/ids/Facebook#source"));
		
		// Print the resulting RDF
		event.getModel().writeTo(System.out, Syntax.Turtle);
		System.out.println();
	}
	
	/**
	 * Another experiment modelling a interval-based event (two timestamps)
	 */
	@Test
	public void testFacebookCepResult() throws ModelRuntimeException, IOException, CardinalityException {
		// Create an event ID used in RDF context and RDF subject
		String eventId = EVENTS.getUri() + "e" + Math.abs(random.nextLong());
		
		FacebookCepResult event = new FacebookCepResult(EventHelpers.createEmptyModel(eventId),
				eventId + EVENT_ID_SUFFIX, true);
		// Create a Calendar for a given date
		event.setStartTime(javax.xml.bind.DatatypeConverter
				.parseDateTime("2011-08-24T14:42:01.011"));
		// Create a Calendar for the current date and time
		event.setEndTime(Calendar.getInstance());
		// Set some payload attributes
		event.addFriend("a person");
		event.addFriend("another person");
		event.addDiscussionTopic("JEANS was mentioned");
		event.setStream(new URIImpl(Stream.FacebookCepResults.getUri()));
		
//		List list = new List(event.getModel(), true);
//		for (String s : eventIds) {
//			list.setFirst(new URIImpl(s));
//		}
//		event.setMembers(list);
		// TODO stuehmer: find nice way in Java to add sorted list of members

		// Print the resulting RDF
		EventHelpers.write(System.out, event);
		System.out.println();
	}

	/**
	 * Another experiment with a TwitterEvent
	 */
	@Test
	public void testTwitterEvent() throws ModelRuntimeException, IOException {
		String eventId = EVENTS.getUri() + "e" + Math.abs(random.nextLong());

		TwitterEvent event = new TwitterEvent(EventHelpers.createEmptyModel(eventId),
				eventId + EVENT_ID_SUFFIX, true);
		event.setEndTime(Calendar.getInstance());
		event.setTwitterFollowersCount(4);
		event.setTwitterFriendsCount(2);
		// Some set of things
		Set<String> set = new HashSet<String>(Arrays.asList("ace", "boom", "crew", "dog", "eon"));
		for (String item : set) {
			// This also works for userMention and link:
			event.addTwitterHashTag(item);
		}
		event.setTwitterIsRetweet(false);
		event.setTwitterScreenName("roland.stuehmer");
		event.setContent("some Tweet");
		event.setTwitterName("Roland Stühmer");
		event.setStream(new URIImpl(Stream.TwitterFeed.getUri()));
		double longitude = 123;
		double latitude = 345;
		EventHelpers.setLocationToEvent(event, longitude, latitude);

		String result = EventHelpers.serialize(event);
		assertTrue("The longitude was not properly added to the event location", result.contains(Double.toString(longitude)));
		
		// Print the resulting RDF
		System.out.println(result);
		System.out.println();
	}
}


