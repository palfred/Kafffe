package kafffe.core.modifiers

import kafffe.core.KafffeComponent
import kotlinx.browser.window
import org.w3c.dom.HTMLElement
import org.w3c.dom.events.Event

/**
 * Debounce events. Only call func once per waitMs period. If triggered in started period then the period is restarted.
 * Example:
 * * oninput = debounceEvent(250) { inputEvent: InputEvent -> doSomething(inputEvent) }
 * * window.addEventListener("resize", debounceEvent(200) { resizeEvent:  doResize() })
 */
fun <T> debounceEvent(waitMs: Int, func: (T) -> Unit) : (T) -> Unit {
    var timeoutHandle: Int? = null;
    return { event : T ->
        var waitingFunc = { event: T ->
            timeoutHandle = null
            func(event)
        }

        timeoutHandle = if (timeoutHandle == null) {
            // First time in period handle directly
            func(event)
            window.setTimeout({ }, waitMs);
        } else {
            window.clearTimeout(timeoutHandle!!)
            window.setTimeout(waitingFunc, waitMs);
        }
    }
}

/**
 * Debounce events. Only call func once per waitMs period. If triggered in started period then the period is restarted.
 * Example:
 * * oninput = debounceEvent(250, 200) { inputEvent: InputEvent -> doSomething(inputEvent) }
 * * window.addEventListener("resize", debounceEvent(200, 200) { resizeEvent:  doResize() })
 */
fun <T> debounceEvent(waitMs: Int, waitFirstMs: Int, func: (T) -> Unit) : (T) -> Unit {
    var timeoutHandle: Int? = null;
    return { event: T ->
        var waitingFunc = { event: T ->
            timeoutHandle = null
            func(event)
        }

        timeoutHandle = if (timeoutHandle == null) {
            // First time in period handle directly
            window.setTimeout(waitingFunc, waitFirstMs);
        } else {
            window.clearTimeout(timeoutHandle!!)
            window.setTimeout(waitingFunc, waitMs);
        }
    }
}

/**
 * Modifies a HTML element by setting the onchange handler.
 */
open class EventHandler(val eventName: String, val eventHandler: ((Event) -> dynamic)) : HtmlElementModifier {
    override fun modify(element: HTMLElement) {
        if (eventName.startsWith("on")) throw Exception("eventName must not start with 'on'")
        element.addEventListener(eventName, eventHandler)
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
