package io.github.kasukusakuraapi.servercommon.utils

import io.github.kasukusakuraapi.servercommon.dto.Resp
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.util.*
import io.ktor.util.pipeline.*
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.json.Json


typealias KtorPipeline = PipelineInterceptor<Unit, ApplicationCall>
typealias KtorPipelineContext = PipelineContext<Unit, ApplicationCall>

operator fun <T : Any> Attributes.set(key: AttributeKey<T>, v: T) {
    put(key, v)
}

private object HS {
    private val byValue: Array<HttpStatusCode?> = Array(1000) { idx ->
        HttpStatusCode.allStatusCodes.firstOrNull { it.value == idx }
    }

    /**
     * Creates an instance of [HttpStatusCode] with the given numeric value.
     */
    fun fromValue(value: Int): HttpStatusCode {
        val knownStatus = if (value in 1 until 1000) byValue[value] else null
        return knownStatus ?: HttpStatusCode.OK
    }
}

val json = Json { }
fun <T> Resp<T>.msg(serializer: SerializationStrategy<Resp<T>>): OutgoingContent {
    return TextContent(
        json.encodeToString(serializer, this),
        ContentType.Application.Json,
        HS.fromValue(this.code)
    )
}