package eu.play_project.play_commons.eventtypes;

import static eu.play_project.play_commons.constants.Namespace.EVENTS;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.util.UUID;

import org.event_processing.events.types.Event;
import org.event_processing.events.types.Point;
import org.event_processing.events.types.Thing1;
import org.ontoware.rdf2go.RDF2Go;
import org.ontoware.rdf2go.model.Model;
import org.ontoware.rdf2go.model.ModelSet;
import org.ontoware.rdf2go.model.Syntax;
import org.ontoware.rdf2go.model.node.URI;
import org.ontoware.rdf2go.model.node.impl.DatatypeLiteralImpl;
import org.ontoware.rdf2go.model.node.impl.URIImpl;
import org.ontoware.rdf2go.vocabulary.XSD;
import org.w3c.dom.Element;

import com.hp.hpl.jena.datatypes.RDFDatatype;
import com.hp.hpl.jena.query.Dataset;
import com.hp.hpl.jena.query.DatasetFactory;
import com.hp.hpl.jena.sparql.util.NodeFactory;

import eu.play_project.play_commons.constants.Namespace;
import eu.play_project.play_commons.eventformat.EventFormatHelpers;
import fr.inria.eventcloud.utils.trigwriter.TriGWriter;

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
	 * Print a {@linkplain ModelSet} to TriG.
	 * 
	 * @param out
	 *            A stream to write to.
	 * @param modelSet
	 */
	public static void write(OutputStream out, ModelSet modelSet) {
		Dataset ds = (Dataset) modelSet.getUnderlyingModelSetImplementation();
		TriGWriter.write(out, ds);
	}

	/**
	 * Print a {@linkplain Model} to TriG.
	 * 
	 * @param out
	 *            A stream to write to.
	 * @param model
	 */
	public static void write(OutputStream out, Model model) {
		com.hp.hpl.jena.rdf.model.Model m = (com.hp.hpl.jena.rdf.model.Model) model
				.getUnderlyingModelImplementation();
		Dataset ds = DatasetFactory.createMem();
		ds.getDefaultModel().setNsPrefixes(m.getNsPrefixMap());
		ds.addNamedModel(model.getContextURI().toString(), m);
		TriGWriter.write(out, ds);
	}

	/**
	 * Print the underlying {@linkplain Model} to TriG.
	 * 
	 * @param out
	 *            A stream to write to.
	 * @param event
	 */
	public static void write(OutputStream out, Event event) {
		write(out, event.getModel());
	}

	/**
	 * Serialize a {@linkplain Model} to TriG and enclose the result in an XML wrapping
	 * element for use in WS-Notification.
	 * 
	 * @param model
	 * @return A string containing the RDF text.
	 */
	public static String serialize(Model model) {
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		EventHelpers.write(bout, model);
		return EventFormatHelpers.wrapWithNativeMessageElement(bout.toString(),
				Syntax.Trig.getMimeType());
	}

	/**
	 * Serialize a model to TriG and enclose the result in an XML wrapping
	 * element for use in WS-Notification.
	 */
	public static Element serializeAsDom(Model model) {
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		EventHelpers.write(bout, model);
		return EventFormatHelpers.wrapWithDomNativeMessageElement(
				bout.toString(), Syntax.Trig.getMimeType());
	}

	/**
	 * Serialize the underlying model to TriG and enclose the result in an XML
	 * wrapping element for use in WS-Notification.
	 */
	public static String serialize(Event event) {
		return serialize(event.getModel());
	}

	/**
	 * Serialize the underlying model to TriG and enclose the result in an XML
	 * wrapping element for use in WS-Notification.
	 */
	public static Element serializeAsDom(Event event) {
		return serializeAsDom(event.getModel());
	}

	/**
	 * Create a Jena node from a string. This makes a few checks if the string
	 * can be a URI, a literal, etc.
	 */
	public static com.hp.hpl.jena.graph.Node toJenaNode(String object) {
		
		com.hp.hpl.jena.graph.Node objectNode = null;
		try {
			@SuppressWarnings("unused")
			java.net.URI u = new java.net.URI(object);
			if (object.contains(":")) { // extra check so that numbers and single words don't become URIs
				objectNode = com.hp.hpl.jena.graph.Node.createURI(object);
			}
		} catch(URISyntaxException e) {
		}
		finally {
			if (objectNode == null) {
				int i = object.lastIndexOf("^^");
				if (i != -1) { // Check if the String is Turtle encoded
					String value = object.substring(0, i);
					RDFDatatype datatype = com.hp.hpl.jena.graph.Node.getType(object.substring(i + 2));
					objectNode = com.hp.hpl.jena.graph.Node.createLiteral(value, datatype);
				}
				else {
					objectNode = NodeFactory.createLiteralNode(object, null, null); // TODO sobermeier: support typed literals from Prolog
				}
			}
		}
		return objectNode;
	}
}
