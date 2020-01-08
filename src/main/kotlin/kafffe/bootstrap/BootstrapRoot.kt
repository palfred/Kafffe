package kafffe.bootstrap

import kafffe.core.KafffeHtmlBase
import kafffe.core.KafffeHtmlOut
import kafffe.core.RootComponent
import org.w3c.dom.HTMLElement

class BootstrapRoot : RootComponent() {
    init {
        disableHistoryBackward()
        addCss("bootstrap", "css/bootstrap.css")
        addCss("bootstrap-grid", "css/bootstrap-grid.css")
        addCss("bootstrap-reboot", "css/bootstrap-reboot.css")
        addCss("fontawesome", "fontawesome/css/all.css")
        addCss("fontawesome.animate", "fontawesome/animate.css")
//        addJsScriptRef("jquery", "js/jquery-3.3.1.js")
        addJsScriptRef("bootstrap-bundle", "js/bootstrap.bundle.js")
    }

    override fun KafffeHtmlBase.kafffeHtml() =
            div {
                for (child in children) {
                    add(child.html)
                }
            }

}


