package io.github.kasukusakuraapi.servercommon.auth

import io.github.kasukusakuraapi.servercommon.service.AccountService
import io.github.kasukusakuraapi.servercommon.utils.AuthFailedException
import io.github.kasukusakuraapi.servercommon.utils.KtorPipelineContext
import io.github.kasukusakuraapi.servercommon.utils.set
import io.ktor.util.*

class AuthService(
    private val accS: AccountService
) {
    companion object {
        val ACCOUNT_ATTRIBUTE_KEY = AttributeKey<Account>("account")
        val KtorPipelineContext.account: Account get() = this.context.attributes[ACCOUNT_ATTRIBUTE_KEY]
    }

    fun KtorPipelineContext.doAuth() {
        context.request.headers["Authorization"]?.let { auth ->
            when (auth.substringBefore(' ')) {
                "token" -> {
                    accS.findAccountByToken(auth.substringAfter(' '))?.let { account ->
                        context.attributes[ACCOUNT_ATTRIBUTE_KEY] = account
                    }
                }
                else -> Unit
            }
        }
        if (!context.attributes.contains(ACCOUNT_ATTRIBUTE_KEY)) {
            throw AuthFailedException
        }
    }

}
