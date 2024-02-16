package io.github.ygojson.tools.test;

import com.fasterxml.jackson.core.JsonProcessingException;

import io.github.ygojson.model.data.Set;
import io.github.ygojson.model.utils.JsonUtils;

public class SetMother {

	private SetMother() {
		// cannot be instantiated - mother class
	}

	public static Set duelistNexus() {
        return fromJson("""
			{
			  "name" : "Duelist Nexus",
			  "setCode" : "DUNE",
			  "type" : "booster pack",
			  "series" : "core booster",
			  "localizedData" : {
			    "de" : {
			      "name" : "Duelist Nexus"
			    },
			    "es" : {
			      "name" : "Nexus Duelista"
			    },
			    "fr" : {
			      "name" : "Nexus du Duelliste"
			    },
			    "it" : {
			      "name" : "Nexus dei Duellanti"
			    },
			    "ja" : {
			      "name" : "デュエリスト・ネクサス"
			    },
			    "ko" : {
			      "name" : "듀얼리스트 넥서스"
			    },
			    "pt" : {
			      "name" : "Nexus Duelista"
			    },
			    "zh-Hans" : {
			      "name" : "决斗者之绊"
			    }
			  }
			}
			""");
    }

	private static Set fromJson(final String json) {
		try {
			return JsonUtils.getObjectMapper().readValue(json, Set.class);
		} catch (final JsonProcessingException e) {
			// TODO: better exception
			throw new RuntimeException(e);
		}
	}

}
