package io.github.ygojson.model.utils;

import java.lang.annotation.*;

/**
 * Metadata for the json-schema generation.
 */
@Target({ElementType.ANNOTATION_TYPE, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface JsonSchema {

    String title() default "";

    String description() default "";

}
