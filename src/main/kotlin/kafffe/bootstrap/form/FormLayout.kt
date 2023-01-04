package kafffe.bootstrap.form

import kafffe.core.KafffeComponentWithModel
import kafffe.core.KafffeHtmlBase
import kafffe.core.Model

/**
 * Base class for form layout which are used as sub components of this class.
 * @param T is the object type if the model of this layout (usually the same or a submodel of the container)
 */
open class FormLayout<T : Any>(val container: FormComponentConsumer<*>, model: Model<T>) :
    KafffeComponentWithModel<T>(model), FormComponentConsumer<T> {
    override val form = container.form
    override var labelStrategy = container.labelStrategy
    override var inputDecorator = container.inputDecorator

    private var useFieldset: Boolean = false
    fun useFieldset() {
        useFieldset = true
    }

    private var useBorder: Boolean = false
    fun useBorder() {
        useBorder = true
    }

    fun useBorderedLegend(legendText: Model<String>): LegendComponent {
        useBorder()
        useFieldset()
        return legend(legendText).apply { useAutoWidth() }
    }

    override fun KafffeHtmlBase.kafffeHtml() =
        if (useFieldset) {
            div {
                fieldset {
                    if (useBorder) {
                        addClass("border me-2 mb-2 p-2")
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