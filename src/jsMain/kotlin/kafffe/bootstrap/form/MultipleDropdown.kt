package kafffe.bootstrap.form

import kafffe.core.*
import kafffe.core.modifiers.HtmlElementModifier
import org.w3c.dom.HTMLButtonElement
import org.w3c.dom.HTMLDivElement
import kotlin.reflect.KProperty1

/**
 * Holds a HTML form element select
 */
abstract class MultipleDropdown<T : Any>(
    override val htmlId: String,
    val valueModel: Model<List<T>>,
    val choiceModel: Model<List<T>>
) : KafffeComponentWithModel<List<T>>(valueModel), FormInput {

    private var currentChoiceIndexes: MutableSet<Int> = choicesToIndexes(valueModel.data, choiceModel.data)

    /**
     * Refresh the current choice from the valueModel
     */
    fun updateFromValueModel() {
        currentChoiceIndexes = choicesToIndexes(valueModel.data, choiceModel.data)
        rerender()
    }

    private fun choicesToIndexes(values: List<T>, choices: List<T>): MutableSet<Int> =
        choices.mapIndexed { i, c -> if (c in values) i else -1 }.filter { it >= 0 }.toMutableSet()

    override fun updateValueModel() {
        model.data = currentChoices()
    }

    /**
     * Modifiers to tweak look of selected values
     */
    val modifiersValue = mutableListOf<(value: T) -> HtmlElementModifier>()

    private var dropdownButton: KafffeHtml<HTMLButtonElement>? = null
    private var dropdownMenu: KafffeHtml<HTMLDivElement>? = null

    override fun KafffeHtmlBase.kafffeHtml(): KafffeHtmlOut {
        val currentChoices = currentChoices()
        return div {
            addClass("dropdown")
            withElement {
                id = htmlId
            }
            dropdownButton = button {
                addClass("form-control dropdown-toggle")
                withElement {
                    id = "dropdownMenuButton"
                    setAttribute("data-bs-toggle", "dropdown")
                    setAttribute("aria-haspopup", "true")
                    setAttribute("aria-expanded", "false")
                    style.textAlign = "left"
                    style.height = "auto"
                }
                renderBadges(currentChoices)
            }
            dropdownMenu = div {
                addClass("dropdown-menu form-control")
                withElement {
                    setAttribute("aria-labelledby", "dropdownMenuButton")
                    setAttribute("x-placement", "bottom-start")
                    style.height = "20em"
                    style.overflowY = "auto"
                }
                renderDropdownChoices(currentChoices)
            }
        }
    }

    private fun KafffeHtml<HTMLDivElement>.renderDropdownChoices(currentChoices: List<T>) {
        choiceModel.data.forEachIndexed { choiceIx, choice ->
            a {
                addClass("dropdown-item bg-white")
                withElement {
                    href = "#"
                    onclick = {
                        if (currentChoiceIndexes.contains(choiceIx)) {
                            currentChoiceIndexes.remove(choiceIx)
                        } else {
                            currentChoiceIndexes.add(choiceIx)
                        }
                        choiceChanged()
                        dropdownButton?.element?.focus()
                        it.preventDefault()
                    }
                }
                if (choice in currentChoices) {
                    span {
                        addClass("badge badge-pill bg-primary text-black  ms-1")
                        i {
                            addClass("fas fa-check fa-fw pe-1")
                        }
                        text(display(choice))
                    }
                } else {
                    text(display(choice))
                }

            }
        }
    }

    private fun choiceChanged() {
        val currentChoices = currentChoices()
        dropdownButton?.let {
            it.element.innerHTML = ""
            it.renderBadges(currentChoices)
        }
        dropdownMenu?.let {
            it.element.innerHTML = ""
            it.renderDropdownChoices(currentChoices)
        }
        if (updateModelOnChange) {
            updateValueModel()
        }
    }

    private fun KafffeHtml<HTMLButtonElement>.renderBadges(currentChoices: List<T>) {
        for (choice in currentChoices) {
            span {
                addClass(valueCssClasses(choice))
                text(display(choice))
                modifiersValue.forEach { mv ->
                    mv(choice).modify(this.element)
                }
            }
        }
    }

    var updateModelOnChange: Boolean = false

    /**
     * Function that set value classes for each selected value.
     * May need display=inline-block to work properly.
     * @see valueCssClassDefault
     */
    var valueCssClasses: (T) -> String = { valueCssClassDefault }
    val valueCssClassDefault = "badge bg-primary text-black "

    private fun currentChoices() = currentChoiceIndexes.map { choiceModel.data[it] }
    private fun isSelected(choice: T): Boolean = currentChoices().contains(choice)

    abstract fun display(choice: T): String

    override fun component(): KafffeComponent = this

    private val extraValidation = ValidationExtra<MultipleDropdown<T>>()

    fun addValidator(validator: Validator<MultipleDropdown<T>>) {
        extraValidation.validators.add(validator)
    }

    fun removeValidator(validator: Validator<MultipleDropdown<T>>) {
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

class MultipleDropdownString(idInput: String, valueModel: Model<List<String>>, choiceModel: Model<List<String>>) :
    MultipleDropdown<String>(idInput, valueModel, choiceModel) {
    override fun display(choice: String) = choice
}

// DSL function for form component consumer DSL
fun <T : Any> FormComponentConsumer<T>.dropdownMultiple(
    idInput: String,
    labelModel: Model<String>,
    valueModel: Model<List<String>>,
    choiceModel: Model<List<String>>
): MultipleDropdownString {
    val inp = MultipleDropdownString(idInput, valueModel, choiceModel)
    decorateAndAdd(labelModel, inp)
    return inp
}

// Property based
fun <T : Any> FormComponentConsumer<T>.dropdownMultiple(
    property: KProperty1<T, List<String>>,
    choiceModel: Model<List<String>>
): MultipleDropdownString =
    dropdownMultiple(property.name, labelStrategy.label(property.name), model.property(property), choiceModel)
