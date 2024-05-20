package io.github.ygojson.runtime;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;

import io.github.ygojson.application.ApplicationInfo;

@QuarkusTest
class ApplicationConfigTest {

	@Inject
	ApplicationInfo info;

	@Test
	void testApplicationInfo() {
		SoftAssertions.assertSoftly(softly -> {
			softly.assertThat(info.name()).describedAs("url").isNotBlank();
			softly.assertThat(info.version()).describedAs("version").isNotBlank();
			softly.assertThat(info.url()).describedAs("url").isNotBlank();
		});
	}
}
