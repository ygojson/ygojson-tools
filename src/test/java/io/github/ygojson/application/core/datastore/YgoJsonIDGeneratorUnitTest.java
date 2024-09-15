package io.github.ygojson.application.core.datastore;

import static io.github.ygojson.application.core.datastore.YgojsonIDTestData.TEST_CARD_ID;
import static io.github.ygojson.application.core.datastore.YgojsonIDTestData.updateCardWithTestData;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.UUID;

import org.assertj.core.api.ThrowableAssert;
import org.instancio.Instancio;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import io.github.ygojson.application.core.datastore.db.card.RawCard;

class YgoJsonIDGeneratorUnitTest {

	private final YgoJsonIDGenerator idGenerator = new YgoJsonIDGenerator();

	@Nested
	@DisplayName("Card ID Generation")
	class CardGenerationTest {

		private RawCard randomCardWithoutIds() {
			RawCard card = Instancio.create(RawCard.class);
			card.id = null;
			card.ygojsonId = null;
			return card;
		}

		@Test
		void given_nullCard_when_generateID_then_throwsIllegalArgumentException() {
			// given
			RawCard card = null;
			// when
			final ThrowableAssert.ThrowingCallable callable = () ->
				idGenerator.generate(card);
			// then
			assertThatThrownBy(callable).isInstanceOf(IllegalArgumentException.class);
		}

		@Test
		void given_cardWithId_when_generateID_then_throwsIllegalArgumentException() {
			// given
			RawCard card = new RawCard();
			card.ygojsonId = UUID.randomUUID();
			// when
			final ThrowableAssert.ThrowingCallable callable = () ->
				idGenerator.generate(card);
			// then
			assertThatThrownBy(callable).isInstanceOf(IllegalArgumentException.class);
		}

		@Test
		void given_emptyCard_when_generateID_then_returnNilUUID() {
			// given
			RawCard card = new RawCard();
			// when
			final UUID id = idGenerator.generate(card);
			// then
			assertThat(id.toString())
				.isEqualTo("00000000-0000-0000-0000-000000000000");
		}

		@Test
		void given_emptyCard_when_generateIdTwice_then_bothAreNil() {
			// given
			RawCard card = new RawCard();
			// when
			final UUID id = idGenerator.generate(card);
			final UUID id2 = idGenerator.generate(card);
			// then
			assertThat(id)
				.isEqualTo(id2)
				.extracting(UUID::toString)
				.isEqualTo("00000000-0000-0000-0000-000000000000");
		}

		@Test
		void given_completeCard_when_generateId_then_returnNonNilUUID() {
			// given
			RawCard card = randomCardWithoutIds();
			// when
			final UUID id = idGenerator.generate(card);
			// then
			assertThat(id.toString())
				.isNotEqualTo("00000000-0000-0000-0000-000000000000");
		}

		@Test
		void given_completeCard_when_generateIdTwice_then_uuidIsEqual() {
			// given
			RawCard card = randomCardWithoutIds();
			// when
			final UUID id = idGenerator.generate(card);
			final UUID id2 = idGenerator.generate(card);
			// then
			assertThat(id).isEqualTo(id2);
		}

		@Test
		void given_cardWithMinimalIdInfo_when_generateId_then_returnExpectedUUID() {
			// given
			RawCard card = new RawCard();
			updateCardWithTestData(card);
			// when
			final UUID id = idGenerator.generate(card);
			// then
			assertThat(id).isEqualTo(TEST_CARD_ID);
		}

		@Test
		void given_cardWithAllInfo_when_generateId_then_returnExpectedUUID() {
			// given
			RawCard card = randomCardWithoutIds();
			updateCardWithTestData(card);
			// when
			final UUID id = idGenerator.generate(card);
			// then
			assertThat(id).isEqualTo(TEST_CARD_ID);
		}
	}
}
