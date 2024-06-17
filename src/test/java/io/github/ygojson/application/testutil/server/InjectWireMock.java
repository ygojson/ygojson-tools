package io.github.ygojson.application.testutil.server;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Marker to inject a {@link com.github.tomakehurst.wiremock.WireMockServer}.
 * <br>
 * Should be used with {@link io.quarkus.test.junit.QuarkusTest} and {@link io.quarkus.test.common.QuarkusTestResource}
 * using {@link WiremockResource}.
 */
@Retention(RUNTIME)
@Target({ FIELD })
public @interface InjectWireMock {
}
