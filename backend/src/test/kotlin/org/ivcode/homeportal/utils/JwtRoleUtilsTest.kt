package org.ivcode.homeportal.utils

import org.springframework.security.oauth2.jwt.Jwt
import kotlin.test.Test
import kotlin.test.assertEquals

class JwtRoleUtilsTest {

    @Test
    fun `reads realm roles from the access token`() {
        val jwt = Jwt.withTokenValue("test")
            .header("alg", "none")
            .claim("realm_access", mapOf("roles" to listOf("admin", "user")))
            .build()

        assertEquals(setOf("admin", "user"), jwt.realmRoles())
    }

    @Test
    fun `returns no roles when the realm access claim is missing`() {
        val withoutRealmAccess = Jwt.withTokenValue("without-realm-access")
            .header("alg", "none")
            .subject("user")
            .build()

        assertEquals(emptySet(), withoutRealmAccess.realmRoles())
    }

    @Test
    fun `returns no roles when realm access is malformed`() {
        val malformedRealmAccess = Jwt.withTokenValue("malformed-realm-access")
            .header("alg", "none")
            .claim("realm_access", "admin")
            .build()

        assertEquals(emptySet(), malformedRealmAccess.realmRoles())
    }

    @Test
    fun `returns no roles when realm roles are malformed`() {
        val withoutRoles = Jwt.withTokenValue("without-roles")
            .header("alg", "none")
            .claim("realm_access", mapOf("roles" to "admin"))
            .build()

        assertEquals(emptySet(), withoutRoles.realmRoles())
    }
}
