package io.github.ygojson.cli;

import static org.assertj.core.api.Assertions.assertThat;

import io.quarkus.test.junit.main.Launch;
import io.quarkus.test.junit.main.LaunchResult;
import io.quarkus.test.junit.main.QuarkusMainTest;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import picocli.CommandLine;

@QuarkusMainTest
class YgoJsonTopCommandTest {

	@Test
	@Launch(value = {}, exitCode = CommandLine.ExitCode.USAGE)
	void given_noArgs_when_launch_doesNotFail(final LaunchResult result) {
		// help is in on the sdterr
		SoftAssertions.assertSoftly(softly -> {
			assertHelpMessage(softly, "stderr", result.getErrorOutput());
			assertNotHelpMessage(softly, "stdout", result.getOutput());
		});
	}

	@Test
	@Launch("--help")
	void given_helpArg_when_launch_doesNotFail(final LaunchResult result) {
		// help text is on stdout
		SoftAssertions.assertSoftly(softly -> {
			assertHelpMessage(softly, "stdout", result.getOutput());
			assertNotHelpMessage(softly, "stderr", result.getErrorOutput());
		});
	}

	@Test
	@Launch("help")
	void given_helpCommand_when_launch_doesNotFail(final LaunchResult result) {
		// help text is on stdout
		SoftAssertions.assertSoftly(softly -> {
			assertHelpMessage(softly, "stdout", result.getOutput());
			assertNotHelpMessage(softly, "stderr", result.getErrorOutput());
		});
	}

	private void assertHelpMessage(
		final SoftAssertions softly,
		final String outputDesc,
		final String helpMessage
	) {
		softly
			.assertThat(helpMessage)
			.describedAs(outputDesc)
			.contains("help")
			.contains("--help");
	}

	private void assertNotHelpMessage(
		final SoftAssertions softly,
		final String outputDesc,
		final String helpMessage
	) {
		softly
			.assertThat(helpMessage)
			.describedAs(outputDesc)
			.doesNotContain("help")
			.doesNotContain("--help");
	}

	@Test
	@Launch("--version")
	void given_versionCommand_when_launch_containsCorrectVersion(
		final LaunchResult result
	) {
		// help text is on stdout
		assertThat(result.getOutput())
			.describedAs("version pattern")
			.containsPattern("[^\\\\d]+ v\\.[0-9]+\\.[0-9]+\\.[0-9]+");
	}
}
