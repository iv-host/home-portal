package org.ivcode.homeportal.utils

import org.springframework.security.oauth2.jwt.Jwt

public fun Jwt.realmRoles(): Set<String> {
    val realmAccess = claims["realm_access"] as? Map<*, *>
    val roles = realmAccess?.get("roles") as? List<*>
    return roles?.filterIsInstance<String>()?.toSet() ?: emptySet()
}
