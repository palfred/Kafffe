package kafffe.bootstrap.form

import kafffe.core.HtmlElementModifier
import kafffe.core.KafffeHtml
import kafffe.core.Model
import kafffe.core.property
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLElement
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.asList
import org.w3c.dom.events.KeyboardEvent
import kotlin.browser.window
import kotlin.dom.addClass
import kotlin.dom.removeClass
import kotlin.reflect.KProperty1

/**
 * Holds an editor thats allows selection of predfined values from a list, either by keyboard or by mouse.
 * An input field will open on click or focus, and a dropdown will open with potetial matches, up and down arrow keys will move through current choices and enter will select the current one.
 * left and right arrow keys will move insertion point in the current values.
 * Backspace will delete previous value if at start of input field and Delete will delete next value if at the end.
 */
abstract class FormGroupMultipleEditSelect<T : Any>(idInput: String, labelModel: Model<String>, valueModel: Model<List<T>>, val choiceModel: Model<List<T>>)
    : FormGroup<HTMLElement, List<T>>(idInput, labelModel, valueModel) {

    var allowDuplicates = false

    private val currentChoiceIndexes: MutableList<Int> = choicesToIndexes(valueModel.data, choiceModel.data)

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
        valueModel.data = currentChoices()
    }

    private var inputIx = 1000;
    private var formControl: KafffeHtml<HTMLDivElement>? = null
    private var inputControl: KafffeHtml<HTMLInputElement>? = null
    private var dropdown: KafffeHtml<HTMLDivElement>? = null
    private var haveFocus: Boolean = false

    override fun KafffeHtml<HTMLDivElement>.createInputElement(): HTMLElement {
        formControl = div {
            addClass("form-control kf-multiple-edit")
            renderBadgesAndEdit()
            onClick {
                inputControl?.element?.focus()
            }
        }
        return formControl!!.element!!
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
                    it.preventDefault()
                }
                modifiersValue.forEach { mv ->
                    mv(choice).modify(this.element!!)
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
    val valueCssClassDefault = "badge badge-info text-white ml-1"


    private fun KafffeHtml<HTMLDivElement>.renderInput() {
        span {
            withElement {
                with(style) {
                    display = "inline-block"
                    position = "relative"
                }
            }
            inputControl = input {
                addClass("kf-multiple-edit-input ml-1")
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
                        window.setTimeout({ inputControl?.element?.focus() }, 200);
                    }
                }
            }
            dropdown = div {
                addClass("sd_dropdown bg-light")
            }
        }
    }

    private fun hideDropdown() {
        dropdown?.element?.style?.display = "none"
    }

    private fun showDropdown() {
        dropdown?.element?.style?.display = "block"
    }

    private fun onkey(keyEvent: KeyboardEvent) {
        if (inputControl?.element?.value?.isBlank() ?: false) {
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
                    }
                }
                "Delete" -> {
                    if (inputIx < currentChoiceIndexes.size) {
                        currentChoiceIndexes.removeAt(inputIx)
                        rerender()
                    }
                }
            }
        }
        when (keyEvent.key) {
            "ArrowDown" -> selectNext()
            "ArrowUp" -> selectPrev()
            "Enter" -> {
                val m = matches();
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
            dropdown?.element?.children?.asList()?.forEachIndexed { i, element ->
                if (i == index) {
                    element.addClass("sd_selected")
                } else {
                    element.removeClass("sd_selected")
                }
            }
        }
    }

    fun matches(): List<T> {
        val txt = inputControl?.element?.value ?: ""
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
        dropdown?.element?.innerHTML = ""
        val htmlConsumer = KafffeHtml(dropdown?.element)
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
    }

    private fun addSelection(selection: T) {
        choicesToIndex(selection)?.let {
            addSelection(it)
        }
    }

    private fun currentChoices() = currentChoiceIndexes.map { choiceModel.data[it] }
    private fun isSelected(choice: T): Boolean = currentChoices().contains(choice)

    abstract fun display(choice: T): String

    // Internal string value commaseparated list of selected indexes. Obs not stable for changes in available choices
    override fun valueFromString(strValue: String): List<T> = strValue.split(",").map { choiceModel.data[it.toInt()] }.toList()

    override fun valueToString(value: List<T>): String = choicesToIndexes(value, choiceModel.data).joinToString(",")

}

class FormGroupMultipleEditSelectString(idInput: String, labelModel: Model<String>, valueModel: Model<List<String>>, choiceModel: Model<List<String>>) :
        FormGroupMultipleEditSelect<String>(idInput, labelModel, valueModel, choiceModel) {

    override fun display(choice: String) = choice
}

// DSL function for form component consumer DSL
fun <T : Any, F : Any>
        FormComponentConsumer<T, F>.editSelectMultiple(idInput: String,
                                                       labelModel: Model<String>,
                                                       valueModel: Model<List<String>>,
                                                       choiceModel: Model<List<String>>): FormGroupMultipleEditSelectString {
    return FormGroupMultipleEditSelectString(idInput, labelModel, valueModel, choiceModel).also { addChild(it) }
}

fun <T : Any, F : Any>
        FormComponentConsumer<T, F>.editSelectMultiple(property: KProperty1<T, List<String>>,
                                                       choiceModel: Model<List<String>>): FormGroupMultipleEditSelectString {
    return editSelectMultiple(property.name, labelStrategy.label(property.name), model.property(property), choiceModel)
}
