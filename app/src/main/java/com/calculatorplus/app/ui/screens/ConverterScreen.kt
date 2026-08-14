package com.calculatorplus.app.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Backspace
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material3.*
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import com.calculatorplus.app.data.util.ExpressionEvaluator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.calculatorplus.app.data.Converters
import com.calculatorplus.app.data.UnitData
import com.calculatorplus.app.data.UnitItem
import com.calculatorplus.app.data.util.PreferencesManager
import com.calculatorplus.app.ui.components.KeypadButton
import com.calculatorplus.app.ui.components.KeypadGrid
import com.calculatorplus.app.ui.theme.GrayText
import com.calculatorplus.app.ui.theme.RedAccent
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

@Composable
fun ConverterScreen(
    preferencesManager: PreferencesManager,
    modifier: Modifier = Modifier
) {
    val buttonShape = preferencesManager.buttonShape
    val haptic = LocalHapticFeedback.current
    val formatter = DecimalFormat("#.########", DecimalFormatSymbols(Locale.US))
    val formulaFormatter = DecimalFormat("#.########", DecimalFormatSymbols(Locale.US))
    val clipboardManager = LocalClipboardManager.current
    val context = LocalContext.current

    var activeCategory by remember { mutableStateOf("Length") }
    var currentUnits by remember(activeCategory) {
        mutableStateOf(UnitData.unitsMap[activeCategory] ?: emptyList())
    }

    var unitFrom by remember(activeCategory) { mutableStateOf(currentUnits.getOrNull(0) ?: UnitData.unitsMap["Length"]!![0]) }
    var unitTo by remember(activeCategory) { mutableStateOf(currentUnits.getOrNull(1) ?: currentUnits.getOrNull(0) ?: UnitData.unitsMap["Length"]!![1]) }

    var inputValue by remember { mutableStateOf("1") }
    var lastValidInputDouble by remember { mutableStateOf(1.0) }

    val conversionFormula = remember(unitFrom, unitTo) {
        val oneConverted = Converters.convert(1.0, unitFrom, unitTo)
        "1 ${unitFrom.symbol} = ${formulaFormatter.format(oneConverted)} ${unitTo.symbol}"
    }

    val convertedValue = remember(inputValue, unitFrom, unitTo) {
        val inputDouble = try {
            val evaluator = ExpressionEvaluator(isDegreeMode = true)
            val res = if (inputValue.isEmpty() || inputValue == "-") 0.0 else evaluator.evaluate(inputValue)
            if (!res.isNaN() && !res.isInfinite()) {
                lastValidInputDouble = res
                res
            } else {
                lastValidInputDouble
            }
        } catch (e: Exception) {
            lastValidInputDouble
        }
        val outputDouble = Converters.convert(inputDouble, unitFrom, unitTo)
        formatter.format(outputDouble)
    }

    // 0 = none, 1 = from-picker, 2 = to-picker
    var pickerTarget by remember { mutableStateOf(0) }

    fun handleKeyPress(key: String) {
        when (key) {
            "AC" -> inputValue = "0"
            "C", "⌫" -> {
                inputValue = if (inputValue.length <= 1) "0" else inputValue.dropLast(1)
            }
            "." -> {
                if (inputValue.isEmpty()) {
                    inputValue = "0."
                } else {
                    val lastChar = inputValue.last()
                    if (lastChar.isDigit()) {
                        val lastNumber = inputValue.split('+', '-', '×', '÷').lastOrNull() ?: ""
                        if (!lastNumber.contains(".")) {
                            inputValue += "."
                        }
                    } else if (lastChar == '+' || lastChar == '-' || lastChar == '×' || lastChar == '÷') {
                        inputValue += "0."
                    }
                }
            }
            "00" -> {
                if (inputValue != "0" && inputValue.isNotEmpty()) {
                    val lastChar = inputValue.last()
                    if (lastChar.isDigit() || lastChar == '.') {
                        inputValue += "00"
                    }
                }
            }
            "⇅" -> {
                val temp = unitFrom
                unitFrom = unitTo
                unitTo = temp
                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
            }
            "±" -> {
                inputValue = if (inputValue.startsWith("-")) {
                    inputValue.substring(1)
                } else {
                    if (inputValue == "0") "-" else "-$inputValue"
                }
            }
            "+", "-", "×", "÷" -> {
                if (inputValue.isNotEmpty()) {
                    val lastChar = inputValue.last()
                    if (lastChar == '+' || lastChar == '-' || lastChar == '×' || lastChar == '÷') {
                        inputValue = inputValue.dropLast(1) + key
                    } else {
                        inputValue += key
                    }
                }
            }
            else -> {
                if (inputValue == "0") {
                    inputValue = key
                } else {
                    inputValue += key
                }
            }
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 8.dp)
        ) {
            // ── Category Chips ────────────────────────────────────────────
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                UnitData.categories.forEach { category ->
                    val isSelected = category == activeCategory
                    val chipBgColor = if (isSelected) MaterialTheme.colorScheme.onBackground else MaterialTheme.colorScheme.secondary
                    val chipContentColor = if (isSelected) MaterialTheme.colorScheme.background else MaterialTheme.colorScheme.onBackground

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(chipBgColor)
                            .clickable {
                                activeCategory = category
                                inputValue = "1"
                                pickerTarget = 0
                            }
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = category,
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                            color = chipContentColor
                        )
                    }
                }
            }

            // ── Mid Section: From Card / Swap Row / To Card ───────────────
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.38f)
                    .padding(horizontal = 12.dp)
            ) {
                // FROM card — unit pill (bottom-left) + expression (right)
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondary),
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp, vertical = 12.dp)
                    ) {
                        // Unit pill — bottom-left
                        UnitPill(
                            unit = unitFrom,
                            modifier = Modifier
                                .align(Alignment.BottomStart)
                                .clickable { pickerTarget = if (pickerTarget == 1) 0 else 1 }
                        )
                        // Input expression — top-right
                        Text(
                            text = inputValue,
                            style = MaterialTheme.typography.displayLarge.copy(fontSize = 36.sp),
                            color = MaterialTheme.colorScheme.onBackground,
                            textAlign = TextAlign.End,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .fillMaxWidth()
                        )
                    }
                }

                // Swap row
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Circular swap button
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.secondary)
                            .clickable {
                                val temp = unitFrom
                                unitFrom = unitTo
                                unitTo = temp
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "⇅",
                            fontSize = 18.sp,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    // Conversion formula
                    Text(
                        text = conversionFormula,
                        style = MaterialTheme.typography.bodyMedium,
                        color = GrayText,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.End
                    )
                }

                // TO card — unit pill (bottom-left) + converted value (right large text)
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondary),
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp, vertical = 12.dp)
                    ) {
                        // Unit pill — bottom-left
                        UnitPill(
                            unit = unitTo,
                            modifier = Modifier
                                .align(Alignment.BottomStart)
                                .clickable { pickerTarget = if (pickerTarget == 2) 0 else 2 }
                        )
                        // Converted value — right
                        Text(
                            text = convertedValue,
                            style = MaterialTheme.typography.displayLarge.copy(fontSize = 36.sp),
                            color = MaterialTheme.colorScheme.onBackground,
                            textAlign = TextAlign.End,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .fillMaxWidth()
                        )
                    }
                }
            }

            // ── Numeric Keypad ────────────────────────────────────────────
            KeypadGrid(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.62f)
                    .navigationBarsPadding()
                    .padding(start = 12.dp, end = 12.dp, bottom = 2.dp, top = 4.dp),
                spacing = 6
            ) {
                // Row 1: AC, ±, ÷, ⌫
                Row(modifier = Modifier.fillMaxWidth().weight(1f)) {
                    KeypadButton(
                        text = "AC",
                        onClick = { handleKeyPress("AC") },
                        shapeName = buttonShape,
                        isAccent = true,
                        modifier = Modifier.weight(1f).fillMaxHeight()
                    )
                    KeypadButton(
                        text = "±",
                        onClick = { handleKeyPress("±") },
                        shapeName = buttonShape,
                        modifier = Modifier.weight(1f).fillMaxHeight()
                    )
                    KeypadButton(
                        text = "÷",
                        onClick = { handleKeyPress("÷") },
                        shapeName = buttonShape,
                        modifier = Modifier.weight(1f).fillMaxHeight()
                    )
                    KeypadButton(
                        text = "⌫",
                        onClick = { handleKeyPress("⌫") },
                        shapeName = buttonShape,
                        icon = Icons.AutoMirrored.Filled.Backspace,
                        modifier = Modifier.weight(1f).fillMaxHeight()
                    )
                }
                // Row 2: 7, 8, 9, ×
                Row(modifier = Modifier.fillMaxWidth().weight(1f)) {
                    KeypadButton(text = "7", onClick = { handleKeyPress("7") }, shapeName = buttonShape, modifier = Modifier.weight(1f).fillMaxHeight())
                    KeypadButton(text = "8", onClick = { handleKeyPress("8") }, shapeName = buttonShape, modifier = Modifier.weight(1f).fillMaxHeight())
                    KeypadButton(text = "9", onClick = { handleKeyPress("9") }, shapeName = buttonShape, modifier = Modifier.weight(1f).fillMaxHeight())
                    KeypadButton(
                        text = "×",
                        onClick = { handleKeyPress("×") },
                        shapeName = buttonShape,
                        modifier = Modifier.weight(1f).fillMaxHeight()
                    )
                }
                // Row 3: 4, 5, 6, -
                Row(modifier = Modifier.fillMaxWidth().weight(1f)) {
                    KeypadButton(text = "4", onClick = { handleKeyPress("4") }, shapeName = buttonShape, modifier = Modifier.weight(1f).fillMaxHeight())
                    KeypadButton(text = "5", onClick = { handleKeyPress("5") }, shapeName = buttonShape, modifier = Modifier.weight(1f).fillMaxHeight())
                    KeypadButton(text = "6", onClick = { handleKeyPress("6") }, shapeName = buttonShape, modifier = Modifier.weight(1f).fillMaxHeight())
                    KeypadButton(
                        text = "-",
                        onClick = { handleKeyPress("-") },
                        shapeName = buttonShape,
                        modifier = Modifier.weight(1f).fillMaxHeight()
                    )
                }
                // Row 4: 1, 2, 3, +
                Row(modifier = Modifier.fillMaxWidth().weight(1f)) {
                    KeypadButton(text = "1", onClick = { handleKeyPress("1") }, shapeName = buttonShape, modifier = Modifier.weight(1f).fillMaxHeight())
                    KeypadButton(text = "2", onClick = { handleKeyPress("2") }, shapeName = buttonShape, modifier = Modifier.weight(1f).fillMaxHeight())
                    KeypadButton(text = "3", onClick = { handleKeyPress("3") }, shapeName = buttonShape, modifier = Modifier.weight(1f).fillMaxHeight())
                    KeypadButton(
                        text = "+",
                        onClick = { handleKeyPress("+") },
                        shapeName = buttonShape,
                        modifier = Modifier.weight(1f).fillMaxHeight()
                    )
                }
                // Row 5: 0, 00, ., copy
                Row(modifier = Modifier.fillMaxWidth().weight(1f)) {
                    KeypadButton(text = "0", onClick = { handleKeyPress("0") }, shapeName = buttonShape, modifier = Modifier.weight(1f).fillMaxHeight())
                    KeypadButton(text = "00", onClick = { handleKeyPress("00") }, shapeName = buttonShape, modifier = Modifier.weight(1f).fillMaxHeight())
                    KeypadButton(text = ".", onClick = { handleKeyPress(".") }, shapeName = buttonShape, modifier = Modifier.weight(1f).fillMaxHeight())
                    KeypadButton(
                        text = "copy",
                        onClick = {
                            clipboardManager.setText(androidx.compose.ui.text.AnnotatedString(convertedValue))
                            android.widget.Toast.makeText(context, "Copied converted value: $convertedValue", android.widget.Toast.LENGTH_SHORT).show()
                        },
                        shapeName = buttonShape,
                        backgroundColor = Color.White,
                        contentColor = Color.Black,
                        icon = Icons.Default.ContentCopy,
                        modifier = Modifier.weight(1f).fillMaxHeight()
                    )
                }
            }
        }

        // ── Scrim + Animated Unit Picker Overlay ──────────────────────────
        val showPicker = pickerTarget != 0
        AnimatedVisibility(
            visible = showPicker,
            enter = fadeIn(tween(180)),
            exit = fadeOut(tween(180)),
            modifier = Modifier.fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.5f))
                    .clickable { pickerTarget = 0 }
            )
        }
        AnimatedVisibility(
            visible = showPicker,
            enter = fadeIn(tween(180)) + slideInVertically(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioNoBouncy,
                    stiffness = Spring.StiffnessMediumLow
                ),
                initialOffsetY = { it / 2 }
            ),
            exit = fadeOut(tween(150)) + slideOutVertically(
                animationSpec = tween(150),
                targetOffsetY = { it / 2 }
            ),
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            // Capture values at composition time to avoid stale reads inside animation
            val capturedTarget = pickerTarget
            val selectedUnit = if (capturedTarget == 1) unitFrom else unitTo
            UnitPickerSheet(
                categoryName = activeCategory,
                units = currentUnits,
                selectedUnit = selectedUnit,
                onSelect = { chosen ->
                    if (capturedTarget == 1) unitFrom = chosen else unitTo = chosen
                    pickerTarget = 0
                },
                onDismiss = { pickerTarget = 0 }
            )
        }
    }
}


// ── Unit pill badge ───────────────────────────────────────────────────────────
@Composable
private fun UnitPill(unit: UnitItem, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.background.copy(alpha = 0.7f))
            .padding(horizontal = 10.dp, vertical = 5.dp)
    ) {
        Text(
            text = unit.symbol,
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}

// ── Unit Picker Bottom Sheet ──────────────────────────────────────────────────
@Composable
fun UnitPickerSheet(
    categoryName: String,
    units: List<UnitItem>,
    selectedUnit: UnitItem,
    onSelect: (UnitItem) -> Unit,
    onDismiss: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1A1A)),
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.55f)
            .clickable(enabled = false) {} // Consume click so scrim doesn't get it
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Drag handle
            Box(
                modifier = Modifier
                    .width(40.dp)
                    .height(4.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(GrayText.copy(alpha = 0.4f))
                    .align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Header: "AREA   13 units"
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = categoryName.uppercase(),
                    style = MaterialTheme.typography.labelLarge.copy(
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.5.sp
                    ),
                    color = GrayText
                )
                Text(
                    text = "${units.size} units",
                    style = MaterialTheme.typography.labelMedium,
                    color = GrayText.copy(alpha = 0.6f)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // 2-column grid of unit cards
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(units) { unit ->
                    val isSelected = unit.symbol == selectedUnit.symbol
                    val cardBorder = if (isSelected) BorderStroke(1.5.dp, RedAccent) else BorderStroke(0.dp, Color.Transparent)

                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isSelected) Color(0xFF2A1A1A) else Color(0xFF2C2C2C)
                        ),
                        border = cardBorder,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onSelect(unit) }
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 12.dp, vertical = 14.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = unit.name,
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                    ),
                                    color = if (isSelected) Color.White else Color(0xFFCCCCCC),
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Text(
                                    text = unit.symbol,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = if (isSelected) RedAccent else GrayText
                                )
                            }
                            if (isSelected) {
                                Box(
                                    modifier = Modifier
                                        .size(24.dp)
                                        .clip(CircleShape)
                                        .background(RedAccent),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = "Selected",
                                        tint = Color.White,
                                        modifier = Modifier.size(14.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
