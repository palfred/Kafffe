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

external open class Carousel : BaseComponent {
    constructor(element: String, options: OptionsPartial = definedExternally)
    constructor(element: String)
    constructor(element: Element, options: OptionsPartial = definedExternally)
    constructor(element: Element)
    open fun cycle()
    open fun pause(event: Any = definedExternally)
    open fun prev()
    open fun next()
    open fun nextWhenVisible()
    open fun to(index: Number)
    interface Options {
        var interval: dynamic /* Number | Boolean */
            get() = definedExternally
            set(value) = definedExternally
        var keyboard: Boolean
        var pause: dynamic /* "hover" | Boolean */
            get() = definedExternally
            set(value) = definedExternally
        var ride: dynamic /* "carousel" | Boolean */
            get() = definedExternally
            set(value) = definedExternally
        var wrap: Boolean
        var touch: Boolean
    }
    interface OptionsPartial {
        var interval: dynamic /* Number? | Boolean? */
            get() = definedExternally
            set(value) = definedExternally
        var keyboard: Boolean?
            get() = definedExternally
            set(value) = definedExternally
        var pause: dynamic /* "hover" | Boolean? */
            get() = definedExternally
            set(value) = definedExternally
        var ride: dynamic /* "carousel" | Boolean? */
            get() = definedExternally
            set(value) = definedExternally
        var wrap: Boolean?
            get() = definedExternally
            set(value) = definedExternally
        var touch: Boolean?
            get() = definedExternally
            set(value) = definedExternally
    }
    interface Event {
        var direction: String /* "left" | "right" */
        var relatedTarget: Element
        var from: Number
        var to: Number
    }

    companion object {
        var Default: Options
        var carouselInstance: Any
    }
}