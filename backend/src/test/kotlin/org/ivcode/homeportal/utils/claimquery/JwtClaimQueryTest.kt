package org.ivcode.homeportal.utils.claimquery

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito
import org.springframework.security.oauth2.jwt.Jwt
import kotlin.test.assertEquals


class JwtClaimQueryTest {
    @Test
    fun scopeHappyPathTest() {
        val scope = "read"
        val jwtClaimQuery = JwtClaimQuery.parse("scope(\"${scope}\")")

        val jwt = Mockito.mock(Jwt::class.java)
        Mockito.`when`(jwt.claims).thenReturn(mapOf<String, Any>(
            "scope" to "read write"
        ))

        val actual = jwtClaimQuery.query(jwt)
        assertEquals(true, actual)
    }

    @Test
    fun scopeMultiplesTest() {
        val jwtClaimQuery = JwtClaimQuery.parse("scope(\"read\", \"write\")")

        val jwt = Mockito.mock(Jwt::class.java)
        Mockito.`when`(jwt.claims).thenReturn(mapOf<String, Any>(
            "scope" to "read write"
        ))

        val actual = jwtClaimQuery.query(jwt)
        assertEquals(true, actual)
    }

    @Test
    fun scopeQueryFailTest() {
        val jwtClaimQuery = JwtClaimQuery.parse("scope(\"write\")")

        val jwt = Mockito.mock(Jwt::class.java)
        Mockito.`when`(jwt.claims).thenReturn(mapOf<String, Any>(
            "scope" to "read"
        ))

        val actual = jwtClaimQuery.query(jwt)
        assertEquals(false, actual)
    }

    @Test
    fun scopeBadInputTest() {
        val jwtClaimQuery = JwtClaimQuery.parse("scope(true)")

        val jwt = Mockito.mock(Jwt::class.java)
        Mockito.`when`(jwt.claims).thenReturn(mapOf<String, Any>(
            "scope" to "read"
        ))

        assertThrows<ClassCastException> {
            jwtClaimQuery.query(jwt)
        }
    }

    @Test
    fun emailHappyPathTest() {
        val email = "isaiah@domain.com"
        val jwtClaimQuery = JwtClaimQuery.parse("email(\"${email}\")")

        val jwt = Mockito.mock(Jwt::class.java)
        Mockito.`when`(jwt.claims).thenReturn(mapOf<String, Any>(
            "email" to email
        ))

        val actual = jwtClaimQuery.query(jwt)
        assertEquals(true, actual)
    }

    @Test
    fun emailMultiplesTest() {
        val email1 = "isaiah@domain.com"
        val email2 = "dave@domain.com"
        val jwtClaimQuery = JwtClaimQuery.parse("email(\"${email1}\", \"${email2}\")")

        val jwt1 = Mockito.mock(Jwt::class.java)
        Mockito.`when`(jwt1.claims).thenReturn(mapOf<String, Any>(
            "email" to email1
        ))

        val actual1 = jwtClaimQuery.query(jwt1)
        assertEquals(true, actual1)

        val jwt2 = Mockito.mock(Jwt::class.java)
        Mockito.`when`(jwt2.claims).thenReturn(mapOf<String, Any>(
            "email" to email2
        ))

        val actual2 = jwtClaimQuery.query(jwt2)
        assertEquals(true, actual2)
    }

    @Test
    fun emailQueryFailTest() {
        val email = "isaiah@domain.com"
        val jwtClaimQuery = JwtClaimQuery.parse("email(\"someone@domain.com\")")

        val jwt = Mockito.mock(Jwt::class.java)
        Mockito.`when`(jwt.claims).thenReturn(mapOf<String, Any>(
            "email" to email
        ))

        val actual = jwtClaimQuery.query(jwt)
        assertEquals(false, actual)
    }

    @Test
    fun containsTest() {
        val jwtClaimQuery = JwtClaimQuery.parse("contains(jwt.realm_access.roles,\"admin\")")

        val jwt = Mockito.mock(Jwt::class.java)
        Mockito.`when`(jwt.claims).thenReturn(mapOf<String, Any>(
            "realm_access" to mapOf<String, Any>(
                "roles" to listOf("admin", "offline_access")
            )
        ))

        val actual = jwtClaimQuery.query(jwt)
        assertEquals(true, actual)
    }

    @Test
    fun badFunctionTypeTest() {
        val jwtClaimQuery = JwtClaimQuery.parse("jwt.scope(\"read\")")

        val jwt = Mockito.mock(Jwt::class.java)
        Mockito.`when`(jwt.claims).thenReturn(mapOf<String, Any>(
            "scope" to "read"
        ))

        assertThrows<ClassCastException> {
            jwtClaimQuery.query(jwt)
        }
    }

    @Test
    fun unknownFunctionTest() {
        val jwtClaimQuery = JwtClaimQuery.parse("foo(\"read\")")

        val jwt = Mockito.mock(Jwt::class.java)

        assertThrows<NoSuchElementException> {
            jwtClaimQuery.query(jwt)
        }
    }

}