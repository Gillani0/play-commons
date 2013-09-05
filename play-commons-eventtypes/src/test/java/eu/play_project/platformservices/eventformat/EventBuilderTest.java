package eu.play_project.platformservices.eventformat;

import static eu.play_project.play_commons.constants.Event.EVENT_ID_SUFFIX;
import static eu.play_project.play_commons.constants.Stream.SituationalEventStream;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Calendar;

import org.event_processing.events.types.CrisisMeasureEvent;
import org.event_processing.events.types.Event;
import org.event_processing.events.types.Point;
import org.event_processing.events.types.UcTelcoCall;
import org.junit.Test;
import org.ontoware.rdf2go.model.node.Variable;
import org.ontoware.rdf2go.model.node.impl.URIImpl;
import org.ontoware.rdf2go.vocabulary.RDF;

import eu.play_project.play_commons.constants.Source;
import eu.play_project.play_commons.constants.Stream;
import eu.play_project.play_commons.eventtypes.EventHelpers;

public class EventBuilderTest {

	@Test(expected = IllegalStateException.class)
	public void testMissingStream() {
		
		@SuppressWarnings("unused")
		Event event = EventHelpers.builder()
				//.stream(Stream.TaxiUCCall)
				.source(Source.UnitTest)
				.type(UcTelcoCall.RDFS_CLASS)
				.build();

		fail("We should have experienced an exception by now. This should not be reached.");
	}
	
	@Test
	public void testEventIdPrefix() {
		
		String unitTest = "UnitTest";
		Event event = EventHelpers.builder(unitTest, true)
				.type(UcTelcoCall.RDFS_CLASS)
				.stream(Stream.TaxiUCCall)
				.source(Source.UnitTest)
				.build();

		assertTrue(event.getModel().getContextURI().toString().contains(unitTest));
	}
	
	@Test
	public void testEventIdSuffix() {
		
		Event event = EventHelpers.builder("http://somenamepspace/01234" + EVENT_ID_SUFFIX) // has event suffix
				.type(UcTelcoCall.RDFS_CLASS)
				.stream(Stream.TaxiUCCall)
				.source(Source.UnitTest)
				.build();

		assertFalse("The #event suffix was not silently managed", event.getModel().getContextURI().toString().contains(EVENT_ID_SUFFIX));
	}
	
	@Test
	public void testStreamIdSuffix() {
		
		String unitTest = "UnitTest";
		Event event = EventHelpers.builder(unitTest, true)
				.type(UcTelcoCall.RDFS_CLASS)
				.stream(Stream.TaxiUCCall.getTopicUri()) // no stream suffix
				.source(Source.UnitTest)
				.build();

		assertTrue("The stream ID suffix was not automatically appended", event.getStream().toString().endsWith(Stream.STREAM_ID_SUFFIX));
	}

	@Test
	public void testCustomProperties() {
		final String MY = "http://mynamespace.domain.invalid/";
		
		Event event = EventHelpers.builder(MY + "01234")
				.type(MY + "MyEventType")
				.stream(Stream.TaxiUCCall)
				.addProperty(MY + "myProp", "Hello World!")
				.addRdf(MY + "MySubject", MY + "myProp", "Hello World again!")
				.build();

		assertEquals(MY + "01234", event.getModel().getContextURI().toString());
		assertTrue(event.getModel().contains(Variable.ANY, new URIImpl(MY + "myProp"), "Hello World!"));
		assertTrue(event.getModel().contains(new URIImpl(MY + "01234" + EVENT_ID_SUFFIX), RDF.type, new URIImpl(MY + "MyEventType")));
		assertTrue(event.getModel().contains(new URIImpl(MY + "MySubject"), new URIImpl(MY + "myProp"), "Hello World again!"));
	}
	
	@Test
	public void testLocationAndMore() {

		Event event = EventHelpers.builder()
				.stream(Stream.TaxiUCCall)
				.source(Source.UnitTest)
				.startTime(Calendar.getInstance())
				.endTime(Calendar.getInstance())
				.location(11.0, 5.8)
				.build();

		assertTrue(event.getModel().contains(Variable.ANY, Point.GEOLATITUDE, Variable.ANY));
		assertTrue(event.getModel().contains(Variable.ANY, Point.GEOLONGITUDE, Variable.ANY));

		assertTrue(event.getModel().contains(Variable.ANY, RDF.type, Event.RDFS_CLASS));
	}

	/**
	 * Create an event using the builder and also using the SDK. Comapre results.
	 */
	@Test
	public void testCompatibilityWithSdk() {
		
		String eventId = EventHelpers.createRandomEventId("crisis");
		Calendar time = Calendar.getInstance();

		CrisisMeasureEvent event = new CrisisMeasureEvent(
				// set the RDF context part
				EventHelpers.createEmptyModel(eventId),
				// set the RDF subject
				eventId + EVENT_ID_SUFFIX,
				// automatically write the rdf:type statement
				true);
		event.setStream(new URIImpl(SituationalEventStream.getUri()));
		event.setEndTime(time);
		event.setCrisisFrequency("1000");
		event.setCrisisComponentName("Component-101");
		event.setCrisisLocalisation("somewhere");
		event.setCrisisSituation("Sit-01");
		event.setCrisisUid(eventId);
		event.setCrisisUnit("MHz");
		event.setCrisisValue("123");
		event.setCrisisComponentSeid("someSEID");

		
		Event event2 = EventHelpers.builder(eventId)
				.type(CrisisMeasureEvent.RDFS_CLASS)
				.stream(SituationalEventStream)
				.endTime(time)
				.addProperty(CrisisMeasureEvent.CRISISFREQUENCY, "1000")
				.addProperty(CrisisMeasureEvent.CRISISCOMPONENTNAME, "Component-101")
				.addProperty(CrisisMeasureEvent.CRISISLOCALISATION, "somewhere")
				.addProperty(CrisisMeasureEvent.CRISISSITUATION, "Sit-01")
				.addProperty(CrisisMeasureEvent.CRISISUID, eventId)
				.addProperty(CrisisMeasureEvent.CRISISUNIT, "MHz")
				.addProperty(CrisisMeasureEvent.CRISISVALUE, "123")
				.addProperty(CrisisMeasureEvent.CRISISCOMPONENTSEID, "someSEID")
				.build();
		
		
		assertTrue(event.getModel().isIsomorphicWith(event2.getModel()));
	}
	
}
