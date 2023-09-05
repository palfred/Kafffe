package kafffe.bootstrap.form

import kafffe.core.*
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.events.KeyboardEvent
import kotlinx.browser.window
import kotlin.reflect.KProperty1

/**
 * Holds an editor thats allows editing of multiple string values (list og String)
 * An input field will open on click or focus, and a then it will be possible to add a value by type text and then enter.
 * Left and right arrow keys will move insertion point in the current values, when at the start of an empty input it will move to the previous tekst,
 * if at start of a tekst it will move to an empty text between the current text and the previous (likewise for right arrow).
 * Backspace will delete previous value if at start of input field and Delete will delete next value if at the end.
 */
open class MultipleEdit(override val htmlId: String, valueModel: Model<List<String>>)
    : KafffeComponentWithModel<List<String>>(valueModel), FormInput {

    private val currentValues: MutableList<String> = valueModel.data.toMutableList()
    fun currentValues() = currentValues.toList()

    private var currentInputValue: String = ""

    // the value that was at current input index when starting the input field
    private var inputIx = 1000

    private lateinit var formControl: KafffeHtml<HTMLDivElement>
    private lateinit var inputControl: KafffeHtml<HTMLInputElement>
    private var haveFocus: Boolean = false

    override fun updateValueModel() {
        model.data = currentValues.toList()
    }

    override fun KafffeHtmlBase.kafffeHtml(): KafffeHtmlOut {
        formControl = div {
            addClass("form-control kf-multiple-edit")
            renderBadges()
            onClick {
                inputControl.element.focus()
            }
        }
        return formControl
    }


    private fun KafffeHtml<HTMLDivElement>.renderBadges() {
        if (inputIx > currentValues.size) inputIx = currentValues.size
        currentValues.forEachIndexed { index, cValue: String ->
            if (index == inputIx) {
                renderInput()
            }
            a {
                addClass("badge bg-primary text-black ms-1")
                text(cValue)
                text(" ")
                i {
                    addClass("fas fa-times")
                }
                onClick {
                    currentValues.removeAt(index)
                    if (inputIx >= index && inputIx > 0) {
                        inputIx--
                    }
                    rerender()
                    it.preventDefault()
                }
            }
        }
        if (inputIx == currentValues.size) {
            renderInput()
        }
    }

    private fun KafffeHtml<HTMLDivElement>.renderInput() {
        span {
            withElement {
                with(style) {
                    display = "inline-block"
                    position = "relative"
                }
            }
            inputControl = input {
                addClass("kf-multiple-edit-input ms-1")
                withElement {
                    type = "text"
                    value = currentInputValue
                    onfocus = {
                        haveFocus = true
                        it
                    }
                    onblur = {
                        haveFocus = false
                        it
                    }
                    onkeydown = { onkey(it) }
                    if (haveFocus) {
                        window.setTimeout({ inputControl.element.focus() }, 200)
                    }
                }
            }
        }
    }

    private fun onkey(keyEvent: KeyboardEvent) {
        inputControl.element.let { input ->
            val atStart = input.selectionStart == 0
            val atEnd = input.selectionStart == input.value.length
            val hasValue = input.value.isNotBlank()
            when (keyEvent.key) {
                "ArrowLeft"  -> {
                    if (atStart) {
                        if (hasValue) {
                            currentValues.add(inputIx, input.value)
                            currentInputValue = ""
                        } else {
                            if (inputIx >= 0) {
                                inputIx--
                                currentInputValue = currentValues[inputIx]
                                currentValues.removeAt(inputIx)
                            }
                        }
                        rerender()
                    }
                }

                "ArrowRight" -> {
                    if (atEnd) {
                        if (hasValue) {
                            currentValues.add(inputIx, input.value)
                            currentInputValue = ""
                            inputIx++
                        } else {
                            if (inputIx < currentValues.size) {
                                currentInputValue = currentValues[inputIx]
                                currentValues.removeAt(inputIx)
                            }
                        }
                        rerender()
                    }
                }

                "Backspace" -> {
                    if (inputIx > 0 && !hasValue) {
                        inputIx--
                        currentValues.removeAt(inputIx)
                        rerender()
                    }
                }

                "Delete" -> {
                    if (inputIx < currentValues.size && !hasValue) {
                        currentValues.removeAt(inputIx)
                        rerender()
                    }
                }

                "Enter" -> {
                    if (input.value.isNotBlank()) {
                        currentValues.add(inputIx, input.value)
                        inputIx++
                        input.value = ""
                    }
                    rerender()
                    keyEvent.preventDefault()
                }

                "Escape" -> {
                    if (currentInputValue.isNotBlank()) {
                        currentValues.add(inputIx, currentInputValue)
                        inputIx++
                        input.value = ""
                    }
                    rerender()
                    keyEvent.preventDefault()
                }
            }
        }
    }

    override fun component(): KafffeComponent = this

    private val extraValidation = ValidationExtra<MultipleEdit>()

    fun addValidator(validator: Validator<MultipleEdit>) {
        extraValidation.validators.add(validator)
    }

    fun removeValidator(validator: Validator<MultipleEdit>) {
        extraValidation.validators.remove(validator)
    }

    override var validationMessageModel: Model<String> = Model.ofGet {
        if (!extraValidation.result.valid) extraValidation.result.message else ""
    }

    override fun validate(): Boolean {
        extraValidation.clear()
        val valid = extraValidation.validate(this)
        if (isRendered) html.applyInputValidCssClasses(valid)
        return valid
    }
}


// DSL function for form component consumer DSL
fun <T : Any> FormComponentConsumer<T>.editMultiple(idInput: String, labelModel: Model<String>, valueModel: Model<List<String>>): MultipleEdit {
    val input = MultipleEdit(idInput, valueModel)
    val group = decorateAndAdd(labelModel, input)
    return input
}

/**
 * Property based
 */
fun <T : Any> FormComponentConsumer<T>.editMultiple(property: KProperty1<T, List<String>>): MultipleEdit {
    return editMultiple(property.name, labelStrategy.label(property.name), model.property(property))
}
