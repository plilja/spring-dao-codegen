-- Intentionally written a little quirky to provoke code generator

USE master

IF DB_ID('docker') IS NOT NULL
BEGIN
    SET NOEXEC ON; 
END

CREATE DATABASE docker

GO

CREATE LOGIN docker WITH PASSWORD = 'superSecure123'

GO

USE docker

CREATE USER docker FOR LOGIN docker WITH DEFAULT_SCHEMA = [DBO]

GO

ALTER ROLE db_owner ADD MEMBER docker; 

GO

CREATE TABLE baz_ms_sql (
    id INT identity(1, 1) primary key,
    name VARCHAR(30) NOT NULL,
    inserted_at datetime not null,
    modified_at datetime
)

CREATE TABLE one_column_generated_id_ms_sql (
    id INT identity(1, 1) primary key
)

CREATE TABLE ONE_COLUMN_NATURAL_ID_MS_SQL (
    id VARCHAR(10) primary key
)

CREATE TABLE DATA_TYPES_MS_SQL (
    id bigint identity(1, 1) primary key,
    bit bit,
    decimal_nine_zero decimal(9,0),
    decimal_ten_zero decimal(10,0),
    decimal_eighteen_zero decimal(18,0),
    decimal_nineteen_zero decimal(19,0),
    decimal_ten_two decimal(10,2),
    int int,
    money money,
    smallint smallint,
    smallmoney smallmoney,
    tinyint tinyint,
    float float,
    real real,
    date date,
    datetime2 datetime2,
    datetime datetime,
    time time,
    char char,
    char10 char(10),
    varchar10 varchar(10),
    text text,
    nchar10 nchar(10),
    nvarchar10 nvarchar(10),
    ntext ntext,
    binary10 binary(10),
    varbinary10 varbinary(10),
    xml xml
)

SET NOEXEC OFF; 
