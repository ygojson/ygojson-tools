package io.github.ygojson.runtime;

import static org.assertj.core.api.SoftAssertions.assertSoftly;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import io.github.ygojson.application.ApplicationInfo;

@QuarkusTest
class ApplicationConfigTest {

	@Inject
	ApplicationInfo info;

	@Test
	void testApplicationInfo() {
		assertSoftly(softly -> {
			softly.assertThat(info.name()).describedAs("name").isNotBlank();
			softly.assertThat(info.version()).describedAs("version").isNotBlank();
			softly.assertThat(info.url()).describedAs("url").isNotBlank();
		});
	}
}
