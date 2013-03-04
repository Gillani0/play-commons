package eu.play_project.play_commons.constants;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Constants {

	private static Properties properties;

	/**
	 * Get the default PLAY properties. Several consecutive calls will return
	 * the <i>cached</i> {@linkplain Properties} object.
	 */
	public static Properties getProperties() {
		if (properties == null) {
			properties = Constants.getProperties("play-commons-constants.properties",
					Constants.getProperties("play-commons-constants.default.properties"));
		}
		return properties;
	}
	
	/**
	 * Produce properties from a specific properties file (to be found on the classpath).
	 */
	public static Properties getProperties(String fileName) {

		Properties result = new Properties();
		InputStream stream = Constants.class.getClassLoader()
				.getResourceAsStream(fileName);
		if (stream != null) {
			try {
				result.load(stream);
			} catch (IOException e) {
				result = new Properties();
			} finally {
				if (stream != null)
					try {
						stream.close();
					} catch (Throwable ignore) {
					}
			}
		}
		return result;
	}
	
	/**
	 * Produce properties from a specific properties file (to be found on the
	 * classpath). The second parameter passes a default properties object.
	 */
	public static Properties getProperties(String fileName, Properties defaultProperties) {
		Properties result = new Properties(defaultProperties);
		InputStream stream = Constants.class.getClassLoader()
				.getResourceAsStream(fileName);
		if (stream != null) {
			try {
				result.load(stream);
			} catch (IOException e) {
				result = new Properties(defaultProperties);
			} finally {
				if (stream != null)
					try {
						stream.close();
					} catch (Throwable ignore) {
					}
			}
		}
		return result;
	}
}
