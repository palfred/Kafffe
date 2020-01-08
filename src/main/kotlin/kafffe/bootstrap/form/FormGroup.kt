package kafffe.bootstrap.form

import kafffe.core.*
import kafffe.messages.Messages
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLElement

/**
 * Holds a HTML form element input, textarea, select, ... and assoiciated label.
 * IE is the type of HTMLElement
 * T is the type of the data model type
 *
 **/
abstract class FormGroup<IE : HTMLElement, T : Any?>(val idInput: String, val labelModel: Model<String>, val valueModel: Model<T>) : KafffeComponent(), FormValueProvider {
    var required: Boolean by rerenderOnChange(false)
    var readOnly: Boolean by rerenderOnChange(false)

    var validationMessageModel: Model<String> = Model.of("")

    val modifiersInput = mutableListOf<HtmlElementModifier>()
    val modifiersLabel = mutableListOf<HtmlElementModifier>()

    override fun attach() {
        super.attach()
        labelModel.listeners.add(onModelChanged)
        valueModel.listeners.add(onValueChanged)
        validationMessageModel.listeners.add(onModelChanged)
    }

    override fun detach() {
        labelModel.listeners.remove(onModelChanged)
        valueModel.listeners.remove(onValueChanged)
        validationMessageModel.listeners.remove(onModelChanged)
        super.detach()
    }

    protected val onModelChanged = ModelChangeListener(::rerender)

    protected val onValueChanged = ModelChangeListener(::rerender)

    // Support other types than String
    abstract fun valueFromString(strValue: String): T

    abstract fun valueToString(value: T): String

    // Example
//    <div class="form-group">
//    <label for="exampleInputEmail1">Email address</label>
//    <input type="email" class="form-control" id="exampleInputEmail1" aria-describedby="emailHelp" placeholder="Enter email">
//    <small id="emailHelp" class="form-text text-muted">We'll never share your email with anyone else.</small>
//    </div>
    protected var inputElement: IE? = null
    protected var labelElement: HTMLElement? = null

    override fun modifyHtml(element: HTMLElement): HTMLElement {
        inputElement?.let {
            for (m in modifiersInput) {
                m.modify(it)
            }
        }
        labelElement?.let {
            for (m in modifiersLabel) {
                m.modify(it)
            }
        }
        return super.modifyHtml(element)
    }

    override fun KafffeHtmlBase.kafffeHtml(): KafffeHtmlOut {
        return div {
            addClass("form-group")
            labelElement = label {
                element?.htmlFor = idInput
                text(labelModel.data)
            }.element
            inputElement = createInputElement()
            small {
                addClass("invalid-feedback")
                if (validationMessageModel.data.isEmpty() && required) {
                    text(Messages.get().validation_required)
                } else {
                    text(validationMessageModel.data)
                }
            }
        }
    }

    abstract fun KafffeHtml<HTMLDivElement>.createInputElement(): IE

    override fun updateValueModel() {
        inputElement?.let {
            valueModel.data = valueFromString(it.asDynamic()["value"] as String)
        }
    }

    open fun refreshFromValueModel() {
        inputElement?.let {
            it.asDynamic()["value"] = valueToString(valueModel.data)
        }
    }

}

fun <T : FormGroup<*,*>> T.addInputClassModifier(cssClass : String): T {
    modifiersInput.add(CssClassModifier(cssClass))
    return this
}

fun <T : FormGroup<*,*>> T.addLabelClassModifier(cssClass : String): T {
    modifiersLabel.add(CssClassModifier(cssClass))
    return this
}

