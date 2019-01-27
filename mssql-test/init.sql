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
    name VARCHAR(30) NOT NULL
)

CREATE TABLE one_column_generated_id_ms_sql (
    id INT identity(1, 1) primary key
)

CREATE TABLE ONE_COLUMN_NATURAL_ID_MS_SQL (
    id VARCHAR(10) primary key
)

SET NOEXEC OFF; 
