package com.calculatorplus.app.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = DarkEquals,
    onPrimary = DarkCanvas,
    secondary = DarkNumberButton,
    onSecondary = DarkTextPrimary,
    background = DarkCanvas,
    surface = DarkOperatorButton,
    surfaceVariant = DarkNavContainer,
    onBackground = DarkTextPrimary,
    onSurface = DarkTextPrimary
)

private val LightColorScheme = lightColorScheme(
    primary = LightEquals,
    onPrimary = Color.White,
    secondary = LightNumberButton,
    onSecondary = LightTextPrimary,
    background = LightCanvas,
    surface = LightOperatorButton,
    onBackground = LightTextPrimary,
    onSurface = LightTextPrimary
)

@Composable
fun CalcPlusTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    useDotMatrix: Boolean = true,
    fontStyle: String = "System",
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    val typography = getTypography(useDotMatrix, fontStyle)

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val activity = view.context as? Activity
            if (activity != null) {
                val window = activity.window
                window.statusBarColor = colorScheme.background.toArgb()
                window.navigationBarColor = colorScheme.background.toArgb()
                val controller = WindowCompat.getInsetsController(window, view)
                controller.isAppearanceLightStatusBars = !darkTheme
                controller.isAppearanceLightNavigationBars = !darkTheme
            }
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = typography,
        content = content
    )
}

// Common / Legacy fallback color constants for project compatibility
val GrayText = Color(0xFF8E8E93)
val WhiteText = Color(0xFFFFFFFF)
val BorderColor = Color(0xFF1C1C1E)

