package kafffe.bootstrap.form

import kafffe.core.*
import kafffe.messages.Messages
import org.w3c.dom.HTMLElement

/**
 * Bootstrap form group that wrap input componenten with label and validation message
 **/
class FormInputGroupDecorator(val labelModel: Model<String>, val inputComponent: FormInput) : KafffeComponent() {
    val modifiersLabel = mutableListOf<HtmlElementModifier>()
    val validationMessageModel: Model<String> get() = inputComponent.validationMessageModel
    val idInput: String get() = inputComponent.htmlId
    var useToolipValidationMessages: Boolean by rerenderOnChange(false)

    init {
        addChild(inputComponent.component())
    }

    override fun attach() {
        super.attach()
        labelModel.listeners.add(onModelChanged)
        validationMessageModel.listeners.add(onModelChanged)
    }

    override fun detach() {
        labelModel.listeners.remove(onModelChanged)
        validationMessageModel.listeners.remove(onModelChanged)
        super.detach()
    }

    protected val onModelChanged = ModelChangeListener(::rerender)

    protected lateinit var labelElement: HTMLElement

    override fun modifyHtml(element: HTMLElement): HTMLElement {
        for (m in modifiersLabel) {
            m.modify(labelElement)
        }
        return super.modifyHtml(element)
    }

    override fun KafffeHtmlBase.kafffeHtml(): KafffeHtmlOut {
        return div {
            addClass("form-group")
            if (useToolipValidationMessages) {
                element?.style?.position = "relative"
            }
            labelElement = label {
                element?.htmlFor = idInput
                text(labelModel.data)
            }.element!!
            add(inputComponent.component().html)
            small {
                if (useToolipValidationMessages) {
                    addClass("invalid-tooltip")
                } else {
                    addClass("invalid-feedback")
                }
                if (validationMessageModel.data.isEmpty()) {
                    text(Messages.get().validation_required)
                } else {
                    text(validationMessageModel.data)
                }
            }
        }
    }

    companion object {
        fun feedback(labelModel: Model<String>, inputComponent: FormInput) =
            FormInputGroupDecorator(labelModel, inputComponent)

        fun tooltip(labelModel: Model<String>, inputComponent: FormInput) =
            FormInputGroupDecorator(labelModel, inputComponent).apply { useToolipValidationMessages = true }
    }
}


