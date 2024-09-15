package io.github.ygojson.application.core.datastore.db.set;

import jakarta.persistence.*;

import io.github.ygojson.application.core.datastore.db.RawBaseEntity;

/**
 * Set-entity.
 * <br>
 * Note that the {@link #id} is internal for the database,
 * and it is not related with the YGOJSON model.
 */
@Entity
@Table(name = "tbl_set")
@AttributeOverride(
	name = RawBaseEntity.ID_COLUMN,
	column = @Column(name = "set_id")
)
public class RawSet extends RawBaseEntity {

	// common independently of the localization
	@Column(name = "print_number_prefix")
	public String printNumberPrefix;

	public String type;
	public String series;

	// MAIN NAME (ENGLISH) WILL NOT BE RENAMED IN DB
	public RawSetLocalizedData en = new RawSetLocalizedData();

	// alternative name (only in English - prefixed to keep consistency)
	@Column(name = "name_alt")
	public String enNameAlt;

	@Embedded
	@AttributeOverrides(
		{
			@AttributeOverride(name = "name", column = @Column(name = "de_name")),
			@AttributeOverride(
				name = "setCode",
				column = @Column(name = "de_setcode")
			),
			@AttributeOverride(
				name = "setCodeAlt",
				column = @Column(name = "de_setcode_alt")
			),
		}
	)
	public RawSetLocalizedData de = new RawSetLocalizedData();

	@Embedded
	@AttributeOverrides(
		{
			@AttributeOverride(name = "name", column = @Column(name = "es_name")),
			@AttributeOverride(
				name = "setCode",
				column = @Column(name = "es_setcode")
			),
			@AttributeOverride(
				name = "setCodeAlt",
				column = @Column(name = "es_setcode_alt")
			),
		}
	)
	public RawSetLocalizedData es = new RawSetLocalizedData();

	@Embedded
	@AttributeOverrides(
		{
			@AttributeOverride(name = "name", column = @Column(name = "fr_name")),
			@AttributeOverride(
				name = "setCode",
				column = @Column(name = "fr_setcode")
			),
			@AttributeOverride(
				name = "setCodeAlt",
				column = @Column(name = "fr_setcode_alt")
			),
		}
	)
	public RawSetLocalizedData fr = new RawSetLocalizedData();

	@Embedded
	@AttributeOverrides(
		{
			@AttributeOverride(name = "name", column = @Column(name = "it_name")),
			@AttributeOverride(
				name = "setCode",
				column = @Column(name = "it_setcode")
			),
			@AttributeOverride(
				name = "setCodeAlt",
				column = @Column(name = "it_setcode_alt")
			),
		}
	)
	public RawSetLocalizedData it = new RawSetLocalizedData();

	@Embedded
	@AttributeOverrides(
		{
			@AttributeOverride(name = "name", column = @Column(name = "ja_name")),
			@AttributeOverride(
				name = "setCode",
				column = @Column(name = "ja_setcode")
			),
			@AttributeOverride(
				name = "setCodeAlt",
				column = @Column(name = "ja_setcode_alt")
			),
		}
	)
	public RawSetLocalizedData ja = new RawSetLocalizedData();

	@Embedded
	@AttributeOverrides(
		{
			@AttributeOverride(name = "name", column = @Column(name = "ko_name")),
			@AttributeOverride(
				name = "setCode",
				column = @Column(name = "ko_setcode")
			),
			@AttributeOverride(
				name = "setCodeAlt",
				column = @Column(name = "ko_setcode_alt")
			),
		}
	)
	public RawSetLocalizedData ko = new RawSetLocalizedData();

	@Embedded
	@AttributeOverrides(
		{
			@AttributeOverride(name = "name", column = @Column(name = "pt_name")),
			@AttributeOverride(
				name = "setCode",
				column = @Column(name = "pt_setcode")
			),
			@AttributeOverride(
				name = "setCodeAlt",
				column = @Column(name = "pt_setcode_alt")
			),
		}
	)
	public RawSetLocalizedData pt = new RawSetLocalizedData();

	@Embedded
	@AttributeOverrides(
		{
			@AttributeOverride(name = "name", column = @Column(name = "zhhans_name")),
			@AttributeOverride(
				name = "setCode",
				column = @Column(name = "zhhans_setcode")
			),
			@AttributeOverride(
				name = "setCodeAlt",
				column = @Column(name = "zhhans_setcode_alt")
			),
		}
	)
	public RawSetLocalizedData zhHans = new RawSetLocalizedData();

	@Embedded
	@AttributeOverrides(
		{
			@AttributeOverride(name = "name", column = @Column(name = "zhhant_name")),
			@AttributeOverride(
				name = "setCode",
				column = @Column(name = "zhhant_setcode")
			),
			@AttributeOverride(
				name = "setCodeAlt",
				column = @Column(name = "zhhant_setcode_alt")
			),
		}
	)
	public RawSetLocalizedData zhHant = new RawSetLocalizedData();
}
