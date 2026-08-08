package com.calculatorplus.app.data

object Converters {
    fun convert(value: Double, fromUnit: UnitItem, toUnit: UnitItem): Double {
        if (fromUnit.category != toUnit.category) return 0.0
        
        // 1. Convert from source unit to base unit
        val baseValue = if (fromUnit.category == "Temperature") {
            (value - fromUnit.offset) * fromUnit.factorToBase
        } else {
            value * fromUnit.factorToBase
        }
        
        // 2. Convert from base unit to target unit
        val result = if (toUnit.category == "Temperature") {
            (baseValue / toUnit.factorToBase) + toUnit.offset
        } else {
            baseValue / toUnit.factorToBase
        }
        
        return result
    }
}
