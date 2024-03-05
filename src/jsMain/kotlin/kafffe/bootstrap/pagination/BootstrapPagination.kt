package kafffe.bootstrap.pagination

import kafffe.core.KafffeComponent
import kafffe.core.KafffeHtml
import kafffe.core.KafffeHtmlBase
import kafffe.core.KafffeHtmlOut
import org.w3c.dom.HTMLUListElement
import kotlin.math.max

class BootstrapPagination(val pager: Pager) : KafffeComponent() {
    init {
        pager.changeListeners.add({ this.rerender() })
        pager.nofPagesChangeListeners.add({ this.rerender() })
    }

    var maxPageLinks: Int = 6
    var itemWidth: String = "0rem"

    var prevNextPage: Boolean = false
    var firstLastPage: Boolean = true

    override fun KafffeHtmlBase.kafffeHtml(): KafffeHtmlOut {
        return ul {
            addClass("pagination")
            if (firstLastPage) {
                firstPageItem()
            }
            if (prevNextPage) {
                prevPageItem()
            }

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
            if (prevNextPage) {
                nextPageItem()
            }
            if (firstLastPage) {
                lastPageItem()
            }
        }
    }

    private fun KafffeHtml<HTMLUListElement>.prevPageItem() {
        if (pager.currentPage > 1) {
            li {
                addClass("page-item")
                a {
                    withElement {
                        href = "#"
                        addClass("page-link")
                        onclick = { pager.prev() }
                    }
                    faIcon("fas", "fa-angle-left")
                }
            }
        }
    }

    private fun KafffeHtml<HTMLUListElement>.nextPageItem() {
        if (pager.currentPage < pager.nofPages) {
            li {
                addClass("page-item")
                a {
                    withElement {
                        href = "#"
                        addClass("page-link")
                        onclick = { pager.next() }
                    }
                    faIcon("fas", "fa-angle-right")
                }
            }
        }
    }

    private fun KafffeHtml<HTMLUListElement>.firstPageItem() {
        if (pager.currentPage > 2) {
            li {
                addClass("page-item")
                a {
                    withElement {
                        href = "#"
                        addClass("page-link")
                        onclick = { pager.first() }
                    }
                    faIcon("fas", "fa-angle-double-left")
                }
            }
        }
    }

    private fun KafffeHtml<HTMLUListElement>.lastPageItem() {
        if (pager.currentPage < (pager.nofPages - 1)) {
            li {
                addClass("page-item")
                a {
                    withElement {
                        href = "#"
                        addClass("page-link")
                        onclick = { pager.last() }
                    }
                    faIcon("fas", "fa-angle-double-right")
                }
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