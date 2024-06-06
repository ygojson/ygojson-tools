package io.github.ygojson.application.core.db.print;

import jakarta.persistence.*;

import io.github.ygojson.application.core.db.RuntimeBaseEntity;
import io.github.ygojson.application.core.db.card.CardEntity;
import io.github.ygojson.application.core.db.set.SetEntity;
import io.github.ygojson.model.data.definition.localization.Language;
import io.github.ygojson.model.data.definition.localization.Region;

/**
 * Print-entity.
 * <br>
 * Note that the {@link #id} is internal for the database,
 * and it is not related with the YGOJSON model.
 */
@Entity
@Table(name = "tbl_print")
@AttributeOverride(
	name = RuntimeBaseEntity.ID_COLUMN,
	column = @Column(name = "print_id")
)
public class PrintEntity extends RuntimeBaseEntity {

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(
		name = "card_id",
		foreignKey = @ForeignKey(value = ConstraintMode.CONSTRAINT)
	)
	public CardEntity card;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(
		name = "set_id",
		foreignKey = @ForeignKey(value = ConstraintMode.CONSTRAINT)
	)
	public SetEntity set;

	@Column(name = "print_code")
	public String printCode;

	@Column(name = "setcode")
	public String setCode;

	@Column(name = "print_number_prefix")
	public String printNumberPrefix;

	@Column(name = "print_number")
	public String printNumber;

	@Column(name = "print_number_suffix")
	public String printNumberSuffix;

	// maybe this should be a
	@Column(name = "rarity")
	public String rarity;

	@Enumerated(EnumType.STRING)
	public Language language;

	@Column(name = "region_code")
	@Enumerated(EnumType.STRING)
	public Region regionCode;
}
