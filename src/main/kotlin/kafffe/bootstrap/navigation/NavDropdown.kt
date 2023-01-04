package kafffe.bootstrap.navigation

import kafffe.core.*

/**
 * Dropdown selector for to be put in a navbar
 */
open class NavDropdown(
    val navigationElement: NavigationElement,
    titleModel: Model<String>,
    val iconClasses: String = "",
    val labelClasses: String = ""
) : KafffeComponentWithModel<String>(titleModel) {
    val dropdownId = "nav-${navId()}"

    fun item(titleModel: Model<String>, path: NavigationPath, icon: String = "", labelCssClass: String = "") =
        NavItem(navigationElement, titleModel, path, icon, labelCssClass).also { this.addChild(it) }

    fun item(title: String, path: NavigationPath, icon: String = "", labelCssClass: String = "") =
        NavItem(navigationElement, Model.of(title), path, icon, labelCssClass).also { this.addChild(it) }

    /**
     * Adds a menu divider. Can be used if extending the menu with further items @see addhild
     */
    fun divider() {
        addChild(KafffeComponent.ofKafffeHtml { div { addClass("dropdown-divider") } })
    }

    override fun KafffeHtmlBase.kafffeHtml(): KafffeHtmlOut {
        return div {
            addClass("nav-item")
            addClass("dropdown")
            a {
                withElement {
                    id = dropdownId
                    addClass("nav-link")
                    addClass("dropdown-toggle")
                    href = "#"
                    setAttribute("role", "button")
                    setAttribute("data-bs-toggle", "dropdown")
                    setAttribute("aria-haspopup", "true")
                    setAttribute("aria-expanded", "false")
                }
                if (!iconClasses.isEmpty()) {
                    i {
                        addClass(iconClasses)
                    }
                    text(" ")
                }
                span {
                    addClass(labelClasses)
                    text(model.data)
                }
            }
            div {
                val dropdown = element!!
                withElement {
                    addClass("dropdown-menu bg-secondary text-white")
                    setAttribute("aria-labelledby", dropdownId)
                }
                for (child in children) {
                    add(child.html)
                }
            }
        }
    }

    companion object {
        private var navId: Int = 1
        fun navId() = navId++
    }
}