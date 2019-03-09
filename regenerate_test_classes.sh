#!/bin/bash
rm $(dirname $0)/integrationtests/src/test/java/dbtests/framework/*
rm $(dirname $0)/generated-different-configs/features-turned-off/src/test/java/db/h2/*
rm $(dirname $0)/generated-different-configs/features-turned-off/src/test/resources/*
find -E $(dirname $0)/integrationtests/src/test/java/dbtests -regex ".*Repository.java|.*Entity.java|.*Repo.java|.*Dao.java" | xargs rm
java -jar $(dirname $0)/daogenerator/target/daogenerator-0.9.0-spring-boot.jar postgres-test/postgres.properties
java -jar $(dirname $0)/daogenerator/target/daogenerator-0.9.0-spring-boot.jar mysql-test/mysql.properties
java -jar $(dirname $0)/daogenerator/target/daogenerator-0.9.0-spring-boot.jar mssql-test/mssql.properties
java -jar $(dirname $0)/daogenerator/target/daogenerator-0.9.0-spring-boot.jar oracle-test/oracle.properties
java -jar $(dirname $0)/daogenerator/target/daogenerator-0.9.0-spring-boot.jar generated-different-configs/features-turned-off/db.properties
