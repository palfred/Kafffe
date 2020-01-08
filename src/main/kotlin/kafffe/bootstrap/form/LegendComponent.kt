package kafffe.bootstrap.form

import kafffe.core.*

class LegendComponent(textModel: Model<String>) : KafffeComponentWithModel<String>(textModel) {
    /**
     * Overwrite bootstrap 100% width
     */
    fun useAutoWidth() {
        modifyStyle { width = "auto" }
    }

    override fun KafffeHtmlBase.kafffeHtml() =  legend { text(model.data) }
}