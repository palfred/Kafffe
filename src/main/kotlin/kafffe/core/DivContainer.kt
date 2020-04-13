package kafffe.core

/**
 * A plain container that adds all the children directly to a div element.
 */
open class DivContainer() : KafffeComponent() {

    override fun KafffeHtmlBase.kafffeHtml() =
        div {
            for (child in children) {
                add(child.html)
            }
        }
}