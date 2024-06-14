package io.github.ygojson.cli.hidden.yugipedia.cmd;

import static org.assertj.core.api.Assertions.assertThat;

import java.nio.file.Path;
import java.nio.file.Paths;

import io.quarkus.test.junit.TestProfile;
import io.quarkus.test.junit.main.Launch;
import io.quarkus.test.junit.main.LaunchResult;
import io.quarkus.test.junit.main.QuarkusMainTest;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.ygojson.cli.YgoJsonCliTestProfile;

@QuarkusMainTest
@TestProfile(YgoJsonCliTestProfile.class)
class YugipediaProviderCommandTest {

	private static final Logger LOG = LoggerFactory.getLogger(
		YugipediaProviderCommandTest.class
	);

	@Test
	@Launch(value = "help")
	void given_noArgs_when_launch_commandNotShown(final LaunchResult result) {
		// help is in on the sdterr
		assertThat(result.getOutput()).doesNotContain("yugipedia-provider");
	}

	@Test
	@Launch(value = { "yugipedia-provider", "--help" })
	void given_yugipediaCommand_when_launch_commandDoesNotFail(
		final LaunchResult result
	) {
		assertThat(result.getOutput()).contains("Usage");
	}

	@Test
	@Launch(value = { "yugipedia-provider", "--output", ".tmp/test.json", "--pretty", "--max", "10", "--model", "sets" })
	void given_yugipediaRealCommand_when_launch_outputExists(
		final LaunchResult result
	) {
		final Path expectedOutput = Paths.get(".tmp/test.json");
		assertThat(expectedOutput).exists();
	}

}
