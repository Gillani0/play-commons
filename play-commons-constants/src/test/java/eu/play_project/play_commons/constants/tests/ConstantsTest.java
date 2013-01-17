package eu.play_project.play_commons.constants.tests;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Properties;

import org.junit.Test;

import eu.play_project.play_commons.constants.Constants;

public class ConstantsTest {

	/**
	 * Some tests to make sure that the PLAY constants are properly loaded from
	 * the properties file.
	 */
	@Test
	public void testConstants() {
		Properties properties = Constants.getProperties();
		assertTrue("The PLAY constants did not contains any properties.", !properties.isEmpty());
		assertNotNull("The PLAY constants are missing at least one known property.", Constants.getProperties().getProperty(
				"governance.eventgovernance.endpoint"));
	}
	
	@Test
	public void testConstantsFromSpecificFileName() {
		int i = 0;
		boolean found = false;
		String[] strings = Constants.getProperties("testfile.properties").getProperty("dcep.startup.registerqueries").split(",");
		for (String queryFileName : strings) {
			queryFileName = queryFileName.trim();
			System.out.println("Property value found: " + queryFileName);
			i++;
			if (queryFileName.equals("play-bdpl-crisis-03-drawgraph.eprq")) {
				found = true;
			}
		}
		assertTrue("There was no single value found in the comma-separated property.", i > 0);
		assertTrue("An expected string was not found in the loaded properties.", found);
	}
}
