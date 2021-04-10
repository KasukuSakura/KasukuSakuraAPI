package io.github.kasukusakuraapi.servercommon.service

interface ServiceSearcher {
    fun <T : Any> findService(type: Class<T>): T?
    fun <T : Any> registerService(type: Class<T>, service: T)
    fun <T : Any> loadService(type: Class<T>): T
}

inline fun <reified T : Any> ServiceSearcher.service(): T = loadService(T::class.java)

