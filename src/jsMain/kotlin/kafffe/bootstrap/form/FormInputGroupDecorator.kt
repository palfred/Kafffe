package kafffe.bootstrap.form

import kafffe.core.*
import kafffe.core.modifiers.HtmlElementModifier
import org.w3c.dom.HTMLElement

/**
 * Bootstrap form group that wrap input componenten with label and validation message
 **/
class FormInputGroupDecorator(val labelModel: Model<String>, val inputComponent: FormInput) : KafffeComponent(),
    FormValidityConsumer {

    val modifiersLabel = mutableListOf<HtmlElementModifier>()
    val validationModel: Model<ValidationEvent> = Model.of(ValidationEvent(this))
    val idInput: String get() = inputComponent.htmlId

    private val messageComponent = addChild(FormInputValidationMessage(validationModel))
    var useTooltipValidationMessages: Boolean
        get() = messageComponent.useTooltip
        set(value) {
            messageComponent.useTooltip = value
        }

    init {
        addChild(inputComponent.component())
    }

    // TODO rewrite to use kafffe component for label
    override fun attach() {
        super.attach()
        labelModel.listeners.add(onModelChanged)
    }

    override fun detach() {
        labelModel.listeners.remove(onModelChanged)
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
            if (useTooltipValidationMessages) {
                element.style.position = "relative"
            }
            labelElement = label {
                element.htmlFor = idInput
                text(labelModel.data)
            }.element
            add(inputComponent.component().html)
            add(messageComponent.html)
        }
    }

    companion object {
        fun feedback(labelModel: Model<String>, inputComponent: FormInput) =
            FormInputGroupDecorator(labelModel, inputComponent)

        fun tooltip(labelModel: Model<String>, inputComponent: FormInput) =
            FormInputGroupDecorator(labelModel, inputComponent).apply { useTooltipValidationMessages = true }
    }

    override fun onValidation(event: ValidationEvent) {
        validationModel.data = event
    }
}


