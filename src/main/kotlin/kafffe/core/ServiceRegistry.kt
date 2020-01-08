package kafffe.core

import kotlin.reflect.KClass

object ServiceRegistry {
    data class ServiceEntry(val name: String, val type: KClass<out Any>, val service: Any)

    private val services = mutableListOf<ServiceEntry>()

    fun add(entry: ServiceEntry) {
        services.add(entry)
    }

    inline fun <reified T : Any> register(name: String, service: T) {
        add(ServiceEntry(name, T::class, service))
    }

    inline fun <reified T : Any> find() = findByType(T::class)
    inline fun <reified T : Any> findAll() = findAllByType(T::class)

    fun <T : Any> findByType(type: KClass<T>): T? {
        val entry = services.find { s -> s.type === type || type.isInstance(s.service) }
        @Suppress("UNCHECKED_CAST")
        return if (entry != null) entry.service as T else null
    }

    fun <T : Any> findAllByType(type: KClass<T>): List<T> {
        @Suppress("UNCHECKED_CAST")
        val services: List<T> = services.filter { e -> e.type === type || type.isInstance(e.service) }.map { e -> e.service as T }
        return services
    }

}