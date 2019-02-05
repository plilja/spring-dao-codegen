-- Intentionally written a little quirky to provoke code generator

CREATE TABLE BazMysql (
    id INT(6) UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(30) NOT NULL,
    created_at DATETIME NOT NULL,
    changed_at DATETIME,
    version tinyint not null
);

CREATE TABLE ONE_COLUMN_GENERATED_ID_MYSQL (
    id INT(6) UNSIGNED AUTO_INCREMENT PRIMARY KEY
);

CREATE TABLE ONE_COLUMN_NATURAL_ID_MYSQL (
    id VARCHAR(10) PRIMARY KEY
);

CREATE TABLE DATA_TYPES_MYSQL (
    id SERIAL PRIMARY KEY,
    `bit` BIT,
    `tinyint` TINYINT,
    `bool` BOOL,
    `smallint` SMALLINT,
    `mediumint` MEDIUMINT,
    `int` INT,
    `integer` INTEGER,
    `bigint` BIGINT,
    decimal_nine_zero decimal(9,0),
    decimal_ten_zero decimal(10,0),
    decimal_eighteen_zero decimal(18,0),
    decimal_nineteen_zero decimal(19,0),
    decimal_ten_two decimal(10,2),
    `float` FLOAT,
    `double` DOUBLE,
    `date` DATE,
    `datetime` DATETIME,
    `timestamp` TIMESTAMP,
    `time` TIME,
    `year` YEAR,
    `blob` BLOB,
    varchar_10 VARCHAR(10),
    varchar_binary_10 VARCHAR(10) CHARACTER SET binary,
    `tinyblob` TINYBLOB,
    `text` TEXT,
    `json` JSON
);
