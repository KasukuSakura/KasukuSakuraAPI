package io.github.kasukusakuraapi.servercommon.impl

import io.github.kasukusakuraapi.servercommon.dto.Resp
import io.github.kasukusakuraapi.servercommon.dto.RespWithUnitSerializer
import io.github.kasukusakuraapi.servercommon.service.ExceptionToResp
import io.github.kasukusakuraapi.servercommon.utils.AuthFailedException
import io.github.kasukusakuraapi.servercommon.utils.NoPermissionException
import io.github.kasukusakuraapi.servercommon.utils.msg
import io.ktor.http.content.*
import java.lang.reflect.InvocationTargetException

object ETR : ExceptionToResp {
    override fun toResp(throwable: Throwable): OutgoingContent {
        if (throwable is InvocationTargetException) return toResp(throwable.targetException)
        val r: Resp<Unit> = when (throwable) {
            is NoPermissionException -> Resp(403, throwable.message)
            is AuthFailedException -> Resp(403, "Auth failed")
            else -> Resp(500, throwable.message, null)
        }
        return r.msg(RespWithUnitSerializer)
    }
}
