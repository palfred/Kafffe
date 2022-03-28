package kafffe.svg

import org.w3c.dom.svg.SVGCircleElement
import org.w3c.dom.svg.SVGElement
import org.w3c.dom.svg.SVGTextElement

fun <T: SVGElement> KafffeSvg<T>.transform(value: String) = element.setAttribute("transform", value)
fun <T: SVGElement> KafffeSvg<T>.translate(x: String, y: String) = element.setAttribute("transform", "translate($x, $y)")
fun <T: SVGElement> KafffeSvg<T>.translate(x: Number, y: Number) = translate(x.toString(), y.toString())

//--- Fill
fun <T: SVGElement> KafffeSvg<T>.fill(paint: String) = element.setAttribute("fill", paint)
fun <T: SVGElement> KafffeSvg<T>.fillOpacity(width: String) = element.setAttribute("fill-opacity", width)
fun <T: SVGElement> KafffeSvg<T>.fillOpacity(width: Number) = fillOpacity(width.toString())
fun <T: SVGElement> KafffeSvg<T>.fillRule(paint: String) = element.setAttribute("fill-rule", paint)

//---- Stroke
enum class StrokeLineCap { butt, round, square }
enum class StrokeLineJoin(val svgValue: String) { arcs("arcs"),  bevel("bevel"), miter("miter"),  miterClip("miter-clip"), round("round")}

fun <T: SVGElement> KafffeSvg<T>.stroke(paint: String) = element.setAttribute("stroke", paint)

fun <T: SVGElement> KafffeSvg<T>.strokeDashArray(widths: List<String>) = element.setAttribute("stroke-dasharray", widths.joinToString(" "))
fun <T: SVGElement> KafffeSvg<T>.strokeDashArray(widths: List<Number>) = strokeDashArray(widths.map{it.toString()})

fun <T: SVGElement> KafffeSvg<T>.strokeDashOffset(width: String) = element.setAttribute("stroke-dashoffset", width)
fun <T: SVGElement> KafffeSvg<T>.strokeDashOffset(width: Number) = strokeDashOffset(width.toString())

fun <T: SVGElement> KafffeSvg<T>.strokeOpacity(width: String) = element.setAttribute("stroke-opacity", width)
fun <T: SVGElement> KafffeSvg<T>.strokeOpacity(width: Number) = strokeOpacity(width.toString())

fun <T: SVGElement> KafffeSvg<T>.strokeWidth(width: String) = element.setAttribute("stroke-width", width)
fun <T: SVGElement> KafffeSvg<T>.strokeWidth(width: Number) = strokeWidth(width.toString())

fun <T: SVGElement> KafffeSvg<T>.strokeLineCap(value: StrokeLineCap) = element.setAttribute("stroke-linecap", value.name)
fun <T: SVGElement> KafffeSvg<T>.strokeLineJoin(value: StrokeLineJoin) = element.setAttribute("stroke-linejoin", value.svgValue)

//  ---- end Stroke
fun <T: SVGElement> KafffeSvg<T>.width(value: String) = element.setAttribute("width", value)
fun <T: SVGElement> KafffeSvg<T>.width(value: Number) = width(value.toString())
fun <T: SVGElement> KafffeSvg<T>.height(value: String) = element.setAttribute("height", value)
fun <T: SVGElement> KafffeSvg<T>.height(value: Number) = height(value.toString())

fun KafffeSvg<SVGCircleElement>.center(x: String, y: String) {
    withElement {
        setAttribute("cx", x)
        setAttribute("cy", y)
    }
}
fun KafffeSvg<SVGCircleElement>.center(x: Number, y: Number) = center(x.toString(), y.toString())

fun KafffeSvg<SVGCircleElement>.radius(r: String) = element.setAttribute("r", r)
fun KafffeSvg<SVGCircleElement>.radius(r: Number) = radius(r.toString())

enum class TextAnchor {start, middle, end}
fun KafffeSvg<SVGTextElement>.textAnchor(value: TextAnchor) = element.setAttribute("text-anchor", value.name)
