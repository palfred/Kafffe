package kafffe.bootstrap.form

import kafffe.core.KafffeComponentWithModel
import kafffe.core.KafffeHtmlBase
import kafffe.core.Model
import kafffe.core.modifiers.modifyStyle

class LegendComponent(textModel: Model<String>) : KafffeComponentWithModel<String>(textModel) {
    init {
        setModelChangedRerender()
    }

    /**
     * Overwrite bootstrap 100% width
     */
    fun useAutoWidth() {
        modifyStyle { width = "auto" }
    }

    override fun KafffeHtmlBase.kafffeHtml() =  legend { text(model.data) }
}