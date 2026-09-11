package com.pixelcraftin.calcplus.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.pixelcraftin.calcplus.ui.theme.DotoFontFamily
import com.pixelcraftin.calcplus.ui.theme.GrayText
import com.pixelcraftin.calcplus.ui.theme.RedAccent
import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode

@Composable
fun DiscountCalculatorScreen(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val isDoto = MaterialTheme.typography.titleLarge.fontFamily == DotoFontFamily
    var baseValueStr by remember { mutableStateOf("") }
    var percentageStr by remember { mutableStateOf("") }
    var isDeduct by remember { mutableStateOf(true) }
    var taxes by remember { mutableStateOf(listOf("")) }

    var finalPrice by remember { mutableStateOf<String?>(null) }
    var discountOrAdditionAmount by remember { mutableStateOf<String?>(null) }
    var totalTaxAmount by remember { mutableStateOf<String?>(null) }
    var priceBeforeTax by remember { mutableStateOf<String?>(null) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val isDark = MaterialTheme.colorScheme.background.red < 0.5f

    fun calculate() {
        if (baseValueStr.isBlank()) {
            errorMessage = "Please enter base amount"
            finalPrice = null
            return
        }
        if (percentageStr.isBlank()) {
            errorMessage = "Please enter percentage"
            finalPrice = null
            return
        }
        try {
            val base = BigDecimal(baseValueStr.trim())
            val percent = BigDecimal(percentageStr.trim())
            if (base < BigDecimal.ZERO || percent < BigDecimal.ZERO) {
                errorMessage = "Values must be positive"
                finalPrice = null
                return
            }
            errorMessage = null
            val percentRatio = percent.divide(BigDecimal("100"), MathContext.DECIMAL128)
            val diffAmount = base.multiply(percentRatio, MathContext.DECIMAL128)
            val afterPercent = if (isDeduct) {
                base.subtract(diffAmount, MathContext.DECIMAL128)
            } else {
                base.add(diffAmount, MathContext.DECIMAL128)
            }

            var totalTax = BigDecimal.ZERO
            taxes.forEach { taxStr ->
                val trimmed = taxStr.trim()
                if (trimmed.isNotBlank()) {
                    val taxRate = BigDecimal(trimmed)
                    if (taxRate > BigDecimal.ZERO) {
                        val taxVal = afterPercent.multiply(taxRate.divide(BigDecimal("100"), MathContext.DECIMAL128), MathContext.DECIMAL128)
                        totalTax = totalTax.add(taxVal, MathContext.DECIMAL128)
                    }
                }
            }
            val totalFinal = afterPercent.add(totalTax, MathContext.DECIMAL128)

            finalPrice = totalFinal.setScale(2, RoundingMode.HALF_UP).stripTrailingZeros().toPlainString()
            discountOrAdditionAmount = diffAmount.setScale(2, RoundingMode.HALF_UP).stripTrailingZeros().toPlainString()
            priceBeforeTax = afterPercent.setScale(2, RoundingMode.HALF_UP).stripTrailingZeros().toPlainString()
            totalTaxAmount = totalTax.setScale(2, RoundingMode.HALF_UP).stripTrailingZeros().toPlainString()
        } catch (e: Exception) {
            errorMessage = "Please enter valid numeric values"
            finalPrice = null
        }
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
                text = "DISCOUNT CALCULATOR",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = if (isDoto) FontWeight.Bold else FontWeight.Medium,
                    fontSize = 17.sp,
                    letterSpacing = 1.sp
                ),
                color = MaterialTheme.colorScheme.onBackground
            )
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
                value = baseValueStr,
                onValueChange = { baseValueStr = it },
                placeholder = "Base Value / Amount",
                isDark = isDark
            )

            NothingOSTextField(
                value = percentageStr,
                onValueChange = { percentageStr = it },
                placeholder = "Percentage (%)",
                isDark = isDark
            )

            // Mode Pill Selector: Deduct % vs Add %
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                val deductBg = if (isDeduct) (if (isDark) Color.White else Color.Black) else (if (isDark) Color(0xFF242426) else Color(0xFFEFEFF2))
                val deductText = if (isDeduct) (if (isDark) Color.Black else Color.White) else (if (isDark) Color(0xFFE5E5E7) else Color(0xFF1C1C1E))
                val addBg = if (!isDeduct) (if (isDark) Color.White else Color.Black) else (if (isDark) Color(0xFF242426) else Color(0xFFEFEFF2))
                val addText = if (!isDeduct) (if (isDark) Color.Black else Color.White) else (if (isDark) Color(0xFFE5E5E7) else Color(0xFF1C1C1E))

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(24.dp))
                        .background(deductBg)
                        .clickable { isDeduct = true }
                        .padding(vertical = 12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("- DEDUCT %", style = MaterialTheme.typography.bodyMedium.copy(fontWeight = if (isDoto) FontWeight.Bold else FontWeight.Medium, fontSize = 13.sp), color = deductText)
                }

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(24.dp))
                        .background(addBg)
                        .clickable { isDeduct = false }
                        .padding(vertical = 12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("+ ADD %", style = MaterialTheme.typography.bodyMedium.copy(fontWeight = if (isDoto) FontWeight.Bold else FontWeight.Medium, fontSize = 13.sp), color = addText)
                }
            }

            // Taxes
            taxes.forEachIndexed { index, taxVal ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    NothingOSTextField(
                        value = taxVal,
                        onValueChange = { updated ->
                            val newList = taxes.toMutableList()
                            newList[index] = updated
                            taxes = newList
                        },
                        placeholder = "Tax ${index + 1} (%)",
                        isDark = isDark,
                        modifier = Modifier.weight(1f)
                    )

                    if (taxes.size > 1) {
                        IconButton(
                            onClick = {
                                val newList = taxes.toMutableList()
                                newList.removeAt(index)
                                taxes = newList
                            }
                        ) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete", tint = RedAccent)
                        }
                    }
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = { taxes = taxes + "0" },
                    shape = RoundedCornerShape(24.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isDark) Color(0xFF2B2B2E) else Color(0xFFE4E4E8),
                        contentColor = if (isDark) Color.White else Color.Black
                    ),
                    modifier = Modifier.weight(1f).height(46.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("ADD TAX", style = MaterialTheme.typography.bodyMedium.copy(fontWeight = if (isDoto) FontWeight.Bold else FontWeight.Medium, fontSize = 12.5.sp))
                }

                Button(
                    onClick = ::calculate,
                    shape = RoundedCornerShape(24.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isDark) Color.White else Color.Black,
                        contentColor = if (isDark) Color.Black else Color.White
                    ),
                    modifier = Modifier.weight(1f).height(46.dp)
                ) {
                    Text("CALCULATE", style = MaterialTheme.typography.bodyMedium.copy(fontWeight = if (isDoto) FontWeight.Bold else FontWeight.Medium, fontSize = 13.sp))
                }
            }

            if (errorMessage != null) {
                Text(text = errorMessage!!, color = RedAccent, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium, fontSize = 13.sp))
            }

            NothingOSSummaryCard(title = "Discount Summary", isDark = isDark) {
                NothingOSMetricRow(
                    label = "Final Price",
                    value = finalPrice ?: "0.00",
                    isProminent = true
                )
                NothingOSMetricRow(
                    label = if (isDeduct) "Discount Amount" else "Added Amount",
                    value = discountOrAdditionAmount ?: "0.00"
                )
                NothingOSMetricRow(
                    label = "Price Before Tax",
                    value = priceBeforeTax ?: "0.00"
                )
                NothingOSMetricRow(
                    label = "Total Tax Amount",
                    value = totalTaxAmount ?: "0.00"
                )
            }
        }
    }
}
