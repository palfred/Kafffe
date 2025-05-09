package kafffe.bootstrap.form

import kafffe.core.*
import kafffe.core.modifiers.HtmlElementModifier
import kotlinx.browser.window
import kotlinx.dom.addClass
import kotlinx.dom.removeClass
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.asList
import org.w3c.dom.events.KeyboardEvent
import kotlin.reflect.KProperty1

/**
 * Holds an editor thats allows selection of predfined values from a list, either by keyboard or by mouse.
 * An input field will open on click or focus, and a dropdown will open with potetial matches, up and down arrow keys will move through current choices and enter will select the current one.
 * left and right arrow keys will move insertion point in the current values.
 * Backspace will delete previous value if at start of input field and Delete will delete next value if at the end.
 */
abstract class MultipleEditSelect<T : Any>(
    override val htmlId: String,
    val valueModel: Model<List<T>>,
    val choiceModel: Model<List<T>>
) : KafffeComponentWithModel<List<T>>(valueModel), FormInput {

    var allowDuplicates = false

    var updateModelOnChange: Boolean = false

    private fun choiceChanged() {
        if (updateModelOnChange)  {
           updateValueModel()
        }
    }

    private var currentChoiceIndexes: MutableList<Int> = choicesToIndexes(valueModel.data, choiceModel.data)

    /**
     * Refresh the current choice from the valueModel
     */
    fun updateFromValueModel() {
        currentChoiceIndexes = choicesToIndexes(valueModel.data, choiceModel.data)
        rerender()
    }

    /**
     * Modifiers to tweak look of selected values
     */
    val modifiersValue = mutableListOf<(value: T) -> HtmlElementModifier>()

    private fun choicesToIndexes(values: List<T>, choices: List<T>): MutableList<Int> =
        choices.mapIndexed { i, c -> if (c in values) i else -1 }.filter { it >= 0 }.toMutableList()

    private fun displayToChoice(displayValue: String): T? = choiceModel.data.find { display(it) == displayValue }

    private fun choicesToIndex(choice: T): Int? {
        choiceModel.data.forEachIndexed { i, c ->
            if (c == choice) return i
        }
        return null
    }

    override fun updateValueModel() {
        model.data = currentChoices()
    }

    private var inputIx = 1000
    private lateinit var formControl: KafffeHtml<HTMLDivElement>
    private lateinit var inputControl: KafffeHtml<HTMLInputElement>
    private lateinit var dropdown: KafffeHtml<HTMLDivElement>
    private var haveFocus: Boolean = false

    override fun KafffeHtmlBase.kafffeHtml(): KafffeHtmlOut {
        formControl = div {
            addClass("form-control kf-multiple-edit")
            renderBadgesAndEdit()
            onClick {
                inputControl.element.focus()
            }
        }
        return formControl
    }


    private fun KafffeHtml<HTMLDivElement>.renderBadgesAndEdit() {
        val currentChoices = currentChoices()
        if (inputIx > currentChoices.size) inputIx = currentChoices.size
        currentChoices.forEachIndexed { index, choice ->
            if (index == inputIx) {
                renderInput()
            }
            a {
                addClass(valueCssClasses(choice))
                addClass("me-1")
                text(display(choice))
                text(" ")
                i {
                    addClass("fas fa-times")
                }
                onClick {
                    currentChoiceIndexes.removeAt(index)
                    if (inputIx >= index && inputIx > 0) {
                        inputIx--
                    }
                    rerender()
                    choiceChanged()
                    it.preventDefault()
                }
                modifiersValue.forEach { mv ->
                    mv(choice).modify(this.element)
                }
            }
        }
        if (inputIx == currentChoices.size) {
            renderInput()
        }
    }

    /**
     * Function that set value classes for each selected value.
     * May need display=inline-block to work properly.
     * @see valueCssClassDefault
     */
    var valueCssClasses: (T) -> String = { valueCssClassDefault }
    val valueCssClassDefault = "badge bg-primary text-black "


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
                    onfocus = {
                        haveFocus = true
                        it
                    }
                    onblur = {
                        haveFocus = false
                        window.setTimeout({ hideDropdown() }, 300)
                        it
                    }
                    onkeydown = { onkey(it) }
                    oninput = {
                        renderMatches()
                    }
                    if (haveFocus) {
                        window.setTimeout({ inputControl.element.focus() }, 200)
                    }
                }
            }
            dropdown = div {
                addClass("sd_dropdown bg-light")
            }
        }
    }

    private fun hideDropdown() {
        dropdown.element.style.display = "none"
    }

    private fun showDropdown() {
        dropdown.element.style.display = "block"
    }

    private fun onkey(keyEvent: KeyboardEvent) {
        if (inputControl.element.value.isBlank() ?: false) {
            when (keyEvent.key) {
                "ArrowLeft" -> {
                    inputIx--
                    rerender()
                }
                "ArrowRight" -> {
                    inputIx++
                    rerender()
                }
                "Backspace" -> {
                    if (inputIx > 0) {
                        inputIx--
                        currentChoiceIndexes.removeAt(inputIx)
                        rerender()
                        choiceChanged()

                    }
                }
                "Delete" -> {
                    if (inputIx < currentChoiceIndexes.size) {
                        currentChoiceIndexes.removeAt(inputIx)
                        rerender()
                        choiceChanged()
                    }
                }
            }
        }
        when (keyEvent.key) {
            "ArrowDown" -> selectNext()
            "ArrowUp" -> selectPrev()
            "Enter" -> {
                keyEvent.preventDefault()
                val m = matches()
                if (selectIndex in 0 until m.size) {
                    addSelection(m[selectIndex])
                }
            }
        }
    }

    val maxMatches: Int = 7
    var selectIndex: Int = -1
    fun selectNext() {
        if (maxMatches >= selectIndex + 1) {
            ++selectIndex
        }
        select(selectIndex)
    }

    fun selectPrev() {
        if (selectIndex > 0) {
            --selectIndex
        }
        select(selectIndex)
    }

    fun select(index: Int) {
        val matches = matches()
        if (index in 0 until matches.size) {
            // set and remove "sd_selected" class
            dropdown.element.children.asList().forEachIndexed { i, element ->
                if (i == index) {
                    element.addClass("sd_selected")
                } else {
                    element.removeClass("sd_selected")
                }
            }
        }
    }

    fun matches(): List<T> {
        val txt = inputControl.element.value
        return if (txt.length > 0) {
            (
                    choiceModel.data.filter { display(it).startsWith(txt, ignoreCase = true) }
                        .union(choiceModel.data.filter { display(it).contains(txt, ignoreCase = true) })
                    )
                .take(maxMatches).toList()
        } else {
            choiceModel.data.take(maxMatches).toList()
        }
    }

    fun renderMatches() {
        selectIndex = -1
        dropdown.element.innerHTML = ""
        val htmlConsumer = KafffeHtml(dropdown.element)
        val matches = matches()

        for (match in matches) {
            val choiceIx = choicesToIndex(match)!!
            htmlConsumer.div {
                addClass("sd_dropdown_item")
                text(display(match))
                onClick {
                    addSelection(choiceIx)
                }
            }
        }

        if (matches.isEmpty()) hideDropdown() else showDropdown()
    }

    private fun addSelection(choiceIx: Int) {
        if (allowDuplicates || choiceIx !in currentChoiceIndexes) {
            currentChoiceIndexes.add(inputIx, choiceIx)
            inputIx++
        }
        rerender()
        choiceChanged()
    }

    private fun addSelection(selection: T) {
        choicesToIndex(selection)?.let {
            addSelection(it)
        }
    }

    fun currentChoices() = currentChoiceIndexes.map { choiceModel.data[it] }
    private fun isSelected(choice: T): Boolean = currentChoices().contains(choice)

    abstract fun display(choice: T): String

    override fun component(): KafffeComponent = this

    private val extraValidation = ValidationExtra<MultipleEditSelect<T>>()

    fun addValidator(validator: Validator<MultipleEditSelect<T>>) {
        extraValidation.validators.add(validator)
    }

    fun removeValidator(validator: Validator<MultipleEditSelect<T>>) {
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

class MultipleEditSelectString(
    idInput: String,
    valueModel: Model<List<String>>,
    choiceModel: Model<List<String>>
) : MultipleEditSelect<String>(idInput, valueModel, choiceModel) {
    override fun display(choice: String) = choice
}

// DSL function for form component consumer DSL
fun <T : Any> FormComponentConsumer<T>.editSelectMultiple(
    idInput: String,
    labelModel: Model<String>,
    valueModel: Model<List<String>>,
    choiceModel: Model<List<String>>
): MultipleEditSelectString {
    val input = MultipleEditSelectString(idInput, valueModel, choiceModel)
    decorateAndAdd(labelModel, input)
    return input
}

fun <T : Any> FormComponentConsumer<T>.editSelectMultiple(
    property: KProperty1<T, List<String>>,
    choiceModel: Model<List<String>>
): MultipleEditSelectString =
    editSelectMultiple(property.name, labelStrategy.label(property.name), model.property(property), choiceModel)
