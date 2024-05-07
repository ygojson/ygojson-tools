package io.github.ygojson.application.testutil.server;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import okhttp3.mockwebserver.Dispatcher;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import io.github.ygojson.application.testutil.TestResourceUtil;

/**
 * Mocked server where mocks are described on a mocks.json file.
 */
public class MockedServer extends Dispatcher implements Closeable {

	private static final String MOCKS_JSON = "mocks.json";

	private final String baseFolder;
	private final Logger log;
	private final MockWebServer server;
	private Map<String, MockDescription> mockByRequest = new HashMap<>();

	/**
	 * Default constructor
	 *
	 * @param baseFolder base-folder where the descriptor json is located.
	 * @param log the log to use
	 */
	public MockedServer(final String baseFolder, final Logger log) {
		this.baseFolder = baseFolder;
		this.log = log;
		this.server = initServer();
		initMocks();
		try {
			this.server.start();
		} catch (final IOException e) {
			throw new IllegalStateException("Cannot start mockserver", e);
		}
	}

	private MockWebServer initServer() {
		final MockWebServer server = new MockWebServer();
		server.setDispatcher(this);
		return server;
	}

	private void initMocks() {
		final Path mocksFile = TestResourceUtil.getTestDataResourcePathByName(
			baseFolder + "/" + MOCKS_JSON
		);
		try (final BufferedReader reader = Files.newBufferedReader(mocksFile)) {
			new ObjectMapper()
				.registerModule(new JavaTimeModule())
				.readValue(reader, new TypeReference<List<MockDescription>>() {})
				.forEach(mock -> this.mockByRequest.put(mock.request(), mock));
			log.info(
				"Loaded {} mock-responses from {}",
				this.mockByRequest.size(),
				MOCKS_JSON
			);
		} catch (final IOException e) {
			throw new IllegalStateException("Cannot read " + mocksFile, e);
		}
	}

	public String getUrl() {
		return server.url("/").toString();
	}

	@NotNull @Override
	public MockResponse dispatch(@NotNull RecordedRequest recordedRequest) {
		if (recordedRequest.getPath() == null) {
			return createErrorResponse(500);
		}
		final String request = URLDecoder.decode(
			recordedRequest.getPath(),
			StandardCharsets.UTF_8
		);
		final MockDescription mock = mockByRequest.get(request);
		log.info("Request: {}", request);

		if (mock == null) {
			log.error("MOCK NOT FOUND");
			return createErrorResponse(400);
		}
		log.info("Mock-description: {}", mock);
		try {
			final Path path = TestResourceUtil.getTestDataResourcePathByName(
				baseFolder + "/" + mock.folder() + "/" + mock.fileName()
			);
			if (!Files.exists(path)) {
				log.error("Mock file not found " + mock);
				return createErrorResponse(404);
			}
			final MockResponse response = new MockResponse();
			final String file = Files.readString(path);
			response.setBody(file);
			return response;
		} catch (final IOException e) {
			log.error("Cannot read mocked response: " + mock, e);
			return createErrorResponse(500);
		}
	}

	private MockResponse createErrorResponse(final int code) {
		final MockResponse response = new MockResponse();
		response.setResponseCode(code);
		return response;
	}

	@Override
	public void close() throws IOException {
		this.server.close();
	}
}
