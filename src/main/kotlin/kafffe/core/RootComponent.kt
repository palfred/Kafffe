package kafffe.core

import kotlin.browser.document
import kotlin.browser.window

/**
 * A root component that roughly corresponds to a HTML page, or could be used as base for SinglePage APP.
 * Can be used to control included Scripts and CSS.
 */
open class RootComponent : KafffeComponent() {
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
                with(element!!) {
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
                with(element!!) {
                    src = "$href#${ident}"
                    type = "text/javascript"
                }
            }
        }
        scriptIds.add(ident)
    }

    /**
     * Attaches the html to the Browser DOM = Show the HTML of this RootComponent
     */
    override fun attach() {
        super.attach()
        document.body!!.appendChild(html)
        attachChildrenRecursive()
    }

    /**
     * Detaches the html from the Browser DOM = Show the HTML of this RootComponent
     */
    override fun detach() {
        if (isRendered) {
            document.body!!.removeChild(html)
        }
        super.detach()
    }
}

