package eu.play_project.platformservices.eventformat;

import static eu.play_project.play_commons.constants.Event.EVENT_ID_SUFFIX;

import java.io.IOException;
import java.util.Calendar;

import org.event_processing.events.types.ProximityInfoEvent;
import org.event_processing.events.types.VesselEvent;
import org.junit.Test;
import org.ontoware.rdf2go.exception.ModelRuntimeException;
import org.ontoware.rdf2go.model.Syntax;
import org.ontoware.rdf2go.model.node.impl.URIImpl;

import eu.play_project.play_commons.constants.Stream;
import eu.play_project.play_commons.eventtypes.EventHelpers;

public class EventTypesAisTest {

	/**
	 * An example for the Telco use case.
	 */
	@Test
	public void testAisVesselEvent() throws ModelRuntimeException, IOException {
		String eventId = EventHelpers.createRandomEventId("testing");
		
		VesselEvent event = new VesselEvent(EventHelpers.createEmptyModel(eventId),
				eventId + EVENT_ID_SUFFIX, true);
		// Run some setters of the event
		event.setAisMMSI("240370000");
		event.setAisCallSign("SY6188");
		event.setAisCourse(155.5);
		event.setAisName("AQUARIUS ALFA");
		event.setAisNavStatus("Under way using engine");
		event.setAisShipCargo("Undefined");
		event.setAisShipType("Sailing");
		event.setAisSpeed(0.0);
		event.setAisTrueHeading(29.7);
		event.setAisWindDirection(356.0);
		event.setAisWindSpeed(15.0);
		// position is missing (not needed)
		
		// Create a Calendar for the current date and time
		event.setEndTime(Calendar.getInstance());
		event.setStream(new URIImpl(Stream.TaxiUCCall.getUri()));
		
		event.getModel().writeTo(System.out, Syntax.Turtle);
		System.out.println();
	}
	
	/**
	 * An example for the Telco use case.
	 */
	@Test
	public void testAisProximityEvent() throws ModelRuntimeException, IOException {
		String eventId = EventHelpers.createRandomEventId("testing");
		
		ProximityInfoEvent event = new ProximityInfoEvent(EventHelpers.createEmptyModel(eventId),
				eventId + EVENT_ID_SUFFIX, true);
		// Run some setters of the event
		event.setAisMMSI("237039500");
		event.setAisDistance(8679.196473457294);
		event.setAisNearbyCallSign("");
		event.setAisNearbyCourse(347.0);
		event.setAisNearbyMMSI("253504000");
		event.setAisNearbyName("BARONG C" );
		event.setAisNearbyShipCargo("Undefined");
		event.setAisNearbyShipType("Undefined");
		event.setAisNearbySpeed(9.0);
		event.setAisNearbyTrueHeading(51.0);
		// position is missing (not needed)
		
		// Create a Calendar for the current date and time
		event.setEndTime(Calendar.getInstance());
		event.setStream(new URIImpl(Stream.TaxiUCCall.getUri()));

		event.getModel().writeTo(System.out, Syntax.Turtle);
		System.out.println();
	}

}


