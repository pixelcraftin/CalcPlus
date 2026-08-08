package com.calculatorplus.app.data.util

import kotlin.math.*

class ExpressionEvaluator(private val isDegreeMode: Boolean = true) {

    fun evaluate(expression: String): Double {
        val tokens = tokenize(expression)
        if (tokens.isEmpty()) return 0.0
        val parser = Parser(tokens, isDegreeMode)
        return parser.parse()
    }

    private fun tokenize(expr: String): List<String> {
        val rawTokens = mutableListOf<String>()
        var i = 0
        val cleaned = expr
            .replace(" ", "")
            .replace("×", "*")
            .replace("÷", "/")
            .replace("π", "p") // p for pi
            .replace("nCr", "C")
            .replace("nPr", "P")
            
        while (i < cleaned.length) {
            val c = cleaned[i]
            when {
                c.isDigit() || c == '.' -> {
                    val sb = StringBuilder()
                    while (i < cleaned.length && (cleaned[i].isDigit() || cleaned[i] == '.')) {
                        sb.append(cleaned[i])
                        i++
                    }
                    rawTokens.add(sb.toString())
                }
                c.isLetter() && c != 'C' && c != 'P' -> {
                    val sb = StringBuilder()
                    while (i < cleaned.length && cleaned[i].isLetter() && cleaned[i] != 'C' && cleaned[i] != 'P') {
                        sb.append(cleaned[i])
                        i++
                    }
                    rawTokens.add(sb.toString())
                }
                c == '+' || c == '-' || c == '*' || c == '/' || c == '(' || c == ')' || c == '^' || c == '%' || c == '!' || c == '√' || c == 'C' || c == 'P' -> {
                    rawTokens.add(c.toString())
                    i++
                }
                else -> {
                    i++ // Ignore
                }
            }
        }
        
        // Insert implicit multiplication tokens
        val result = mutableListOf<String>()
        for (j in 0 until rawTokens.size) {
            val current = rawTokens[j]
            result.add(current)
            if (j < rawTokens.size - 1) {
                val next = rawTokens[j + 1]
                val currentIsOperand = isNumberOrConstantOrRightBracket(current)
                val nextIsOperandOrStart = isNumberOrConstantOrLeftBracketOrFunction(next)
                if (currentIsOperand && nextIsOperandOrStart) {
                    result.add("*")
                }
            }
        }
        return result
    }

    private fun isNumberOrConstantOrRightBracket(token: String): Boolean {
        if (token == ")" || token == "p" || token == "e" || token == "%" || token == "!") return true
        val firstChar = token.firstOrNull() ?: return false
        return firstChar.isDigit() || firstChar == '.'
    }

    private fun isNumberOrConstantOrLeftBracketOrFunction(token: String): Boolean {
        if (token == "(" || token == "p" || token == "e" || token == "√") return true
        val firstChar = token.firstOrNull() ?: return false
        if (firstChar.isDigit() || firstChar == '.') return true
        return token == "sin" || token == "cos" || token == "tan" || token == "ln" || token == "log" || token == "asin" || token == "acos" || token == "atan"
    }

    private class Parser(private val tokens: List<String>, private val isDegreeMode: Boolean) {
        private var index = 0

        fun parse(): Double {
            val value = parseExpression()
            if (index < tokens.size) {
                throw IllegalArgumentException("Unexpected token: ${tokens[index]}")
            }
            return value
        }

        private fun peek(): String? {
            return if (index < tokens.size) tokens[index] else null
        }

        private fun consume(expected: String) {
            if (peek() == expected) {
                index++
            } else {
                throw IllegalArgumentException("Expected $expected but found ${peek()}")
            }
        }

        private fun parseExpression(): Double {
            var value = parseTerm()
            while (true) {
                val next = peek()
                if (next == "+" || next == "-") {
                    index++
                    val right = parseTerm()
                    if (next == "+") value += right else value -= right
                } else {
                    break
                }
            }
            return value
        }

        private fun parseTerm(): Double {
            var value = parseFactor()
            while (true) {
                val next = peek()
                if (next == "*" || next == "/") {
                    index++
                    val right = parseFactor()
                    if (next == "*") {
                        value *= right
                    } else {
                        if (right == 0.0) throw ArithmeticException("Division by zero")
                        value /= right
                    }
                } else {
                    break
                }
            }
            return value
        }

        private fun parseFactor(): Double {
            var value = parsePower()
            while (peek() == "%") {
                index++
                value /= 100.0
            }
            return value
        }

        private fun parsePower(): Double {
            var value = parseUnary()
            while (peek() == "^" || peek() == "C" || peek() == "P") {
                val op = peek()
                index++
                if (op == "^") {
                    val exponent = parseUnary()
                    value = value.pow(exponent)
                } else if (op == "C") {
                    val r = parseUnary()
                    value = combinations(value, r)
                } else if (op == "P") {
                    val r = parseUnary()
                    value = permutations(value, r)
                }
            }
            return value
        }

        private fun combinations(n: Double, r: Double): Double {
            if (n < 0 || r < 0 || n != floor(n) || r != floor(r) || r > n) {
                throw ArithmeticException("Invalid arguments for combinations")
            }
            return factorial(n) / (factorial(r) * factorial(n - r))
        }

        private fun permutations(n: Double, r: Double): Double {
            if (n < 0 || r < 0 || n != floor(n) || r != floor(r) || r > n) {
                throw ArithmeticException("Invalid arguments for permutations")
            }
            return factorial(n) / factorial(n - r)
        }

        private fun parseUnary(): Double {
            val next = peek()
            if (next == "+") {
                index++
                return parseUnary()
            }
            if (next == "-") {
                index++
                return -parseUnary()
            }
            if (next == "√") {
                index++
                val arg = parseUnary()
                if (arg < 0) throw ArithmeticException("Square root of negative number")
                return sqrt(arg)
            }
            
            var value = parsePrimary()
            while (peek() == "!") {
                index++
                value = factorial(value)
            }
            return value
        }

        private fun parsePrimary(): Double {
            val token = peek() ?: throw IllegalArgumentException("Unexpected end of expression")
            
            if (token == "(") {
                index++
                val value = parseExpression()
                consume(")")
                return value
            }

            if (token == "p") {
                index++
                return Math.PI
            }
            if (token == "e") {
                index++
                return Math.E
            }

            if (token == "sin" || token == "cos" || token == "tan" || token == "ln" || token == "log" || token == "asin" || token == "acos" || token == "atan") {
                index++
                val arg = parseUnary()
                return when (token) {
                    "sin" -> {
                        val rad = if (isDegreeMode) Math.toRadians(arg) else arg
                        sin(rad)
                    }
                    "cos" -> {
                        val rad = if (isDegreeMode) Math.toRadians(arg) else arg
                        cos(rad)
                    }
                    "tan" -> {
                        val rad = if (isDegreeMode) Math.toRadians(arg) else arg
                        val cosVal = cos(rad)
                        if (abs(cosVal) < 1e-10) throw ArithmeticException("Tangent undefined")
                        tan(rad)
                    }
                    "asin" -> {
                        if (arg < -1.0 || arg > 1.0) throw ArithmeticException("Arc sine domain error")
                        val rad = asin(arg)
                        if (isDegreeMode) Math.toDegrees(rad) else rad
                    }
                    "acos" -> {
                        if (arg < -1.0 || arg > 1.0) throw ArithmeticException("Arc cosine domain error")
                        val rad = acos(arg)
                        if (isDegreeMode) Math.toDegrees(rad) else rad
                    }
                    "atan" -> {
                        val rad = atan(arg)
                        if (isDegreeMode) Math.toDegrees(rad) else rad
                    }
                    "ln" -> {
                        if (arg <= 0) throw ArithmeticException("Logarithm of non-positive number")
                        ln(arg)
                    }
                    "log" -> {
                        if (arg <= 0) throw ArithmeticException("Logarithm of non-positive number")
                        log10(arg)
                    }
                    else -> 0.0
                }
            }

            try {
                val value = token.toDouble()
                index++
                return value
            } catch (e: NumberFormatException) {
                throw IllegalArgumentException("Invalid token: $token")
            }
        }

        private fun factorial(n: Double): Double {
            if (n < 0 || n != floor(n)) throw ArithmeticException("Factorial only defined for non-negative integers")
            if (n > 170) return Double.POSITIVE_INFINITY
            var result = 1.0
            for (i in 1..n.toInt()) {
                result *= i
            }
            return result
        }
    }
}
