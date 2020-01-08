package kafffe.bootstrap.navigation

import kafffe.bootstrap.BasicColor
import kafffe.bootstrap.ColorStrength
import kafffe.bootstrap.ResponsiveSize
import kafffe.core.*

class Nav(navigationTarget: NavigationElement, val navType: NavType = NavType.bar) : NavContainer(navigationTarget) {
    companion object {
        fun create(navigationTarget: NavigationElement, navType: NavType = NavType.bar, block: Nav.() -> Unit) = Nav(navigationTarget, navType).apply(block)
    }

    fun addExpand(expand: ResponsiveSize) {
        val className = "navbar-expand-${expand}"
        modifiers.add(CssClassModifier(className))
    }

    private var _style: CssClassModifier? = null
    var style: ColorStrength = ColorStrength.normal
        set(value: ColorStrength) {
            field = value
            if (_style != null) {
                modifiers.remove(_style!!)
            }
            val s = CssClassModifier("navbar-${value}")
            modifiers.add(s)
            _style = s
        }

    private var _bg: CssClassModifier? = null
    var background: BasicColor = BasicColor.normal
        set(value: BasicColor) {
            field = value
            if (_bg != null) {
                modifiers.remove(_bg!!)
            }
            val s = CssClassModifier("bg-${value}")
            modifiers.add(s)
            _bg = s
        }

    private var _bgColor: HtmlElementModifier? = null
    var backgroundColor: String = ""
        set(newColor: String) {
            field = newColor
            if (_bgColor != null) {
                modifiers.remove(_bgColor!!)
            }
            val s: HtmlElementModifier = HtmlElementModifier.style {
                backgroundColor = newColor
            }
            modifiers.add(s)
            _bgColor = s
        }

    fun setBgExpand(expand: ResponsiveSize) {
        val className = "navbar-expand-${expand}"
        modifiers.add(CssClassModifier(className))
    }

    override fun KafffeHtmlBase.kafffeHtml() =
            div {
                addClass("nav")
                if (navType != NavType.none) {
                    addClass(navType.cssClass)
                }
                for (child in children) {
                    add(child.html)
                }
            }

    var currentActive: NavElement? = null
        set(element: NavElement?) {
            val oldActive = currentActive
            if (oldActive != null) {
                oldActive.active = false
            }
            element?.active = true
            field = element
        }

    fun currentActivePath(navPath: NavigationPath) {
        visitChildrenRecusive {
            if (this is Item && navPath.startsWith(this.path)) {
                currentActive = this
            }
        }
    }

}
