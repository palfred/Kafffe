package kafffe.bootstrap.modifier

import bootstrap.Tooltip
import kafffe.bootstrap.external.createTooltip
import kafffe.bootstrap.external.jsCreate
import kafffe.core.KafffeComponent
import kafffe.core.Model
import kafffe.core.modifiers.AttachAwareModifier
import kafffe.core.modifiers.HtmlElementModifier
import kotlinx.browser.document
import org.w3c.dom.HTMLElement
import org.w3c.dom.NodeList
import org.w3c.dom.get

class BootstrapTooltipModifier(val model: Model<String>) : HtmlElementModifier, AttachAwareModifier {
    val options: Tooltip.Options = jsCreate()
    var tooltip: Tooltip? = null

    override fun modify(element: HTMLElement) {
        element.title = model.data
        element.attributes["data-bs-toggle"]?.value = "tooltip"
        tooltip= createTooltip(element, options)
    }

    companion object {
        fun removeAll() {

            val tooltips: NodeList = document.querySelectorAll("[role='tooltip']")
            for (i in 0 until tooltips.length) {
                (tooltips[i] as? HTMLElement) ?.remove()
            }
        }
    }

    override fun attach(component: KafffeComponent) {
    }

    override fun detach(component: KafffeComponent) {
        tooltip?.dispose()
        tooltip = null
    }
}