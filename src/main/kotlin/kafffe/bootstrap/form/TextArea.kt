package kafffe.bootstrap.form

import kafffe.core.*
import kafffe.messages.Messages
import org.w3c.dom.HTMLTextAreaElement
import kotlinx.dom.addClass
import kotlin.reflect.KProperty1

/**
 * Holds a HTML form element (input, select or similar) with the corresponding lable, error message, help message, ...
 * "firstName" -> leads to id "firstName", a lookup for label in messages using a key based on "firstName" and
 * use a property submodel of the surrounding form model for property "firstname"
 */
class TextArea(override val htmlId: String, valueModel: Model<String>)
    : KafffeComponentWithModel<String>(valueModel), FormInput {

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
    override var validationMessageModel: Model<String> = Model.ofGet{ if (required) Messages.get().validation_required else htmlInput.validationMessage}
    override fun validate(): Boolean {
        val valid = htmlInput.checkValidity()
        htmlInput.applyInputValidCssClasses(valid)
        return valid
    }
}

// DSL function for form component consumer DSL
fun <T : Any> FormComponentConsumer<T>.textArea(idInput: String, labelModel: Model<String>, valueModel: Model<String>): TextArea {
    val input = TextArea(idInput, valueModel)
    decorateAndAdd(labelModel, input)
    return input
}

/**
 * Property based
 */
fun <T : Any> FormComponentConsumer<T>.textArea(property: KProperty1<T, String>): TextArea =
    textArea(property.name, labelStrategy.label(property.name), model.property(property))
