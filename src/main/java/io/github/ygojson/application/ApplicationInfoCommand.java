package io.github.ygojson.application;

import java.text.MessageFormat;

import io.quarkus.runtime.ApplicationConfig;
import jakarta.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import picocli.CommandLine.Command;

@Command(name = "ygojson-tools", mixinStandardHelpOptions = true)
public class ApplicationInfoCommand implements Runnable {

	private static final Logger LOG = LoggerFactory.getLogger(
		ApplicationInfoCommand.class
	);

	@Inject
	ApplicationConfig applicationConfig;

	@Override
	public void run() {
		final ApplicationInfo info = createApplicationInfo();
		System.out.println(
			MessageFormat.format("{0} v{1}", info.title(), info.version())
		);
	}

	private ApplicationInfo createApplicationInfo() {
		return new ApplicationInfo(
			applicationConfig.name.orElse("ygojson-tools"),
			applicationConfig.version.orElse("unknown"),
			"https://ygojson.github.io/"
		);
	}
}
