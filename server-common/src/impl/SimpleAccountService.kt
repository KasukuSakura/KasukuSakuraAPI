package io.github.kasukusakuraapi.servercommon.impl

import io.github.kasukusakuraapi.servercommon.auth.Account
import io.github.kasukusakuraapi.servercommon.service.AccountService

class SimpleAccountService : AccountService {
    val accounts = mutableMapOf<String, Account>()
    override fun findAccountByToken(token: String): Account? {
        return accounts[token]
    }
}