package kafffe.bootstrap.form

import kafffe.core.AttributeSetModifier
import kafffe.core.FunctionalModel
import kafffe.core.KafffeHtml
import kafffe.core.Model
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLInputElement

/**
 * Holds a HTML form element (input, select or similar) with the corresponding lable, error message, help message, ...
 * "firstName" -> leads to id "firstName", a lookup for label in messages using a key based on "firstName" and
 * use a property submodel of the surrounding form model for propperty "firstname"
 */
abstract class FormGroupInput<T : Any>(idInput: String, labelModel: Model<String>, valueModel: Model<T>)
    : FormGroup<HTMLInputElement, T>(idInput, labelModel, valueModel) {

    var validationPattern: String by rerenderOnChange("")
    var inputType: String by rerenderOnChange("text")

    override fun KafffeHtml<HTMLDivElement>.createInputElement(): HTMLInputElement {
        return input {
            withElement {
                id = idInput
                type = inputType
                value = valueToString(valueModel.data)
                addClass("form-control")
                required = this@FormGroupInput.required
                readOnly = this@FormGroupInput.readOnly
                if (validationPattern.isNotEmpty()) {
                    pattern = validationPattern
                }
            }
        }.element!!
    }

}

class FormGroupInputString(idInput: String, labelModel: Model<String>, valueModel: Model<String>) :
        FormGroupInput<String>(idInput, labelModel, valueModel) {
    override fun valueToString(value: String): String = value
    override fun valueFromString(strValue: String): String = strValue
}

class FormGroupInputInt(idInput: String, labelModel: Model<String>, valueModel: Model<Int>) :
        FormGroupInput<Int>(idInput, labelModel, valueModel) {
    var minimum: Int = 0
    var maximum: Int = Int.MAX_VALUE

    init {
        inputType = "number"
        modifiersInput.add(AttributeSetModifier("min", FunctionalModel({ minimum.toString() })))
        modifiersInput.add(AttributeSetModifier("max", FunctionalModel({ maximum.toString() })))
    }

    override fun valueToString(value: Int): String = value.toString()
    override fun valueFromString(strValue: String): Int = strValue.toInt()
}