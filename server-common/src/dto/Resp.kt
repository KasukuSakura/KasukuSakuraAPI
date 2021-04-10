package io.github.kasukusakuraapi.servercommon.dto

import io.github.kasukusakuraapi.servercommon.utils.UnitSerializer
import kotlinx.serialization.Serializable

@Serializable
class Resp<T>(
    val code: Int,
    val message: String? = null,
    val response: T? = null,
)

val RespWithUnitSerializer = Resp.serializer(UnitSerializer)
