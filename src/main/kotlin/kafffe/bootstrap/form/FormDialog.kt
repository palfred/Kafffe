package kafffe.bootstrap.form

import kafffe.bootstrap.Modal
import kafffe.bootstrap.ModalSize
import kafffe.core.KafffeHtml
import kafffe.core.Model
import org.w3c.dom.DOMPoint
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.events.Event

open class FormDialog<T : Any>(val title: Model<String>, formModel: Model<T>) : BootstrapForm<T>(formModel) {
    var absolutePosition: DOMPoint? = null
    var size: ModalSize = ModalSize.medium
    val modal = object : Modal(title) {
        override fun KafffeHtml<HTMLDivElement>.createBody() {
            add(this@FormDialog.html)
        }
    }

    override fun onSubmit(event: Event) {
        if (isRendered) {
            event.preventDefault()
            event.stopPropagation();
            processForm({ onSubmitOk(); detach() })
        }
    }

    override fun onCancel() {
        detach()
    }

    override fun attach() {
        with(modal) {
            absolutePosition = this@FormDialog.absolutePosition
            size = this@FormDialog.size
            attach()
        }
    }

    override fun detach() {
        modal.detach()
    }

}