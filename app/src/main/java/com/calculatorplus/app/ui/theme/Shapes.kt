package com.calculatorplus.app.ui.theme

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp

object CalcShapes {
    val roundedRectangle = RoundedCornerShape(24.dp)
    val circle = CircleShape

    fun getButtonShape(shapeName: String): Shape {
        return if (shapeName == "Circle") {
            circle
        } else {
            roundedRectangle
        }
    }
}
