# Generate daos

 Work in progress

# TODO:s
* Write a proper readme
* blob.getBinaryStream().readAllBytes() doesn't work in java 8
* queryForOne ?
* Optimize nullVersions, can exclude clause if not nullable

# Maybe later release
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
* Views (how to handle abscence of pk? only read?)
* Array data types
