package kafffe.bootstrap

import kafffe.core.CssClassModifier
import kafffe.core.DivContainer

enum class BootstrapContainerClass(val cssClass: String) {
    plain("container"),
    fluid("container-fluid");

    val modifier = CssClassModifier(cssClass)
}

/**
 * Bootstrap DivContainer intended for a single content element
 */
class BootstrapContainer(val cssClass: BootstrapContainerClass) : DivContainer() {
    companion object {
        fun fluid() = BootstrapContainer(BootstrapContainerClass.fluid)
        fun plain() = BootstrapContainer(BootstrapContainerClass.plain)
    }

    init {
        modifiers.add(cssClass.modifier)
    }

}
