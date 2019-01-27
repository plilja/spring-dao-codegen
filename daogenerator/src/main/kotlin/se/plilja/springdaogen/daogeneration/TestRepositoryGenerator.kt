package se.plilja.springdaogen.daogeneration

import org.springframework.stereotype.Repository
import se.plilja.springdaogen.codegeneration.ClassGenerator
import se.plilja.springdaogen.model.Config
import se.plilja.springdaogen.model.Table
import se.plilja.springdaogen.sqlgeneration.update


fun generateTestRepository(config: Config, table: Table): ClassGenerator? {
    if (config.testRepositoryOutputPackage != null && config.testRepositoryOutputFolder != null) {
        val g = ClassGenerator(
            config.testRepositoryPrefix + table.repositoryName(), // TODO rewrite!
            config.testRepositoryOutputPackage,
            config.testRepositoryOutputFolder
        )
        if (!config.repositoriesAreAbstract) {
            g.addClassAnnotation("@Repository")
            g.addImport(Repository::class.java)
        }
        val updatesAreSupported = update(table, config.databaseDialect) != null
        g.addCustomConstructor(
            """
            public ${g.name}() {
                super(${table.primaryKey.generated}, ${config.maxSelectAllCount}, $updatesAreSupported);
            }
        """
        )
        g.implements = table.repositoryName()
        g.extends = "BaseTestRepository<${table.entityName()}, ${table.primaryKey.javaType.simpleName}>"
        if (config.repositoryOutputPackage != config.testRepositoryOutputPackage) {
            g.addImport("${config.repositoryOutputPackage}.${table.repositoryName()}")
        }
        if (config.entityOutputPackage != config.testRepositoryOutputPackage) {
            g.addImport("${config.entityOutputPackage}.${table.entityName()}")
        }
        return g
    } else {
        return null
    }
}
