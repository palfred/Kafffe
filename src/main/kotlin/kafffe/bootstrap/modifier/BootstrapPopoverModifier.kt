package kafffe.bootstrap.modifier

import kafffe.bootstrap.external.PopoverOption
import kafffe.bootstrap.external.bsJquery
import kafffe.bootstrap.external.jsCreate
import kafffe.core.modifiers.HtmlElementModifier
import org.w3c.dom.HTMLElement

class BootstrapPopoverModifier() : HtmlElementModifier {
    val options: PopoverOption = jsCreate()

    init {
        options.trigger = "hover"
    }

    override fun modify(element: HTMLElement) {
        //element.attributes["data-bs-toggle"]?.value = "popover"
        bsJquery(element).popover(options)
    }

    companion object {
        fun removeAll() {
            bsJquery(".popover").remove();
        }
    }
}