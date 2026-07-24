package com.calculatorplus.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
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
import androidx.compose.ui.window.Dialog
import com.calculatorplus.app.data.Converters
import com.calculatorplus.app.data.UnitData
import com.calculatorplus.app.data.UnitItem
import com.calculatorplus.app.data.util.PreferencesManager
import com.calculatorplus.app.ui.components.KeypadButton
import com.calculatorplus.app.ui.components.KeypadGrid
import com.calculatorplus.app.ui.theme.GrayText
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

    var activeCategory by remember { mutableStateOf("Length") }
    var currentUnits by remember(activeCategory) {
        mutableStateOf(UnitData.unitsMap[activeCategory] ?: emptyList())
    }

    var unitFrom by remember(activeCategory) { mutableStateOf(currentUnits.getOrNull(0) ?: UnitData.unitsMap["Length"]!![0]) }
    var unitTo by remember(activeCategory) { mutableStateOf(currentUnits.getOrNull(1) ?: currentUnits.getOrNull(0) ?: UnitData.unitsMap["Length"]!![1]) }

    var inputValue by remember { mutableStateOf("1") }
    val convertedValue = remember(inputValue, unitFrom, unitTo) {
        val inputDouble = inputValue.toDoubleOrNull() ?: 0.0
        val outputDouble = Converters.convert(inputDouble, unitFrom, unitTo)
        formatter.format(outputDouble)
    }

    var showPickerFrom by remember { mutableStateOf(false) }
    var showPickerTo by remember { mutableStateOf(false) }

    fun handleKeyPress(key: String) {
        when (key) {
            "AC" -> inputValue = "0"
            "C" -> {
                inputValue = if (inputValue.length <= 1) "0" else inputValue.dropLast(1)
            }
            "." -> {
                if (!inputValue.contains(".")) {
                    inputValue += "."
                }
            }
            "00" -> {
                if (inputValue != "0") {
                    inputValue += "00"
                }
            }
            "⇅" -> {
                val temp = unitFrom
                unitFrom = unitTo
                unitTo = temp
                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
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

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(top = 8.dp)
    ) {
        // Horizontal Scroll Category Selector
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

        // Dual Converter Cards
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.35f)
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Card 1: Input Value & Padded Full-Width Unit Selector
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondary),
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(MaterialTheme.colorScheme.background)
                            .clickable { showPickerFrom = true }
                            .padding(horizontal = 14.dp, vertical = 10.dp)
                    ) {
                        Text(
                            text = "FROM UNIT",
                            style = MaterialTheme.typography.labelMedium.copy(fontSize = 10.sp, fontWeight = FontWeight.Bold),
                            color = GrayText
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = unitFrom.name,
                                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.weight(1f)
                            )
                            Text(
                                text = unitFrom.symbol,
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary),
                                modifier = Modifier.padding(start = 8.dp)
                            )
                        }
                    }
                    Text(
                        text = inputValue,
                        style = MaterialTheme.typography.displayLarge.copy(fontSize = 32.sp),
                        color = MaterialTheme.colorScheme.onBackground,
                        textAlign = TextAlign.End,
                        modifier = Modifier.fillMaxWidth(),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            // Card 2: Output Value & Padded Full-Width Unit Selector
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondary),
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(MaterialTheme.colorScheme.background)
                            .clickable { showPickerTo = true }
                            .padding(horizontal = 14.dp, vertical = 10.dp)
                    ) {
                        Text(
                            text = "TO UNIT",
                            style = MaterialTheme.typography.labelMedium.copy(fontSize = 10.sp, fontWeight = FontWeight.Bold),
                            color = GrayText
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = unitTo.name,
                                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.weight(1f)
                            )
                            Text(
                                text = unitTo.symbol,
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary),
                                modifier = Modifier.padding(start = 8.dp)
                            )
                        }
                    }
                    Text(
                        text = convertedValue,
                        style = MaterialTheme.typography.displayLarge.copy(fontSize = 32.sp),
                        color = MaterialTheme.colorScheme.onBackground,
                        textAlign = TextAlign.End,
                        modifier = Modifier.fillMaxWidth(),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }

        // Soft Numeric Keypad
        KeypadGrid(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.65f)
                .navigationBarsPadding()
                .padding(start = 12.dp, end = 12.dp, bottom = 2.dp),
            spacing = 6
        ) {
            Row(modifier = Modifier.fillMaxWidth().weight(1f)) {
                KeypadButton(text = "7", onClick = { handleKeyPress("7") }, shapeName = buttonShape, modifier = Modifier.weight(1f).fillMaxHeight())
                KeypadButton(text = "8", onClick = { handleKeyPress("8") }, shapeName = buttonShape, modifier = Modifier.weight(1f).fillMaxHeight())
                KeypadButton(text = "9", onClick = { handleKeyPress("9") }, shapeName = buttonShape, modifier = Modifier.weight(1f).fillMaxHeight())
                KeypadButton(text = "C", onClick = { handleKeyPress("C") }, shapeName = buttonShape, modifier = Modifier.weight(1f).fillMaxHeight())
            }
            Row(modifier = Modifier.fillMaxWidth().weight(1f)) {
                KeypadButton(text = "4", onClick = { handleKeyPress("4") }, shapeName = buttonShape, modifier = Modifier.weight(1f).fillMaxHeight())
                KeypadButton(text = "5", onClick = { handleKeyPress("5") }, shapeName = buttonShape, modifier = Modifier.weight(1f).fillMaxHeight())
                KeypadButton(text = "6", onClick = { handleKeyPress("6") }, shapeName = buttonShape, modifier = Modifier.weight(1f).fillMaxHeight())
                KeypadButton(text = "AC", onClick = { handleKeyPress("AC") }, shapeName = buttonShape, isAccent = true, modifier = Modifier.weight(1f).fillMaxHeight())
            }
            Row(modifier = Modifier.fillMaxWidth().weight(1f)) {
                KeypadButton(text = "1", onClick = { handleKeyPress("1") }, shapeName = buttonShape, modifier = Modifier.weight(1f).fillMaxHeight())
                KeypadButton(text = "2", onClick = { handleKeyPress("2") }, shapeName = buttonShape, modifier = Modifier.weight(1f).fillMaxHeight())
                KeypadButton(text = "3", onClick = { handleKeyPress("3") }, shapeName = buttonShape, modifier = Modifier.weight(1f).fillMaxHeight())
                KeypadButton(text = "⇅", onClick = { handleKeyPress("⇅") }, shapeName = buttonShape, modifier = Modifier.weight(1f).fillMaxHeight())
            }
            Row(modifier = Modifier.fillMaxWidth().weight(1f)) {
                KeypadButton(text = "0", onClick = { handleKeyPress("0") }, shapeName = buttonShape, modifier = Modifier.weight(2f).fillMaxHeight(), aspectRatio = null)
                KeypadButton(text = "00", onClick = { handleKeyPress("00") }, shapeName = buttonShape, modifier = Modifier.weight(1f).fillMaxHeight())
                KeypadButton(text = ".", onClick = { handleKeyPress(".") }, shapeName = buttonShape, modifier = Modifier.weight(1f).fillMaxHeight())
            }
        }
    }

    // Picker from
    if (showPickerFrom) {
        UnitPickerDialog(
            title = "Convert From",
            units = currentUnits,
            selectedUnit = unitFrom,
            onDismiss = { showPickerFrom = false },
            onSelect = {
                unitFrom = it
                showPickerFrom = false
            }
        )
    }

    // Picker to
    if (showPickerTo) {
        UnitPickerDialog(
            title = "Convert To",
            units = currentUnits,
            selectedUnit = unitTo,
            onDismiss = { showPickerTo = false },
            onSelect = {
                unitTo = it
                showPickerTo = false
            }
        )
    }
}

@Composable
fun UnitPickerDialog(
    title: String,
    units: List<UnitItem>,
    selectedUnit: UnitItem,
    onDismiss: () -> Unit,
    onSelect: (UnitItem) -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background),
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.6f)
                .border(0.5.dp, GrayText.copy(alpha = 0.3f), RoundedCornerShape(16.dp))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Text(
                    text = title.uppercase(),
                    style = MaterialTheme.typography.titleLarge.copy(fontSize = 16.sp, letterSpacing = 1.sp, fontWeight = FontWeight.Bold),
                    modifier = Modifier.padding(bottom = 12.dp)
                )
                
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .verticalScroll(rememberScrollState())
                ) {
                    units.forEach { unit ->
                        val isSelected = unit.symbol == selectedUnit.symbol
                        val bgColor = if (isSelected) MaterialTheme.colorScheme.secondary else Color.Transparent
                        
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(bgColor)
                                .clickable { onSelect(unit) }
                                .padding(horizontal = 12.dp, vertical = 14.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = unit.name,
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                )
                            )
                            Text(
                                text = unit.symbol,
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
