package io.github.kasukusakuraapi.servercommon.test

import io.github.karlatemp.mxlib.logger.slf4j.MSlf4JLoggerInstaller
import io.github.karlatemp.mxlib.logger.slf4j.MSlf4jLoggerFactory
import io.github.kasukusakuraapi.servercommon.KasukuSakuraCommonServer
import io.github.kasukusakuraapi.servercommon.auth.Account
import io.github.kasukusakuraapi.servercommon.impl.ETR
import io.github.kasukusakuraapi.servercommon.impl.SimpleAccountService
import io.github.kasukusakuraapi.servercommon.impl.SimpleServiceSearcher
import io.github.kasukusakuraapi.servercommon.service.AccountService
import io.github.kasukusakuraapi.servercommon.service.ExceptionToResp
import org.slf4j.ILoggerFactory


fun main() {
    val factory: ILoggerFactory = MSlf4jLoggerFactory { LogFactory.Lg(it) }
    MSlf4JLoggerInstaller.install(factory)
    val s = SimpleServiceSearcher()
    val acc = SimpleAccountService()
    s.registerService(AccountService::class.java, acc)
    s.registerService(ExceptionToResp::class.java, ETR)
    acc.accounts["test"] = Account(
        false, listOf(
            "test", "whoami", "sb"
        )
    )

    KasukuSakuraCommonServer(s).also { sr ->
        sr.registerController(TestController::class.java)
    }.server.start(true)
}
