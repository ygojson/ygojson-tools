package io.github.ygojson.application.core.db.set;

import jakarta.enterprise.context.ApplicationScoped;

import io.github.ygojson.application.core.db.RuntimeRepository;

/**
 * Repository for {@link SetEntity}.
 */
@ApplicationScoped
public class SetRepository extends RuntimeRepository<SetEntity> {}
