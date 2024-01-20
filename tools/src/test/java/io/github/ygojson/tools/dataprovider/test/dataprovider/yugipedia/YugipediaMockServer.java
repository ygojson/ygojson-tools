package io.github.ygojson.tools.dataprovider.test.dataprovider.yugipedia;

import java.io.Closeable;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import okhttp3.mockwebserver.Dispatcher;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.ygojson.tools.test.TestResourceUtils;

/**
 * MockServer used for testing YGOJSON against Yugipedia.
 * <br>
 * All API-endpoints use-cases on tests should be covered in this class.
 */
public class YugipediaMockServer implements Closeable {

	private static final String BASE_RESOURCE_FOLDER = "yugipedia/mockserver/";

	private final Logger log = LoggerFactory.getLogger(this.getClass());
	private final MockWebServer server;

	public YugipediaMockServer() {
		this.server = initServer();
		try {
			this.server.start();
		} catch (final IOException e) {
			throw new IllegalStateException("Cannot start mockserver", e);
		}
	}

	public String getUrl() {
		return server.url("/").toString();
	}

	private MockWebServer initServer() {
		final MockWebServer server = new MockWebServer();
		server.setDispatcher(
			new Dispatcher() {
				@NotNull @Override
				public MockResponse dispatch(@NotNull RecordedRequest recordedRequest) {
					return loadResponseFromFile(recordedRequest.getPath());
				}
			}
		);
		return server;
	}

	private MockResponse createErrorResponse(final int code) {
		final MockResponse response = new MockResponse();
		response.setResponseCode(code);
		return response;
	}

	private MockResponse loadResponseFromFile(final String request) {
		final Path path = getRequestFile(request);
		try {
			if (!Files.exists(path)) {
				log.error("File not found: " + path);
				return createErrorResponse(404);
			}
			final MockResponse response = new MockResponse();
			final String file = Files.readString(path);
			response.setBody(file);
			return response;
		} catch (final IOException e) {
			log.error("Cannot read test-response source: " + path, e);
			return createErrorResponse(500);
		}
	}

	private Path getRequestFile(final String request) {
		final String fileName =
			UUID.nameUUIDFromBytes(request.getBytes()) + ".json";
		try {
			log.info("File {} represents request {}", fileName, request);
			return TestResourceUtils.getTestDataResourcePathByName(
				BASE_RESOURCE_FOLDER + fileName
			);
		} catch (final IllegalStateException e) {
			log.error("Request not found: " + request);
			return Paths.get(fileName);
		}
	}

	@Override
	public void close() throws IOException {
		this.server.close();
	}
}
