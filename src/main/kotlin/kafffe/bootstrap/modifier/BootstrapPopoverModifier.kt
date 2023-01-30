package kafffe.bootstrap.modifier

import bootstrap.Popover
import kafffe.bootstrap.external.createPopover
import kafffe.bootstrap.external.jsCreate
import kafffe.core.KafffeComponent
import kafffe.core.modifiers.AttachAwareModifier
import kafffe.core.modifiers.HtmlElementModifier
import kotlinx.browser.document
import org.w3c.dom.HTMLCollection
import org.w3c.dom.HTMLElement
import org.w3c.dom.get

class BootstrapPopoverModifier() : HtmlElementModifier, AttachAwareModifier {
    val options: Popover.Options = jsCreate()
    var popover: Popover? = null

    init {
        options.trigger = "hover"
    }

    override fun modify(element: HTMLElement) {
        //element.attributes["data-bs-toggle"]?.value = "popover"
        popover = createPopover(element, options)
    }

    companion object {
        fun removeAll() {
            val popovers: HTMLCollection = document.getElementsByClassName("popover")
            for (i in 0 until popovers.length) {
                (popovers[i] as? HTMLElement) ?.remove()
            }
        }
    }

    override fun attach(component: KafffeComponent) {
    }

    override fun detach(component: KafffeComponent) {
        popover?.dispose()
    }
}