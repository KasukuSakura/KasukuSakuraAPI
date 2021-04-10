package io.github.kasukusakuraapi.servercommon.test

import io.github.kasukusakuraapi.servercommon.annotations.RequestMapping
import io.github.kasukusakuraapi.servercommon.auth.AuthService.Companion.account
import io.github.kasukusakuraapi.servercommon.auth.checkPermission
import io.github.kasukusakuraapi.servercommon.dto.Resp
import io.github.kasukusakuraapi.servercommon.utils.KtorPipelineContext

@RequestMapping("/")
class TestController {
    @RequestMapping("", methods = ["GET"])
    suspend fun KtorPipelineContext.handle(): Resp<Int> {
        return Resp(200, "Test")
    }


    @RequestMapping("ex", methods = ["GET"])
    suspend fun KtorPipelineContext.handle2(): Int {
        return 200
    }

    @RequestMapping("perm", methods = ["GET"])
    suspend fun KtorPipelineContext.checkPerm(): Any {
        this.account.checkPermission("sb.yellow")
        return this.account
    }
}
