package kafffe.bootstrap.form

import kafffe.core.HtmlElementModifier
import kafffe.core.KafffeHtml
import kafffe.core.Model
import kafffe.core.property
import org.w3c.dom.HTMLButtonElement
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLElement
import kotlin.reflect.KProperty1

/**
 * Holds a HTML form element select
 */
abstract class FormGroupMultipleDropdown<T : Any>(idInput: String, labelModel: Model<String>, valueModel: Model<List<T>>, val choiceModel: Model<List<T>>)
    : FormGroup<HTMLElement, List<T>>(idInput, labelModel, valueModel) {

    val currentChoiceIndexes: MutableSet<Int> = choicesToIndexes(valueModel.data, choiceModel.data)

    private fun choicesToIndexes(values: List<T>, choices: List<T>): MutableSet<Int> =
            choices.mapIndexed { i, c -> if (c in values) i else -1 }.filter { it >= 0 }.toMutableSet()

    override fun updateValueModel() {
        valueModel.data = currentChoices()
    }

    /**
     * Modifiers to tweak look of selected values
     */
    val modifiersValue = mutableListOf<(value: T) -> HtmlElementModifier>()

    private var dropdownButton: KafffeHtml<HTMLButtonElement>? = null
    private var dropdownMenu: KafffeHtml<HTMLDivElement>? = null

    override fun KafffeHtml<HTMLDivElement>.createInputElement(): HTMLElement {
        val currentChoices = currentChoices()
        return div {
            addClass("dropdown")
            dropdownButton = button {
                addClass("form-control dropdown-toggle")
                withElement {
                    id = "dropdownMenuButton"
                    setAttribute("data-toggle", "dropdown")
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
        }.element!!
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
                        addClass("badge badge-pill badge-info ml-1")
                        i {
                            addClass("fas fa-check fa-fw pr-1")
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
            it.element?.innerHTML = ""
            it.renderBadges(currentChoices)
        }
        dropdownMenu?.let {
            it.element?.innerHTML = ""
            it.renderDropdownChoices(currentChoices)
        }
    }

    private fun KafffeHtml<HTMLButtonElement>.renderBadges(currentChoices: List<T>) {
        for (choice in currentChoices) {
            span {
                addClass(valueCssClasses(choice))
                text(display(choice))
                modifiersValue.forEach { mv ->
                    mv(choice).modify(this.element!!)
                }
            }
        }
    }

    /**
     * Function that set value classes for each selected value.
     * May need display=inline-block to work properly.
     * @see valueCssClassDefault
     */
    var valueCssClasses: (T) -> String = { valueCssClassDefault }
    val valueCssClassDefault = "badge badge-info text-white ml-1"

    private fun currentChoices() = currentChoiceIndexes.map { choiceModel.data[it] }
    private fun isSelected(choice: T): Boolean = currentChoices().contains(choice)

    abstract fun display(choice: T): String

    // Internal string value commaseparated list of selected indexes. Obs not stable for changes in available choices
    override fun valueFromString(strValue: String): List<T> = strValue.split(",").map { choiceModel.data[it.toInt()] }.toList()

    override fun valueToString(value: List<T>): String = choicesToIndexes(value, choiceModel.data).joinToString(",")

}

class FormGroupMultipleDropdownString(idInput: String, labelModel: Model<String>, valueModel: Model<List<String>>, choiceModel: Model<List<String>>) :
        FormGroupMultipleDropdown<String>(idInput, labelModel, valueModel, choiceModel) {

    override fun display(choice: String) = choice
}

// DSL function for form component consumer DSL
fun <T : Any, F : Any>
        FormComponentConsumer<T, F>.dropdownMultiple(idInput: String,
                                                     labelModel: Model<String>,
                                                     valueModel: Model<List<String>>,
                                                     choiceModel: Model<List<String>>): FormGroupMultipleDropdownString {
    return FormGroupMultipleDropdownString(idInput, labelModel, valueModel, choiceModel).also { addChild(it) }
}

// Property based
fun <T : Any, F : Any>
        FormComponentConsumer<T, F>.dropdownMultiple(property: KProperty1<T, List<String>>,
                                                     choiceModel: Model<List<String>>): FormGroupMultipleDropdownString {
    return dropdownMultiple(property.name, labelStrategy.label(property.name), model.property(property), choiceModel)
}
