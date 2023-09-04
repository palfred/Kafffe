package samples

import kafffe.bootstrap.navigation.NavSimpleContainer
import kafffe.bootstrap.navigation.NavType
import kafffe.core.Model
import kafffe.core.NavigationPath
import kafffe.core.modifiers.CssClassModifier.Companion.cssClassModifier

class SimpleNav(path: NavigationPath) : NavSimpleContainer(NavType.tabs, "simplenav") {

    fun nestedNav(navPath: NavigationPath, navType: NavType): NavSimpleContainer =
            NavSimpleContainer(navType, "samples").also {
                nav.cssClassModifier("bg-info txt-info")
                it.nav.cssClassModifier("bg-success txt-success")
                it.add("table", Model.of("Table"), "fas fa-table") { TableSample() }
                it.add("form", Model.of("Form"), "fab fa-wpforms") { FormSample() }
                println("NestedNav $navPath")
                if (navPath.rest.empty) {
                    it.navigateTo(NavigationPath.fromString("samples/table"))
                } else {
                    it.navigateTo(navPath)
                }
            }

    init {
        for (t in NavType.values()) {
            add(t.toString(), Model.of(t.toString())) { p -> nestedNav(p, t) }
        }
        navigateTo(path)
    }
}