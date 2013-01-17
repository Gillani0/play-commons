package eu.play_project.play_commons.constants;

/**
 * Enum of namespaces commonly used in PLAY events.
 */
public enum Namespace {

	/* 
	 * PLAY specific namespaces:
	 */
	TYPES("", "http://events.event-processing.org/types/"),
	//TYPES2("types", TYPES.getUri()),
	EVENTS("e", "http://events.event-processing.org/ids/"),
	STREAMS("s", "http://streams.event-processing.org/ids/"),
	SOURCE("src", "http://sources.event-processing.org/ids/"),
	UCCRISIS("uccrisis", "http://www.mines-albi.fr/nuclearcrisisevent/"),
	UCTELCO("uctelco", "http://events.event-processing.org/uc/telco/"),
	ESR("esr", "http://imu.ntua.gr/play/esr/mcm/2#"),
	
	/*
	 * generic namespaces:
	 */
	XSD_NS("xsd", "http://www.w3.org/2001/XMLSchema#"),
	RDF("rdf", "http://www.w3.org/1999/02/22-rdf-syntax-ns#"),
	//RDFS("rdfs", "http://www.w3.org/2000/01/rdf-schema#"),
	//OWL("owl", "http://www.w3.org/2002/07/owl#"),
	//GEONAMES("gn", "http://www.geonames.org/ontology#"),
	GEO("geo", "http://www.w3.org/2003/01/geo/wgs84_pos#"),
	USER("user", "http://graph.facebook.com/schema/user#"),
	SIOC("sioc", "http://rdfs.org/sioc/ns#");
	
	private final String NS_PREFIX;
	private final String NS;
	
	Namespace(String NS_PREFIX, String NS) {
		this.NS_PREFIX = NS_PREFIX;
		this.NS = NS;
	}
	
	public String getUri() {
		return NS;
	}

	public String getPrefix() {
		return NS_PREFIX;
	}

}
