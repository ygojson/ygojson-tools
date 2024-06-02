package io.github.ygojson.application.core.db;

import java.util.UUID;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import org.hibernate.annotations.JdbcType;
import org.hibernate.type.descriptor.jdbc.VarcharJdbcType;

/**
 * Base entity for YGOJSON runtime DB.
 * <br>
 * This base entity contains the {@link #id} and {@link #ygojsonId} IDs pre-configured.
 */
@MappedSuperclass
public abstract class RuntimeBaseEntity extends PanacheEntityBase {

	/**
	 * ID-column name to be used on indexes.
	 */
	public static final String ID_COLUMN = "id";

	/**
	 * YGOJSON_ID-column name to be used on indexes.
	 */
	public static final String YGOJSON_ID = "ygojson_id";

	@Id
	@Column(
		name = RuntimeBaseEntity.ID_COLUMN,
		columnDefinition = "VARCHAR(36)",
		nullable = false,
		unique = true
	)
	@JdbcType(VarcharJdbcType.class)
	public UUID id;

	// YGOJSON-ID
	@Column(
		name = RuntimeBaseEntity.YGOJSON_ID,
		columnDefinition = "VARCHAR(36)",
		unique = true
	)
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
