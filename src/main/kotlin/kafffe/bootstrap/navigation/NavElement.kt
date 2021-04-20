package kafffe.bootstrap.navigation

import kafffe.core.modifiers.CssClassModifier
import kafffe.core.KafffeComponent

open class NavElement() : KafffeComponent() {
    private val activeClass = CssClassModifier("active").apply { add = false }
    var active: Boolean
        get() = activeClass.add
        set(value) {
            activeClass.add = value
            if (isRendered) {
                activeClass.modify(html)
            }
        }

    val nav: Nav? get() = parentOfType(Nav::class)

    init {
        modifiers.add(activeClass)
    }
}