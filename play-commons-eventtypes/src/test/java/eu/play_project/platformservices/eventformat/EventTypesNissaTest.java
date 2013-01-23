package eu.play_project.platformservices.eventformat;

import static eu.play_project.play_commons.constants.Event.EVENT_ID_SUFFIX;
import static eu.play_project.play_commons.constants.Namespace.EVENTS;

import java.io.IOException;
import java.util.Random;

import org.event_processing.events.types.NissaHeatbeatAlert;
import org.junit.Test;
import org.ontoware.rdf2go.exception.ModelRuntimeException;
import org.ontoware.rdf2go.model.Model;
import org.ontoware.rdf2go.model.Syntax;
import org.ontoware.rdf2go.model.node.impl.DatatypeLiteralImpl;
import org.ontoware.rdf2go.model.node.impl.URIImpl;
import org.ontoware.rdf2go.vocabulary.XSD;

import eu.play_project.play_commons.constants.Stream;
import eu.play_project.play_commons.eventtypes.EventHelpers;

public class EventTypesNissaTest {
	
	private static Random random = new Random();
	

	/**
	 * An example for the Telco use case.
	 */
	@Test
	public void testHeatbeatAlert() throws ModelRuntimeException, IOException {
		String uniqueId = "nissa1340688541673999872";
		String eventId = EVENTS.getUri() + uniqueId;
		Model m = EventHelpers.createEmptyModel(eventId);
		m.setNamespace("nissa", "http://www.nissatech.rs/ns/types/");

		int heartRate = 91;
		String facebookId= "12345";
				
		NissaHeatbeatAlert event = new NissaHeatbeatAlert(m, eventId + EVENT_ID_SUFFIX, true);
		event.setEndTime(new DatatypeLiteralImpl("2012-12-22T13:31:13Z", XSD._dateTime));
		event.setStream(new URIImpl(Stream.TaxiUCGeoLocation.getUri()));
		event.setMessage(String.format("This is a heatbeat alert (currently at %s) sent from Android.", heartRate));
		event.setNissaSessionId(6);
		event.setNissaHeartRate(heartRate);
		event.setNissaDistance(12.5);
		event.setNissaSteps(10);
		event.setNissaSpeed(1.2);
		event.setNissaDuration(26);
		event.setNissaCalories(0.08778622072179713);
		event.setNissaBattery(96);
		event.setNissaTemp(0.0);
		event.setNissaHumidity(0.0);
		event.setNissaWind(0.0);
		event.setNissaPressure(0.0);
		event.setNissaAcc(50.0);
		EventHelpers.setLocationToEvent(event, "blank://0", 8.401188900000001, 49.008156);
		event.setFacebookId(facebookId);
		event.setFacebookLink(new URIImpl(String.format("http://graph.facebook.com/%s#", facebookId)));
		event.setTwitterScreenName("firstname.lastname");
		event.setUcTelcoPhoneNumber("12345");
		
		event.getModel().writeTo(System.out, Syntax.Turtle);
		System.out.println();
	}

}


