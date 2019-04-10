package se.plilja.springdaogen.engine.dao


import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import lombok.AllArgsConstructor
import lombok.Data
import lombok.NoArgsConstructor
import se.plilja.springdaogen.classgenerators.ClassGenerator
import se.plilja.springdaogen.config.Config
import se.plilja.springdaogen.engine.model.Left
import se.plilja.springdaogen.engine.model.View
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

fun generateViewEntity(config: Config, view: View): ClassGenerator {
    println("Generating entity for view '${view.name}', entity will be named '${view.entityName()}'.")
    val g = ClassGenerator(view.entityName(), config.entityOutputPackage, config.entityOutputFolder)
    if (config.featuresUseLombok) {
        g.addClassAnnotation("@Data")
        g.addClassAnnotation("@NoArgsConstructor")
        g.addClassAnnotation("@AllArgsConstructor")
        g.addImport(Data::class.java)
        g.addImport(AllArgsConstructor::class.java)
        g.addImport(NoArgsConstructor::class.java)
        g.hideConstructors = true
    }
    if (config.featureGenerateJacksonAnnotations) {
        g.addImport(JsonIgnoreProperties::class.java)
        g.addClassAnnotation("@JsonIgnoreProperties(ignoreUnknown = true)")
    }
    for (column in view.columns) {
        val type = column.type()
        when (type) {
            is Left -> g.addImport(type.value)
        }
        val annotations = ArrayList<String>()
        if (config.featureGenerateJavaxValidation) {
            if (!column.nullable) {
                g.addImport(NotNull::class.java)
                annotations.add("@NotNull")
            }
            if (column.type() == Left(String::class.java) && !column.isClobLike()) {
                if (column.size < Integer.MAX_VALUE) {
                    g.addImport(Size::class.java)
                    annotations.add("@Size(max = ${column.size})")
                }
            }
        }
        if (config.featuresUseLombok) {
            g.addPrivateField(column.fieldName(), column.typeName(), annotations)
        } else {
            g.addField(column.fieldName(), column.typeName(), annotations)
        }
    }

    return g
}

