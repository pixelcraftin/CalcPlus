package com.pixelcraftin.calcplus.ui.screens

import android.app.DatePickerDialog
import android.content.res.Configuration
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.pixelcraftin.calcplus.data.MathEngine
import com.pixelcraftin.calcplus.data.model.CurrencyData
import com.pixelcraftin.calcplus.data.model.CurrencyModel
import com.pixelcraftin.calcplus.ui.components.CurrencyPickerDialog
import com.pixelcraftin.calcplus.ui.theme.DotoFontFamily
import com.pixelcraftin.calcplus.ui.theme.GrayText
import com.pixelcraftin.calcplus.ui.theme.RedAccent
import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Locale

@Composable
fun UtilitiesScreen(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val haptic = LocalHapticFeedback.current
    var activeSubTab by remember { mutableStateOf("PRICE/KG") }
    
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE || configuration.screenWidthDp > 600

    val allTabs = listOf(
        "PRICE/KG" to "PRICE/KG",
        "AGE" to "AGE",
        "EMI" to "EMI",
        "SPLITTER" to "SPLITTER",
        "BMI" to "BMI",
        "DISCOUNT" to "DISCOUNT"
    )
    val row1Tabs = listOf(
        "PRICE/KG" to "PRICE/KG",
        "AGE" to "AGE",
        "EMI" to "EMI"
    )
    val row2Tabs = listOf(
        "SPLITTER" to "SPLITTER",
        "BMI" to "BMI",
        "DISCOUNT" to "DISCOUNT"
    )

    var selectedCurrency by remember { mutableStateOf(CurrencyData.currencies[0]) }
    var showCurrencyDialog by remember { mutableStateOf(false) }

    val isDark = MaterialTheme.colorScheme.background.red < 0.5f

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 14.dp, vertical = if (isLandscape) 4.dp else 8.dp)
    ) {
        // Tab Selector Row
        if (isLandscape) {
            // Single-row rounded pill buttons with equal weighting (Modifier.weight(1f))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                allTabs.forEach { (tabId, label) ->
                    val isSelected = tabId == activeSubTab
                    val tabBg = if (isSelected) {
                        if (isDark) Color.White else Color(0xFF000000)
                    } else {
                        if (isDark) Color(0xFF242426) else Color(0xFFEFEFF2)
                    }
                    val tabTextColor = if (isSelected) {
                        if (isDark) Color.Black else Color(0xFFFFFFFF)
                    } else {
                        if (isDark) Color(0xFFFFFFFF) else Color(0xFF000000)
                    }

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(12.dp))
                            .background(tabBg)
                            .clickable {
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                activeSubTab = tabId
                            }
                            .padding(vertical = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        val isDoto = MaterialTheme.typography.labelMedium.fontFamily == com.pixelcraftin.calcplus.ui.theme.DotoFontFamily
                        Text(
                            text = label,
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = if (isDoto) FontWeight.Bold else FontWeight.Medium,
                                fontSize = 11.5.sp,
                                letterSpacing = 0.5.sp
                            ),
                            color = tabTextColor,
                            maxLines = 1,
                            softWrap = false
                        )
                    }
                }
            }
        } else {
            // Portrait: 2 rows of 3 equal boxes
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Row 1: price/kg, age, emi
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    row1Tabs.forEach { (tabId, label) ->
                        val isSelected = tabId == activeSubTab
                        val tabBg = if (isSelected) {
                            if (isDark) Color.White else Color(0xFF000000)
                        } else {
                            if (isDark) Color(0xFF242426) else Color(0xFFEFEFF2)
                        }
                        val tabTextColor = if (isSelected) {
                            if (isDark) Color.Black else Color(0xFFFFFFFF)
                        } else {
                            if (isDark) Color(0xFFFFFFFF) else Color(0xFF000000)
                        }

                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(12.dp))
                                .background(tabBg)
                                .clickable {
                                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                    activeSubTab = tabId
                                }
                                .padding(vertical = 10.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            val isDoto = MaterialTheme.typography.labelMedium.fontFamily == com.pixelcraftin.calcplus.ui.theme.DotoFontFamily
                            Text(
                                text = label,
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = if (isDoto) FontWeight.Bold else FontWeight.Medium,
                                    fontSize = 12.5.sp,
                                    letterSpacing = 0.5.sp
                                ),
                                color = tabTextColor,
                                maxLines = 1
                            )
                        }
                    }
                }

                // Row 2: splitter, bmi, discount
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    row2Tabs.forEach { (tabId, label) ->
                        val isSelected = tabId == activeSubTab
                        val tabBg = if (isSelected) {
                            if (isDark) Color.White else Color(0xFF000000)
                        } else {
                            if (isDark) Color(0xFF242426) else Color(0xFFEFEFF2)
                        }
                        val tabTextColor = if (isSelected) {
                            if (isDark) Color.Black else Color(0xFFFFFFFF)
                        } else {
                            if (isDark) Color(0xFFFFFFFF) else Color(0xFF000000)
                        }

                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(12.dp))
                                .background(tabBg)
                                .clickable {
                                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                    activeSubTab = tabId
                                }
                                .padding(vertical = 10.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            val isDoto = MaterialTheme.typography.labelMedium.fontFamily == com.pixelcraftin.calcplus.ui.theme.DotoFontFamily
                            Text(
                                text = label,
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = if (isDoto) FontWeight.Bold else FontWeight.Medium,
                                    fontSize = 12.5.sp,
                                    letterSpacing = 0.5.sp
                                ),
                                color = tabTextColor,
                                maxLines = 1
                            )
                        }
                    }
                }
            }
        }

        // Shared Currency Selector Bar for financial calculators
        if (activeSubTab == "EMI" || activeSubTab == "SPLITTER" || activeSubTab == "PRICE/KG" || activeSubTab == "DISCOUNT") {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = if (isLandscape) 8.dp else 12.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(if (isDark) Color(0xFF1E1E20) else Color(0xFFF2F2F4))
                    .border(
                        BorderStroke(
                            1.dp,
                            if (isDark) Color(0xFF333336) else Color(0xFFE5E5EA)
                        ),
                        RoundedCornerShape(12.dp)
                    )
                    .clickable { showCurrencyDialog = true }
                    .padding(horizontal = 16.dp, vertical = if (isLandscape) 8.dp else 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                val isDoto = MaterialTheme.typography.labelSmall.fontFamily == com.pixelcraftin.calcplus.ui.theme.DotoFontFamily
                Text(
                    text = "CURRENCY SYMBOL",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = if (isDoto) FontWeight.Bold else FontWeight.Medium,
                        fontSize = 12.sp,
                        letterSpacing = 1.sp
                    ),
                    color = GrayText
                )
                Text(
                    text = "${selectedCurrency.name} (${selectedCurrency.symbol})",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = if (isDoto) FontWeight.Bold else FontWeight.Medium,
                        fontSize = 13.5.sp
                    ),
                    color = if (isDark) Color.White else Color(0xFF111111)
                )
            }
        }

        // Active Calculator
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            Crossfade(
                targetState = activeSubTab,
                animationSpec = tween(200),
                label = "NothingOSSubTabCrossfade"
            ) { subTab ->
                when (subTab) {
                    "PRICE/KG" -> PricePerKgNothingOSContent(currency = selectedCurrency, isDark = isDark, isLandscape = isLandscape)
                    "AGE" -> AgeNothingOSContent(isDark = isDark, isLandscape = isLandscape)
                    "EMI" -> EmiNothingOSContent(currency = selectedCurrency, isDark = isDark, isLandscape = isLandscape)
                    "SPLITTER" -> SplitterNothingOSContent(currency = selectedCurrency, isDark = isDark, isLandscape = isLandscape)
                    "BMI" -> BmiNothingOSContent(isDark = isDark, isLandscape = isLandscape)
                    "DISCOUNT" -> DiscountNothingOSContent(currency = selectedCurrency, isDark = isDark, isLandscape = isLandscape)
                }
            }
        }
    }

    if (showCurrencyDialog) {
        CurrencyPickerDialog(
            selectedCurrency = selectedCurrency,
            onDismiss = { showCurrencyDialog = false },
            onSelect = {
                selectedCurrency = it
                showCurrencyDialog = false
            }
        )
    }
}

// ── Common Input Field ─────────────────────────────────
@Composable
fun NothingOSTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    isDark: Boolean,
    modifier: Modifier = Modifier,
    keyboardType: KeyboardType = KeyboardType.Decimal
) {
    val borderColor = if (isDark) Color(0xFF3A3A3D) else Color(0xFFD1D1D6)
    val bgColor = if (isDark) Color(0xFF141416) else Color.White
    val textColor = if (isDark) Color.White else Color(0xFF111111)
    val isDoto = MaterialTheme.typography.titleLarge.fontFamily == DotoFontFamily

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(bgColor)
            .border(1.dp, borderColor, RoundedCornerShape(8.dp))
            .padding(horizontal = 14.dp, vertical = 14.dp)
    ) {
        if (value.isEmpty()) {
            Text(
                text = placeholder,
                style = MaterialTheme.typography.bodyMedium,
                fontSize = 14.sp,
                color = GrayText
            )
        }
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            textStyle = MaterialTheme.typography.bodyMedium.copy(
                fontSize = 15.sp,
                fontWeight = if (isDoto) FontWeight.Bold else FontWeight.Medium,
                color = textColor
            ),
            cursorBrush = SolidColor(if (isDark) Color.White else Color.Black),
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

// ── Common Result Card ───────────────────────────────────────────
@Composable
fun NothingOSResultCard(
    label: String,
    value: String,
    isDark: Boolean,
    modifier: Modifier = Modifier
) {
    val cardBg = if (isDark) Color(0xFF1B1B1E) else Color(0xFFF7F7F8)
    val borderColor = if (isDark) Color(0xFF333336) else Color(0xFFE5E5EA)
    val valueColor = if (isDark) Color.White else Color(0xFF111111)
    val isDoto = MaterialTheme.typography.titleLarge.fontFamily == DotoFontFamily

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = cardBg),
        border = BorderStroke(1.dp, borderColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = label.uppercase(),
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = if (isDoto) FontWeight.Bold else FontWeight.Medium,
                    letterSpacing = 1.5.sp
                ),
                color = GrayText
            )
            Text(
                text = value,
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = if (isDoto) FontWeight.Bold else FontWeight.Medium,
                    fontSize = 28.sp,
                    letterSpacing = 0.5.sp
                ),
                color = valueColor
            )
        }
    }
}

// ── Common Breakdown Summary Card ─────────────────────────────────
@Composable
fun NothingOSSummaryCard(
    title: String,
    isDark: Boolean,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    val cardBg = if (isDark) Color(0xFF1B1B1E) else Color(0xFFF7F7F8)
    val borderColor = if (isDark) Color(0xFF333336) else Color(0xFFE5E5EA)
    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = cardBg),
        border = BorderStroke(1.dp, borderColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            val isDoto = MaterialTheme.typography.labelSmall.fontFamily == com.pixelcraftin.calcplus.ui.theme.DotoFontFamily
            Text(
                text = title.uppercase(),
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = if (isDoto) FontWeight.Bold else FontWeight.Medium,
                    fontSize = 12.sp,
                    letterSpacing = 1.2.sp
                ),
                color = GrayText
            )
            content()
        }
    }
}

// ── Common Metric Row ─────────────────────────────────────────────
@Composable
fun NothingOSMetricRow(
    label: String,
    value: String,
    isProminent: Boolean = false,
    textColor: Color = MaterialTheme.colorScheme.onSurface
) {
    val isDoto = MaterialTheme.typography.bodyMedium.fontFamily == com.pixelcraftin.calcplus.ui.theme.DotoFontFamily
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontSize = if (isProminent) 15.sp else 13.5.sp,
                fontWeight = if (isProminent) (if (isDoto) FontWeight.Bold else FontWeight.Medium) else FontWeight.Normal
            ),
            color = if (isProminent) textColor else GrayText
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontSize = if (isProminent) 17.sp else 14.sp,
                fontWeight = if (isDoto) FontWeight.Bold else FontWeight.Medium
            ),
            color = textColor
        )
    }
}

// ── 1. Price Per Kg Calculator ───────────────────────────────
@Composable
fun PricePerKgNothingOSContent(currency: CurrencyModel, isDark: Boolean, isLandscape: Boolean = false) {
    var pricePerKgStr by remember { mutableStateOf("") }
    var budgetStr by remember { mutableStateOf("") }
    var gramsStr by remember { mutableStateOf("") }

    var resultQtyGrams by remember { mutableStateOf<String?>(null) }
    var resultCost by remember { mutableStateOf<String?>(null) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

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

    if (isLandscape) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.Top
        ) {
            // Left Column (Inputs & Actions)
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                NothingOSTextField(
                    value = pricePerKgStr,
                    onValueChange = { pricePerKgStr = it },
                    placeholder = "Price per Kg (${currency.symbol})",
                    isDark = isDark
                )

                NothingOSTextField(
                    value = budgetStr,
                    onValueChange = { budgetStr = it },
                    placeholder = "Your Budget (${currency.symbol})",
                    isDark = isDark
                )

                Button(
                    onClick = ::calculateQuantity,
                    shape = RoundedCornerShape(24.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isDark) Color.White else Color.Black,
                        contentColor = if (isDark) Color.Black else Color.White
                    ),
                    modifier = Modifier.fillMaxWidth().height(44.dp)
                ) {
                    val isDoto = MaterialTheme.typography.bodyMedium.fontFamily == com.pixelcraftin.calcplus.ui.theme.DotoFontFamily
                    Text(
                        "CALCULATE QUANTITY",
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = if (isDoto) FontWeight.Bold else FontWeight.Medium, fontSize = 13.sp)
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
                    shape = RoundedCornerShape(24.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isDark) Color.White else Color.Black,
                        contentColor = if (isDark) Color.Black else Color.White
                    ),
                    modifier = Modifier.fillMaxWidth().height(44.dp)
                ) {
                    val isDoto = MaterialTheme.typography.bodyMedium.fontFamily == com.pixelcraftin.calcplus.ui.theme.DotoFontFamily
                    Text(
                        "CALCULATE COST",
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = if (isDoto) FontWeight.Bold else FontWeight.Medium, fontSize = 13.sp)
                    )
                }

                if (errorMessage != null) {
                    Text(
                        text = errorMessage!!,
                        color = RedAccent,
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium, fontSize = 13.sp)
                    )
                }
            }

            // Right Column (Result Display Card)
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (resultCost != null) {
                    NothingOSResultCard(
                        label = "Total Cost",
                        value = "${currency.symbol} $resultCost",
                        isDark = isDark
                    )
                } else if (resultQtyGrams != null) {
                    NothingOSResultCard(
                        label = "Calculated Quantity",
                        value = "$resultQtyGrams g",
                        isDark = isDark
                    )
                } else {
                    NothingOSResultCard(
                        label = "Calculation Result",
                        value = "—",
                        isDark = isDark
                    )
                }
            }
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            NothingOSTextField(
                value = pricePerKgStr,
                onValueChange = { pricePerKgStr = it },
                placeholder = "Price per Kg (${currency.symbol})",
                isDark = isDark
            )

            NothingOSTextField(
                value = budgetStr,
                onValueChange = { budgetStr = it },
                placeholder = "Your Budget (${currency.symbol})",
                isDark = isDark
            )

            Button(
                onClick = ::calculateQuantity,
                shape = RoundedCornerShape(24.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isDark) Color.White else Color.Black,
                    contentColor = if (isDark) Color.Black else Color.White
                ),
                modifier = Modifier.fillMaxWidth().height(48.dp)
            ) {
                val isDoto = MaterialTheme.typography.bodyMedium.fontFamily == com.pixelcraftin.calcplus.ui.theme.DotoFontFamily
                Text(
                    "CALCULATE QUANTITY",
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = if (isDoto) FontWeight.Bold else FontWeight.Medium, fontSize = 14.sp)
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
                shape = RoundedCornerShape(24.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isDark) Color.White else Color.Black,
                    contentColor = if (isDark) Color.Black else Color.White
                ),
                modifier = Modifier.fillMaxWidth().height(48.dp)
            ) {
                val isDoto = MaterialTheme.typography.bodyMedium.fontFamily == com.pixelcraftin.calcplus.ui.theme.DotoFontFamily
                Text(
                    "CALCULATE COST",
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = if (isDoto) FontWeight.Bold else FontWeight.Medium, fontSize = 14.sp)
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
                    value = "${currency.symbol} $resultCost",
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

// ── 2. Age Calculator ────────────────────────────────────────
@Composable
fun AgeNothingOSContent(isDark: Boolean, isLandscape: Boolean = false) {
    val context = LocalContext.current
    val today = remember { LocalDate.now() }
    val dateFormatter = remember { DateTimeFormatter.ofPattern("dd/MM/yyyy") }

    var dobDate by remember { mutableStateOf<LocalDate?>(null) }
    var targetDate by remember { mutableStateOf(today) }

    var ageYears by remember { mutableStateOf<Int?>(null) }
    var ageMonths by remember { mutableStateOf<Int?>(null) }
    var ageDays by remember { mutableStateOf<Int?>(null) }
    var totalDays by remember { mutableStateOf<Long?>(null) }
    var totalWeeks by remember { mutableStateOf<Long?>(null) }
    var totalMonths by remember { mutableStateOf<Long?>(null) }
    var nextBirthdayDays by remember { mutableStateOf<Long?>(null) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    fun showDatePicker(initialDate: LocalDate, onDateSelected: (LocalDate) -> Unit) {
        val dpd = DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                onDateSelected(LocalDate.of(year, month + 1, dayOfMonth))
            },
            initialDate.year,
            initialDate.monthValue - 1,
            initialDate.dayOfMonth
        )
        dpd.show()
    }

    fun calculateAge() {
        val birth = dobDate
        if (birth == null) {
            errorMessage = "Please select Date of Birth"
            return
        }
        if (birth.isAfter(targetDate)) {
            errorMessage = "DOB cannot be after Target Date"
            return
        }
        errorMessage = null
        val period = Period.between(birth, targetDate)
        ageYears = period.years
        ageMonths = period.months
        ageDays = period.days

        val daysBetween = ChronoUnit.DAYS.between(birth, targetDate)
        totalDays = daysBetween
        totalWeeks = daysBetween / 7
        totalMonths = ChronoUnit.MONTHS.between(birth, targetDate)

        var nextBday = birth.withYear(targetDate.year)
        if (nextBday.isBefore(targetDate) || nextBday.isEqual(targetDate)) {
            nextBday = nextBday.plusYears(1)
        }
        nextBirthdayDays = ChronoUnit.DAYS.between(targetDate, nextBday)
    }

    val borderColor = if (isDark) Color(0xFF3A3A3D) else Color(0xFFD1D1D6)
    val boxBg = if (isDark) Color(0xFF141416) else Color.White

    if (isLandscape) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.Top
        ) {
            // Left Column (Inputs & Actions)
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Date of Birth
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(boxBg)
                        .border(1.dp, borderColor, RoundedCornerShape(8.dp))
                        .clickable {
                            showDatePicker(dobDate ?: LocalDate.of(2000, 1, 1)) { dobDate = it }
                        }
                        .padding(horizontal = 14.dp, vertical = 12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = dobDate?.let { "DOB: ${it.format(dateFormatter)}" } ?: "Select Date of Birth (DOB)",
                            style = MaterialTheme.typography.bodyMedium,
                            fontSize = 14.sp,
                            color = if (dobDate != null) MaterialTheme.colorScheme.onSurface else GrayText
                        )
                        Icon(Icons.Default.CalendarToday, contentDescription = null, tint = GrayText, modifier = Modifier.size(18.dp))
                    }
                }

                // Target Date
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(boxBg)
                        .border(1.dp, borderColor, RoundedCornerShape(8.dp))
                        .clickable {
                            showDatePicker(targetDate) { targetDate = it }
                        }
                        .padding(horizontal = 14.dp, vertical = 12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Age at: ${targetDate.format(dateFormatter)}",
                            style = MaterialTheme.typography.bodyMedium,
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Icon(Icons.Default.CalendarToday, contentDescription = null, tint = GrayText, modifier = Modifier.size(18.dp))
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Button(
                        onClick = ::calculateAge,
                        shape = RoundedCornerShape(24.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isDark) Color.White else Color.Black,
                            contentColor = if (isDark) Color.Black else Color.White
                        ),
                        modifier = Modifier.weight(1f).height(44.dp)
                    ) {
                        val isDoto = MaterialTheme.typography.bodyMedium.fontFamily == com.pixelcraftin.calcplus.ui.theme.DotoFontFamily
                        Text("CALCULATE", style = MaterialTheme.typography.bodyMedium.copy(fontWeight = if (isDoto) FontWeight.Bold else FontWeight.Medium, fontSize = 13.5.sp))
                    }

                    Button(
                        onClick = {
                            dobDate = null
                            targetDate = today
                            ageYears = null
                            errorMessage = null
                        },
                        shape = RoundedCornerShape(24.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = RedAccent,
                            contentColor = Color.White
                        ),
                        modifier = Modifier.height(44.dp)
                    ) {
                        val isDoto = MaterialTheme.typography.bodyMedium.fontFamily == com.pixelcraftin.calcplus.ui.theme.DotoFontFamily
                        Text("RESET", style = MaterialTheme.typography.bodyMedium.copy(fontWeight = if (isDoto) FontWeight.Bold else FontWeight.Medium, fontSize = 13.5.sp))
                    }
                }

                if (errorMessage != null) {
                    Text(text = errorMessage!!, color = RedAccent, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium, fontSize = 13.sp))
                }
            }

            // Right Column (Age Breakdown Summary)
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.Top
            ) {
                NothingOSSummaryCard(title = "Age Breakdown Summary", isDark = isDark) {
                    NothingOSMetricRow(
                        label = "Age",
                        value = if (ageYears != null) "$ageYears Y, $ageMonths M, $ageDays D" else "—",
                        isProminent = true
                    )
                    NothingOSMetricRow(
                        label = "Total Months",
                        value = totalMonths?.toString() ?: "—"
                    )
                    NothingOSMetricRow(
                        label = "Total Weeks",
                        value = totalWeeks?.toString() ?: "—"
                    )
                    NothingOSMetricRow(
                        label = "Total Days",
                        value = totalDays?.toString() ?: "—"
                    )
                    NothingOSMetricRow(
                        label = "Next Birthday in",
                        value = nextBirthdayDays?.let { "$it days" } ?: "—",
                        textColor = RedAccent
                    )
                }
            }
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Date of Birth
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(boxBg)
                    .border(1.dp, borderColor, RoundedCornerShape(8.dp))
                    .clickable {
                        showDatePicker(dobDate ?: LocalDate.of(2000, 1, 1)) { dobDate = it }
                    }
                    .padding(horizontal = 14.dp, vertical = 14.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = dobDate?.let { "DOB: ${it.format(dateFormatter)}" } ?: "Select Date of Birth (DOB)",
                        style = MaterialTheme.typography.bodyMedium,
                        fontSize = 14.sp,
                        color = if (dobDate != null) MaterialTheme.colorScheme.onSurface else GrayText
                    )
                    Icon(Icons.Default.CalendarToday, contentDescription = null, tint = GrayText, modifier = Modifier.size(18.dp))
                }
            }

            // Target Date
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(boxBg)
                    .border(1.dp, borderColor, RoundedCornerShape(8.dp))
                    .clickable {
                        showDatePicker(targetDate) { targetDate = it }
                    }
                    .padding(horizontal = 14.dp, vertical = 14.dp)
                ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Age at: ${targetDate.format(dateFormatter)}",
                        style = MaterialTheme.typography.bodyMedium,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Icon(Icons.Default.CalendarToday, contentDescription = null, tint = GrayText, modifier = Modifier.size(18.dp))
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Button(
                    onClick = ::calculateAge,
                    shape = RoundedCornerShape(24.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isDark) Color.White else Color.Black,
                        contentColor = if (isDark) Color.Black else Color.White
                    ),
                    modifier = Modifier.weight(1f).height(48.dp)
                ) {
                    val isDoto = MaterialTheme.typography.bodyMedium.fontFamily == com.pixelcraftin.calcplus.ui.theme.DotoFontFamily
                    Text("CALCULATE", style = MaterialTheme.typography.bodyMedium.copy(fontWeight = if (isDoto) FontWeight.Bold else FontWeight.Medium, fontSize = 14.sp))
                }

                Button(
                    onClick = {
                        dobDate = null
                        targetDate = today
                        ageYears = null
                        errorMessage = null
                    },
                    shape = RoundedCornerShape(24.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = RedAccent,
                        contentColor = Color.White
                    ),
                    modifier = Modifier.height(48.dp)
                ) {
                    val isDoto = MaterialTheme.typography.bodyMedium.fontFamily == com.pixelcraftin.calcplus.ui.theme.DotoFontFamily
                    Text("RESET", style = MaterialTheme.typography.bodyMedium.copy(fontWeight = if (isDoto) FontWeight.Bold else FontWeight.Medium, fontSize = 14.sp))
                }
            }

            if (errorMessage != null) {
                Text(text = errorMessage!!, color = RedAccent, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium, fontSize = 13.sp))
            }

            NothingOSSummaryCard(title = "Age Breakdown Summary", isDark = isDark) {
                NothingOSMetricRow(
                    label = "Age",
                    value = if (ageYears != null) "$ageYears Y, $ageMonths M, $ageDays D" else "—",
                    isProminent = true
                )
                NothingOSMetricRow(
                    label = "Total Months",
                    value = totalMonths?.toString() ?: "—"
                )
                NothingOSMetricRow(
                    label = "Total Weeks",
                    value = totalWeeks?.toString() ?: "—"
                )
                NothingOSMetricRow(
                    label = "Total Days",
                    value = totalDays?.toString() ?: "—"
                )
                NothingOSMetricRow(
                    label = "Next Birthday in",
                    value = nextBirthdayDays?.let { "$it days" } ?: "—",
                    textColor = RedAccent
                )
            }
        }
    }
}

// ── 3. EMI Calculator ─
@Composable
fun EmiNothingOSContent(currency: CurrencyModel, isDark: Boolean, isLandscape: Boolean = false) {
    var principalStr by remember { mutableStateOf("") }
    var interestRateStr by remember { mutableStateOf("") }
    var tenureStr by remember { mutableStateOf("") }

    val formatter = remember { DecimalFormat("#,##0.00", DecimalFormatSymbols(Locale.US)) }

    val emiResult = remember(principalStr, interestRateStr, tenureStr) {
        val p = principalStr.toDoubleOrNull() ?: 0.0
        val r = interestRateStr.toDoubleOrNull() ?: 0.0
        val t = tenureStr.toIntOrNull() ?: 0
        MathEngine.calculateEmi(p, r, t)
    }

    if (isLandscape) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.Top
        ) {
            // Left Column (Inputs)
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                NothingOSTextField(
                    value = principalStr,
                    onValueChange = { principalStr = it },
                    placeholder = "Loan Amount / Principal",
                    isDark = isDark
                )

                NothingOSTextField(
                    value = interestRateStr,
                    onValueChange = { interestRateStr = it },
                    placeholder = "Annual Interest Rate (%)",
                    isDark = isDark
                )

                NothingOSTextField(
                    value = tenureStr,
                    onValueChange = { tenureStr = it },
                    placeholder = "Tenure (Months)",
                    isDark = isDark
                )
            }

            // Right Column (Breakdown Summary)
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.Top
            ) {
                NothingOSSummaryCard(title = "Breakdown Summary", isDark = isDark) {
                    NothingOSMetricRow(
                        label = "Monthly EMI",
                        value = "${currency.symbol} ${formatter.format(emiResult.monthlyPayment)}",
                        isProminent = true
                    )
                    NothingOSMetricRow(
                        label = "Principal Amount",
                        value = "${currency.symbol} ${formatter.format(emiResult.principal)}"
                    )
                    NothingOSMetricRow(
                        label = "Total Interest Payable",
                        value = "${currency.symbol} ${formatter.format(emiResult.totalInterest)}"
                    )
                    NothingOSMetricRow(
                        label = "Total Payment",
                        value = "${currency.symbol} ${formatter.format(emiResult.totalPayment)}"
                    )
                }
            }
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            NothingOSTextField(
                value = principalStr,
                onValueChange = { principalStr = it },
                placeholder = "Loan Amount / Principal",
                isDark = isDark
            )

            NothingOSTextField(
                value = interestRateStr,
                onValueChange = { interestRateStr = it },
                placeholder = "Annual Interest Rate (%)",
                isDark = isDark
            )

            NothingOSTextField(
                value = tenureStr,
                onValueChange = { tenureStr = it },
                placeholder = "Tenure (Months)",
                isDark = isDark
            )

            Spacer(modifier = Modifier.height(4.dp))

            NothingOSSummaryCard(title = "Breakdown Summary", isDark = isDark) {
                NothingOSMetricRow(
                    label = "Monthly EMI",
                    value = "${currency.symbol} ${formatter.format(emiResult.monthlyPayment)}",
                    isProminent = true
                )
                NothingOSMetricRow(
                    label = "Principal Amount",
                    value = "${currency.symbol} ${formatter.format(emiResult.principal)}"
                )
                NothingOSMetricRow(
                    label = "Total Interest Payable",
                    value = "${currency.symbol} ${formatter.format(emiResult.totalInterest)}"
                )
                NothingOSMetricRow(
                    label = "Total Payment",
                    value = "${currency.symbol} ${formatter.format(emiResult.totalPayment)}"
                )
            }
        }
    }
}

// ── 4. Splitter Calculator ───────────────────────────────────
@Composable
fun SplitterNothingOSContent(currency: CurrencyModel, isDark: Boolean, isLandscape: Boolean = false) {
    var billAmountStr by remember { mutableStateOf("") }
    var tipStr by remember { mutableStateOf("") }
    var peopleStr by remember { mutableStateOf("") }

    val formatter = remember { DecimalFormat("#,##0.00", DecimalFormatSymbols(Locale.US)) }

    val splitResult = remember(billAmountStr, tipStr, peopleStr) {
        val b = billAmountStr.toDoubleOrNull() ?: 0.0
        val t = tipStr.toDoubleOrNull() ?: 0.0
        val p = peopleStr.toIntOrNull() ?: 1
        MathEngine.calculateBillSplit(b, t, p)
    }

    if (isLandscape) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.Top
        ) {
            // Left Column (Inputs)
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                NothingOSTextField(
                    value = billAmountStr,
                    onValueChange = { billAmountStr = it },
                    placeholder = "Bill Amount",
                    isDark = isDark
                )

                NothingOSTextField(
                    value = tipStr,
                    onValueChange = { tipStr = it },
                    placeholder = "Tip Percentage (%)",
                    isDark = isDark
                )

                NothingOSTextField(
                    value = peopleStr,
                    onValueChange = { peopleStr = it },
                    placeholder = "Number of People",
                    isDark = isDark,
                    keyboardType = KeyboardType.Number
                )
            }

            // Right Column (Split Bill Summary)
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.Top
            ) {
                NothingOSSummaryCard(title = "Split Bill Summary", isDark = isDark) {
                    NothingOSMetricRow(
                        label = "Amount Per Person",
                        value = "${currency.symbol} ${formatter.format(splitResult.perPersonAmount)}",
                        isProminent = true
                    )
                    NothingOSMetricRow(
                        label = "Original Bill",
                        value = "${currency.symbol} ${formatter.format(splitResult.baseAmount)}"
                    )
                    NothingOSMetricRow(
                        label = "Tip Amount",
                        value = "${currency.symbol} ${formatter.format(splitResult.tipAmount)}"
                    )
                    NothingOSMetricRow(
                        label = "Total Bill",
                        value = "${currency.symbol} ${formatter.format(splitResult.totalAmount)}"
                    )
                }
            }
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            NothingOSTextField(
                value = billAmountStr,
                onValueChange = { billAmountStr = it },
                placeholder = "Bill Amount",
                isDark = isDark
            )

            NothingOSTextField(
                value = tipStr,
                onValueChange = { tipStr = it },
                placeholder = "Tip Percentage (%)",
                isDark = isDark
            )

            NothingOSTextField(
                value = peopleStr,
                onValueChange = { peopleStr = it },
                placeholder = "Number of People",
                isDark = isDark,
                keyboardType = KeyboardType.Number
            )

            Spacer(modifier = Modifier.height(4.dp))

            NothingOSSummaryCard(title = "Split Bill Summary", isDark = isDark) {
                NothingOSMetricRow(
                    label = "Amount Per Person",
                    value = "${currency.symbol} ${formatter.format(splitResult.perPersonAmount)}",
                    isProminent = true
                )
                NothingOSMetricRow(
                    label = "Original Bill",
                    value = "${currency.symbol} ${formatter.format(splitResult.baseAmount)}"
                )
                NothingOSMetricRow(
                    label = "Tip Amount",
                    value = "${currency.symbol} ${formatter.format(splitResult.tipAmount)}"
                )
                NothingOSMetricRow(
                    label = "Total Bill",
                    value = "${currency.symbol} ${formatter.format(splitResult.totalAmount)}"
                )
            }
        }
    }
}

// ── 5. BMI Calculator ─────────────────────────────────────────
@Composable
fun BmiNothingOSContent(isDark: Boolean, isLandscape: Boolean = false) {
    var heightStr by remember { mutableStateOf("") }
    var weightStr by remember { mutableStateOf("") }

    val formatter = remember { DecimalFormat("#.##", DecimalFormatSymbols(Locale.US)) }

    val bmiResult = remember(heightStr, weightStr) {
        val h = heightStr.toDoubleOrNull() ?: 0.0
        val w = weightStr.toDoubleOrNull() ?: 0.0
        MathEngine.calculateBmi(w, h)
    }

    if (isLandscape) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.Top
        ) {
            // Left Column (Inputs)
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                NothingOSTextField(
                    value = heightStr,
                    onValueChange = { heightStr = it },
                    placeholder = "Height (cm)",
                    isDark = isDark
                )

                NothingOSTextField(
                    value = weightStr,
                    onValueChange = { weightStr = it },
                    placeholder = "Weight (kg)",
                    isDark = isDark
                )
            }

            // Right Column (Result Display Card with Gauge)
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.Top
            ) {
                NothingOSSummaryCard(title = "Body Mass Index", isDark = isDark) {
                    NothingOSMetricRow(
                        label = "BMI Score",
                        value = formatter.format(bmiResult.bmiValue),
                        isProminent = true
                    )
                    NothingOSMetricRow(
                        label = "Category",
                        value = bmiResult.category.uppercase(),
                        textColor = when (bmiResult.category) {
                            "Underweight" -> Color(0xFF64B5F6)
                            "Normal", "Normal weight" -> Color(0xFF81C784)
                            "Overweight" -> Color(0xFFFFB74D)
                            else -> RedAccent
                        }
                    )

                    Spacer(modifier = Modifier.height(6.dp))
                    NothingOSBmiGauge(bmi = bmiResult.bmiValue, isDark = isDark)
                }
            }
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            NothingOSTextField(
                value = heightStr,
                onValueChange = { heightStr = it },
                placeholder = "Height (cm)",
                isDark = isDark
            )

            NothingOSTextField(
                value = weightStr,
                onValueChange = { weightStr = it },
                placeholder = "Weight (kg)",
                isDark = isDark
            )

            Spacer(modifier = Modifier.height(4.dp))

            NothingOSSummaryCard(title = "Body Mass Index", isDark = isDark) {
                NothingOSMetricRow(
                    label = "BMI Score",
                    value = formatter.format(bmiResult.bmiValue),
                    isProminent = true
                )
                NothingOSMetricRow(
                    label = "Category",
                    value = bmiResult.category.uppercase(),
                    textColor = when (bmiResult.category) {
                        "Underweight" -> Color(0xFF64B5F6)
                        "Normal", "Normal weight" -> Color(0xFF81C784)
                        "Overweight" -> Color(0xFFFFB74D)
                        else -> RedAccent
                    }
                )

                Spacer(modifier = Modifier.height(6.dp))
                NothingOSBmiGauge(bmi = bmiResult.bmiValue, isDark = isDark)
            }
        }
    }
}

@Composable
fun NothingOSBmiGauge(bmi: Double, isDark: Boolean, modifier: Modifier = Modifier) {
    val clampedBmi = bmi.coerceIn(10.0, 40.0)
    val progress = ((clampedBmi - 10.0) / 30.0).toFloat()

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(24.dp)
        ) {
            val w = size.width
            val h = 8.dp.toPx()
            val y = 4.dp.toPx()

            val segUnder = w * (8.5f / 30f)
            val segNorm = w * (6.4f / 30f)
            val segOver = w * (5.0f / 30f)
            val segObese = w - (segUnder + segNorm + segOver)

            drawRect(Color(0xFF64B5F6), Offset(0f, y), Size(segUnder, h))
            drawRect(Color(0xFF81C784), Offset(segUnder, y), Size(segNorm, h))
            drawRect(Color(0xFFFFB74D), Offset(segUnder + segNorm, y), Size(segOver, h))
            drawRect(RedAccent, Offset(segUnder + segNorm + segOver, y), Size(segObese, h))

            val pointerX = (w * progress).coerceIn(4f, w - 4f)
            val pointerPath = Path().apply {
                moveTo(pointerX, y + h + 2.dp.toPx())
                lineTo(pointerX - 5.dp.toPx(), y + h + 10.dp.toPx())
                lineTo(pointerX + 5.dp.toPx(), y + h + 10.dp.toPx())
                close()
            }
            drawPath(pointerPath, if (isDark) Color.White else Color.Black)
        }

        Spacer(modifier = Modifier.height(4.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("10", style = MaterialTheme.typography.bodySmall, fontSize = 11.sp, color = GrayText)
            Text("18.5", style = MaterialTheme.typography.bodySmall, fontSize = 11.sp, color = GrayText)
            Text("25", style = MaterialTheme.typography.bodySmall, fontSize = 11.sp, color = GrayText)
            Text("30", style = MaterialTheme.typography.bodySmall, fontSize = 11.sp, color = GrayText)
            Text("40", style = MaterialTheme.typography.bodySmall, fontSize = 11.sp, color = GrayText)
        }
    }
}

// ── 6. Discount Calculator ───────────────────────────────────
@Composable
fun DiscountNothingOSContent(currency: CurrencyModel, isDark: Boolean, isLandscape: Boolean = false) {
    var baseValueStr by remember { mutableStateOf("") }
    var percentageStr by remember { mutableStateOf("") }
    var isDeduct by remember { mutableStateOf(true) }
    var taxes by remember { mutableStateOf(listOf("")) }

    var finalPrice by remember { mutableStateOf<String?>(null) }
    var discountOrAdditionAmount by remember { mutableStateOf<String?>(null) }
    var totalTaxAmount by remember { mutableStateOf<String?>(null) }
    var priceBeforeTax by remember { mutableStateOf<String?>(null) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

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

    if (isLandscape) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.Top
        ) {
            // Left Column (Inputs & Controls)
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                NothingOSTextField(
                    value = baseValueStr,
                    onValueChange = { baseValueStr = it },
                    placeholder = "Base Value / Amount (${currency.symbol})",
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
                            .padding(vertical = 10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        val isDoto = MaterialTheme.typography.bodyMedium.fontFamily == com.pixelcraftin.calcplus.ui.theme.DotoFontFamily
                        Text("- DEDUCT %", style = MaterialTheme.typography.bodyMedium.copy(fontWeight = if (isDoto) FontWeight.Bold else FontWeight.Medium, fontSize = 13.sp), color = deductText)
                    }

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(24.dp))
                            .background(addBg)
                            .clickable { isDeduct = false }
                            .padding(vertical = 10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        val isDoto = MaterialTheme.typography.bodyMedium.fontFamily == com.pixelcraftin.calcplus.ui.theme.DotoFontFamily
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
                        modifier = Modifier.weight(1f).height(44.dp)
                    ) {
                        val isDoto = MaterialTheme.typography.bodyMedium.fontFamily == com.pixelcraftin.calcplus.ui.theme.DotoFontFamily
                        Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("ADD TAX", style = MaterialTheme.typography.bodyMedium.copy(fontWeight = if (isDoto) FontWeight.Bold else FontWeight.Medium, fontSize = 12.sp))
                    }

                    Button(
                        onClick = ::calculate,
                        shape = RoundedCornerShape(24.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isDark) Color.White else Color.Black,
                            contentColor = if (isDark) Color.Black else Color.White
                        ),
                        modifier = Modifier.weight(1f).height(44.dp)
                    ) {
                        val isDoto = MaterialTheme.typography.bodyMedium.fontFamily == com.pixelcraftin.calcplus.ui.theme.DotoFontFamily
                        Text("CALCULATE", style = MaterialTheme.typography.bodyMedium.copy(fontWeight = if (isDoto) FontWeight.Bold else FontWeight.Medium, fontSize = 12.5.sp))
                    }
                }

                if (errorMessage != null) {
                    Text(text = errorMessage!!, color = RedAccent, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium, fontSize = 13.sp))
                }
            }

            // Right Column (Discount Summary)
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.Top
            ) {
                NothingOSSummaryCard(title = "Discount Summary", isDark = isDark) {
                    NothingOSMetricRow(
                        label = "Final Price",
                        value = "${currency.symbol} ${finalPrice ?: "0.00"}",
                        isProminent = true
                    )
                    NothingOSMetricRow(
                        label = if (isDeduct) "Discount Amount" else "Added Amount",
                        value = "${currency.symbol} ${discountOrAdditionAmount ?: "0.00"}"
                    )
                    NothingOSMetricRow(
                        label = "Price Before Tax",
                        value = "${currency.symbol} ${priceBeforeTax ?: "0.00"}"
                    )
                    NothingOSMetricRow(
                        label = "Total Tax Amount",
                        value = "${currency.symbol} ${totalTaxAmount ?: "0.00"}"
                    )
                }
            }
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            NothingOSTextField(
                value = baseValueStr,
                onValueChange = { baseValueStr = it },
                placeholder = "Base Value / Amount (${currency.symbol})",
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
                    val isDoto = MaterialTheme.typography.bodyMedium.fontFamily == com.pixelcraftin.calcplus.ui.theme.DotoFontFamily
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
                    val isDoto = MaterialTheme.typography.bodyMedium.fontFamily == com.pixelcraftin.calcplus.ui.theme.DotoFontFamily
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
                    val isDoto = MaterialTheme.typography.bodyMedium.fontFamily == com.pixelcraftin.calcplus.ui.theme.DotoFontFamily
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
                    val isDoto = MaterialTheme.typography.bodyMedium.fontFamily == com.pixelcraftin.calcplus.ui.theme.DotoFontFamily
                    Text("CALCULATE", style = MaterialTheme.typography.bodyMedium.copy(fontWeight = if (isDoto) FontWeight.Bold else FontWeight.Medium, fontSize = 13.sp))
                }
            }

            if (errorMessage != null) {
                Text(text = errorMessage!!, color = RedAccent, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium, fontSize = 13.sp))
            }

            NothingOSSummaryCard(title = "Discount Summary", isDark = isDark) {
                NothingOSMetricRow(
                    label = "Final Price",
                    value = "${currency.symbol} ${finalPrice ?: "0.00"}",
                    isProminent = true
                )
                NothingOSMetricRow(
                    label = if (isDeduct) "Discount Amount" else "Added Amount",
                    value = "${currency.symbol} ${discountOrAdditionAmount ?: "0.00"}"
                )
                NothingOSMetricRow(
                    label = "Price Before Tax",
                    value = "${currency.symbol} ${priceBeforeTax ?: "0.00"}"
                )
                NothingOSMetricRow(
                    label = "Total Tax Amount",
                    value = "${currency.symbol} ${totalTaxAmount ?: "0.00"}"
                )
            }
        }
    }
}
