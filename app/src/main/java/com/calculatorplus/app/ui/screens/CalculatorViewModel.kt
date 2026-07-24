package com.calculatorplus.app.ui.screens

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.calculatorplus.app.data.util.ExpressionEvaluator
import com.calculatorplus.app.data.util.PreferencesManager
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

class CalculatorViewModel : ViewModel() {
    var expression by mutableStateOf("")
        private set

    var resultPreview by mutableStateOf("")
        private set

    var isDegreeMode by mutableStateOf(true)

    private val formatter = DecimalFormat("#.##########", DecimalFormatSymbols(Locale.US))

    fun onKeyClick(key: String, preferencesManager: PreferencesManager? = null) {
        when (key) {
            "AC" -> {
                expression = ""
                resultPreview = ""
            }
            "⌫" -> {
                if (expression.isNotEmpty()) {
                    expression = expression.dropLast(1)
                    updatePreview()
                }
            }
            "=" -> {
                if (expression.isNotEmpty()) {
                    try {
                        val evaluator = ExpressionEvaluator(isDegreeMode)
                        val result = evaluator.evaluate(expression)
                        val formattedResult = formatResult(result)
                        
                        // Add to history
                        preferencesManager?.addHistoryEntry("$expression = $formattedResult")
                        
                        expression = formattedResult
                        resultPreview = ""
                    } catch (e: Exception) {
                        resultPreview = "Error"
                    }
                }
            }
            "()" -> {
                expression = handleParentheses(expression)
                updatePreview()
            }
            "±" -> {
                if (expression.isNotEmpty()) {
                    expression = if (expression.startsWith("-")) {
                        expression.substring(1)
                    } else {
                        "-$expression"
                    }
                    updatePreview()
                } else {
                    expression = "-"
                }
            }
            else -> {
                if (resultPreview == "Error") {
                    resultPreview = ""
                }
                expression += key
                updatePreview()
            }
        }
    }

    fun appendFunction(fn: String) {
        // Appends functions like "sin(", "cos(", "tan(", "ln(", "log("
        if (resultPreview == "Error") {
            resultPreview = ""
        }
        expression += "$fn("
        updatePreview()
    }

    fun toggleDegreeRad() {
        isDegreeMode = !isDegreeMode
        updatePreview()
    }

    private fun updatePreview() {
        if (expression.isEmpty()) {
            resultPreview = ""
            return
        }
        
        try {
            // Check if it's already a simple number (excluding functions or multiple terms)
            val number = expression.toDoubleOrNull()
            if (number != null) {
                resultPreview = ""
                return
            }
            val evaluator = ExpressionEvaluator(isDegreeMode)
            val result = evaluator.evaluate(expression)
            resultPreview = formatResult(result)
        } catch (e: Exception) {
            resultPreview = "" // Don't show preview for incomplete syntax
        }
    }

    private fun formatResult(value: Double): String {
        if (value.isNaN()) return "Error"
        if (value.isInfinite()) return "Infinity"
        return formatter.format(value)
    }

    private fun handleParentheses(expr: String): String {
        if (expr.isEmpty()) return "("
        val openCount = expr.count { it == '(' }
        val closeCount = expr.count { it == ')' }
        if (openCount > closeCount) {
            val lastChar = expr.last()
            if (lastChar.isDigit() || lastChar == ')' || lastChar == '%' || lastChar == 'π' || lastChar == 'e' || lastChar == '!') {
                return expr + ")"
            }
        }
        return expr + "("
    }
}
