package io.github.ygojson.application.core.db.set;

import java.util.List;
import java.util.UUID;
import java.util.function.BiConsumer;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;

/**
 * Set-entity.
 * <br>
 * Note that the {@link #id} is internal for the database,
 * and it is not related with the YGOJSON model.
 */
@Entity
@Table(name = "SET_TABLE")
public class SetEntity extends PanacheEntity {

	// YGOJSON-ID
	public UUID ygojsonId;
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

	public Long getId() {
		return id;
	}

	// helper method to set several set codes at the same time
	private void setSetCodes(
		final List<String> values,
		final BiConsumer<SetEntity, String> setCodeSetter,
		final BiConsumer<SetEntity, String> setCodeAltSetter
	) {
		if (values == null) {
			throw new IllegalArgumentException("Cannot set null set-codes");
		}
		switch (values.size()) {
			case 0:
				setCodeSetter.accept(this, null);
				setCodeAltSetter.accept(this, null);
				break;
			case 1:
				setCodeSetter.accept(this, values.getFirst());
				break;
			case 2:
				setCodeSetter.accept(this, values.getFirst());
				setCodeAltSetter.accept(this, values.getLast());
				break;
			default:
				throw new IllegalArgumentException(
					"Cannot set " + values.size() + " set-codes"
				);
		}
	}

	/**
	 * Set the English set-codes (default and alternative).
	 *
	 * @param values the set codes to be set as a list.
	 * @throws IllegalArgumentException if the list has more than two elements.
	 */
	public void setEnSetCodes(final List<String> values) {
		setSetCodes(
			values,
			(entity, value) -> entity.enSetCode = value,
			(entity, value) -> entity.enSetCodeAlt = value
		);
	}

	/**
	 * Set the German set-codes (default and alternative).
	 *
	 * @param values the set codes to be set as a list.
	 * @throws IllegalArgumentException if the list has more than two elements.
	 */
	public void setDeSetCodes(final List<String> values) {
		setSetCodes(
			values,
			(entity, value) -> entity.deSetCode = value,
			(entity, value) -> entity.deSetCodeAlt = value
		);
	}

	/**
	 * Set the Spanish set-codes (default and alternative).
	 *
	 * @param values the set codes to be set as a list.
	 * @throws IllegalArgumentException if the list has more than two elements.
	 */
	public void setEsSetCodes(final List<String> values) {
		setSetCodes(
			values,
			(entity, value) -> entity.esSetCode = value,
			(entity, value) -> entity.esSetCodeAlt = value
		);
	}

	/**
	 * Set the French set-codes (default and alternative).
	 *
	 * @param values the set codes to be set as a list.
	 * @throws IllegalArgumentException if the list has more than two elements.
	 */
	public void setFrSetCodes(final List<String> values) {
		setSetCodes(
			values,
			(entity, value) -> entity.frSetCode = value,
			(entity, value) -> entity.frSetCodeAlt = value
		);
	}

	/**
	 * Set the Italian set-codes (default and alternative).
	 *
	 * @param values the set codes to be set as a list.
	 * @throws IllegalArgumentException if the list has more than two elements.
	 */
	public void setItSetCodes(final List<String> values) {
		setSetCodes(
			values,
			(entity, value) -> entity.itSetCode = value,
			(entity, value) -> entity.itSetCodeAlt = value
		);
	}

	/**
	 * Set the Japanese set-codes (default and alternative).
	 *
	 * @param values the set codes to be set as a list.
	 * @throws IllegalArgumentException if the list has more than two elements.
	 */
	public void setJaSetCodes(final List<String> values) {
		setSetCodes(
			values,
			(entity, value) -> entity.jaSetCode = value,
			(entity, value) -> entity.jaSetCodeAlt = value
		);
	}

	/**
	 * Set the Korean set-codes (default and alternative).
	 *
	 * @param values the set codes to be set as a list.
	 * @throws IllegalArgumentException if the list has more than two elements.
	 */
	public void setKoSetCodes(final List<String> values) {
		setSetCodes(
			values,
			(entity, value) -> entity.koSetCode = value,
			(entity, value) -> entity.koSetCodeAlt = value
		);
	}

	/**
	 * Set the Portuguese set-codes (default and alternative).
	 *
	 * @param values the set codes to be set as a list.
	 * @throws IllegalArgumentException if the list has more than two elements.
	 */
	public void setPtSetCodes(final List<String> values) {
		setSetCodes(
			values,
			(entity, value) -> entity.ptSetCode = value,
			(entity, value) -> entity.ptSetCodeAlt = value
		);
	}

	/**
	 * Set the Simplified Chinese set-codes (default and alternative).
	 *
	 * @param values the set codes to be set as a list.
	 * @throws IllegalArgumentException if the list has more than two elements.
	 */
	public void setZhHansSetCodes(final List<String> values) {
		setSetCodes(
			values,
			(entity, value) -> entity.zhHansSetCode = value,
			(entity, value) -> entity.zhHansSetCodeAlt = value
		);
	}

	/**
	 * Set the Traditional Chinese set-codes (default and alternative).
	 *
	 * @param values the set codes to be set as a list.
	 * @throws IllegalArgumentException if the list has more than two elements.
	 */
	public void setZhHantSetCodes(final List<String> values) {
		setSetCodes(
			values,
			(entity, value) -> entity.zhHantSetCode = value,
			(entity, value) -> entity.zhHantSetCodeAlt = value
		);
	}
}
