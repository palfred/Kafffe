package kafffe.bootstrap.modifier

import kafffe.bootstrap.external.TooltipOption
import kafffe.bootstrap.external.bsJquery
import kafffe.bootstrap.external.jsCreate
import kafffe.core.modifiers.HtmlElementModifier
import kafffe.core.Model
import org.w3c.dom.HTMLElement
import org.w3c.dom.get

class BootstrapTooltipModifier(val model: Model<String>) : HtmlElementModifier {
    val options: TooltipOption = jsCreate()

    override fun modify(element: HTMLElement) {
        element.title = model.data
        element.attributes["data-toggle"]?.value = "tooltip"
        bsJquery(element).tooltip(options)
    }

    companion object {
        fun remove() {
            bsJquery("[data-toggle='tooltip']").remove();
        }
    }
}