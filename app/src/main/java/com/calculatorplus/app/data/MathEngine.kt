package com.calculatorplus.app.data

import kotlin.math.pow

data class BillSplitResult(
    val baseAmount: Double,
    val tipAmount: Double,
    val totalAmount: Double,
    val perPersonAmount: Double
)

data class EmiResult(
    val monthlyPayment: Double,
    val totalInterest: Double,
    val totalPayment: Double,
    val principal: Double
)

data class BmiResult(
    val bmiValue: Double,
    val category: String,
    val colorCode: Long // Hex color for status display
)

object MathEngine {

    fun calculateBillSplit(billAmount: Double, tipPercent: Double, numPeople: Int): BillSplitResult {
        val people = if (numPeople <= 0) 1 else numPeople
        val tip = billAmount * (tipPercent / 100.0)
        val total = billAmount + tip
        val perPerson = total / people
        return BillSplitResult(billAmount, tip, total, perPerson)
    }

    fun calculateEmi(principal: Double, annualRate: Double, tenureMonths: Int): EmiResult {
        if (tenureMonths <= 0) return EmiResult(0.0, 0.0, 0.0, principal)
        val r = annualRate / 12.0 / 100.0
        val n = tenureMonths.toDouble()
        
        val emi = if (r == 0.0) {
            principal / n
        } else {
            (principal * r * (1.0 + r).pow(n)) / ((1.0 + r).pow(n) - 1.0)
        }
        
        val totalPayment = emi * n
        val totalInterest = totalPayment - principal
        return EmiResult(emi, totalInterest, totalPayment, principal)
    }

    fun calculateBmi(weightKg: Double, heightCm: Double): BmiResult {
        if (heightCm <= 0.0 || weightKg <= 0.0) return BmiResult(0.0, "Invalid Inputs", 0xFF888888)
        val heightM = heightCm / 100.0
        val bmi = weightKg / (heightM * heightM)
        
        val (category, color) = when {
            bmi < 18.5 -> "Underweight" to 0xFFFFB300 // Amber
            bmi in 18.5..24.9 -> "Normal weight" to 0xFF4CAF50 // Green
            bmi in 25.0..29.9 -> "Overweight" to 0xFFFF9800 // Orange
            else -> "Obesity" to 0xFFF44336 // Red
        }
        return BmiResult(bmi, category, color)
    }
}
