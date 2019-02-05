CREATE SCHEMA test_schema;


CREATE TABLE test_schema.one_column_natural_id_h2 (
id VARCHAR(100),
PRIMARY KEY(id));

CREATE TABLE test_schema.one_column_generated_id_h2 (
id IDENTITY,
PRIMARY KEY(id));

CREATE TABLE test_schema.baz_h2 (
baz_id IDENTITY,
baz_name VARCHAR(100),
changed_at TIMESTAMP,
created_at TIMESTAMP,
version INTEGER,
PRIMARY KEY(baz_id));








