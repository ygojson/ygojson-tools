package io.github.ygojson.application.core.datastore.db;

import java.util.UUID;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import org.hibernate.annotations.JdbcType;
import org.hibernate.type.descriptor.jdbc.VarcharJdbcType;

/**
 * Base entity for Raw Data-Store Database.
 * <br>
 * This base entity contains the {@link #id} and {@link #ygojsonId} IDs pre-configured.
 */
@MappedSuperclass
public abstract class RawBaseEntity extends PanacheEntityBase {

	/**
	 * ID-column name (can be overriden by subclasses to include the entity name).
	 */
	public static final String ID_COLUMN = "id";

	@Id
	@Column(
		name = RawBaseEntity.ID_COLUMN,
		columnDefinition = "VARCHAR(36)",
		nullable = false,
		unique = true
	)
	@JdbcType(VarcharJdbcType.class)
	public UUID id;

	// YGOJSON-ID
	@Column(name = "ygojson_id", columnDefinition = "VARCHAR(36)", unique = true)
	@JdbcType(VarcharJdbcType.class)
	public UUID ygojsonId;

	/**
	 * Similar to {@link io.quarkus.hibernate.orm.panache.PanacheEntity} to
	 * provide development-friendly string.
	 *
	 * @return the entity-name with the associated ID.
	 */
	@Override
	public String toString() {
		return this.getClass().getSimpleName() + "<" + id + ">";
	}
}
