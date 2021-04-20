package kafffe.core.modifiers

import kafffe.core.KafffeComponent
import org.w3c.dom.HTMLElement
import org.w3c.dom.events.Event

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
