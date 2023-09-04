package kafffe.bootstrap

import kafffe.core.modifiers.CssClassModifier

enum class ResponsiveSize {
    sm, md, lg, xl;

    /**
     * Produce a ColWidth to be iuse in grid rows
     */
    fun col(span: Int = 0) = ColWidth(this, span)
    fun width(span: Int = 0) = ColWidth(this, span)
}

enum class ColorStrength { dark, normal, light }

enum class BasicColor(val cssName: String, val iconClasses: String) {
    primary("primary", "fas"),
    secondary("secondary", "fas"),
    success("success", "far fa-thumbs-up"),
    danger("danger", "fas fa-exclamation-triangle"),
    error("danger", "fas fa-exclamation-triangle"),
    warning("warning", "fas fa-exclamation-circle"),
    info("info", "fas fa-info-circle"),
    light("light", "fas"),
    dark("dark", "fas"),
    normal("normal", "fas"),
    white("white", "fas"),
    black("black", "fas");

    val backgroundClass = "bg-$cssName"
    val textClass = "text-$cssName"
    val btnClass = "btn-$cssName"
    val btnOutlineClass = "btn-outline-$cssName"
    val alertClass = "alert-$cssName"

    val backgroundModifer = CssClassModifier(backgroundClass)
    val textModifer = CssClassModifier(textClass)

    val btnModifier = CssClassModifier(btnClass)
    val btnOutlineModifier = CssClassModifier(btnOutlineClass)
}

typealias BootstrapLevel = BasicColor

data class ColWidth(val responsizeSize: ResponsiveSize, val numCols: Int = 0) {
    private val colspan: String = if (numCols in (1..12)) "-${numCols}" else ""
    val cssClass = "col-${responsizeSize}$colspan"
    val cssClassModifer = CssClassModifier(cssClass)

}
