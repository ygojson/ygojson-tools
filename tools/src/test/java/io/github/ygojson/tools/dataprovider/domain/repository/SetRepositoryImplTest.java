package io.github.ygojson.tools.dataprovider.domain.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.instancio.Select.field;

import java.util.Optional;

import org.assertj.core.api.ThrowableAssert;
import org.dizitart.no2.Nitrite;
import org.dizitart.no2.common.mapper.JacksonMapperModule;
import org.instancio.Instancio;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.ygojson.model.data.Set;
import io.github.ygojson.tools.dataprovider.impl.repository.nitrite.SetRepositoryImpl;

class SetRepositoryImplTest {

	private static final Logger log = LoggerFactory.getLogger(SetRepositoryImplTest.class);

	private SetRepository setNitriteRepository;

	@BeforeEach
	void beforeEach() {
		final Nitrite nitrite = Nitrite.builder().loadModule(new JacksonMapperModule()).openOrCreate();
		setNitriteRepository = new SetRepositoryImpl(nitrite);
	}

	@AfterEach
	void afterEach() {
		try {
			setNitriteRepository.close();
		} catch (Exception e) {
			log.error("Failed to close test nitrite repository", e);
		}
	}

	@Test
	void given_emptySetRepository_when_saveSet_thenRepositoryCountIsOne() {
		// given
		final Set set = Instancio.create(Set.class);
		// when
		setNitriteRepository.save(set);
		// then
		assertThat(setNitriteRepository.count()).isEqualTo(1);
	}

	@Test
	void given_emptySetRepository_when_saveSetWithoutId_then_throwsRepositoryException() {
		// given
		final Set set = Instancio.of(Set.class)
			.set(field("id"), null)
			.create();
		// when
		final ThrowableAssert.ThrowingCallable callable = () -> setNitriteRepository.save(set);
		// then
		assertThatThrownBy(callable).isInstanceOf(RepositoryException.class);
	}


	@Test
	void given_setWasSaved_when_saveSameSet_then_throwsRepositoryException() {
		// given
		final Set set = Instancio.create(Set.class);
		setNitriteRepository.save(set);
		// when
		final ThrowableAssert.ThrowingCallable callable = () -> setNitriteRepository.save(set);
		// then
		assertThatThrownBy(callable).isInstanceOf(RepositoryException.class);
	}

	@Test
	void given_setWasSaved_when_findByNameWithName_then_returnCorrectSet() {
		// given
		final Set set = Instancio.create(Set.class);
		setNitriteRepository.save(set);
		// when
		final Optional<Set> foundSet = setNitriteRepository.findByName(set.getName());
		// then
		assertThat(foundSet)
			.get()
			.isEqualTo(set);
	}

	@Test
	void given_setWasSaved_when_findByNameWithAltName_then_returnCorrectSet() {
		// given
		final Set set = Instancio.create(Set.class);
		setNitriteRepository.save(set);
		// when
		final Optional<Set> foundSet = setNitriteRepository.findByName(set.getNameAlt());
		// then
		assertThat(foundSet)
			.get()
			.isEqualTo(set);
	}

	@Test
	void given_setWasSaved_when_findByNameWithNotPresent_then_returnEmpty() {
		// given
		final Set set = Instancio.create(Set.class);
		setNitriteRepository.save(set);
		// when
		final Optional<Set> foundSet = setNitriteRepository.findByName("test");
		// then
		assertThat(foundSet)
			.isEmpty();
	}

	@Test
	void given_setWasSaved_when_findBySetCodeWithSetCode_then_returnCorrectSet() {
		// given
		final Set set = Instancio.create(Set.class);
		setNitriteRepository.save(set);
		// when
		final Optional<Set> foundSet = setNitriteRepository.findBySetCode(set.getSetCode());
		// then
		assertThat(foundSet)
			.get()
			.isEqualTo(set);
	}

	@Test
	void given_setWasSaved_when_findBySetCodeWithAltSetCode_then_returnCorrectSet() {
		// given
		final Set set = Instancio.create(Set.class);
		setNitriteRepository.save(set);
		// when
		final Optional<Set> foundSet = setNitriteRepository.findBySetCode(set.getSetCodeAlt());
		// then
		assertThat(foundSet)
			.get()
			.isEqualTo(set);
	}

	@Test
	void given_setWasSaved_when_findBySetCodeWithNotPresentSetCode_then_returnEmpty() {
		// given
		final Set set = Instancio.create(Set.class);
		setNitriteRepository.save(set);
		// when
		final Optional<Set> foundSet = setNitriteRepository.findBySetCode("test");
		// then
		assertThat(foundSet)
			.isEmpty();
	}

}
