package kafffe.messages

import kafffe.bootstrap.BasicColor
import kafffe.bootstrap.ColorStrength
import kafffe.core.*
import org.w3c.dom.asList
import kotlinx.dom.removeClass

/**
 * Dropdown selector for UI language to be put in a navbar
 */
open class NavLanguageSelector(titleModel: Model<String>, val iconClasses: String = "fas fa-globe") : KafffeComponentWithModel<String>(titleModel) {

    var backgroundClass = BasicColor.secondary.backgroundClass
    var textClass = BasicColor.white.textClass
        /**
         * Adds a menu divider. Can be used if extending the menu with further items @see addhild
         */
        /**
         * Adds a menu divider. Can be used if extending the menu with further items @see addhild
         */
        fun addDivider() {
            addChild(ofKafffeHtml { div { addClass("dropdown-divider") } })
        }

                override fun KafffeHtmlBase.kafffeHtml(): KafffeHtmlOut {
            return div {
                addClass("nav-item")
                addClass("dropdown")
                a {
                    withElement {
                        id = "langDropdown"
                        addClass("nav-link")
                        addClass("dropdown-toggle")
                        href = "#"
                        setAttribute("role", "button")
                        setAttribute("data-toggle", "dropdown")
                        setAttribute("aria-haspopup", "true")
                        setAttribute("aria-expanded", "false")
                    }
                    if (!iconClasses.isEmpty()) {
                        i {
                            addClass(iconClasses)
                        }
                        text(" ")
                    }
                    text(Messages.get().languageSelect)
                }
                div {
                    val dropdown = element!!
                    withElement {
                        addClass("dropdown-menu $backgroundClass $textClass")
                        setAttribute("aria-labelledby", "langDropdown")
                    }
                    for (message in Messages.allLanguages) {
                        val active = (Messages.currentLanguage == message.language)
                        a {
                            withElement {
                                addClass("dropdown-item")
                                if (active) addClass("active")
                                onclick = {
                                    Messages.currentLanguage = message.language
                                    messagesModel.changed()
                                    root()?.rerenderRecursive()
                                    for (activeElm in dropdown.getElementsByClassName("active").asList()) {
                                        activeElm.removeClass("active")
                                    }
                                    this@a.addClass("active")
                                }
                            }
                            img {
                                withElement {
                                    src = message.smallFlag
                                    alt = "Flag for ${message.languageName}"
                                }
                            }
                            text(" ")
                            text(message.languageName)
                        }
                    }
                    for (child in children) {
                        add(child.html)
                    }
                }
            }
        }
}