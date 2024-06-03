package kafffe.core

import kafffe.svg.SvgDsl
import org.w3c.dom.*
import org.w3c.dom.css.CSSStyleDeclaration
import org.w3c.dom.events.Event
import kotlinx.browser.document
import kotlinx.dom.addClass
import org.w3c.dom.svg.SVGSVGElement

/**
 * Helps to build HTMLElements directly using kotlinx.browser.document.
 * This makes it easier to embed Kaffee Componets directly in the HTML Dom,
 * where kotlinx.html does not allow to access a "current HTMLElement" during the nested DSL.
 * It is possible to use it ase a wrapper for an existing HTMLElement in order to work with that.
 *
 * There is no enforcement on which child elements that can be created, so invalid HTML may be produced.
 */
typealias KafffeHtmlConsumer<C> = KafffeHtml<C>.() -> Unit

typealias KafffeHtmlOut = KafffeHtml<out HTMLElement>

interface KafffeHtmlOperations {
    fun <C : HTMLElement> createElement(tagName: String, block: KafffeHtmlConsumer<C>): KafffeHtml<C>

    fun <C : HTMLElement> createElement(namespace: String, tagName: String, block: KafffeHtmlConsumer<C>): KafffeHtml<C>

    // HTML tags
    fun a(block: KafffeHtmlConsumer<HTMLAnchorElement> = {}) = createElement("a", block)

    fun a(hrefValue: String, block: KafffeHtmlConsumer<HTMLAnchorElement> = {}) =
        createElement<HTMLAnchorElement>("a", { withElement { href = hrefValue }; this.block()})

    fun abbr(block: KafffeHtmlConsumer<HTMLElement> = {}) = createElement("abbr", block)
    fun acronym(block: KafffeHtmlConsumer<HTMLElement> = {}) = createElement("acronym", block)
    fun address(block: KafffeHtmlConsumer<HTMLElement> = {}) = createElement("address", block)
    fun applet(block: KafffeHtmlConsumer<HTMLAppletElement> = {}) = createElement("applet", block)
    fun area(block: KafffeHtmlConsumer<HTMLAreaElement> = {}) = createElement("area", block)
    fun b(block: KafffeHtmlConsumer<HTMLElement> = {}) = createElement("b", block)
    fun base(block: KafffeHtmlConsumer<HTMLBaseElement> = {}) = createElement("base", block)
    fun bdo(block: KafffeHtmlConsumer<HTMLElement> = {}) = createElement("bdo", block)
    fun big(block: KafffeHtmlConsumer<HTMLElement> = {}) = createElement("big", block)
    fun blockquote(block: KafffeHtmlConsumer<HTMLQuoteElement> = {}) = createElement("blockquote", block)
    fun body(block: KafffeHtmlConsumer<HTMLBodyElement> = {}) = createElement("body", block)
    fun br(block: KafffeHtmlConsumer<HTMLBRElement> = {}) = createElement("br", block)
    fun button(block: KafffeHtmlConsumer<HTMLButtonElement> = {}) = createElement("button", block)
    fun caption(block: KafffeHtmlConsumer<HTMLTableCaptionElement> = {}) = createElement("caption", block)
    fun center(block: KafffeHtmlConsumer<HTMLElement> = {}) = createElement("center", block)
    fun cite(block: KafffeHtmlConsumer<HTMLElement> = {}) = createElement("cite", block)
    fun code(block: KafffeHtmlConsumer<HTMLElement> = {}) = createElement("code", block)
    fun col(block: KafffeHtmlConsumer<HTMLTableColElement> = {}) = createElement("col", block)
    fun colgroup(block: KafffeHtmlConsumer<HTMLTableColElement> = {}) = createElement("colgroup", block)
    fun dd(block: KafffeHtmlConsumer<HTMLElement> = {}) = createElement("dd", block)
    fun del(block: KafffeHtmlConsumer<HTMLModElement> = {}) = createElement("del", block)
    fun dfn(block: KafffeHtmlConsumer<HTMLElement> = {}) = createElement("dfn", block)
    fun dir(block: KafffeHtmlConsumer<HTMLDirectoryElement> = {}) = createElement("dir", block)
    fun div(block: KafffeHtmlConsumer<HTMLDivElement> = {}) = createElement("div", block)
    fun dl(block: KafffeHtmlConsumer<HTMLDListElement> = {}) = createElement("dl", block)
    fun dt(block: KafffeHtmlConsumer<HTMLElement> = {}) = createElement("dt", block)
    fun em(block: KafffeHtmlConsumer<HTMLElement> = {}) = createElement("em", block)
    fun fieldset(block: KafffeHtmlConsumer<HTMLFieldSetElement> = {}) = createElement("fieldset", block)
    fun font(block: KafffeHtmlConsumer<HTMLFontElement> = {}) = createElement("font", block)
    fun form(block: KafffeHtmlConsumer<HTMLFormElement> = {}) = createElement("form", block)
    fun frame(block: KafffeHtmlConsumer<HTMLFrameElement> = {}) = createElement("frame", block)
    fun frameset(block: KafffeHtmlConsumer<HTMLFrameSetElement> = {}) = createElement("frameset", block)
    fun h1(block: KafffeHtmlConsumer<HTMLHeadingElement> = {}) = createElement("h1", block)
    fun h2(block: KafffeHtmlConsumer<HTMLHeadingElement> = {}) = createElement("h2", block)
    fun h3(block: KafffeHtmlConsumer<HTMLHeadingElement> = {}) = createElement("h3", block)
    fun h4(block: KafffeHtmlConsumer<HTMLHeadingElement> = {}) = createElement("h4", block)
    fun h5(block: KafffeHtmlConsumer<HTMLHeadingElement> = {}) = createElement("h5", block)
    fun h6(block: KafffeHtmlConsumer<HTMLHeadingElement> = {}) = createElement("h6", block)
    fun head(block: KafffeHtmlConsumer<HTMLHeadElement> = {}) = createElement("head", block)
    fun hr(block: KafffeHtmlConsumer<HTMLHRElement> = {}) = createElement("hr", block)
    fun html(block: KafffeHtmlConsumer<HTMLHtmlElement> = {}) = createElement("html", block)
    fun i(block: KafffeHtmlConsumer<HTMLElement> = {}) = createElement("i", block)
    fun iframe(block: KafffeHtmlConsumer<HTMLIFrameElement> = {}) = createElement("iframe", block)
    fun img(block: KafffeHtmlConsumer<HTMLImageElement> = {}) = createElement("img", block)
    fun input(block: KafffeHtmlConsumer<HTMLInputElement> = {}) = createElement("input", block)
    fun ins(block: KafffeHtmlConsumer<HTMLModElement> = {}) = createElement("ins", block)
    fun kbd(block: KafffeHtmlConsumer<HTMLElement> = {}) = createElement("kbd", block)
    fun label(block: KafffeHtmlConsumer<HTMLLabelElement> = {}) = createElement("label", block)
    fun legend(block: KafffeHtmlConsumer<HTMLLegendElement> = {}) = createElement("legend", block)
    fun li(block: KafffeHtmlConsumer<HTMLLIElement> = {}) = createElement("li", block)
    fun link(block: KafffeHtmlConsumer<HTMLLinkElement> = {}) = createElement("link", block)
    fun map(block: KafffeHtmlConsumer<HTMLMapElement> = {}) = createElement("map", block)
    fun menu(block: KafffeHtmlConsumer<HTMLMenuElement> = {}) = createElement("menu", block)
    fun meta(block: KafffeHtmlConsumer<HTMLMetaElement> = {}) = createElement("meta", block)
    fun noframes(block: KafffeHtmlConsumer<HTMLElement> = {}) = createElement("noframes", block)
    fun noscript(block: KafffeHtmlConsumer<HTMLElement> = {}) = createElement("noscript", block)
    fun objectTag(block: KafffeHtmlConsumer<HTMLObjectElement> = {}) = createElement("object", block)
    fun ol(block: KafffeHtmlConsumer<HTMLOListElement> = {}) = createElement("ol", block)
    fun optgroup(block: KafffeHtmlConsumer<HTMLOptGroupElement> = {}) = createElement("optgroup", block)
    fun option(block: KafffeHtmlConsumer<HTMLOptionElement> = {}) = createElement("option", block)
    fun p(block: KafffeHtmlConsumer<HTMLParagraphElement> = {}) = createElement("p", block)
    fun param(block: KafffeHtmlConsumer<HTMLParamElement> = {}) = createElement("param", block)
    fun pre(block: KafffeHtmlConsumer<HTMLPreElement> = {}) = createElement("pre", block)
    fun q(block: KafffeHtmlConsumer<HTMLQuoteElement> = {}) = createElement("q", block)
    fun s(block: KafffeHtmlConsumer<HTMLElement> = {}) = createElement("s", block)
    fun samp(block: KafffeHtmlConsumer<HTMLElement> = {}) = createElement("samp", block)
    fun script(block: KafffeHtmlConsumer<HTMLScriptElement> = {}) = createElement("script", block)
    fun select(block: KafffeHtmlConsumer<HTMLSelectElement> = {}) = createElement("select", block)
    fun small(block: KafffeHtmlConsumer<HTMLElement> = {}) = createElement("small", block)
    fun span(block: KafffeHtmlConsumer<HTMLElement> = {}) = createElement("span", block)
    fun strike(block: KafffeHtmlConsumer<HTMLElement> = {}) = createElement("strike", block)
    fun strong(block: KafffeHtmlConsumer<HTMLElement> = {}) = createElement("strong", block)
    fun style(block: KafffeHtmlConsumer<HTMLStyleElement> = {}) = createElement("style", block)
    fun sub(block: KafffeHtmlConsumer<HTMLElement> = {}) = createElement("sub", block)
    fun sup(block: KafffeHtmlConsumer<HTMLElement> = {}) = createElement("sup", block)
    fun table(block: KafffeHtmlConsumer<HTMLTableElement> = {}) = createElement("table", block)
    fun tbody(block: KafffeHtmlConsumer<HTMLTableSectionElement> = {}) = createElement("tbody", block)
    fun td(block: KafffeHtmlConsumer<HTMLTableCellElement> = {}) = createElement("td", block)
    fun textarea(block: KafffeHtmlConsumer<HTMLTextAreaElement> = {}) = createElement("textarea", block)
    fun tfoot(block: KafffeHtmlConsumer<HTMLTableSectionElement> = {}) = createElement("tfoot", block)
    fun th(block: KafffeHtmlConsumer<HTMLTableCellElement> = {}) = createElement("th", block)
    fun thead(block: KafffeHtmlConsumer<HTMLTableSectionElement> = {}) = createElement("thead", block)
    fun title(block: KafffeHtmlConsumer<HTMLTitleElement> = {}) = createElement("title", block)
    fun tr(block: KafffeHtmlConsumer<HTMLTableRowElement> = {}) = createElement("tr", block)
    fun tt(block: KafffeHtmlConsumer<HTMLElement> = {}) = createElement("tt", block)
    fun u(block: KafffeHtmlConsumer<HTMLElement> = {}) = createElement("u", block)
    fun ul(block: KafffeHtmlConsumer<HTMLUListElement> = {}) = createElement("ul", block)
    fun varTag(block: KafffeHtmlConsumer<HTMLElement> = {}) = createElement("var", block)

    fun faIcon(vararg classes: String) = i { for (c in classes) addClass(c) }

}

class KafffeHtmlBase : KafffeHtmlOperations {     // child creators
    override fun <C : HTMLElement> createElement(tagName: String, block: KafffeHtmlConsumer<C>): KafffeHtml<C> {
        @kotlin.Suppress("UNCHECKED_CAST")
        val child = document.createElement(tagName) as C
        KafffeHtml(child).block()
        return KafffeHtml(child)
    }

    override fun <C : HTMLElement> createElement(
        namespace: String,
        tagName: String,
        block: KafffeHtmlConsumer<C>
    ): KafffeHtml<C> {
        @Suppress("UNCHECKED_CAST")
        val child = document.createElementNS(namespace, tagName) as C
        return KafffeHtml(child)
    }
}

class KafffeHtml<T : HTMLElement>(val element: T) : KafffeHtmlOperations {

    companion object {
        val start = KafffeHtmlBase()
    }

    fun <C : HTMLElement> add(child: KafffeHtml<C>): KafffeHtml<C> {
        element.appendChild(child.element)
        return child
    }

    fun <C : HTMLElement> add(child: C): KafffeHtml<C> = add(KafffeHtml(child))

    // child creators
    @Suppress("UNCHECKED_CAST")
    override fun <C : HTMLElement> createElement(tagName: String, block: KafffeHtmlConsumer<C>): KafffeHtml<C> {
        val child = document.createElement(tagName) as C
        val kafffeHtml = add(child)
        kafffeHtml.block()
        return kafffeHtml
    }

    override fun <C : HTMLElement> createElement(namespace: String, tagName: String, block: KafffeHtmlConsumer<C>): KafffeHtml<C> {
        @Suppress("UNCHECKED_CAST")
        val child = document.createElementNS(namespace, tagName) as C
        val kafffeHtml = add(child)
        kafffeHtml.block()
        return kafffeHtml
    }

    /** Do with element if not null */
    inline fun withElement(block: T.() -> Unit) {
        element.block()
    }

    /** Do with element style if not null */
    inline fun withStyle(block: CSSStyleDeclaration.() -> Unit) {
        element.style.block()
    }

    fun addClass(cssClass: String) = element.addClass(cssClass)
    fun removeClass(cssClass: String) = element.classList?.remove(cssClass)

    fun onClick(eventHandler: ((Event) -> dynamic)) {
        element.onclick = eventHandler
    }

    fun text(txt: String) {
        element.appendChild(document.createTextNode(txt))
    }

    fun svg(block: SvgDsl<SVGSVGElement>.() -> Unit = {}): SvgDsl<SVGSVGElement> {
        val svgDsl = SvgDsl<SVGSVGElement>("svg")
        element.appendChild(svgDsl.element)
        svgDsl.block()
        return svgDsl
    }

    /**
     * Shorthand for adding text inside an kafffe html build ie:
     * p { + "some text" }
     * h1 { + "Some heading" }
     */
    operator fun String.unaryPlus() = text(this)
}




