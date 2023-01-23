package kafffe.bootstrap.modifier

import bootstrap.Popover
import kafffe.bootstrap.external.createPopover
import kafffe.bootstrap.external.jsCreate
import kafffe.core.modifiers.HtmlElementModifier
import kotlinx.browser.document
import org.w3c.dom.HTMLCollection
import org.w3c.dom.HTMLElement
import org.w3c.dom.get

class BootstrapPopoverModifier() : HtmlElementModifier {
    val options: Popover.Options = jsCreate()

    init {
        options.trigger = "hover"
    }

    override fun modify(element: HTMLElement) {
        //element.attributes["data-bs-toggle"]?.value = "popover"
        createPopover(element, options)
    }

    companion object {
        fun removeAll() {
            val popovers: HTMLCollection = document.getElementsByClassName("popover")
            for (i in 0 until popovers.length) {
                (popovers[i] as? HTMLElement) ?.remove()
            }
        }
    }
}