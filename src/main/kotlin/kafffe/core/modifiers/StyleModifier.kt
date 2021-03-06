package kafffe.core.modifiers

import kafffe.core.KafffeComponent
import org.w3c.dom.HTMLElement
import org.w3c.dom.css.CSSStyleDeclaration

class StyleModifier(val block: CSSStyleDeclaration.() -> Unit) : HtmlElementModifier {
    override fun modify(element: HTMLElement) {
        element.style.block()
    }

    companion object {
        /**
         * Helper to shorted add if StyleModifier
         */
        fun KafffeComponent.styleModifier(block: CSSStyleDeclaration.() -> Unit) = this.modifiers.add(StyleModifier(block))
    }
}

