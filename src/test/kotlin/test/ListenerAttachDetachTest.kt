package test

import kafffe.core.ModelChangeListener
import kotlin.test.Test
import kotlin.test.assertEquals


class ListenerAttachDetachTest {
    val listeners = mutableListOf<ModelChangeListener>()

    val listener = ModelChangeListener(this::listen)

    @Test
    fun attachAndDetachFuntionRef() {
        listeners.add(listener)
        listeners.remove(listener)
        assertEquals(0, listeners.size)
    }

    private fun listen() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}