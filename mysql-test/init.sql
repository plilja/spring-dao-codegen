-- Intentionally written a little quirky to provoke code generator

CREATE TABLE BazMysql (
    id INT(6) UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(30) NOT NULL
);

CREATE TABLE ONE_COLUMN_MYSQL (
    id INT(6) UNSIGNED AUTO_INCREMENT PRIMARY KEY
);
