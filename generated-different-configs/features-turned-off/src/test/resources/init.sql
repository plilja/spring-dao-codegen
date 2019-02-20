CREATE SCHEMA IF NOT EXISTS test_schema;


CREATE TABLE IF NOT EXISTS test_schema.color_enum_h2 (
name VARCHAR(10) NOT NULL,
hex VARCHAR(10) NOT NULL,
PRIMARY KEY(name));

CREATE TABLE IF NOT EXISTS test_schema.one_column_natural_id_h2 (
id VARCHAR(10) NOT NULL,
PRIMARY KEY(id));

CREATE TABLE IF NOT EXISTS test_schema.one_column_generated_id_h2 (
id IDENTITY NOT NULL,
PRIMARY KEY(id));

CREATE TABLE IF NOT EXISTS public.data_types_h2 (
id IDENTITY NOT NULL,
bigint BIGINT,
boolean_b BOOLEAN,
char VARCHAR(1),
char10 VARCHAR(10),
date DATE,
decimal_eighteen_zero BIGINT,
decimal_nine_zero INTEGER,
decimal_nineteen_zero DECIMAL(19, 0),
decimal_ten_two DECIMAL(10, 2),
decimal_ten_zero BIGINT,
double DOUBLE,
float REAL,
guid UUID,
integer INTEGER,
numeric_ten_two DECIMAL(10, 2),
smallint INTEGER,
text CLOB,
time TIME,
timestamp TIMESTAMP,
timestamp_tz TIMESTAMP WITH TIME ZONE,
varchar10 VARCHAR(10),
PRIMARY KEY(id));

CREATE TABLE IF NOT EXISTS test_schema.baz_h2 (
baz_id IDENTITY NOT NULL,
baz_name VARCHAR(100),
changed_at TIMESTAMP,
changed_by VARCHAR(50),
color VARCHAR(10),
created_at TIMESTAMP NOT NULL,
created_by VARCHAR(50) NOT NULL,
version INTEGER,
PRIMARY KEY(baz_id));

ALTER TABLE test_schema.baz_h2
ADD FOREIGN KEY (color)
REFERENCES test_schema.color_enum_h2(name);
