package eu.play_project.play_commons.eventformat.tests;

import static eu.play_project.play_commons.constants.Event.WSN_MSG_ELEMENT;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Map;

import org.junit.Test;
import org.w3c.dom.Element;

import eu.play_project.play_commons.constants.Event;
import eu.play_project.play_commons.eventformat.EventFormatHelpers;

/**
 * Some tests for play-commons.
 * 
 * @author lpellegrino
 * @author stuehmer
 * 
 */
public class EventFormatHelpersTest {

	private String mimeType = Event.WSN_MSG_DEFAULT_SYNTAX;

	private String eventId = "http://events.event-processing.org/ids/call-19";

	private final String payload = "@prefix :        <http://events.event-processing.org/types/> .\r\n"
			+ "@prefix e:       <http://events.event-processing.org/ids/> .\r\n"
			+ "@prefix dsb:     <http://www.petalslink.org/dsb/topicsns/> .\r\n"
			+ "@prefix xsd:     <http://www.w3.org/2001/XMLSchema#> .\r\n"
			+ "@prefix uccal:    <http://calservice.ws.play.orange.com/> .\r\n"
			+ "e:call-19 {\r\n"
			+ "e:call-19#event uccal:sequenceNumber \"19\"^^xsd:integer ;\r\n"
			+ "uccal:timestamp \"2011-12-06T18:33:36.681\"^^xsd:datetime ;\r\n"
			+ "uccal:uniqueId \"taxiUC:call-19\" ;\r\n"
			+ "uccal:direction \"outgoing\" ;\r\n"
			+ "uccal:message \"message\" ;\r\n"
			+ "uccal:callerPhoneNumber \"33600000011\" ;\r\n"
			+ "uccal:calleePhoneNumber \"33600000010\" ;\r\n"
			+ "uccal:latitude \"1.1\" ;\r\n"
			+ "uccal:longitude \"2.2\" ;\r\n"
			+ "# but also some other data according to event format\r\n"
			+ "a <http://ws.play.orange.com/taxiEventType.xsd> ;\r\n"
			+ ":endTime \"2011-12-06T18:33:36.681\"^^xsd:dateTime ;\r\n"
			+ ":source <jbi://Endpoint#source> ;\r\n"
			+ ":stream <http://streams.event-processing.org/ids/TaxiUCCAL#stream> .\r\n"
			+ "}";

	/**
	 * This test makes sure that some given strings are really part of the
	 * (wrapped) output.
	 */
	@Test
	public void testWrapAndUnwrapNativeMessageElement() {
		String wrappedMessage;
		String unwrappedMessage;

		// Test if the mimetype is really in the wrappedMessage
		wrappedMessage = EventFormatHelpers.wrapWithNativeMessageElement(
				payload, mimeType);
		assertTrue(
				"The wrapped message did not contains the supplied mimetype.",
				wrappedMessage.contains(mimeType));

		// Test if mimetype and graph name (eventId) are really part of the
		// wrappedMessage
		mimeType = "application/rdf+xml";
		eventId = "http://events.event-processing.org/ids/call-19";
		wrappedMessage = EventFormatHelpers.wrapWithNativeMessageElement(
				payload, mimeType, eventId);

		assertTrue(
				"The wrapped message did not contains the supplied mimetype.",
				wrappedMessage.contains(mimeType));
		assertTrue(
				"The wrapped message did not contains the supplied graph value.",
				wrappedMessage.contains(eventId));
		assertTrue(
				"The wrapped message did not contain the string from the wrapper: "
						+ WSN_MSG_ELEMENT.getLocalPart(),
				wrappedMessage.contains(WSN_MSG_ELEMENT
						.getLocalPart()));

		// Unwrap the same message and see if the string of nativeElement is
		// gone:
		unwrappedMessage = EventFormatHelpers
				.unwrapFromNativeMessageElement(wrappedMessage);
		assertTrue(
				"The unwrapped message still contained the string from the wrapper: "
						+ WSN_MSG_ELEMENT.getLocalPart(),
				!unwrappedMessage.contains(WSN_MSG_ELEMENT
						.getLocalPart()));
	}

	@Test
	public void testWrapAndUnwrapDomNativeMessageElement() {
		Element wrappedMessage;

		wrappedMessage = EventFormatHelpers.wrapWithDomNativeMessageElement(
				payload, mimeType);

		assertEquals(payload,
				EventFormatHelpers
						.unwrapFromDomNativeMessageElement(wrappedMessage));

		wrappedMessage = EventFormatHelpers.wrapWithDomNativeMessageElement(
				payload, mimeType, eventId);

		assertEquals(payload,
				EventFormatHelpers
						.unwrapFromDomNativeMessageElement(wrappedMessage));
	}

	@Test
	public void testGetSyntaxFromDomNativeMessageElement() {
		Element wrappedMessage = EventFormatHelpers
				.wrapWithDomNativeMessageElement(payload, mimeType);
		assertEquals(mimeType,
				EventFormatHelpers
						.getSyntaxFromDomNativeMessageElement(wrappedMessage));
	}

	@Test
	public void testGetGraphFromDomNativeMessageElement() {
		Element wrappedMessage = EventFormatHelpers
				.wrapWithDomNativeMessageElement(payload, mimeType, eventId);
		assertEquals(eventId,
				EventFormatHelpers
						.getGraphFromDomNativeMessageElement(wrappedMessage));

		wrappedMessage = EventFormatHelpers.wrapWithDomNativeMessageElement(
				payload, mimeType);
		assertTrue(EventFormatHelpers
				.getGraphFromDomNativeMessageElement(wrappedMessage) == null);
	}
	
	/**
	 * This test makes sure that the common namespace mapping for PLAY is at
	 * least non-empty.
	 */
	@Test
	public void testGetNsPrefixMap() {
		Map<String, String> map = EventFormatHelpers.getNsPrefixMap();
		assertTrue(!map.isEmpty());
	}

}
