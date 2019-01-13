CREATE SCHEMA test_schema
    AUTHORIZATION "docker";


CREATE TABLE public."FOO_POSTGRES"
(
    "FOO_ID" bigint NOT NULL,
    "BOOLEAN_BIT" bit(1) NOT NULL,
    "BOOLEAN_B" boolean NOT NULL,
    "CHAR" "char" NOT NULL,
    "DATE" date,
    "TIMESTAMP" timestamp with time zone,
    "BIGDECIMAL" numeric NOT NULL,
    "FLOAT" real,
    "DOUBLE" double precision,
    "GUID" uuid,
    "XML" xml,
    "TEXT" text COLLATE pg_catalog."default",
    "BYTEA" bytea,
    CONSTRAINT "Foo_pkey" PRIMARY KEY ("FOO_ID")
)
WITH (
    OIDS = FALSE
);
-- TABLESPACE pg_default;
-- 
-- ALTER TABLE public."FOO"
--     OWNER to "docker";

CREATE SEQUENCE test_schema."BAZ_BAZ_ID_seq";

CREATE TABLE test_schema."baz_postgres"
(
    "baz_id" integer NOT NULL DEFAULT nextval('test_schema."BAZ_BAZ_ID_seq"'::regclass),
    "baz_name" character varying(100) COLLATE pg_catalog."default",
    CONSTRAINT "baz_pkey" PRIMARY KEY ("baz_id")
)
WITH (
    OIDS = FALSE
);
-- TABLESPACE pg_default;
-- 
-- ALTER TABLE test_schema."BAZ"
--     OWNER to "docker";
COMMENT ON TABLE test_schema."baz_postgres"
    IS 'Baz comment';
