@file:JsModule("bootstrap")
@file:JsNonModule
@file:Suppress("INTERFACE_WITH_SUPERCLASS", "OVERRIDING_FINAL_MEMBER", "RETURN_TYPE_MISMATCH_ON_OVERRIDE", "CONFLICTING_OVERLOADS")

package bootstrap

import kotlin.js.*
import org.khronos.webgl.*
import org.w3c.dom.*
import org.w3c.dom.events.*
import org.w3c.dom.parsing.*
import org.w3c.dom.svg.*
import org.w3c.dom.url.*
import org.w3c.fetch.*
import org.w3c.files.*
import org.w3c.notifications.*
import org.w3c.performance.*
import org.w3c.workers.*
import org.w3c.xhr.*

external open class Modal : BaseComponent {
    constructor(element: String, options: OptionsPartial = definedExternally)
    constructor(element: String)
    constructor(element: Element, options: OptionsPartial = definedExternally)
    constructor(element: Element)
    open fun toggle(relatedTarget: HTMLElement = definedExternally)
    open fun show(relatedTarget: HTMLElement = definedExternally)
    open fun hide()
    open fun handleUpdate()
    interface Options {
        var backdrop: dynamic /* "static" | Boolean */
            get() = definedExternally
            set(value) = definedExternally
        var keyboard: Boolean
        var focus: Boolean
    }
    interface OptionsPartial {
        var backdrop: dynamic /* "static" | Boolean? */
            get() = definedExternally
            set(value) = definedExternally
        var keyboard: Boolean?
            get() = definedExternally
            set(value) = definedExternally
        var focus: Boolean?
            get() = definedExternally
            set(value) = definedExternally
    }
    interface Event : CustomEvent {
        var relatedTarget: HTMLElement?
            get() = definedExternally
            set(value) = definedExternally
    }

    companion object {
        var Default: Options
    }
}