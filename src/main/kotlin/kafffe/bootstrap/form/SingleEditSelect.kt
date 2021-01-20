package kafffe.bootstrap.form

import kafffe.core.*
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLElement
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.asList
import org.w3c.dom.events.KeyboardEvent
import org.w3c.dom.events.MouseEvent
import kotlinx.browser.window
import kotlinx.dom.addClass
import kotlinx.dom.removeClass
import kotlin.reflect.KProperty1

/**
 * Holds an editor thats allows selection of predefined values from a list, either by keyboard or by mouse.
 * An input field will open on click or focus, and a dropdown will open with potential matches, up and down arrow keys will move through current choices and enter will select the current one.
 * Code is based on FormGroupSingleEditSelectString
 */
abstract class SingleEditSelect<T : Any>(
    override val htmlId: String,
    valueModel: Model<T?>,
    val choiceModel: Model<List<T>>
) :
    KafffeComponentWithModel<T?>(valueModel), FormInput {

    val changeListeners = mutableListOf<ChangeListenerWithValue<SingleEditSelect<T>, T?>>()

    /**
     * Modifiers to tweak look of selected values
     */
    val modifiersValue = mutableListOf<(value: T?) -> HtmlElementModifier>()

    /**
     * Modifiers to tweak HTMLInputElement
     */
    val modifiersInputControl = mutableListOf<HtmlElementModifier>()

    var currentChoice: T? by ChangeDelegateWithValue(valueModel.data, changeListeners)

    private fun displayToChoice(displayValue: String): T? = choiceModel.data.find { display(it) == displayValue }

    private fun choiceToIndex(choice: T?): Int? {
        choiceModel.data.forEachIndexed { i, c ->
            if (c == choice) return i
        }
        return null
    }

    override fun updateValueModel() {
        model.data = currentChoice
    }

    private inner class BadgeComponent() : KafffeComponent() {
        override fun KafffeHtmlBase.kafffeHtml(): KafffeHtmlOut =
            span {
                addClass(valueCssClasses(currentChoice))
                choiceRender(currentChoice)
                modifiersValue.forEach { mv -> mv(currentChoice).modify(element!!) }
            }
    }

    protected open fun <H : HTMLElement> KafffeHtml<H>.choiceRender(choice: T?) {
        text(display(choice))
    }

    private var badge: BadgeComponent = BadgeComponent()
    private lateinit var inputControl: KafffeHtml<HTMLInputElement>
    private lateinit var dropdown: KafffeHtml<HTMLDivElement>

    private var haveFocus = false
        get() = field
        set(value) {
            field = value
            if (value) {
                inputControl.element?.value = display(currentChoice)
                badge.html.style.display = "none"
            } else {
                inputControl.element?.value = ""
                badge.html.style.display = "inline-block"
            }
        }

    fun focusClaim() {
        inputControl.element?.focus()
    }

    override fun KafffeHtmlBase.kafffeHtml(): KafffeHtmlOut =
        div {
            addClass("form-control kf-single-edit")
            renderInput()
            add(badge.html)
            withElement {
                onclick = {
                    focusClaim()
                }
            }
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
                    width = "1%"
                }
            }
            val container = this.element!!
            inputControl = input {
                addClass("kf-single-edit-input ml-1")
                withElement {
                    id = htmlId
                    type = "text"
                    onfocus = {
                        container.style.width = "100%"
                        haveFocus = true
                        it
                    }
                    onblur = {
                        haveFocus = false
                        container.style.width = "1%"
                        window.setTimeout({
                            hideDropdown();
                        }, 300)
                        it
                    }
                    onkeydown = { onkey(it) }
                    oninput = {
                        renderMatches()
                    }
                    modifiersInputControl.forEach { it.modify(this) }
                }
            }
            dropdown = div {
                addClass("sd_dropdown bg-light text-dark")
                withElement { style.width = "100%" }
            }
        }
    }

    private fun hideDropdown() {
        dropdown.element?.style?.display = "none"
    }

    private fun showDropdown() {
        dropdown.element?.style?.display = "block"
    }

    protected open fun onkey(keyEvent: KeyboardEvent) {
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
            dropdown.element?.children?.asList()?.forEachIndexed { i, element ->
                if (i == index) {
                    element.addClass("sd_selected")
                } else {
                    element.removeClass("sd_selected")
                }
            }
        }
    }

    fun matches(): List<T> {
        val txt = inputControl.element?.value ?: ""
        return if (txt.length > 0) {
            matchesText(txt)
        } else {
            choiceModel.data.take(maxMatches).toList()
        }
    }

    protected open fun matchesText(txt: String): List<T> {
        return (
                choiceModel.data.filter { display(it).startsWith(txt, ignoreCase = true) }
                    .union(choiceModel.data.filter { display(it).contains(txt, ignoreCase = true) })
                )
            .take(maxMatches).toList()
    }

    fun renderMatches() {
        selectIndex = -1
        dropdown.element?.innerHTML = ""
        val htmlConsumer = KafffeHtml(dropdown.element)
        val matches = matches()

        for (match in matches) {
            htmlConsumer.div {
                addClass("sd_dropdown_item")
                choiceRender(match)
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
            window.setTimeout({ inputControl.element?.blur() /* how to focus next focusable element */ }, 200)
        }
    }

    private fun isSelected(choice: T): Boolean = currentChoice == choice

    abstract fun display(choice: T?): String

    override fun component(): KafffeComponent = this
    override fun validate(): Boolean = true
    override var validationMessageModel: Model<String> = Model.of("")
}

class SingleEditSelectString(idInput: String, valueModel: Model<String?>, choiceModel: Model<List<String>>) :
    SingleEditSelect<String>(idInput, valueModel, choiceModel) {
    override fun display(choice: String?) = choice ?: ""
}

// DSL function for form component consumer DSL
fun <T : Any>
        FormComponentConsumer<T>.editSelectSingle(
    idInput: String,
    valueModel: Model<String?>,
    choiceModel: Model<List<String>>
): SingleEditSelectString {
    return SingleEditSelectString(idInput, valueModel, choiceModel).also { addChild(it) }
}

fun <T : Any> FormComponentConsumer<T>.editSelectSingleNoLabel(
    property: KProperty1<T, String?>,
    choiceModel: Model<List<String>>
): SingleEditSelectString {
    return editSelectSingle(property.name, model.property(property), choiceModel)
}

fun <T : Any> FormComponentConsumer<T>.editSelectSingle(
    idInput: String,
    labelModel: Model<String>,
    valueModel: Model<String?>,
    choiceModel: Model<List<String>>
): SingleEditSelectString {
    val input = SingleEditSelectString(idInput, valueModel, choiceModel)
    decorateAndAdd(labelModel, input)
    return input
}

fun <T : Any> FormComponentConsumer<T>.editSelectSingle(
    property: KProperty1<T, String?>,
    choiceModel: Model<List<String>>
): SingleEditSelectString {
    return editSelectSingle(property.name, labelStrategy.label(property.name), model.property(property), choiceModel)
}
