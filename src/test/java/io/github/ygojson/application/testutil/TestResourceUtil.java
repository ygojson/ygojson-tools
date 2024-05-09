package io.github.ygojson.application.testutil;

import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Resources utils for getting test-data from the classpath.
 */
public class TestResourceUtil {

	private static final String TEST_DATA_FOLDER = "testdata/";

	private TestResourceUtil() {
		// utility class
	}

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
				TestResourceUtil.class.getClassLoader().getResource(folder);
			if (url == null) {
				throw new IllegalStateException(
					"Exception retrieving test-resource: " + folder
				);
			}
			return Paths.get(url.toURI());
		} catch (final URISyntaxException e) {
			throw new IllegalStateException("Exception retrieving test-resource", e);
		}
	}
}
