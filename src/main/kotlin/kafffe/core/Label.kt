package kafffe.core

import org.w3c.dom.HTMLElement

class Label(textModel: Model<String>) : KafffeComponentWithModel<String>(textModel) {
    constructor(text: String) : this(Model.of(text))

    var text: String by delegateToModel()

    override fun KafffeHtmlBase.kafffeHtml() = span { text(text) }

    fun preformatted(): Label {
        modifiers.add(StyleModifier { whiteSpace = "pre-wrap" })
        return this
    }
}