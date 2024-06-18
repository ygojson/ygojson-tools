package io.github.ygojson.application.util.http;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;

/**
 * Dummy rest client to test different custom implementations.
 */
@Path("/")
public interface DummyRestClient {
	@GET
	public Response getResponse();
}
