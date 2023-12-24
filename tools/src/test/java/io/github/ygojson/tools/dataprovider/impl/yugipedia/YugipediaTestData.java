package io.github.ygojson.tools.dataprovider.impl.yugipedia;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.github.ygojson.tools.test.TestResourceUtils;

/**
 * Resource class with wikitext data.
 */
public class YugipediaTestData {

	private static Path BASE_PATH =
		TestResourceUtils.getTestDataResourcePathByName("yugipedia");

	private static final Map<String, Path> PARSE_WIKITEXT_PAGE_DATA =
		new HashMap<>();

	private YugipediaTestData() {
		// utility class
	}

	/**
	 * Test data for parse wikitext page.
	 *
	 * @param testName name of the test (indicating why it was selected)
	 * @param pageId the ID on yugipedia that corresponds to this data
	 * @param pageTitle the title of the page on yugipedia that corresponds to this data
	 * @param wikitext the wikitext of the page.
	 */
	public record ParseWikitextPageTestData(
		String testName,
		Long pageId,
		String pageTitle,
		String wikitext
	) {}

	/**
	 * Get all the parse wikitext page test data.
	 * <br>
	 * This contains similar information to a single query page on yugipedia,
	 * including a test name indicating why this test is useful.
	 *
	 * @return list of current test data.s
	 */
	public static List<ParseWikitextPageTestData> getParseWikitextTestData() {
		return PARSE_WIKITEXT_PAGE_DATA
			.entrySet()
			.stream()
			.map(entry -> {
				try (
					final BufferedReader reader = Files.newBufferedReader(
						entry.getValue()
					)
				) {
					final JsonNode parse = new ObjectMapper()
						.readTree(reader)
						.get("parse");
					return new ParseWikitextPageTestData(
						entry.getKey(),
						parse.get("pageid").asLong(),
						parse.get("title").asText(),
						parse.get("wikitext").asText()
					);
				} catch (final IOException e) {
					throw new IllegalStateException("Cannot read test data", e);
				}
			})
			.toList();
	}

	static {
		// to register a new parse_wikitext_page file, it should be grabbed from the yugipedia api
		// https://yugipedia.com/api.php?action=parse&format=json&formatversion=2&prop=wikitext&page=${page_name}
		// where page_name is the one for the card that should be used for the specific test
		// the filename should indicate the reason the card is considered for a test (with extension of .json)
		// this method scans that folder and adds directly to the tests (failing the first time because cause it needs to be approved)
		registerAllParseWikitextPageData();
	}

	private static void registerAllParseWikitextPageData() {
		final Path parseWikitextPageFolder = BASE_PATH.resolve(
			"parse_wikitext_page"
		);
		try {
			Files
				.list(parseWikitextPageFolder)
				.forEach(YugipediaTestData::registerParseWikitextPageData);
		} catch (final IOException e) {
			throw new IllegalStateException("Cannot register test data", e);
		}
	}

	private static void registerParseWikitextPageData(
		final Path parseWikitextPage
	) {
		final String testName = parseWikitextPage
			.getFileName()
			.toString()
			.replaceAll(".json", "");
		if (Files.notExists(parseWikitextPage)) {
			throw new IllegalStateException(
				"Resource not found: " + parseWikitextPage
			);
		}
		PARSE_WIKITEXT_PAGE_DATA.put(testName, parseWikitextPage);
	}
}
