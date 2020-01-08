package kafffe.core

import org.w3c.dom.events.Event

fun KafffeComponent.click(eventHandler: ((Event) -> dynamic)): ClickHandler {
    val handler = ClickHandler(eventHandler)
    this.modifiers.add(handler)
    return handler
}

