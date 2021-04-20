package kafffe.bootstrap

import kafffe.core.*
import kafffe.core.modifiers.CssClassModifier
import org.w3c.dom.HTMLButtonElement
import org.w3c.dom.HTMLElement

open class BootstrapButton(label: Model<String>, val onClick: (source: BootstrapButton) -> Unit) : KafffeComponentWithModel<String>(label) {
    init {
        modifiers.add(CssClassModifier("btn"))
    }

    var label: String by delegateToModel()
    var btnType: ButtonType by rerenderOnChange(ButtonType.button)
    var color: BasicColor by rerenderOnChange(BasicColor.secondary)
    var iconClasses: String by rerenderOnChange("")
    var iconBefore: Boolean by rerenderOnChange(false)
    var disabled: Boolean by rerenderOnChange(false)

    override fun modifyHtml(element: HTMLElement): HTMLElement {
        color.btnModifier.modify(element)
        return super.modifyHtml(element)
    }

    override fun KafffeHtmlBase.kafffeHtml() =
            button() {
                withElement {
                    onclick = { onClick(this@BootstrapButton) }
                    type = btnType.toString()
                    if (this@BootstrapButton.disabled) {
                        addClass("disabled")
                        disabled = true
                    }
                }
                if (iconBefore && !iconClasses.isNullOrEmpty()) {
                    icon()
                    text(" ")
                }
                text(label)
                if (!iconBefore && !iconClasses.isNullOrEmpty()) {
                    text(" ")
                    icon()
                }
            }

    protected open fun KafffeHtml<HTMLButtonElement>.icon() {
        i {
            iconClasses.split(' ').forEach { addClass(it) }
        }
    }
}