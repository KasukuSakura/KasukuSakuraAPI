package io.github.kasukusakuraapi.servercommon.service

import io.github.kasukusakuraapi.servercommon.annotations.ImplClass
import io.github.kasukusakuraapi.servercommon.impl.StandardControllerResponseTranslator
import io.ktor.http.content.*
import kotlin.reflect.KFunction

@ImplClass(StandardControllerResponseTranslator::class)
interface ControllerResponseToContentService {
    interface Translator {
        suspend operator fun invoke(value: Any?): OutgoingContent
    }

    fun getTranslator(met: KFunction<*>, instance: Any): Translator
}
