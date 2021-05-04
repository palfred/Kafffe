package kafffe.core.modifiers

import kafffe.core.KafffeComponent
import kotlinx.browser.window
import org.w3c.dom.events.Event

class ResizeWindowAwareModifier(val onResize: () -> Unit) : AttachAwareModifier {
    private val callback: (Event) -> Unit = {onResize()}

    override fun attach(component: KafffeComponent) {
        window.addEventListener("resize", callback)
    }

    override fun detach(component: KafffeComponent) {
        window.removeEventListener("resize", callback)
    }
}