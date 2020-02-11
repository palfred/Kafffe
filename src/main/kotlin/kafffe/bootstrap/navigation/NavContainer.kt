package kafffe.bootstrap.navigation

import kafffe.core.*
import kotlin.dom.addClass

open class NavContainer(val navigationTarget: NavigationElement) : NavElement() {

    val thisContainer = this

    inner class Item(val titleModel: Model<String>, val path: NavigationPath, val icon: String = "") : NavElement() {
        constructor(title: String, path: NavigationPath, icon: String = "") : this(Model.of(title), path, icon)

        override fun attach() {
            super.attach()
            titleModel.listeners.add(onModelChanged)
        }

        override fun detach() {
            super.detach()
            titleModel.listeners.remove(onModelChanged)
        }

        val onModelChanged = ModelChangeListener(::rerender)

        override fun KafffeHtmlBase.kafffeHtml() =
                a {
                    withElement {
                        href = "#"
                        if (!(nav?.navType?.isPills ?: true)) {
                            addClass("nav-item")
                        }
                        addClass("nav-link")
                        onclick = {
                            nav?.currentActive = this@Item
                            navigationTarget.navigateTo(path)
                        }
                    }
                    if (!icon.isEmpty()) {
                        i() {
                            addClass(icon)
                        }
                        text(" ")
                    }
                    text(titleModel.data)
                }

    }

    inner class Brand(val title: String, val imageUrl: String = "", val path: NavigationPath = NavigationPath.fromString("/")) : NavElement() {
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
                        setAttribute("data-toggle", "collapse")
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


    fun item(titleModel: Model<String>, path: NavigationPath, icon: String = "") = Item(titleModel, path, icon).apply { thisContainer.addChild(this) }
    fun item(title: String, path: NavigationPath, icon: String = "") = Item(title, path, icon).apply { thisContainer.addChild(this) }

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

    fun brand(title: String, imageUrl: String, path: NavigationPath = NavigationPath.fromString("/")) = Brand(title, imageUrl, path).apply { thisContainer.addChild(this) }

}