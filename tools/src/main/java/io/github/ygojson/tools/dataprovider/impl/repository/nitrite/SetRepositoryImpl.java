package io.github.ygojson.tools.dataprovider.impl.repository.nitrite;

import io.github.ygojson.tools.dataprovider.domain.repository.set.SetEntity;
import io.github.ygojson.tools.dataprovider.domain.repository.set.SetRepository;
import org.dizitart.no2.Nitrite;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

public class SetRepositoryImpl implements SetRepository {

	private final NitriteRepository<SetEntity> nitriteRepository;

	public SetRepositoryImpl(final Nitrite nitrite) {
		this.nitriteRepository = new NitriteRepository<>(nitrite, new InternalModelEntityDecorator<>(
			SetEntity.class,
			"set",
			List.of(
				"name",
				"nameAlt",
				"setCode",
				"setCodeAlt"
			)));
	}

	@Override
	public long count() {
		return nitriteRepository.size();
	}

	@Override
	public void save(SetEntity entity) {
		nitriteRepository.insert(entity);
	}

	@Override
	public Stream<SetEntity> findAll() {
		return nitriteRepository.findAll();
	}

	@Override
	public Optional<SetEntity> findById(UUID id) {
		return nitriteRepository.findFirstModelBy("set.id", id);
	}

	@Override
	public Optional<SetEntity> findByName(String name) {
		return nitriteRepository
			.findFirstModelBy("set.name", name)
			.or(() -> nitriteRepository.findFirstModelBy("set.nameAlt", name));
	}

	@Override
	public Optional<SetEntity> findBySetCode(String code) {
		return nitriteRepository
			.findFirstModelBy("set.setCode", code)
			.or(() -> nitriteRepository.findFirstModelBy("set.setCodeAlt", code));
	}

	@Override
	public void close() throws Exception {
		nitriteRepository.close();
	}

}
