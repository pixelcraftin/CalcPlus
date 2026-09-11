package com.pixelcraftin.calcplus.ui.screens

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.pixelcraftin.calcplus.app.R
import com.pixelcraftin.calcplus.data.util.PreferencesManager
import com.pixelcraftin.calcplus.ui.components.SettingItem
import com.pixelcraftin.calcplus.ui.navigation.Screen
import com.pixelcraftin.calcplus.ui.theme.GrayText
import com.pixelcraftin.calcplus.ui.theme.PlusJakartaSansFontFamily
import com.pixelcraftin.calcplus.ui.theme.RedAccent

@Composable
fun SettingsScreen(
    navController: NavController,
    preferencesManager: PreferencesManager,
    onThemeChange: () -> Unit,
    onShapeChange: () -> Unit,
    onFontChange: () -> Unit,
    modifier: Modifier = Modifier
) {
    val uriHandler = LocalUriHandler.current

    var selectedTheme by remember { mutableStateOf(preferencesManager.theme) }
    var selectedShape by remember { mutableStateOf(preferencesManager.buttonShape) }
    var selectedFont by remember { mutableStateOf(preferencesManager.fontStyle) }

    var showThemeDialog by remember { mutableStateOf(false) }
    var showShapeDialog by remember { mutableStateOf(false) }
    var showFontDialog by remember { mutableStateOf(false) }

    var isAboutExpanded by remember { mutableStateOf(false) }
    val isDoto = MaterialTheme.typography.titleLarge.fontFamily == com.pixelcraftin.calcplus.ui.theme.DotoFontFamily

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier
                .widthIn(max = 600.dp)
                .fillMaxWidth()
        ) {
        // Standard Inline Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
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
                text = "SETTINGS",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = if (isDoto) FontWeight.Bold else FontWeight.Medium,
                    fontSize = 20.sp,
                    letterSpacing = 1.sp
                ),
                color = MaterialTheme.colorScheme.onBackground
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .verticalScroll(rememberScrollState())
        ) {
            // Section A: Appearance
            Text(
                text = "APPEARANCE & CUSTOMIZATION",
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = if (isDoto) FontWeight.Bold else FontWeight.Medium,
                    letterSpacing = 1.sp
                ),
                color = GrayText,
                modifier = Modifier.padding(start = 20.dp, top = 24.dp, bottom = 8.dp)
            )

            SettingItem(
                title = "Theme",
                subtitle = selectedTheme,
                onClick = { showThemeDialog = true }
            )

            SettingItem(
                title = "Keypad Button Shape",
                subtitle = selectedShape,
                onClick = { showShapeDialog = true }
            )

            SettingItem(
                title = "Font Style",
                subtitle = when (selectedFont) {
                    PreferencesManager.FONT_DOT_MATRIX -> "Dot Matrix (Doto Font)"
                    PreferencesManager.FONT_INTER -> "Inter"
                    PreferencesManager.FONT_ROBOTO_SLAB -> "Roboto Slab"
                    else -> "System"
                },
                onClick = { showFontDialog = true }
            )

            // Section B: Legal & Licenses
            Text(
                text = "LEGAL & LICENSES",
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = if (isDoto) FontWeight.Bold else FontWeight.Medium,
                    letterSpacing = 1.sp
                ),
                color = GrayText,
                modifier = Modifier.padding(start = 20.dp, top = 24.dp, bottom = 8.dp)
            )

            SettingItem(
                title = "About",
                subtitle = "Application details and creator",
                onClick = { isAboutExpanded = !isAboutExpanded }
            )

            AnimatedVisibility(
                visible = isAboutExpanded,
                enter = expandVertically(
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioNoBouncy,
                        stiffness = Spring.StiffnessMedium
                    )
                ) + fadeIn(
                    animationSpec = tween(150)
                ),
                exit = shrinkVertically(
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioNoBouncy,
                        stiffness = Spring.StiffnessMedium
                    )
                ) + fadeOut(
                    animationSpec = tween(150)
                )
            ) {
                val context = LocalContext.current
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.4f)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 8.dp)
                        .border(0.5.dp, GrayText.copy(alpha = 0.2f), RoundedCornerShape(16.dp))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 24.dp, horizontal = 20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.icon2),
                            contentDescription = "App Icon",
                            modifier = Modifier
                                .size(90.dp)
                                .clip(RoundedCornerShape(20.dp))
                        )
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        Text(
                            text = "Calc +",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = if (isDoto) FontWeight.Bold else FontWeight.Medium,
                                fontSize = 20.sp
                            ),
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        
                        Spacer(modifier = Modifier.height(4.dp))
                        
                        Text(
                            text = "Version 1.1.1",
                            style = MaterialTheme.typography.bodyMedium,
                            color = GrayText
                        )
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        HorizontalDivider(
                            modifier = Modifier.fillMaxWidth(),
                            thickness = 0.5.dp,
                            color = GrayText.copy(alpha = 0.2f)
                        )
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        Text(
                            text = "Created by",
                            style = MaterialTheme.typography.bodySmall,
                            color = GrayText
                        )
                        
                        Spacer(modifier = Modifier.height(4.dp))
                        
                        Text(
                            text = "pixelcraftin",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = if (isDoto) FontWeight.Bold else FontWeight.Medium,
                                fontSize = 18.sp
                            ),
                            color = RedAccent,
                            modifier = Modifier.clickable {
                                try {
                                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/pixelcraftin"))
                                    context.startActivity(intent)
                                } catch (e: Exception) {
                                    Toast.makeText(context, "No app available to open links", Toast.LENGTH_SHORT).show()
                                }
                            }
                        )
                    }
                }
            }

            SettingItem(
                title = "Check out our other apps",
                subtitle = "Explore more utilities from pixelcraftin",
                onClick = { uriHandler.openUri("https://play.google.com/store/apps/dev?id=4643660879459504423") }
            )

            SettingItem(
                title = "Privacy Policy",
                subtitle = "Read the application privacy guidelines",
                onClick = { uriHandler.openUri("https://pixelcraftin.co.in/App%20Policy/calc-policy") }
            )

            SettingItem(
                title = "Open Source Libraries",
                subtitle = "Third-party dependency licenses",
                onClick = { navController.navigate(Screen.OpenSourceLibraries.route) }
            )

            // Section C: Feedback
            Text(
                text = "FEEDBACK",
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = if (isDoto) FontWeight.Bold else FontWeight.Medium,
                    letterSpacing = 1.sp
                ),
                color = GrayText,
                modifier = Modifier.padding(start = 20.dp, top = 24.dp, bottom = 8.dp)
            )

            SettingItem(
                title = "Report an Issue",
                subtitle = "Open feedback and report page",
                showDivider = false,
                onClick = {
                    uriHandler.openUri("https://github.com/pixelcraftin/CalcPlus/issues")
                }
            )

            Spacer(modifier = Modifier.height(32.dp))
            Text(
                text = "",
                style = MaterialTheme.typography.bodyMedium,
                color = GrayText.copy(alpha = 0.6f),
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.height(32.dp))
        }
    }

    // Theme Selector
    if (showThemeDialog) {
        OptionSelectionDialog(
            title = "Choose Theme",
            options = listOf("System", "Light", "Dark"),
            selectedOption = selectedTheme,
            onDismiss = { showThemeDialog = false },
            onSelect = {
                preferencesManager.theme = it
                selectedTheme = it
                showThemeDialog = false
                onThemeChange()
            }
        )
    }

    // Shape Selector
    if (showShapeDialog) {
        OptionSelectionDialog(
            title = "Choose Button Shape",
            options = listOf("Rounded Rectangle", "Circle"),
            selectedOption = selectedShape,
            onDismiss = { showShapeDialog = false },
            onSelect = {
                preferencesManager.buttonShape = it
                selectedShape = it
                showShapeDialog = false
                onShapeChange()
            }
        )
    }

    // Font Selector
    if (showFontDialog) {
        OptionSelectionDialog(
            title = "Choose Font Style",
            options = listOf(
                PreferencesManager.FONT_DOT_MATRIX,
                PreferencesManager.FONT_SYSTEM,
                PreferencesManager.FONT_INTER,
                PreferencesManager.FONT_ROBOTO_SLAB
            ),
            selectedOption = selectedFont,
            onDismiss = { showFontDialog = false },
            onSelect = {
                preferencesManager.fontStyle = it
                selectedFont = it
                showFontDialog = false
                onFontChange()
            }
        )
    }
        }
}

@Composable
fun OptionSelectionDialog(
    title: String,
    options: List<String>,
    selectedOption: String,
    onDismiss: () -> Unit,
    onSelect: (String) -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background),
            modifier = Modifier
                .fillMaxWidth()
                .border(0.5.dp, GrayText.copy(alpha = 0.3f), RoundedCornerShape(16.dp))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                val isDotoDialog = MaterialTheme.typography.titleLarge.fontFamily == com.pixelcraftin.calcplus.ui.theme.DotoFontFamily
                Text(
                    text = title.uppercase(),
                    style = MaterialTheme.typography.titleLarge.copy(fontSize = 16.sp, letterSpacing = 1.sp, fontWeight = if (isDotoDialog) FontWeight.Bold else FontWeight.Medium),
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                options.forEach { option ->
                    val isSelected = option == selectedOption
                    val bgColor = if (isSelected) MaterialTheme.colorScheme.secondary else Color.Transparent

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(bgColor)
                            .clickable { onSelect(option) }
                            .padding(horizontal = 12.dp, vertical = 14.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = when (option) {
                                PreferencesManager.FONT_SYSTEM -> "System"
                                PreferencesManager.FONT_DOT_MATRIX -> "Dot Matrix (Doto)"
                                PreferencesManager.FONT_INTER -> "Inter"
                                PreferencesManager.FONT_ROBOTO_SLAB -> "Roboto Slab"
                                else -> option
                            },
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontWeight = if (isSelected) (if (isDotoDialog) FontWeight.Bold else FontWeight.Medium) else FontWeight.Normal
                            )
                        )
                        if (isSelected) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(MaterialTheme.colorScheme.primary)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = onDismiss,
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.onBackground),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "CANCEL",
                        color = MaterialTheme.colorScheme.background,
                        fontWeight = if (isDotoDialog) FontWeight.Bold else FontWeight.Medium
                    )
                }
            }
        }
    }
}
