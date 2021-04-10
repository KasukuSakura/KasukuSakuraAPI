@file:Suppress("UNCHECKED_CAST")

package io.github.kasukusakuraapi.servercommon.impl

import io.github.kasukusakuraapi.servercommon.dto.Resp
import io.github.kasukusakuraapi.servercommon.dto.RespWithUnitSerializer
import io.github.kasukusakuraapi.servercommon.service.ControllerResponseToContentService
import io.github.kasukusakuraapi.servercommon.service.ControllerResponseToContentService.Translator
import io.github.kasukusakuraapi.servercommon.utils.msg
import io.ktor.http.content.*
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.serializer
import kotlinx.serialization.serializerOrNull
import kotlin.reflect.KFunction

class StandardControllerResponseTranslator : ControllerResponseToContentService {
    private object ToStrCC : Translator {
        override suspend fun invoke(value: Any?): OutgoingContent {
            return Resp<Unit>(200, value?.toString()).msg(RespWithUnitSerializer)
        }
    }

    private class RespS(val s: SerializationStrategy<Resp<*>>) : Translator {
        override suspend fun invoke(value: Any?): OutgoingContent {
            if (value == null) {
                return Resp<Unit>(200).msg(RespWithUnitSerializer)
            }
            return (value as Resp<*>).msg(s)
        }
    }

    private class WResp(s: KSerializer<*>) : Translator {
        val ex = Resp.serializer(s) as KSerializer<Resp<*>>
        override suspend fun invoke(value: Any?): OutgoingContent {
            if (value == null) {
                return Resp<Unit>(200).msg(RespWithUnitSerializer)
            }
            return Resp<Any?>(200, null, value).msg(ex)
        }
    }

    @OptIn(ExperimentalStdlibApi::class)
    override fun getTranslator(met: KFunction<*>, instance: Any): Translator {
        met.returnType.let { t ->
            if (t.classifier == Resp::class) {
                return RespS(serializer(t))
            }
            serializerOrNull(t)?.let { return WResp(it) }
        }
        return ToStrCC
    }
}
