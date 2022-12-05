package kafffe.bootstrap.navigation

import kafffe.core.*
import kotlinx.dom.addClass

open class NavContainer(val navigationTarget: NavigationElement) : NavElement() {

    val thisContainer = this

    inner class Brand(
        val title: String,
        val imageUrl: String = "",
        val path: NavigationPath = NavigationPath.fromString("/")
    ) : NavElement() {
        override fun KafffeHtmlBase.kafffeHtml() =
            a {
                withElement {
                    href = "#"
                    addClass("navbar-brand")
                    onclick = {
                        navigationTarget.navigateTo(path)
                    }
                }
                if (!imageUrl.isEmpty()) {
                    img {
                        withElement { src = imageUrl }
                    }
                }
                text(this@Brand.title)
            }

    }

    inner class Toggle(val toggleId: String) : NavElement() {
        override fun KafffeHtmlBase.kafffeHtml() =
            button {
                withElement {
                    addClass("navbar-toggler")
                    type = "button"
                    setAttribute("data-bs-toggle", "collapse")
                    setAttribute("data-target", "#" + toggleId)
                    setAttribute("aria-controls", toggleId)
                    setAttribute("aria-expanded", "false")
                    setAttribute("aria-label", "Toggle navigation")
                }
                span() {
                    addClass("navbar-toggler-icon")
                }
            }
    }

    inner class NavbarNav() : NavContainer(navigationTarget) {
        override fun KafffeHtmlBase.kafffeHtml(): KafffeHtmlOut {
            return div {
                addClass("navbar-nav")
                for (child in children) {
                    add(child.html)
                }
            }
        }
    }

    inner class ToggleBlock(val toggleId: String) : NavContainer(navigationTarget) {
        override fun KafffeHtmlBase.kafffeHtml(): KafffeHtmlOut {
            return div() {
                withElement {
                    id = toggleId
                    addClass("collapse")
                    addClass("navbar-collapse")
                }
                for (child in children) {
                    add(child.html)
                }
            }
        }
    }


    fun item(titleModel: Model<String>, path: NavigationPath, icon: String = "", labelCssClass: String = "") =
        NavItem(navigationTarget, titleModel, path, icon, labelCssClass)
            .apply { thisContainer.addChild(this) }

    fun item(title: String, path: NavigationPath, icon: String = "", labelCssClass: String = "") =
        NavItem(navigationTarget, Model.of(title), path, icon, labelCssClass)
            .apply { thisContainer.addChild(this) }

    fun dropdown(titleModel: Model<String>, path: NavigationPath, icon: String = "", labelCssClass: String = "") =
        NavDropdown(navigationTarget, titleModel, icon, labelCssClass).also { addChild(it) }

    fun toggle(toggleId: String) = Toggle(toggleId).apply { thisContainer.addChild(this) }

    fun toggleBlock(toggleId: String, block: ToggleBlock.() -> Unit): ToggleBlock {
        val tb = ToggleBlock(toggleId)
        tb.block()
        thisContainer.addChild(tb)
        return tb
    }

    fun navbarNav(block: NavbarNav .() -> Unit): NavbarNav {
        val tb = NavbarNav()
        tb.block()
        thisContainer.addChild(tb)
        return tb
    }

    fun brand(title: String, imageUrl: String, path: NavigationPath = NavigationPath.fromString("/")) =
        Brand(title, imageUrl, path).apply { thisContainer.addChild(this) }

}