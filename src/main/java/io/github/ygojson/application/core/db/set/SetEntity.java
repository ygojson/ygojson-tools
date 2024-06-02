package io.github.ygojson.application.core.db.set;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import io.github.ygojson.application.core.db.RuntimeBaseEntity;

/**
 * Set-entity.
 * <br>
 * Note that the {@link #id} is internal for the database,
 * and it is not related with the YGOJSON model.
 */
@Entity
@Table(name = "tbl_set")
@AttributeOverride(
	name = RuntimeBaseEntity.ID_COLUMN,
	column = @Column(name = "set_id")
)
public class SetEntity extends RuntimeBaseEntity {

	// common independently of the localization
	public String printNumberPrefix;
	public String type;
	public String series;

	// name (localized)
	public String enName;
	public String deName;
	public String esName;
	public String frName;
	public String itName;
	public String jaName;
	public String koName;
	public String ptName;
	public String zhHansName;
	public String zhHantName;
	// alternative name (only in English - prefixed to be able to extend)
	public String enNameAlt;
	// set codes (localized)
	public String enSetCode;
	public String deSetCode;
	public String esSetCode;
	public String frSetCode;
	public String itSetCode;
	public String jaSetCode;
	public String koSetCode;
	public String ptSetCode;
	public String zhHansSetCode;
	public String zhHantSetCode;
	// alternative set codes (localized)
	public String enSetCodeAlt;
	public String deSetCodeAlt;
	public String esSetCodeAlt;
	public String frSetCodeAlt;
	public String itSetCodeAlt;
	public String jaSetCodeAlt;
	public String koSetCodeAlt;
	public String ptSetCodeAlt;
	public String zhHansSetCodeAlt;
	public String zhHantSetCodeAlt;
}
