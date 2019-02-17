create schema test_schema
    authorization "docker";

create sequence test_schema.one_col_seq;

create table test_schema.one_column_generated_id_h2
(
    id integer not null default nextval('test_schema."one_col_seq"'::regclass),
    constraint one_col_pkey primary key (id)
);

create table test_schema.one_column_natural_id_h2
(
    id character varying(10) not null,
    constraint one_col_natural_id_pkey primary key (id)
);

CREATE TABLE public.DATA_TYPES_H2
(
    "id" bigserial NOT NULL,
    "boolean_b" boolean,
    "char" "char",
    "date" date,
    "time" time,
    "timestamp" timestamp without time zone,
    "timestamp_tz" timestamp with time zone,
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
    "varchar10" varchar(10),
    "char10" char(10),
    "text" text COLLATE pg_catalog."default",
    CONSTRAINT "Foo_pkey" PRIMARY KEY (id)
);

create table test_schema.color_enum_h2
(
    name character varying(10) NOT NULL,
    hex character varying(10) NOT NULL,
    constraint color_enum_id_pkey primary key (name)
);

insert into test_schema.color_enum_h2 (name, hex) values ('red', '#FF0000');
insert into test_schema.color_enum_h2 (name, hex) values ('green', '#00FF00');
insert into test_schema.color_enum_h2 (name, hex) values ('blue', '#0000FF');

create sequence test_schema.baz_baz_id_seq;

create table test_schema.baz_h2
(
    baz_id integer not null default nextval('test_schema."baz_baz_id_seq"'::regclass),
    baz_name character varying(100) collate pg_catalog.default,
    color character varying(10) references test_schema.color_enum_h2(name),
    created_at timestamp not null,
    changed_at timestamp,
    created_by varchar(50) not null,
    changed_by varchar(50),
    version smallint,
    constraint baz_pkey primary key (baz_id)
);

