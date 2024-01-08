package org.ivcode.homeportal.utils.claimquery

internal class TokenWalker(
    private val tokens: List<Token>
) {
    private var index: Int = -1

    @Suppress("unused")
    fun getIndex() = index

    fun get(index: Int = this.index): Token? {
        return if (index<0 || index >= tokens.size) {
            null
        } else {
            tokens[index]
        }
    }

    fun next(): Token? {
        if(index<tokens.size) index++
        return get()
    }

    fun peekAhead(lookahead: Int = 1): Token? {
        return get(index+lookahead)
    }

    @Suppress("unused")
    fun back(): Token? {
        if(index>0) index--
        return get()
    }

    @Suppress("unused")
    fun peekBehind(lookbehind: Int = 1): Token? {
        return get(index-lookbehind)
    }
}