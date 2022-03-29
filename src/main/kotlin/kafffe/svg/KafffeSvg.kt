package kafffe.svg

import org.w3c.dom.css.CSSStyleDeclaration
import org.w3c.dom.events.Event
import kotlinx.browser.document
import kotlinx.dom.addClass
import org.w3c.dom.svg.*

/**
 * Helps to build SVGElements directly using kotlinx.browser.document.
 * Curretnly only a subset of the SVG elements is supported this is Proof of Concept
 */
typealias KafffeSvgConsumer<C> = KafffeSvg<C>.() -> Unit

typealias KafffeSvgBase = KafffeSvg<SVGElement>
typealias KafffeSvgOut = KafffeSvg<out SVGElement>

class KafffeSvg<T : SVGElement>(val element: T) {

    fun <C : SVGElement> add(child: KafffeSvg<C>): KafffeSvg<C> {
        element.appendChild(child.element)
        return child
    }

    fun <C : SVGElement> add(child: C): KafffeSvg<C> = add(KafffeSvg(child))

    // child creators
    @Suppress("UNCHECKED_CAST")
    fun <C : SVGElement> createElement(tagName: String, block: KafffeSvgConsumer<C>): KafffeSvg<C> {
        val child = document.createElementNS("http://www.w3.org/2000/svg", tagName) as C
        val kafffeSvg = add(child)
        kafffeSvg.block()
        return kafffeSvg
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
    fun removeClass(cssClass: String) = element.classList.remove(cssClass)

    fun onClick(eventHandler: ((Event) -> dynamic)) {
        element.onclick = eventHandler
    }


    // SVG tags
    fun g(block: KafffeSvgConsumer<SVGGElement> = {}) = createElement("g", block)

    fun path(block: KafffeSvgConsumer<SVGPathElement> = {}) = createElement("path", block)
    fun pathFromString(pathSegBuilder: () -> String, block : KafffeSvgConsumer<SVGPathElement> = {}) = path {
        element.setAttribute("d", pathSegBuilder())
        block()
    }
    fun pathBuild(pathSegBuilder: PathSegmentsBuilder.() -> Unit, block : KafffeSvgConsumer<SVGPathElement> = {}) = path {
        val psb = PathSegmentsBuilder()
        psb.pathSegBuilder()
        element.setAttribute("d", psb.build())
        block()
    }
    fun pathDonutSlice(
        innerRadius: Double,
        outerRadius: Double,
        startAngelRadians: Double,
        endAngelRadians: Double,
        block: KafffeSvgConsumer<SVGPathElement> = {}
    ) = pathBuild({donutSlice(innerRadius, outerRadius, startAngelRadians, endAngelRadians)}, block)

    fun circle(block: KafffeSvgConsumer<SVGCircleElement> = {}) = createElement("circle", block)
    fun circle(centerX: Number, centerY: Number, radius: Number, block: KafffeSvgConsumer<SVGCircleElement> = {}) = circle() {
        center(centerX, centerY)
        radius(radius)
        block()
        }

    fun rectangle(block: KafffeSvgConsumer<SVGRectElement> = {}) = createElement("rect", block)
    fun rectangle(x: Number, y: Number, width: Number, height: Number,  block: KafffeSvgConsumer<SVGRectElement> = {}) = rectangle() {
        pos(x,y)
        dim(width, height)
        block()
    }

    fun text(block: KafffeSvgConsumer<SVGTextElement> = {}) = createElement("text", block)
    fun text(x: Number, y: Number, textContent: String, block: KafffeSvgConsumer<SVGTextElement> = {}) = text {
        pos(x,y)
        element.append(textContent)
        block()
    }

    fun defs(block: KafffeSvgConsumer<SVGDefsElement> = {}) = createElement("defs", block)

    fun linearGradiant(gradiantId:String, block: KafffeSvgConsumer<SVGLinearGradientElement> = {}) = createElement("linearGradient", block).apply{element.id = gradiantId}
    fun radialGradiant(gradiantId:String, block: KafffeSvgConsumer<SVGRadialGradientElement> = {}) = createElement("radialGradient", block).apply{element.id = gradiantId}
    fun gradiantStop(block: KafffeSvgConsumer<SVGStopElement> = {}) = createElement("stop", block)
    fun gradiantStop(offset: String, color: String, block: KafffeSvgConsumer<SVGStopElement> = {}) = gradiantStop {
        element.setAttribute("offset", offset)
        element.setAttribute("stop-color", color)
        block()
    }

}




