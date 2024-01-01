package org.ivcode.homeportal.utils.claimquery

internal abstract class Statement(val index: Int) {
    abstract fun execute(context: Map<String, Any>): Any
}
internal class VariableStatement (
    index: Int,
    val name: String,
    private val parent: VariableStatement?
): Statement(index) {
    override fun execute(context: Map<String, Any>): Any {
        val myContext: Map<String, Any> = if(parent!=null) {
            parent.execute(context) as Map<String, Any>
        } else {
            context
        }

        return myContext[name] ?: throw NoSuchElementException("variable not defined in context: name=${name}")
    }
}

internal class StringLiteralStatement (
    index: Int,
    private val value: String
): Statement(index) {
    override fun execute(context: Map<String, Any>): Any {
        return value
    }
}

internal class BooleanLiteralStatement (
    index: Int,
    private val value: Boolean
): Statement(index) {
    override fun execute(context: Map<String, Any>): Any {
        return value
    }
}

internal class FunctionStatement (
    index: Int,
    val name: String,
    private val params: List<Statement>,
    private val parent: VariableStatement?
): Statement(index) {
    override fun execute(context: Map<String, Any>): Any {
        val myContext: Map<String, Any> = if(parent!=null) {
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
