package eu.play_project.platformservices.eventformat;

import static eu.play_project.play_commons.constants.Event.EVENT_ID_SUFFIX;
import static eu.play_project.play_commons.constants.Namespace.EVENTS;

import java.io.IOException;
import java.util.Calendar;
import java.util.Random;

import org.event_processing.events.types.EsrShowFriendGeolocation;
import org.event_processing.events.types.EsrSubscribeTo;
import org.event_processing.events.types.FacebookStatusFeedEvent;
import org.event_processing.events.types.TwitterEvent;
import org.event_processing.events.types.UcTelcoAnswer;
import org.event_processing.events.types.UcTelcoComposeMail;
import org.event_processing.events.types.UcTelcoEsrRecom;
import org.event_processing.events.types.UcTelcoGeoLocation;
import org.event_processing.events.types.UcTelcoOpenFacebook;
import org.event_processing.events.types.UcTelcoOpenTwitter;
import org.junit.Test;
import org.ontoware.rdf2go.exception.ModelRuntimeException;
import org.ontoware.rdf2go.model.Syntax;
import org.ontoware.rdf2go.model.node.impl.DatatypeLiteralImpl;
import org.ontoware.rdf2go.model.node.impl.URIImpl;
import org.ontoware.rdf2go.vocabulary.XSD;

import eu.play_project.play_commons.constants.Stream;
import eu.play_project.play_commons.eventtypes.EventHelpers;

public class EventTypesTelcoTest {

	/**
	 * An example for the Telco use case.
	 */
	@Test
	public void testTaxiUCESRRecomEvent() throws ModelRuntimeException, IOException {
		String uniqueId = "esr1340688541673999872";
		String eventId = EVENTS.getUri() + uniqueId;

		UcTelcoEsrRecom event = new UcTelcoEsrRecom(EventHelpers.createEmptyModel(eventId),
				eventId + EVENT_ID_SUFFIX, true);
		event.setEndTime(new DatatypeLiteralImpl("2012-06-26T05:29:01Z", XSD._dateTime));
		event.setStream(new URIImpl(Stream.TaxiUCESRRecom.getUri()));
		event.setUcTelcoSequenceNumber(1);
		event.setUcTelcoCalleePhoneNumber("33492945370");
		event.setUcTelcoCallerPhoneNumber("33638611117");
		event.setUcTelcoUniqueId(new URIImpl(eventId + EVENT_ID_SUFFIX));
		event.setMessage("Your friend (33492945370) is somewhere in the vicinity. We recommend to subscribe to mobile phone geolocation event stream (http://streams.event-processing.org/ids/ s:TaxiUCGeoLocation)");
		event.setEsrRecommendation(new URIImpl("http://imu.ntua.gr/san/esr/1.1/recommendation/" + uniqueId));
		event.setUcTelcoAnswerRequired(true);
		event.setUcTelcoAckRequired(true);
		
		// Create an action object and connect it to the event:
		EsrSubscribeTo action = new EsrSubscribeTo(event.getModel(), true);
		action.setToStream(new URIImpl(Stream.TaxiUCGeoLocation.getUri()));
		event.addUcTelcoAction(action);
		
		// Create an action object and connect it to the event:
		UcTelcoOpenFacebook action2 = new UcTelcoOpenFacebook(event.getModel(), true);
		action2.setFacebookId("100004102810379");
		event.addUcTelcoAction(action2);
		
		// Create an action object and connect it to the event:
		EsrShowFriendGeolocation action3 = new EsrShowFriendGeolocation(event.getModel(), true);
		action3.setUcTelcoPhoneNumber("33638611117");
		EventHelpers.addLocation(action3, 1.0, 1.0);
		event.addUcTelcoAction(action3);

		// Create an action object and connect it to the event:
		UcTelcoComposeMail action4 = new UcTelcoComposeMail(event.getModel(), true);
		action4.setUcTelcoMailAddress(new URIImpl("mailto:roland.stuehmer@fzi.de"));
		action4.setUcTelcoMailSubject("Regards");
		action4.setUcTelcoMailContent("Hello world,\nwith kind regards,\n...");
		event.addUcTelcoAction(action4);

		// Create an action object and connect it to the event:
		UcTelcoOpenTwitter action5 = new UcTelcoOpenTwitter(event.getModel(), true);
		action5.setTwitterScreenName("rolandstuehmer");
		event.addUcTelcoAction(action5);
		
		event.getModel().writeTo(System.out, Syntax.Turtle);
		System.out.println();
	}
	
	/**
	 * An example for the Telco use case.
	 */
	@Test
	public void testUcTelcoAnswerEvent() throws ModelRuntimeException, IOException {
		String uniqueId = "ack1340688541673999872";
		String eventId = EVENTS.getUri() + uniqueId;

		UcTelcoAnswer event = new UcTelcoAnswer(EventHelpers.createEmptyModel(eventId),
				eventId + EVENT_ID_SUFFIX, true);
		event.setEndTime(Calendar.getInstance());
		event.setStream(new URIImpl(Stream.TaxiUCESRRecom.getUri()));
		event.setUcTelcoSequenceNumber(2);
		event.setUcTelcoUniqueId(new URIImpl(eventId + EVENT_ID_SUFFIX));

		event.setMessage("The recommendation was accepted.");
		event.setUcTelcoAckResult(true);
		// We can identify the actions by using their property URIs, multiple actions allowed:
		event.addUcTelcoRelAction(EsrSubscribeTo.RDFS_CLASS);
		event.addUcTelcoRelAction(UcTelcoOpenFacebook.RDFS_CLASS);
		event.setUcTelcoRelRecommendation(new URIImpl("http://imu.ntua.gr/san/esr/1.1/recommendation/esr1340688541673999872"));
		event.setUcTelcoRelUniqueId(new URIImpl(EVENTS.getUri() + "esr1340688541673999872" + EVENT_ID_SUFFIX));
		event.setUcTelcoUserType("Customer");
		event.setUcTelcoPhoneNumber("33492945370");
		EventHelpers.setLocationToEvent(event, 6.0, 7.0);
		
		event.getModel().writeTo(System.out, Syntax.Turtle);
		System.out.println();
	}

	/**
	 * An example for the Telco use case.
	 */
	@Test
	public void testTwitterTelcoEvent() throws ModelRuntimeException, IOException {
		String phone = "33492945370";
		String eventId = EventHelpers.createRandomEventId("taxiUC" + phone);

		TwitterEvent event = new TwitterEvent(EventHelpers.createEmptyModel(eventId),
				eventId + EVENT_ID_SUFFIX, true);
		event.setStream(new URIImpl(Stream.TwitterFeed.getUri()));
		event.setEndTime(Calendar.getInstance());
		event.setUcTelcoSequenceNumber(1);
		event.setUcTelcoUniqueId(new URIImpl(eventId + EVENT_ID_SUFFIX));
		event.setTwitterScreenName("antonio_aversa");
		event.setUcTelcoPhoneNumber(phone);
		event.setUcTelcoUserType("Customer");
		event.setContent("New \"car!");
		EventHelpers.setLocationToEvent(event, 123, 456);

		// Print the resulting RDF
		event.getModel().writeTo(System.out, Syntax.Turtle);
		System.out.println();
	}
	
	/**
	 * An example for the Telco use case.
	 */
	@Test
	public void testFacebookTelcoEvent() throws ModelRuntimeException, IOException {
		String phone = "33492945370";
		String eventId = EventHelpers.createRandomEventId("taxiUC" + phone);

		FacebookStatusFeedEvent event = new FacebookStatusFeedEvent(
				// set the RDF context part
				EventHelpers.createEmptyModel(eventId),
				// set the RDF subject
				eventId + EVENT_ID_SUFFIX,
				// automatically write the rdf:type statement
				true);
		
		event.setEndTime(Calendar.getInstance());
		event.setStream(new URIImpl(Stream.FacebookStatusFeed.getUri()));

		event.setUcTelcoPhoneNumber(phone);
		event.setUcTelcoUserType("Customer");	
		event.setFacebookName("Antonio Aversa");
		event.setFacebookLink(new URIImpl("http://graph.facebook.com/100004102810379#"));
		event.setFacebookId("100004102810379");
		// toName
		// postId
		event.setStatus("I’ve bought a new car!");
		event.addLinksto(new URIImpl("http://en.wikipedia.org/wiki/Car_%28disambiguation%29"));
		event.addLinksto(new URIImpl("http://en.wikipedia.org/wiki/Automobile"));
		// createdTime
		// updatedTime
		
		// Print the resulting RDF
		event.getModel().writeTo(System.out, Syntax.Turtle);
		System.out.println();
	}
	 
	 
	@Test
	public void testGeolocationTelcoEvent() throws ModelRuntimeException, IOException {
		final String phone = "33492945370";
		final int sequenceNumber = 1234;
		final String uniqueId = "esr1340688541673999872";
		final String eventId = EVENTS.getUri() + uniqueId;
	 
		UcTelcoGeoLocation event = new UcTelcoGeoLocation(EventHelpers.createEmptyModel(eventId),
				eventId + EVENT_ID_SUFFIX, true);
		event.setEndTime(Calendar.getInstance());
		event.setStream(new URIImpl(Stream.TaxiUCGeoLocation.getUri()));
		EventHelpers.setLocationToEvent(event, "blank://0", 1.0, 2.0);
		event.setTwitterScreenName("roland.stuehmer");
		event.setUcTelcoMailAddress(new URIImpl("mailto:roland.stuehmer@fzi.de"));
		event.setUcTelcoPhoneNumber(phone);
		event.setUcTelcoSequenceNumber(sequenceNumber);
		event.setUcTelcoUniqueId(uniqueId);

		System.out.println(EventHelpers.serialize(event));
	}

}


