package io.github.ygojson.tools.dataprovider.domain.uuid;

import io.github.ygojson.model.data.Card;
import io.github.ygojson.model.data.Print;
import io.github.ygojson.model.data.Set;
import org.assertj.core.api.ThrowableAssert;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static io.github.ygojson.tools.dataprovider.domain.uuid.YgojsonIDGenerator.YgojsonID;
import static io.github.ygojson.tools.dataprovider.test.YgojsonIDTestData.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class YgojsonIDGeneratorTest {

	private final YgojsonIDGenerator idGenerator = new YgojsonIDGenerator();

	private Card randomCardWithoutId() {
		Card card = Instancio.create(Card.class);
		card.setId(null);
		return card;
	}

	private Set randomSetWithoutId() {
		Set set = Instancio.create(Set.class);
		set.setId(null);
		return set;
	}

	private Print randomPrintWithoutId() {
		Print print = Instancio.create(Print.class);
		print.setId(null);
		return print;
	}

	@Test
	void given_nullCard_when_generateID_then_throwsIllegalArgumentException() {
		// given
		Card card = null;
		// when
		final ThrowableAssert.ThrowingCallable callable = () -> idGenerator.generate(card);
		// then
		assertThatThrownBy(callable)
			.isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	void given_cardWithId_when_generateID_then_throwsIllegalArgumentException() {
		// given
		Card card = new Card();
		card.setId(UUID.randomUUID());
		// when
		final ThrowableAssert.ThrowingCallable callable = () -> idGenerator.generate(card);
		// then
		assertThatThrownBy(callable)
			.isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	void given_nullSet_when_generateID_then_throwsIllegalArgumentException() {
		// given
		Set set = null;
		// when
		final ThrowableAssert.ThrowingCallable callable = () -> idGenerator.generate(set);
		// then
		assertThatThrownBy(callable)
			.isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	void given_setWithId_when_generateID_then_throwsIllegalArgumentException() {
		// given
		Set set = new Set();
		set.setId(UUID.randomUUID());
		// when
		final ThrowableAssert.ThrowingCallable callable = () -> idGenerator.generate(set);
		// then
		assertThatThrownBy(callable)
			.isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	void given_nullPrint_when_generateID_then_throwsIllegalArgumentException() {
		// given
		Print print = null;
		// when
		final ThrowableAssert.ThrowingCallable callable = () -> idGenerator.generate(print);
		// then
		assertThatThrownBy(callable)
			.isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	void given_emptyCard_when_generateID_then_returnRandomUUID() {
		// given
		Card card = new Card();
		// when
		final YgojsonID id = idGenerator.generate(card);
		// then
		assertThat(id.isRandom())
			.isTrue();
	}

	@Test
	void given_emptyCard_when_generateIdTwice_then_uuidIsDifferent() {
		// given
		Card card = new Card();
		// when
		final YgojsonID id = idGenerator.generate(card);
		final YgojsonID id2 = idGenerator.generate(card);
		// then
		assertThat(id)
			.isNotEqualTo(id2);
	}

	@Test
	void given_completeCard_when_generateId_then_returnNonRandomUUID() {
		// given
		Card card = randomCardWithoutId();
		// when
		final YgojsonID id = idGenerator.generate(card);
		// then
		assertThat(id.isRandom())
			.isFalse();
	}

	@Test
	void given_completeCard_when_generateIdTwice_then_uuidIsEqual() {
		// given
		Card card = randomCardWithoutId();
		// when
		final YgojsonID id = idGenerator.generate(card);
		final YgojsonID id2 = idGenerator.generate(card);
		// then
		assertThat(id)
			.isEqualTo(id2);
	}

	@Test
	void given_cardWithMinimalIdInfo_when_generateId_then_returnExpectedUUID() {
		// given
		Card card = new Card();
		updateCardWithTestData(card);
		// when
		final YgojsonID id = idGenerator.generate(card);
		// then
		assertThat(id)
			.isEqualTo(TEST_CARD_ID);
	}

	@Test
	void given_cardWithAllInfo_when_generateId_then_returnExpectedUUID() {
		// given
		Card card = randomCardWithoutId();
		updateCardWithTestData(card);
		// when
		final YgojsonID id = idGenerator.generate(card);
		// then
		assertThat(id)
			.isEqualTo(TEST_CARD_ID);
	}

	@Test
	void given_setWithMinimalIdInfo_when_generateId_then_returnExpectedUUID() {
		// given
		Set set = new Set();
		updateSetWithTestData(set);
		// when
		final YgojsonID id = idGenerator.generate(set);
		// then
		assertThat(id)
			.isEqualTo(TEST_SET_ID);
	}

	@Test
	void given_setWithAllInfo_when_generateId_then_returnExpectedUUID() {
		// given
		Set set = randomSetWithoutId();
		updateSetWithTestData(set);
		// when
		final YgojsonID id = idGenerator.generate(set);
		// then
		assertThat(id)
			.isEqualTo(TEST_SET_ID);
	}

	@Test
	void given_printWithMinimalIdInfo_when_generateId_then_returnExpectedUUID() {
		// given
		Print print = new Print();
		updatePrintWithTestData(print);
		// when
		final YgojsonID id = idGenerator.generate(print);
		// then
		assertThat(id)
			.isEqualTo(TEST_PRINT_ID);
	}

	@Test
	void given_printWithAllInfo_when_generateId_then_returnExpectedUUID() {
		// given
		Print print = randomPrintWithoutId();
		updatePrintWithTestData(print);
		// when
		final YgojsonID id = idGenerator.generate(print);
		// then
		assertThat(id)
			.isEqualTo(TEST_PRINT_ID);
	}

}
