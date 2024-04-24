package io.github.ygojson.application.yugipedia;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.github.ygojson.application.testutil.TestResourceUtil;

/**
 * Singleton test-helper containing some yugipedia models.
 */
public class YugipediaTestDataRegistry {

	private static final Path PARSE_WIKITEXT_PAGE_PATH =
		TestResourceUtil.getTestDataResourcePathByName(
			"yugipedia/parse_wikitext_page"
		);

	// registry
	private final Map<String, Path> cardTable2Wikitext = new HashMap<>();
	private final Map<String, Path> infoBoxSetWikitext = new HashMap<>();

	// cache values
	private List<WikitextPageTestCase> cardTable2Cache;
	private List<WikitextPageTestCase> infoBoxSetCache;

	private static YugipediaTestDataRegistry INSTANCE;

	/**
	 * Test data for parse wikitext page.
	 *
	 * @param testName name of the test (indicating why it was selected)
	 * @param pageId the ID on yugipedia that corresponds to this data
	 * @param pageTitle the title of the page on yugipedia that corresponds to this data
	 * @param wikitext the wikitext of the page.
	 */
	public record WikitextPageTestCase(
		String testName,
		Long pageId,
		String pageTitle,
		String wikitext
	) {}

	private YugipediaTestDataRegistry() {
		// to register a new parse_wikitext_page file, it should be grabbed from the yugipedia api
		// https://yugipedia.com/api.php?action=parse&format=json&formatversion=2&prop=wikitext&page=${page_name}
		// where page_name is the one for the card that should be used for the specific test
		// the filename should indicate the reason the card is considered for a test (with extension of .json)
		// this method scans that folder and adds directly to the tests (failing the first time because it needs to be approved)
		registerAllParseWikitextPageData("cardtable2", cardTable2Wikitext);
		registerAllParseWikitextPageData("infobox_set", infoBoxSetWikitext);
	}

	/**
	 * Get all the CardTable2 parse wikitext page test data.
	 * <br>
	 * This contains similar information to a single query page on yugipedia,
	 * including a test name indicating why this test is useful.
	 *
	 * @return list of current test data.
	 */
	public synchronized List<WikitextPageTestCase> getCardTable2WikitextTestCase() {
		if (cardTable2Cache == null) {
			cardTable2Cache =
				getRegisteredParseWikitextPageTestData(cardTable2Wikitext);
		}
		return cardTable2Cache;
	}

	public synchronized List<WikitextPageTestCase> getInfoboxSetWikitextTestCase() {
		if (infoBoxSetCache == null) {
			infoBoxSetCache =
				getRegisteredParseWikitextPageTestData(infoBoxSetWikitext);
		}
		return infoBoxSetCache;
	}

	private static List<WikitextPageTestCase> getRegisteredParseWikitextPageTestData(
		final Map<String, Path> registry
	) {
		return registry
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
					return new WikitextPageTestCase(
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

	/**
	 * Gets the test-data registry instance.
	 *
	 * @return gets the instance for the class.
	 */
	public static synchronized YugipediaTestDataRegistry getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new YugipediaTestDataRegistry();
		}
		return INSTANCE;
	}

	private static void registerAllParseWikitextPageData(
		final String pathName,
		final Map<String, Path> registry
	) {
		final Path parseWikitextPageFolder = PARSE_WIKITEXT_PAGE_PATH.resolve(
			pathName
		);
		try {
			Files
				.list(parseWikitextPageFolder)
				.forEach(path -> registerParseWikitextPageData(path, registry));
		} catch (final IOException e) {
			throw new IllegalStateException("Cannot register test data", e);
		}
	}

	private static void registerParseWikitextPageData(
		final Path parseWikitextPage,
		final Map<String, Path> registry
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
		registry.put(testName, parseWikitextPage);
	}
}
