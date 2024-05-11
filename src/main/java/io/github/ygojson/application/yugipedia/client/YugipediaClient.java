package io.github.ygojson.application.yugipedia.client;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

import io.github.ygojson.application.util.http.ClientConfig;
import io.github.ygojson.application.yugipedia.client.params.*;
import io.github.ygojson.application.yugipedia.client.response.QueryResponse;

/**
 * Yugipedia Client with methods specific to YGOJSON.
 */
public interface YugipediaClient {
	/**
	 * Gets the configuration for the Yugipedia client.
	 *
	 * @return the configuration for the Yugipedia client.
	 */
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
	@GET(
		"api.php?action=query" +
		"&format=json&formatversion=2" +
		"&redirects=true" +
		"&prop=revisions" +
		"&rvprop=content|timestamp" +
		"&generator=categorymembers" +
		"&gcmsort=timestamp"
	)
	public Call<QueryResponse> queryCategoryMembersByTimestamp(
		@Query("gcmtitle") Category category,
		@Query("gcmlimit") Limit resultsPerQuery,
		@Query("gcmdir") SortDirection sortDir,
		@Query("gcmcontinue") String gcmcontinue
	);

	/**
	 * Query all the pages on containing a given template.
	 *
	 * @param template template to request.
	 * @param resultsPerQuery the number of results per-query.
	 * @param geicontinue continue token (if {@code null} initial request.
	 * @return the typed JSON response.
	 */
	@GET(
		"api.php?action=query" +
		"&format=json&formatversion=2" +
		"&redirects=true" +
		"&prop=revisions" +
		"&rvprop=content|timestamp" +
		"&generator=embeddedin"
	)
	public Call<QueryResponse> queryPagesWithTemplate(
		@Query("geititle") Template template,
		@Query("geilimit") Limit resultsPerQuery,
		@Query("geicontinue") String geicontinue
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
	@GET(
		"api.php?action=query" +
		"&format=json&formatversion=2" +
		"&redirects=true" +
		"&prop=revisions|categories" +
		"&rvprop=content|timestamp" +
		"&generator=recentchanges" +
		"&grctype=new|edit|categorize" +
		"&grctoponly=true" +
		"&cllimit=max"
	)
	public Call<QueryResponse> queryRecentChanges(
		@Query("grclimit") Limit resultsPerQuery,
		@Query("grcstart") Timestamp startAt,
		@Query("grcend") Timestamp endAt,
		@Query("grccontinue") String grccontinue
	);

	/**
	 * Query the requested pages from the API.
	 *
	 * @param titles the titles to search for.
	 * @return the typed JSON response.
	 */
	@GET(
		"api.php?action=query" +
		"&format=json&formatversion=2" +
		"&redirects=true" +
		"&prop=revisions" +
		"&rvprop=content|timestamp"
	)
	public Call<QueryResponse> queryPagesByTitle(
		@Query("titles") PipeSeparated titles
	);
}
