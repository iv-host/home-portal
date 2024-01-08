package org.ivcode.homeportal.utils.claimquery

internal class StringWalker(
    private val value: String
) {
    private var index: Int = -1

    init {
        if(value.isBlank()) {
            throw IllegalArgumentException()
        }
    }

    fun getIndex() = index

    fun get(index: Int = this.index): Char? {
        return if (index<0 || index >= value.length) {
            null
        } else {
            value[index]
        }
    }

    fun next(): Char? {
        if(index<value.length) index++
        return get()
    }

    @Suppress("unused")
    fun peekAhead(lookahead: Int = 1): Char? {
        return get(index+lookahead)
    }

    @Suppress("unused")
    fun seekNextWithoutWhitespace(): Char? {
        var i = 1
        var ch = get(index+i)
        while(ch!=null && ch.isWhitespace()) {
            i++
            ch = get(index+i)
        }

        return ch
    }


    fun back(): Char? {
        if(index>0) index--
        return get()
    }

    @Suppress("unused")
    fun peekBehind(lookbehind: Int = 1): Char? {
        return get(index-lookbehind)
    }
}