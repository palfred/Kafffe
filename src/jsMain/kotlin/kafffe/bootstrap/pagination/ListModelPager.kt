package kafffe.bootstrap.pagination

import kafffe.core.Model
import kafffe.core.ModelChangeListener
import kotlin.math.min

class ListModelPager<T : Any>(val listModel: Model<List<T>>, pageSize: Int = 10) : Pager(1, pageSize) {


    val pageModel = Model.of(listOf<T>())

    val listModelListener = ModelChangeListener(::recalcPages)

    init {
        listModel.listeners.add(listModelListener)
        changeListeners.add { _ -> updatePageModel() }
        recalcPages()
    }


    override fun updatePageModel() {
        val offset = pageSize * (currentPage - 1)
        val end = min(offset + pageSize, listModel.data.size)
        pageModel.data = listModel.data.subList(offset, end)
    }

    override fun totalCount(): Int =
        listModel.data.size

}