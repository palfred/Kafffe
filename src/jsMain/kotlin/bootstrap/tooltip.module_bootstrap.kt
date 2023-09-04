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

open external class Tooltip(element: Element, options: Options = definedExternally) : BaseComponent {
    constructor(element: String, options: Options = definedExternally)
    constructor(element: String)
    constructor(element: Element)
    open fun show()
    open fun hide()
    open fun toggle(event: Any = definedExternally)
    open fun enable()
    open fun disable()
    open fun toggleEnabled()
    open fun update()
    open fun setContent(content: Record<String, Any? /* String? | Element? | Tooltip.SetContentFunction? */> = definedExternally)
    interface `T$15` {
        var show: Number
        var hide: Number
    }
    interface Options {
        var animation: Boolean
        var container: dynamic /* String | Element | Boolean */
            get() = definedExternally
            set(value) = definedExternally
        var delay: dynamic /* Number | `T$15` */
            get() = definedExternally
            set(value) = definedExternally
        var html: Boolean
        var placement: dynamic /* "auto" | "top" | "bottom" | "left" | "right" | () -> String */
            get() = definedExternally
            set(value) = definedExternally
        var selector: dynamic /* String | Boolean */
            get() = definedExternally
            set(value) = definedExternally
        var template: String
        var title: dynamic /* String | Element | JQuery | (this: HTMLElement) -> dynamic */
            get() = definedExternally
            set(value) = definedExternally
        var trigger: String /* "click" | "hover" | "focus" | "manual" | "click hover" | "click focus" | "hover focus" | "click hover focus" */
        var offset: dynamic /* JsTuple<Number, Number> | String | OffsetFunction */
            get() = definedExternally
            set(value) = definedExternally
        var fallbackPlacements: Array<String>
        var boundary: dynamic /* Element | Array<Element> | Any */
            get() = definedExternally
            set(value) = definedExternally
        var customClass: dynamic /* String? | (() -> String)? */
            get() = definedExternally
            set(value) = definedExternally
        var sanitize: Boolean
        var allowList: Record<String /* "a" | "abbr" | "address" | "applet" | "area" | "article" | "aside" | "audio" | "b" | "base" | "basefont" | "bdi" | "bdo" | "blockquote" | "body" | "br" | "button" | "canvas" | "caption" | "cite" | "code" | "col" | "colgroup" | "data" | "datalist" | "dd" | "del" | "details" | "dfn" | "dialog" | "dir" | "div" | "dl" | "dt" | "em" | "embed" | "fieldset" | "figcaption" | "figure" | "font" | "footer" | "form" | "frame" | "frameset" | "h1" | "h2" | "h3" | "h4" | "h5" | "h6" | "head" | "header" | "hgroup" | "hr" | "html" | "i" | "iframe" | "img" | "input" | "ins" | "kbd" | "label" | "legend" | "li" | "link" | "main" | "map" | "mark" | "marquee" | "menu" | "meta" | "meter" | "nav" | "noscript" | "object" | "ol" | "optgroup" | "option" | "output" | "p" | "param" | "picture" | "pre" | "progress" | "q" | "rp" | "rt" | "ruby" | "s" | "samp" | "script" | "section" | "select" | "slot" | "small" | "source" | "span" | "strong" | "style" | "sub" | "summary" | "sup" | "table" | "tbody" | "td" | "template" | "textarea" | "tfoot" | "th" | "thead" | "time" | "title" | "tr" | "track" | "u" | "ul" | "var" | "video" | "wbr" | "*" */, Array<dynamic /* String | RegExp */>>
        var sanitizeFn: () -> Unit?
        var popperConfig: dynamic /* Any? | PopperConfigFunction? */
            get() = definedExternally
            set(value) = definedExternally
    }

    companion object {
        var NAME: String /* "tooltip" */
        var Default: Options
        var Event: Record<String /* "CLICK" | "FOCUSIN" | "FOCUSOUT" | "HIDDEN" | "HIDE" | "INSERTED" | "MOUSEENTER" | "MOUSELEAVE" | "SHOW" | "SHOWN" */, String>
        var DefaultType: Record<String /* "animation" | "container" | "delay" | "html" | "placement" | "selector" | "template" | "title" | "trigger" | "offset" | "fallbackPlacements" | "boundary" | "customClass" | "sanitize" | "allowList" | "sanitizeFn" | "popperConfig" */, String>
    }
}