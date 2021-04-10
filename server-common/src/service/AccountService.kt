package io.github.kasukusakuraapi.servercommon.service

import io.github.kasukusakuraapi.servercommon.auth.Account

interface AccountService {
    fun findAccountByToken(token: String): Account?
}
