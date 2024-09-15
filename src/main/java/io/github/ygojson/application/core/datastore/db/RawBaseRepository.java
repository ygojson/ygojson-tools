package io.github.ygojson.application.core.datastore.db;

import java.util.UUID;

import com.fasterxml.uuid.Generators;
import com.fasterxml.uuid.impl.TimeBasedEpochGenerator;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.transaction.Transactional;

/**
 * Base repository for Raw Data-Store Database.
 * <br>
 * Provides the logic to create the entity ID on save (if not set already).
 * The generated IDs are UUID v7, to be more DB-friendly.
 *
 * @param <E> the base entity (with a UUID ID field)
 */
public abstract class RawBaseRepository<E extends RawBaseEntity>
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
