package org.ivcode.homeportal.utils.claimquery

internal class JwtClaimTokenParser {
    fun parse(query: String): List<Token> {
        val queryStringWalker = StringWalker(query)
        val tokens = mutableListOf<Token>()

        rootTokenizerNode(queryStringWalker, tokens)

        return tokens
    }

    private fun rootTokenizerNode(query: StringWalker, tokens: MutableList<Token>) {
        while (query.next()!=null) {
            val ch: Char = query.get() ?: throw IllegalStateException()
            if(ch.isWhitespace()) {
                continue
            }

            when (ch) {
                '(' -> {
                    tokens.add(Token('(', query.getIndex(), TokenType.OPEN_PARENTHESIS))
                }
                ')' -> {
                    tokens.add(Token(')', query.getIndex(), TokenType.CLOSE_PARENTHESIS))
                }
                '.' -> {
                    tokens.add(Token('.', query.getIndex(), TokenType.DOT))
                }
                ',' -> {
                    tokens.add(Token(',', query.getIndex(), TokenType.COMMA))
                }
                '"' -> {
                    stringLiteralTokenizerNode(query, tokens)
                }
                else -> {
                    identifierOrBooleanLiteralTokenizerNode(query, tokens)
                }
            }
        }
    }

    private fun stringLiteralTokenizerNode(query: StringWalker, tokens: MutableList<Token>) {
        val startIndex = query.getIndex()
        val stringBuilder = StringBuilder()

        while(query.next()!='"') {
            val ch: Char = query.get()
                ?: throw IllegalArgumentException("Unexpected end of stream when parsing string literal. index=${query.getIndex()}")

            stringBuilder.append(ch)
        }

        tokens.add(Token(stringBuilder.toString(), startIndex, TokenType.STRING_LITERAL))
    }

    private fun identifierOrBooleanLiteralTokenizerNode(query: StringWalker, tokens: MutableList<Token>) {
        val terminatorChars = listOf('"', '(', ')', '.', ',')

        val startIndex = query.getIndex()

        val stringBuilder = StringBuilder()
        stringBuilder.append(query.get())

        while(query.next()!=null) {
            val ch: Char = query.get() ?: throw IllegalStateException()

            if(ch.isWhitespace() || terminatorChars.contains(ch)) {
                query.back()

                val value = stringBuilder.toString()
                if(value=="true" || value == "false") {
                    tokens.add(Token(value.toBoolean(), startIndex, TokenType.BOOLEAN_LITERAL))
                } else {
                    tokens.add(Token(value, startIndex, TokenType.IDENTIFIER))
                }
                return
            } else {
                stringBuilder.append(ch)
            }
        }

        // EOF is a valid terminator
        val value = stringBuilder.toString()
        if(value=="true" || value == "false") {
            tokens.add(Token(value.toBoolean(), startIndex, TokenType.BOOLEAN_LITERAL))
        } else {
            tokens.add(Token(value, startIndex, TokenType.IDENTIFIER))
        }
    }
}

enum class TokenType {
    IDENTIFIER,
    OPEN_PARENTHESIS,
    CLOSE_PARENTHESIS,
    DOT,
    COMMA,
    STRING_LITERAL,
    BOOLEAN_LITERAL
}
data class Token (
    val value: Any,
    val index: Int,
    val type: TokenType
)
