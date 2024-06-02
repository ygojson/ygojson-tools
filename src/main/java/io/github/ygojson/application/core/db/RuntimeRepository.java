package io.github.ygojson.application.core.db;

import java.util.UUID;

import com.fasterxml.uuid.Generators;
import com.fasterxml.uuid.impl.TimeBasedEpochGenerator;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.transaction.Transactional;

/**
 * Base repository for YGOJSON runtime DB.
 * <br>
 * Provides the logic to create
 * @param <E>
 */
public abstract class RuntimeRepository<E extends RuntimeBaseEntity>
	implements PanacheRepositoryBase<E, UUID> {

	// generator for UUIDs
	private final TimeBasedEpochGenerator idGenerator =
		Generators.timeBasedEpochGenerator();

	/**
	 * Transactional method to save the entity.
	 * <br>
	 * The entities could have already an ID or a new one will be generated.
	 * Generated UUIDs are v7, to be more DB-friendly.
	 *
	 * @param entity entity to save (with or without already set ID).
	 *
	 * @return the saved entity ID
	 */
	@Transactional
	public UUID save(final E entity) {
		if (entity.id == null) {
			entity.id = idGenerator.generate();
		}
		persist(entity);
		return entity.id;
	}
}
