package kafffe.core

import kotlin.reflect.KProperty

class RerenderDelegate<Value>(val rerenderComponent: KafffeComponent, var value: Value) {
    operator fun getValue(thisRef: Any?, prop: KProperty<*>): Value {
        return value
    }

    operator fun setValue(thisRef: Any?, prop: KProperty<*>, newValue: Value) {
        value = newValue
        rerenderComponent.rerender()
    }
}