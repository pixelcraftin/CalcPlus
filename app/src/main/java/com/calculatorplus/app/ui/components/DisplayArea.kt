package com.calculatorplus.app.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.calculatorplus.app.ui.theme.GrayText

@Composable
fun DisplayArea(
    expression: String,
    resultPreview: String,
    modifier: Modifier = Modifier,
    showDegRad: Boolean = false,
    isDegreeMode: Boolean = false,
    onDegRadToggle: (() -> Unit)? = null,
    onCopyClick: () -> Unit
) {
    val fontSize = when {
        expression.length <= 8 -> 54.sp
        expression.length <= 12 -> 42.sp
        expression.length <= 18 -> 32.sp
        else -> 24.sp
    }

    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.onBackground.copy(alpha = 0.15f)),
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Row 1: Left (DEG/RAD Toggle) and Right (Copy Icon)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (showDegRad && onDegRadToggle != null) {
                    Box(
                        modifier = Modifier
                            .background(
                                color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.6f),
                                shape = RoundedCornerShape(12.dp)
                            )
                            .clickable { onDegRadToggle() }
                            .padding(horizontal = 8.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = if (isDegreeMode) "deg" else "rad",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            ),
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }
                } else {
                    Spacer(modifier = Modifier.weight(1f))
                }

                // Copy button
                IconButton(
                    onClick = onCopyClick,
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.ContentCopy,
                        contentDescription = "Copy to Clipboard",
                        tint = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Expressions display text
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.Bottom
            ) {
                Text(
                    text = expression.ifEmpty { "0" },
                    style = MaterialTheme.typography.displayLarge.copy(
                        fontSize = fontSize,
                        textAlign = TextAlign.End,
                        lineHeight = (fontSize.value * 1.3).sp
                    ),
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.fillMaxWidth()
                )
                
                if (resultPreview.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = resultPreview,
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontSize = 26.sp,
                            textAlign = TextAlign.End
                        ),
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                        maxLines = 1,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}
