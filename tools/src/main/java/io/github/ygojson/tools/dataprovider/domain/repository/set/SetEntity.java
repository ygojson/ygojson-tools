package io.github.ygojson.tools.dataprovider.domain.repository.set;

import io.github.ygojson.model.data.Set;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.UUID;

/**
 * Entity for the YGOJSON tooling for data provider.
 *
 * @param _uuid random-generated ID for the entity - used to store the entity but not as {@link Set#getId()}
 * @param set the actual set model that is stored
 * @param lastModified the last time the set was modified
 */
public record SetEntity(
	UUID _uuid,
	Set set,
	ZonedDateTime lastModified
	) {

	/**
	 * Creates a new set entity.
	 * <br>
	 * The ID is randomly generated and the last-modified time is set to the current time.
	 *
	 * @param set set model to store.
	 */
	public SetEntity(Set set) {
		this(UUID.randomUUID(), set, ZonedDateTime.now(ZoneOffset.UTC));
	}


}
