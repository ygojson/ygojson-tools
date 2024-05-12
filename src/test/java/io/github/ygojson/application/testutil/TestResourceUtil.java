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
	 * Gets the resource on the package resource folder.
	 *
	 * @param clazz the test class to find the resource for.
	 *
	 * @return a path on the resources folder for the package.
	 */
	public static Path getPackageResourceFolder(final Class<?> clazz) {
		final String folderName = clazz.getPackageName().replace('.', '/');
		return getPathOnClassPath(clazz.getClassLoader(), folderName);
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
		return getPathOnClassPath(
			TestResourceUtil.class.getClassLoader(),
			TEST_DATA_FOLDER + name
		);
	}

	private static Path getPathOnClassPath(
		final ClassLoader loader,
		final String name
	) {
		try {
			final URL url = loader.getResource(name);
			if (url == null) {
				throw new IllegalStateException(
					"Exception retrieving test-resource: " + name
				);
			}
			return Paths.get(url.toURI());
		} catch (final URISyntaxException e) {
			throw new IllegalStateException("Exception retrieving test-resource", e);
		}
	}
}
