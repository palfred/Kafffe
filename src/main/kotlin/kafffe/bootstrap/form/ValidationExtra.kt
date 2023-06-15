package kafffe.bootstrap.form

/**
 * Helper class to aid extra validation in FormValidityProviders
 */
class ValidationExtra<T: FormValidityProvider> {
    private var message: String = ""
    private var valid = true

    val validators = mutableListOf<Validator<T>>()
    val result get() = ValidationResult(valid, message)

    fun  validate(input: T): Boolean {
        clear()
        validators.forEach {validator ->
            val result =  validator(input)
            if (valid && !result.valid) {
                valid = false
                message = result.message
            }
        }
        return valid
    }

    fun clear() {
        message = ""
        valid = true
    }

}