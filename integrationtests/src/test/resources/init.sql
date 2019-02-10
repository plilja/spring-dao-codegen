CREATE SCHEMA test_schema;


CREATE TABLE test_schema.color_enum_h2 (
name VARCHAR(100) NOT NULL,
hex VARCHAR(100) NOT NULL,
PRIMARY KEY(name));

CREATE TABLE test_schema.one_column_natural_id_h2 (
id VARCHAR(100) NOT NULL,
PRIMARY KEY(id));

CREATE TABLE test_schema.one_column_generated_id_h2 (
id IDENTITY NOT NULL,
PRIMARY KEY(id));

CREATE TABLE test_schema.baz_h2 (
baz_id IDENTITY NOT NULL,
baz_name VARCHAR(100),
changed_at TIMESTAMP,
color VARCHAR(100),
created_at TIMESTAMP NOT NULL,
version INTEGER NOT NULL,
PRIMARY KEY(baz_id));

ALTER TABLE test_schema.baz_h2
ADD FOREIGN KEY (color)
REFERENCES test_schema.color_enum_h2(name);

INSERT INTO test_schema.color_enum_h2(name, hex) VALUES('red', '#FF0000');
INSERT INTO test_schema.color_enum_h2(name, hex) VALUES('green', '#00FF00');
INSERT INTO test_schema.color_enum_h2(name, hex) VALUES('blue', '#0000FF');
