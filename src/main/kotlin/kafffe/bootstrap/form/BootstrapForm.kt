package kafffe.bootstrap.form

import kafffe.bootstrap.form.FormInputGroupDecorator.Companion.feedback
import kafffe.core.*
import org.w3c.dom.HTMLElement
import org.w3c.dom.HTMLFormElement
import org.w3c.dom.events.Event
import kotlin.dom.addClass
import kotlin.dom.removeClass

open class BootstrapForm<T : Any>(model: Model<T>) : KafffeComponentWithModel<T>(model), FormComponentConsumer<T> {
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

    /**
     * Updates value model by recursive visit of value providing children
     */
    fun updateValueModel() {
        noRerender {
            visitChildrenRecusive {
                if (this is FormValueProvider) {
                    updateValueModel()
                }
            }
        }
    }

    /**
     * Validates model by recursive visit of value providing children and checking HTML Form validity (HTML5 Form validity.
     */
    fun validate(): Boolean {
        val form = (html as HTMLFormElement);
        val htmlValidity = form.checkValidity()
        var kafffeValidity = true
        visitChildrenRecusive {
            if (this is FormValidityProvider) {
                if (!validate()) kafffeValidity = false
            }
        }
        return htmlValidity && kafffeValidity
    }

    fun processForm(onOk: () -> Unit, onError: () -> Unit = {}) {
        if (isRendered) {
            @Suppress("UNUSED_VARIABLE")
            val form = (html as HTMLFormElement);
            if (validate()) {
                updateValueModel()
                onOk()
            } else {
                onError()
            }
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
                if (inline) {
                    addClass("form-inline")
                }
            }
            for (child in children) {
                add(child.html)
            }
        }

    override var inputDecorator: (labelModel: Model<String>, inputComp: FormInput) -> KafffeComponent =
        FormInputGroupDecorator.Companion::feedback

}

/**
 * Applies the bootstrap classes for custom marking valid on form-control input
 */
fun HTMLElement.applyInputValidCssClasses(valid: Boolean) {
    if (valid) {
        removeClass("is-invalid")
        // element.addClass("is-valid") we not want to make the interface to noisy, so we not apply valid as default
    } else {
        addClass("is-invalid")
        removeClass("is-valid")
    }
}