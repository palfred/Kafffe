package kafffe.core

import kotlin.reflect.KProperty

typealias ChangeListener<T> = (source: T) -> Unit

/**
 * Delegate supporting change listenes
 * Example of how to use:
 * ```
 * class Pager() {
 *   val changeListeners = mutableListOf<ChangeListener<Pager>>()
 *
 *   var currentPage: Int by ChangeDelegate<Pager, Int>(1, changeListeners)*
 *   var nofPages: Int by ChangeDelegate<Pager, Int>(nofPages, changeListeners)
 *   ```
 */
class ChangeDelegate<Source, Value>(var value: Value, val listeners: Iterable<ChangeListener<Source>>) {

    operator fun getValue(thisRef: Any?, prop: KProperty<*>): Value {
        return value
    }

    operator fun setValue(thisRef: Any?, prop: KProperty<*>, newValue: Value) {
        if (newValue != value) {
            value = newValue
            for (listener in listeners) {
                @Suppress("UNCHECKED_CAST")
                listener(thisRef as Source)
            }
        }
    }
}

typealias ChangeListenerWithValue<T,V> = (source: T, newValue: V) -> Unit
/**
 * Delegate supporting change listeners receiving the new value
 * Example of how to use:
 * ```
 * class Pager() {
 *   val changeListeners = mutableListOf<ChangeListenerWithValue<Pager; Int>>()
 *
 *   var currentPage: Int by ChangeDelegateWithValue<Pager, Int>(1, changeListeners)
 *   var nofPages: Int by ChangeDelegateWithValue<Pager, Int>(nofPages, changeListeners)
 *   ```
 *
 */
class ChangeDelegateWithValue<Source, Value>(var value: Value, val listeners: Iterable<ChangeListenerWithValue<Source, Value>>) {

    operator fun getValue(thisRef: Any?, prop: KProperty<*>): Value {
        return value
    }

    operator fun setValue(thisRef: Any?, prop: KProperty<*>, newValue: Value) {
        value = newValue
        for (listener in listeners) {
            @Suppress("UNCHECKED_CAST")
            listener(thisRef as Source, newValue)
        }
    }
}

