package io.github.ygojson.tools.documentation;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.SoftAssertions;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.ygojson.tools.common.YgoJsonToolException;
import io.github.ygojson.tools.test.TestFilesystem;

class GenerateDocsToolTest {

	private static final Logger log = LoggerFactory.getLogger(
		GenerateDocsToolTest.class
	);
	private static final Set<String> EXPECTED_GENERATED_SCHEMAS = Set.of(
		"card.schema.json",
		"set.schema.json",
		"print.schema.json",
		"versioninfo.schema.json"
	);

	private static TestFilesystem TEST_FS;
	private static ObjectMapper OBJECT_MAPPER;

	private GenerateDocsTool testTool;

	@BeforeAll
	static void beforeAll() {
		// use an in-memory FS to avoid writing to disk and speed up tests
		TEST_FS = TestFilesystem.init();
		OBJECT_MAPPER = new ObjectMapper();
	}

	@AfterAll
	static void afterAll() {
		OBJECT_MAPPER = null;
		TEST_FS.close();
	}

	@BeforeEach
	void beforeEach() throws IOException {
		testTool = new GenerateDocsTool(OBJECT_MAPPER);
	}

	@AfterEach
	void afterEach() {
		TEST_FS.cleanup();
	}

	static Stream<GenerateDocsTool.Input> invalidInputs() throws IOException {
		return Stream.of(
			// null directory
			new GenerateDocsTool.Input(null, "", false),
			// null prefix
			new GenerateDocsTool.Input(TEST_FS.getTestRoot(), null, false)
		);
	}

	@ParameterizedTest
	@MethodSource("invalidInputs")
	void given_invalidInput_when_executed_then_inputException(
		final GenerateDocsTool.Input input
	) {
		// given
		// invalid input
		// when
		final ThrowableAssert.ThrowingCallable throwingCallable = () ->
			testTool.execute(input);
		// then
		assertThatThrownBy(throwingCallable)
			.isInstanceOf(YgoJsonToolException.InputException.class);
	}

	private static Map<String, JsonNode> getFileSchemasInPath(final Path path)
		throws IOException {
		return Files
			.list(path)
			.collect(
				Collectors.toMap(
					file -> file.getFileName().toString(),
					file -> {
						try {
							return OBJECT_MAPPER.readTree(Files.readString(file));
						} catch (IOException e) {
							throw new RuntimeException(e);
						}
					}
				)
			);
	}

	@Test
	void given_validInput_when_executed_then_filesAreGenerated()
		throws Exception {
		// given
		final String prefix = "https://ygojson.github.io/";
		final GenerateDocsTool.Input input = new GenerateDocsTool.Input(
			TEST_FS.getTestRoot(),
			prefix,
			false
		);
		// when
		testTool.execute(input);
		// then
		assertSchemaOutput(prefix, TEST_FS.getTestRoot());
	}

	private static void assertSchemaOutput(
		final String expectedPrefix,
		Path outputPath
	) throws IOException {
		final Map<String, JsonNode> fileToSchema = getFileSchemasInPath(outputPath);
		// not checking the actual data from the schema
		// that should be an acceptance/integration test instead
		// and the processor unit-test should ensure the content follow our rules
		SoftAssertions.assertSoftly(softly -> {
			// all files should be present
			softly
				.assertThat(fileToSchema.keySet())
				.describedAs("generated schemas")
				.isEqualTo(EXPECTED_GENERATED_SCHEMAS);
			fileToSchema
				.values()
				.forEach(schema -> {
					softly
						.assertThat(schema.get("$id").textValue())
						.describedAs("id-prefix")
						.startsWith(expectedPrefix);
				});
		});
	}
}
