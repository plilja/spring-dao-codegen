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
    version smallint,
    constraint baz_pkey primary key (baz_id)
);

