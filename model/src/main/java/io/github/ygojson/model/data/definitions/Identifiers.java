package io.github.ygojson.model.data.definitions;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import io.github.ygojson.model.data.Card;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * Identifiers associated to the {@link Card}.
 * </br>
 * Note that this model is associated with an atomic card and not a print,
 * except {@link #getPasswordAlt()}, which makes sense as it should also
 * identify the atomic card.
 */
@JsonClassDescription(
        """
        Identifiers associated to the Card (atomic).
        
        This model is a container of third-party IDs associated with the Card (atomic)."""
)
@JsonPropertyOrder({
        Identifiers.KONAMI_ID_PROPERTY,
        Identifiers.PASSWORD_PROPERTY,
        Identifiers.PASSWORD_ALT_PROPERTY,
        Identifiers.YUGIPEDIA_PAGE_ID_PROPERTY
})
@Getter
@JsonPOJOBuilder
@SuperBuilder(setterPrefix = "with", toBuilder = true)
public class Identifiers {

    public static final String KONAMI_ID_PROPERTY = "konamiId";
    public static final String PASSWORD_PROPERTY = "password";
    public static final String PASSWORD_ALT_PROPERTY = "passwordAlt";
    public static final String YUGIPEDIA_PAGE_ID_PROPERTY = "yugipediaPageId";

    /**
     * ID on the <a href="https://www.db.yugioh-card.com/yugiohdb/">Yu-Gi-Oh official database</a>.
     * </br>
     * The ID might not appear on the official database for some languages if not
     * released yet on that location.
     * Also, cards might not have an ID if not released at all.
     */
    @JsonPropertyDescription(
            """
            ID on the <a href="https://www.db.yugioh-card.com/yugiohdb/">Yu-Gi-Oh official database</a>.
            
            The ID might not appear on the official database for some languages if it was not released yet on that location.
            Also, cards might not have an ID if nor released at all."""
    )
    @JsonProperty(value = KONAMI_ID_PROPERTY)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long konamiId;

    /**
     * Unique 8-digit number printed in the bottom of cards, which is the same in all languages and prints.
     * </br>
     * Certain cards does not have a password.
     * </br>
     * Only few cards have more than one password for different prints (see also {@link #getPasswordAlt()}
     * for those.
     */
    @JsonPropertyDescription(
            """
            Unique 8-digit number printed in the bottom of cards, which is the same in all languages and prints.
            
            Note that certain cards does not have a password."""
    )
    @JsonProperty(value = PASSWORD_PROPERTY)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long password;

    /**
     * Unique 8-digit identifier assigned to a different print of a card on some special cases.
     * </br>
     * Examples of these are the <em>Dark Magician (Arkana)</em> and
     * the alternative art of <em>Polymerization</em>.
     */
    @JsonPropertyDescription(
            """
            Unique 8-digit number printed in the bottom of different prints of a card on some special cases.
             
            Examples of these are the <em>Dark Magician (Arkana)</em> and the alternative art of <em>Polymerization</em>"""
    )
    @JsonProperty(value = PASSWORD_ALT_PROPERTY)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long passwordAlt;

    /**
     * Page ID on <a href="https://yugipedia.com">Yugipedia</a>.
     * </br>
     * This ID can be used on the <a href="https://yugipedia.com/api.php">Yugipedia API</a>
     * as the {@code pageId} or {@code pageids} parameters.
     */
    @JsonPropertyDescription(
            """
            Page ID on <a href="https://yugipedia.com">Yugipedia</a>.
            
            This ID can be used on the <a href="https://yugipedia.com/api.php">Yugipedia API</a> as the <em>pageId</em> or <em>pageids</em> parameters."""
    )
    @JsonProperty(value = YUGIPEDIA_PAGE_ID_PROPERTY)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long yugipediaPageId;

}
