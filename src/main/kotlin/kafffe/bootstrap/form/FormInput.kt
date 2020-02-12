package kafffe.bootstrap.form

import kafffe.core.KafffeComponent

interface FormInput: FormValueProvider, FormValidityProvider {
    val htmlId: String
    fun component(): KafffeComponent
}
