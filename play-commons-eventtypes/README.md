PLAY Modelling Events for PLAY
==============================
Events to be sent to [PLAY](http://www.play-project.eu/) **should** have an RDF Schema, it is defined in [types.n3](src/main/resources/types.n3).

Modelling Events
-----------------
After describing the minimum features of an event in [play-commons-eventformat](https://github.com/play-project/play-commons/tree/master/play-commons-eventformat/) here is the same example:

### Example
```
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

To extend the event with useful parameters (*payload*, *event body*, ...), we give it a new event type and add RDF properties. Properties can be in the default namespace or we can use dedicated namespaces to avoid any name clashes.

### Example
```
@PREFIX :     <http://events.event-processing.org/types/> .
@PREFIX xsd:  <http://www.w3.org/2001/XMLSchema#> .
@prefix sioc: <http://rdfs.org/sioc/ns#> .

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


Instantiating Events
--------------------
Events can be created in two ways: Using the PLAY SDK in Java an event object can be instantiated and getters/setters can be called conveniently for each event property. If the complexity of the SDK is not needed or not wanted (Maven dependencies) a template approach can be used to create a string like the above examples.

Examples how to create an event using the SDK: <a href="src/test/java/eu/play_project/platformservices/eventformat/EventTypesTest.java">EventTypesTest.java</a>

An example using a template can look like this:

```
@PREFIX :     <http://events.event-processing.org/types/> .
@PREFIX xsd:  <http://www.w3.org/2001/XMLSchema#> .
@prefix sioc: <http://rdfs.org/sioc/ns#> .

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

... where strings like `%ID%`, `%MYSTATUS%`, `%LAT%` and `%LON%` must be replaced with actual values when the event happens.

### Comparison of approaches

Advantages/disadvantages:

1. Using the PLAY SDK
 * PROs: type safety for values, RDF-escaping for values, guidance for new programmers by using clear setters, schema changes will result in clear compile errors, always produces valid/parsable RDF
 * CONs: large Maven footprint, schema is needed at compile time
2. Using a template approach
 * PROs: light-weight approach e.g. for resource-constrained devices, schema is optional, no recompilation needed on schema change
 * CONs: no type safety, no checks in case of schema evolution, no RDF-escaping for values, mistakes can lead to unparsable RDF

A **combined approach** can be reasonable where *templates are used at runtime*, but *a schema is maintained for Unit Testing* to compare a template-generated event dummy with a SDK-generated event dummy at compile time. This test will expose any divergence if the schema should change and the template is forgotten or has syntax errors.
