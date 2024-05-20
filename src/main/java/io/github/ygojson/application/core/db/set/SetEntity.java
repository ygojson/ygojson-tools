package io.github.ygojson.application.core.db.set;

import java.util.List;
import java.util.UUID;
import java.util.function.BiConsumer;

import jakarta.persistence.*;

@Entity
@Table(name = "SET_TABLE")
public class SetEntity {

	// runtime DB ID
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long id;

	// YGOJSON-ID
	private UUID ygojsonId;
	// common independently of the localization
	private String printNumberPrefix;
	private String type;
	private String series;

	// name (localized)
	private String nameEn;
	private String nameDe;
	private String nameEs;
	private String nameFr;
	private String nameIt;
	private String nameJa;
	private String nameKo;
	private String namePt;
	private String nameZhHans;
	private String nameZhHant;
	// alternative name (only in English - prefixed to be able to extend)
	private String nameAltEn;
	// set codes (localized)
	private String setCodeEn;
	private String setCodeDe;
	private String setCodeEs;
	private String setCodeFr;
	private String setCodeIt;
	private String setCodeJa;
	private String setCodeKo;
	private String setCodePt;
	private String setCodeZhHans;
	private String setCodeZhHant;
	// alternative set codes (localized)
	private String setCodeAltEn;
	private String setCodeAltDe;
	private String setCodeAltEs;
	private String setCodeAltFr;
	private String setCodeAltIt;
	private String setCodeAltJa;
	private String setCodeAltKo;
	private String setCodeAltPt;
	private String setCodeAltZhHans;
	private String setCodeAltZhHant;

	public Long getId() {
		return id;
	}

	public UUID getYgojsonId() {
		return ygojsonId;
	}

	public void setYgojsonId(UUID ygojsonId) {
		this.ygojsonId = ygojsonId;
	}

	public String getPrintNumberPrefix() {
		return printNumberPrefix;
	}

	public void setPrintNumberPrefix(String printNumberPrefix) {
		this.printNumberPrefix = printNumberPrefix;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getSeries() {
		return series;
	}

	public void setSeries(String series) {
		this.series = series;
	}

	public String getNameEn() {
		return nameEn;
	}

	public void setNameEn(String nameEn) {
		this.nameEn = nameEn;
	}

	public String getNameDe() {
		return nameDe;
	}

	public void setNameDe(String nameDe) {
		this.nameDe = nameDe;
	}

	public String getNameEs() {
		return nameEs;
	}

	public void setNameEs(String nameEs) {
		this.nameEs = nameEs;
	}

	public String getNameFr() {
		return nameFr;
	}

	public void setNameFr(String nameFr) {
		this.nameFr = nameFr;
	}

	public String getNameIt() {
		return nameIt;
	}

	public void setNameIt(String nameIt) {
		this.nameIt = nameIt;
	}

	public String getNameJa() {
		return nameJa;
	}

	public void setNameJa(String nameJa) {
		this.nameJa = nameJa;
	}

	public String getNameKo() {
		return nameKo;
	}

	public void setNameKo(String nameKo) {
		this.nameKo = nameKo;
	}

	public String getNamePt() {
		return namePt;
	}

	public void setNamePt(String namePt) {
		this.namePt = namePt;
	}

	public String getNameZhHans() {
		return nameZhHans;
	}

	public void setNameZhHans(String nameZhHans) {
		this.nameZhHans = nameZhHans;
	}

	public String getNameZhHant() {
		return nameZhHant;
	}

	public void setNameZhHant(String nameZhHant) {
		this.nameZhHant = nameZhHant;
	}

	public String getNameAltEn() {
		return nameAltEn;
	}

	public void setNameAltEn(String nameAltEn) {
		this.nameAltEn = nameAltEn;
	}

	public String getSetCodeEn() {
		return setCodeEn;
	}

	public void setSetCodeEn(String setCodeEn) {
		this.setCodeEn = setCodeEn;
	}

	public String getSetCodeDe() {
		return setCodeDe;
	}

	public void setSetCodeDe(String setCodeDe) {
		this.setCodeDe = setCodeDe;
	}

	public String getSetCodeEs() {
		return setCodeEs;
	}

	public void setSetCodeEs(String setCodeEs) {
		this.setCodeEs = setCodeEs;
	}

	public String getSetCodeFr() {
		return setCodeFr;
	}

	public void setSetCodeFr(String setCodeFr) {
		this.setCodeFr = setCodeFr;
	}

	public String getSetCodeIt() {
		return setCodeIt;
	}

	public void setSetCodeIt(String setCodeIt) {
		this.setCodeIt = setCodeIt;
	}

	public String getSetCodeJa() {
		return setCodeJa;
	}

	public void setSetCodeJa(String setCodeJa) {
		this.setCodeJa = setCodeJa;
	}

	public String getSetCodeKo() {
		return setCodeKo;
	}

	public void setSetCodeKo(String setCodeKo) {
		this.setCodeKo = setCodeKo;
	}

	public String getSetCodePt() {
		return setCodePt;
	}

	public void setSetCodePt(String setCodePt) {
		this.setCodePt = setCodePt;
	}

	public String getSetCodeZhHans() {
		return setCodeZhHans;
	}

	public void setSetCodeZhHans(String setCodeZhHans) {
		this.setCodeZhHans = setCodeZhHans;
	}

	public String getSetCodeZhHant() {
		return setCodeZhHant;
	}

	public void setSetCodeZhHant(String setCodeZhHant) {
		this.setCodeZhHant = setCodeZhHant;
	}

	public String getSetCodeAltEn() {
		return setCodeAltEn;
	}

	public void setSetCodeAltEn(String setCodeAltEn) {
		this.setCodeAltEn = setCodeAltEn;
	}

	public String getSetCodeAltDe() {
		return setCodeAltDe;
	}

	public void setSetCodeAltDe(String setCodeAltDe) {
		this.setCodeAltDe = setCodeAltDe;
	}

	public String getSetCodeAltEs() {
		return setCodeAltEs;
	}

	public void setSetCodeAltEs(String setCodeAltEs) {
		this.setCodeAltEs = setCodeAltEs;
	}

	public String getSetCodeAltFr() {
		return setCodeAltFr;
	}

	public void setSetCodeAltFr(String setCodeAltFr) {
		this.setCodeAltFr = setCodeAltFr;
	}

	public String getSetCodeAltIt() {
		return setCodeAltIt;
	}

	public void setSetCodeAltIt(String setCodeAltIt) {
		this.setCodeAltIt = setCodeAltIt;
	}

	public String getSetCodeAltJa() {
		return setCodeAltJa;
	}

	public void setSetCodeAltJa(String setCodeAltJa) {
		this.setCodeAltJa = setCodeAltJa;
	}

	public String getSetCodeAltKo() {
		return setCodeAltKo;
	}

	public void setSetCodeAltKo(String setCodeAltKo) {
		this.setCodeAltKo = setCodeAltKo;
	}

	public String getSetCodeAltPt() {
		return setCodeAltPt;
	}

	public void setSetCodeAltPt(String setCodeAltPt) {
		this.setCodeAltPt = setCodeAltPt;
	}

	public String getSetCodeAltZhHans() {
		return setCodeAltZhHans;
	}

	public void setSetCodeAltZhHans(String setCodeAltZhHans) {
		this.setCodeAltZhHans = setCodeAltZhHans;
	}

	public String getSetCodeAltZhHant() {
		return setCodeAltZhHant;
	}

	public void setSetCodeAltZhHant(String setCodeAltZhHant) {
		this.setCodeAltZhHant = setCodeAltZhHant;
	}

	// helper method to set several set codes at the same time
	private void setSetCodes(
		final List<String> values,
		final BiConsumer<SetEntity, String> setCodeSetter,
		final BiConsumer<SetEntity, String> setCodeAltSetter
	) {
		switch (values.size()) {
			case 0:
				break;
			case 1:
				setCodeSetter.accept(this, values.getFirst());
				break;
			case 2:
				setCodeSetter.accept(this, values.getFirst());
				setCodeAltSetter.accept(this, values.getLast());
				break;
			default:
				// TODO: better a cachable exception
				throw new IllegalStateException("Cannot set set-codes");
		}
	}

	/**
	 * Set the English set-codes (default and alternative).
	 *
	 * @param values the set codes to be set as a list.
	 * @throws IllegalArgumentException if the list has more than two elements.
	 */
	public void setSetCodesEn(final List<String> values) {
		setSetCodes(values, SetEntity::setSetCodeEn, SetEntity::setSetCodeAltEn);
	}

	/**
	 * Set the German set-codes (default and alternative).
	 *
	 * @param values the set codes to be set as a list.
	 * @throws IllegalArgumentException if the list has more than two elements.
	 */
	public void setSetCodesDe(final List<String> values) {
		setSetCodes(values, SetEntity::setSetCodeDe, SetEntity::setSetCodeAltDe);
	}

	/**
	 * Set the Spanish set-codes (default and alternative).
	 *
	 * @param values the set codes to be set as a list.
	 * @throws IllegalArgumentException if the list has more than two elements.
	 */
	public void setSetCodesEs(final List<String> values) {
		setSetCodes(values, SetEntity::setSetCodeEs, SetEntity::setSetCodeAltEs);
	}

	/**
	 * Set the French set-codes (default and alternative).
	 *
	 * @param values the set codes to be set as a list.
	 * @throws IllegalArgumentException if the list has more than two elements.
	 */
	public void setSetCodesFr(final List<String> values) {
		setSetCodes(values, SetEntity::setSetCodeFr, SetEntity::setSetCodeAltFr);
	}

	/**
	 * Set the Italian set-codes (default and alternative).
	 *
	 * @param values the set codes to be set as a list.
	 * @throws IllegalArgumentException if the list has more than two elements.
	 */
	public void setSetCodesIt(final List<String> values) {
		setSetCodes(values, SetEntity::setSetCodeIt, SetEntity::setSetCodeAltIt);
	}

	/**
	 * Set the Japanese set-codes (default and alternative).
	 *
	 * @param values the set codes to be set as a list.
	 * @throws IllegalArgumentException if the list has more than two elements.
	 */
	public void setSetCodesJa(final List<String> values) {
		setSetCodes(values, SetEntity::setSetCodeJa, SetEntity::setSetCodeAltJa);
	}

	/**
	 * Set the Korean set-codes (default and alternative).
	 *
	 * @param values the set codes to be set as a list.
	 * @throws IllegalArgumentException if the list has more than two elements.
	 */
	public void setSetCodesKo(final List<String> values) {
		setSetCodes(values, SetEntity::setSetCodeKo, SetEntity::setSetCodeAltKo);
	}

	/**
	 * Set the Portuguese set-codes (default and alternative).
	 *
	 * @param values the set codes to be set as a list.
	 * @throws IllegalArgumentException if the list has more than two elements.
	 */
	public void setSetCodesPt(final List<String> values) {
		setSetCodes(values, SetEntity::setSetCodePt, SetEntity::setSetCodeAltPt);
	}

	/**
	 * Set the Simplified Chinese set-codes (default and alternative).
	 *
	 * @param values the set codes to be set as a list.
	 * @throws IllegalArgumentException if the list has more than two elements.
	 */
	public void setSetCodesZhHans(final List<String> values) {
		setSetCodes(
			values,
			SetEntity::setSetCodeZhHans,
			SetEntity::setSetCodeAltZhHans
		);
	}

	/**
	 * Set the Traditional Chinese set-codes (default and alternative).
	 *
	 * @param values the set codes to be set as a list.
	 * @throws IllegalArgumentException if the list has more than two elements.
	 */
	public void setSetCodesZhHant(final List<String> values) {
		setSetCodes(
			values,
			SetEntity::setSetCodeZhHant,
			SetEntity::setSetCodeAltZhHant
		);
	}
}
