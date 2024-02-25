package io.github.ygojson.tools.dataprovider.impl.repository.nitrite;

import java.util.Optional;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.dizitart.no2.Nitrite;
import org.dizitart.no2.collection.FindOptions;
import org.dizitart.no2.exceptions.NitriteException;
import org.dizitart.no2.filters.Filter;
import org.dizitart.no2.filters.FluentFilter;
import org.dizitart.no2.repository.Cursor;
import org.dizitart.no2.repository.EntityDecorator;
import org.dizitart.no2.repository.ObjectRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.ygojson.tools.dataprovider.domain.repository.RepositoryException;

public class NitriteRepository<T> implements AutoCloseable {

	private final Logger log = LoggerFactory.getLogger(NitriteRepository.class);

	private final Nitrite nitrite;
	private final EntityDecorator<T> entityDecorator;

	public NitriteRepository(final Nitrite nitrite, final EntityDecorator<T> entityDecorator) {
		this.nitrite = nitrite;
		this.entityDecorator = entityDecorator;
	}

	// utility method to throw on null objects
	private static void assertNotNull(final Object object, final String msg) {
		if (object == null) {
			throw new IllegalArgumentException(msg);
		}
	}

	private ObjectRepository<T> getRepository(final Nitrite nitrite) {
		return nitrite.getRepository(entityDecorator);
	}

	public long size() {
		try {
			return getRepository(nitrite).size();
		} catch (final NitriteException e) {
			log.error("Failed to count entities", e);
			throw new RepositoryException("Failed to count entities", e);
		}
	}

	public T insert(T entity) throws RepositoryException {
		assertNotNull(entity, "entity cannot be null");
		try {
			getRepository(nitrite).insert(entity);
			return entity;
		} catch (NitriteException e) {
			log.error("Failed to insert entity {}", entity, e);
			throw new RepositoryException("Failed to insert entity " + entity, e);
		}
	}

	public Stream<T> findAll() throws RepositoryException {
		return findAll(null);
	}

	private Stream<T> findAll(final FindOptions options) {
		try {
			return StreamSupport.stream(getRepository(nitrite).find(options).spliterator(), false);
		} catch (final NitriteException e) {
			log.error("Failed to find all entities", e);
			throw new RepositoryException("Failed to find all entities", e);
		}
	}

	private Cursor<T> internalFindBy(final Filter filter, final FindOptions options) {
		try {
			return getRepository(nitrite).find(filter, options);
		} catch (final NitriteException e) {
			log.error("Failed to find entity by {} with options {}", filter, options, e);
			throw new RepositoryException("Failed to find entity by " + filter, e);
		}
	}

	public Optional<T> findFirstBy(final String property, final Object value) throws RepositoryException {
		assertNotNull(property, "property cannot be null");
		assertNotNull(value, "value cannot be null");
		return findFirstBy(FluentFilter.where(property).eq(value));
	}

	public Optional<T> findFirstBy(final Filter filter) throws RepositoryException {
		assertNotNull(filter, "filter cannot be null");
		try {
			final T found = internalFindBy(filter, null).firstOrNull();
			return Optional.ofNullable(found);
		} catch (final NitriteException e) {
			log.error("Failed to find first entity by {}", filter, e);
			throw new RepositoryException("Failed to find first entity by " + filter, e);
		}
	}

	public Stream<T> findBy(final String property, final Object value) throws RepositoryException {
		assertNotNull(property, "property cannot be null");
		assertNotNull(value, "value cannot be null");
		return findBy(FluentFilter.where(property).eq(value));
	}

	public Stream<T> findBy(final Filter filter) throws RepositoryException {
		assertNotNull(filter, "filter cannot be null");
		try {
			return StreamSupport.stream(internalFindBy(filter, null).spliterator(), false);
		} catch (final NitriteException e) {
			log.error("Failed to find entity by {}", filter, e);
			throw new RepositoryException("Failed to find entity by " + filter, e);
		}
	}

	public void update(T entity) throws RepositoryException {
		assertNotNull(entity, "entity cannot be null");
		try {
			getRepository(nitrite).update(entity);
		} catch (final NitriteException e) {
			log.error("Failed to update entity {}", entity, e);
			throw new RepositoryException("Failed to update entity " + entity, e);
		}
	}

	public void delete(final T entity) throws RepositoryException {
		assertNotNull(entity, "entity cannot be null");
		try {
			getRepository(nitrite).remove(entity);
		} catch (final NitriteException e) {
			log.error("Failed to delete entity {}", entity, e);
			throw new RepositoryException("Failed to delete entity " + entity, e);
		}
	}

	public void deleteAll() throws RepositoryException {
		try {
			getRepository(nitrite).clear();
		} catch (final NitriteException e) {
			log.error("Failed to delete all entities", e);
			throw new RepositoryException("Failed to delete all entities", e);
		}
	}

	@Override
	public void close() throws Exception {
		nitrite.close();
	}
}
