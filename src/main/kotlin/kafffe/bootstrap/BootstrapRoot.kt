package kafffe.bootstrap

import kafffe.core.KafffeApplication
import kafffe.core.KafffeHtmlBase
import kafffe.core.RootComponent

class BootstrapRoot : RootComponent() {
    init {
        KafffeApplication.apply {
            disableHistoryBackward()
            addCss("bootstrap", "css/bootstrap.css")
            addCss("bootstrap-grid", "css/bootstrap-grid.css")
            addCss("bootstrap-reboot", "css/bootstrap-reboot.css")
            addCss("fontawesome", "fontawesome/css/all.css")
            // The  following needs to be loaded before this
//        addJsScriptRef("jquery", "js/jquery-3.6.2.js")
//        addJsScriptRef("bootstrap-bundle", "js/bootstrap.bundle.js")
        }
    }

    override fun KafffeHtmlBase.kafffeHtml() =
        div {
            for (child in children) {
                add(child.html)
            }
        }

}


