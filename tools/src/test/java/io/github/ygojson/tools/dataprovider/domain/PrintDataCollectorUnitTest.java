package io.github.ygojson.tools.dataprovider.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.instancio.Select.field;

import java.util.List;

import org.assertj.core.api.SoftAssertions;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import io.github.ygojson.model.data.Card;
import io.github.ygojson.model.data.Print;
import io.github.ygojson.model.data.Set;
import io.github.ygojson.tools.dataprovider.PrintData;

@Disabled("TODO")
class PrintDataCollectorUnitTest {

	PrintDataCollector collector;

	@BeforeEach
	void beforeEach() {
		collector = new PrintDataCollector();
	}

	private PrintData randomPrintData() {
		return new PrintData(
			Instancio.of(Print.class).ignore(field("id")).create(),
			Instancio.of(Card.class).ignore(field("id")).create(),
			Instancio.of(Set.class).ignore(field("id")).create()
		);
	}

	private PrintData withFixedSet(final Set set) {
		return new PrintData(
			Instancio.of(Print.class).ignore(field("id")).create(),
			Instancio.of(Card.class).ignore(field("id")).create(),
			set
		);
	}



	@Test
	void given_emptyCollector_when_addPrintData_then_dataIsPresent() {
		// given
		final PrintData toAdd = randomPrintData();
		// when
		collector.addPrintData(toAdd);
		final List<PrintData> collectedData = collector.getPrintData().toList();
		// then
		assertThat(collectedData)
			.singleElement()
			.isEqualTo(toAdd);
	}

	@Test
	void given_emptyCollector_when_addPrintDataWithSameSet_then_onlyOneSet() {
		// given
		final PrintData toAdd1 = randomPrintData();
		final Set set = toAdd1.set();
		final PrintData toAdd2 = withFixedSet(set);
		// when
		collector.addPrintData(toAdd1);
		collector.addPrintData(toAdd2);
		final List<PrintData> collectedData = collector.getPrintData().toList();
		final List<Set> collectedSets = collector.getSets().toList();
		// then
		SoftAssertions.assertSoftly(softly -> {
			softly.assertThat(collectedData)
				.contains(toAdd1, toAdd2);
			softly.assertThat(collectedSets)
				.singleElement()
				.isEqualTo(set);
		});
	}

	@Test
	void given_filledCollector_when_updateSetWithSameName_then_setIsUpdated() {
		// given
		final PrintData toAdd = randomPrintData();
		final Set setToUpdate = Instancio.create(Set.class);
		setToUpdate.setName(toAdd.set().getName());
		collector.addPrintData(toAdd);
		// when
		final boolean updated = collector.updateSet(setToUpdate);
		// then
		SoftAssertions.assertSoftly(softly -> {
			softly.assertThat(updated)
				.isTrue();
			softly.assertThat(collector.getSets())
				.singleElement()
				.isEqualTo(setToUpdate);
			softly.assertThat(collector.getPrintData())
				.singleElement()
				.extracting(PrintData::set)
				.isEqualTo(setToUpdate);
		});
	}

	@Test
	void given_filledCollector_when_updatingSetWhileStream_then_setIsUpdated() {
		// given
		final PrintData toAdd = randomPrintData();
		collector.addPrintData(toAdd);
		// when
		final Set setToUpdate = Instancio.create(Set.class);
		collector.getSets()
			.map(oldSet -> {
				setToUpdate.setName(toAdd.set().getName());
				return setToUpdate;
			})
			.forEach(newSet -> collector.updateSet(newSet));
		// then
		SoftAssertions.assertSoftly(softly -> {
			softly.assertThat(collector.getSets())
				.singleElement()
				.isEqualTo(setToUpdate);
			softly.assertThat(collector.getPrintData())
				.singleElement()
				.extracting(PrintData::set)
				.isEqualTo(setToUpdate);
		});
	}
}
