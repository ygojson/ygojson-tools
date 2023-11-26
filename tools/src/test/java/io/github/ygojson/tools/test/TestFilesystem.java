package io.github.ygojson.tools.test;

import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

import com.google.common.jimfs.Configuration;
import com.google.common.jimfs.Jimfs;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.FileSystemUtils;

/**
 * Test filesystem with utility methods.
 */
@Slf4j
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class TestFilesystem {

	private final FileSystem fs;
	private Path currentTestRootDir;

	/**
	 * Initialize a new filesystem.
	 * <br>
	 * To be used before a test class that requires filesystem.
	 */
	public static final TestFilesystem init() {
		// use only unix-configuration for filesystem tests
		return new TestFilesystem(Jimfs.newFileSystem(Configuration.unix()));
	}

	/**
	 * Gets the current test root directory.
	 *
	 * @return the current test root directory.
	 */
	private synchronized Path getCurrentTestRootDir() {
		if (currentTestRootDir == null) {
			try {
				currentTestRootDir = fs.getPath("/" + UUID.randomUUID());
				Files.createDirectories(currentTestRootDir);
			} catch (IOException e) {
				log.error("Unable to create test-root directory");
				throw new RuntimeException("Error on test-filesystem", e);
			}
		}
		return currentTestRootDir;
	}

	public Path getTestRoot() {
		return getCurrentTestRootDir();
	}

	public Path getFile(final String name) {
		return getCurrentTestRootDir().resolve(name);
	}

	public Path createDir(final String name) {
		try {
			final Path dir = getFile(name);
			return Files.createDirectories(dir);
		} catch (IOException e) {
			log.error("Unable to create test-directory");
			throw new RuntimeException("Error on test-filesystem", e);
		}
	}

	public Path createFileWithContent(
		final String filename,
		final CharSequence content
	) {
		try {
			return Files.writeString(getFile(filename), content);
		} catch (IOException e) {
			log.error("Cannot create file on test-filesystem");
			throw new RuntimeException("Exception on test-filesystem", e);
		}
	}

	/**
	 * Cleanup the test filesytem.
	 * <br>
	 * To be used after each test.
	 */
	public synchronized void cleanup() {
		if (currentTestRootDir != null) {
			try {
				FileSystemUtils.deleteRecursively(currentTestRootDir);
			} catch (IOException e) {
				log.error("Unable to cleanup the test filesystem", e);
			}
			currentTestRootDir = null;
		}
	}

	public synchronized void close() {
		try {
			fs.close();
		} catch (IOException e) {
			log.error("Cannot close test filesystem", e);
		}
	}
}
