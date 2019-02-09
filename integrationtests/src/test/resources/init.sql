CREATE SCHEMA test_schema;


CREATE TABLE test_schema.color_enum_h2 (
name VARCHAR(100),
hex VARCHAR(100),
PRIMARY KEY(name));

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
color VARCHAR(100),
created_at TIMESTAMP,
version INTEGER,
PRIMARY KEY(baz_id));

ALTER TABLE test_schema.baz_h2
ADD FOREIGN KEY (color)
REFERENCES test_schema.color_enum_h2(name);

INSERT INTO test_schema.color_enum_h2(name, hex) VALUES('red', '#FF0000');
INSERT INTO test_schema.color_enum_h2(name, hex) VALUES('green', '#00FF00');
INSERT INTO test_schema.color_enum_h2(name, hex) VALUES('blue', '#0000FF');
