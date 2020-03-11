package kafffe.bootstrap.form

import kafffe.core.KafffeComponent
import kafffe.core.Model

interface FormValidityProvider {
    // Checks validity and return valid state
    fun validate() : Boolean

    var validationMessageModel: Model<String>

    fun validateAndNotify(): Boolean {
        val valid = validate()
        if (valid) {
            notifyParentConsumer(ValidationState.OK, "")
        } else {
            notifyParentConsumer(ValidationState.ERROR, validationMessageModel.data)
        }
        return valid
    }

    fun notifyParentConsumer(state: ValidationState, message: String) {
        if (this is KafffeComponent) {
            parentOfType(FormValidityConsumer::class)?.onValidation(ValidationEvent(this, state, message) )
        }
    }
}

enum class ValidationState {
    OK, WARNING, ERROR, INFO
}
data class ValidationEvent(val component: KafffeComponent, val state: ValidationState = ValidationState.OK, val message: String = "" )

interface FormValidityConsumer {
    fun onValidation(event: ValidationEvent)
}