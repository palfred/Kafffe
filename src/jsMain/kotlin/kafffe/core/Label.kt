package kafffe.core

import kafffe.core.modifiers.StyleModifier

class Label(textModel: Model<String>) : KafffeComponentWithModel<String>(textModel) {
    constructor(text: String) : this(Model.of(text))
    init {
        setModelChangedRerender()
    }
    var text: String by delegateToModel()

    override fun KafffeHtmlBase.kafffeHtml() = span { text(text) }

    fun preformatted(): Label {
        modifiers.add(StyleModifier { whiteSpace = "pre-wrap" })
        return this
    }
}