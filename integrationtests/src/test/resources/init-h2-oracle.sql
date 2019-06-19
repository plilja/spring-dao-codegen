CREATE SCHEMA IF NOT EXISTS DOCKER;


CREATE TABLE IF NOT EXISTS DOCKER.COLOR_ENUM_ORACLE (
NAME VARCHAR(9) NOT NULL,
PRIMARY KEY(NAME));

CREATE TABLE IF NOT EXISTS DOCKER.DATA_TYPES_ORACLE (
ID VARCHAR(100) NOT NULL,
BINARY_DOUBLE DOUBLE NOT NULL,
BINARY_FLOAT REAL NOT NULL,
BLOB BINARY(4000) NOT NULL,
CHAR1 VARCHAR(1) NOT NULL,
CHAR10 VARCHAR(10) NOT NULL,
CLOB CLOB NOT NULL,
DATE DATE NOT NULL,
NLOB CLOB NOT NULL,
NUMBER_EIGHTEEN_ZERO BIGINT NOT NULL,
NUMBER_NINE_ZERO INTEGER NOT NULL,
NUMBER_NINETEEN_ZERO DECIMAL(19, 0) NOT NULL,
NUMBER_TEN_TWO DECIMAL(10, 2) NOT NULL,
NUMBER_TEN_ZERO BIGINT NOT NULL,
TIMESTAMP TIMESTAMP NOT NULL,
TIMESTAMP_TZ TIMESTAMP WITH TIME ZONE NOT NULL,
VARCHAR VARCHAR(100) NOT NULL,
VARCHAR2 VARCHAR(100) NOT NULL,
PRIMARY KEY(ID));

CREATE TABLE IF NOT EXISTS DOCKER.ONE_COLUMN_GENERATED_ID_ORACLE (
ID IDENTITY NOT NULL,
PRIMARY KEY(ID));

CREATE TABLE IF NOT EXISTS DOCKER.ONE_COLUMN_NATURAL_ID_ORACLE (
ID VARCHAR(9) NOT NULL,
PRIMARY KEY(ID));

CREATE TABLE IF NOT EXISTS DOCKER.BAZ_ORACLE (
ID IDENTITY NOT NULL,
CHANGED_AT TIMESTAMP,
CHANGED_BY VARCHAR(50),
COLOR VARCHAR(9),
CREATED_AT TIMESTAMP NOT NULL,
CREATED_BY VARCHAR(50) NOT NULL,
NAME VARCHAR(30) NOT NULL,
VERSION INTEGER,
PRIMARY KEY(ID));

ALTER TABLE DOCKER.BAZ_ORACLE
ADD FOREIGN KEY (COLOR)
REFERENCES DOCKER.COLOR_ENUM_ORACLE(NAME);

MERGE INTO DOCKER.COLOR_ENUM_ORACLE(NAME) VALUES('BLUE');
MERGE INTO DOCKER.COLOR_ENUM_ORACLE(NAME) VALUES('GREEN');
MERGE INTO DOCKER.COLOR_ENUM_ORACLE(NAME) VALUES('RED');
