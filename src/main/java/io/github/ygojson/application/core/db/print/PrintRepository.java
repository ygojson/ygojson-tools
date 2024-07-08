package io.github.ygojson.application.core.db.print;

import jakarta.enterprise.context.ApplicationScoped;

import io.github.ygojson.application.core.db.RuntimeRepository;

/**
 * Repository for {@link PrintEntity}.
 */
@ApplicationScoped
public class PrintRepository extends RuntimeRepository<PrintEntity> {}
