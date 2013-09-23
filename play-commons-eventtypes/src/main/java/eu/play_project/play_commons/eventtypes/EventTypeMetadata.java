package eu.play_project.play_commons.eventtypes;

import static eu.play_project.play_commons.constants.Event.EVENT_ICON_DEFAULT;
import static eu.play_project.play_commons.constants.Event.EVENT_ID_SUFFIX;

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
	 * Return the URI of a favicon for the given event instance. Fall back to
	 * the icon of the event type if the event instance has no icon.
	 */
	public static String getIcon(Model event) {
		return getIcon(event, getId(event), getType(event));
	}

	
	/**
	 * Return the URI of a favicon for the given event instance. Fall back to
	 * the icon of the event type if the event instance has no icon.
	 */
	public static String getIcon(org.event_processing.events.types.Event event) {
		return getIcon(event.getModel(), getId(event), getType(event));
	}

	/**
	 * Return the URI of a favicon for the given event instance. Fall back to
	 * the icon of the event type if the event instance has no icon.
	 */
	private static String getIcon(Model event, String eventId, String eventType) {
		String iconUrl = null;
		
		try {
			ClosableIterator<Statement> it = event.findStatements(new URIImpl(eventId), new URIImpl(Event.EVENT_ICON), Variable.ANY);
			if (it.hasNext()) {
				iconUrl = it.next().getObject().toString();
			}
			it.close();
		} catch (ModelRuntimeException e) {
		}
		
		// Fall back to event type:
		if (iconUrl == null) {
			iconUrl = getIconForType(eventType);
		}
		
		return iconUrl;	}
	
	/**
	 * Return the URI of a favicon for the given event type.
	 */
	public static String getIconForType(String eventTypeUri) {
		return getIconForType(new URIImpl(eventTypeUri));
	}
	
	/**
	 * Return the URI of a favicon for the given event type.
	 */
	public static String getIconForType(URI eventType) {
		String iconUrl = EVENT_ICON_DEFAULT;
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
	 * @see eu.play_project.dcep.distributedetalis.utils.EventCloudHelpers#getType(fr.inria.eventcloud.api.CompoundEvent)
	 */
	public static String getType(Model event) {

		// First try RDF types with the expected event ID
		if (event.getContextURI() != null) {
			URI eventId = new URIImpl(event.getContextURI().toString() + EVENT_ID_SUFFIX);
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
	 * @see eu.play_project.dcep.distributedetalis.utils.EventCloudHelpers#getType(fr.inria.eventcloud.api.CompoundEvent)
	 */
	public static String getType(org.event_processing.events.types.Event event) {
		String eventType = null;
		try {
			Field f = event.getClass().getField("RDFS_CLASS");
			eventType = f.get(event).toString();
		} catch (NoSuchFieldException e) {
		} catch (SecurityException e) {
		} catch (IllegalArgumentException e) {
		} catch (IllegalAccessException e) {
		}

		if (eventType == null) {
			eventType = getType(event.getModel());
		}
		
		return eventType;
	}
	
	public static String getId(Model event) {
		if (event.getContextURI() != null) {
			return event.getContextURI().toString() + EVENT_ID_SUFFIX;
		}

		Iterator<Statement> it2 = event.findStatements(Variable.ANY, RDF.type,
				Variable.ANY);
		if (it2.hasNext()) {
			Statement stat = it2.next();
			return stat.getSubject().toString();
		}
	
		Iterator<Statement> it3 = event.findStatements(Variable.ANY, org.event_processing.events.types.Event.ENDTIME,
				Variable.ANY);
		if (it3.hasNext()) {
			Statement stat = it3.next();
			return stat.getSubject().toString();
		}
		
		// This should never be reached but there is no other default event ID
		return "";
	}
		
	public static String getId(org.event_processing.events.types.Event event) {
		return event.getResource().toString();
	}
	
}
