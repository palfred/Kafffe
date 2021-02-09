package kafffe.bootstrap.form

import kafffe.core.*
import kafffe.messages.Messages
import org.w3c.dom.HTMLSelectElement
import kotlin.reflect.KProperty1

/**
 * Holds a HTML form element select
 */
abstract class Dropdown<T : Any>(val idInput: String, valueModel: Model<T>, choiceModel: Model<List<T>>) :
    KafffeComponentWithModel<T>(valueModel), FormInput {

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

    private lateinit var htmlSelect: HTMLSelectElement

    override fun KafffeHtmlBase.kafffeHtml() =
        select {
            withElement {
                htmlSelect = this
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
        model.data = valueFromString(htmlSelect.value)
    }

    override val htmlId: String get() = idInput
    override fun component(): KafffeComponent = this
    override fun validate(): Boolean = htmlSelect.checkValidity()
    override var validationMessageModel: Model<String> =
        Model.ofGet { if (required) Messages.get().validation_required else htmlSelect.validationMessage }
}

class DropdownString(idInput: String, valueModel: Model<String>, choiceModel: Model<List<String>>) :
    Dropdown<String>(idInput, valueModel, choiceModel) {

    override fun display(choice: String) = choice
    override fun valueToString(value: String) = value
    override fun valueFromString(strValue: String) = strValue
}

class DropdownInt(idInput: String, valueModel: Model<Int>, choiceModel: Model<List<Int>>) :
    Dropdown<Int>(idInput, valueModel, choiceModel) {

    override fun display(choice: Int) = choice.toString()
    override fun valueToString(value: Int) = value.toString()
    override fun valueFromString(strValue: String) = strValue.toInt()
}


// DSL function for form component consumer DSL
fun <T : Any> FormComponentConsumer<T>.dropdown(
    idInput: String,
    labelModel: Model<String>,
    valueModel: Model<String>,
    choiceModel: Model<List<String>>
): DropdownString {
    val inp = DropdownString(idInput, valueModel, choiceModel)
    decorateAndAdd(labelModel, inp)
    return inp
}

fun <T : Any> FormComponentConsumer<T>.dropdownNoFormGroup(
    idInput: String,
    valueModel: Model<String>,
    choiceModel: Model<List<String>>
): DropdownString {
    return DropdownString(idInput, valueModel, choiceModel).also { addChild(it) }
}

/**
 * Property based
 */
fun <T : Any> FormComponentConsumer<T>.dropdown(
    property: KProperty1<T, String>,
    choiceModel: Model<List<String>>
): DropdownString =
    dropdown(property.name, labelStrategy.label(property.name), model.property(property), choiceModel)

fun <T : Any> FormComponentConsumer<T>.dropdownNoFormGroup(
    property: KProperty1<T, String>,
    choiceModel: Model<List<String>>
): DropdownString {
    return DropdownString(property.name, model.property(property), choiceModel).also { addChild(it) }
}