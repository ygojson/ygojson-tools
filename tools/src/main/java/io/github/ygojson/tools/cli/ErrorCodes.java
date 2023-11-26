package io.github.ygojson.tools.cli;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ErrorCodes {

	/**
	 * Success code.
	 */
	public static final int SUCCESS = 0;

	/**
	 * Unknown error.
	 */
	public static final int UNKNOWN = 100;
}
