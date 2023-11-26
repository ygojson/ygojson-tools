package io.github.ygojson.tools.cli;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import picocli.CommandLine;

@SpringBootTest
class ParentCliTest {

	@Autowired
	public ParentCli parentCli;

	@Autowired
	public CommandLine.IFactory factory;

	@Test
	void given_noArguments_when_toolsParentCLi_then_fail() {
		// given
		final String[] args = new String[0];
		// when
		int exitCode = new CommandLine(parentCli, factory).execute(args);
		// then
		assertThat(exitCode).isEqualTo(2);
	}

	@Test
	void given_helpCOmmand_when_toolsParentCLi_then_doNotFail() {
		// given
		final String[] args = new String[] { "help" };
		// when
		int exitCode = new CommandLine(parentCli, factory).execute(args);
		// then
		assertThat(exitCode).isEqualTo(0);
	}

	@ParameterizedTest
	@ValueSource(strings = { "help", "documentation" })
	void given_validSubcomman_when_executeWithHelp_then_doNotFail(
		final String subcommand
	) {
		// given
		final String[] args = new String[] { "help", subcommand };
		// when
		int exitCode = new CommandLine(parentCli, factory).execute(args);
		// then
		assertThat(exitCode).isEqualTo(0);
	}
}
