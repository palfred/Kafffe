package kafffe.bootstrap.form

import kafffe.bootstrap.external.PopoverOption
import kafffe.bootstrap.external.bsJquery
import kafffe.bootstrap.external.jsCreate
import kafffe.core.HtmlElementModifier
import kafffe.core.KafffeHtml
import org.w3c.dom.HTMLElement

/**
 * Test of popover (bootstrap.bundle js modified to placement in place of attachement, whcih did not support bottom-start)
 *  May later be used for sigle and multi select edits to allow drop down or up depending of available space.
 */
class AutocompleteModifier : HtmlElementModifier {
    override fun modify(element: HTMLElement) {

        val dropdown = KafffeHtml.start.div {
            ul {
                li { text("test 1") }
                li { text("test 2") }
                li { text("test 3") }
                li { text("test 4") }
                li { text("test 5") }
                li { text("test 6") }
            }
        }.element!!

        val options: PopoverOption = jsCreate()
        options.apply {
            content = dropdown
            trigger = "focus"
            html = true
            placement  = "bottom-start"

        }
        bsJquery(element).popover(options)
    }
}

