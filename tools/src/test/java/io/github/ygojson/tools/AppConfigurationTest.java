package io.github.ygojson.tools;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import io.github.ygojson.tools.common.ApplicationInfo;

@SpringBootTest
class AppConfigurationTest {

	@Autowired
	private ApplicationInfo appInfo;

	@Test
	void testApplicationInfo() {
		assertThat(appInfo)
			.isEqualTo(
				new ApplicationInfo(
					"ygojson-tools",
					"develop",
					"https://ygojson.github.io"
				)
			);
	}
}
