package kafffe.bootstrap.pagination

import kafffe.core.ChangeDelegate
import kafffe.core.ChangeListener
import kotlin.math.ceil
import kotlin.math.max
import kotlin.math.min


open class Pager(nofPages: Int, pageSize: Int = 10) {
    val changeListeners = mutableListOf<ChangeListener<Pager>>()
    val nofPagesChangeListeners = mutableListOf<ChangeListener<Pager>>()

    var pageSize: Int = pageSize
        get() = field
        set(value) {
            if (field != value) {
                field = value
                recalcNofPages()
                changeListeners.forEach { listener -> listener(this) }
            }
        }

    protected fun recalcNofPages() {
        nofPages = max(ceil(totalCount.toDouble() / pageSize.toDouble()).toInt(), 1)
        if (currentPage > nofPages) {
            last()
        }
    }

    open var totalCount: Int = 0
        get() = field
        set(value) {
            if (field != value) {
                field = value
                recalcNofPages()
            }
        }

    /**
     * The current page number. Should be in the range 1 to nofPages (inclusive).
     */
    var currentPage: Int by ChangeDelegate<Pager, Int>(1, changeListeners)
        private set

    var nofPages: Int by ChangeDelegate<Pager, Int>(nofPages, nofPagesChangeListeners)

    val currentOffset: Int get() = pageSize * (currentPage - 1)
    val currentEnd: Int get()= min(currentOffset + pageSize, totalCount)

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

