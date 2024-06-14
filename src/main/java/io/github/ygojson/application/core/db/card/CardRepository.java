package io.github.ygojson.application.core.db.card;

import jakarta.enterprise.context.ApplicationScoped;

import io.github.ygojson.application.core.db.RuntimeRepository;

/**
 * Repository for {@link CardEntity}.
 */
@ApplicationScoped
public class CardRepository extends RuntimeRepository<CardEntity> {}
