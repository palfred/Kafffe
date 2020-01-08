package kafffe.bootstrap.pagination

import kafffe.core.Model
import kafffe.core.ModelChangeListener
import kotlin.math.ceil
import kotlin.math.max
import kotlin.math.min

class ListModelPager<T : Any>(val listModel: Model<List<T>>, pageSize: Int = 10) : Pager(1) {
    var pageSize = pageSize
        set(value) {
            field = value
            recalcPages()
        }

    val pageModel = Model.of(listOf<T>())

    val listModelListener = ModelChangeListener(::recalcPages)

    init {
        listModel.listeners.add(listModelListener)
        changeListeners.add { _ -> updatePageModel() }
        recalcPages()
    }


    private fun updatePageModel() {
        val offset = pageSize * (currentPage - 1)
        val end = min(offset + pageSize, listModel.data.size)
        pageModel.data = listModel.data.subList(offset, end)
    }

    private fun recalcPages() {
        nofPages = max(ceil(listModel.data.size.toDouble() / pageSize.toDouble()).toInt(), 1)
        if (currentPage > nofPages) {
            last()
        }
        updatePageModel()
    }

}