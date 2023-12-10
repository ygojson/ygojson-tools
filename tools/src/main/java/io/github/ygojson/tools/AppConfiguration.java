package io.github.ygojson.tools;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.github.ygojson.model.utils.JsonUtils;
import io.github.ygojson.tools.common.ApplicationInfo;

/**
 * Application configuration.
 */
@Configuration
public class AppConfiguration {

	/**
	 * Object mapper to be used on the tool.
	 * <br>
	 * Should be ygojson-data ready.
	 *
	 * @return object-mapper.
	 */
	@Bean
	public ObjectMapper objectMapper() {
		// use our model object-mapper
		return JsonUtils.getObjectMapper();
	}

	// TODO: fix properties management (see https://github.com/ygojson/ygojson-tools/issues/19)
	@Bean
	public ApplicationInfo applicationInfo(
		@Value("${application.title:ygojson-tools}") final String title,
		@Value("${application.version:develop}") final String version,
		@Value("${application.url:https://ygojson.github.io}") final String url
	) {
		return new ApplicationInfo(title, version, url);
	}
}
