package io.github.ygojson.application.core.db.set;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

/**
 * Repository for {@link SetEntity}.
 */
@ApplicationScoped
public class SetRepository implements PanacheRepository<SetEntity> {

	/**
	 * Transactional method to save the entity.
	 *
	 * @param entity entity to save.
	 *
	 * @return the saved entity ID
	 */
	@Transactional
	public Long save(final SetEntity entity) {
		persist(entity);
		return entity.id;
	}
}