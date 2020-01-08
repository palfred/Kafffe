package kafffe.bootstrap.form

import kafffe.core.*
import org.w3c.dom.HTMLElement
import org.w3c.dom.HTMLInputElement
import kotlin.reflect.KProperty1

/**
 * Holds a HTML form element checkbox
 */
class Checkbox(val idInput: String, valueModel: Model<Boolean>, val labelModel: Model<String>)
    : KafffeComponentWithModel<Boolean>(valueModel), FormValueProvider {

    var required: Boolean by rerenderOnChange(false)
    var readOnly: Boolean by rerenderOnChange(false)
    var useCustom: Boolean = true

    private var htmlCheckInput: HTMLInputElement? = null

    override fun KafffeHtmlBase.kafffeHtml() =
            div {
                addClass(if (useCustom) "custom-control custom-checkbox" else "form-check")
                htmlCheckInput = input {
                    withElement {
                        addClass(if (useCustom) "custom-control-input" else "form-check-input")
                        type = "checkbox"
                        id = idInput
                        required = this@Checkbox.required
                        disabled = this@Checkbox.readOnly
                        checked = model.data
                    }
                }.element
                label {
                    withElement {
                        htmlFor = idInput
                        addClass(if (useCustom) "custom-control-label" else "form-check-label")
                    }
                    text(labelModel.data)
                }
            }

    override fun updateValueModel() {
        model.data = htmlCheckInput?.checked ?: false
    }
}

// DSL function for form component consumer DSL
fun <T : Any, F : Any> FormComponentConsumer<T, F>.checkbox(idInput: String, valueModel: Model<Boolean>, labelModel: Model<String>): Checkbox {
    return Checkbox(idInput, valueModel, labelModel).also { addChild(it) }
}

/**
 * Property based
 */
fun <T : Any, F : Any> FormComponentConsumer<T, F>.checkbox(property: KProperty1<T, Boolean>): Checkbox {
    return checkbox(property.name, model.property(property), labelStrategy.label(property.name))
}