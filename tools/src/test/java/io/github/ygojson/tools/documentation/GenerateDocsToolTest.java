package io.github.ygojson.tools.documentation;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.jimfs.Configuration;
import com.google.common.jimfs.Jimfs;
import io.github.ygojson.tools.common.YgoJsonToolException;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.SoftAssertions;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.util.FileSystemUtils;

import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Slf4j
class GenerateDocsToolTest {

    private static final Set<String> EXPECTED_GENERATED_SCHEMAS = Set.of(
            "card.schema.json",
            "set.schema.json",
            "print.schema.json",
            "versioninfo.schema.json"
    );

    private static FileSystem FS;
    private static ObjectMapper OBJECT_MAPPER;

    private GenerateDocsTool testTool;
    private Path testPath;

    @BeforeAll
    static void beforeAll() {
        // use an in-memory FS to avoid writing to disk and speed up tests
        FS = Jimfs.newFileSystem(Configuration.unix());
        OBJECT_MAPPER = new ObjectMapper();
    }
    @AfterAll
    static void afterAll() {
        try {
            FS.close();
        } catch (final IOException e) {
            log.error("Cannot close in-memory filesystem", e);
            FS = null;
        }
        OBJECT_MAPPER = null;
    }

    @BeforeEach
    void beforeEach() throws IOException {
        testTool = new GenerateDocsTool(OBJECT_MAPPER);
        testPath = FS.getPath("/schemas/" + UUID.randomUUID() + "/");
    }

    @AfterEach
    void afterEach() {
        try {
            FileSystemUtils.deleteRecursively(testPath);
            testPath = null;
        } catch (final IOException e) {
            log.error("Cannot delete in-memory path", e);
        }
    }

    static Stream<GenerateDocsTool.Input> invalidInputs() throws IOException {
        return Stream.of(
                // null directory
                new GenerateDocsTool.Input(null, "", false),
                // null prefix
                new GenerateDocsTool.Input(FS.getPath("/" + UUID.randomUUID()), null, false)
        );
    }

    @ParameterizedTest
    @MethodSource("invalidInputs")
    void given_invalidInput_when_executed_then_inputException(final GenerateDocsTool.Input input) {
        // given
        // invalid input
        // when
        final ThrowableAssert.ThrowingCallable throwingCallable = () -> testTool.execute(input);
        // then
        assertThatThrownBy(throwingCallable)
                .isInstanceOf(YgoJsonToolException.InputException.class);
    }

    private static Map<String, JsonNode> getFileSchemasInPath(final Path path) throws IOException {
        return Files.list(path)
                .collect(Collectors.toMap(
                        file -> file.getFileName().toString(),
                        file -> {
                            try {
                                return OBJECT_MAPPER.readTree(Files.readString(file));
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                ));
    }

    @Test
    void given_validInput_when_executed_then_filesAreGenerated() throws Exception {
        // given
        final String prefix = "https://ygojson.github.io/";
        final GenerateDocsTool.Input input = new GenerateDocsTool.Input(testPath, prefix, false);
        // when
        testTool.execute(input);
        // then
        assertSchemaOutput(prefix, testPath);
    }

    private static void assertSchemaOutput(final String expectedPrefix, Path outputPath) throws IOException {
        final Map<String, JsonNode> fileToSchema = getFileSchemasInPath(outputPath);
        // not checking the actual data from the schema
        // that should be an acceptance/integration test instead
        // and the processor unit-test should ensure the content follow our rules
        SoftAssertions.assertSoftly(softly -> {
            // all files should be present
            softly.assertThat(fileToSchema.keySet())
                    .describedAs("generated schemas")
                    .isEqualTo(EXPECTED_GENERATED_SCHEMAS);
            fileToSchema.values().forEach(schema -> {
                softly.assertThat(schema.get("$id").textValue())
                        .describedAs("id-prefix")
                        .startsWith(expectedPrefix);
            });
        });
    }

}