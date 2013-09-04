package eu.play_project.play_commons.eventtypes;

import static eu.play_project.play_commons.constants.Event.EVENT_ID_SUFFIX;
import static eu.play_project.play_commons.constants.Event.WSN_MSG_DEFAULT_SYNTAX;
import static eu.play_project.play_commons.constants.Namespace.EVENTS;
import static eu.play_project.play_commons.constants.Stream.STREAM_ID_SUFFIX;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.UUID;

import org.event_processing.events.types.Event;
import org.event_processing.events.types.Point;
import org.event_processing.events.types.Thing1;
import org.ontoware.rdf2go.RDF2Go;
import org.ontoware.rdf2go.exception.ModelRuntimeException;
import org.ontoware.rdf2go.exception.SyntaxNotSupportedException;
import org.ontoware.rdf2go.model.Model;
import org.ontoware.rdf2go.model.ModelSet;
import org.ontoware.rdf2go.model.Syntax;
import org.ontoware.rdf2go.model.node.URI;
import org.ontoware.rdf2go.model.node.impl.DatatypeLiteralImpl;
import org.ontoware.rdf2go.model.node.impl.PlainLiteralImpl;
import org.ontoware.rdf2go.model.node.impl.URIImpl;
import org.ontoware.rdf2go.vocabulary.XSD;
import org.w3c.dom.Element;

import com.hp.hpl.jena.datatypes.RDFDatatype;
import com.hp.hpl.jena.graph.Node;
import com.hp.hpl.jena.graph.NodeFactory;

import eu.play_project.play_commons.constants.Namespace;
import eu.play_project.play_commons.constants.Source;
import eu.play_project.play_commons.constants.Stream;
import eu.play_project.play_commons.eventformat.EventFormatHelpers;

public final class EventHelpers {

	/**
	 * Create an empty RDF2Go model with some useful namespaces predefined.
	 * Please make sure that you use a contextURI which defines this event
	 * uniquely within it's stream.
	 * 
	 * @param contextUri
	 *            RDF context URI for quads.
	 */
	public static Model createEmptyModel(URI contextUri) {
		Model model = RDF2Go.getModelFactory().createModel(contextUri);
		model.open();

		return addNamespaces(model);
	}

	/**
	 * Create an empty RDF2Go model with some useful namespaces predefined.
	 * Please make sure that you use a contextURI which defines this event
	 * uniquely within it's stream.
	 * 
	 * @param contextUri
	 *            RDF context URI for quads.
	 */
	public static Model createEmptyModel(String contextUri) {
		return createEmptyModel(new URIImpl(contextUri));
	}

	/**
	 * Create an empty RDF2Go model with some useful namespaces predefined.
	 */
	public static ModelSet createEmptyModelSet() {
		ModelSet modelSet = RDF2Go.getModelFactory().createModelSet();
		modelSet.open();

		return addNamespaces(modelSet);
	}

	/**
	 * Create a random event ID. The returned ID is a URI string in namespace
	 * {@linkplain EVENTS} and is randomized using
	 * {@linkplain UUID#randomUUID()}.
	 * 
	 * @return a URI string to be used as event ID.
	 */
	public static String createRandomEventId() {
		return createRandomEventId("e");
	}

	/**
	 * Create a random event ID. The returned ID is a URI string in namespace
	 * {@linkplain EVENTS} and is randomized using
	 * {@linkplain UUID#randomUUID()}.
	 * 
	 * @param prefix
	 *            an alphabetical(!) string possibly used to identify events by
	 *            your application in a simple way
	 * @return a URI string to be used as event ID.
	 */
	public static String createRandomEventId(String prefix) {
		// we are adamant about the prefix because it is important for URI construction:
		if (prefix == null || prefix.isEmpty()) {
			throw new IllegalArgumentException("Prefix must not be empty or null, avoiding illegal URIs.");
		}

		return EVENTS.getUri() + prefix + "_" + UUID.randomUUID();
	}

	/**
	 * Add a set of commonly used namespaces from PLAY.
	 */
	public static ModelSet addNamespaces(ModelSet modelSet) {
		for (Namespace ns : Namespace.values()) {
			modelSet.setNamespace(ns.getPrefix(), ns.getUri());
		}
		return modelSet;
	}

	/**
	 * Add a set of commonly used namespaces from PLAY.
	 */
	public static Model addNamespaces(Model model) {
		for (Namespace ns : Namespace.values()) {
			model.setNamespace(ns.getPrefix(), ns.getUri());
		}
		return model;
	}

	/**
	 * Add latitude and longitude to an existing event object. This method
	 * simplifies the process of creating a random node, etc, for the n-ary
	 * predicate.
	 * 
	 * Calling this method overrides any previous {@code :location} property.
	 * 
	 * @deprecated Use {@linkplain #setLocationToEvent(Event, double, double)}.
	 */
	@Deprecated
	public static void addLocationToEvent(Event event, double latitude,
			double longitude) {
		setLocationToEvent(event, latitude,	longitude);
	}

	/**
	 * Add latitude and longitude to an existing event object. This method
	 * simplifies the process of creating a random node, etc, for the n-ary
	 * predicate.
	 * 
	 * Calling this method overrides any previous {@code :location} property.
	 */
	public static void setLocationToEvent(Event event, double latitude,
			double longitude) {

		setLocationToEvent(event, null, latitude, longitude);
	}

	/**
	 * Add latitude and longitude to an existing event object. This method
	 * simplifies the process of creating a new node, etc, for the n-ary
	 * predicate.
	 * 
	 * Calling this method overrides any previous {@code :location} property.
	 * 
	 * @param event
	 * @param locationResource
	 *            A URI string which is used for the {@code geo:Point}. This
	 *            parameter may be {@code null} to create a random URI.
	 * @param latitude
	 * @param longitude
	 */
	public static void setLocationToEvent(Event event, String locationResource, double latitude,
			double longitude) {

		Point p;
		if (locationResource == null) {
			p = new Point(event.getModel(), true);
		}
		else {
			p = new Point(event.getModel(), locationResource, true);
		}

		p.setGeoLatitude(new DatatypeLiteralImpl(Double.toString(latitude), XSD._double));
		p.setGeoLongitude(new DatatypeLiteralImpl(Double.toString(longitude), XSD._double));

		event.setLocation(p);
	}

	/**
	 * Add a {@code geo:location} (i.e. a point with latitude and longitude) to an existing
	 * Thing. This method simplifies the process of random node, etc, for the n-ary
	 * predicate {@code Thing -> lat, long}.
	 * 
	 * Several calls to this method add several {@code geo:location} properties.
	 */
	public static void addLocation(Thing1 thing, double latitude,
			double longitude) {

		Point p = new Point(thing.getModel(), true);
		p.setGeoLatitude(new DatatypeLiteralImpl(Double.toString(latitude), XSD._double));
		p.setGeoLongitude(new DatatypeLiteralImpl(Double.toString(longitude), XSD._double));

		thing.addGeoLocation(p);
	}

	/**
	 * Print a {@linkplain ModelSet} to syntax {@link #WSN_MSG_DEFAULT_SYNTAX}.
	 * 
	 * @param out
	 *            A stream to write to.
	 * @param modelSet
	 */
	public static void write(OutputStream out, ModelSet modelSet) {
		try {
			modelSet.writeTo(out, Syntax.forMimeType(WSN_MSG_DEFAULT_SYNTAX));
		} catch (SyntaxNotSupportedException e) {
			e.printStackTrace();
		} catch (ModelRuntimeException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Print a {@linkplain Model} to syntax {@link #WSN_MSG_DEFAULT_SYNTAX}.
	 * 
	 * @param out
	 *            A stream to write to.
	 * @param model
	 */
	public static void write(OutputStream out, Model model) {
		if (model.getContextURI() == null) {
			throw new IllegalArgumentException("Context was not defined. We need quadruples.");
		}

		ModelSet ms = EventHelpers.createEmptyModelSet();
		ms.addModel(model);

		try {
			ms.writeTo(out, Syntax.forMimeType(WSN_MSG_DEFAULT_SYNTAX));
		} catch (ModelRuntimeException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Print the underlying {@linkplain Model} to syntax {@link #WSN_MSG_DEFAULT_SYNTAX}.
	 * 
	 * @param out
	 *            A stream to write to.
	 * @param event
	 */
	public static void write(OutputStream out, Event event) {
		write(out, event.getModel());
	}

	/**
	 * Serialize a {@linkplain Model} to syntax {@link #WSN_MSG_DEFAULT_SYNTAX} and enclose the result in an XML wrapping
	 * element for use in WS-Notification.
	 * 
	 * @param model
	 * @return A string containing the RDF text.
	 */
	public static String serialize(Model model) {
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		EventHelpers.write(bout, model);
		return EventFormatHelpers.wrapWithNativeMessageElement(bout.toString(),
				WSN_MSG_DEFAULT_SYNTAX);
	}

	/**
	 * Serialize a model to syntax {@link #WSN_MSG_DEFAULT_SYNTAX} and enclose the result in an XML wrapping
	 * element for use in WS-Notification.
	 */
	public static Element serializeAsDom(Model model) {
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		EventHelpers.write(bout, model);
		return EventFormatHelpers.wrapWithDomNativeMessageElement(
				bout.toString(), WSN_MSG_DEFAULT_SYNTAX);
	}

	/**
	 * Serialize the underlying model to syntax {@link #WSN_MSG_DEFAULT_SYNTAX} and enclose the result in an XML
	 * wrapping element for use in WS-Notification.
	 */
	public static String serialize(Event event) {
		return serialize(event.getModel());
	}

	/**
	 * Serialize the underlying model to syntax {@link #WSN_MSG_DEFAULT_SYNTAX} and enclose the result in an XML
	 * wrapping element for use in WS-Notification.
	 */
	public static Element serializeAsDom(Event event) {
		return serializeAsDom(event.getModel());
	}

	/**
	 * Create a Jena node from a string. This makes a few checks if the string
	 * can be a URI, a literal, etc.
	 */
	public static Node toJenaNode(String object) {

		Node objectNode = null;
		try {
			@SuppressWarnings("unused")
			java.net.URI u = new java.net.URI(object);
			if (object.contains(":")) { // extra check so that numbers and single words don't become URIs
				objectNode = NodeFactory.createURI(object);
			}
		} catch(URISyntaxException e) {
		}
		finally {
			if (objectNode == null) {
				int i = object.lastIndexOf("^^");
				if (i != -1) { // Check if the String is Turtle encoded
					String value = object.substring(0, i);
					RDFDatatype datatype = NodeFactory.getType(object.substring(i + 2));
					objectNode = NodeFactory.createLiteral(value, datatype);
				}
				else {
					objectNode = NodeFactory.createLiteral(object, null, null); // TODO sobermeier: support typed literals from Prolog
				}
			}
		}
		return objectNode;
	}
	

	/**
	 * Set the event ID URI.
	 * 
	 * DEFAULT: {@link EventHelpers#createRandomEventId()}
	 */
	public static Builder builder(URI eventId) {
		return new Builder(eventId);
	}
	
	/**
	 * Set the event ID URI.
	 * 
	 * DEFAULT: {@link EventHelpers#createRandomEventId()}
	 */
	public static Builder builder(String eventId) {
		return new Builder(eventId);
	}
	
	/**
	 * Set no event ID URI. The default is used.
	 * 
	 * DEFAULT: {@link EventHelpers#createRandomEventId()}
	 */
	public static Builder builder() {
		return new Builder();
	}
	
	/**
	 * Set a known prefix for a randomized event ID URI.
	 * 
	 * DEFAULT: none
	 */
	public static Builder builder(String eventIdPrefix, boolean usePrefix) {
		return new Builder(eventIdPrefix, usePrefix);
	}
	
	
	public static class Builder {
		
		private final URI eventId;
		private URI stream;
		private URI source;
		private Calendar endTime;
		private Calendar startTime;
		private URI type;
		private Double latitude;
		private Double longitude;
		private final Model additionalProperties = RDF2Go.getModelFactory().createModel().open();

		/**
		 * Set the event ID URI.
		 * 
		 * DEFAULT: {@link EventHelpers#createRandomEventId()}
		 */
		public Builder(URI eventId) {
			String s = eventId.toString();
			if (s.endsWith(EVENT_ID_SUFFIX)) {
				eventId = new URIImpl(s.substring(0, s.indexOf(EVENT_ID_SUFFIX)));
			}
			this.eventId = eventId;
		}
		/**
		 * Set the event ID URI.
		 * 
		 * DEFAULT: {@link EventHelpers#createRandomEventId()}
		 */
		public Builder(String eventId) {
			this(new URIImpl(eventId));
		}
		/**
		 * Set no event ID URI. The default is used.
		 * 
		 * DEFAULT: {@link EventHelpers#createRandomEventId()}
		 */
		public Builder() {
			this(EventHelpers.createRandomEventId());
		}
		/**
		 * Set a known prefix for a randomized event ID URI.
		 * 
		 * DEFAULT: none
		 */
		public Builder(String eventIdPrefix, boolean usePrefix) {
			this(EventHelpers.createRandomEventId(eventIdPrefix));
		}

	
		/**
		 * Set the stream including '#stream' suffix.
		 * 
		 * DEFAULT: none. MANDATORY.
		 */
		public Builder stream(Stream stream) {
			return stream(stream.getUri());
		}

		/**
		 * Set the stream including '#stream' suffix.
		 * 
		 * DEFAULT: none. MANDATORY.
		 */
		public Builder stream(String stream) {
			return stream(new URIImpl(stream));
		}
		
		/**
		 * Set the stream.
		 * 
		 * DEFAULT: none. MANDATORY.
		 */
		public Builder stream(URI stream) {
			if (!stream.toString().endsWith(STREAM_ID_SUFFIX)) {
				stream = new URIImpl(stream + STREAM_ID_SUFFIX);
			}
			this.stream = stream;
			return this;
		}
		
		/**
		 * Set the end time.
		 * 
		 * DEFAULT: current time
		 */
		public Builder endTime(Date endTime) {
			Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
			cal.setTime(endTime);
			return endTime(cal);
		}
		
		/**
		 * Set the end time.
		 * 
		 * DEFAULT: current time
		 */
		public Builder endTime(Calendar endTime) {
			this.endTime = endTime;
			return this;
		}

		/**
		 * Set the timestamp. Synonymous to {@link #endTime(Date)}.
		 * 
		 * DEFAULT: current time
		 */
		public Builder timestamp(Date timestamp) {
			Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
			cal.setTime(timestamp);
			return timestamp(cal);
		}
		
		/**
		 * Set the timestamp. Synonymous to {@link #endTime(Calendar)}.
		 * 
		 * DEFAULT: current time
		 */
		public Builder timestamp(Calendar timestamp) {
			return endTime(timestamp);
		}
		
		/**
		 * Set the start time.
		 * 
		 * DEFAULT: none
		 */
		public Builder startTime(Date startTime) {
			Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
			cal.setTime(startTime);
			return startTime(cal);
		}
		
		/**
		 * Set the start time.
		 * 
		 * DEFAULT: none
		 */
		public Builder startTime(Calendar startTime) {
			this.startTime = startTime;
			return this;
		}
		
		public Builder source(Source source) {
			return source(source.getUri());
		}

		public Builder source(String source) {
			return source(new URIImpl(source));
		}
		
		public Builder source(URI source) {
			this.source = source;
			return this;
		}

		/**
		 * Set the event type.
		 * 
		 * DEFAULT: {@link Event#RDFS_CLASS}
		 */
		public Builder type(String type) {
			return type(new URIImpl(type));
		}
		
		/**
		 * Set the event type.
		 * 
		 * DEFAULT: {@link Event#RDFS_CLASS}
		 */
		public Builder type(URI type) {
			this.type = type;
			return this;
		}

		/**
		 * Set the event location.
		 * 
		 * DEFAULT: none
		 */
		public Builder location(double latitude, double longitude) {
			latitude(latitude);
			longitude(longitude);
			return this;
		}
		
		/**
		 * Set the event latitude.
		 * 
		 * DEFAULT: none
		 */
		public Builder latitude(double latitude) {
			this.latitude = latitude;
			return this;
		}

		/**
		 * Set the event longitude.
		 * 
		 * DEFAULT: none
		 */
		public Builder longitude(double longitude) {
			this.longitude = longitude;
			return this;
		}

		/**
		 * Set arbitrary String property.
		 * 
		 * DEFAULT: none
		 * 
		 * @param property the name of the property (URI)
		 * @param value the string literal value
		 */
		public Builder addProperty(String property, String value) {
			return addProperty(new URIImpl(property), value);
		}

		/**
		 * Set arbitrary String property.
		 * 
		 * DEFAULT: none
		 * 
		 * @param property the name of the property (URI)
		 * @param value the string literal value
		 */
		public Builder addProperty(URI property, String value) {
			return addRdf(new URIImpl(eventId + EVENT_ID_SUFFIX), property, value);
		}

		/**
		 * Set arbitrary String property.
		 * 
		 * DEFAULT: none
		 * 
		 * @param subject the name of the subject (URI)
		 * @param property the name of the property (URI)
		 * @param value the string literal value
		 */
		public Builder addRdf(String subject, String property, String value) {
			return addRdf(new URIImpl(subject), new URIImpl(property), value);
		}

		/**
		 * Set arbitrary String property.
		 * 
		 * DEFAULT: none
		 * 
		 * @param subject the name of the subject (URI)
		 * @param property the name of the property (URI)
		 * @param value the string literal value
		 */
		public Builder addRdf(URI subject, URI property, String value) {
			return addRdf(subject, property, new PlainLiteralImpl(value));
		}

		/**
		 * Set arbitrary property.
		 * 
		 * DEFAULT: none
		 * 
		 * @param subject the name of the subject (URI)
		 * @param property the name of the property (URI)
		 * @param value the object (can be literal value or URI)
		 */
		public Builder addRdf(URI subject, URI property, org.ontoware.rdf2go.model.node.Node value) {
			additionalProperties.addStatement(subject, property, value);
			return this;
		}
		
		/**
		 * Build {@link Event}.
		 * 
		 * @throws IllegalStateException when the event is missing mandatory properties.
		 */
		public Event build() {
			if (endTime == null) {
				this.endTime = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
			}
			if (type == null) {
				this.type = Event.RDFS_CLASS;
			}
			
			validate();
			
			Event event = new Event(EventHelpers.createEmptyModel(eventId), eventId + EVENT_ID_SUFFIX, false);
			event.setStream(stream);
			event.setType(type);
			event.setEndTime(endTime);
			if (startTime != null) {
				event.setStartTime(startTime);
			}
			if (source != null) {
				event.setSource(source);
			}
			if (latitude != null && longitude != null) {
				EventHelpers.setLocationToEvent(event, latitude, longitude);
			}

			event.getModel().addModel(additionalProperties);
			
			return event;
		}
		
		/**
		 * Check for ENDTIME, STREAM
		 */
		private void validate() {
			if (stream == null) {
				throw new IllegalStateException("stream was not set on builder.");
			}
		}
	}
}
