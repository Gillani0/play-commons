package org.ow2.play.commons.accesscontrol.api;

/**
 * Permission levels defined by {@link http://www.w3.org/wiki/WebAccessControl}
 * and extended for PLAY by reusing publish/subscribe vocabulary from
 * {@link https://www.oasis-open.org/committees/wsn/}.
 * 
 * @author Roland Stühmer
 */
public enum Permission {
	/**
	 * The right to read from a resource e.g., to query storage.
	 */
	Read("http://www.w3.org/ns/auth/acl#", "Read"),
	/**
	 * The right to write/alter a resrouce such as to add/delete events from storage.
	 */
	Write(Read.getNsUri(), "Write"),
	/**
	 * The right to notify a new event on a stream resource i.e., to publish events.
	 */
	Notify("http://docs.oasis-open.org/wsn/bw-2/NotificationConsumer/", "Notify"),
	/**
	 * The right to subscribe to a stream resource i.e., to receive events in real-time.
	 */
	Subscribe("http://docs.oasis-open.org/wsn/bw-2/NotificationProducer/", "SubscribeRequest");

	private final String name;
	private final String nsUri;
	public static final String ACL_NS_URI = Read.getNsUri();
	public static final String NOTIFY_NS_URI = Notify.getNsUri();
	public static final String SUBSCRIBE_NS_URI = Subscribe.getNsUri();

	Permission(String nsUri, String name) {
		this.nsUri = nsUri;
		this.name = name;
	}

	public String getNsUri() {
		return this.nsUri;
	}

	public String getName() {
		return this.name;
	}

	@Override
	public String toString() {
		return this.nsUri + this.name;
	}
}
