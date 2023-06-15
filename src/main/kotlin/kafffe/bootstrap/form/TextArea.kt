package kafffe.bootstrap.form

import kafffe.core.*
import kotlinx.dom.addClass
import org.w3c.dom.HTMLTextAreaElement
import kotlin.reflect.KProperty1

/**
 * Holds a HTML form element (input, select or similar) with the corresponding lable, error message, help message, ...
 * "firstName" -> leads to id "firstName", a lookup for label in messages using a key based on "firstName" and
 * use a property submodel of the surrounding form model for property "firstname"
 */
class TextArea(override val htmlId: String, valueModel: Model<String>) : KafffeComponentWithModel<String>(valueModel),
    FormInput {

    var required: Boolean by rerenderOnChange(false)
    var readOnly: Boolean by rerenderOnChange(false)
    var lines: Int by rerenderOnChange(0)

    private lateinit var htmlInput: HTMLTextAreaElement
    override fun KafffeHtmlBase.kafffeHtml(): KafffeHtmlOut = textarea {
        withElement {
            htmlInput = this
            addClass("form-control")
            id = htmlId
            value = model.data
            if (lines > 0) rows = lines
            required = this@TextArea.required
            readOnly = this@TextArea.readOnly
        }
    }

    override fun updateValueModel() {
        model.data = htmlInput.value
    }

    override fun component(): KafffeComponent = this

    private val extraValidation = ValidationExtra<TextArea>()

    fun addValidator(validator: Validator<TextArea>) {
        extraValidation.validators.add(validator)
    }

    fun removeValidator(validator: Validator<TextArea>) {
        extraValidation.validators.remove(validator)
    }

    override var validationMessageModel: Model<String> = Model.ofGet {
        when {
            !extraValidation.result.valid -> extraValidation.result.message
            else -> htmlInput.validationMessage
        }
    }

    override fun validate(): Boolean {
        extraValidation.clear()
        val valid = htmlInput.checkValidity() && extraValidation.validate(this)
        htmlInput.applyInputValidCssClasses(valid)
        return valid
    }
}

// DSL function for form component consumer DSL
fun <T : Any> FormComponentConsumer<T>.textArea(
    idInput: String,
    labelModel: Model<String>,
    valueModel: Model<String>
): TextArea {
    val input = TextArea(idInput, valueModel)
    decorateAndAdd(labelModel, input)
    return input
}

/**
 * Property based
 */
fun <T : Any> FormComponentConsumer<T>.textArea(property: KProperty1<T, String>): TextArea =
    textArea(property.name, labelStrategy.label(property.name), model.property(property))
