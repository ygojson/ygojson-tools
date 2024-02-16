package io.github.ygojson.tools.data.impl;

import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.dizitart.no2.Nitrite;
import org.dizitart.no2.common.WriteResult;
import org.dizitart.no2.repository.ObjectRepository;

/**
 *
 * @param <E> entity type
 */
abstract class AbstractNitriteRepository<E> implements AutoCloseable {

	private final Nitrite nitriteDb;

	AbstractNitriteRepository(final Nitrite nitriteDb) {
		this.nitriteDb = nitriteDb;
		createIndexes(getObjectRepository(this.nitriteDb));
	}

	protected abstract ObjectRepository<E> getObjectRepository(final Nitrite nitriteDb);

	protected abstract void createIndexes(final ObjectRepository<E> repository);

	/**
	 * Saves a given entity.
	 * </br>
	 * Performs the operation on a similar manner than
	 * {@code org.springframework.data.repository.CrudRepository#save(Object)}.
	 *
	 * @param entity non null entity
	 *
	 */
	public final void save(final E entity) {
		assertNotNull(entity, "entity must not be null");
		final WriteResult result = getObjectRepository(nitriteDb).insert(entity);
		if (result.getAffectedCount() != 1) {
			throw new IllegalArgumentException("wrongly saved entities: " + result);
		}
	}

	/**
	 * Returns a stream of all entities.
	 *
	 * @return a stream of entities
	 */
	public final Stream<E> findAll() {
		return StreamSupport.stream(getObjectRepository(nitriteDb).find().spliterator(), false);
	}

	// utility method to throw on null objects
	private static void assertNotNull(final Object object, final String msg) {
		if (object == null) {
			throw new IllegalArgumentException(msg);
		}
	}

	@Override
	public final void close() {
		nitriteDb.close();
	}
}
