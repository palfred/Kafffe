package kafffe.core

import org.w3c.dom.HTMLElement
import org.w3c.dom.css.CSSStyleDeclaration
import org.w3c.dom.events.Event

interface HtmlElementModifier {
    fun modify(element: HTMLElement)

    companion object {
        fun create(block: HTMLElement.() -> Unit): HtmlElementModifier = FunctionalHtmlElementModifier(block)
        fun style(block: CSSStyleDeclaration.() -> Unit): HtmlElementModifier = StyleModifier(block)
    }
}

class FunctionalHtmlElementModifier(val block: HTMLElement.() -> Unit) : HtmlElementModifier {
    override fun modify(element: HTMLElement) {
        element.block()
    }
}

class StyleModifier(val block: CSSStyleDeclaration.() -> Unit) : HtmlElementModifier {
    override fun modify(element: HTMLElement) {
        element.style.block()
    }
}

/**
 * Helper to shorted add if StyleModifier
 */
fun KafffeComponent.modifyStyle(block: CSSStyleDeclaration.() -> Unit) = this.modifiers.add(StyleModifier(block))

/**
 * Modifies a HTML element by setting the onchange handler.
 */
open class EventHandler(val eventName: String, val eventHandler: ((Event) -> dynamic)) : HtmlElementModifier {
    override fun modify(element: HTMLElement) {
        val onEventName = if (eventName.startsWith("on")) eventName else "on$eventName"
        element.asDynamic()[onEventName] = eventHandler
    }
}

/**
 * Helper to shorten add if ClickHandler
 */
fun KafffeComponent.on(eventName: String, eventHandler: ((Event) -> dynamic)) = this.modifiers.add(EventHandler(eventName, eventHandler))

/**
 * Modifies a HTML element by setting the onclick handler.
 */
class ClickHandler(val eventHandler: ((Event) -> dynamic)) : HtmlElementModifier {
    override fun modify(element: HTMLElement) {
        element.onclick = {
            it.preventDefault()
            eventHandler(it)
        }
    }
}

/**
 * Helper to shorten add if ClickHandler
 */
fun KafffeComponent.onclick(eventHandler: ((Event) -> dynamic)) = this.modifiers.add(ClickHandler(eventHandler))

/**
 * Modifies a HTML element by setting the onchange handler.
 */
class ChangeHandler(eventHandler: ((Event) -> dynamic)) : EventHandler("change", eventHandler)

/**
 * Helper to shorten add if ClickHandler
 */
fun KafffeComponent.onchange(eventHandler: ((Event) -> dynamic)) = this.modifiers.add(ChangeHandler(eventHandler))


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
}

/**
 * Helper to shorten add of CssClassModifier
 */
fun KafffeComponent.addClass(cssClass: String) = this.modifiers.add(CssClassModifier(cssClass, add = true))

fun KafffeComponent.removeClass(cssClass: String) = this.modifiers.add(CssClassModifier(cssClass, add = false))

class AttributeSetModifier(val attribute: String, val valueModel: Model<String>) : HtmlElementModifier {
    override fun modify(element: HTMLElement) {
        element.setAttribute(attribute, valueModel.data)
    }
}

/**
 * Helper to shorted add if AttribteModifier
 */
fun KafffeComponent.modifyAttributeSet(attribute: String, valueModel: Model<String>) = this.modifiers.add(AttributeSetModifier(attribute, valueModel))
