Modelling Events for PLAY
=========================
Events to be sent to [PLAY](http://www.play-project.eu/) **should** have an RDF Schema, it is defined in [types.n3](src/main/resources/types.n3).

Modelling Events
-----------------
After describing the minimum features of an event in [play-commons-eventformat](https://github.com/play-project/play-commons/tree/master/play-commons-eventformat/) here is the same example:

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

The event has these features:

* **event ID**: `e1`
* **event type** `:TempEvent`
* **stream** `http://streams.event-processing.org/ids/WeatherStream#stream`
* **time stamp** `2011-08-24T14:40:59.837`

... but the event has no other parameters, its occurrence merely says that it happened (not where it happend nor any useful parameters).

To extend the event with useful parameters ( *payload*, *event body*, ...), we give it a new event type and add RDF properties. Properties can be in the default namespace or we can use dedicated namespaces to avoid any name clashes.

### Example
```none
@PREFIX :     <http://events.event-processing.org/types/> .
@PREFIX xsd:  <http://www.w3.org/2001/XMLSchema#> .
@PREFIX sioc: <http://rdfs.org/sioc/ns#> .

<http://events.event-processing.org/ids/e1> {
    <http://events.event-processing.org/ids/e1#event>
        a :TempEvent ;
        :endTime "2011-08-24T14:40:59.837"^^xsd:dateTime ;
        :stream <http://streams.event-processing.org/ids/WeatherStream#stream> ;
		sioc:content "My new social media status update... :)" ;
		:location <urn:rnd:238742dd:13c583f7675:-7ffa> .
    <urn:rnd:238742dd:13c583f7675:-7ffa>
        a geo:Point ;
        geo:lat "6.0"^^xsd:double ;
        geo:long "7.0"^^xsd:double .
}
```

* `sioc:content` adds a single attribute with a message content, no datatype is specified so it is the default, `xsd:string`
* `:location` adds a *structured* attribute using a randomly generated object with respective properties `a`, `geo:lat`, `geo:long`
* `geo:lat` and `geo:long` add properties to the randomly generated object (not directly to the event)
* `geo:lat` and `geo:long` have typed values of `xsd:double`


User guide
----------
Events can be created in three ways: (1) Using the **PLAY SDK** in Java an event object can be instantiated and getters/setters can be called conveniently for each event property. (2) Using the **builder** class in Java to create events quickly e.g., without a schema. (3) If the complexity of the SDK is not needed or not wanted (Maven dependencies) a **template approach** can be used to create a string:

### Creating events using the PLAY SDK
The PLAY SDK provides Java event classes which are generated using [RDFReactor](http://semanticweb.org/wiki/RDFReactor) to match the schema mentioned above. Example use:
```
		TwitterEvent event = new TwitterEvent(EventHelpers.createEmptyModel(eventId),
				eventId + "#event", true);
		event.setEndTime(Calendar.getInstance());
		event.setTwitterFollowersCount(4);
		event.setTwitterFriendsCount(2);
		event.setTwitterIsRetweet(false);
		event.setTwitterScreenName("roland.stuehmer");
		event.setContent("some Tweet");
		event.setTwitterName("Roland Stühmer");
		event.setStream(new URIImpl(Stream.TwitterFeed.getUri()));
		double longitude = 123;
		double latitude = 345;
		EventHelpers.setLocationToEvent(event, longitude, latitude);
```
Full examples: [EventTypesTest.java](src/test/java/eu/play_project/platformservices/eventformat/EventTypesTest.java)

### Creating events using the PLAY builder class
There is a fluent interface **builder** class to create events. Example:
```
		Event event = EventHelpers.builder()
				.type(UcTelcoCall.RDFS_CLASS)
				.stream(Stream.TaxiUCCall)
				.location(11.0, 5.8)
				.build();
```
The `type()` method shows how constants from the PLAY SDK can be used in the builder, too.

An example using an ad-hoc schema from the made-up namespace `MY`. The `addProperty()` method is used in this case to specify the name of the property in addition to its value because the builder does not know your schema:
```
		final String MY = "http://mynamespace.domain.invalid/";
		
		Event event = EventHelpers.builder(MY + "01234")
				.type(MY + "MyEventType")
				.stream(Stream.TaxiUCCall)
				.addProperty(MY + "myProp", "Hello World!")
				.addRdf(MY + "MySubject", MY + "myProp", "Hello World again!")
				.build();
```

Full examples: [EventBuilderTest.java](src/test/java/eu/play_project/platformservices/eventformat/EventBuilderTest.java)

### Creating events using a string template approach

An example using a template can look like this:
```none
@PREFIX :     <http://events.event-processing.org/types/> .
@PREFIX xsd:  <http://www.w3.org/2001/XMLSchema#> .
@PREFIX sioc: <http://rdfs.org/sioc/ns#> .

<http://events.event-processing.org/ids/%ID%> {
    <http://events.event-processing.org/ids/%ID%#event>
        a :TempEvent ;
        :endTime "%TIMESTAMP%"^^xsd:dateTime ;
        :stream <http://streams.event-processing.org/ids/WeatherStream#stream> ;
		sioc:content "%MYSTATUS%" ;
		:location <urn:rnd:238742dd:13c583f7675:-7ffa> .
    <urn:rnd:238742dd:13c583f7675:-7ffa>
        a geo:Point ;
        geo:lat "%LAT%"^^xsd:double ;
        geo:long "%LON%"^^xsd:double .
}
```
Please note:
* strings like `%ID%`, `%MYSTATUS%`, `%LAT%` and `%LON%` must be replaced with actual values when the event happens.
* `%TIMESTAMP%` needs a date in `xsd:dateTime`, it should be in UTC and use ISO 8601 with this date formatter: `yyyy-MM-dd'T'HH:mm:ss.SSS'Z'`, find the Java constant here: [code](https://github.com/play-project/play-commons/blob/master/play-commons-constants/src/main/java/eu/play_project/play_commons/constants/Event.java)

### Comparison of approaches
Advantages/disadvantages:

1. Using the PLAY SDK
 * PROs: type safety for values, RDF-escaping for values, guidance for new programmers by using clear setters, schema changes will result in clear compile errors, always produces valid/parsable RDF
 * CONs: large Maven footprint, schema is needed at compile time
2. Using the builder
 * PROs: fluent interface, less code, RDF-escaping for values, schema is optional, always produces valid/parsable RDF, validates the event on `build()`
 * CONs: large Maven footprint, you must know your event properties because there is no schema
3. Using a template approach
 * PROs: light-weight approach e.g. for resource-constrained devices, schema is optional, no recompilation needed on schema change
 * CONs: no type safety, no checks in case of schema evolution, no RDF-escaping for values, mistakes can lead to unparsable RDF

A **combined approach** can be reasonable where *templates are used at runtime*, but *a schema is maintained for Unit Testing* to compare a template-generated event dummy with a SDK-generated event dummy at compile time. This test will expose any divergence if the schema should change and the template is forgotten or has syntax errors.

Sending Events and Receiving Events
-----------------------------------
See details at [play-eventadapters](https://github.com/play-project/play-eventadapters).
