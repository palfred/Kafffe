package kafffe.core

open class RootComponentWithModel<T : Any?>(model: Model<T>) : RootComponent() {
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
        model.listeners.remove(onModelChanged)
    }
}