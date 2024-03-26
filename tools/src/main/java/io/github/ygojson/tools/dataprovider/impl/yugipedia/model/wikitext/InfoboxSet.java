package io.github.ygojson.tools.dataprovider.impl.yugipedia.model.wikitext;

import com.fasterxml.jackson.annotation.JsonProperty;

public record InfoboxSet(
	String en_name,
	String en_name_2,
	String fr_name,
	String de_name,
	String it_name,
	String pt_name,
	String es_name,
	MarkupString ja_name,
	String romaji_name,
	String ja_trans_name,
	MarkupString ko_name,
	String hanja_name,
	String ko_rr_name,
	String ko_trans_name,
	String sc_name,
	String tc_name,
	MarkupString type,
	MarkupString series,
	String parent_set,
	String prefix,
	String en_prefix,
	String na_prefix,
	String eu_prefix,
	String oc_prefix,
	String fr_prefix,
	String fc_prefix,
	String de_prefix,
	String it_prefix,
	String pt_prefix,
	String sp_prefix,
	String jp_prefix,
	String ja_prefix,
	String ae_prefix,
	String tc_prefix,
	String sc_prefix,
	String kr_prefix,
	String order_number,
	String size,
	String width,
	String image,
	MarkupString cover_card,
	MarkupString main_set,
	MarkupString sneak_peek,
	MarkupString premiere,
	MarkupString medium,
	@JsonProperty("2-pack_set") MarkupString two_pack_set,
	MarkupString vendor_edition,
	MarkupString special_edition,
	MarkupString deluxe_edition,
	MarkupString other_sets,
	MarkupString release_date,
	MarkupString jp_release_date,
	MarkupString ja_release_date,
	MarkupString ae_release_date,
	MarkupString en_release_date,
	MarkupString na_release_date,
	MarkupString eu_release_date,
	MarkupString oc_release_date,
	MarkupString tc_release_date,
	MarkupString sc_release_date,
	@JsonProperty("fr/de/it/sp_release_date")
	MarkupString fr_de_it_sp_release_date,
	MarkupString fr_release_date,
	MarkupString fc_release_date,
	MarkupString de_release_date,
	MarkupString it_release_date,
	MarkupString kr_release_date,
	MarkupString pt_release_date,
	MarkupString sp_release_date,
	@JsonProperty("sp-lat_release_date") MarkupString sp_lat_release_date,
	String database_id,
	String en_database_id,
	String fr_database_id,
	String de_database_id,
	String it_database_id,
	String pt_database_id,
	String es_database_id,
	String ja_database_id,
	String ko_database_id,
	String ae_database_id,
	String sc_database_id,
	String isbn,
	String ean,
	String upc,
	String postfix,
	MarkupString appears_in_video_games,
	MarkupString prev,
	MarkupString curr,
	MarkupString next
) {}
