package kafffe.bootstrap.form

import kafffe.core.*
import kafffe.messages.Messages
import org.w3c.dom.HTMLInputElement
import kotlinx.dom.addClass

/**
 * Holds a HTML form INPUT element
 */
abstract class Input<T : Any>(var idInput: String, valueModel: Model<T>) : KafffeComponentWithModel<T>(valueModel), FormInput {
    override val htmlId: String
        get() = idInput

    override var validationMessageModel: Model<String> = Model.ofGet{ if (required) Messages.get().validation_required else htmlInput.validationMessage}
    override fun validate(): Boolean {
        val valid = htmlInput.checkValidity()
        htmlInput.applyInputValidCssClasses(valid)
        return valid
    }

    var validationPattern: String by rerenderOnChange("")
    var inputType: String by rerenderOnChange("text")
    var required: Boolean by rerenderOnChange(false)
    var readOnly: Boolean by rerenderOnChange(false)

    abstract fun valueToString(value: T): String
    abstract fun valueFromString(strValue: String): T

    protected lateinit var htmlInput: HTMLInputElement

    override fun KafffeHtmlBase.kafffeHtml(): KafffeHtmlOut =
        input {
            withElement {
                htmlInput = this
                id = idInput
                type = inputType
                value = valueToString(model.data)
                addClass("form-control")
                required = this@Input.required
                readOnly = this@Input.readOnly
                if (validationPattern.isNotEmpty()) {
                    pattern = validationPattern
                }
            }
        }

    override fun updateValueModel() {
        model.data = valueFromString(htmlInput.value)
    }

    override fun component(): KafffeComponent = this
}

open class InputString(idInput: String, valueModel: Model<String>) : Input<String>(idInput, valueModel) {
    override fun valueToString(value: String): String = value
    override fun valueFromString(strValue: String): String = strValue
}

open class InputInt(idInput: String, valueModel: Model<Int>) : Input<Int>(idInput, valueModel) {
    var minimum: Int = 0
    var maximum: Int = Int.MAX_VALUE

    init {
        inputType = "number"
        modifiers.add(AttributeSetModifier("min", FunctionalModel({ minimum.toString() })))
        modifiers.add(AttributeSetModifier("max", FunctionalModel({ maximum.toString() })))
    }

    override fun valueToString(value: Int): String = value.toString()
    override fun valueFromString(strValue: String): Int = strValue.toInt()
}