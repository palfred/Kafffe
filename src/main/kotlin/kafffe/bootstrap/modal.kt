package kafffe.bootstrap

import kafffe.core.*
import kafffe.messages.Messages
import kafffe.messages.i18nText
import org.w3c.dom.DOMPoint
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLElement
import kotlin.dom.addClass

enum class ModalSize(val css: String) {
    small("modal-sm"), medium("modal-md"), large("modal-lg")
}

/**
 * Modal component / dialog support. Unlike using bootstrap with jquery and static markup, the modals will usually be created and shown and then removed on close.
 */
open class Modal(title: Model<String>) : RootComponentWithModel<String>(title) {
    var size: ModalSize = ModalSize.medium
    // Absolut position to this point if not null
    var absolutePosition: DOMPoint? = null

    val modifiersModal = mutableListOf<HtmlElementModifier>()
    val modifiersContent = mutableListOf<HtmlElementModifier>()
    val modifiersHeader = mutableListOf<HtmlElementModifier>()
    val modifiersBody = mutableListOf<HtmlElementModifier>()
    val modifiersFooter = mutableListOf<HtmlElementModifier>()

    companion object {

        /**
         * Constructs and shows confirm dialog
         */
        fun confirm(
            title: Model<String>,
            question: Model<String>,
            absolutePosition: DOMPoint?,
            yesHandler: () -> Unit
        ) {
            val confirm = TextDialog(title, question)
            confirm.absolutePosition = absolutePosition
            confirm.yesNo({
                yesHandler()
                confirm.detach()
            })
            confirm.attach()
        }

        fun confirm(title: Model<String>, question: Model<String>, yesHandler: () -> Unit) =
            confirm(title, question, null, yesHandler)
    }

    // The components rendered as footer
    val footerChildren = mutableListOf<KafffeComponent>()

    override fun KafffeHtmlBase.kafffeHtml() =
            div {
                withElement {
                    addClass("modal")
                    addClass("fade")
                    addClass("show")
                    style.display = "block"
                    style.backgroundColor = "rgba(0,0,0,0.4)"
                    div {
                        addClass("modal-dialog")
                        addClass(size.css)
                        if (absolutePosition != null) {
                            withElement {
                                with(style) {
                                    position = "fixed"
                                    left = "${absolutePosition!!.x}px"
                                    top = "${absolutePosition!!.y}px"
                                    marginTop = "0"
                                    paddingTop = "0"
                                }
                            }
                        }
                        modifiersModal.forEach { it.modify(this.element!!) }

                        div {
                            addClass("modal-content")
                            div {
                                addClass("modal-header")
                                createHeader()
                                modifiersHeader.forEach { it.modify(this.element!!) }
                            }
                            div {
                                addClass("modal-body")
                                createBody()
                                modifiersBody.forEach { it.modify(this.element!!) }
                            }
                            if (footerChildren.isNotEmpty() || modifiersFooter.isNotEmpty()) {
                                div {
                                    addClass("modal-footer")
                                    createFooter()
                                    modifiersFooter.forEach { it.modify(this.element!!) }
                                }
                            }
                            modifiersContent.forEach { it.modify(this.element!!) }
                        }
                        for (child in this@Modal.children) {
                            add(child.html)
                        }
                    }

                }
            }

    protected open fun KafffeHtml<HTMLDivElement>.createHeader() {
        h4 { text(model.data) }
        button {
            withElement {
                addClass("close")
                onclick = { this@Modal.detach() }
                innerHTML = "&times;"
            }
        }
    }

    protected open fun KafffeHtml<HTMLDivElement>.createBody() {
        for (child in children) {
            add(child.html)
        }
    }

    protected open fun KafffeHtml<HTMLDivElement>.createFooter() {
        for (child in footerChildren) {
            add(child.html)
        }
    }

    fun addOkCancelButtons(okHandler: () -> Unit, ok: Model<String> = Model.of("OK"), cancel: Model<String> = Model.of("Cancel")) {
        with(footerChildren) {
            add(BootstrapButton(ok, { okHandler() }).apply {
                iconClasses = "fas fa-check"
                color = BasicColor.primary
            })
            add(BootstrapButton(cancel, { detach() }).apply {
                iconClasses = "fas fa-times"
                color = BasicColor.secondary
            })
        }

    }

    fun okCancel(okHandler: () -> Unit) = this.addOkCancelButtons(okHandler, ok = i18nText(Messages::ok), cancel = i18nText(Messages::cancel))

    fun addYesNoButtons(yesHandler: () -> Unit, noHandler: () -> Unit, yes: Model<String> = Model.of("Yees"), no: Model<String> = Model.of("No")) {
        with(footerChildren) {
            add(BootstrapButton(yes, { yesHandler() }).apply {
                iconClasses = "fas fa-thumbs-up"
                color = BasicColor.primary
            })
            add(BootstrapButton(no, { noHandler() }).apply {
                iconClasses = "fas fa-thumbs-down"
                color = BasicColor.secondary
            })
        }

    }

    fun yesNo(yesHandler: () -> Unit = { detach() }, noHandler: () -> Unit = { detach() }) = this.addYesNoButtons(yesHandler, noHandler, yes = i18nText(Messages::yes), no = i18nText(Messages::no))

}

open class TextDialog(titleText: Model<String>, val bodyText: Model<String>) : Modal(titleText) {
    override fun KafffeHtml<HTMLDivElement>.createBody() {
        text(bodyText.data)
    }

}
