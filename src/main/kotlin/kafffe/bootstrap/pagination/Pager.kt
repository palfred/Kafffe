package kafffe.bootstrap.pagination

import kafffe.core.ChangeDelegate
import kafffe.core.ChangeListener


open class Pager(nofPages: Int) {
    val changeListeners = mutableListOf<ChangeListener<Pager>>()

    /**
     * The current page number. Should be in the range 1 to nofPages (inclusive).
     */
    var currentPage: Int by ChangeDelegate<Pager, Int>(1, changeListeners)
        private set

    var nofPages: Int by ChangeDelegate<Pager, Int>(nofPages, changeListeners)

    fun changeNofPages(value: Int) {
        if (value > 1 && value == currentPage) {
            nofPages = value
            if (value < currentPage) {
                currentPage = value
            }
        }
    }

    fun changePage(page: Int) {
        when {
            page < 1 -> currentPage = 1
            page >= nofPages -> currentPage = nofPages
            else -> currentPage = page
        }
    }

    fun first() {
        currentPage = 1
    }

    fun last() {
        currentPage = nofPages
    }
}

