package kafffe.svg

class PathSegmentsBuilder {
    val segments = mutableListOf<String>()

    fun moveTo(x: Number, y: Number) = segments.add("M $x,$y")
    fun moveToRel(x: Number, y: Number) = segments.add("m $x,$y")
    fun lineTo(x: Number, y: Number) = segments.add("L $x,$y")
    fun lineToRel(x: Number, y: Number) = segments.add("l $x,$y")

    fun arc(
        xRadius: Number,
        yRadius: Number,
        xAxisRotation: Number,
        largeFlag: Boolean,
        sweepFlag: Boolean,
        x: Number,
        y: Number
    ) = segments.add("A $xRadius, $yRadius, $xAxisRotation, ${if (largeFlag) 1 else 0}, ${if (sweepFlag) 1 else 0}, $x,$y")

    fun closePath() = segments.add("Z")
    // TODO curves, horizontal line, ...

    fun build(): String = segments.joinToString(" ")

    fun donutSlice(
        innerRadius: Double,
        outerRadius: Double,
        startAngelRadians: Double,
        endAngelRadians: Double
    ) {
        val (xInnerStart, yInnerStart) = GeometryHelp.polarToCartesian(startAngelRadians,innerRadius)
        val (xOuterStart, yOuterStart) = GeometryHelp.polarToCartesian(startAngelRadians,outerRadius)
        val (xInnerEnd, yInnerEnd) = GeometryHelp.polarToCartesian(endAngelRadians,innerRadius)
        val (xOuterEnd, yOuterEnd) = GeometryHelp.polarToCartesian(endAngelRadians,outerRadius)
        moveTo(xInnerStart, yInnerStart)
        lineTo(xOuterStart, yOuterStart)
        arc(outerRadius, outerRadius, 0, false, true, xOuterEnd, yOuterEnd)
        lineTo(xInnerEnd, yInnerEnd)
        arc(innerRadius, innerRadius, 0, false, false, xInnerStart, yInnerStart)
        closePath()
    }
}

