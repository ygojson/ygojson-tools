package io.github.ygojson.tools.dataprovider.impl.yugipedia.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

/**
 * Yugipedia's Model for CardTable2.
 * <br>
 * {@link MarkupString} is used where markup content might be present.
 * The rest of fileds are typed as {@link String} to prevent issues
 * while typing values with special markers (i.e., atk/def values as '?'
 * cannot be typed as a numeric).
 *
 * @see <a href=https://yugipedia.com/wiki/Template:CardTable2>CardTable2</a>
 */
public record CardTable2(
	String cardclass,
	String name,
	String fr_name,
	String de_name,
	String it_name,
	String es_name,
	@JsonSerialize(using = ToStringSerializer.class) // ensure text with markup serialization
	MarkupString ja_name,
	String romaji_name,
	String trans_name,
	String base_romaji_name,
	String base_trans_name,
	String zh_name,
	String zh_pinyin_name,
	String zh_trans_name,
	String ko_name,
	String ko_rr_name,
	String ko_trans_name,
	String alt_name,
	String alt_name2,
	String alt_name3,
	String ja_alt_name,
	String ja_alt_romaji,
	String ja_alt_trans,
	String image,
	String image2,
	String card_type,
	String property,
	String attribute,
	String character,
	String types,
	String level,
	String rank,
	String link_arrows,
	String pendulum_scale,
	String maximum_atk,
	String atk,
	String def,
	String password,
	String limitation_text,
	String ritualcard,
	String ritualmonster,
	String summon,
	String effect_types,
	String pendulum_effect_types,
	String ocg_status,
	String tcg_status,
	String tcg_speed_duel_status,
	String source_card,
	String skill_activation,
	String fr_skill_activation,
	String de_skill_activation,
	String it_skill_activation,
	String pt_skill_activation,
	String es_skill_activation,
	@JsonSerialize(using = ToStringSerializer.class) // ensure text with markup serialization
	MarkupString pendulum_effect,
	@JsonSerialize(using = ToStringSerializer.class) // ensure text with markup serialization
	MarkupString fr_pendulum_effect,
	@JsonSerialize(using = ToStringSerializer.class) // ensure text with markup serialization
	MarkupString de_pendulum_effect,
	@JsonSerialize(using = ToStringSerializer.class) // ensure text with markup serialization
	MarkupString it_pendulum_effect,
	@JsonSerialize(using = ToStringSerializer.class) // ensure text with markup serialization
	MarkupString pt_pendulum_effect,
	@JsonSerialize(using = ToStringSerializer.class) // ensure text with markup serialization
	MarkupString es_pendulum_effect,
	@JsonSerialize(using = ToStringSerializer.class) // ensure text with markup serialization
	MarkupString ja_pendulum_effect,
	@JsonSerialize(using = ToStringSerializer.class) // ensure text with markup serialization
	MarkupString trans_pendulum_effect,
	@JsonSerialize(using = ToStringSerializer.class) // ensure text with markup serialization
	MarkupString zh_pendulum_effect,
	@JsonSerialize(using = ToStringSerializer.class) // ensure text with markup serialization
	MarkupString ko_pendulum_effect,
	@JsonSerialize(using = ToStringSerializer.class) // ensure text with markup serialization
	MarkupString lore,
	@JsonSerialize(using = ToStringSerializer.class) // ensure text with markup serialization
	MarkupString fr_lore,
	@JsonSerialize(using = ToStringSerializer.class) // ensure text with markup serialization
	MarkupString de_lore,
	@JsonSerialize(using = ToStringSerializer.class) // ensure text with markup serialization
	MarkupString it_lore,
	@JsonSerialize(using = ToStringSerializer.class) // ensure text with markup serialization
	MarkupString pt_lore,
	@JsonSerialize(using = ToStringSerializer.class) // ensure text with markup serialization
	MarkupString es_lore,
	@JsonSerialize(using = ToStringSerializer.class) // ensure text with markup serialization
	MarkupString ja_lore,
	@JsonSerialize(using = ToStringSerializer.class) // ensure text with markup serialization
	MarkupString trans_lore,
	@JsonSerialize(using = ToStringSerializer.class) // ensure text with markup serialization
	MarkupString zh_lore,
	@JsonSerialize(using = ToStringSerializer.class) // ensure text with markup serialization
	MarkupString ko_lore,
	String use,
	String database_id,
	@JsonSerialize(using = ToStringSerializer.class) // ensure text with markup serialization
	MarkupString en_sets,
	@JsonSerialize(using = ToStringSerializer.class) // ensure text with markup serialization
	MarkupString na_sets,
	@JsonSerialize(using = ToStringSerializer.class) // ensure text with markup serialization
	MarkupString eu_sets,
	@JsonSerialize(using = ToStringSerializer.class) // ensure text with markup serialization
	MarkupString au_sets,
	@JsonSerialize(using = ToStringSerializer.class) // ensure text with markup serialization
	MarkupString fr_sets,
	@JsonSerialize(using = ToStringSerializer.class) // ensure text with markup serialization
	MarkupString fc_sets,
	@JsonSerialize(using = ToStringSerializer.class) // ensure text with markup serialization
	MarkupString de_sets,
	@JsonSerialize(using = ToStringSerializer.class) // ensure text with markup serialization
	MarkupString it_sets,
	@JsonSerialize(using = ToStringSerializer.class) // ensure text with markup serialization
	MarkupString pt_sets,
	@JsonSerialize(using = ToStringSerializer.class) // ensure text with markup serialization
	MarkupString sp_sets,
	@JsonSerialize(using = ToStringSerializer.class) // ensure text with markup serialization
	MarkupString jp_sets,
	@JsonSerialize(using = ToStringSerializer.class) // ensure text with markup serialization
	MarkupString ja_sets,
	@JsonSerialize(using = ToStringSerializer.class) // ensure text with markup serialization
	MarkupString ae_sets,
	@JsonSerialize(using = ToStringSerializer.class) // ensure text with markup serialization
	MarkupString tc_sets,
	@JsonSerialize(using = ToStringSerializer.class) // ensure text with markup serialization
	MarkupString sc_sets,
	@JsonSerialize(using = ToStringSerializer.class) // ensure text with markup serialization
	MarkupString kr_sets,
	@JsonSerialize(using = ToStringSerializer.class) // ensure text with markup serialization
	MarkupString supports,
	@JsonProperty("anti-supports") // not valid java variable name
	String anti_supports,
	String archseries,
	String supports_archetypes,
	@JsonProperty("anti-supports_archetypes") // not valid java variable name
	String anti_supports_archetypes,
	String related_to_archetypes,
	String counter,
	String action,
	String stat_change,
	@JsonProperty("m/s/t") // not valid java variable name
	String m_s_t,
	String summoning,
	String attack,
	String banished,
	String life_points,
	String fm_for,
	String sm_for,
	String misc,
	String fusion_material,
	String synchro_material,
	String material
) {}
