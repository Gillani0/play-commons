PLAY Events
===========
Events to be sent to [PLAY](http://www.play-project.eu/):
* Events **must** be RDF format, preferably in [**TriG** serialization syntax](http://www4.wiwiss.fu-berlin.de/bizer/TriG/).
* Events **should** have an RDF Schema, it is defined in [types.n3](src/main/resources/types.n3).

Formatting Events
-----------------
In RDF a quadruple is a 4-tuple composed of a *graph name*, a *subject*, a *predicate* and an *object* value (g,s,p,o). Graph, subject and predicate values are URIs whereas object maybe either an URI or a Literal such as xsd:int, xsd:dateTime, string (the default), ... (Blank nodes are not used in RDF of PLAY. They must be replaced/skolemized first.)

An event **must** have a minimum set of features:
* **event ID** (unique *subject* and *graph* used per event) 
 * the graph is preferably a unique, generated URI ending in `#event` in the namespace `http://events.event-processing.org/ids/`
 * the subject is the same URI ending in `#event` (we model a difference between the *graph* describing/holding the event data and the *subject* describing the event 
* **event type** (predicate *rdf:type* or shorthand *a*) linking to a type from the PLAY schema
* **stream** (predicate *:stream*) linking to a stream URI ending in `#stream`, preferably in the namespace `http://streams.event-processing.org/ids/`
* **time stamp** (predicate *:endTime*) in xsd:dateTime format, an optional *:startTime* is supported for events which happened over an interval

### Example
```
<http://events.event-processing.org/ids/e1> {
    <http://events.event-processing.org/ids/e1#event>
          a       :TempEvent ;
          :endTime "2011-08-24T14:40:59.837"^^xsd:dateTime ;
          :stream <http://streams.event-processing.org/ids/WeatherStream#stream> .
}
```

The TriG syntax uses a shorthand for tuples (g,s,p,o) with repeated graph and subject like this:

```
graph {
    subject
        predicate object ;
        ... ;
        predicate object .
}
```


Sending Events
--------------
When sending events to PLAY they must be:

1. escaped for XML ([code](https://github.com/play-project/play-commons/blob/master/play-commons-eventformat/src/main/java/eu/play_project/play_commons/eventformat/EventFormatHelpers.java))
2. wrapped in an XML `<mt:nativeMessage>` element ([code](https://github.com/play-project/play-commons/blob/master/play-commons-eventformat/src/main/java/eu/play_project/play_commons/eventformat/EventFormatHelpers.java))
3. wrapped in a WS-Notification SOAP message ([code]())

See [PLAY Abstract RDF Sender](https://github.com/play-project/play-eventadapters/tree/master/play-eventadapter-abstractrdfsender).

Receiving Events
----------------
See [PLAY Abstract RDF Receiver](https://github.com/play-project/play-eventadapters/tree/master/play-eventadapter-abstractrdfreceiver).



Further reading
---------------
* Technical Report on designing the event format: http://km.aifb.kit.edu/sites/lodstream/

Code
----
* Examples how to **create an event** in Java using the SDK: <a href="src/test/java/eu/play_project/platformservices/eventformat/EventTypesTest.java">EventTypesTest.java</a>

