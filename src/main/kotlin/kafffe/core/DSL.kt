package kafffe.core

import kafffe.core.modifiers.ClickHandler
import org.w3c.dom.events.Event

fun KafffeComponent.click(eventHandler: ((Event) -> dynamic)): ClickHandler {
    val handler = ClickHandler(eventHandler)
    this.modifiers.add(handler)
    return handler
}

