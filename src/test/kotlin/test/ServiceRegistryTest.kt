package test

import kafffe.core.ServiceRegistry
import kafffe.messages.Messages
import kafffe.messages.Messages_da
import kafffe.messages.Messages_en
import kotlin.test.*

class ServiceRegistryTest {
    interface TestService {
        fun doTheStuff(): String
    }

    class TestServiceImpl : TestService {
        override fun doTheStuff(): String = "The stuff"
    }

    class DoTheStuffDirectly {
        fun doTheStuff(): String = "The stuff Directly"
    }

    init {
        ServiceRegistry.register("test1", TestServiceImpl())
        ServiceRegistry.register("test2", DoTheStuffDirectly())

        ServiceRegistry.register("msg_en", Messages_da())
        ServiceRegistry.register("msg_da", Messages_en())
    }

    @Test
    fun findByInterfaceType() {
        val service = ServiceRegistry.find<TestService>()
        assertNotNull(service)
        assertEquals("The stuff", service.doTheStuff())
    }


    @Test
    fun findByDirectType() {
        val service = ServiceRegistry.find<DoTheStuffDirectly>()
        assertNotNull(service)
        assertEquals("The stuff Directly", service.doTheStuff())
    }

    @Test
    fun findAllByType() {
        val services = ServiceRegistry.findAll<Messages>()
        assertNotNull(services)
        assertNotNull(services.size == 2)
        assertEquals(setOf("en", "da"), services.map { s -> s.language }.toSet())
    }
}