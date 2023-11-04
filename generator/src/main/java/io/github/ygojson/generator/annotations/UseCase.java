package io.github.ygojson.generator.annotations;

import java.lang.annotation.*;

/**
 * Annotation marker for a use-case.
 * </br>
 * Useful for dependency injection frameworks to detect components.
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface UseCase {
}
