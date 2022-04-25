package kafffe.svg

import kotlinx.browser.document
import kotlinx.dom.addClass
import org.w3c.dom.css.CSSStyleDeclaration
import org.w3c.dom.events.Event
import org.w3c.dom.svg.*

interface KafffeSvg2InterfaceBase<SvgElementType : SVGElement> {
    val element: SvgElementType
    fun set(attributeName : String, value: String) = element.setAttribute(attributeName, value)
}

interface SvgCenter<T : SVGElement> : KafffeSvg2InterfaceBase<T> {
    fun cx(value: String) = set("cx", value)
    fun cx(value: Number) = cx(value.toString())
    fun cy(value: String) = set("cy", value)
    fun cy(value: Number) = cy(value.toString())

    fun center(x: Number, y: Number) {
        cx(x)
        cy(y)
    }

    fun center(x: String, y: String) {
        cx(x)
        cy(y)
    }
}

interface SvgPosition<T : SVGElement> : KafffeSvg2InterfaceBase<T> {
    fun x(value: String) = set("x", value)
    fun x(value: Number) = x(value.toString())
    fun y(value: String) = set("y", value)
    fun y(value: Number) = y(value.toString())

    fun pos(x: Number, y: Number) {
        x(x)
        y(y)
    }

    fun pos(x: String, y: String) {
        x(x)
        y(y)
    }
}

interface SvgDimension<T : SVGElement> : KafffeSvg2InterfaceBase<T> {
    fun width(value: String) = set("width", value)
    fun width(value: Number) = width(value.toString())
    fun height(value: String) = set("height", value)
    fun height(value: Number) = height(value.toString())

    fun dim(width: Number, height: Number) {
        width(width)
        height(height)
    }

    fun dim(width: String, height: String) {
        width(width)
        height(height)
    }
}

@Suppress("UNCHECKED_CAST")
fun <C : SVGElement> createSvgElement(tagName: String) : C = document.createElementNS("http://www.w3.org/2000/svg", tagName) as C

typealias KafffeSvg2Consumer<C> = KafffeSvg2<C>.() -> Unit

open class KafffeSvg2<T : SVGElement>(override val element: T) : KafffeSvg2InterfaceBase<T> {
    constructor(tagName : String) : this(createSvgElement(tagName)) {}

    fun <C : SVGElement> add(child: KafffeSvg2<C>): KafffeSvg2<C> {
        element.appendChild(child.element)
        return child
    }

    fun <C : SVGElement> add(child: C): KafffeSvg2<C> = add(KafffeSvg2(child))

    // child creators
    fun <C : SVGElement> createElement(tagName: String, block: KafffeSvg2Consumer<C>): KafffeSvg2<C> {
        val child: C = createSvgElement(tagName)
        val KafffeSvg2 = add(child)
        KafffeSvg2.block()
        return KafffeSvg2
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
    fun g(block: KafffeSvg2Consumer<SVGGElement> = {}) = createElement("g", block)

    class Path() : KafffeSvg2<SVGPathElement>("path"), SvgCenter<SVGPathElement> {
        fun d(d: String) = set("d", d)
    }
    fun path(block: Path.() -> Unit = {}) = Path().also { add(it); it.block() }
    fun pathFromString(pathSegBuilder: () -> String, block : Path.() -> Unit  = {}) = path {
        d(pathSegBuilder())
        block()
    }
    fun pathBuild(pathSegBuilder: PathSegmentsBuilder.() -> Unit, block : Path.() -> Unit  = {}) =
        pathFromString({val psb = PathSegmentsBuilder(); psb.pathSegBuilder(); psb.build()}, block)

    fun pathDonutSlice(
        innerRadius: Double,
        outerRadius: Double,
        startAngelRadians: Double,
        endAngelRadians: Double,
        block: KafffeSvg2Consumer<SVGPathElement> = {}
    ) = pathBuild({donutSlice(innerRadius, outerRadius, startAngelRadians, endAngelRadians)}, block)

    class Circle() : KafffeSvg2<SVGCircleElement>("circle"), SvgCenter<SVGCircleElement> {
        fun radius(r: String) = set("r", r)
        fun radius(r: Number) = radius(r.toString())
    }
    fun circle(block: Circle.() -> Unit = {}) = Circle().also { add(it); it.block() }
    fun circle(centerX: Number, centerY: Number, radius: Number, block: Circle.() -> Unit = {}) = circle() {
        center(centerX, centerY)
        radius(radius)
        block()
    }

    class Rectangle() : KafffeSvg2<SVGRectElement>("rect"), SvgPosition<SVGRectElement>, SvgDimension<SVGRectElement> {}
    fun rectangle(block: Rectangle.() -> Unit = {}) = Rectangle().also { add(it); it.block() }
    fun rectangle(x: Number, y: Number, width: Number, height: Number,  block: Rectangle.() -> Unit = {}) = rectangle {
        pos(x,y)
        dim(width, height)
        block()
    }

    class Text() : KafffeSvg2<SVGTextElement>("text"), SvgPosition<SVGTextElement> {}
    fun text(block: Text.() -> Unit = {}) = Text().also { add(it); it.block() }
    fun text(x: Number, y: Number, textContent: String, block: Text.() -> Unit = {}) = text {
        pos(x,y)
        element.append(textContent)
        block()
    }

    class Defs() : KafffeSvg2<SVGDefsElement>("defs") {}
    fun defs(block: Defs.() -> Unit = {}) = Defs().also { add(it); it.block() }

    class LinearGradient() : KafffeSvg2<SVGLinearGradientElement>("linearGradient") {}
    fun linearGradient(gradientId:String, block: LinearGradient.() -> Unit = {}) = LinearGradient().also{element.id = gradientId; add(it); it.block() }


    class RadialGradient() : KafffeSvg2<SVGRadialGradientElement>("radialGradient") {}
    fun radialGradient(gradientId:String, block: RadialGradient.() -> Unit= {}) = RadialGradient().also{element.id = gradientId; add(it); it.block() }

    class Stop() : KafffeSvg2<SVGStopElement>("stop") {}
    fun stop(block: Stop.() -> Unit= {}) = Stop().also {add(it); it.block() }
    fun stop(offset: String, color: String, block: Stop.() -> Unit = {}) = stop {
        set("offset", offset)
        set("stop-color", color)
        block()
    }

}




fun main() {
    var rect = KafffeSvg2.Rectangle()
    rect.pos(10, 10)
    rect.dim(200, 300)
}