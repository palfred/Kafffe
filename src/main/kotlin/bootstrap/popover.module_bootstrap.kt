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
import tsstdlib.Record

open external class Popover(element: Element, options: Options = definedExternally) : BaseComponent {
//    constructor(element: String, options: Options = definedExternally)
//    constructor(element: String)
//    constructor(element: Element)
    open fun show()
    open fun hide()
    open fun toggle()
    open fun enable()
    open fun disable()
    open fun toggleEnabled()
    open fun update()
    open fun setContent(content: Record<String, Any? /* String? | Element? | Tooltip.SetContentFunction? */> = definedExternally)
    interface Options : Tooltip.Options {
        var content: dynamic /* String | Element | JQuery | (this: HTMLElement) -> dynamic */
            get() = definedExternally
            set(value) = definedExternally
    }

    companion object {
        var NAME: String /* "popover" */
        var Default: Options
    }
}