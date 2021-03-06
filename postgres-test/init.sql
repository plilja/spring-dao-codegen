-- Intentionally written a little quirky to provoke code generator

CREATE SCHEMA test_schema
    AUTHORIZATION "docker";

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
    "timestamp" timestamp without time zone,
    "timestamp_tz" timestamp with time zone,
    "time" time without time zone,
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

CREATE TABLE test_schema.color_enum_postgres
(
    id character varying(10) NOT NULL,
    hex character varying(10) NOT NULL,
    CONSTRAINT color_enum_id_pkey PRIMARY KEY (id)
);

insert into test_schema.color_enum_postgres (id, hex) values ('red', '#FF0000');
insert into test_schema.color_enum_postgres (id, hex) values ('green', '#00FF00');
insert into test_schema.color_enum_postgres (id, hex) values ('blue', '#0000FF');

CREATE SEQUENCE test_schema."BAZ_BAZ_ID_seq";

CREATE TABLE test_schema."baz_postgres"
(
    "baz_id" integer NOT NULL DEFAULT nextval('test_schema."BAZ_BAZ_ID_seq"'::regclass),
    "baz_name" character varying(100) COLLATE pg_catalog."default",
    "color" character varying(10) REFERENCES test_schema.color_enum_postgres(id),
    "created_at" timestamp without time zone not null,
    "changed_at" timestamp without time zone,
    "created_by" character varying(50) not null,
    "changed_by" character varying(50),
    "counter" bigint,
    CONSTRAINT "baz_pkey" PRIMARY KEY ("baz_id")
);

CREATE TABLE test_schema.excluded_postgres
(
    excluded_id serial NOT NULL,
    name character varying(100),
    CONSTRAINT "excluded_pkey" PRIMARY KEY (excluded_id)
);


CREATE VIEW test_schema.baz_view_postgres AS
SELECT 
baz_id,
baz_name as "name with space",
color 
FROM test_schema.baz_postgres;

