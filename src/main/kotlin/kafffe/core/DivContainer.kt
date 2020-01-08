package kafffe.core

import org.w3c.dom.HTMLElement

/**
 * A plain container that adds all the children directly to a div element.
 */
open class DivContainer() : KafffeComponent() {

    override fun createHtml(): HTMLElement {
        val div = htmlStart.div {}
        children.forEach { div.add(it.html) }
        return div.element!!
    }
}