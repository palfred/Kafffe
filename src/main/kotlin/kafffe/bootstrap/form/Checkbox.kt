package kafffe.bootstrap.form

import kafffe.core.KafffeComponent
import kafffe.core.KafffeComponentWithModel
import kafffe.core.KafffeHtmlBase
import kafffe.core.Model
import kafffe.messages.Messages
import org.w3c.dom.HTMLInputElement

/**
 * Holds a HTML form element checkbox
 */
class Checkbox(var idInput: String, valueModel: Model<Boolean>, val labelModel: Model<String>) :
    KafffeComponentWithModel<Boolean>(valueModel), FormInput {

    var required: Boolean by rerenderOnChange(false)
    var readOnly: Boolean by rerenderOnChange(false)
    var useCustom: Boolean = true

    private lateinit var htmlInput: HTMLInputElement

    override fun KafffeHtmlBase.kafffeHtml() =
        div {
            addClass(if (useCustom) "custom-control custom-checkbox" else "form-check")
            input {
                withElement {
                    htmlInput = this
                    addClass(if (useCustom) "custom-control-input" else "form-check-input")
                    type = "checkbox"
                    id = idInput
                    required = this@Checkbox.required
                    disabled = this@Checkbox.readOnly
                    checked = model.data
                }
            }
            label {
                withElement {
                    htmlFor = idInput
                    addClass(if (useCustom) "custom-control-label" else "form-check-label")
                }
                text(labelModel.data)
            }
        }

    override val htmlId: String get() = idInput
    override fun component(): KafffeComponent = this
    override fun updateValueModel() {
        model.data = htmlInput.checked
    }

    override fun validate(): Boolean = htmlInput.checkValidity()
    override var validationMessageModel: Model<String> =
        Model.ofGet { if (required) Messages.get().validation_required else htmlInput.validationMessage }
}

