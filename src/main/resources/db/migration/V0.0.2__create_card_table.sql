CREATE TABLE
    IF NOT EXISTS "tbl_card" (
        "card_id" VARCHAR(36) UNIQUE NOT NULL,
        "ygojson_id" VARCHAR(36) UNIQUE,
        "konami_id" BIGINT UNIQUE,
        "password" BIGINT UNIQUE,
        "password_alt" BIGINT UNIQUE,
        "yugipedia_pageid" BIGINT UNIQUE,
        "card_type" TEXT,
        "property" TEXT,
        "monster_types" TEXT,
        "attribute" TEXT,
        "atk" INTEGER,
        "atk_undef" BOOLEAN,
        "def" INTEGER,
        "def_undef" BOOLEAN,
        "level" INTEGER,
        "pendulum_scale" INTEGER,
        "link_rating" INTEGER,
        "link_arrows" TEXT,
        "xyz_rank" INTEGER,
        "name" TEXT,
        "effect_text" TEXT,
        "flavor_text" TEXT,
        "materials_text" TEXT,
        "pendulum_effect_text" TEXT,
        "de_name" TEXT,
        "de_effect_text" TEXT,
        "de_flavor_text" TEXT,
        "de_materials_text" TEXT,
        "de_pendulum_effect_text" TEXT,
        "es_name" TEXT,
        "es_effect_text" TEXT,
        "es_flavor_text" TEXT,
        "es_materials_text" TEXT,
        "es_pendulum_effect_text" TEXT,
        "fr_name" TEXT,
        "fr_effect_text" TEXT,
        "fr_flavor_text" TEXT,
        "fr_materials_text" TEXT,
        "fr_pendulum_effect_text" TEXT,
        "it_name" TEXT,
        "it_effect_text" TEXT,
        "it_flavor_text" TEXT,
        "it_materials_text" TEXT,
        "it_pendulum_effect_text" TEXT,
        "ja_name" TEXT,
        "ja_effect_text" TEXT,
        "ja_flavor_text" TEXT,
        "ja_materials_text" TEXT,
        "ja_pendulum_effect_text" TEXT,
        "ko_name" TEXT,
        "ko_effect_text" TEXT,
        "ko_flavor_text" TEXT,
        "ko_materials_text" TEXT,
        "ko_pendulum_effect_text" TEXT,
        "pt_name" TEXT,
        "pt_effect_text" TEXT,
        "pt_flavor_text" TEXT,
        "pt_materials_text" TEXT,
        "pt_pendulum_effect_text" TEXT,
        "zhhans_name" TEXT,
        "zhhans_effect_text" TEXT,
        "zhhans_flavor_text" TEXT,
        "zhhans_materials_text" TEXT,
        "zhhans_pendulum_effect_text" TEXT,
        "zhhant_name" TEXT,
        "zhhant_effect_text" TEXT,
        "zhhant_flavor_text" TEXT,
        "zhhant_materials_text" TEXT,
        "zhhant_pendulum_effect_text" TEXT,
        PRIMARY KEY ("card_id")
    );

CREATE UNIQUE INDEX "index.tbl_card.id" ON "tbl_card" ("card_id");

CREATE UNIQUE INDEX "index.tbl_card.ygojson_id" ON "tbl_card" ("ygojson_id");

CREATE UNIQUE INDEX "index.tbl_card.konami_id" ON "tbl_card" ("konami_id");

CREATE UNIQUE INDEX "index.tbl_card.password" ON "tbl_card" ("password");

CREATE UNIQUE INDEX "index.tbl_card.yugipedia_pageid" ON "tbl_card" ("yugipedia_pageid");

CREATE INDEX "index.tbl_card.name" ON "tbl_card" ("name");