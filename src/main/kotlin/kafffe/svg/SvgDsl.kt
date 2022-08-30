package kafffe.svg

import kotlinx.browser.document
import kotlinx.dom.addClass
import org.w3c.dom.css.CSSStyleDeclaration
import org.w3c.dom.events.Event
import org.w3c.dom.svg.*

interface SvgDslInterface<SvgElementType : SVGElement> {
    val element: SvgElementType
    fun set(attributeName : String, value: String) = element.setAttribute(attributeName, value)
}

interface SvgCenter<T : SVGElement> : SvgDslInterface<T> {
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

interface SvgPosition<T : SVGElement> : SvgDslInterface<T> {
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

interface SvgDimension<T : SVGElement> : SvgDslInterface<T> {
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
interface SvgFill<T : SVGElement> : SvgDslInterface<T> {
    fun fill(paint: String) = set("fill", paint)
    fun fillOpacity(width: String) = set("fill-opacity", width)
    fun fillOpacity(width: Number) = fillOpacity(width.toString())
    fun fillRule(paint: String) = set("fill-rule", paint)
}

interface SvgStroke<T : SVGElement> : SvgDslInterface<T> {
    enum class LineCap { butt, round, square }
    enum class LineJoin(val svgValue: String) { arcs("arcs"),  bevel("bevel"), miter("miter"),  miterClip("miter-clip"), round("round")}

    fun stroke(paint: String) = set("stroke", paint)

    fun strokeDashArray(widths: List<String>) = set("stroke-dasharray", widths.joinToString(" "))
    fun strokeDashArray(widths: List<Number>) = strokeDashArray(widths.map{it.toString()})

    fun strokeDashOffset(width: String) = set("stroke-dashoffset", width)
    fun strokeDashOffset(width: Number) = strokeDashOffset(width.toString())

    fun strokeOpacity(width: String) = set("stroke-opacity", width)
    fun strokeOpacity(width: Number) = strokeOpacity(width.toString())

    fun strokeWidth(width: String) = set("stroke-width", width)
    fun strokeWidth(width: Number) = strokeWidth(width.toString())

    fun strokeLineCap(value: LineCap) = set("stroke-linecap", value.name)
    fun strokeLineJoin(value: LineJoin) = set("stroke-linejoin", value.svgValue)

}

interface SvgTransform<T: SVGElement> : SvgDslInterface<T> {
    fun transform(value: String) = set("transform", value)
    fun translate(x: String, y: String) = set("transform", "translate($x, $y)")
    fun translate(x: Number, y: Number) = translate(x.toString(), y.toString())
}

@Suppress("UNCHECKED_CAST")
fun <C : SVGElement> createSvgElement(tagName: String) : C = document.createElementNS("http://www.w3.org/2000/svg", tagName) as C

typealias SvgDslConsumer<C> = SvgDsl<C>.() -> Unit

open class SvgDsl<T : SVGElement>(override val element: T) : SvgDslInterface<T>, SvgStroke<T>, SvgFill<T>, SvgTransform<T> {
    constructor(tagName : String) : this(createSvgElement(tagName)) {}

    fun <S: SVGElement, C : SvgDsl<S>> addAndApply(child: C, block: C.() -> Unit): C {
        element.appendChild(child.element)
        child.block()
        return child
    }

    fun <C : SVGElement> add(child: SvgDsl<C>): SvgDsl<C> {
        element.appendChild(child.element)
        return child
    }

    fun <C : SVGElement> add(child: C): SvgDsl<C> = add(SvgDsl(child))

    // child creators
    fun <C : SVGElement> createElement(tagName: String, block: SvgDslConsumer<C>): SvgDsl<C> {
        val child: C = createSvgElement(tagName)
        val svgDsl = add(child)
        svgDsl.block()
        return svgDsl
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
    class G() : SvgDsl<SVGGElement>("g"), SvgCenter<SVGGElement> {}
    fun g(block: G.() -> Unit = {}): G = addAndApply(G(), block)

    class Path() : SvgDsl<SVGPathElement>("path"), SvgCenter<SVGPathElement> {
        fun d(d: String) = set("d", d)
    }
    fun path(block: Path.() -> Unit = {}) = addAndApply(Path(), block)
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
        block: SvgDslConsumer<SVGPathElement> = {}
    ) = pathBuild({donutSlice(innerRadius, outerRadius, startAngelRadians, endAngelRadians)}, block)

    class Circle() : SvgDsl<SVGCircleElement>("circle"), SvgCenter<SVGCircleElement> {
        fun radius(r: String) = set("r", r)
        fun radius(r: Number) = radius(r.toString())
    }
    fun circle(block: Circle.() -> Unit = {}) = addAndApply(Circle(), block)
    fun circle(centerX: Number, centerY: Number, radius: Number, block: Circle.() -> Unit = {}) = circle() {
        center(centerX, centerY)
        radius(radius)
        block()
    }

    class Rectangle() : SvgDsl<SVGRectElement>("rect"), SvgPosition<SVGRectElement>, SvgDimension<SVGRectElement> {}
    fun rectangle(block: Rectangle.() -> Unit = {}) = addAndApply(Rectangle(), block)
    fun rectangle(x: Number, y: Number, width: Number, height: Number,  block: Rectangle.() -> Unit = {}) = rectangle {
        pos(x,y)
        dim(width, height)
        block()
    }

    class Text() : SvgDsl<SVGTextElement>("text"), SvgPosition<SVGTextElement> {
        enum class Anchor {start, middle, end}
        fun textAnchor(value: Anchor) = set("text-anchor", value.name)
    }
    fun text(block: Text.() -> Unit = {}) = addAndApply(Text(), block)
    fun text(x: Number, y: Number, textContent: String, block: Text.() -> Unit = {}) = text {
        pos(x,y)
        element.append(textContent)
        block()
    }

    class Defs() : SvgDsl<SVGDefsElement>("defs") {}
    fun defs(block: Defs.() -> Unit = {}) = addAndApply(Defs(), block)

    class LinearGradient() : SvgDsl<SVGLinearGradientElement>("linearGradient") {}
    fun linearGradient(gradientId:String, block: LinearGradient.() -> Unit = {}) = LinearGradient().also{it.element.id = gradientId; addAndApply(it, block) }


    class RadialGradient() : SvgDsl<SVGRadialGradientElement>("radialGradient") {}
    fun radialGradient(gradientId:String, block: RadialGradient.() -> Unit= {}) = RadialGradient().also{it.element.id = gradientId; addAndApply(it, block) }

    class Stop() : SvgDsl<SVGStopElement>("stop") {}
    fun stop(block: Stop.() -> Unit= {}) = addAndApply(Stop(), block)
    fun stop(offset: String, color: String, block: Stop.() -> Unit = {}) = stop {
        set("offset", offset)
        set("stop-color", color)
        block()
    }

}




fun main() {
    var rect = SvgDsl.Rectangle()
    rect.pos(10, 10)
    rect.dim(200, 300)
}