package kafffe.bootstrap.navigation

import kafffe.core.*
import kafffe.core.modifiers.CssClassModifier
import kafffe.core.modifiers.CssClassModifier.Companion.cssClassModifier

open class NavSimpleContainer(navType: NavType = NavType.tabs, val containerName: String = "container") : KafffeComponent() {
    val navigator = NavigationElement.create(containerName)
    val nav = addChild(Nav.create(navigator, navType) {})

    fun add(navId: String, label: Model<String>, iconClases: String = "", block: NavigateToComponent) {
        navigator.component(navId, block)
        nav.item(label, NavigationPath(listOf(containerName, navId)), iconClases)
    }

    val container = addChild(DivContainer().apply { modifiers.add(CssClassModifier("tab-content")) })

    init {
        navigator.componentNavigator = { container.replaceContent(it) }
        if (nav.navType.isVertical) {
            cssClassModifier("d-flex hgap-4")
            nav.cssClassModifier("flex-grow-0")
            container.cssClassModifier("flex-grow-1")
        }
    }

    fun navigateTo(path: NavigationPath) {
        nav.currentActivePath(path)
        navigator.navigateTo(path)
    }

    override fun KafffeHtmlBase.kafffeHtml() =
            div {
                add(nav.html)
                add(container.html)
            }

}