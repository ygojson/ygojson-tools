package io.github.ygojson.application.core.db.set;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.util.List;

import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.Test;

class SetEntityUnitTest {

	@Test
	void given_singleEnCode_when_setEnCodes_then_entityContainsMainSetCode() {
		// given
		final SetEntity entity = new SetEntity();
		// when
		entity.setEnSetCodes(List.of("DUNE"));
		// then
		assertSoftly(softly -> {
			softly.assertThat(entity.enSetCode).isEqualTo("DUNE");
			softly.assertThat(entity.enSetCodeAlt).isNull();
		});
	}

	@Test
	void given_twoEnCodes_when_setEnCodes_then_entityContainsMainAndAltSetCodes() {
		// given
		final SetEntity entity = new SetEntity();
		// when
		entity.setEnSetCodes(List.of("SLR", "MLR"));
		// then
		assertSoftly(softly -> {
			softly.assertThat(entity.enSetCode).isEqualTo("SLR");
			softly.assertThat(entity.enSetCodeAlt).isEqualTo("MLR");
		});
	}

	@Test
	void given_tooManySetCodes_when_setEnCodes_then_throwIllegalArgumentException() {
		// given
		final SetEntity entity = new SetEntity();
		// when
		final ThrowableAssert.ThrowingCallable call = () ->
			entity.setEnSetCodes(List.of("SLR", "MLR", "OTHER"));
		// then
		assertThatThrownBy(call).isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	void given_nullSetCodes_when_seEnCodes_then_throwIllegalArgumentException() {
		// given
		final SetEntity entity = new SetEntity();
		// when
		final ThrowableAssert.ThrowingCallable call = () ->
			entity.setEnSetCodes(null);
		// then
		assertThatThrownBy(call).isInstanceOf(IllegalArgumentException.class);
	}
}
