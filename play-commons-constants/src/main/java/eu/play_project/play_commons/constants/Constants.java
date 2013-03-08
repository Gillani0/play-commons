package eu.play_project.play_commons.constants;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Properties for the PLAY Project. This class reads the file
 * {@code play-commons-constants.properties} from the classpath and also has a
 * default file included in the JAR.
 * 
 * @author Roland Stühmer
 */
public class Constants {

	private static Properties properties;
	private static final String PROPERTIES = "play-commons-constants.properties";
	private static final String PROPERTIES_DEFAULTS = "play-commons-constants-defaults.properties";

	/**
	 * Get the default PLAY properties. Several consecutive calls will return
	 * the <i>cached</i> {@linkplain Properties} object.
	 */
	public static Properties getProperties() {
		if (properties == null) {
			properties = Constants.getProperties(PROPERTIES,
					Constants.getProperties(PROPERTIES_DEFAULTS));
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
