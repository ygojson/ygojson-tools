package io.github.ygojson.cli.hidden;

import static org.assertj.core.api.Assertions.assertThat;

import io.quarkus.test.junit.main.Launch;
import io.quarkus.test.junit.main.LaunchResult;
import io.quarkus.test.junit.main.QuarkusMainTest;
import org.junit.jupiter.api.Test;

@QuarkusMainTest
class YugipediaCommandTest {

	@Test
	@Launch(value = "help")
	void given_noArgs_when_launch_commandNotShown(final LaunchResult result) {
		// help is in on the sdterr
		assertThat(result.getOutput()).doesNotContain("yugipedia");
	}

	@Test
	@Launch(value = { "yugipedia", "--help" })
	void given_yugipediaCommand_when_launch_commandDoesNotFail(
		final LaunchResult result
	) {
		assertThat(result.getOutput()).contains("Usage");
	}
}
