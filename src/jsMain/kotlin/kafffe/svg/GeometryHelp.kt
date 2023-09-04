package kafffe.svg

import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

object GeometryHelp {
     fun degreeToRadians(degree: Double) : Double = degree * PI / 180.0
     fun polarToCartesian(radiansAngle: Double, length: Double) = Pair(cos(radiansAngle) * length, sin(radiansAngle) * length)
}