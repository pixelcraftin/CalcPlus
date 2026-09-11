package com.pixelcraftin.calcplus.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.pixelcraftin.calcplus.data.MathEngine
import com.pixelcraftin.calcplus.ui.theme.DotoFontFamily
import com.pixelcraftin.calcplus.ui.theme.GrayText
import com.pixelcraftin.calcplus.ui.theme.RedAccent
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

@Composable
fun BmiCalculatorScreen(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val isDoto = MaterialTheme.typography.titleLarge.fontFamily == DotoFontFamily
    var heightStr by remember { mutableStateOf("") }
    var weightStr by remember { mutableStateOf("") }

    val formatter = remember { DecimalFormat("#.##", DecimalFormatSymbols(Locale.US)) }
    val isDark = MaterialTheme.colorScheme.background.red < 0.5f

    val bmiResult = remember(heightStr, weightStr) {
        val h = heightStr.toDoubleOrNull() ?: 0.0
        val w = weightStr.toDoubleOrNull() ?: 0.0
        MathEngine.calculateBmi(w, h)
    }

    val configuration = androidx.compose.ui.platform.LocalConfiguration.current
    val isLandscape = configuration.orientation == android.content.res.Configuration.ORIENTATION_LANDSCAPE || configuration.screenWidthDp > 600

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
                text = "BMI CALCULATOR",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = if (isDoto) FontWeight.Bold else FontWeight.Medium,
                    fontSize = 17.sp,
                    letterSpacing = 1.sp
                ),
                color = MaterialTheme.colorScheme.onBackground
            )
        }

        HorizontalDivider(color = GrayText.copy(alpha = 0.2f), thickness = 0.5.dp)

        if (isLandscape) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
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

                // Right Column (Result Summary Card)
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
                    .padding(16.dp)
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
}
