package io.github.ygojson.application.core.db.set;

import jakarta.persistence.*;

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
	@Column(name = "print_number_prefix")
	public String printNumberPrefix;

	public String type;
	public String series;

	// MAIN NAME (ENGLISH) WILL NOT BE RENAMED IN DB
	public SetLocalizedValues en = new SetLocalizedValues();

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
	public SetLocalizedValues de = new SetLocalizedValues();

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
	public SetLocalizedValues es = new SetLocalizedValues();

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
	public SetLocalizedValues fr = new SetLocalizedValues();

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
	public SetLocalizedValues it = new SetLocalizedValues();

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
	public SetLocalizedValues ja = new SetLocalizedValues();

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
	public SetLocalizedValues ko = new SetLocalizedValues();

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
	public SetLocalizedValues pt = new SetLocalizedValues();

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
	public SetLocalizedValues zhHans = new SetLocalizedValues();

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
	public SetLocalizedValues zhHant = new SetLocalizedValues();
}
