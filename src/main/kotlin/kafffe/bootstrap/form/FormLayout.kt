package kafffe.bootstrap.form

import kafffe.core.*

open class FormLayout<T : Any, ContainerT : Any, F : Any>(val container: FormComponentConsumer<ContainerT, F>, model: Model<T>) : KafffeComponentWithModel<T>(model), FormComponentConsumer<T, F> {
    override val form = container.form
    override var labelStrategy = container.labelStrategy
    override var formGroupFactory = container.formGroupFactory

    private var useFieldset: Boolean = false
    fun useFieldset() { useFieldset = true }

    private var useBorder: Boolean = false
    fun useBorder() {useBorder = true}

    fun useBorderedLegend(legendText : Model<String>) : LegendComponent {
        useBorder()
        useFieldset()
        return legend(legendText).apply { useAutoWidth() }
    }

    override fun KafffeHtmlBase.kafffeHtml() =
            if (useFieldset) {
                div {
                    fieldset {
                        if (useBorder) {
                            addClass("border mr-2 mb-2 p-2")
                        }
                        children.forEach { add(it.html) }
                    }
                }
            } else {
                div {
                    if (useBorder) {
                        addClass("border")
                    }
                    children.forEach { add(it.html) }
                }
            }

}