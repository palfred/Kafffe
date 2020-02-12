package kafffe.bootstrap.form

import kafffe.core.Model

interface FormValidityProvider {
    // Checks validity and return valid state
    fun validate() : Boolean

    var validationMessageModel: Model<String>
}