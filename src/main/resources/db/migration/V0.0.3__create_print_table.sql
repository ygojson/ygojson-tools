CREATE TABLE
    IF NOT EXISTS "tbl_print" (
        "print_id" VARCHAR(36) UNIQUE,
        "ygojson_id" VARCHAR(36) UNIQUE,
        "set_id" VARCHAR(36),
        "card_id" VARCHAR(36),
        "print_code" TEXT,
        "setcode" TEXT,
        "print_number_prefix" TEXT,
        "print_number" TEXT,
        "print_number_suffix" TEXT,
        "rarity" TEXT,
        "language" TEXT,
        "region_code" TEXT,
        -- TODO: decide the cascading mode
        FOREIGN KEY ("card_id") REFERENCES "tbl_card" ("card_id"),
        FOREIGN KEY ("set_id") REFERENCES "tbl_set" ("set_id"),
        PRIMARY KEY ("print_id")
    );

CREATE UNIQUE INDEX "index.tbl_print.id" ON "tbl_print" ("print_id");

CREATE UNIQUE INDEX "index.tbl_print.ygojson_id" ON "tbl_print" ("ygojson_id");

CREATE INDEX "index.tbl_print.print_code" ON "tbl_print" ("print_code");

CREATE INDEX "index.tbl_print.setcode" ON "tbl_print" ("setcode");