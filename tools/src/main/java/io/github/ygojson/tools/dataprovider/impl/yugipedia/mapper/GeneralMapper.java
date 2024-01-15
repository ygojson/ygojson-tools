package io.github.ygojson.tools.dataprovider.impl.yugipedia.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Named;

/**
 * Mapper with general conversion utils.
 */
@Mapper
public abstract class GeneralMapper {

	/**
	 * Converts into a lower-case.
	 *
	 * @param value the field to lower-case
	 * @return lower-case string; {@code null} if value is {@code null}.
	 */
	@Named("toLowerCase")
	public String mapToLowerCase(final String value) {
		if (value == null) {
			return null;
		}
		return value.toLowerCase();
	}

	/**
	 * Maps a list to its size.
	 *
	 * @param list the input list
	 *
	 * @return size of the list; {@code null} if the input list is {@code null}.
	 */
	protected Integer mapToSize(final List<?> list) {
		if (list == null) {
			return null;
		}
		return list.size();
	}

	/**
	 * Maps a string to an integer or to a {@code null}.
	 *
	 * @param value the value to map
	 *
	 * @return {@code null} if the value is {@code null} or blank;
	 * 			{@link Integer#valueOf(String)} otherwise.
	 */
	@Named("nullableInteger")
	protected Integer mapToNullableInteger(final String value) {
		if (value == null || value.isBlank()) {
			return null;
		}
		return Integer.valueOf(value);
	}

	/**
	 * Maps a blank string to {@code null}.
	 *
	 * @param value the input string
	 *
	 * @return {@code null} if the input string is blank; the input string otherwise.
	 */
	@Named("blankStringToNull")
	public String mapBlankStringToNull(final String value) {
		if (value == null || value.isBlank()) {
			return null;
		}
		return value;
	}
}
