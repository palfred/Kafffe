package kafffe.bootstrap.pagination

import kafffe.core.KafffeComponent
import kafffe.core.KafffeHtml
import kafffe.core.KafffeHtmlBase
import kafffe.core.KafffeHtmlOut
import kafffe.messages.MessagesObject
import org.w3c.dom.HTMLUListElement
import kotlin.math.max
import kotlin.math.min

class BootstrapPagination(val pager: Pager) : KafffeComponent() {
    init {
        pager.changeListeners.add({ this.rerender() })
        pager.nofPagesChangeListeners.add({ this.rerender() })
    }

    var maxPageLinks: Int = 6
    var itemWidth: String = "0rem"

    var prevNextPage: Boolean = false
    var firstLastPage: Boolean = true

    var includePageSizeSelector: Boolean = false
    var pageSizes: MutableList<Int> = mutableListOf(5, 10, 20, 40, 80)
    var pageSizeLabel: String = MessagesObject.get().pageSize

    var includePageInfo: Boolean = false

    var pageInfoText: String = "<start>-<end> ${MessagesObject.get().countOutOf} <total>"

    override fun KafffeHtmlBase.kafffeHtml(): KafffeHtmlOut {
        return div {
            addClass("d-flex align-items-baseline")
            if (includePageSizeSelector) {
                div {
                    addClass("me-2 input-group")
                    withStyle {
                        width = "auto"
                    }
                    label {
                        addClass("input-group-text")
                        text(pageSizeLabel)
                    }
                    select {
                        addClass("form-select me-4")
                        withElement {
                            onchange = {
                                pager.pageSize = value.toInt()
                                true
                            }
                            style.width = "auto"
                        }
                        val pageSizes: List<Int> = (pageSizes.toSet() + pager.pageSize).sorted()
                        for (pageSize in pageSizes) {
                            option {
                                withElement {
                                    value = pageSize.toString()
                                    text(pageSize.toString())
                                    selected = (pageSize == pager.pageSize)
                                }
                            }
                        }
                    }
                }
            }
            if (includePageInfo) {
                div {
                    addClass("me-2")
                    val offset = (pager.currentPage -1) * pager.pageSize
                    val start = (offset + 1).toString()
                    val end = (min(offset + pager.pageSize, pager.totalCount)).toString()
                    text(pageInfoText
                        .replace("<start>", start)
                        .replace("<end>", end)
                        .replace("<total>", pager.totalCount.toString())
                    )
                }
            }
            ul {
                addClass("pagination ms-auto")
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