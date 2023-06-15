package kafffe.bootstrap.form

data class ValidationResult(val valid: Boolean, val message: String)

typealias Validator<T> = (input: T) -> ValidationResult
