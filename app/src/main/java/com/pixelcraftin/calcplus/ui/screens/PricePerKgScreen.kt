package com.pixelcraftin.calcplus.ui.screens

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.pixelcraftin.calcplus.data.model.CurrencyData
import com.pixelcraftin.calcplus.ui.theme.GrayText
import com.pixelcraftin.calcplus.ui.theme.RedAccent
import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode
import java.util.Locale

@Composable
fun PricePerKgScreen(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    var pricePerKgStr by remember { mutableStateOf("") }
    var budgetStr by remember { mutableStateOf("") }
    var gramsStr by remember { mutableStateOf("") }
    var resultQtyGrams by remember { mutableStateOf<String?>(null) }
    var resultCost by remember { mutableStateOf<String?>(null) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val currency = CurrencyData.currencies.find { it.code == "INR" } ?: CurrencyData.currencies[0]
    val isDark = MaterialTheme.colorScheme.background.red < 0.5f

    fun calculateQuantity() {
        val trimmedPrice = pricePerKgStr.trim()
        val trimmedBudget = budgetStr.trim()

        if (trimmedPrice.isEmpty() || trimmedBudget.isEmpty()) {
            errorMessage = "Please enter price and budget"
            resultQtyGrams = null
            return
        }
        val price = trimmedPrice.toBigDecimalOrNull()
        val budget = trimmedBudget.toBigDecimalOrNull()
        if (price == null || price <= BigDecimal.ZERO || budget == null || budget <= BigDecimal.ZERO) {
            errorMessage = "Values must be greater than zero"
            resultQtyGrams = null
            return
        }
        errorMessage = null
        val qtyKg = budget.divide(price, MathContext.DECIMAL128)
        val qtyGrams = qtyKg.multiply(BigDecimal("1000"), MathContext.DECIMAL128)

        val formattedGrams = qtyGrams.setScale(2, RoundingMode.HALF_UP).stripTrailingZeros().toPlainString()
        gramsStr = formattedGrams
        resultQtyGrams = formattedGrams
        resultCost = null
    }

    fun calculateCost() {
        val trimmedPrice = pricePerKgStr.trim()
        val trimmedGrams = gramsStr.trim()

        if (trimmedGrams.isEmpty()) {
            errorMessage = "Please enter quantity in grams"
            resultCost = null
            return
        }
        val grams = trimmedGrams.toBigDecimalOrNull()
        if (grams == null || grams <= BigDecimal.ZERO) {
            errorMessage = "Please enter a valid quantity in grams"
            resultCost = null
            return
        }
        if (trimmedPrice.isEmpty()) {
            errorMessage = "Please enter price per kg"
            resultCost = null
            return
        }
        val price = trimmedPrice.toBigDecimalOrNull()
        if (price == null || price <= BigDecimal.ZERO) {
            errorMessage = "Price must be greater than zero"
            resultCost = null
            return
        }
        errorMessage = null
        val cost = price.multiply(grams, MathContext.DECIMAL128).divide(BigDecimal("1000"), MathContext.DECIMAL128)
        val formattedCost = cost.setScale(2, RoundingMode.HALF_UP).stripTrailingZeros().toPlainString()
        resultCost = formattedCost
        budgetStr = formattedCost
    }

    fun resetAll() {
        pricePerKgStr = ""
        budgetStr = ""
        gramsStr = ""
        resultQtyGrams = null
        resultCost = null
        errorMessage = null
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Nothing OS Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "PRICE PER KG (₹)",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp,
                    letterSpacing = 1.sp
                ),
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.weight(1f))
            IconButton(onClick = ::resetAll) {
                Icon(Icons.Default.Refresh, contentDescription = "Reset", tint = GrayText)
            }
        }

        HorizontalDivider(color = GrayText.copy(alpha = 0.2f), thickness = 0.5.dp)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            NothingOSTextField(
                value = pricePerKgStr,
                onValueChange = { pricePerKgStr = it },
                placeholder = "Price per Kg (₹)",
                isDark = isDark
            )

            NothingOSTextField(
                value = budgetStr,
                onValueChange = { budgetStr = it },
                placeholder = "Your Budget (₹)",
                isDark = isDark
            )

            Button(
                onClick = ::calculateQuantity,
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isDark) Color.White else Color.Black,
                    contentColor = if (isDark) Color.Black else Color.White
                ),
                modifier = Modifier.fillMaxWidth().height(48.dp)
            ) {
                Text(
                    "CALCULATE QUANTITY",
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold, fontSize = 14.sp)
                )
            }

            NothingOSTextField(
                value = gramsStr,
                onValueChange = { gramsStr = it },
                placeholder = "Quantity in Grams",
                isDark = isDark
            )

            Button(
                onClick = ::calculateCost,
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isDark) Color.White else Color.Black,
                    contentColor = if (isDark) Color.Black else Color.White
                ),
                modifier = Modifier.fillMaxWidth().height(48.dp)
            ) {
                Text(
                    "CALCULATE COST",
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold, fontSize = 14.sp)
                )
            }

            if (errorMessage != null) {
                Text(
                    text = errorMessage!!,
                    color = RedAccent,
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium, fontSize = 13.sp)
                )
            }

            // Only show result card when calculated
            if (resultCost != null) {
                NothingOSResultCard(
                    label = "Total Cost",
                    value = "₹ $resultCost",
                    isDark = isDark
                )
            } else if (resultQtyGrams != null) {
                NothingOSResultCard(
                    label = "Calculated Quantity",
                    value = "$resultQtyGrams g",
                    isDark = isDark
                )
            }
        }
    }
}
