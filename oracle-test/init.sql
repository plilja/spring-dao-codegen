CREATE USER DOCKER
    IDENTIFIED BY password
    QUOTA 50M ON SYSTEM;

GRANT create session TO DOCKER;
GRANT create table TO DOCKER;
GRANT create view TO DOCKER;
GRANT create any trigger TO DOCKER;
GRANT create any procedure TO DOCKER;
GRANT create sequence TO DOCKER;
GRANT create synonym TO DOCKER;

CREATE SEQUENCE DOCKER.BAZ_SEQ START WITH 1 INCREMENT BY 1;

CREATE TABLE DOCKER.BAZ_ORACLE (
    id NUMBER(10) NOT NULL,
    name VARCHAR(30) NOT NULL,
    CONSTRAINT BAZ_ORACLE_PK PRIMARY KEY (id)
);

-- No identity functionality in Oracle 11c. And no 
-- Express edition of Oracle 12c on Docker hub. 
-- Hence this trigger.
CREATE OR REPLACE TRIGGER DOCKER.baz_trg 
BEFORE INSERT ON DOCKER.BAZ_ORACLE 
FOR EACH ROW
WHEN (new.id IS NULL)
BEGIN
    :new.id := DOCKER.BAZ_SEQ.NEXTVAL;
END;
/

