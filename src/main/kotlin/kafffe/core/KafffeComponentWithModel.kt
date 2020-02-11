package kafffe.core

import kotlin.reflect.KMutableProperty0

open class KafffeComponentWithModel<T : Any?>(model: Model<T>) : KafffeComponent() {

    var model: Model<T> = model
        set(value) {
            field = value
            onModelChanged()
        }

    val onModelChanged = ModelChangeListener(::modelChanged)
    /**
     * Behavior on model changed if modelChanged not overridden.
     * @see #modelChangedNoop default
     * @see #modelChangedRerender
     * @see #modelChangedRerenderRecursive
     */
    var modelChangedStandardBehavior: () -> Unit = {}

    fun setModelChangedRerender() { modelChangedStandardBehavior = ::rerender }
    fun setModelChangedRerenderRecursive() { modelChangedStandardBehavior = ::rerenderRecursive }
    fun setModelChangedNoop() { modelChangedStandardBehavior = {} }

    open fun modelChanged() {
        modelChangedStandardBehavior()
    }

    override fun attach() {
        super.attach()
        model.listeners.add(onModelChanged)
    }

    override fun detach() {
        super.detach()
        model.listeners.remove(onModelChanged)
    }

    fun delegateToModel(): ModelDelegate<T> = ModelDelegate<T>(::model)

    fun <S> delegateToModel(m: KMutableProperty0<Model<S>>): ModelDelegate<S> = ModelDelegate<S>(m)

}