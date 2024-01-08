package org.ivcode.homeportal.utils.claimquery

import org.springframework.security.oauth2.jwt.Jwt

public class JwtClaimQuery private constructor (
    private val statement: Statement
) {
    public companion object {
        public fun parse(query: String): JwtClaimQuery {
            val tokens = JwtClaimTokenParser().parse(query)
            val statement = JwtClaimStatementParser().parse(tokens)

            return JwtClaimQuery(statement)
        }
    }

    public fun query(jwt: Jwt): Boolean =
        statement.execute(createContext(jwt)) as Boolean

    private fun createContext(jwt: Jwt): Map<String, Any> = mutableMapOf<String, Any>().apply {
        putAll(createFunctionMap())
        put("jwt", jwt.claims)
    }.toMap()
}
