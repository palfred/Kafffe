package kafffe.core

import kotlinx.browser.document
import kotlinx.browser.window

/**
 * A root component that attach and detach it self to the HTML body
 */
open class RootComponent : KafffeComponent() {

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

