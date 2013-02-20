package org.ow2.play.commons.accesscontrol;

import java.util.List;

import org.ow2.play.metadata.api.Data;
import org.ow2.play.metadata.api.Metadata;
import org.ow2.play.metadata.api.MetadataException;
import org.ow2.play.metadata.api.Resource;
import org.ow2.play.metadata.client.MetadataClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.play_project.play_commons.constants.Constants;
import eu.play_project.play_commons.constants.Stream;

public class Acl {
	
	private static String METADATA_SERVICE = 
			Constants.getProperties().getProperty("governance.metadataservice.endpoint");
	
	private Logger logger;

	public Acl() {
		this.logger = LoggerFactory.getLogger(this.getClass());
	}

	public boolean check(String agentUri, String documentUri, Permission permission) {
		try {
			MetadataClient client = new MetadataClient(METADATA_SERVICE);
			List<Metadata> metadataList;
			Resource r = new org.ow2.play.metadata.api.Resource(
					"stream", Stream.VesselStream.getTopicUri());
			metadataList = client
					.getMetaData(r);
			for (Metadata m : metadataList) {
				for (Data d : m.getData()) {
					logger.info(String.format("%s %s %s", r.getUrl(), m.getName(), d.getValue()));
				}
			}
		} catch (MetadataException e) {
			logger.warn("Exception", e);
		}

		return false;
	}
}
