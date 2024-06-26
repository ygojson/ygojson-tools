package io.github.ygojson.model.utils.data.utils.serialization;

import static org.assertj.core.api.Assertions.assertThat;
import static org.instancio.Select.all;
import static org.instancio.Select.field;

import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;

import io.github.ygojson.model.data.Set;
import io.github.ygojson.model.data.definition.SetInfo;
import io.github.ygojson.model.utils.serialization.JsonUtils;

public class SetSerializationUnitTest {

	private static final List<String> FIELDS_TO_EXCLUDE = List.of(
		"name",
		"nameAlt",
		"setCode",
		"setCodeAlt"
	);

	@Test
	void given_setWithNullSetText_when_serialize_then_textNotPresent()
		throws JsonProcessingException {
		// given
		final var instancioBuilder = Instancio.of(Set.class);
		FIELDS_TO_EXCLUDE.forEach(f -> instancioBuilder.ignore(field(f)));
		final Set set = instancioBuilder.ignore(all(SetInfo.class)).create();
		// when
		final String value = JsonUtils.getObjectMapper().writeValueAsString(set);
		// then
		assertThat(value).doesNotContain(FIELDS_TO_EXCLUDE);
	}

	@Test
	void given_setWithNonNullMainSetText_when_serialize_then_textPresent()
		throws JsonProcessingException {
		// given
		final Set set = Instancio.of(Set.class).ignore(all(SetInfo.class)).create();
		// when
		final String value = JsonUtils.getObjectMapper().writeValueAsString(set);
		// then
		assertThat(value).contains(FIELDS_TO_EXCLUDE);
	}
}
