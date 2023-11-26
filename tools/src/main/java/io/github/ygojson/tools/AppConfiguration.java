package io.github.ygojson.tools;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.github.ygojson.model.utils.JsonUtils;

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
}
