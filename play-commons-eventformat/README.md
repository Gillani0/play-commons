PLAY Events
===========
Events to be sent to [PLAY](http://www.play-project.eu/):
* Events **must** be RDF format, preferably in [**TriG** serialization syntax](http://www4.wiwiss.fu-berlin.de/bizer/TriG/).
* Events **should** have an RDF Schema

Formatting Events
-----------------
In PLAY and event is composed of RDF quadruples: 4-tuples composed of a *graph name*, a *subject*, a *predicate* and an *object* value (g,s,p,o). Graph, subject and predicate are URIs whereas object is either an URI or a literal such as an xsd:int, xsd:dateTime, string (the default), ... (Blank nodes are not used in RDF of PLAY. They must be replaced/skolemized first.)

An event **must** have a minimum set of features:
* **event ID** (unique *subject* and *graph* used per event)
 * the graph is preferably a unique, generated URI ending in `#event` in the namespace `http://events.event-processing.org/ids/`
 * the subject is the same URI ending in `#event` (we model a difference between the *graph* describing/holding the event data and the *subject* describing the event
* **event type** (predicate *rdf:type* or shorthand *a*) linking to a type from the PLAY schema
* **stream** (predicate *:stream*) linking to a stream URI ending in `#stream`, preferably in the namespace `http://streams.event-processing.org/ids/`
* **time stamp** (predicate *:endTime*) in xsd:dateTime format, an optional *:startTime* is supported for events which happened over an interval

### Example
```none
@PREFIX :    &lt;http://events.event-processing.org/types/&gt; .
@PREFIX xsd: &lt;http://www.w3.org/2001/XMLSchema#&gt; .

<http://events.event-processing.org/ids/e1> {
    <http://events.event-processing.org/ids/e1#event>
        a       :TempEvent ;
        :endTime "2011-08-24T14:40:59.837"^^xsd:dateTime ;
        :stream <http://streams.event-processing.org/ids/WeatherStream#stream> .
}
```

The TriG syntax uses a shorthand for tuples (g,s,p,o) with repeated graph and subject like this:

```none
graph {
    subject
        predicate object ;
        ... ;
        predicate object .
}
```


Modelling Events
----------------
To define event types a schema is optional but recommended. Added value is verification in Unit Tests and future use cases for reasoning using RDFS.

See details at [play-commons-eventtypes](https://github.com/play-project/play-commons/tree/master/play-commons-eventtypes/).


Sending Events
--------------
When sending events to PLAY they must be:

1. in RDF format, TriG syntax (see above)
2. escaped for XML ([code](https://github.com/play-project/play-commons/blob/master/play-commons-eventformat/src/main/java/eu/play_project/play_commons/eventformat/EventFormatHelpers.java))
3. wrapped in an XML `<mt:nativeMessage>` element ([code](https://github.com/play-project/play-commons/blob/master/play-commons-eventformat/src/main/java/eu/play_project/play_commons/eventformat/EventFormatHelpers.java))
4. wrapped in a WS-Notification SOAP message

See details at [PLAY Abstract RDF Sender](https://github.com/play-project/play-eventadapters/tree/master/play-eventadapter-abstractrdfsender).

Receiving Events
----------------
See details at [PLAY Abstract RDF Receiver](https://github.com/play-project/play-eventadapters/tree/master/play-eventadapter-abstractrdfreceiver).


Further reading
---------------
* Technical Report on designing the event format: http://km.aifb.kit.edu/sites/lodstream/


