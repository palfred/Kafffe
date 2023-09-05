package kafffe.core

import kotlinx.browser.document
import kotlinx.browser.window

/**
 * This application object gives access to "global" function on the HTML document.
 */
object KafffeApplication {
    /** The current rootcomponent allows access with out traversing parent components, which may not yet be established */
    private val scriptIds = mutableSetOf<String>()
    private val cssIds = mutableSetOf<String>()

    fun disableHistoryBackward() {
        window.history.pushState(null, document.title, window.location.href);
        window.onpopstate = { window.history.pushState(null, document.title, window.location.href) }
    }

    fun addCss(ident: String, href_: String) {
        if (cssIds.contains(ident)) return
        KafffeHtml(document.head!!).apply {
            link {
                with(element) {
                    id = ident
                    href = href_
                    type = "text/css"
                    rel = "Stylesheet"
                }
            }
            cssIds.add(ident)
        }
    }

    fun addJsScriptRef(ident: String, href: String) {
        if (scriptIds.contains(ident)) return
        KafffeHtml(document.head!!).apply {
            script {
                with(element) {
                    src = "$href#${ident}"
                    type = "text/javascript"
                }
            }
        }
        scriptIds.add(ident)
    }

}