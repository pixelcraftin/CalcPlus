package com.pixelcraftin.calcplus.ui.components

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pixelcraftin.calcplus.ui.theme.PlusJakartaSansFontFamily
import com.pixelcraftin.calcplus.ui.theme.RedAccent

@Composable
fun TopHeaderNav(
    currentPage: Int,
    onTabSelected: (Int) -> Unit,
    onHistoryClick: () -> Unit,
    onSettingsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val haptic = LocalHapticFeedback.current
    val tabs = listOf("Simple", "Scientific", "Converter", "More")

    val configuration = androidx.compose.ui.platform.LocalConfiguration.current
    val isLandscape = configuration.orientation == android.content.res.Configuration.ORIENTATION_LANDSCAPE

    Box(
        modifier = modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .widthIn(max = if (isLandscape) 1100.dp else 600.dp)
                .height(54.dp)
                .padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Single Capsule Bar container
            BoxWithConstraints(
                modifier = Modifier
                    .weight(1f)
                    .height(38.dp)
                    .background(
                        color = MaterialTheme.colorScheme.secondary,
                        shape = RoundedCornerShape(19.dp)
                    )
                    .padding(2.5.dp)
            ) {
                val tabWidth = maxWidth / tabs.size
                val animatedOffset by animateDpAsState(
                    targetValue = tabWidth * currentPage,
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessMedium
                    ),
                    label = "CapsuleOffset"
                )

                // Sliding active background capsule
                Box(
                    modifier = Modifier
                        .offset(x = animatedOffset)
                        .width(tabWidth)
                        .fillMaxHeight()
                        .background(
                            color = RedAccent, // Coral Red accent
                            shape = RoundedCornerShape(16.5.dp)
                        )
                )

                // Tab Text labels overlay
                Row(modifier = Modifier.fillMaxSize()) {
                    tabs.forEachIndexed { index, tab ->
                        val isSelected = index == currentPage
                        val textColor = if (isSelected) Color.White else MaterialTheme.colorScheme.onBackground.copy(alpha = 0.75f)
                        val textWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium

                        // Adaptive label for ultra-compact or split screens (< 58dp per tab)
                        val displayLabel = when (index) {
                            0 -> "Simple"
                            1 -> if (tabWidth < 58.dp) "Sci" else "Scientific"
                            2 -> if (tabWidth < 58.dp) "Convert" else "Converter"
                            3 -> "More"
                            else -> tab
                        }

                        val fontSize = when {
                            tabWidth < 60.dp -> 10.sp
                            tabWidth < 70.dp -> 10.5.sp
                            tabWidth < 80.dp -> 11.sp
                            else -> 11.5.sp
                        }

                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = null
                                ) {
                                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                    onTabSelected(index)
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = displayLabel,
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontFamily = PlusJakartaSansFontFamily,
                                    fontWeight = textWeight,
                                    fontSize = fontSize,
                                    letterSpacing = 0.sp
                                ),
                                maxLines = 1,
                                softWrap = false,
                                overflow = TextOverflow.Clip,
                                color = textColor
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.width(4.dp))

            Row(
                modifier = Modifier
                    .height(38.dp)
                    .background(
                        color = MaterialTheme.colorScheme.secondary,
                        shape = RoundedCornerShape(19.dp)
                    )
                    .padding(horizontal = 3.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(1.dp)
            ) {
                // History Action Icon
                IconButton(
                    onClick = {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        onHistoryClick()
                    },
                    modifier = Modifier.size(31.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.History,
                        contentDescription = "Calculation History",
                        tint = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.size(17.dp)
                    )
                }

                // Settings Action Icon
                IconButton(
                    onClick = {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        onSettingsClick()
                    },
                    modifier = Modifier.size(31.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = "Settings",
                        tint = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.size(17.dp)
                    )
                }
            }
        }
    }
}
