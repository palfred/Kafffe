package kafffe.bootstrap.pagination

import kafffe.core.Model
import kafffe.core.ModelChangeListener

class ListModelPager<T : Any>(val listModel: Model<List<T>>, pageSize: Int = 10) : Pager(1, pageSize) {
    val pageModel = Model.of(listOf<T>())

    init {
        listModel.listeners.add(ModelChangeListener {
            updateNofPagesAndPage()
        })

        changeListeners.add { _ -> updatePageModel() }
        updateNofPagesAndPage()
    }

    private fun updateNofPagesAndPage() {
        totalCount = listModel.data.size
        updatePageModel()
    }


    fun updatePageModel() {
        pageModel.data = listModel.data.subList(currentOffset, currentEnd)
    }

}