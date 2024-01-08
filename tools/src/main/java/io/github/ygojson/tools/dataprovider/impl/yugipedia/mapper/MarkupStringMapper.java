package io.github.ygojson.tools.dataprovider.impl.yugipedia.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Named;

import io.github.ygojson.tools.dataprovider.impl.yugipedia.model.MarkupString;

/**
 * Mapper to be used with other mappers for converting fields
 * to markup strings.
 */
@Mapper
public abstract class MarkupStringMapper {

	/**
	 * Maps a plain-string property into a markup string.
	 *
	 * @param property plain string property to map.
	 *
	 * @return markup string
	 */
	public MarkupString map(final String property) {
		return MarkupString.of(property);
	}

	/**
	 * Maps a plain-string property into a ruby-characters-aware markup string.
	 *
	 * @param property plain-string property to map.
	 *
	 * @return ruby-characters aware markup string.
	 */
	@Named("rubyCharacters")
	public MarkupString mapToMaybeRubyCharacters(final String property) {
		return MarkupString.ofMaybeRubyCharacters(property);
	}

	/**
	 * Maps the markup string to a string with every markup cleanup.
	 *
	 * @param property property as a markup string to map..
	 *
	 * @return property without markup.
	 */
	public String map(final MarkupString property) {
		if (property == null) {
			return null;
		}
		return property.withoutMarkup();
	}

	public List<MarkupString> mapToNewLineList(final String property) {
		return map(property).splitByNewLine(true).toList();
	}

	@Named("commaSeparatedStringList")
	public List<String> mapToCommaSeparatedStringList(final String property) {
		return map(property).splitByComma().map(MarkupString::toString).toList();
	}
}
