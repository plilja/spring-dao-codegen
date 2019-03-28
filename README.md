# Spring Dao class generator

Generate DAO classes from your database schema for your Spring project. 

If you want to have control over what SQL queries are run in your database,
then this project is for you. You might be hesitant to use a tool like JPA
for this reason. But you still want some of the benefits of JPA
like not having to write the boilerplate code and having consistent interfaces
for your repositories.

The generated code expects Spring framework and Spring JDBC as dependencies.
If you choose to use some of the opt in features some additional dependencies
might be required.

The intention is that the generated code should be readable and debuggable.
Once generated it should also be maintanable and extendable by you. You
can also choose to run the generator again if you change your schema.

# Features

* Generates DAO/Repositories with CRUD functionality
* Generates accompanying entities
* Supports Postgres, Oracle, SQL Server and MySQL
* Opt-in enum generation for tables with fixed content
* Opt-in JPA-like features LastModifiedBy, CreatedBy, LastModifiedDate, CreatedDate
* Opt-in Lombok for generated entities
* Opt-in query API (you can opt out and write the queries you need yourself)
* Opt-in Javax validation annotations
* Opt-in generation of a DDL file for a H2 database to be used in tests

# Limitations

* Tables with composite primary keys are not supported
* Columns with array data types are not supported
* Bulk/batch inserts are not supported

# Building

Checkout the project and run
```bash
mvn clean install -P '!oracle'
```
If you intend to use Oracle database you need to download and install the Oracle driver manually (they don't have their driver in a central maven repository). 

After having downloaded ojdbc8.jar from the Oracle homepage you should be able to run something like the following to install it.
```bash
mvn install:install-file -Dfile=~/Downloads/ojdbc8.jar -DgroupId=com.oracle -DartifactId=ojdbc8 -Dversion=12.2.0.1 -Dpackaging=jar 

mvn clean install
```

# How-to

See the [template configuration](https://github.com/plilja/spring-dao-codegen/blob/master/examples/template.properties) in the example folder how to configure this program.

After having built the project run this command to run the program:
```
java -jar target/daogenerator-version-spring-boot.jar your-config.properties
```

# Examples

REST API on top of a MySQL database:

https://github.com/plilja/springdaogen-example-sakila

# Features that might appear in a coming release

* SqlLite
* MariaDb
* Tables with composite id-columns
* Smart way to build and run all IT-tests
* Possibility to choose some formatting (for example lower case SQL vs upper case)
* Example projects (java8, java11)
* Customize lombok annotations?
* Kotlin support?
* More database patterns? (soft delete etc...)
* Merge into, would increase performance for inserts with natural key
* Bulk insert (looks like it will be hard to set generated keys after)
* Array data types
