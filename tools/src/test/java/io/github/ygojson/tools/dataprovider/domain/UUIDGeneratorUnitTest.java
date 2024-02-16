package io.github.ygojson.tools.dataprovider.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.ThrowableAssert.ThrowingCallable;

import java.util.UUID;

import org.junit.jupiter.api.Test;

import io.github.ygojson.model.data.Card;
import io.github.ygojson.model.data.Print;
import io.github.ygojson.model.data.Set;
import io.github.ygojson.tools.test.CardMother;
import io.github.ygojson.tools.test.PrintMother;
import io.github.ygojson.tools.test.SetMother;

class UUIDGeneratorUnitTest {

	private static final UUID EXPECTED_DARK_MAGICIAN_UUID = UUID.fromString("34add14a-cad3-5e69-bc47-342fead16634");
	private static final UUID EXPECTED_DARK_MAGICIAN_PRINT_UUID = UUID.fromString("5d21fa1b-8c55-505b-ba2b-376827ad0e37");
	private static final UUID EXPECTED_DUELIST_NEXUS_UUID = UUID.fromString("d5b28866-6d18-514a-a8a9-a11fa4c21379");

	private UUIDGenerator testGenerator = new UUIDGenerator();

	@Test
	void given_cardWithIdAlreadyPresent_when_generateUUID_then_throwException() {
		// given
		final Card card = new Card();
		card.setId(UUID.randomUUID());
		// when
		final ThrowingCallable generate = () -> testGenerator.generate(card);
		// then
		assertThatThrownBy(generate)
			.isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	void given_printWithIdAlreadyPresent_when_generateUUID_then_throwException() {
		// given
		final Print print = new Print();
		print.setId(UUID.randomUUID());
		// when
		final ThrowingCallable generate = () -> testGenerator.generate(print);
		// then
		assertThatThrownBy(generate)
			.isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	void given_setWithIdAlreadyPresent_when_generateUUID_then_throwException() {
		// given
		final Set set = new Set();
		set.setId(UUID.randomUUID());
		// when
		final ThrowingCallable generate = () -> testGenerator.generate(set);
		// then
		assertThatThrownBy(generate)
			.isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	void given_emptyCard_when_generateUUID_then_returnNull() {
		// given
		final Card card = new Card();
		// when
		final UUID uuid = testGenerator.generate(card);
		// then
		assertThat(uuid)
			.isNull();
	}

	@Test
	void given_emptyPrint_when_generateUUID_then_returnNull() {
		// given
		final Print print = new Print();
		// when
		final UUID uuid = testGenerator.generate(print);
		// then
		assertThat(uuid)
			.isNull();
	}

	@Test
	void given_emptySet_when_generateUUID_then_returnNull() {
		// given
		final Set set = new Set();
		// when
		final UUID uuid = testGenerator.generate(set);
		// then
		assertThat(uuid)
			.isNull();
	}

	@Test
	void given_cardWithAllRequiredFields_when_generateUUID_then_returnExpectedUUID() {
		// given
		final Card card = CardMother.darkMagian();
		// when
		final UUID uuid = testGenerator.generate(card);
		// then
		assertThat(uuid)
			.isEqualTo(EXPECTED_DARK_MAGICIAN_UUID);
	}

	@Test
	void given_cardWithoutRequiredFields_when_generateUUID_then_returnsDifferentUUID() {
		// given - name not present
		final Card card = CardMother.darkMagian();
		card.setName(null); // remove name
		// when
		final UUID uuid = testGenerator.generate(card);
		// then
		assertThat(uuid)
			.isNotEqualTo(EXPECTED_DARK_MAGICIAN_UUID);
	}

	@Test
	void given_printWithAllRequiredFields_when_generateUUID_then_returnExpectedUUID() {
		// given
		final Print print = PrintMother.darkMagicianSYE001();
		// when
		final UUID uuid = testGenerator.generate(print);
		// then
		assertThat(uuid)
			.isEqualTo(EXPECTED_DARK_MAGICIAN_PRINT_UUID);
	}

	@Test
	void given_printWithoutRequiredFields_when_generateUUID_then_returnsDifferentUUID() {
		// given - printCode not present
		final Print print = PrintMother.darkMagicianSYE001();
		print.setCardId(null);
		// when
		final UUID uuid = testGenerator.generate(print);
		// then
		assertThat(uuid)
			.isNotEqualTo(EXPECTED_DARK_MAGICIAN_UUID);
	}

	@Test
	void given_setWithAllRequiredFields_when_generateUUID_then_returnExpectedUUID() {
		// given
		final Set set = SetMother.duelistNexus();
		// when
		final UUID uuid = testGenerator.generate(set);
		// then
		assertThat(uuid)
			.isEqualTo(EXPECTED_DUELIST_NEXUS_UUID);
	}

	@Test
	void given_setWithoutRequiredFields_when_generateUUID_then_returnsDifferentUUID() {
		// given
		final Set set = SetMother.duelistNexus();
		set.setName(null);
		// when
		final UUID uuid = testGenerator.generate(set);
		// then
		assertThat(uuid)
			.isNotEqualTo(EXPECTED_DUELIST_NEXUS_UUID);
	}

	@Test
	void given_setWithoutNameOrSetCode_when_generateUUID_then_returnNull() {
		// given - participation card unknown set
		final Set set = new Set();
		set.setPrintNumberPrefix("SP");
		set.setType("participation card");
		set.setSeries("sneak peek participation cards");
		// when
		final UUID uuid = testGenerator.generate(set);
		// then
		assertThat(uuid)
			.isNull();
	}

}
