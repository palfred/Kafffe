package kafffe.bootstrap.form

import kafffe.core.Model
import kafffe.core.property
import kotlin.reflect.KProperty1

// DSL function for form component consumer DSL
fun <T : Any, F : Any> FormComponentConsumer<T, F>.checkbox(
    idInput: String,
    valueModel: Model<Boolean>,
    labelModel: Model<String>
): Checkbox {
    return Checkbox(idInput, valueModel, labelModel).also { addChild(it) }
}

/**
 * Property based
 */
fun <T : Any, F : Any> FormComponentConsumer<T, F>.checkbox(property: KProperty1<T, Boolean>): Checkbox {
    return checkbox(property.name, model.property(property), labelStrategy.label(property.name))
}