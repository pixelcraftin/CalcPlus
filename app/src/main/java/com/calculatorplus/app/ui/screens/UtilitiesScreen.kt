package com.calculatorplus.app.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.calculatorplus.app.data.MathEngine
import com.calculatorplus.app.data.model.CurrencyData
import com.calculatorplus.app.data.model.CurrencyModel
import com.calculatorplus.app.ui.theme.GrayText
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

@Composable
fun UtilitiesScreen(
    modifier: Modifier = Modifier
) {
    var activeSubTab by remember { mutableStateOf("EMI") }
    val tabs = listOf("EMI", "Splitter", "BMI")
    
    // Shared Currency State across EMI and Splitter
    var selectedCurrency by remember { mutableStateOf(CurrencyData.currencies[0]) } // Defaults to USD ($)
    var showCurrencyDialog by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Tab Selector Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            tabs.forEach { tab ->
                val isSelected = tab == activeSubTab
                val tabBg = if (isSelected) MaterialTheme.colorScheme.onBackground else MaterialTheme.colorScheme.secondary
                val tabText = if (isSelected) MaterialTheme.colorScheme.background else MaterialTheme.colorScheme.onBackground
                
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(12.dp))
                        .background(tabBg)
                        .clickable { activeSubTab = tab }
                        .padding(vertical = 10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = tab.uppercase(),
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                        color = tabText
                    )
                }
            }
        }

        // Shared Currency Picker Bar at the top of controls (for EMI and Splitter tabs)
        if (activeSubTab == "EMI" || activeSubTab == "Splitter") {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(MaterialTheme.colorScheme.secondary.copy(alpha = 0.6f))
                    .clickable { showCurrencyDialog = true }
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "CURRENCY SYMBOL",
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                    color = GrayText
                )
                Text(
                    text = "${selectedCurrency.name} (${selectedCurrency.symbol})",
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }

        // Sub Screen content Box
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            when (activeSubTab) {
                "EMI" -> EmiCalculatorSubScreen(currency = selectedCurrency)
                "Splitter" -> BillSplitterSubScreen(currency = selectedCurrency)
                "BMI" -> BmiCalculatorSubScreen()
            }
        }
    }

    // Global Currency Dialog Picker
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

@Composable
fun CurrencyPickerDialog(
    selectedCurrency: CurrencyModel,
    onDismiss: () -> Unit,
    onSelect: (CurrencyModel) -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background),
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.5f)
                .border(0.5.dp, GrayText.copy(alpha = 0.3f), RoundedCornerShape(16.dp))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Text(
                    text = "CHOOSE CURRENCY",
                    style = MaterialTheme.typography.titleLarge.copy(fontSize = 16.sp, letterSpacing = 1.sp, fontWeight = FontWeight.Bold),
                    modifier = Modifier.padding(bottom = 12.dp)
                )
                
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .verticalScroll(rememberScrollState())
                ) {
                    CurrencyData.currencies.forEach { currency ->
                        val isSelected = currency.code == selectedCurrency.code
                        val bgColor = if (isSelected) MaterialTheme.colorScheme.secondary else Color.Transparent
                        
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(bgColor)
                                .clickable { onSelect(currency) }
                                .padding(horizontal = 12.dp, vertical = 14.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = currency.name,
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                )
                            )
                            Text(
                                text = "${currency.code} (${currency.symbol})",
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                                color = if (isSelected) MaterialTheme.colorScheme.primary else GrayText
                            )
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = onDismiss,
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.onBackground),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "CANCEL",
                        color = MaterialTheme.colorScheme.background,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun EmiCalculatorSubScreen(currency: CurrencyModel) {
    var principalStr by remember { mutableStateOf("") }
    var interestRateStr by remember { mutableStateOf("") }
    var tenureStr by remember { mutableStateOf("") }

    val formatter = DecimalFormat("#,##0.00", DecimalFormatSymbols(Locale.US))

    val emiResult = remember(principalStr, interestRateStr, tenureStr) {
        val p = principalStr.toDoubleOrNull() ?: 0.0
        val r = interestRateStr.toDoubleOrNull() ?: 0.0
        val t = tenureStr.toIntOrNull() ?: 0
        MathEngine.calculateEmi(p, r, t)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        OutlinedTextField(
            value = principalStr,
            onValueChange = { principalStr = it },
            label = { Text("Loan Amount / Principal") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.onBackground,
                focusedLabelColor = MaterialTheme.colorScheme.onBackground
            ),
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = interestRateStr,
            onValueChange = { interestRateStr = it },
            label = { Text("Annual Interest Rate (%)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.onBackground,
                focusedLabelColor = MaterialTheme.colorScheme.onBackground
            ),
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = tenureStr,
            onValueChange = { tenureStr = it },
            label = { Text("Tenure (Months)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.onBackground,
                focusedLabelColor = MaterialTheme.colorScheme.onBackground
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "BREAKDOWN SUMMARY",
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold, letterSpacing = 1.sp),
                    color = GrayText
                )
                
                DividerItem("Monthly EMI", "${currency.symbol} ${formatter.format(emiResult.monthlyPayment)}", isTotal = true)
                DividerItem("Principal Amount", "${currency.symbol} ${formatter.format(emiResult.principal)}")
                DividerItem("Total Interest Payable", "${currency.symbol} ${formatter.format(emiResult.totalInterest)}")
                DividerItem("Total Payment", "${currency.symbol} ${formatter.format(emiResult.totalPayment)}")
            }
        }
    }
}

@Composable
fun BillSplitterSubScreen(currency: CurrencyModel) {
    var billStr by remember { mutableStateOf("") }
    var tipStr by remember { mutableStateOf("10") }
    var peopleStr by remember { mutableStateOf("2") }

    val formatter = DecimalFormat("#,##0.00", DecimalFormatSymbols(Locale.US))

    val splitResult = remember(billStr, tipStr, peopleStr) {
        val b = billStr.toDoubleOrNull() ?: 0.0
        val t = tipStr.toDoubleOrNull() ?: 0.0
        val p = peopleStr.toIntOrNull() ?: 1
        MathEngine.calculateBillSplit(b, t, p)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        OutlinedTextField(
            value = billStr,
            onValueChange = { billStr = it },
            label = { Text("Bill Amount") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.onBackground,
                focusedLabelColor = MaterialTheme.colorScheme.onBackground
            ),
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = tipStr,
            onValueChange = { tipStr = it },
            label = { Text("Tip Percentage (%)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.onBackground,
                focusedLabelColor = MaterialTheme.colorScheme.onBackground
            ),
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = peopleStr,
            onValueChange = { peopleStr = it },
            label = { Text("Number of People") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.onBackground,
                focusedLabelColor = MaterialTheme.colorScheme.onBackground
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "SPLIT BILL SUMMARY",
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold, letterSpacing = 1.sp),
                    color = GrayText
                )
                
                DividerItem("Amount Per Person", "${currency.symbol} ${formatter.format(splitResult.perPersonAmount)}", isTotal = true)
                DividerItem("Original Bill", "${currency.symbol} ${formatter.format(splitResult.baseAmount)}")
                DividerItem("Tip Amount", "${currency.symbol} ${formatter.format(splitResult.tipAmount)}")
                DividerItem("Total Bill", "${currency.symbol} ${formatter.format(splitResult.totalAmount)}")
            }
        }
    }
}

@Composable
fun BmiCalculatorSubScreen() {
    var heightStr by remember { mutableStateOf("") }
    var weightStr by remember { mutableStateOf("") }

    val formatter = DecimalFormat("#.##", DecimalFormatSymbols(Locale.US))

    val bmiResult = remember(heightStr, weightStr) {
        val h = heightStr.toDoubleOrNull() ?: 0.0
        val w = weightStr.toDoubleOrNull() ?: 0.0
        MathEngine.calculateBmi(w, h)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        OutlinedTextField(
            value = heightStr,
            onValueChange = { heightStr = it },
            label = { Text("Height (cm)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.onBackground,
                focusedLabelColor = MaterialTheme.colorScheme.onBackground
            ),
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = weightStr,
            onValueChange = { weightStr = it },
            label = { Text("Weight (kg)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.onBackground,
                focusedLabelColor = MaterialTheme.colorScheme.onBackground
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "BMI RESULT",
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold, letterSpacing = 1.sp),
                    color = GrayText
                )
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Your BMI",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        text = if (bmiResult.bmiValue > 0.0) formatter.format(bmiResult.bmiValue) else "0.0",
                        style = MaterialTheme.typography.displayMedium.copy(fontSize = 32.sp, fontWeight = FontWeight.Bold)
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Classification",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        text = if (bmiResult.bmiValue > 0.0) bmiResult.category.uppercase() else "NORMAL",
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                        color = if (bmiResult.bmiValue > 0.0) Color(bmiResult.colorCode) else Color(0xFF4CAF50)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                BmiIndicatorBar(bmi = if (bmiResult.bmiValue > 0.0) bmiResult.bmiValue else 21.7)
            }
        }
    }
}

@Composable
fun BmiIndicatorBar(bmi: Double) {
    val clampedBmi = bmi.coerceIn(10.0, 40.0)
    val percentage = ((clampedBmi - 10.0) / 30.0).toFloat()
    val markerColor = MaterialTheme.colorScheme.onBackground

    Column(modifier = Modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp))
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val w = size.width
                val h = size.height
                val uLimit = w * ((18.5f - 10f) / 30f)
                val nLimit = w * ((25.0f - 10f) / 30f)
                val oLimit = w * ((30.0f - 10f) / 30f)

                drawRect(Color(0xFFFFB300), topLeft = Offset(0f, 0f), size = androidx.compose.ui.geometry.Size(uLimit, h))
                drawRect(Color(0xFF4CAF50), topLeft = Offset(uLimit, 0f), size = androidx.compose.ui.geometry.Size(nLimit - uLimit, h))
                drawRect(Color(0xFFFF9800), topLeft = Offset(nLimit, 0f), size = androidx.compose.ui.geometry.Size(oLimit - nLimit, h))
                drawRect(Color(0xFFF44336), topLeft = Offset(oLimit, 0f), size = androidx.compose.ui.geometry.Size(w - oLimit, h))
            }
        }

        Spacer(modifier = Modifier.height(4.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(16.dp)
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val w = size.width
                val x = w * percentage
                val path = androidx.compose.ui.graphics.Path().apply {
                    moveTo(x, 0f)
                    lineTo(x - 10f, 15f)
                    lineTo(x + 10f, 15f)
                    close()
                }
                drawPath(path, color = markerColor)
            }
        }
    }
}

@Composable
fun DividerItem(
    label: String,
    value: String,
    isTotal: Boolean = false
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = if (isTotal) MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold) else MaterialTheme.typography.bodyMedium,
            color = if (isTotal) MaterialTheme.colorScheme.onBackground else GrayText
        )
        Text(
            text = value,
            style = if (isTotal) MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold, fontSize = 22.sp) else MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}
