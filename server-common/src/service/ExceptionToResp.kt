package io.github.kasukusakuraapi.servercommon.service

import io.github.kasukusakuraapi.servercommon.annotations.ImplClass
import io.github.kasukusakuraapi.servercommon.impl.ETR
import io.ktor.http.content.*

@ImplClass(ETR::class)
interface ExceptionToResp {
    fun toResp(throwable: Throwable): OutgoingContent
}
