package eu.play_project.play_commons.constants;

import static eu.play_project.play_commons.constants.Namespace.SOURCE;

/**
 * Enum of event source identifiers commonly used in PLAY. The source IDs
 * usually end in {@code #source} and are used when creating metadata about
 * streams, largely when describing the stream in RDF.
 * 
 * @author stuehmer
 * 
 */
public enum Source {

	/*
	 * PLAY sources:
	 */
	Dcep(SOURCE.getUri() + "Dcep#source"),
	WebApp(SOURCE.getUri() + "WebApp#source"),
	FacebookAdapter(SOURCE.getUri() + "FacebookAdapter#source"),
	TwitterAdapter(SOURCE.getUri() + "TwitterAdapter#source"),
	PachubeAdapter(SOURCE.getUri() + "PachubeAdapter#source"),
	UnitTest(SOURCE.getUri() + "UnitTest#source");

	private final String source;

	/**
	 * Source IDs usually end in this special suffix (URI fragment identifier).
	 */
	public static final String SOURCE_ID_SUFFIX = "#source";

	Source(String source) {
		this.source = source;
	}

	public String getUri() {
		return source;
	}
	
	@Override
	public String toString() {
		return source;
	}
}
