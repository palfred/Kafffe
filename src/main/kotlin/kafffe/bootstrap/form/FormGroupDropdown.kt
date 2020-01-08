package kafffe.bootstrap.form

import kafffe.core.KafffeHtml
import kafffe.core.Model
import kafffe.core.property
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLSelectElement
import kotlin.reflect.KProperty1

/**
 * Holds a HTML form element select
 */
abstract class FormGroupDropdown<T : Any>(idInput: String, labelModel: Model<String>, valueModel: Model<T>, choiceModel: Model<List<T>>)
    : FormGroup<HTMLSelectElement, T>(idInput, labelModel, valueModel) {
    var useCustom: Boolean = true
    var choicesModel by rerenderOnChange(choiceModel)

    override fun attach() {
        super.attach()
        choicesModel.listeners.add(onModelChanged)
    }

    override fun detach() {
        choicesModel.listeners.remove(onModelChanged)
        super.detach()
    }

    override fun KafffeHtml<HTMLDivElement>.createInputElement(): HTMLSelectElement {
        return select {
            withElement {
                addClass(if (useCustom) "custom-select" else "form-control")
                id = idInput
                value = valueToString(valueModel.data)
                required = this@FormGroupDropdown.required
                disabled = this@FormGroupDropdown.readOnly
            }
            for (c in choicesModel.data) {
                option {
                    withElement {
                        value = valueToString(c)
                        selected = (c == valueModel.data)
                    }
                    text(display(c))
                }
            }
        }.element!!
    }

    abstract fun display(choice: T): String

}

class FormGroupDropdownString(idInput: String, labelModel: Model<String>, valueModel: Model<String>, choiceModel: Model<List<String>>) :
        FormGroupDropdown<String>(idInput, labelModel, valueModel, choiceModel) {

    override fun display(choice: String) = choice
    override fun valueToString(value: String) = value
    override fun valueFromString(strValue: String) = strValue
}

// DSL function for form component consumer DSL
fun <T : Any, F : Any> FormComponentConsumer<T, F>.dropdown(idInput: String, labelModel: Model<String>, valueModel: Model<String>, choiceModel: Model<List<String>>): FormGroupDropdownString {
    return FormGroupDropdownString(idInput, labelModel, valueModel, choiceModel).also { addChild(it) }
}

/**
 * Property based
 */
fun <T : Any, F : Any> FormComponentConsumer<T, F>.dropdown(property: KProperty1<T, String>, choiceModel: Model<List<String>>): FormGroupDropdownString {
    return FormGroupDropdownString(property.name, labelStrategy.label(property.name), model.property(property), choiceModel).also { addChild(it) }
}