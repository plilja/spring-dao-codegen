-- Intentionally written a little quirky to provoke code generator

CREATE SCHEMA test_schema
    AUTHORIZATION "docker";

CREATE SEQUENCE test_schema."BAZ_BAZ_ID_seq";

CREATE TABLE test_schema."baz_postgres"
(
    "baz_id" integer NOT NULL DEFAULT nextval('test_schema."BAZ_BAZ_ID_seq"'::regclass),
    "baz_name" character varying(100) COLLATE pg_catalog."default",
    "created_at" timestamp with time zone not null,
    "changed_at" timestamp with time zone,
    "counter" bigint not null,
    CONSTRAINT "baz_pkey" PRIMARY KEY ("baz_id")
);

CREATE SEQUENCE test_schema."ONE_COL_seq";

CREATE TABLE test_schema."one_column_generated_id_postgres"
(
    "id" integer NOT NULL DEFAULT nextval('test_schema."ONE_COL_seq"'::regclass),
    CONSTRAINT "one_col_pkey" PRIMARY KEY ("id")
);

CREATE TABLE test_schema."one_column_natural_id_postgres"
(
    "id" character varying(10) NOT NULL,
    CONSTRAINT "one_col_natural_id_pkey" PRIMARY KEY ("id")
);

CREATE TABLE public.DATA_TYPES_POSTGRES
(
    "id" bigserial NOT NULL,
    "boolean_b" boolean,
    "char" "char",
    "date" date,
    "timestamp" timestamp with time zone,
    "decimal_nine_zero" decimal(9,0),
    "decimal_ten_zero" decimal(10,0),
    "decimal_eighteen_zero" decimal(18,0),
    "decimal_nineteen_zero" decimal(19,0),
    "decimal_ten_two" decimal(10,2),
    "numeric_ten_two" numeric(10,2),
    "smallint" smallint,
    "integer" integer,
    "bigint" bigint,
    "float" real,
    "double" double precision,
    "guid" uuid,
    "xml" xml,
    "varchar10" varchar(10),
    "char10" char(10),
    "text" text COLLATE pg_catalog."default",
    "bytea" bytea,
    CONSTRAINT "Foo_pkey" PRIMARY KEY (id)
);

