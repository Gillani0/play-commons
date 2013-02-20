package org.ow2.play.commons.accesscontrol;

/**
 * Permission levels defined by {@link http://www.w3.org/wiki/WebAccessControl}.
 * 
 * @author stuehmer
 */
public enum Permission {
	/**
	 * Read a resource such as subscribe to a stream.
	 */
	Read,
	/**
	 * Write/alter a resrouce such as add/delete events. 
	 */
	Write,
	/**
	 * Append a resource such as a stream by publishing events.
	 */
	Append
}
