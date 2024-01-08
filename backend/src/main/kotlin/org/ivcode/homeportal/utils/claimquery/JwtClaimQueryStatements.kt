package org.ivcode.homeportal.utils.claimquery

internal abstract class Statement {
    abstract fun execute(context: Map<String, Any>): Any
}
internal class VariableStatement (
    val name: String,
    private val parent: VariableStatement?
): Statement() {
    override fun execute(context: Map<String, Any>): Any {
        val myContext: Map<String, Any> = if(parent!=null) {
            @Suppress("UNCHECKED_CAST")
            parent.execute(context) as Map<String, Any>
        } else {
            context
        }

        return myContext[name] ?: throw NoSuchElementException("variable not defined in context: name=${name}")
    }
}

internal class StringLiteralStatement (
    private val value: String
): Statement() {
    override fun execute(context: Map<String, Any>): Any {
        return value
    }
}

internal class BooleanLiteralStatement (
    private val value: Boolean
): Statement() {
    override fun execute(context: Map<String, Any>): Any {
        return value
    }
}

internal class FunctionStatement (
    val name: String,
    private val params: List<Statement>,
    private val parent: VariableStatement?
): Statement() {
    override fun execute(context: Map<String, Any>): Any {
        val myContext: Map<String, Any> = if(parent!=null) {
            @Suppress("UNCHECKED_CAST")
            parent.execute(context) as Map<String, Any>
        } else {
            context
        }

        val funcValue = myContext[name] ?: throw NoSuchElementException("function not defined in context: name=${name}")
        val func = funcValue as JwtClaimFunction
        val paramValues = params.map { p -> p.execute(context) }

        return func.execute(context, paramValues)
    }
}
