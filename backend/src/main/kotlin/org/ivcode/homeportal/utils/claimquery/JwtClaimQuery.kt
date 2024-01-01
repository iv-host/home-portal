package org.ivcode.homeportal.utils.claimquery

import org.springframework.security.oauth2.jwt.Jwt

class JwtClaimQuery private constructor (
    private val statement: Statement
) {
    companion object {
        fun parse(query: String): JwtClaimQuery {
            val tokens = JwtClaimTokenParser().parse(query)
            val statement = JwtClaimStatementParser().parse(tokens)

            return JwtClaimQuery(statement)
        }
    }

    fun query(jwt: Jwt): Boolean =
        statement.execute(createContext(jwt)) as Boolean

    private fun createContext(jwt: Jwt): Map<String, Any> = mutableMapOf<String, Any>().apply {
        putAll(createFunctionMap())
        put("jwt", jwt.claims)
    }.toMap()
}
