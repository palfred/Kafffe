package kafffe.bootstrap.form

import kafffe.core.KafffeHtml
import kafffe.core.Model
import kafffe.core.property
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLTextAreaElement
import kotlin.reflect.KProperty1

/**
 * Holds a HTML form element (input, select or similar) with the corresponding lable, error message, help message, ...
 * "firstName" -> leads to id "firstName", a lookup for label in messages using a key based on "firstName" and
 * use a property submodel of the surrounding form model for property "firstname"
 */
class FormGroupTextArea(idInput: String, labelModel: Model<String>, valueModel: Model<String>)
    : FormGroup<HTMLTextAreaElement, String>(idInput, labelModel, valueModel) {

    override fun valueToString(value: String): String = value
    override fun valueFromString(strValue: String): String = strValue
    var lines: Int = 0

    override fun KafffeHtml<HTMLDivElement>.createInputElement(): HTMLTextAreaElement {
        return textarea {
            withElement {
                addClass("form-control")
                id = idInput
                value = valueModel.data
                if (lines > 0) rows = lines
                required = this@FormGroupTextArea.required
                readOnly = this@FormGroupTextArea.readOnly
            }
        }.element!!
    }

}

// DSL function for form component consumer DSL
fun <T : Any, F : Any> FormComponentConsumer<T, F>.textArea(idInput: String, labelModel: Model<String>, valueModel: Model<String>): FormGroupTextArea {
    return FormGroupTextArea(idInput, labelModel, valueModel).also { addChild(it) }
}

/**
 * Property based
 */
fun <T : Any, F : Any> FormComponentConsumer<T, F>.textArea(property: KProperty1<T, String>): FormGroupTextArea {
    return FormGroupTextArea(property.name, labelStrategy.label(property.name), model.property(property)).also { addChild(it) }
}