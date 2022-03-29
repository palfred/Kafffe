package samples

import kafffe.core.KafffeComponent
import kafffe.core.KafffeHtmlBase
import kafffe.svg.center
import kafffe.svg.radius
import kafffe.svg.*
import kotlin.math.PI
import kotlin.random.Random

class SvgSample : KafffeComponent() {

    override fun KafffeHtmlBase.kafffeHtml() =
        div {
            svg {
                defs {
                    radialGradiant("redGold") {
                        gradiantStop("40%", "gold")
                        gradiantStop("99%", "red")
                    }
                    linearGradiant("greens") {
                        element.setAttribute("gradientTransform", "rotate(45)")
                        gradiantStop("1%", "#004400")
                        gradiantStop("99%", "#AAFFAA")
                    }
                }
                width(1000)
                height(1000)
                circle {
                    center(500, 500)
                    radius(400)
                    stroke("#556677")
                    strokeWidth(33)
                    strokeDashArray(listOf(150, 20, 50, 10))
                    strokeOpacity("50%")
                    element.setAttribute("fill", "wheat")
                }
                rectangle(0, 0, 400, 200) {
                    fill("url('#greens')")
                    fillOpacity("80%")
                    stroke("black")
                    strokeWidth(5)
                }
                text(200, 100, "Test middle") {
                    stroke("red")
                    strokeWidth(2)
                    textAnchor(TextAnchor.middle)
                    withStyle {
                        fontSize = "4rem"
                    }
                }
                g {
                    translate(500, 500)
                    circle {
                        radius(300)
                        stroke("black")
                        strokeWidth(10)
                        fill("url('#redGold')")
                    }
                    text {
                        transform("rotate(45)")
                        element.textContent = "SVG Test"
                        withStyle { fontSize = "2.5rem" }
                        textAnchor(TextAnchor.middle)
                    }
                    val radStart = - PI / 2.0
                    val count = Random.nextInt(2, 15)
                    val radSlice = (2 * PI) / count
                    for (i in 1..count ) {
                        val curStart = radStart + (i - 1) * radSlice
//                        pathDonutSlice(150.0, 498.0, curStart, curStart + radSlice) {
//                            stroke("darkgrey")
//                            strokeWidth(2)
//                            fill("wheat")
//                        }
                        pathDonutSlice(150.0, Random.nextDouble(170.0, 400.0), curStart, curStart + radSlice) {
                            stroke("darkgrey")
                            strokeWidth(3)
                            fill("url('#greens')")
                        }
                    }
                }

            }
        }


}

