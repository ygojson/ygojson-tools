CREATE TABLE
    IF NOT EXISTS "tbl_set" (
        "set_id" VARCHAR(36) UNIQUE NOT NULL,
        "ygojson_id" VARCHAR(36) UNIQUE,
        "yugipedia_pageid" BIGINT UNIQUE,
        "konami_id" BIGINT UNIQUE,
        "de_konami_id" BIGINT UNIQUE,
        "es_konami_id" BIGINT UNIQUE,
        "fr_konami_id" BIGINT UNIQUE,
        "it_konami_id" BIGINT UNIQUE,
        "ja_konami_id" BIGINT UNIQUE,
        "ko_konami_id" BIGINT UNIQUE,
        "pt_konami_id" BIGINT UNIQUE,
        "zhhans_konami_id" BIGINT UNIQUE,
        "zhhant_konami_id" BIGINT UNIQUE,
        "print_number_prefix" TEXT,
        "series" TEXT,
        "type" TEXT,
        "name" TEXT,
        "name_alt" TEXT,
        "setcode" TEXT,
        "setcode_alt" TEXT,
        "de_name" TEXT,
        "de_setcode" TEXT,
        "de_setcode_alt" TEXT,
        "es_name" TEXT,
        "es_setcode" TEXT,
        "es_setcode_alt" TEXT,
        "fr_name" TEXT,
        "fr_setcode" TEXT,
        "fr_setcode_alt" TEXT,
        "it_name" TEXT,
        "it_setcode" TEXT,
        "it_setcode_alt" TEXT,
        "ja_name" TEXT,
        "ja_setcode" TEXT,
        "ja_setcode_alt" TEXT,
        "ko_name" TEXT,
        "ko_setcode" TEXT,
        "ko_setcode_alt" TEXT,
        "pt_name" TEXT,
        "pt_setcode" TEXT,
        "pt_setcode_alt" TEXT,
        "zhhans_name" TEXT,
        "zhhans_setcode" TEXT,
        "zhhans_setcode_alt" TEXT,
        "zhhant_name" TEXT,
        "zhhant_setcode" TEXT,
        "zhhant_setcode_alt" TEXT,
        PRIMARY KEY ("set_id")
    );

CREATE UNIQUE INDEX "index.tbl_set.id" ON "tbl_set" ("set_id");

CREATE UNIQUE INDEX "index.tbl_set.ygojson_id" ON "tbl_set" ("ygojson_id");

CREATE UNIQUE INDEX "index.tbl_set.konami_id" ON "tbl_set" ("konami_id");

CREATE UNIQUE INDEX "index.tbl_set.yugipedia_pageid" ON "tbl_set" ("yugipedia_pageid");

CREATE INDEX "index.tbl_set.setcode" ON "tbl_set" ("setcode");