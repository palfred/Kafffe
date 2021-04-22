package kafffe.core.modifiers

import kafffe.core.KafffeComponent
import kafffe.core.Model
import org.w3c.dom.HTMLElement
import org.w3c.dom.css.CSSStyleDeclaration
import org.w3c.dom.events.Event

interface HtmlElementModifier {
    fun modify(element: HTMLElement)

    companion object {
        fun create(block: HTMLElement.() -> Unit): HtmlElementModifier = FunctionalHtmlElementModifier(block)
        fun style(block: CSSStyleDeclaration.() -> Unit): HtmlElementModifier = StyleModifier(block)

        /**
         * Creates and add html modier.
         */
        fun KafffeComponent.htmlElementModifier(block: HTMLElement.() -> Unit): HtmlElementModifier {
            val modifier = create(block)
            this.modifiers.add(modifier)
            return modifier
        }
    }
}

class FunctionalHtmlElementModifier(val block: HTMLElement.() -> Unit) : HtmlElementModifier {
    override fun modify(element: HTMLElement) {
        element.block()
    }
}


class AttributeSetModifier(val attribute: String, val valueModel: Model<String>) : HtmlElementModifier {
    override fun modify(element: HTMLElement) {
        element.setAttribute(attribute, valueModel.data)
    }
}

/**
 * Helper to shorted add if AttribteModifier
 */
fun KafffeComponent.modifyAttributeSet(attribute: String, valueModel: Model<String>) = this.modifiers.add(
    AttributeSetModifier(attribute, valueModel)
)
