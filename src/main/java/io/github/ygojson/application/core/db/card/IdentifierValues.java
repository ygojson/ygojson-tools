package io.github.ygojson.application.core.db.card;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

/**
 * Embeddable identifierss for the {@link CardEntity}.
 */
@Embeddable
public class IdentifierValues {

	@Column(name = "konami_id")
	public Long konamiId;

	public Long password;

	@Column(name = "password_alt")
	public Long passwordAlt;

	@Column(name = "yugipedia_pageid")
	public Long yugipediaPageId;
}
