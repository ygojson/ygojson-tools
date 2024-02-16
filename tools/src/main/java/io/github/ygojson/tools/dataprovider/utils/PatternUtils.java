package io.github.ygojson.tools.dataprovider.utils;

import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class PatternUtils {

	private PatternUtils() {
		// NO-OP - utility class
	}

	public static String removeRecursively(
		final String text,
		Pattern pattern
	) {
		return replaceRecursively(text, pattern, m -> "");
	}

	public static String replaceRecursively(
		final String text,
		Pattern pattern,
		Function<Matcher, String> replaceFunction
	) {
		final Matcher matcher = pattern.matcher(text);
		final StringBuilder result = new StringBuilder();
		while (matcher.find()) {
			matcher.appendReplacement(result, replaceFunction.apply(matcher));
		}
		matcher.appendTail(result);
		return result.toString();
	}
}
