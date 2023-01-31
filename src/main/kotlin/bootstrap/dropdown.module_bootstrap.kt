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

external open class Dropdown : BaseComponent {
    constructor(element: String, options: OptionsPartial = definedExternally)
    constructor(element: String)
    constructor(element: Element, options: OptionsPartial = definedExternally)
    constructor(element: Element)
    open fun toggle()
    open fun show()
    open fun hide()
    open fun update()
    interface Options {
        var offset: dynamic /* JsTuple<Number, Number> | String | OffsetFunction */
            get() = definedExternally
            set(value) = definedExternally
        var boundary: dynamic /* Element | Array<Element> | Any */
            get() = definedExternally
            set(value) = definedExternally
        var reference: dynamic /* "toggle" | "parent" | Element | Popper.Rect */
            get() = definedExternally
            set(value) = definedExternally
        var display: String /* "dynamic" | "static" */
        var popperConfig: dynamic /* Any? | PopperConfigFunction? */
            get() = definedExternally
            set(value) = definedExternally
        var autoClose: dynamic /* Boolean | "inside" | "outside" */
            get() = definedExternally
            set(value) = definedExternally
    }
    interface OptionsPartial {
        var offset: dynamic /* JsTuple<Number, Number> | String? | OffsetFunction? */
            get() = definedExternally
            set(value) = definedExternally
        var boundary: dynamic /* Element? | Array<Element>? | Any? */
            get() = definedExternally
            set(value) = definedExternally
        var reference: dynamic /* "toggle" | "parent" | Element? | Popper.Rect? */
            get() = definedExternally
            set(value) = definedExternally
        var display: String? /* "dynamic" | "static" */
            get() = definedExternally
            set(value) = definedExternally
        var popperConfig: dynamic /* Partial<Popper.Options>? | PopperConfigFunction? */
            get() = definedExternally
            set(value) = definedExternally
        var autoClose: dynamic /* Boolean? | "inside" | "outside" */
            get() = definedExternally
            set(value) = definedExternally
    }

    companion object {
        var Default: Options
        var DefaultType: Record<String /* "offset" | "boundary" | "reference" | "display" | "popperConfig" | "autoClose" */, String>
    }
}