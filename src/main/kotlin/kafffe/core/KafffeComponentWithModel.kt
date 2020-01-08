package kafffe.core

import kotlin.reflect.KMutableProperty0

open class KafffeComponentWithModel<T : Any?>(model: Model<T>) : KafffeComponent() {

    var model: Model<T> = model
        set(value) {
            field = value
            onModelChanged()
        }

    val onModelChanged = ModelChangeListener(::rerender)

    override fun attach() {
        super.attach()
        model.listeners.add(onModelChanged)
    }

    override fun detach() {
        super.detach()
        // will this work - or do we need to save the "change listener" in a val ??
        model.listeners.remove(onModelChanged)
    }

    fun delegateToModel(): ModelDelegate<T> = ModelDelegate<T>(::model)

    fun <S> delegateToModel(m: KMutableProperty0<Model<S>>): ModelDelegate<S> = ModelDelegate<S>(m)

}