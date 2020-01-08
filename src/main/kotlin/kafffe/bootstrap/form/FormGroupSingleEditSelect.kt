package kafffe.bootstrap.form

import kafffe.core.*
import org.w3c.dom.*
import org.w3c.dom.events.KeyboardEvent
import org.w3c.dom.events.MouseEvent
import kotlin.browser.window
import kotlin.dom.addClass
import kotlin.dom.removeClass
import kotlin.reflect.KProperty1

/**
 * Holds an editor thats allows selection of predefined values from a list, either by keyboard or by mouse.
 * An input field will open on click or focus, and a dropdown will open with potential matches, up and down arrow keys will move through current choices and enter will select the current one.
 * Code is based on FormGroupMultipleEditSelectString - with value change to a single value.
 */
abstract class FormGroupSingleEditSelect<T : Any>(idInput: String, labelModel: Model<String>, valueModel: Model<T?>, val choiceModel: Model<List<T>>)
    : FormGroup<HTMLElement, T?>(idInput, labelModel, valueModel) {

    val changeListeners = mutableListOf<ChangeListenerWithValue<FormGroupSingleEditSelect<T>, T?>>()

    /**
     * Modifiers to tweak look of selected values
     */
    val modifiersValue = mutableListOf<(value: T?) -> HtmlElementModifier>()

    private var currentChoice: T? by ChangeDelegateWithValue(valueModel.data, changeListeners)

    private fun displayToChoice(displayValue: String): T? = choiceModel.data.find { display(it) == displayValue }

    private fun choiceToIndex(choice: T?): Int? {
        choiceModel.data.forEachIndexed { i, c ->
            if (c == choice) return i
        }
        return null
    }

    override fun updateValueModel() {
        valueModel.data = currentChoice
    }

    private inner class BadgeComponent() : KafffeComponent() {
        override fun KafffeHtmlBase.kafffeHtml(): KafffeHtmlOut {
            return a {
                addClass(valueCssClasses(currentChoice))
                text(display(currentChoice))
                modifiersValue.forEach { mv -> mv(currentChoice).modify(element!!) }
            }
        }
    }

    private var badge: BadgeComponent = BadgeComponent()
    private var formControl: KafffeHtml<HTMLDivElement>? = null
    private var inputControl: KafffeHtml<HTMLInputElement>? = null
    private var dropdown: KafffeHtml<HTMLDivElement>? = null
    private var haveFocus = false
        get() = field
        set(value) {
            field = value
            if (value) {
                inputControl?.element?.value = display(currentChoice)
                badge.html.style.display = "none"
            } else {
                inputControl?.element?.value = ""
                badge.html.style.display = "inline-block"
            }
        }

    override fun KafffeHtml<HTMLDivElement>.createInputElement(): HTMLElement {
        formControl = div {
            addClass("form-control kf-single-edit")
            renderInput()
            add(badge.html)
            onClick {
                inputControl?.element?.focus()
            }
        }
        return formControl!!.element!!
    }



    /**
     * Function that set value classes for selected value.
     * May need display=inline-block to work properly.
     * @see valueCssClassDefault
     */
    var valueCssClasses: (T?) -> String = { valueCssClassDefault }
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
                addClass("kf-single-edit-input ml-1")
                withElement {
                    type = "text"
                    onfocus = {
                        haveFocus = true
                        it
                    }
                    onblur = {
                        haveFocus = false
                        window.setTimeout({
                            hideDropdown();
                        }, 300)
                        it
                    }
                    onkeydown = { onkey(it) }
                    oninput = {
                        renderMatches()
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
        when (keyEvent.key) {
            // Escape
            "ArrowDown" -> selectNext()
            "ArrowUp" -> selectPrev()
            "Enter" -> {
                val m = matches();
                if (selectIndex in 0 until m.size) {
                    updateChoice(m[selectIndex])
                }
                keyEvent.preventDefault()
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
            htmlConsumer.div {
                addClass("sd_dropdown_item")
                text(display(match))
                withElement {
                    onclick = { evt: MouseEvent ->
                        updateChoice(match)
                        evt.preventDefault()
                    }
                }
            }
        }

        if (matches.isEmpty()) hideDropdown() else showDropdown()
    }

    private fun updateChoice(newChoice: T?) {
        currentChoice = newChoice
        badge.rerender()
        if (haveFocus) {
            window.setTimeout({ inputControl?.element?.blur() /* how to focus next focusable element */ }, 200)
        }
    }

    private fun isSelected(choice: T): Boolean = currentChoice == choice

    abstract fun display(choice: T?): String

    override fun valueFromString(strValue: String): T? = if (strValue.isNullOrEmpty()) null else choiceModel.data[strValue.toInt()]

    override fun valueToString(value: T?): String = choiceToIndex(value)?.toString() ?: ""

}

class FormGroupSingleEditSelectString(idInput: String, labelModel: Model<String>, valueModel: Model<String?>, choiceModel: Model<List<String>>) :
        FormGroupSingleEditSelect<String>(idInput, labelModel, valueModel, choiceModel) {
    override fun display(choice: String?) = choice ?: ""
}

// DSL function for form component consumer DSL
fun <T : Any, F : Any>
        FormComponentConsumer<T, F>.editSelectSingle(idInput: String,
                                                     labelModel: Model<String>,
                                                     valueModel: Model<String?>,
                                                     choiceModel: Model<List<String>>): FormGroupSingleEditSelectString {
    return FormGroupSingleEditSelectString(idInput, labelModel, valueModel, choiceModel).also { addChild(it) }
}

fun <T : Any, F : Any> FormComponentConsumer<T, F>.editSelectSingle(property: KProperty1<T, String?>,
                                                                    choiceModel: Model<List<String>>): FormGroupSingleEditSelectString {
    return editSelectSingle(property.name, labelStrategy.label(property.name), model.property(property), choiceModel)
}
