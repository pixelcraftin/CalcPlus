package com.pixelcraftin.calcplus.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.pixelcraftin.calcplus.data.MathEngine
import com.pixelcraftin.calcplus.data.model.CurrencyData
import com.pixelcraftin.calcplus.data.model.CurrencyModel
import com.pixelcraftin.calcplus.ui.components.CurrencyPickerDialog
import com.pixelcraftin.calcplus.ui.theme.GrayText
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

@Composable
fun SplitterCalculatorScreen(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    var selectedCurrency by remember { mutableStateOf(CurrencyData.currencies[0]) }
    var showCurrencyDialog by remember { mutableStateOf(false) }

    var billAmountStr by remember { mutableStateOf("") }
    var tipStr by remember { mutableStateOf("") }
    var peopleStr by remember { mutableStateOf("") }

    val formatter = remember { DecimalFormat("#,##0.00", DecimalFormatSymbols(Locale.US)) }
    val isDark = MaterialTheme.colorScheme.background.red < 0.5f

    val splitResult = remember(billAmountStr, tipStr, peopleStr) {
        val b = billAmountStr.toDoubleOrNull() ?: 0.0
        val t = tipStr.toDoubleOrNull() ?: 0.0
        val p = peopleStr.toIntOrNull() ?: 1
        MathEngine.calculateBillSplit(b, t, p)
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
                text = "BILL SPLITTER",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
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
            // Currency Selector Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
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
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "CURRENCY SYMBOL",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        letterSpacing = 1.sp
                    ),
                    color = GrayText
                )
                Text(
                    text = "${selectedCurrency.name} (${selectedCurrency.symbol})",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.5.sp
                    ),
                    color = if (isDark) Color.White else Color(0xFF111111)
                )
            }

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
                    value = "${selectedCurrency.symbol} ${formatter.format(splitResult.perPersonAmount)}",
                    isProminent = true
                )
                NothingOSMetricRow(
                    label = "Original Bill",
                    value = "${selectedCurrency.symbol} ${formatter.format(splitResult.baseAmount)}"
                )
                NothingOSMetricRow(
                    label = "Tip Amount",
                    value = "${selectedCurrency.symbol} ${formatter.format(splitResult.tipAmount)}"
                )
                NothingOSMetricRow(
                    label = "Total Bill",
                    value = "${selectedCurrency.symbol} ${formatter.format(splitResult.totalAmount)}"
                )
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
