package io.github.ygojson.model.utils.data;

import static org.assertj.core.api.Assertions.assertThat;
import static org.instancio.Select.all;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;

import io.github.ygojson.model.data.Set;
import io.github.ygojson.model.data.definition.SetInfo;
import io.github.ygojson.model.utils.JsonUtils;

public class SetSerializationUnitTest {

	@Test
	void given_setWithNullSetText_when_serialize_then_textNotPresent()
		throws JsonProcessingException {
		// given
		final Set set = Instancio.of(Set.class).ignore(all(SetInfo.class)).create();
		// when
		final String value = JsonUtils.getObjectMapper().writeValueAsString(set);
		// then
		assertThat(value)
			.doesNotContain(
				"name",
				"nameAlt",
				"setCode",
				"regionCodes",
				"releaseDate"
			);
	}
}
