#!/bin/bash
rm $(dirname $0)/integrationtests/src/test/java/dbtests/framework/*
find -E $(dirname $0)/integrationtests/src/test/java/dbtests -regex ".*Repository.java|.*Entity.java|.*Repo.java|.*Dao.java" | xargs rm
java -jar $(dirname $0)/daogenerator/target/daogenerator-0.1-spring-boot.jar postgres-test/postgres.properties
java -jar $(dirname $0)/daogenerator/target/daogenerator-0.1-spring-boot.jar mysql-test/mysql.properties
java -jar $(dirname $0)/daogenerator/target/daogenerator-0.1-spring-boot.jar mssql-test/mssql.properties
java -jar $(dirname $0)/daogenerator/target/daogenerator-0.1-spring-boot.jar oracle-test/oracle.properties
java -jar $(dirname $0)/daogenerator/target/daogenerator-0.1-spring-boot.jar h2-test/h2.properties
