package kafffe.bootstrap.form

import kafffe.core.*
import org.w3c.dom.HTMLFormElement
import org.w3c.dom.events.Event
import kotlin.dom.addClass

open class BootstrapForm<T : Any>(model: Model<T>) : KafffeComponentWithModel<T>(model), FormComponentConsumer<T, T> {
    init {
        setModelChangedNoop()
    }

    override val form = this

    var inline: Boolean by rerenderOnChange(false)
    override var labelStrategy: LabelStrategy by rerenderOnChange(CamelCaseWordsStrategy())

    var onSubmitOk: () -> Unit = { println("Submit OK") }

    open fun onSubmit(event: Event) {
        if (isRendered) {
            event.preventDefault()
            event.stopPropagation(); // Should we support actual form submit
            processForm(onSubmitOk)
        }
    }

    fun processForm(onOk: () -> Unit, onError: () -> Unit = {}) {
        if (isRendered) {
            val form = (html as HTMLFormElement);
            if (form.checkValidity()) {
                noRerender {
                    visitChildrenRecusive {
                        if (this is FormValueProvider) {
                            updateValueModel()
                        }
                    }
                }

                onOk()
            } else {
                onError()
            }
            form.addClass("was-validated")
        }
    }

    open fun onCancel() {
        onCancelClick()
    }

    var onCancelClick: () -> Unit = {}

    override fun KafffeHtmlBase.kafffeHtml() =
        form {
            withElement {
                onsubmit = this@BootstrapForm::onSubmit
                noValidate = true // Disable browserdefault
                addClass("needs-validation")
                if (inline) {
                    addClass("form-inline")
                }
            }
            for (child in children) {
                add(child.html)
            }
        }

}