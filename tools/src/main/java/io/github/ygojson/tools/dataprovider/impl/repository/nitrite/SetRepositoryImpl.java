package io.github.ygojson.tools.dataprovider.impl.repository.nitrite;

import static io.github.ygojson.tools.dataprovider.impl.repository.nitrite.SetEntityDecorator.SetWrapper;
import static io.github.ygojson.tools.dataprovider.impl.repository.nitrite.SetEntityDecorator.getProperty;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

import org.dizitart.no2.Nitrite;

import io.github.ygojson.model.data.Set;
import io.github.ygojson.tools.dataprovider.domain.repository.SetRepository;

public class SetRepositoryImpl implements SetRepository {

	private final NitriteRepository<SetWrapper> nitriteRepository;

	public SetRepositoryImpl(final Nitrite nitrite) {
		this.nitriteRepository = new NitriteRepository<>(nitrite, new SetEntityDecorator());
	}

	@Override
	public long count() {
		return nitriteRepository.size();
	}

	@Override
	public void save(Set set) {
		nitriteRepository.insert(new SetWrapper(set));
	}

	@Override
	public Stream<Set> findAll() {
		return nitriteRepository.findAll()
			.map(SetWrapper::set);
	}

	@Override
	public Optional<Set> findById(UUID id) {
		return nitriteRepository.findFirstBy(getProperty("id"), id)
			.map(SetWrapper::set);
	}

	@Override
	public Optional<Set> findByName(String name) {
		return nitriteRepository
			.findFirstBy(getProperty("name"), name)
			.or(() -> nitriteRepository.findFirstBy(getProperty("nameAlt"), name))
			.map(SetWrapper::set);
	}

	@Override
	public Optional<Set> findBySetCode(String code) {
		return nitriteRepository
			.findFirstBy(getProperty("setCode"), code)
			.or(() -> nitriteRepository.findFirstBy(getProperty("setCodeAlt"), code))
			.map(SetWrapper::set);
	}

	@Override
	public void close() throws Exception {
		nitriteRepository.close();
	}

}
