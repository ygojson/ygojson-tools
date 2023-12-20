package io.github.ygojson.tools.test;

import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Resources utils for getting test-data from the classpath.
 */
public class TestResourceUtils {

	private TestResourceUtils() {
		// utility class
	}

	private static final String TEST_DATA_FOLDER = "testdata/";

	/**
	 * Gets the resource on the testdata directory.
	 *
	 * @param name name of the resource (can include subfolders).
	 *
	 * @return path to the resource.
	 *
	 * @throws RuntimeException if the resource cannot be found.
	 */
	public static Path getTestDataResourcePathByName(final String name) {
		try {
			final String folder = TEST_DATA_FOLDER + name;
			final URL url =
				TestResourceUtils.class.getClassLoader().getResource(folder);
			if (url == null) {
				throw new RuntimeException(
					"Exception retrieving test-resource: " + folder
				);
			}
			return Paths.get(url.toURI());
		} catch (final URISyntaxException e) {
			throw new RuntimeException("Exception retrieving test-resource", e);
		}
	}
}
