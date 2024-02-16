package io.github.ygojson.tools.test;

import com.fasterxml.jackson.core.JsonProcessingException;

import io.github.ygojson.model.data.Card;
import io.github.ygojson.model.utils.JsonUtils;

public class CardMother {

	private CardMother() {
		// cannot be instantiated - mother class
	}

	public static Card darkMagian() {
        return fromJson("""
			{
			  "identifiers" : {
			    "konamiId" : 4041,
			    "password" : 46986414,
			    "yugipediaPageId" : 1132
			  },
			  "name" : "Dark Magician",
			  "cardType" : "monster",
			  "monsterTypes" : [ "spellcaster", "normal" ],
			  "flavorText" : "The ultimate wizard in terms of attack and defense.",
			  "attribute" : "dark",
			  "atk" : 2500,
			  "def" : 2100,
			  "level" : 7,
			  "localizedData" : {
			    "de" : {
			      "name" : "Dunkler Magier",
			      "flavorText" : "Der ultimative Hexer im Hinblick auf Angriff und Verteidigung."
			    },
			    "es" : {
			      "name" : "Mago Oscuro",
			      "flavorText" : "El más grande de los magos en cuanto al ataque y la defensa."
			    },
			    "fr" : {
			      "name" : "Magicien Sombre",
			      "flavorText" : "Mage suprême en termes d'attaque et de défense."
			    },
			    "it" : {
			      "name" : "Mago Nero",
			      "flavorText" : "Il più potente tra i maghi per abilità offensive e difensive."
			    },
			    "ja" : {
			      "name" : "ブラック・マジシャン",
			      "flavorText" : "<ruby>魔<rt>ま</rt></ruby><ruby>法<rt>ほう</rt></ruby><ruby>使<rt>つか</rt></ruby>いとしては、<ruby>攻<rt>こう</rt></ruby><ruby>撃<rt>げき</rt></ruby><ruby>力<rt>りょく</rt></ruby>・<ruby>守<rt>しゅ</rt></ruby><ruby>備<rt>び</rt></ruby><ruby>力<rt>りょく</rt></ruby>ともに<ruby>最<rt>さい</rt></ruby><ruby>高<rt>こう</rt></ruby>クラス。"
			    },
			    "ko" : {
			      "name" : "블랙 매지션",
			      "flavorText" : "마법사 중에서 공격력 / 수비력이 동시에 가장 높은 계급."
			    },
			    "pt" : {
			      "name" : "Mago Negro",
			      "flavorText" : "O mago definitivo em termos de ataque e defesa."
			    },
			    "zh-Hans" : {
			      "name" : "黑魔导",
			      "flavorText" : "作为魔法使，攻击力・守备力均为最高级别。"
			    },
			    "zh-Hant" : {
			      "name" : "黒魔道士",
			      "flavorText" : "魔法族中攻撃力、防守力都最彊"
			    }
			  }
			}
			"""
		);
    }

	private static Card fromJson(final String json) {
		try {
			return JsonUtils.getObjectMapper().readValue(json, Card.class);
		} catch (final JsonProcessingException e) {
			// TODO: better exception
			throw new RuntimeException(e);
		}
	}
}
