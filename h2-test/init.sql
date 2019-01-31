create schema test_schema
    authorization "docker";

create sequence test_schema.baz_baz_id_seq;

create table test_schema.baz_h2
(
    baz_id integer not null default nextval('test_schema."baz_baz_id_seq"'::regclass),
    baz_name character varying(100) collate pg_catalog.default,
    constraint baz_pkey primary key (baz_id)
);

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
