package kafffe.bootstrap.form

import kafffe.core.KafffeComponent
import kafffe.core.Model
import org.w3c.dom.HTMLElement

/**
 * Adapts any KafffeComponent to be used as form input. No validation and data model update by default.
 */
open class KaffeeComponentAsInputAdapter(val childComp: KafffeComponent): KafffeComponent(), FormInput {
    init {
        addChild(childComp)
    }

    override fun createHtml(): HTMLElement  =
        childComp.html

    override val htmlId: String = "notused"

    override fun component(): KafffeComponent = this
    override fun updateValueModel() { }
    override fun validate(): Boolean = true
    override var validationMessageModel: Model<String> = Model.of("")

}

