package io.github.ygojson.runtime;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Singleton;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import io.github.ygojson.application.ApplicationInfo;

/**
 * Configuration for the shared beans of the ygojson-tools application.
 */
@ApplicationScoped
public class ApplicationConfig {

	@ConfigProperty(
		name = "quarkus.application.name",
		defaultValue = "ygojson-tools"
	)
	String applicationName;

	@ConfigProperty(name = "quarkus.application.version", defaultValue = "0.0.0")
	String applicationVersion;

	@ConfigProperty(name = "ygojson.url")
	String ygojsonUrl = null;

	/**
	 * Produces the application info configured with the properties.
	 *
	 * @return the application info instance.
	 */
	@Produces
	@Singleton
	public ApplicationInfo applicationInfo() {
		return new ApplicationInfo(applicationName, applicationVersion, ygojsonUrl);
	}
}
