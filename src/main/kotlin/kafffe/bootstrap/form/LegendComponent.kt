package kafffe.bootstrap.form

import kafffe.core.KafffeComponentWithModel
import kafffe.core.KafffeHtmlBase
import kafffe.core.Model
import kafffe.core.modifiers.StyleModifier.Companion.styleModifier

class LegendComponent(textModel: Model<String>) : KafffeComponentWithModel<String>(textModel) {
    init {
        setModelChangedRerender()
    }

    /**
     * Overwrite bootstrap 100% width
     */
    fun useAutoWidth() {
        styleModifier { width = "auto" }
    }

    override fun KafffeHtmlBase.kafffeHtml() =  legend { text(model.data) }
}