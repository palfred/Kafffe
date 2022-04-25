package samples

import kafffe.core.KafffeComponent
import kafffe.core.KafffeHtmlBase
import kafffe.svg.*
import kotlin.math.PI
import kotlin.random.Random

class SvgSample : KafffeComponent() {

    override fun KafffeHtmlBase.kafffeHtml() =
        div {
            svg {
                defs {
                    radialGradient("redGold") {
                        stop("40%", "gold")
                        stop("99%", "red")
                    }
                    linearGradient("redGold2") {
                        set("gradientTransform", "rotate(90)")
                        stop("1%", "red")
                        stop("50%", "gold")
                        stop("90%", "pink")
                    }
                    linearGradient("greens") {
                        set("gradientTransform", "rotate(45)")
                        stop("1%", "#004400")
                        stop("99%", "#AAFFAA")
                    }
                }
                set("width", "1000")
                set("height", "1000")
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
                    textAnchor(SvgDsl.Text.Anchor.middle)
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
                        val thisText: SvgDsl.Text = this
                        var rotate = 45
                        element.onclick = {
                            rotate += 10
                            if (rotate > 365) rotate = rotate - 365
                            thisText.transform("rotate($rotate)")
                        }
                        transform("rotate(45)")
                        element.textContent = "SVG Test"
                        withStyle { fontSize = "2.5rem" }
                        textAnchor(SvgDsl.Text.Anchor.middle)
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
                            element.onclick = { rerender()}
                            stroke("darkgrey")
                            strokeWidth(3)
                            if (i % 2 == 0)
                                fill("url('#greens')")
                            else
                                fill("url('#redGold2')")
                        }
                    }
                }

            }
        }


}

