package io.github.ygojson.tools.client.yugipedia.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * Model to represent all possible continue properties from a Yugipedia API response
 * used on YGOJSON.
 * <br>
 * Note that not all of them are used for the same return type.
 */
@Data
public class Continue {

	private String gcmcontinue;

	private String grccontinue;

	@JsonProperty("continue")
	private String continueProperty;
}
