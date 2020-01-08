package kafffe.bootstrap.form

import kafffe.core.KafffeComponentWithModel
import kafffe.core.KafffeHtmlBase
import kafffe.core.Model
import kafffe.core.property
import kotlin.reflect.KProperty1

/**
 * Holds a HTML form element select
 */
abstract class Dropdown<T : Any>(val idInput: String, valueModel: Model<T>, choiceModel: Model<List<T>>)
    : KafffeComponentWithModel<T>(valueModel), FormValueProvider {

    var required: Boolean by rerenderOnChange(false)
    var readOnly: Boolean by rerenderOnChange(false)
    var useCustom: Boolean = false
    var choicesModel by rerenderOnChange(choiceModel)

    override fun attach() {
        super.attach()
        choicesModel.listeners.add(onModelChanged)
    }

    override fun detach() {
        choicesModel.listeners.remove(onModelChanged)
        super.detach()
    }

    abstract fun display(choice: T): String
    abstract fun valueFromString(strValue: String): T
    abstract fun valueToString(value: T): String

    override fun KafffeHtmlBase.kafffeHtml() =
            select {
                withElement {
                    addClass(if (useCustom) "custom-select" else "form-control")
                    id = idInput
                    value = valueToString(this@Dropdown.model.data)
                    required = this@Dropdown.required
                    disabled = this@Dropdown.readOnly
                }
                for (c in choicesModel.data) {
                    option {
                        withElement {
                            value = valueToString(c)
                            selected = (c == this@Dropdown.model.data)
                        }
                        text(display(c))
                    }
                }
            }

    override fun updateValueModel() {
        html.let {
            model.data = valueFromString(it.asDynamic()["value"] as String)
        }
    }
}

class DropdownString(idInput: String, valueModel: Model<String>, choiceModel: Model<List<String>>) :
        Dropdown<String>(idInput, valueModel, choiceModel) {

    override fun display(choice: String) = choice
    override fun valueToString(value: String) = value
    override fun valueFromString(strValue: String) = strValue
}

// DSL function for form component consumer DSL
fun <T : Any, F : Any> FormComponentConsumer<T, F>.dropdownNoFormGroup(idInput: String, valueModel: Model<String>, choiceModel: Model<List<String>>): DropdownString {
    return DropdownString(idInput, valueModel, choiceModel).also { addChild(it) }
}

/**
 * Property based
 */
fun <T : Any, F : Any> FormComponentConsumer<T, F>.dropdownNoFormGroup(property: KProperty1<T, String>, choiceModel: Model<List<String>>): DropdownString {
    return DropdownString(property.name, model.property(property), choiceModel).also { addChild(it) }
}