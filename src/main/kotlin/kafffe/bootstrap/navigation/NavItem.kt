package kafffe.bootstrap.navigation

import kafffe.core.*
import kotlin.dom.addClass

class NavItem(
    val navigationTarget: NavigationElement,
    val titleModel: Model<String>,
    val path: NavigationPath,
    val icon: String = "",
    val labelCssClass: String = ""
) : NavElement() {
    constructor(navigationTarget: NavigationElement, title: String, path: NavigationPath, icon: String = "") :
            this(navigationTarget, Model.of(title), path, icon)

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
                    nav?.currentActive = this@NavItem
                    navigationTarget.navigateTo(path)
                }
            }
            if (!icon.isEmpty()) {
                i() {
                    addClass(icon)
                }
                text(" ")
            }
            span {
                if (labelCssClass.isNotEmpty()) {
                    addClass(labelCssClass)
                }
                text(titleModel.data)
            }
        }

}