package kafffe.bootstrap.pagination

import kafffe.core.KafffeComponent
import kafffe.core.KafffeHtml
import kafffe.core.KafffeHtmlBase
import kafffe.core.KafffeHtmlOut
import org.w3c.dom.HTMLUListElement
import kotlin.math.max

class BootstrapPagination(pager: Pager) : KafffeComponent() {
    init {
        pager.changeListeners.add({ this.rerender() })
    }

    var pager: Pager = pager
        set(value) {
            field = value
            value.changeListeners.add({ this.rerender() })
            rerender()
        }

    var maxPageLinks: Int = 6
    var itemWidth: String = "2.7em"

    override fun KafffeHtmlBase.kafffeHtml(): KafffeHtmlOut {
       return ul {
            addClass("pagination")
            firstPageItem()

            // Handle large number of pages only x on each side and some indicator
            var start = max(pager.currentPage - maxPageLinks / 2, 1)
            var end = start + maxPageLinks
            if (end > pager.nofPages) {
                start = max(start - (end - pager.nofPages), 1)
                end = pager.nofPages
            }

            val needStart = start > 1
            if (needStart) start++
            val needEnd = end < pager.nofPages
            if (needEnd) end--
            if (needStart) {
                fillItem()
            }
            for (page in (start..end)) {
                val active = page == pager.currentPage
                pageItem(active, page)
            }
            if (needEnd) {
                fillItem()
            }
            lastPageItem()
        }
    }


    private fun KafffeHtml<HTMLUListElement>.firstPageItem() {
        li {
            addClass("page-item")
            a {
                withElement {
                    href = "#"
                    addClass("page-link")
                    onclick = { pager.first() }
                }
                faIcon("fas", "fa-fast-backward")
            }
        }
    }

    private fun KafffeHtml<HTMLUListElement>.lastPageItem() {
        li {
            addClass("page-item")
            a {
                withElement {
                    href = "#"
                    addClass("page-link")
                    onclick = { pager.last() }
                }
                faIcon("fas", "fa-fast-forward")
            }
        }
    }


    private fun KafffeHtml<HTMLUListElement>.pageItem(active: Boolean, page: Int) {
        li {
            addClass("page-item")
            if (active) addClass("active")
            a {
                withElement {
                    href = "#"
                    addClass("page-link")
                    style.minWidth = itemWidth
                    onclick = { pager.changePage(page) }
                }
                text(page.toString())
            }
        }
    }

    private fun KafffeHtml<HTMLUListElement>.fillItem() {
        li {
            withElement {
                addClass("page-item")
                addClass("page-link")
                addClass("disabled")
                style.minWidth = itemWidth
            }
            text(".")
        }
    }
}