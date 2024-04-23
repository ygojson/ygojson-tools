package io.github.ygojson.application.testutil.server;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

/**
 * Record representing the format of the mock-server mocks.json file
 * (a json with a list of this file).
 *
 * @param folder the folder where the file is
 * @param fileName the filename of the response
 * @param request the request matching the mock
 * @param date the date when the mock was created
 */
public record MockDescription(
	String folder,
	@JsonProperty("file") String fileName,
	Date date,
	String request
) {}
