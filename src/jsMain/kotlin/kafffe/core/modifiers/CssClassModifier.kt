package kafffe.core.modifiers

import kafffe.core.KafffeComponent
import org.w3c.dom.HTMLElement

/**
 * Modifies a HTML element by adding or removing a CSS class in the class attribute.
 */
class CssClassModifier(val cssClass: String, var add: Boolean = true) : HtmlElementModifier {
    override fun modify(element: HTMLElement) {
        for (cssC in cssClass.split(" ")) {
            if (add) {
                if (!element.classList.contains(cssC)) {
                    element.classList.add(cssC)
                }
            } else {
                while (element.classList.contains(cssC)) {
                    element.classList.remove(cssC)
                }
            }
        }
    }

    companion object {
        /**
         * Helper to shorten add of CssClassModifier
         */
        fun KafffeComponent.cssClassModifier(cssClass: String, add: Boolean = true) = this.modifiers.add(CssClassModifier(cssClass, add = add))
    }
}

