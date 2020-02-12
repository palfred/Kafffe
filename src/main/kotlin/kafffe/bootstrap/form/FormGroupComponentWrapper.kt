package kafffe.bootstrap.form

import kafffe.core.*
import org.w3c.dom.HTMLElement

/**
 * Wraps a KafffeCompoent with label and from group like most FormGroup input elements
 **/
class FormGroupComponentWrapper( val labelModel: Model<String>, val inputComponent: KafffeComponent) : KafffeComponent() {
    init {
        addChild(inputComponent)
    }
    val modifiersInput = mutableListOf<HtmlElementModifier>()
    val modifiersLabel = mutableListOf<HtmlElementModifier>()

    protected var inputElement: HTMLElement? = null
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
                text(labelModel.data)
            }.element
            inputElement = add(inputComponent.html).element
        }
    }

    fun addInputClassModifier(cssClass: String): FormGroupComponentWrapper {
        modifiersInput.add(CssClassModifier(cssClass))
        return this
    }

    fun addLabelClassModifier(cssClass: String): FormGroupComponentWrapper {
        modifiersLabel.add(CssClassModifier(cssClass))
        return this
    }

}

fun <T : Any, F : Any> FormComponentConsumer<T, F>.labelWrapper(labelModel: Model<String>, component: KafffeComponent) : FormGroupComponentWrapper {
    val wrapper = FormGroupComponentWrapper(labelModel, component)
    addChild(wrapper)
    return wrapper
}

fun <T : Any, F : Any> FormComponentConsumer<T, F>.labelWrapperInput(labelModel: Model<String>, component: FormInput)  {
    val wrapper = formGroupFactory(labelModel, component)
    addChild(wrapper)
}