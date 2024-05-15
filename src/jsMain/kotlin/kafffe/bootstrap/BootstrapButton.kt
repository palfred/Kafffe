package kafffe.bootstrap

import kafffe.core.*
import kafffe.core.modifiers.CssClassModifier
import org.w3c.dom.HTMLButtonElement
import org.w3c.dom.HTMLElement

open class BootstrapButton(label: Model<String>, val onClick: (source: BootstrapButton) -> Unit) :
    KafffeComponentWithModel<String>(label) {
    init {
        modifiers.add(CssClassModifier("btn"))
    }

    var label: String by delegateToModel()
    var btnType: ButtonType by rerenderOnChange(ButtonType.button)
    var color: BasicColor by rerenderOnChange(BasicColor.secondary)
    var iconClasses: String by rerenderOnChange("")
    var iconBefore: Boolean by rerenderOnChange(false)
    var disabled: Boolean by rerenderOnChange(false)

    /**
     * Can be used to make the button show processing on click. The busy icons is #indicatorIconClasses and defaults to a spinner icon.
     * If a busy indicator is used then the onclick function is repsponsible for calling #processingDone
     */
    var useBusyIndicator: Boolean by rerenderOnChange(false)
    var indicatorLabel: String by rerenderOnChange("")
    var indicatorIconClasses: String by rerenderOnChange("fas fa-spinner fa-spin")
    private var processing: Boolean by rerenderOnChange(false)

    /**
     * Should be called to get back to a non processing state in case busy indicator is used.
     */
    fun processingDone() {
        processing = false
    }

    /**
     * Setup busy indator using icon, good canditates can be found here: [spinners](https://fontawesome.com/search?o=r&m=free&c=spinners&s=solid)
     */
    fun useFontAwesomeSpinner(fontAwesomeIcon: String) {
        indicatorIconClasses = "fas $fontAwesomeIcon fa-spin"
        useBusyIndicator = true
    }

    fun useSpinner() = useFontAwesomeSpinner("fa-spinner")
    fun useSpinGear() = useFontAwesomeSpinner("fa-gear")

    override fun modifyHtml(element: HTMLElement): HTMLElement {
        color.btnModifier.modify(element)
        return super.modifyHtml(element)
    }

    override fun KafffeHtmlBase.kafffeHtml() =
        button() {
            withElement {
                onclick = { internalClick() }
                type = btnType.toString()
                if (this@BootstrapButton.disabled || processing) {
                    addClass("disabled")
                    disabled = true
                }
            }
            if (iconBefore && hasIcons()) {
                icon()
                text(" ")
            }
            text(label)
            if (!iconBefore && hasIcons()) {
                text(" ")
                icon()
            }
        }

    private fun hasIcons() = iconClasses.isNotEmpty() || (processing && indicatorIconClasses.isNotEmpty())

    private fun internalClick() {
        if (useBusyIndicator) {
            processing = true
        }
        onClick(this@BootstrapButton)
    }

    protected open fun KafffeHtml<HTMLButtonElement>.icon() {
        i {
            addClass(
                when {
                    processing -> indicatorIconClasses
                    else -> iconClasses
                }
            )
        }
    }
}