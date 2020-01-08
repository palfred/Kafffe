package kafffe.core

import kotlin.js.Date
import kotlin.reflect.*

data class ModelChangeListener(val listen: () -> Unit) {
    operator fun invoke() = listen()
}

/**
 * Models for data used by UI Components. This give a lvel of indirection that makes it possible to wrap any property of model objects.
 */
abstract class Model<T : Any?> {
    /**
     * Listeners needs to remove them self in order to be garbage collected. Do not know how to make weak ref in Kotlin JS.
     * Components could for instance do this when they do not have parent, and then maybe add as a listener when they get a (new) parent.
     * In that way the component could be freed when it is not part of a componenthierarchy
     */
    open val listeners = mutableListOf<ModelChangeListener>()

    abstract var data: T

    /**
     * Tells the listener that the model data might have changed.
     */
    fun changed() {
        listeners.forEach { it() }
    }

    companion object {
        fun <S : Any> of(data: S): ModelNotNullable<S> = ModelNotNullable<S>(data)
        fun <S : Any> ofNullable(data: S? = null): ModelNullable<S> = ModelNullable<S>(data)
        fun <S : Any> ofGet(getter: () -> S): Model<S> = FunctionalModel<S>(getData = getter)
        fun <S : Any> ofGetSet(getter: () -> S, setter: (value: S) -> Unit): Model<S> = FunctionalModel<S>(getData = getter, setData = setter)
    }
}

open class ModelNotNullable<T : Any>(initData: T) : Model<T>() {
    override var data: T = initData
        set(value) {
            field = value
            changed()
        }
}

open class ModelNullable<T : Any>(initData: T? = null) : Model<T?>() {
    override var data: T? = initData
        set(value) {
            field = value
            changed()
        }
}


open class FunctionalModel<T : Any?>(
        val getData: () -> T,
        val setData: (value: T) -> Unit = { _ -> }
) : Model<T>() {
    override var data: T
        get() = getData()
        set(value) {
            setData(value)
            changed()
        }
}

/**
 * The property model will not detect changes directly on object having the property. THis will only give access to the property through a model
 */
open class PropertyModel<T : Any?>(val property: KProperty0<T>) : Model<T>() {

    override var data: T
        get() = property()
        set(value) {
            if (property is KMutableProperty0) {
                property.set(value)
            }
            changed()
        }

}

open class FunctionalSubModel<T : Any?, TP : Any>(
        val parentModel: Model<TP>,
        val getData: (Model<TP>) -> T,
        val setData: (parent: Model<TP>, value: T) -> Unit = { _, _ -> }
) : Model<T>() {

    override val listeners = parentModel.listeners

    override var data: T
        get() = getData(parentModel)
        set(value) {
            setData(parentModel, value)
            changed()
        }

}

/**
 * Creates a submodel as functions of the receiver
 */
fun <T : Any?, TP : Any> Model<TP>.func(getData: (Model<TP>) -> T, setData: (parent: Model<TP>, value: T) -> Unit = { _, _ -> }) = FunctionalSubModel(this, getData, setData)


open class PropertySubModel<T : Any?, TP : Any>(
        val parentModel: Model<TP>,
        val property: KProperty1<TP, T>
) : Model<T>() {

    override val listeners = parentModel.listeners

    override var data: T
        get() = property(parentModel.data)
        set(value) {
            if (property is KMutableProperty1) {
                property.set(parentModel.data, value)
            }
            changed()
        }

}

/**
 * Creates a property submodel of the receiver
 */
fun <T : Any?, TP : Any> Model<TP>.property(property: KProperty1<TP, T>) = PropertySubModel(this, property)

class ModelDelegate<Value>(val modelProperty: KMutableProperty0<Model<Value>>) {
    operator fun getValue(thisRef: Any?, prop: KProperty<*>): Value {
        return modelProperty().data
    }

    operator fun setValue(thisRef: Any?, prop: KProperty<*>, newValue: Value) {
        modelProperty().data = newValue
    }
}


/**
 * Refreshing cache model
 */
open class RefreshingCacheModel<T : Any>(val refresh: (model: Model<T>) -> Unit, initData: T, val timeToLiveSeconds: Int = 60 * 5) : Model<T>() {
    private var lastRefreshed = 0.0

    private fun timestampNow() = Date(Date.now())

    override var data: T = initData
        get() {
            if (needRefresh()) {
                refresh(this)
            }
            return field
        }
        set(value) {
            lastRefreshed = timestampNow().getTime()
            field = value
            changed()
        }

    fun expire() {
        lastRefreshed = 0.0
    }

    open fun needRefresh(): Boolean {
        return timestampNow().getTime() > (lastRefreshed + timeToLiveSeconds * 1000)
    }
}
