package eu.play_project.platformservices.eventtypes;

import static eu.play_project.play_commons.constants.Event.EVENT_ID_SUFFIX;
import static eu.play_project.play_commons.constants.Stream.SituationalEventStream;

import java.io.IOException;
import java.util.Calendar;

import org.event_processing.events.types.CrisisMeasureEvent;
import org.junit.Test;
import org.ontoware.rdf2go.exception.ModelRuntimeException;
import org.ontoware.rdf2go.model.Syntax;
import org.ontoware.rdf2go.model.node.impl.URIImpl;

import eu.play_project.play_commons.eventtypes.EventHelpers;

public class EventTypesCrisisTest {

	@Test
	public void testNotifyModel() throws ModelRuntimeException, IOException {
		// Create an event ID used in RDF context and RDF subject
		String eventId = EventHelpers.createRandomEventId("crisis");

		CrisisMeasureEvent event = new CrisisMeasureEvent(
				// set the RDF context part
				EventHelpers.createEmptyModel(eventId),
				// set the RDF subject
				eventId + EVENT_ID_SUFFIX,
				// automatically write the rdf:type statement
				true);

		event.setStream(new URIImpl(SituationalEventStream.getUri()));
		event.setEndTime(Calendar.getInstance());
		
		event.setCrisisFrequency("1000");
		event.setCrisisComponentName("Component-101");
		event.setCrisisLocalisation("somewhere");
		event.setCrisisSituation("Sit-01");
		event.setCrisisUid(eventId);
		event.setCrisisUnit("MHz");
		event.setCrisisValue("123");
		event.setCrisisComponentSeid("someSEID");
		
		// Print the resulting RDF
		event.getModel().writeTo(System.out, Syntax.Turtle);
		System.out.println();
	}
}
