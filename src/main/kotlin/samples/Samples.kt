package samples

import kafffe.bootstrap.*
import kafffe.bootstrap.ResponsiveSize.md
import kafffe.bootstrap.navigation.Nav
import kafffe.core.*
import kafffe.core.modifiers.CssClassModifier.Companion.cssClassModifier
import kafffe.core.modifiers.HtmlElementModifier
import kafffe.messages.*
import org.w3c.dom.DOMPoint
import org.w3c.dom.HTMLImageElement
import org.w3c.dom.get
import kotlinx.browser.window

fun initServices() {
    ServiceRegistry.register("msg_en", Messages_en())
    ServiceRegistry.register("msg_da", Messages_da())
}

fun addStuff() {
    initServices()

    window.onload = {
        val rootNavigation = NavigationElement.create("root") {
            component("config") {
                bootstrapRowComponent {
                    cssClassModifier("mt-")
                    val labelModel = Model.of("Here goes result")

                    val btn1 = BootstrapButton(Model.of("Open Dialog"), {
                        labelModel.data = "Here goes"
                        Modal.confirm(Model.of("Please Confirm"), Model.of("Is this a dialog ?")) {
                            println("Yes Pressed")
                            labelModel.data = "Yes pressed"
                        }
                    }
                    )
                    cell(btn1, md.col(2))

                    val btn2 = BootstrapButton(Model.of("Open Dialog Position at Button"), onClick = { thisBtn: BootstrapButton ->
                        labelModel.data = "Here goes"
                        val btnElement = thisBtn.html
                        val btnRect = btnElement.getBoundingClientRect()
                        val top = btnRect.top + btnElement.clientHeight
                        val left = btnRect.left
                        Modal.confirm(Model.of("Please Confirm"), Model.of("Is this a position dialog ?"), absolutePosition = DOMPoint(left, top)) {
                            println("Yes Pressed")
                            labelModel.data = "Yes pressed"
                        }
                    }
                    )
                    cell(btn2, md.col(2))

                    cell(Label(labelModel), md.col())
                }
            }
            component("simplenav") { path: NavigationPath -> SimpleNav(path) }
            component("tablesample") { TableSample() }
            component("formsample") { FormSample() }
            component("calendarsample") { CalendarSample() }
            component("svgsample") { SvgSample() }
        }
        BootstrapRoot().apply {
            val container = BootstrapContainer.fluid()
            rootNavigation.componentNavigator = { container.replaceContent(it) }
            addChild(Nav.create(rootNavigation) {
                addExpand(ResponsiveSize.md)
                style = ColorStrength.dark
                //backgroundColor = "#4bb7e5"
                background = BasicColor.primary
                brand("", "images/BrandLogo.png").apply { modifiers.add(HtmlElementModifier.create { (this.getElementsByTagName("img")[0] as HTMLImageElement).style.maxHeight = "3rem" }) }
                toggle("menuToggle")
                toggleBlock("menuToggle") {
                    item(Model.of("Config"), NavigationPath.fromString("root/config"), "fas fa-cogs")
                    item(Model.of("Table Sample 1"), NavigationPath.fromString("root/tablesample"), "fas fa-table")
                    item(Model.of("Form Sample 1"), NavigationPath.fromString("root/formsample"), "fab fa-wpforms")
                    item(Model.of("Table Sample Nest"), NavigationPath.fromString("root/simplenav/tabs/table"), "fas fa-table")
                    item(Model.of("Form Sample Nest"), NavigationPath.fromString("root/simplenav/pills/form"), "fab fa-wpforms")
                    item(Model.of("Calendar"), NavigationPath.fromString("root/calendarsample"), "fas fa-calendar")
                    item(Model.of("SVG"), NavigationPath.fromString("root/svgsample"), "fas fa-chart-pie")
                    addChild(NavLanguageSelector(i18nText(Messages::languageSelect)))
                }
            })
            addChild(container)
            attach()
        }
    }
}

