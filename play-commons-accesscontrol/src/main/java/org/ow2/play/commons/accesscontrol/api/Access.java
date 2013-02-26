package org.ow2.play.commons.accesscontrol.api;

/**
 * Validate permissions based on URIs for agents, objects and rights.
 * 
 * @author Roland Stühmer
 */
public interface Access {

	/**
	 * Check if a given <i>agent</i> for a given <i>document or stream</i> has
	 * the right <i>permission</i>.
	 * 
	 * @param agentUri
	 *            URI of a given user or group
	 * @param documentUri
	 *            URI of a stream
	 * @param permission
	 *            the permission level
	 */
	public boolean check(String agentUri, String documentUri,
			Permission permission);

	/**
	 * Refresh the in-memory permission information from storage.
	 */
	void flushPermissions() throws AccessManagementException;

}