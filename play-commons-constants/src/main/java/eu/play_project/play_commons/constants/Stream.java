package eu.play_project.play_commons.constants;

import static eu.play_project.play_commons.constants.Namespace.STREAMS;

import javax.xml.namespace.QName;

/**
 * Enum of stream identifiers (also used for topic names) commonly used in PLAY.
 * Full stream IDs ending in {@code #stream} are used when creating metadata
 * about streams, largely when describing the stream in RDF. Shorter IDs without
 * this suffix are used for topics on the DSB which can be obtained by
 * {@linkplain Stream#getTopicUri()} and {@linkplain Stream#getTopicQName()}.
 */
public enum Stream {

	/*
	 * PLAY streams:
	 */
	// FZI
	FacebookStatusFeed(STREAMS, "FacebookStatusFeed#stream"),
	FacebookCepResults(STREAMS, "FacebookCepResults#stream"),
	PachubeFeed(STREAMS, "PachubeFeed#stream"),
	TwitterFeed(STREAMS, "TwitterFeed#stream"),
	PersonalStream1(STREAMS, "PersonalStream1#stream"),
	// ARMINES
	SituationalEventStream(STREAMS, "situationalEvent#stream"),
	SituationalAlertEventStream(STREAMS, "situationalAlertEvent#stream"),
	ActivityEventStream(STREAMS, "activityEvent#stream"),
	ResourcesEventStream(STREAMS, "resourcesEvent#stream"),
	ConsequenceEventStream(STREAMS, "consequenceEvent#stream"),
	VisualizationEventStream(STREAMS, "visualizationEvent#stream"),
	// ICCS
	ESRRecom(STREAMS, "ESRRecom#stream"),
	SARRecom(STREAMS, "SARRecom#stream"),
	VesselStream(STREAMS, "VesselStream#stream"),
	ProximityInfoStream(STREAMS, "ProximityInfoStream#stream"),
	// ORANGE
	//TaxiUCAvailability(STREAMS, "TaxiUCAvailability#stream"),
	TaxiUCCall(STREAMS, "TaxiUCCall#stream"),
	TaxiUCClic2Call(STREAMS, "TaxiUCClic2Call#stream"),
	TaxiUCFaceBook(STREAMS, "TaxiUCFaceBook#stream"),
	TaxiUCGeoLocation(STREAMS, "TaxiUCGeoLocation#stream"),
	//TaxiUCIncomingCall(STREAMS, "TaxiUCIncomingCall#stream"),
	//TaxiUCOutNetwork(STREAMS, "TaxiUCOutNetwork#stream"),
	//TaxiUCPresence(STREAMS, "TaxiUCPresence#stream"),
	//TaxiUCSMSCustomerAlert(STREAMS, "TaxiUCSMSCustomerAlert#stream"),
	//TaxiUCTrafficJam(STREAMS, "TaxiUCTrafficJam#stream"),
	TaxiUCTwitter(STREAMS, "TaxiUCTwitter#stream"),
	//TaxiUCUnexpected(STREAMS, "TaxiUCUnexpected#stream"),
	ContextualizedLatitudeFeed(STREAMS, "ContextualizedLatitudeFeed#stream"),
	TaxiUCESRRecom(STREAMS, "TaxiUCESRRecom#stream"),
	TaxiUCESRRecomDcep(STREAMS, "TaxiUCESRRecomDcep#stream"),
	Friend_Geo_Event(STREAMS, "Friend_Geo_Event#stream"),
	TaxiUCAnswer(STREAMS, "TaxiUCAnswer#stream");
	
	private final QName qname;
	
	/**
	 * Stream IDs usually end in this special suffix (URI fragment identifier).
	 */
	public static final String STREAM_ID_SUFFIX = "#stream";
	
	/*
	 * Some constants for stream attribute keys:
	 */
	/** The stream title attribute. Values should be 30 characters max. */
	public static final String STREAM_TITLE = "http://purl.org/dc/elements/1.1/title";
	/** The stream descriptions attribute. */
	public static final String STREAM_DESCRIPTION = "http://purl.org/dc/elements/1.1/description";
	/** The stream icon attribute. Value should be a uri to the image. Size should be like a favicon. The attribute is also used in {@linkplain Event}. */
	public static final String STREAM_ICON = "http://www.w3.org/2002/06/xhtml2/icon";
	/** The fully qualified topic URI */
	public static final String STREAM_TOPIC = "http://www.play-project.eu/xml/ns/topic";
	/** Whether a stream has complex or not. Defaults to false. Boolean. */
	public static final String STREAM_COMPLEXEVENTS = "http://www.play-project.eu/xml/ns/complexEvents";
	/** Whether DSB needs to subscribe to the EventCloud, i.e. EC->DSB. Defaults to false (DSB->EC). Boolean. */
	public static final String STREAM_DSBNEEDSTOSUBSCRIBE = "http://www.play-project.eu/xml/ns/dsbneedstosubscribe";
	

	Stream(String namespaceUri, String localpart, String prefix) {
		this.qname = new QName(namespaceUri, localpart, prefix);
	}

	Stream(Namespace namespace, String localpart) {
		this.qname = new QName(namespace.getUri(), localpart, namespace.getPrefix());
	}

	Stream(QName qname) {
		this.qname = qname;
	}
	
	public QName getQName() {
		return qname;
	}

	public String getUri() {
		return qname.getNamespaceURI() + qname.getLocalPart();
	}

	/**
	 * Obtain short stream IDs for use with the DSB. This representation omits
	 * the trailing {@code #stream} suffix.
	 */
	public String getTopicUri() {
		return toTopicUri(getUri());
	}
	
	/**
	 * Obtain short stream IDs for use with the DSB. This representation omits
	 * the trailing {@code #stream} suffix.
	 */
	public static String toTopicUri(String streamId) {
		return streamId.substring(0, streamId.lastIndexOf("#"));
	}

	/**
	 * Obtain short stream IDs for use with the DSB. This representation omits
	 * the trailing {@code #stream} suffix.
	 */
	public QName getTopicQName() {
		return toTopicQName(this.getQName());
	}
	
	/**
	 * Obtain short stream IDs for use with the DSB. This representation omits
	 * the trailing {@code #stream} suffix.
	 */
	public static QName toTopicQName(QName streamId) {
		return new QName(
				streamId.getNamespaceURI(),
				streamId.getLocalPart().substring(0, streamId.getLocalPart().lastIndexOf("#")),
				streamId.getPrefix());
	}

	@Override
	public String toString() {
		return getUri();
	}

}
