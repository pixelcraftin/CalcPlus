package com.pixelcraftin.calcplus.data.util

import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode
import kotlin.math.*

class ExpressionEvaluator(private val isDegreeMode: Boolean = true) {

    companion object {
        val MATH_CONTEXT: MathContext = MathContext.DECIMAL128

        /**
         * Formats and sanitizes a BigDecimal calculation result:
         * - Rounds values to 10 decimal places to eliminate floating-point inaccuracies
         * - Strips trailing zeros and dangling decimal points (e.g. 0.5000000000 -> 0.5, 1.0 -> 1)
         * - Returns "0" cleanly for zero or negative zero
         */
        fun formatResult(value: BigDecimal): String {
            if (value.compareTo(BigDecimal.ZERO) == 0) return "0"

            var rounded = value
            if (rounded.scale() > 10) {
                rounded = rounded.setScale(10, RoundingMode.HALF_UP)
            }

            if (rounded.compareTo(BigDecimal.ZERO) == 0) return "0"

            val stripped = rounded.stripTrailingZeros()
            val plain = stripped.toPlainString()

            return if (plain.contains(".")) {
                val trimmed = plain.trimEnd('0').trimEnd('.')
                if (trimmed.isEmpty() || trimmed == "-0") "0" else trimmed
            } else {
                if (plain == "-0") "0" else plain
            }
        }

        /**
         * Formats/sanitizes numeric calculation results from string representations.
         * Keeps error messages (Error, Infinity, -Infinity, NaN) unaffected.
         */
        fun formatResult(valueStr: String): String {
            if (valueStr.isEmpty()) return ""
            if (valueStr.equals("Error", ignoreCase = true) ||
                valueStr.equals("Infinity", ignoreCase = true) ||
                valueStr.equals("-Infinity", ignoreCase = true) ||
                valueStr.equals("NaN", ignoreCase = true)
            ) {
                return valueStr
            }

            return try {
                val bd = BigDecimal(valueStr, MATH_CONTEXT)
                formatResult(bd)
            } catch (e: Exception) {
                valueStr
            }
        }
    }

    fun evaluate(expression: String): BigDecimal {
        var expr = expression
        val openCount = expr.count { it == '(' }
        val closeCount = expr.count { it == ')' }
        if (openCount > closeCount) {
            expr += ")".repeat(openCount - closeCount)
        }
        val tokens = tokenize(expr)
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
                    "sin" -> evaluateSin(argDouble, isDegreeMode)
                    "cos" -> evaluateCos(argDouble, isDegreeMode)
                    "tan" -> evaluateTan(argDouble, isDegreeMode)
                    "asin" -> evaluateAsin(argDouble, isDegreeMode)
                    "acos" -> evaluateAcos(argDouble, isDegreeMode)
                    "atan" -> evaluateAtan(argDouble, isDegreeMode)
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
                if (doubleVal.isNaN() || doubleVal.isInfinite()) {
                    throw ArithmeticException("Calculation error")
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

        private fun evaluateSin(argDouble: Double, isDegreeMode: Boolean): Double {
            if (isDegreeMode) {
                var deg = argDouble % 360.0
                if (deg < 0) deg += 360.0
                if (abs(deg - 0.0) < 1e-9 || abs(deg - 180.0) < 1e-9 || abs(deg - 360.0) < 1e-9) return 0.0
                if (abs(deg - 90.0) < 1e-9) return 1.0
                if (abs(deg - 270.0) < 1e-9) return -1.0
                if (abs(deg - 30.0) < 1e-9 || abs(deg - 150.0) < 1e-9) return 0.5
                if (abs(deg - 210.0) < 1e-9 || abs(deg - 330.0) < 1e-9) return -0.5
            }
            val rad = if (isDegreeMode) Math.toRadians(argDouble) else argDouble
            val res = sin(rad)
            return if (abs(res) < 1e-15) 0.0 else res
        }

        private fun evaluateCos(argDouble: Double, isDegreeMode: Boolean): Double {
            if (isDegreeMode) {
                var deg = argDouble % 360.0
                if (deg < 0) deg += 360.0
                if (abs(deg - 90.0) < 1e-9 || abs(deg - 270.0) < 1e-9) return 0.0
                if (abs(deg - 0.0) < 1e-9 || abs(deg - 360.0) < 1e-9) return 1.0
                if (abs(deg - 180.0) < 1e-9) return -1.0
                if (abs(deg - 60.0) < 1e-9 || abs(deg - 300.0) < 1e-9) return 0.5
                if (abs(deg - 120.0) < 1e-9 || abs(deg - 240.0) < 1e-9) return -0.5
            }
            val rad = if (isDegreeMode) Math.toRadians(argDouble) else argDouble
            val res = cos(rad)
            return if (abs(res) < 1e-15) 0.0 else res
        }

        private fun evaluateTan(argDouble: Double, isDegreeMode: Boolean): Double {
            if (isDegreeMode) {
                var deg = argDouble % 360.0
                if (deg < 0) deg += 360.0
                if (abs(deg - 90.0) < 1e-9 || abs(deg - 270.0) < 1e-9) {
                    throw ArithmeticException("Tangent undefined")
                }
                if (abs(deg - 0.0) < 1e-9 || abs(deg - 180.0) < 1e-9 || abs(deg - 360.0) < 1e-9) return 0.0
                if (abs(deg - 45.0) < 1e-9 || abs(deg - 225.0) < 1e-9) return 1.0
                if (abs(deg - 135.0) < 1e-9 || abs(deg - 315.0) < 1e-9) return -1.0
            }
            val rad = if (isDegreeMode) Math.toRadians(argDouble) else argDouble
            val cosVal = cos(rad)
            if (abs(cosVal) < 1e-15) throw ArithmeticException("Tangent undefined")
            val res = tan(rad)
            return if (abs(res) < 1e-15) 0.0 else res
        }

        private fun evaluateAsin(argDouble: Double, isDegreeMode: Boolean): Double {
            if (argDouble < -1.0 || argDouble > 1.0) throw ArithmeticException("Arc sine domain error")
            if (isDegreeMode) {
                if (abs(argDouble) < 1e-15) return 0.0
                if (abs(argDouble - 0.5) < 1e-15) return 30.0
                if (abs(argDouble - (-0.5)) < 1e-15) return -30.0
                if (abs(argDouble - 1.0) < 1e-15) return 90.0
                if (abs(argDouble - (-1.0)) < 1e-15) return -90.0
            }
            val rad = asin(argDouble)
            val res = if (isDegreeMode) Math.toDegrees(rad) else rad
            return if (abs(res) < 1e-15) 0.0 else res
        }

        private fun evaluateAcos(argDouble: Double, isDegreeMode: Boolean): Double {
            if (argDouble < -1.0 || argDouble > 1.0) throw ArithmeticException("Arc cosine domain error")
            if (isDegreeMode) {
                if (abs(argDouble - 1.0) < 1e-15) return 0.0
                if (abs(argDouble - 0.5) < 1e-15) return 60.0
                if (abs(argDouble) < 1e-15) return 90.0
                if (abs(argDouble - (-0.5)) < 1e-15) return 120.0
                if (abs(argDouble - (-1.0)) < 1e-15) return 180.0
            }
            val rad = acos(argDouble)
            val res = if (isDegreeMode) Math.toDegrees(rad) else rad
            return if (abs(res) < 1e-15) 0.0 else res
        }

        private fun evaluateAtan(argDouble: Double, isDegreeMode: Boolean): Double {
            if (isDegreeMode) {
                if (abs(argDouble) < 1e-15) return 0.0
                if (abs(argDouble - 1.0) < 1e-15) return 45.0
                if (abs(argDouble - (-1.0)) < 1e-15) return -45.0
            }
            val rad = atan(argDouble)
            val res = if (isDegreeMode) Math.toDegrees(rad) else rad
            return if (abs(res) < 1e-15) 0.0 else res
        }
    }
}
