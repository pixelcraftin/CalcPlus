package com.calculatorplus.app.ui.components

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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.calculatorplus.app.ui.theme.GrayText
import com.calculatorplus.app.ui.theme.RedAccent

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

    Column(
        modifier = modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .padding(horizontal = 12.dp),
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
                    .padding(3.dp)
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
                            color = RedAccent, // Coral Red instead of Blue
                            shape = RoundedCornerShape(16.dp)
                        )
                )

                // Tab Text labels overlay
                Row(modifier = Modifier.fillMaxSize()) {
                    tabs.forEachIndexed { index, tab ->
                        val isSelected = index == currentPage
                        val textColor = if (isSelected) Color.White else MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
                        val textWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal

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
                                text = tab,
                                style = MaterialTheme.typography.labelLarge.copy(
                                    fontWeight = textWeight,
                                    fontSize = 11.sp,
                                    letterSpacing = 0.5.sp
                                ),
                                color = textColor
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.width(6.dp))

            Row(
                modifier = Modifier
                    .height(38.dp)
                    .background(
                        color = MaterialTheme.colorScheme.secondary,
                        shape = RoundedCornerShape(19.dp)
                    )
                    .padding(horizontal = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                // History Action Icon
                IconButton(
                    onClick = {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        onHistoryClick()
                    },
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.History,
                        contentDescription = "Calculation History",
                        tint = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.size(18.dp)
                    )
                }

                // Settings Action Icon
                IconButton(
                    onClick = {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        onSettingsClick()
                    },
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = "Settings",
                        tint = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}
