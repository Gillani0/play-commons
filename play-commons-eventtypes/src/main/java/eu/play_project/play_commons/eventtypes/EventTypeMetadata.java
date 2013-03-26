package eu.play_project.play_commons.eventtypes;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Iterator;

import org.ontoware.aifbcommons.collection.ClosableIterator;
import org.ontoware.rdf2go.RDF2Go;
import org.ontoware.rdf2go.exception.ModelRuntimeException;
import org.ontoware.rdf2go.model.Model;
import org.ontoware.rdf2go.model.Statement;
import org.ontoware.rdf2go.model.Syntax;
import org.ontoware.rdf2go.model.node.URI;
import org.ontoware.rdf2go.model.node.Variable;
import org.ontoware.rdf2go.model.node.impl.URIImpl;
import org.ontoware.rdf2go.vocabulary.RDF;

import eu.play_project.play_commons.constants.Event;
import eu.play_project.play_commons.constants.Namespace;

/**
 * This class provides methods to deal with event metadata such as decorative
 * icons for various event classes.
 * 
 * @author Roland Stühmer
 */
public class EventTypeMetadata {
	
	private static Model types;
	
	/**
	 * Return the URI of a favicon for the given event type.
	 */
	public static String getEventTypeIcon(String eventTypeUri) {
		return getEventTypeIcon(new URIImpl(eventTypeUri));
	}
	
	/**
	 * Return the URI of a favicon for the given event type.
	 */
	public static String getEventTypeIcon(URI eventType) {
		String iconUrl = "";
		try {
			Model types = getMetadata();
			ClosableIterator<Statement> it = types.findStatements(eventType, new URIImpl(Event.EVENT_ICON), Variable.ANY);
			if (it.hasNext()) {
				iconUrl = it.next().getObject().toString();
			}
			it.close();
		} catch (ModelRuntimeException e) {
		} catch (IOException e) {
		}
		return iconUrl;
	}
	
	/**
	 * Returns a {@linkplain Model} of all event metadata. The model is cached
	 * in a singleton for subsequent calls.
	 */
	public static Model getMetadata() throws ModelRuntimeException, IOException {
		if (types == null) {
			types = RDF2Go.getModelFactory().createModel(new URIImpl(Namespace.TYPES.getUri())).open();
			types.readFrom(EventTypeMetadata.class.getClassLoader().getResourceAsStream("types.n3"), Syntax.Turtle);
		}
		return types;
	}
	
	/**
	 * Guess the event type URI from the given RDF.
	 * 
	 * @see eu.play_project.dcep.distributedetalis.utils.EventCloudHelpers#getEventType(fr.inria.eventcloud.api.CompoundEvent)
	 */
	public static String getEventType(Model event) {

		// First try RDF types with the expected event ID
		if (event.getContextURI() != null) {
			URI eventId = new URIImpl(event.getContextURI().toString()
					+ eu.play_project.play_commons.constants.Event.EVENT_ID_SUFFIX);
			Iterator<Statement> it = event.findStatements(eventId, RDF.type,
					Variable.ANY);
			if (it.hasNext()) {
				Statement stat = it.next();
				return stat.getObject().toString();
			}
		}
		// Then try any RDF types
		Iterator<Statement> it2 = event.findStatements(Variable.ANY, RDF.type,
				Variable.ANY);
		if (it2.hasNext()) {
			Statement stat = it2.next();
			return stat.getObject().toString();
		}
		// Then fall back to a constant String
		else {
			return org.event_processing.events.types.Event.RDFS_CLASS.toString();
		}
	}
	
	/**
	 * Return the event type URI for the given event.
	 * 
	 * @see eu.play_project.dcep.distributedetalis.utils.EventCloudHelpers#getEventType(fr.inria.eventcloud.api.CompoundEvent)
	 */
	public static String getEventType(org.event_processing.events.types.Event event) {
		String eventType = org.event_processing.events.types.Event.RDFS_CLASS.toString();
		try {
			Field f = event.getClass().getField("RDFS_CLASS");
			eventType = f.get(event).toString();
		} catch (NoSuchFieldException e) {
		} catch (SecurityException e) {
		} catch (IllegalArgumentException e) {
		} catch (IllegalAccessException e) {
		}
		return eventType;
	}
}
