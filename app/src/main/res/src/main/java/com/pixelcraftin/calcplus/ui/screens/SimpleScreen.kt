package com.pixelcraftin.calcplus.ui.screens

import android.content.res.Configuration
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Backspace
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import com.pixelcraftin.calcplus.data.util.PreferencesManager
import com.pixelcraftin.calcplus.ui.components.DisplayArea
import com.pixelcraftin.calcplus.ui.components.KeypadButton
import com.pixelcraftin.calcplus.ui.components.KeypadGrid

@Composable
fun SimpleScreen(
    viewModel: CalculatorViewModel,
    preferencesManager: PreferencesManager,
    modifier: Modifier = Modifier
) {
    val buttonShape = preferencesManager.buttonShape
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    if (isLandscape) {
        // Landscape Mode: Side-by-Side Layout
        Row(
            modifier = modifier
                .fillMaxSize()
                .padding(horizontal = 8.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Left: Display Area Card
            DisplayArea(
                expression = viewModel.expression,
                resultPreview = viewModel.resultPreview,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .padding(vertical = 2.dp),
                onCopyClick = {
                    val textToCopy = viewModel.expression.ifEmpty { "0" }
                    clipboardManager.setText(AnnotatedString(textToCopy))
                    Toast.makeText(context, "Copied to clipboard", Toast.LENGTH_SHORT).show()
                }
            )

            // Right: 4x5 Keypad Grid Layout
            KeypadGrid(
                modifier = Modifier
                    .weight(1.3f)
                    .fillMaxHeight()
                    .padding(bottom = 2.dp),
                spacing = 4
            ) {
                // Row 1: AC, %, (), ÷
                Row(modifier = Modifier.fillMaxWidth().weight(1f)) {
                    KeypadButton(
                        text = "AC",
                        onClick = { viewModel.onKeyClick("AC") },
                        shapeName = buttonShape,
                        isAccent = true,
                        modifier = Modifier.weight(1f).fillMaxHeight()
                    )
                    KeypadButton(
                        text = "%",
                        onClick = { viewModel.onKeyClick("%") },
                        shapeName = buttonShape,
                        modifier = Modifier.weight(1f).fillMaxHeight()
                    )
                    KeypadButton(
                        text = "()",
                        onClick = { viewModel.onKeyClick("()") },
                        shapeName = buttonShape,
                        modifier = Modifier.weight(1f).fillMaxHeight()
                    )
                    KeypadButton(
                        text = "÷",
                        onClick = { viewModel.onKeyClick("÷") },
                        shapeName = buttonShape,
                        modifier = Modifier.weight(1f).fillMaxHeight()
                    )
                }

                // Row 2: 7, 8, 9, ×
                Row(modifier = Modifier.fillMaxWidth().weight(1f)) {
                    KeypadButton(
                        text = "7",
                        onClick = { viewModel.onKeyClick("7") },
                        shapeName = buttonShape,
                        modifier = Modifier.weight(1f).fillMaxHeight()
                    )
                    KeypadButton(
                        text = "8",
                        onClick = { viewModel.onKeyClick("8") },
                        shapeName = buttonShape,
                        modifier = Modifier.weight(1f).fillMaxHeight()
                    )
                    KeypadButton(
                        text = "9",
                        onClick = { viewModel.onKeyClick("9") },
                        shapeName = buttonShape,
                        modifier = Modifier.weight(1f).fillMaxHeight()
                    )
                    KeypadButton(
                        text = "×",
                        onClick = { viewModel.onKeyClick("×") },
                        shapeName = buttonShape,
                        modifier = Modifier.weight(1f).fillMaxHeight()
                    )
                }

                // Row 3: 4, 5, 6, -
                Row(modifier = Modifier.fillMaxWidth().weight(1f)) {
                    KeypadButton(
                        text = "4",
                        onClick = { viewModel.onKeyClick("4") },
                        shapeName = buttonShape,
                        modifier = Modifier.weight(1f).fillMaxHeight()
                    )
                    KeypadButton(
                        text = "5",
                        onClick = { viewModel.onKeyClick("5") },
                        shapeName = buttonShape,
                        modifier = Modifier.weight(1f).fillMaxHeight()
                    )
                    KeypadButton(
                        text = "6",
                        onClick = { viewModel.onKeyClick("6") },
                        shapeName = buttonShape,
                        modifier = Modifier.weight(1f).fillMaxHeight()
                    )
                    KeypadButton(
                        text = "-",
                        onClick = { viewModel.onKeyClick("-") },
                        shapeName = buttonShape,
                        modifier = Modifier.weight(1f).fillMaxHeight()
                    )
                }

                // Row 4: 1, 2, 3, +
                Row(modifier = Modifier.fillMaxWidth().weight(1f)) {
                    KeypadButton(
                        text = "1",
                        onClick = { viewModel.onKeyClick("1") },
                        shapeName = buttonShape,
                        modifier = Modifier.weight(1f).fillMaxHeight()
                    )
                    KeypadButton(
                        text = "2",
                        onClick = { viewModel.onKeyClick("2") },
                        shapeName = buttonShape,
                        modifier = Modifier.weight(1f).fillMaxHeight()
                    )
                    KeypadButton(
                        text = "3",
                        onClick = { viewModel.onKeyClick("3") },
                        shapeName = buttonShape,
                        modifier = Modifier.weight(1f).fillMaxHeight()
                    )
                    KeypadButton(
                        text = "+",
                        onClick = { viewModel.onKeyClick("+") },
                        shapeName = buttonShape,
                        modifier = Modifier.weight(1f).fillMaxHeight()
                    )
                }

                // Row 5: 0, ., ⌫, =
                Row(modifier = Modifier.fillMaxWidth().weight(1f)) {
                    KeypadButton(
                        text = "0",
                        onClick = { viewModel.onKeyClick("0") },
                        shapeName = buttonShape,
                        modifier = Modifier.weight(1f).fillMaxHeight()
                    )
                    KeypadButton(
                        text = ".",
                        onClick = { viewModel.onKeyClick(".") },
                        shapeName = buttonShape,
                        modifier = Modifier.weight(1f).fillMaxHeight()
                    )
                    KeypadButton(
                        text = "⌫",
                        icon = Icons.AutoMirrored.Outlined.Backspace,
                        onClick = { viewModel.onKeyClick("⌫") },
                        onLongClick = { viewModel.onKeyClick("AC") },
                        shapeName = buttonShape,
                        modifier = Modifier.weight(1f).fillMaxHeight()
                    )
                    KeypadButton(
                        text = "=",
                        onClick = { viewModel.onKeyClick("=", preferencesManager) },
                        shapeName = buttonShape,
                        isEquals = true,
                        modifier = Modifier.weight(1f).fillMaxHeight()
                    )
                }
            }
        }
    } else {
        // Portrait Mode: Vertical Column
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(4.dp)
        ) {
            val context = LocalContext.current
            val clipboardManager = LocalClipboardManager.current

        // Display Panel Area
        DisplayArea(
            expression = viewModel.expression,
            resultPreview = viewModel.resultPreview,
            modifier = Modifier.weight(0.30f),
            onCopyClick = {
                val textToCopy = viewModel.expression.ifEmpty { "0" }
                clipboardManager.setText(AnnotatedString(textToCopy))
                Toast.makeText(context, "Copied to clipboard", Toast.LENGTH_SHORT).show()
            }
        )

        // Keypad Grid Layout
        KeypadGrid(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.70f)
                .padding(bottom = 4.dp),
            spacing = 6
        ) {
            // Row 1
            Row(modifier = Modifier.fillMaxWidth().weight(1f)) {
                KeypadButton(
                    text = "AC",
                    onClick = { viewModel.onKeyClick("AC") },
                    shapeName = buttonShape,
                    isAccent = true,
                    modifier = Modifier.weight(1f).fillMaxHeight()
                )
                KeypadButton(
                    text = "()",
                    onClick = { viewModel.onKeyClick("()") },
                    shapeName = buttonShape,
                    modifier = Modifier.weight(1f).fillMaxHeight()
                )
                KeypadButton(
                    text = "%",
                    onClick = { viewModel.onKeyClick("%") },
                    shapeName = buttonShape,
                    modifier = Modifier.weight(1f).fillMaxHeight()
                )
                KeypadButton(
                    text = "÷",
                    onClick = { viewModel.onKeyClick("÷") },
                    shapeName = buttonShape,
                    modifier = Modifier.weight(1f).fillMaxHeight()
                )
            }

            // Row 2
            Row(modifier = Modifier.fillMaxWidth().weight(1f)) {
                KeypadButton(
                    text = "7",
                    onClick = { viewModel.onKeyClick("7") },
                    shapeName = buttonShape,
                    modifier = Modifier.weight(1f).fillMaxHeight()
                )
                KeypadButton(
                    text = "8",
                    onClick = { viewModel.onKeyClick("8") },
                    shapeName = buttonShape,
                    modifier = Modifier.weight(1f).fillMaxHeight()
                )
                KeypadButton(
                    text = "9",
                    onClick = { viewModel.onKeyClick("9") },
                    shapeName = buttonShape,
                    modifier = Modifier.weight(1f).fillMaxHeight()
                )
                KeypadButton(
                    text = "×",
                    onClick = { viewModel.onKeyClick("×") },
                    shapeName = buttonShape,
                    modifier = Modifier.weight(1f).fillMaxHeight()
                )
            }

            // Row 3
            Row(modifier = Modifier.fillMaxWidth().weight(1f)) {
                KeypadButton(
                    text = "4",
                    onClick = { viewModel.onKeyClick("4") },
                    shapeName = buttonShape,
                    modifier = Modifier.weight(1f).fillMaxHeight()
                )
                KeypadButton(
                    text = "5",
                    onClick = { viewModel.onKeyClick("5") },
                    shapeName = buttonShape,
                    modifier = Modifier.weight(1f).fillMaxHeight()
                )
                KeypadButton(
                    text = "6",
                    onClick = { viewModel.onKeyClick("6") },
                    shapeName = buttonShape,
                    modifier = Modifier.weight(1f).fillMaxHeight()
                )
                KeypadButton(
                    text = "-",
                    onClick = { viewModel.onKeyClick("-") },
                    shapeName = buttonShape,
                    modifier = Modifier.weight(1f).fillMaxHeight()
                )
            }

            // Row 4
            Row(modifier = Modifier.fillMaxWidth().weight(1f)) {
                KeypadButton(
                    text = "1",
                    onClick = { viewModel.onKeyClick("1") },
                    shapeName = buttonShape,
                    modifier = Modifier.weight(1f).fillMaxHeight()
                )
                KeypadButton(
                    text = "2",
                    onClick = { viewModel.onKeyClick("2") },
                    shapeName = buttonShape,
                    modifier = Modifier.weight(1f).fillMaxHeight()
                )
                KeypadButton(
                    text = "3",
                    onClick = { viewModel.onKeyClick("3") },
                    shapeName = buttonShape,
                    modifier = Modifier.weight(1f).fillMaxHeight()
                )
                KeypadButton(
                    text = "+",
                    onClick = { viewModel.onKeyClick("+") },
                    shapeName = buttonShape,
                    modifier = Modifier.weight(1f).fillMaxHeight()
                )
            }

            // Row 5
            Row(modifier = Modifier.fillMaxWidth().weight(1f)) {
                KeypadButton(
                    text = "0",
                    onClick = { viewModel.onKeyClick("0") },
                    shapeName = buttonShape,
                    modifier = Modifier.weight(1f).fillMaxHeight()
                )
                KeypadButton(
                    text = ".",
                    onClick = { viewModel.onKeyClick(".") },
                    shapeName = buttonShape,
                    modifier = Modifier.weight(1f).fillMaxHeight()
                )
                KeypadButton(
                    text = "C",
                    icon = Icons.AutoMirrored.Outlined.Backspace,
                    onClick = { viewModel.onKeyClick("⌫") },
                    onLongClick = { viewModel.onKeyClick("AC") },
                    shapeName = buttonShape,
                    modifier = Modifier.weight(1f).fillMaxHeight()
                )
                KeypadButton(
                    text = "=",
                    onClick = { viewModel.onKeyClick("=", preferencesManager) },
                    shapeName = buttonShape,
                    isEquals = true,
                    modifier = Modifier.weight(1f).fillMaxHeight()
                )
            }
        }
    }
}
}
