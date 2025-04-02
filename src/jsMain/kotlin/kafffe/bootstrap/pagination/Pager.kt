package kafffe.bootstrap.pagination

import kafffe.core.ChangeDelegate
import kafffe.core.ChangeListener
import kotlin.math.ceil
import kotlin.math.max


open class Pager(nofPages: Int, pageSize: Int = 10) {
    val changeListeners = mutableListOf<ChangeListener<Pager>>()
    val nofPagesChangeListeners = mutableListOf<ChangeListener<Pager>>()

    var pageSize: Int = pageSize
        get() = field
        set(value) {
            field = value
            recalcPages()
        }

    protected fun recalcPages() {
        nofPages = max(ceil(totalCount.toDouble() / pageSize.toDouble()).toInt(), 1)
        if (currentPage > nofPages) {
            last()
        }
        updatePageModel()
    }

    open var totalCount: Int = 0

    open fun updatePageModel() {}

    /**
     * The current page number. Should be in the range 1 to nofPages (inclusive).
     */
    var currentPage: Int by ChangeDelegate<Pager, Int>(1, changeListeners)
        private set

    var nofPages: Int by ChangeDelegate<Pager, Int>(nofPages, nofPagesChangeListeners)

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

    fun prev() {
        if (currentPage > 1) {
            currentPage -= 1
        }
    }

    fun next() {
        if (currentPage < nofPages) {
            currentPage += 1
        }
    }

}

