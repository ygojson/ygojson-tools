CREATE TABLE
    IF NOT EXISTS "tbl_set" (
        "set_id" VARCHAR(36) UNIQUE NOT NULL,
        "ygojson_id" VARCHAR(36) UNIQUE,
        "print_number_prefix" VARCHAR(255),
        "series" VARCHAR(255),
        "type" VARCHAR(255),
        "name" VARCHAR(255),
        "name_alt" VARCHAR(255),
        "setcode" VARCHAR(255),
        "setcode_alt" VARCHAR(255),
        "de_name" VARCHAR(255),
        "de_setcode" VARCHAR(255),
        "de_setcode_alt" VARCHAR(255),
        "es_name" VARCHAR(255),
        "es_setcode" VARCHAR(255),
        "es_setcode_alt" VARCHAR(255),
        "fr_name" VARCHAR(255),
        "fr_setcode" VARCHAR(255),
        "fr_setcode_alt" VARCHAR(255),
        "it_name" VARCHAR(255),
        "it_setcode" VARCHAR(255),
        "it_setcode_alt" VARCHAR(255),
        "ja_name" VARCHAR(255),
        "ja_setcode" VARCHAR(255),
        "ja_setcode_alt" VARCHAR(255),
        "ko_name" VARCHAR(255),
        "ko_setcode" VARCHAR(255),
        "ko_setcode_alt" VARCHAR(255),
        "pt_name" VARCHAR(255),
        "pt_setcode" VARCHAR(255),
        "pt_setcode_alt" VARCHAR(255),
        "zhhans_name" VARCHAR(255),
        "zhhans_setcode" VARCHAR(255),
        "zhhans_setcode_alt" VARCHAR(255),
        "zhhant_name" VARCHAR(255),
        "zhhant_setcode" VARCHAR(255),
        "zhhant_setcode_alt" VARCHAR(255),
        PRIMARY KEY ("set_id")
    );

CREATE UNIQUE INDEX "index.tbl_set.id" ON "tbl_set" ("set_id");

CREATE UNIQUE INDEX "index.tbl_set.ygojson_id" ON "tbl_set" ("ygojson_id");

CREATE INDEX "index.tbl_set.setcode" ON "tbl_set" ("setcode");