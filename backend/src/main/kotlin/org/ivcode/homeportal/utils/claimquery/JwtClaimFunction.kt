package org.ivcode.homeportal.utils.claimquery

/**
 * Creates the function entries for the claim query context
 */
internal fun createFunctionMap(): Map<String, JwtClaimFunction> = mapOf(
    "scope" to ScopeClaimFunction(),
    "email" to EmailClaimFunction(),
    "contains" to ContainsClaimFunction()
)

/**
 * Represents an invokable jwt claim function
 */
internal interface JwtClaimFunction {
    fun execute(context: Map<String, Any>, params: List<Any>): Any
}

/**
 * A scope claim query
 * Signature:
 *  - scope(String... scopes)
 * Example:
 *  - scope("read") : returns true if the current jwt has the scope "read"
 *  - scope("read", "write") : returns true if the current jwt has the scopes "read" AND "write"
 */
internal class ScopeClaimFunction: JwtClaimFunction {
    override fun execute(context: Map<String, Any>, params: List<Any>): Any {
        if(params.isEmpty()) {
            return false
        }
        params.forEach {
            if(it !is String) {
                throw ClassCastException("The scope function parameters must be of type String: function_signature = scope(String... scopes)")
            }
        }

        val scopes: List<String> = params as List<String>

        val jwt: Map<String, Any> = context["jwt"] as Map<String, Any>? ?: return false
        val scopesString = jwt["scope"] as String? ?: return false

        val scopesList = scopesString.split(" ")
        return scopesList.containsAll(scopes)
    }
}

/**
 * An email claim query
 * Signature:
 *  - email(String... emailAddresses)
 * Example:
 *  - email("user@domain.com") : returns true if the current jwt has the email claim, and it equals "user@domain.com"
 *  - email("user1@domain.com", "user2@domain.com") : returns true if the current jwt has the email claim, and it ANY of the given emails
 */
internal class EmailClaimFunction: JwtClaimFunction {
    override fun execute(context: Map<String, Any>, params: List<Any>): Any {
        if(params.isEmpty()) {
            return false
        }
        params.forEach {
            if(it !is String) {
                throw ClassCastException("The email function parameters must be of type String: function_signature = email(String... scopes)")
            }
        }

        val emailParams: List<String> = params as List<String>

        val jwt: Map<String, Any> = context["jwt"] as Map<String, Any>? ?: return false
        val jwtEmail: String = jwt["email"] as String? ?: return false

        val matches = emailParams.filter { email -> email.equals(jwtEmail, ignoreCase = true) }
        return matches.isNotEmpty()
    }
}

/**
 * List contains query. Tests if a list claim contains a given value. The test is not case-sensitive.
 * Signature:
 * - contains(List<String> list, String value)
 * Example:
 * - contains(jwt.realm_access.roles, "admin") : tests that the list claim "realm_access -> roles" contains to string "admin"
 */
internal class ContainsClaimFunction: JwtClaimFunction {
    override fun execute(context: Map<String, Any>, params: List<Any>): Any {
        val list = params[0] as List<String>? ?: return false
        val value: String = params[1] as String

        val matches = list.filter { v -> v.equals(value, ignoreCase = true) }
        return matches.isNotEmpty()
    }
}