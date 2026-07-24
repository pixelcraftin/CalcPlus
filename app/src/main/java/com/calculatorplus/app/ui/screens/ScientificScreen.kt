package com.calculatorplus.app.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.calculatorplus.app.data.util.PreferencesManager
import com.calculatorplus.app.ui.components.DisplayArea
import com.calculatorplus.app.ui.components.KeypadButton
import com.calculatorplus.app.ui.components.KeypadGrid

@Composable
fun ScientificScreen(
    viewModel: CalculatorViewModel,
    preferencesManager: PreferencesManager,
    modifier: Modifier = Modifier
) {
    val buttonShape = preferencesManager.buttonShape
    val context = androidx.compose.ui.platform.LocalContext.current
    val clipboardManager = androidx.compose.ui.platform.LocalClipboardManager.current
    var isSecondActive by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        // Display Panel Area with inline DEG/RAD pill and Copy Action
        DisplayArea(
            expression = viewModel.expression,
            resultPreview = viewModel.resultPreview,
            showDegRad = true,
            isDegreeMode = viewModel.isDegreeMode,
            onDegRadToggle = { viewModel.toggleDegreeRad() },
            onCopyClick = {
                val textToCopy = viewModel.expression.ifEmpty { "0" }
                clipboardManager.setText(androidx.compose.ui.text.AnnotatedString(textToCopy))
                android.widget.Toast.makeText(context, "Copied to clipboard", android.widget.Toast.LENGTH_SHORT).show()
            },
            modifier = Modifier.weight(0.35f)
        )

        // 7-Row Keypad Grid matching the reference image layout
        KeypadGrid(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.65f)
                .navigationBarsPadding()
                .padding(bottom = 2.dp),
            spacing = 6
        ) {
            // Row 1: 2nd, sin, cos, tan, pi
            Row(modifier = Modifier.fillMaxWidth().weight(1f)) {
                KeypadButton(
                    text = "2nd",
                    onClick = { isSecondActive = !isSecondActive },
                    shapeName = buttonShape,
                    backgroundColor = if (isSecondActive) MaterialTheme.colorScheme.primary.copy(alpha = 0.2f) else MaterialTheme.colorScheme.surface,
                    modifier = Modifier.weight(1f).fillMaxHeight(),
                    aspectRatio = null
                )
                KeypadButton(text = if (isSecondActive) "asin" else "sin", onClick = { viewModel.appendFunction(if (isSecondActive) "asin" else "sin") }, shapeName = buttonShape, modifier = Modifier.weight(1f), aspectRatio = null)
                KeypadButton(text = if (isSecondActive) "acos" else "cos", onClick = { viewModel.appendFunction(if (isSecondActive) "acos" else "cos") }, shapeName = buttonShape, modifier = Modifier.weight(1f), aspectRatio = null)
                KeypadButton(text = if (isSecondActive) "atan" else "tan", onClick = { viewModel.appendFunction(if (isSecondActive) "atan" else "tan") }, shapeName = buttonShape, modifier = Modifier.weight(1f), aspectRatio = null)
                KeypadButton(text = "π", onClick = { viewModel.onKeyClick("π") }, shapeName = buttonShape, modifier = Modifier.weight(1f), aspectRatio = null)
            }

            // Row 2: x², ln, 10˟, 1/x, e
            Row(modifier = Modifier.fillMaxWidth().weight(1f)) {
                KeypadButton(text = "x²", onClick = { viewModel.onKeyClick("^2") }, shapeName = buttonShape, modifier = Modifier.weight(1f), aspectRatio = null)
                KeypadButton(text = "ln", onClick = { viewModel.appendFunction("ln") }, shapeName = buttonShape, modifier = Modifier.weight(1f), aspectRatio = null)
                KeypadButton(text = "10˟", onClick = { viewModel.onKeyClick("10^") }, shapeName = buttonShape, modifier = Modifier.weight(1f), aspectRatio = null)
                KeypadButton(text = "1/x", onClick = { viewModel.onKeyClick("1÷") }, shapeName = buttonShape, modifier = Modifier.weight(1f), aspectRatio = null)
                KeypadButton(text = "e", onClick = { viewModel.onKeyClick("e") }, shapeName = buttonShape, modifier = Modifier.weight(1f), aspectRatio = null)
            }

            // Row 3: √, AC, (), %, ÷
            Row(modifier = Modifier.fillMaxWidth().weight(1f)) {
                KeypadButton(text = "√", onClick = { viewModel.onKeyClick("√") }, shapeName = buttonShape, modifier = Modifier.weight(1f), aspectRatio = null)
                KeypadButton(text = "AC", onClick = { viewModel.onKeyClick("AC") }, shapeName = buttonShape, isAccent = true, modifier = Modifier.weight(1f), aspectRatio = null)
                KeypadButton(text = "()", onClick = { viewModel.onKeyClick("()") }, shapeName = buttonShape, modifier = Modifier.weight(1f), aspectRatio = null)
                KeypadButton(text = "%", onClick = { viewModel.onKeyClick("%") }, shapeName = buttonShape, modifier = Modifier.weight(1f), aspectRatio = null)
                KeypadButton(text = "÷", onClick = { viewModel.onKeyClick("÷") }, shapeName = buttonShape, modifier = Modifier.weight(1f), aspectRatio = null)
            }

            // Row 4: x˟, 7, 8, 9, ×
            Row(modifier = Modifier.fillMaxWidth().weight(1f)) {
                KeypadButton(text = "x˟", onClick = { viewModel.onKeyClick("^") }, shapeName = buttonShape, modifier = Modifier.weight(1f), aspectRatio = null)
                KeypadButton(text = "7", onClick = { viewModel.onKeyClick("7") }, shapeName = buttonShape, modifier = Modifier.weight(1f), aspectRatio = null)
                KeypadButton(text = "8", onClick = { viewModel.onKeyClick("8") }, shapeName = buttonShape, modifier = Modifier.weight(1f), aspectRatio = null)
                KeypadButton(text = "9", onClick = { viewModel.onKeyClick("9") }, shapeName = buttonShape, modifier = Modifier.weight(1f), aspectRatio = null)
                KeypadButton(text = "×", onClick = { viewModel.onKeyClick("×") }, shapeName = buttonShape, modifier = Modifier.weight(1f), aspectRatio = null)
            }

            // Row 5: x!, 4, 5, 6, -
            Row(modifier = Modifier.fillMaxWidth().weight(1f)) {
                KeypadButton(text = "x!", onClick = { viewModel.onKeyClick("!") }, shapeName = buttonShape, modifier = Modifier.weight(1f), aspectRatio = null)
                KeypadButton(text = "4", onClick = { viewModel.onKeyClick("4") }, shapeName = buttonShape, modifier = Modifier.weight(1f), aspectRatio = null)
                KeypadButton(text = "5", onClick = { viewModel.onKeyClick("5") }, shapeName = buttonShape, modifier = Modifier.weight(1f), aspectRatio = null)
                KeypadButton(text = "6", onClick = { viewModel.onKeyClick("6") }, shapeName = buttonShape, modifier = Modifier.weight(1f), aspectRatio = null)
                KeypadButton(text = "-", onClick = { viewModel.onKeyClick("-") }, shapeName = buttonShape, modifier = Modifier.weight(1f), aspectRatio = null)
            }

            // Row 6: ±, 1, 2, 3, +
            Row(modifier = Modifier.fillMaxWidth().weight(1f)) {
                KeypadButton(text = "±", onClick = { viewModel.onKeyClick("±") }, shapeName = buttonShape, modifier = Modifier.weight(1f), aspectRatio = null)
                KeypadButton(text = "1", onClick = { viewModel.onKeyClick("1") }, shapeName = buttonShape, modifier = Modifier.weight(1f), aspectRatio = null)
                KeypadButton(text = "2", onClick = { viewModel.onKeyClick("2") }, shapeName = buttonShape, modifier = Modifier.weight(1f), aspectRatio = null)
                KeypadButton(text = "3", onClick = { viewModel.onKeyClick("3") }, shapeName = buttonShape, modifier = Modifier.weight(1f), aspectRatio = null)
                KeypadButton(text = "+", onClick = { viewModel.onKeyClick("+") }, shapeName = buttonShape, modifier = Modifier.weight(1f), aspectRatio = null)
            }

            // Row 7: nCr, 0, ., ⌫, =
            Row(modifier = Modifier.fillMaxWidth().weight(1f)) {
                KeypadButton(text = "nCr", onClick = { viewModel.onKeyClick("nCr") }, shapeName = buttonShape, modifier = Modifier.weight(1f), aspectRatio = null)
                KeypadButton(text = "0", onClick = { viewModel.onKeyClick("0") }, shapeName = buttonShape, modifier = Modifier.weight(1f), aspectRatio = null)
                KeypadButton(text = ".", onClick = { viewModel.onKeyClick(".") }, shapeName = buttonShape, modifier = Modifier.weight(1f), aspectRatio = null)
                KeypadButton(text = "⌫", onClick = { viewModel.onKeyClick("⌫") }, onLongClick = { viewModel.onKeyClick("AC") }, shapeName = buttonShape, modifier = Modifier.weight(1f), aspectRatio = null)
                KeypadButton(
                    text = "=",
                    onClick = { viewModel.onKeyClick("=", preferencesManager) },
                    shapeName = buttonShape,
                    isEquals = true,
                    modifier = Modifier.weight(1f).fillMaxHeight(),
                    aspectRatio = null
                )
            }
        }
    }
}
