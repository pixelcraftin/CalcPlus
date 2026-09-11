package com.pixelcraftin.calcplus

import com.pixelcraftin.calcplus.data.util.ExpressionEvaluator
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test
import java.math.BigDecimal

class ExpressionEvaluatorTest {

    @Test
    fun testSin30InDegreeMode() {
        val evaluator = ExpressionEvaluator(isDegreeMode = true)
        val result = evaluator.evaluate("sin(30)")
        val formatted = ExpressionEvaluator.formatResult(result)
        assertEquals("0.5", formatted)
    }

    @Test
    fun testCos60InDegreeMode() {
        val evaluator = ExpressionEvaluator(isDegreeMode = true)
        val result = evaluator.evaluate("cos(60)")
        val formatted = ExpressionEvaluator.formatResult(result)
        assertEquals("0.5", formatted)
    }

    @Test
    fun testTan45InDegreeMode() {
        val evaluator = ExpressionEvaluator(isDegreeMode = true)
        val result = evaluator.evaluate("tan(45)")
        val formatted = ExpressionEvaluator.formatResult(result)
        assertEquals("1", formatted)
    }

    @Test
    fun testCos90InDegreeMode() {
        val evaluator = ExpressionEvaluator(isDegreeMode = true)
        val result = evaluator.evaluate("cos(90)")
        val formatted = ExpressionEvaluator.formatResult(result)
        assertEquals("0", formatted)
    }

    @Test
    fun testSin180InDegreeMode() {
        val evaluator = ExpressionEvaluator(isDegreeMode = true)
        val result = evaluator.evaluate("sin(180)")
        val formatted = ExpressionEvaluator.formatResult(result)
        assertEquals("0", formatted)
    }

    @Test
    fun testTan90Undefined() {
        val evaluator = ExpressionEvaluator(isDegreeMode = true)
        assertThrows(ArithmeticException::class.java) {
            evaluator.evaluate("tan(90)")
        }
    }

    @Test
    fun testAutoCloseParentheses() {
        val evaluator = ExpressionEvaluator(isDegreeMode = true)
        val result = evaluator.evaluate("sin(30")
        val formatted = ExpressionEvaluator.formatResult(result)
        assertEquals("0.5", formatted)
    }

    @Test
    fun testFormatResultTrailingZerosAndDecimals() {
        assertEquals("0.5", ExpressionEvaluator.formatResult(BigDecimal("0.5000000000")))
        assertEquals("1", ExpressionEvaluator.formatResult(BigDecimal("1.0")))
        assertEquals("0", ExpressionEvaluator.formatResult(BigDecimal("0.0000000000")))
        assertEquals("0.1", ExpressionEvaluator.formatResult(BigDecimal("0.1000000000")))
        assertEquals("123", ExpressionEvaluator.formatResult(BigDecimal("123.000")))
    }

    @Test
    fun testFormatResultStringOverloadAndErrors() {
        assertEquals("Error", ExpressionEvaluator.formatResult("Error"))
        assertEquals("Infinity", ExpressionEvaluator.formatResult("Infinity"))
        assertEquals("-Infinity", ExpressionEvaluator.formatResult("-Infinity"))
        assertEquals("NaN", ExpressionEvaluator.formatResult("NaN"))
        assertEquals("0.5", ExpressionEvaluator.formatResult("0.5000000000"))
        assertEquals("1", ExpressionEvaluator.formatResult("1.0000000000"))
    }

    @Test
    fun testFloatingPrecisionSubtraction() {
        val evaluator = ExpressionEvaluator(isDegreeMode = true)
        val result = evaluator.evaluate("10000000000.1 - 10000000000")
        val formatted = ExpressionEvaluator.formatResult(result)
        assertEquals("0.1", formatted)
    }
}
