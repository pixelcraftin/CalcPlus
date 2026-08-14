package com.calculatorplus.app.ui.components

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.calculatorplus.app.ui.theme.CalcShapes
import com.calculatorplus.app.ui.theme.RedAccent

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun KeypadButton(
    text: String,
    onClick: () -> Unit,
    onLongClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier,
    shapeName: String = "Rounded Rectangle",
    backgroundColor: Color = MaterialTheme.colorScheme.secondary,
    contentColor: Color = MaterialTheme.colorScheme.onBackground,
    isAccent: Boolean = false,
    isEquals: Boolean = false,
    aspectRatio: Float? = null,
    icon: androidx.compose.ui.graphics.vector.ImageVector? = null
) {
    val haptic = LocalHapticFeedback.current
    val shape: Shape = CalcShapes.getButtonShape(shapeName)
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    // Depress scaling spring animation (StiffnessMedium -> StiffnessMediumHigh for snappiness)
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.90f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = 1500f
        ),
        label = "ButtonScale"
    )

    // Opacity shift spring animation
    val alpha by animateFloatAsState(
        targetValue = if (isPressed) 0.80f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioNoBouncy,
            stiffness = Spring.StiffnessHigh
        ),
        label = "ButtonAlpha"
    )

    // Choose secondary (number) or surface (operator) button theme colors
    val isDigit = text.all { it.isDigit() } || text == "." || text == "00" || text == "π" || text == "e"
    val defaultBgColor = if (isDigit) {
        MaterialTheme.colorScheme.secondary  // Number Button Color
    } else {
        MaterialTheme.colorScheme.surface    // Operator Button Color
    }

    val finalBgColor = when {
        isAccent -> RedAccent
        isEquals -> MaterialTheme.colorScheme.primary
        else -> if (backgroundColor == MaterialTheme.colorScheme.secondary) defaultBgColor else backgroundColor
    }

    val finalContentColor = when {
        isAccent -> Color.White
        isEquals -> MaterialTheme.colorScheme.onPrimary
        contentColor != MaterialTheme.colorScheme.onBackground -> contentColor
        else -> {
            if (isDigit) {
                MaterialTheme.colorScheme.onSecondary
            } else {
                MaterialTheme.colorScheme.onSurface
            }
        }
    }

    Card(
        shape = shape,
        colors = CardDefaults.cardColors(
            containerColor = finalBgColor,
            contentColor = finalContentColor
        ),
        modifier = modifier
            .padding(4.dp)
            .then(if (aspectRatio != null) Modifier.aspectRatio(aspectRatio) else Modifier)
            // Smooth 120Hz response
            .graphicsLayer {
                this.scaleX = scale
                this.scaleY = scale
                this.alpha = alpha
            }
            .combinedClickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = {
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    onClick()
                },
                onLongClick = if (onLongClick != null) {
                    {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        onLongClick()
                    }
                } else null
            ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = text,
                    tint = finalContentColor,
                    modifier = Modifier.size(24.dp)
                )
            } else {
                val isOperatorSymbol = text == "%" || text == "⌫" || text == "⇅" || text == "nCr" || text == "1/x" || text == "10˟" || text == "x²" || text == "x˟" || text == "±" || text == "2nd" || text == "x!"
                val calculatedFontSize = when {
                    isOperatorSymbol -> 22.sp
                    text.length > 2 -> 20.sp
                    text.length > 1 -> 24.sp
                    else -> 28.sp
                }

                Text(
                    text = text,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontSize = calculatedFontSize,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    ),
                    color = finalContentColor
                )
            }
        }
    }
}
