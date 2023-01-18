package kafffe.core.modifiers

import kafffe.core.KafffeComponent
import kotlinx.browser.window
import org.w3c.dom.MediaQueryList
import org.w3c.dom.events.Event

class MediaQueryAwareModifier(val mediaQuery: String, val onMatch: () -> Unit, val onNoMatch: () -> Unit = {}) : AttachAwareModifier {
    private var callback: (Event) -> Unit = {if (matchMedia.matches) onMatch() else onNoMatch()}
    private lateinit var matchMedia: MediaQueryList;

    fun matches() = matchMedia.matches

    override fun attach(component: KafffeComponent) {
        matchMedia = window.matchMedia(mediaQuery)
        if (matchMedia.matches) onMatch() else onNoMatch()
        matchMedia.addListener(callback)
    }

    override fun detach(component: KafffeComponent) {
        matchMedia.removeListener(callback)
    }
}