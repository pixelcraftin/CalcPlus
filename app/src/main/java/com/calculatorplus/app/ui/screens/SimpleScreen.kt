package com.calculatorplus.app.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.calculatorplus.app.data.util.PreferencesManager
import com.calculatorplus.app.ui.components.DisplayArea
import com.calculatorplus.app.ui.components.KeypadButton
import com.calculatorplus.app.ui.components.KeypadGrid

@Composable
fun SimpleScreen(
    viewModel: CalculatorViewModel,
    preferencesManager: PreferencesManager,
    modifier: Modifier = Modifier
) {
    val buttonShape = preferencesManager.buttonShape

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(4.dp)
    ) {
        val context = androidx.compose.ui.platform.LocalContext.current
        val clipboardManager = androidx.compose.ui.platform.LocalClipboardManager.current

        // Display Panel Area
        DisplayArea(
            expression = viewModel.expression,
            resultPreview = viewModel.resultPreview,
            modifier = Modifier.weight(0.35f),
            onCopyClick = {
                val textToCopy = viewModel.expression.ifEmpty { "0" }
                clipboardManager.setText(androidx.compose.ui.text.AnnotatedString(textToCopy))
                android.widget.Toast.makeText(context, "Copied to clipboard", android.widget.Toast.LENGTH_SHORT).show()
            }
        )

        // Keypad Grid Layout
        KeypadGrid(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.65f)
                .navigationBarsPadding()
                .padding(bottom = 2.dp),
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
