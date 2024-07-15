package io.github.ygojson.application.logic;

/**
 * Represents the card-text after splitting in different parts
 *
 * @param effect the effect text (if any).
 * @param materials the extra-deck materials line (if any).
 * @param flavor the flavor/non-effect text (if any).
 */
public record SplitCardText(String effect, String materials, String flavor) {}
