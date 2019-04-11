CREATE SCHEMA IF NOT EXISTS dbo;


CREATE TABLE IF NOT EXISTS dbo.color_enum_ms_sql (
name VARCHAR(30) NOT NULL,
PRIMARY KEY(name));

CREATE TABLE IF NOT EXISTS dbo.DATA_TYPES_MS_SQL (
id IDENTITY NOT NULL,
binary10 BINARY(10),
bit BOOLEAN,
char VARCHAR(1),
char10 VARCHAR(10),
date DATE,
datetime TIMESTAMP,
datetime2 TIMESTAMP,
decimal_eighteen_zero BIGINT,
decimal_nine_zero INTEGER,
decimal_nineteen_zero DECIMAL(19, 0),
decimal_ten_two DECIMAL(10, 2),
decimal_ten_zero BIGINT,
float REAL,
int INTEGER,
money DECIMAL(19, 4),
nchar10 VARCHAR(10),
ntext VARCHAR(1073741823),
nvarchar10 VARCHAR(10),
real REAL,
smallint INTEGER,
smallmoney DECIMAL(10, 4),
text CLOB,
time TIME,
tinyint INTEGER,
varbinary10 BINARY(10),
varchar10 VARCHAR(10),
xml CLOB,
PRIMARY KEY(id));

CREATE TABLE IF NOT EXISTS dbo.one_column_generated_id_ms_sql (
id IDENTITY NOT NULL,
PRIMARY KEY(id));

CREATE TABLE IF NOT EXISTS dbo.ONE_COLUMN_NATURAL_ID_MS_SQL (
id VARCHAR(10) NOT NULL,
PRIMARY KEY(id));

CREATE TABLE IF NOT EXISTS dbo.baz_ms_sql (
id IDENTITY NOT NULL,
color VARCHAR(30),
inserted_at TIMESTAMP NOT NULL,
inserted_by VARCHAR(50) NOT NULL,
modified_at TIMESTAMP,
modified_by VARCHAR(50),
name VARCHAR(30) NOT NULL,
version INTEGER,
PRIMARY KEY(id));

ALTER TABLE dbo.baz_ms_sql
ADD FOREIGN KEY (color)
REFERENCES dbo.color_enum_ms_sql(name);

MERGE INTO dbo.color_enum_ms_sql(name) VALUES('blue');
MERGE INTO dbo.color_enum_ms_sql(name) VALUES('green');
MERGE INTO dbo.color_enum_ms_sql(name) VALUES('red');
