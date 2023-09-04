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

external open class ScrollSpy : BaseComponent {
    constructor(element: String, options: OptionsPartial = definedExternally)
    constructor(element: String)
    constructor(element: Element, options: OptionsPartial = definedExternally)
    constructor(element: Element)
    open fun refresh()
    interface Options {
        var offset: Number
        var method: String /* "auto" | "offset" | "position" */
        var rootMargin: String
        var smoothScroll: Boolean
        var target: dynamic /* String | Element | JQuery */
            get() = definedExternally
            set(value) = definedExternally
        var threshold: dynamic /* Array<Number>? | String? */
            get() = definedExternally
            set(value) = definedExternally
    }
    interface OptionsPartial {
        var offset: Number?
            get() = definedExternally
            set(value) = definedExternally
        var method: String? /* "auto" | "offset" | "position" */
            get() = definedExternally
            set(value) = definedExternally
        var rootMargin: String?
            get() = definedExternally
            set(value) = definedExternally
        var smoothScroll: Boolean?
            get() = definedExternally
            set(value) = definedExternally
        var target: dynamic /* String? | Element? | JQuery? */
            get() = definedExternally
            set(value) = definedExternally
        var threshold: dynamic /* Array<Number>? | String? */
            get() = definedExternally
            set(value) = definedExternally
    }

    companion object {
        var Default: Options
    }
}