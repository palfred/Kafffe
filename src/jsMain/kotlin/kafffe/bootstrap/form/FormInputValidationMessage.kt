package kafffe.bootstrap.form

import kafffe.core.KafffeComponentWithModel
import kafffe.core.KafffeHtmlBase
import kafffe.core.Model

/**
 * Shows validation event messages
 **/
class FormInputValidationMessage(model: Model<ValidationEvent>) :
    KafffeComponentWithModel<ValidationEvent>(model) {
    init {
        setModelChangedRerender()
    }
    var useTooltip: Boolean by rerenderOnChange(false)

    override fun KafffeHtmlBase.kafffeHtml() =
        small {
            val validation = model.data
            val cssPrefix = if (validation.state == ValidationState.OK) "valid" else "invalid"
            // TODO other states
            if (useTooltip) {
                addClass("$cssPrefix-tooltip")
            } else {
                addClass("$cssPrefix-feedback")
            }
            withElement {
                style.display = if (validation.message.isEmpty()) "none" else "block"
            }
            text(validation.message)
        }


}


