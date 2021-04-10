package io.github.kasukusakuraapi.servercommon.auth

import io.github.kasukusakuraapi.servercommon.utils.NoPermissionException

data class Account(
    val trusted: Boolean,
    val permissions: Collection<String>,
)

fun Account.checkPermission(permission: String) {
    if (trusted) return
    var c = permission
    while (true) {
        if (c in permissions) return
        val index = c.lastIndexOf('.')
        if (index == -1) {
            noPermissionException(permission)
        }
        c = c.substring(0, index)
    }
}

private fun Account.noPermissionException(permission: String): Nothing {
    throw NoPermissionException("Missing permission `$permission`")
}

