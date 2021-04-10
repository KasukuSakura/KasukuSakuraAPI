package io.github.kasukusakuraapi.servercommon.impl

import io.github.kasukusakuraapi.servercommon.annotations.Component
import io.github.kasukusakuraapi.servercommon.annotations.ImplClass
import io.github.kasukusakuraapi.servercommon.service.ServiceSearcher

class SimpleServiceSearcher : ServiceSearcher {
    val services = mutableMapOf<Class<*>, Any>()
    override fun <T : Any> findService(type: Class<T>): T? {
        return type.cast(services[type])
    }

    override fun <T : Any> registerService(type: Class<T>, service: T) {
        services[type] = service
    }

    override fun <T : Any> loadService(type: Class<T>): T {
        return findService(type) ?: try {
            loadService0(type).also {
                registerService(type, it)
            }
        } catch (e: Throwable) {
            throw RuntimeException("Exception in loading service `$type`", e)
        }
    }
}

private fun <T> ServiceSearcher.loadService0(type: Class<T>): T {
    val type0 = type.getDeclaredAnnotation(ImplClass::class.java)?.value?.java ?: type
    type0.kotlin.objectInstance?.let { return type.cast(it) }

    return type0.constructors.single().let { cor ->
        val args = cor.parameterTypes.map {
            loadService(it)
        }.toTypedArray()
        cor.newInstance(*args)
    }.also { instance ->
        generateSequence<Class<*>>(type0) { it.superclass }.flatMap {
            it.declaredFields.asSequence()
        }.filter { it.isAnnotationPresent(Component::class.java) }.onEach {
            it.isAccessible = true
        }.forEach { field ->
            field.set(instance, loadService(field.type))
        }
    }.let { type.cast(it) }
}