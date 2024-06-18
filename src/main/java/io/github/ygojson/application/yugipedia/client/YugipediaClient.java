package io.github.ygojson.application.yugipedia.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.quarkus.rest.client.reactive.ClientExceptionMapper;
import io.quarkus.rest.client.reactive.ClientQueryParam;
import io.quarkus.rest.client.reactive.jackson.ClientObjectMapper;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.rest.client.annotation.RegisterClientHeaders;
import org.eclipse.microprofile.rest.client.annotation.RegisterProvider;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import io.github.ygojson.application.util.http.ClientConfig;
import io.github.ygojson.application.util.http.YgoJsonAppUserAgentHeaderFactory;
import io.github.ygojson.application.yugipedia.YugipediaException;
import io.github.ygojson.application.yugipedia.client.params.*;
import io.github.ygojson.application.yugipedia.client.response.QueryResponse;

/**
 * Yugipedia Client with methods specific to YGOJSON.
 */
@RegisterRestClient(
	baseUri = "https://yugipedia.com",
	configKey = YugipediaClient.NAME
)
@Path("/api.php")
@ClientQueryParam(name = "format", value = "json")
@ClientQueryParam(name = "formatversion", value = "2")
@ClientQueryParam(name = "redirects", value = "true")
@RegisterClientHeaders(YgoJsonAppUserAgentHeaderFactory.class)
@RegisterProvider(YugipediaRateLimitRequestFilter.class)
public interface YugipediaClient {
	/**
	 * The name of the client (and config-key).
	 */
	String NAME = "yugipedia";

	/**
	 * Gets the configuration for the Yugipedia client.
	 *
	 * @return the configuration for the Yugipedia client.
	 * @deprecated constructing with the {@link io.github.ygojson.application.util.http.ClientFactory} not any longer supported.
	 */
	@Deprecated
	static ClientConfig<YugipediaClient> getConfig() {
		return new Config();
	}

	/**
	 * Query all the pages on a given category sorted by timestamp.
	 *
	 * @param category        category to request.
	 * @param resultsPerQuery the number of results per-query.
	 * @param sortDir         direction of the sort
	 * @param gcmcontinue     continue token (if {@code null} initial request.
	 * @return the typed JSON response.
	 */
	@GET
	@ClientQueryParam(name = "action", value = "query")
	@ClientQueryParam(name = "prop", value = "revisions")
	@ClientQueryParam(name = "rvprop", value = "content|timestamp")
	@ClientQueryParam(name = "generator", value = "categorymembers")
	@ClientQueryParam(name = "gcmsort", value = "timestamp")
	public QueryResponse queryCategoryMembersByTimestamp(
		@QueryParam("gcmtitle") Category category,
		@QueryParam("gcmlimit") Limit resultsPerQuery,
		@QueryParam("gcmdir") SortDirection sortDir,
		@QueryParam("gcmcontinue") String gcmcontinue
	);

	/**
	 * Query all the pages on containing a given template.
	 *
	 * @param template template to request.
	 * @param resultsPerQuery the number of results per-query.
	 * @param geicontinue continue token (if {@code null} initial request.
	 * @return the typed JSON response.
	 */
	@GET
	@ClientQueryParam(name = "action", value = "query")
	@ClientQueryParam(name = "prop", value = "revisions")
	@ClientQueryParam(name = "rvprop", value = "content|timestamp")
	@ClientQueryParam(name = "generator", value = "embeddedin")
	public QueryResponse queryPagesWithTemplate(
		@QueryParam("geititle") Template template,
		@QueryParam("geilimit") Limit resultsPerQuery,
		@QueryParam("geicontinue") String geicontinue
	);

	/**
	 * Query all recent changes from the API.
	 *
	 * @param resultsPerQuery the number of results per-query.
	 * @param startAt         initial date to start requesting rencent changes.
	 * @param endAt           final date to stop requesting rencent changes.
	 * @param grccontinue     continue token (if {@code null} initial request.
	 * @return the typed JSON response.
	 */
	@GET
	@ClientQueryParam(name = "action", value = "query")
	@ClientQueryParam(name = "prop", value = "revisions|categories")
	@ClientQueryParam(name = "rvprop", value = "content|timestamp")
	@ClientQueryParam(name = "generator", value = "recentchanges")
	@ClientQueryParam(name = "grctype", value = "new|edit|categorize")
	@ClientQueryParam(name = "grctoponly", value = "true")
	@ClientQueryParam(name = "cllimit", value = "max")
	public QueryResponse queryRecentChanges(
		@QueryParam("grclimit") Limit resultsPerQuery,
		@QueryParam("grcstart") Timestamp startAt,
		@QueryParam("grcend") Timestamp endAt,
		@QueryParam("grccontinue") String grccontinue
	);

	/**
	 * Query the requested pages from the API.
	 *
	 * @param titles the titles to search for.
	 * @return the typed JSON response.
	 */
	@GET
	@ClientQueryParam(name = "action", value = "query")
	@ClientQueryParam(name = "prop", value = "revisions")
	@ClientQueryParam(name = "rvprop", value = "content|timestamp")
	public QueryResponse queryPagesByTitle(
		@QueryParam("titles") PipeSeparated titles
	);

	/**
	 * Object mapper to be used.
	 *
	 * @param defaultObjectMapper default object-mapper.
	 *
	 * @return the default object mapper.
	 */
	@ClientObjectMapper
	static ObjectMapper objectMapper(ObjectMapper defaultObjectMapper) {
		return defaultObjectMapper.copy().registerModule(new JavaTimeModule());
	}

	/**
	 * Maps the client exceptions to {@link YugipediaException}.
	 * <br>
	 * Note that server/other exceptions are mapped to plain {@link RuntimeException}.
	 *
	 * @param response the response to map.
	 *
	 * @return the exception to throw.
	 */
	@ClientExceptionMapper
	static RuntimeException toException(Response response) {
		final Response.StatusType type = response.getStatusInfo();
		return switch (type.getFamily()) {
			case CLIENT_ERROR -> new YugipediaException(type.getReasonPhrase());
			case SERVER_ERROR, OTHER -> new RuntimeException(type.getReasonPhrase());
			default -> null;
		};
	}
}
