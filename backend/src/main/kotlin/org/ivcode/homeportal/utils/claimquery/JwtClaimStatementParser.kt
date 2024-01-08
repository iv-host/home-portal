package org.ivcode.homeportal.utils.claimquery

internal class JwtClaimStatementParser {
    fun parse(tokens: List<Token>): Statement {
        val tokenWalker = TokenWalker(tokens)
        val statement = statementNode(tokenWalker)

        val next = tokenWalker.next()
        if(next!=null) {
            throw IllegalArgumentException("Unexpected Token: tokens found after root statement: index=${next.index}")
        }

        return statement
    }

    private fun statementNode(tokens: TokenWalker): Statement {
        val token = tokens.next()!!
        when(token.type) {
            TokenType.DOT -> {
                throw IllegalArgumentException("statements cannot start with a dot. index=${token.index}")
            }
            TokenType.OPEN_PARENTHESIS -> {
                throw IllegalArgumentException("statements cannot start with open parenthesis. index=${token.index}")
            }
            TokenType.CLOSE_PARENTHESIS -> {
                throw IllegalArgumentException("statements cannot start with close parenthesis. index=${token.index}")
            }
            TokenType.COMMA -> {
                throw IllegalArgumentException("statements cannot start with close parenthesis. index=${token.index}")
            }
            TokenType.STRING_LITERAL -> {
                return StringLiteralStatement(token.value as String)
            }
            TokenType.BOOLEAN_LITERAL -> {
                return BooleanLiteralStatement(token.value as Boolean)
            }
            TokenType.IDENTIFIER -> {
                return identifierNode(tokens)
            }
        }
    }

    private fun identifierNode(tokens: TokenWalker, parent: VariableStatement?=null): Statement {
        val identifierToken = tokens.get()!!
        if(identifierToken.type!=TokenType.IDENTIFIER) {
            throw IllegalArgumentException("unexpected token type: expected IDENTIFIER but found ${identifierToken.type}. index=${identifierToken.index}")
        }

        val token = tokens.peekAhead()!!
        when(token.type) {
            TokenType.DOT -> {
                tokens.next() // consume dot
                val next = tokens.next()!! // consume next
                if(next.type!=TokenType.IDENTIFIER) {
                    throw IllegalArgumentException("Unexpected Token: Dot separators must be followed by an identifier. index=${token.index}")
                }

                return identifierNode(tokens, VariableStatement(identifierToken.value as String, parent))
            }
            TokenType.OPEN_PARENTHESIS -> {
                tokens.next() // consume open paren
                return functionNode(tokens, identifierToken.value as String, parent)
            }
            TokenType.CLOSE_PARENTHESIS -> {
                return VariableStatement(identifierToken.value as String, parent)
            }
            TokenType.COMMA -> {
                return VariableStatement(identifierToken.value as String, parent)
            }
            TokenType.STRING_LITERAL -> {
                throw IllegalArgumentException("identifiers cannot end with a string literal. index=${token.index}")
            }
            TokenType.BOOLEAN_LITERAL -> {
                throw IllegalArgumentException("identifiers cannot end with a boolean literal. index=${token.index}")
            }
            TokenType.IDENTIFIER -> {
                throw IllegalArgumentException("identifiers cannot follow identifiers. index=${token.index}")
            }
        }
    }

    private fun functionNode(tokens: TokenWalker, name: String, parent: VariableStatement?): FunctionStatement {
        var next: Token = tokens.peekAhead()!!
        if(next.type==TokenType.CLOSE_PARENTHESIS) {
            // function without params
            return FunctionStatement(
                name = name,
                params = listOf(),
                parent = parent
            )
        }

        val params = mutableListOf<Statement>()
        do {
            params.add(statementNode(tokens))
            next = tokens.next()!!
            if(next.type!=TokenType.CLOSE_PARENTHESIS && next.type!=TokenType.COMMA) {
                throw IllegalArgumentException("Unexpected Token: function params must end in a comma or close paren. index=${next.index}")
            }
        } while (next.type!=TokenType.CLOSE_PARENTHESIS)

        return FunctionStatement(
            name = name,
            params = params,
            parent = parent
        )
    }
}
