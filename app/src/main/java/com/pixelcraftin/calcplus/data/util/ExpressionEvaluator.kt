package com.pixelcraftin.calcplus.data.util

import java.math.BigDecimal
import java.math.MathContext
import kotlin.math.*

class ExpressionEvaluator(private val isDegreeMode: Boolean = true) {

    companion object {
        val MATH_CONTEXT: MathContext = MathContext.DECIMAL128

        fun formatResult(value: BigDecimal): String {
            if (value.compareTo(BigDecimal.ZERO) == 0) return "0"
            val stripped = value.stripTrailingZeros()
            return stripped.toPlainString()
        }
    }

    fun evaluate(expression: String): BigDecimal {
        val tokens = tokenize(expression)
        if (tokens.isEmpty()) return BigDecimal.ZERO
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

        fun parse(): BigDecimal {
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

        private fun parseExpression(): BigDecimal {
            var value = parseTerm()
            while (true) {
                val next = peek()
                if (next == "+" || next == "-") {
                    index++
                    val right = parseTerm()
                    value = if (next == "+") {
                        value.add(right, MATH_CONTEXT)
                    } else {
                        value.subtract(right, MATH_CONTEXT)
                    }
                } else {
                    break
                }
            }
            return value
        }

        private fun parseTerm(): BigDecimal {
            var value = parseFactor()
            while (true) {
                val next = peek()
                if (next == "*" || next == "/") {
                    index++
                    val right = parseFactor()
                    if (next == "*") {
                        value = value.multiply(right, MATH_CONTEXT)
                    } else {
                        if (right.compareTo(BigDecimal.ZERO) == 0) {
                            throw ArithmeticException("Division by zero")
                        }
                        value = value.divide(right, MATH_CONTEXT)
                    }
                } else {
                    break
                }
            }
            return value
        }

        private fun parseFactor(): BigDecimal {
            var value = parsePower()
            while (peek() == "%") {
                index++
                value = value.divide(BigDecimal("100"), MATH_CONTEXT)
            }
            return value
        }

        private fun parsePower(): BigDecimal {
            var value = parseUnary()
            while (peek() == "^" || peek() == "C" || peek() == "P") {
                val op = peek()
                index++
                if (op == "^") {
                    val exponent = parseUnary()
                    value = power(value, exponent)
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

        private fun power(base: BigDecimal, exponent: BigDecimal): BigDecimal {
            val strippedExp = exponent.stripTrailingZeros()
            if (strippedExp.scale() <= 0) {
                try {
                    val intExp = strippedExp.intValueExact()
                    if (intExp in 0..9999) {
                        return base.pow(intExp, MATH_CONTEXT)
                    } else if (intExp in -9999..-1) {
                        return BigDecimal.ONE.divide(base.pow(-intExp, MATH_CONTEXT), MATH_CONTEXT)
                    }
                } catch (e: Exception) {
                    // Fallback to double pow
                }
            }
            val res = base.toDouble().pow(exponent.toDouble())
            if (res.isNaN() || res.isInfinite()) {
                throw ArithmeticException("Invalid power calculation")
            }
            return BigDecimal(res.toString(), MATH_CONTEXT)
        }

        private fun combinations(n: BigDecimal, r: BigDecimal): BigDecimal {
            val nStripped = n.stripTrailingZeros()
            val rStripped = r.stripTrailingZeros()
            if (n < BigDecimal.ZERO || r < BigDecimal.ZERO || nStripped.scale() > 0 || rStripped.scale() > 0 || r > n) {
                throw ArithmeticException("Invalid arguments for combinations")
            }
            val nFact = factorial(n)
            val rFact = factorial(r)
            val nMinusRFact = factorial(n.subtract(r, MATH_CONTEXT))
            val denom = rFact.multiply(nMinusRFact, MATH_CONTEXT)
            return nFact.divide(denom, MATH_CONTEXT)
        }

        private fun permutations(n: BigDecimal, r: BigDecimal): BigDecimal {
            val nStripped = n.stripTrailingZeros()
            val rStripped = r.stripTrailingZeros()
            if (n < BigDecimal.ZERO || r < BigDecimal.ZERO || nStripped.scale() > 0 || rStripped.scale() > 0 || r > n) {
                throw ArithmeticException("Invalid arguments for permutations")
            }
            val nFact = factorial(n)
            val nMinusRFact = factorial(n.subtract(r, MATH_CONTEXT))
            return nFact.divide(nMinusRFact, MATH_CONTEXT)
        }

        private fun parseUnary(): BigDecimal {
            val next = peek()
            if (next == "+") {
                index++
                return parseUnary()
            }
            if (next == "-") {
                index++
                return parseUnary().negate(MATH_CONTEXT)
            }
            if (next == "√") {
                index++
                val arg = parseUnary()
                if (arg < BigDecimal.ZERO) throw ArithmeticException("Square root of negative number")
                val res = sqrt(arg.toDouble())
                if (res.isNaN() || res.isInfinite()) {
                    throw ArithmeticException("Invalid square root")
                }
                return BigDecimal(res.toString(), MATH_CONTEXT)
            }
            
            var value = parsePrimary()
            while (peek() == "!") {
                index++
                value = factorial(value)
            }
            return value
        }

        private fun parsePrimary(): BigDecimal {
            val token = peek() ?: throw IllegalArgumentException("Unexpected end of expression")
            
            if (token == "(") {
                index++
                val value = parseExpression()
                consume(")")
                return value
            }

            if (token == "p") {
                index++
                return BigDecimal("3.1415926535897932384626433832795028841971693993751")
            }
            if (token == "e") {
                index++
                return BigDecimal("2.7182818284590452353602874713526624977572470936999")
            }

            if (token == "sin" || token == "cos" || token == "tan" || token == "ln" || token == "log" || token == "asin" || token == "acos" || token == "atan") {
                index++
                val arg = parseUnary()
                val argDouble = arg.toDouble()
                val doubleVal = when (token) {
                    "sin" -> {
                        val rad = if (isDegreeMode) Math.toRadians(argDouble) else argDouble
                        sin(rad)
                    }
                    "cos" -> {
                        val rad = if (isDegreeMode) Math.toRadians(argDouble) else argDouble
                        cos(rad)
                    }
                    "tan" -> {
                        val rad = if (isDegreeMode) Math.toRadians(argDouble) else argDouble
                        val cosVal = cos(rad)
                        if (abs(cosVal) < 1e-10) throw ArithmeticException("Tangent undefined")
                        tan(rad)
                    }
                    "asin" -> {
                        if (argDouble < -1.0 || argDouble > 1.0) throw ArithmeticException("Arc sine domain error")
                        val rad = asin(argDouble)
                        if (isDegreeMode) Math.toDegrees(rad) else rad
                    }
                    "acos" -> {
                        if (argDouble < -1.0 || argDouble > 1.0) throw ArithmeticException("Arc cosine domain error")
                        val rad = acos(argDouble)
                        if (isDegreeMode) Math.toDegrees(rad) else rad
                    }
                    "atan" -> {
                        val rad = atan(argDouble)
                        if (isDegreeMode) Math.toDegrees(rad) else rad
                    }
                    "ln" -> {
                        if (argDouble <= 0) throw ArithmeticException("Logarithm of non-positive number")
                        ln(argDouble)
                    }
                    "log" -> {
                        if (argDouble <= 0) throw ArithmeticException("Logarithm of non-positive number")
                        log10(argDouble)
                    }
                    else -> 0.0
                }
                return BigDecimal(doubleVal.toString(), MATH_CONTEXT)
            }

            try {
                // Parse directly into BigDecimal(stringVal) rather than converting through toDouble()
                val value = BigDecimal(token, MATH_CONTEXT)
                index++
                return value
            } catch (e: NumberFormatException) {
                throw IllegalArgumentException("Invalid token: $token")
            }
        }

        private fun factorial(n: BigDecimal): BigDecimal {
            val stripped = n.stripTrailingZeros()
            if (n < BigDecimal.ZERO || stripped.scale() > 0) {
                throw ArithmeticException("Factorial only defined for non-negative integers")
            }
            val intVal = try {
                stripped.intValueExact()
            } catch (e: ArithmeticException) {
                throw ArithmeticException("Factorial overflow")
            }
            if (intVal > 170) {
                throw ArithmeticException("Factorial overflow")
            }
            var result = BigDecimal.ONE
            for (i in 1..intVal) {
                result = result.multiply(BigDecimal(i), MATH_CONTEXT)
            }
            return result
        }
    }
}
