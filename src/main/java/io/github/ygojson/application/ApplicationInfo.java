package io.github.ygojson.application;

/**
 * Represents the application information.
 * <br>
 * This information would be populated with quarkus to get the information from the application on runtime.
 *
 * @param name name of the application
 * @param version version of the application
 * @param url url from the application
 */
public record ApplicationInfo(String name, String version, String url) {}
