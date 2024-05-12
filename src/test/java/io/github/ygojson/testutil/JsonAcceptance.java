package io.github.ygojson.testutil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.MessageFormat;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.util.DefaultIndenter;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.json.JSONException;
import org.opentest4j.AssertionFailedError;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.ygojson.application.testutil.TestResourceUtil;

public class JsonAcceptance {

	private static final Logger LOGGER = LoggerFactory.getLogger(
		JsonAcceptance.class.getName()
	);
	private static final ObjectMapper OBJECT_MAPPER = createObjectMapper();
	private final Path expectedFolder;

	public JsonAcceptance(final Path expectedFolder) {
		this.expectedFolder = expectedFolder;
	}

	public static JsonAcceptance fromTestClass(final Class<?> testClass) {
		return new JsonAcceptance(
			TestResourceUtil.getPackageResourceFolder(testClass)
		);
	}

	private static ObjectMapper createObjectMapper() {
		final DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
		prettyPrinter.indentArraysWith(DefaultIndenter.SYSTEM_LINEFEED_INSTANCE);
		return new ObjectMapper()
			.enable(SerializationFeature.INDENT_OUTPUT)
			.enable(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS)
			.setSerializationInclusion(JsonInclude.Include.NON_NULL)
			.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
			.registerModule(new JavaTimeModule())
			.setDefaultPrettyPrinter(prettyPrinter);
	}

	public void verify(final String testCase, final Object value) {
		final Path receivedFile = expectedFolder.resolve(
			testCase + ".received.json"
		);
		final Path approvedFile = expectedFolder.resolve(
			testCase + ".approved.json"
		);
		// create the received file with the content of the object
		final String received = writeReceived(receivedFile, value);
		final String approved = readApproved(approvedFile, receivedFile);
		// assertion
		assertJson(getErrorMsg(approvedFile, receivedFile), approved, received);
		// compare the received file with the expected file
		// delete received file if it exists at the end of the assertion
		deleteReceived(receivedFile);
	}

	private void assertJson(
		final String message,
		final String approved,
		final String received
	) {
		try {
			JSONAssert.assertEquals(
				message,
				approved,
				received,
				JSONCompareMode.STRICT
			);
		} catch (JSONException e) {
			throw new AssertionFailedError(e.getMessage());
		}
	}

	private String readApproved(
		final Path approvedFile,
		final Path receivedFile
	) {
		try {
			return Files.readString(approvedFile);
		} catch (IOException e) {
			throw new AssertionFailedError(
				getErrorMsg(approvedFile, receivedFile),
				e
			);
		}
	}

	private String getErrorMsg(final Path approved, final Path received) {
		return MessageFormat.format(
			"Expected file {0} not found. Accept the received file {1} to approve",
			approved,
			received
		);
	}

	private String writeReceived(final Path receivedFile, final Object value) {
		String json;
		try {
			json = OBJECT_MAPPER.writeValueAsString(value);
		} catch (IOException e) {
			throw new AssertionFailedError("Cannot write JSON file", e);
		}
		try {
			Files.writeString(receivedFile, json);
		} catch (IOException e) {
			LOGGER.error("Error writing received file", e);
		}
		return json;
	}

	private void deleteReceived(final Path receivedFile) {
		try {
			Files.deleteIfExists(receivedFile);
		} catch (IOException e) {
			LOGGER.error("Error deleting received file", e);
		}
	}
}
