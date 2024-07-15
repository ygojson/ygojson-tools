package io.github.ygojson.application.core.db.card;

import java.util.List;

import jakarta.persistence.*;

import io.github.ygojson.application.core.db.RuntimeBaseEntity;
import io.github.ygojson.application.core.db.converter.JsonStringListAttributeConverter;

/**
 * Card-entity.
 * <br>
 * Note that the {@link #id} is internal for the database,
 * and it is not related with the YGOJSON model.
 */
@Entity
@Table(name = "tbl_card")
@AttributeOverride(
	name = RuntimeBaseEntity.ID_COLUMN,
	column = @Column(name = "card_id")
)
public class CardEntity extends RuntimeBaseEntity {

	@Embedded
	public IdentifierValues identifiers;

	// common independently of the localization
	@Column(name = "card_type")
	public String cardType;

	@Column(name = "property")
	public String property;

	// TODO: keep the original moster-type line?
	@Column(name = "monster_types")
	@Convert(converter = JsonStringListAttributeConverter.class)
	public List<String> monsterTypes;

	public String attribute;

	// TODO: use String and have properly mapped with updater
	public Integer atk;

	@Column(name = "atk_undef")
	public Boolean atkUndefined;

	// TODO: use String and have properly mapped with updater
	public Integer def;

	@Column(name = "def_undef")
	public Boolean defUndefined;

	public Integer level;

	@Column(name = "pendulum_scale")
	public Integer pendulumScale;

	@Column(name = "link_rating")
	public Integer linkRating;

	// TODO: we should have an enum to ensure that mappers are always in line with expectations
	@Column(name = "link_arrows")
	@Convert(converter = JsonStringListAttributeConverter.class)
	public List<String> linkArrows;

	@Column(name = "xyz_rank")
	public Integer xyzRank;

	// MAIN PROPERTIES (ENGLISH) WILL NOT BE RENAMED IN DB
	@Embedded
	public CardLocalizedValues en;

	@AttributeOverrides(
		{
			@AttributeOverride(name = "name", column = @Column(name = "de_name")),
			@AttributeOverride(
				name = "effectText",
				column = @Column(name = "de_effect_text")
			),
			@AttributeOverride(
				name = "flavorText",
				column = @Column(name = "de_flavor_text")
			),
			@AttributeOverride(
				name = "materialsText",
				column = @Column(name = "de_materials_text")
			),
			@AttributeOverride(
				name = "pendulumEffectText",
				column = @Column(name = "de_pendulum_effect_text")
			),
		}
	)
	public CardLocalizedValues de;

	@AttributeOverrides(
		{
			@AttributeOverride(name = "name", column = @Column(name = "es_name")),
			@AttributeOverride(
				name = "effectText",
				column = @Column(name = "es_effect_text")
			),
			@AttributeOverride(
				name = "flavorText",
				column = @Column(name = "es_flavor_text")
			),
			@AttributeOverride(
				name = "materialsText",
				column = @Column(name = "es_materials_text")
			),
			@AttributeOverride(
				name = "pendulumEffectText",
				column = @Column(name = "es_pendulum_effect_text")
			),
		}
	)
	public CardLocalizedValues es;

	@AttributeOverrides(
		{
			@AttributeOverride(name = "name", column = @Column(name = "fr_name")),
			@AttributeOverride(
				name = "effectText",
				column = @Column(name = "fr_effect_text")
			),
			@AttributeOverride(
				name = "flavorText",
				column = @Column(name = "fr_flavor_text")
			),
			@AttributeOverride(
				name = "materialsText",
				column = @Column(name = "fr_materials_text")
			),
			@AttributeOverride(
				name = "pendulumEffectText",
				column = @Column(name = "fr_pendulum_effect_text")
			),
		}
	)
	public CardLocalizedValues fr;

	@AttributeOverrides(
		{
			@AttributeOverride(name = "name", column = @Column(name = "it_name")),
			@AttributeOverride(
				name = "effectText",
				column = @Column(name = "it_effect_text")
			),
			@AttributeOverride(
				name = "flavorText",
				column = @Column(name = "it_flavor_text")
			),
			@AttributeOverride(
				name = "materialsText",
				column = @Column(name = "it_materials_text")
			),
			@AttributeOverride(
				name = "pendulumEffectText",
				column = @Column(name = "it_pendulum_effect_text")
			),
		}
	)
	public CardLocalizedValues it;

	@AttributeOverrides(
		{
			@AttributeOverride(name = "name", column = @Column(name = "ja_name")),
			@AttributeOverride(
				name = "effectText",
				column = @Column(name = "ja_effect_text")
			),
			@AttributeOverride(
				name = "flavorText",
				column = @Column(name = "ja_flavor_text")
			),
			@AttributeOverride(
				name = "materialsText",
				column = @Column(name = "ja_materials_text")
			),
			@AttributeOverride(
				name = "pendulumEffectText",
				column = @Column(name = "ja_pendulum_effect_text")
			),
		}
	)
	public CardLocalizedValues ja;

	@AttributeOverrides(
		{
			@AttributeOverride(name = "name", column = @Column(name = "ko_name")),
			@AttributeOverride(
				name = "effectText",
				column = @Column(name = "ko_effect_text")
			),
			@AttributeOverride(
				name = "flavorText",
				column = @Column(name = "ko_flavor_text")
			),
			@AttributeOverride(
				name = "materialsText",
				column = @Column(name = "ko_materials_text")
			),
			@AttributeOverride(
				name = "pendulumEffectText",
				column = @Column(name = "ko_pendulum_effect_text")
			),
		}
	)
	public CardLocalizedValues ko;

	@AttributeOverrides(
		{
			@AttributeOverride(name = "name", column = @Column(name = "pt_name")),
			@AttributeOverride(
				name = "effectText",
				column = @Column(name = "pt_effect_text")
			),
			@AttributeOverride(
				name = "flavorText",
				column = @Column(name = "pt_flavor_text")
			),
			@AttributeOverride(
				name = "materialsText",
				column = @Column(name = "pt_materials_text")
			),
			@AttributeOverride(
				name = "pendulumEffectText",
				column = @Column(name = "pt_pendulum_effect_text")
			),
		}
	)
	public CardLocalizedValues pt;

	@AttributeOverrides(
		{
			@AttributeOverride(name = "name", column = @Column(name = "zhhans_name")),
			@AttributeOverride(
				name = "effectText",
				column = @Column(name = "zhhans_effect_text")
			),
			@AttributeOverride(
				name = "flavorText",
				column = @Column(name = "zhhans_flavor_text")
			),
			@AttributeOverride(
				name = "materialsText",
				column = @Column(name = "zhhans_materials_text")
			),
			@AttributeOverride(
				name = "pendulumEffectText",
				column = @Column(name = "zhhans_pendulum_effect_text")
			),
		}
	)
	public CardLocalizedValues zhHans;

	@AttributeOverrides(
		{
			@AttributeOverride(name = "name", column = @Column(name = "zhhant_name")),
			@AttributeOverride(
				name = "effectText",
				column = @Column(name = "zhhant_effect_text")
			),
			@AttributeOverride(
				name = "flavorText",
				column = @Column(name = "zhhant_flavor_text")
			),
			@AttributeOverride(
				name = "materialsText",
				column = @Column(name = "zhhant_materials_text")
			),
			@AttributeOverride(
				name = "pendulumEffectText",
				column = @Column(name = "zhhant_pendulum_effect_text")
			),
		}
	)
	public CardLocalizedValues zhHant;
}
