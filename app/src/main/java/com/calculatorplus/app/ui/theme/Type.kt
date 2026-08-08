package com.calculatorplus.app.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontSynthesis
import androidx.compose.ui.unit.sp
import com.calculatorplus.app.R

val DotoFontFamily = try {
    FontFamily(
        Font(R.font.doto, FontWeight.Normal),
        Font(R.font.doto, FontWeight.Medium),
        Font(R.font.doto, FontWeight.SemiBold),
        Font(R.font.doto, FontWeight.Bold)
    )
} catch (e: Throwable) {
    FontFamily.Default
}

val PlusJakartaSansFontFamily = try {
    FontFamily(
        Font(R.font.plus_jakarta_sans, FontWeight.Normal),
        Font(R.font.plus_jakarta_sans_bold, FontWeight.Bold)
    )
} catch (e: Throwable) {
    FontFamily.Default
}

val RobotoSlabFontFamily = try {
    FontFamily(
        Font(R.font.roboto_slab_regular, FontWeight.Normal)
    )
} catch (e: Throwable) {
    FontFamily.Default
}

val OpenSansCondensedFontFamily = try {
    FontFamily(
        Font(R.font.open_sans_condensed_regular, FontWeight.Normal)
    )
} catch (e: Throwable) {
    FontFamily.Default
}

fun getTypography(useDotMatrix: Boolean, fontStyle: String = "System"): Typography {
    val fontFamily = when {
        useDotMatrix || fontStyle == "Dot Matrix" -> DotoFontFamily
        fontStyle == "Roboto Slab" -> RobotoSlabFontFamily
        fontStyle == "Open Sans Condensed" -> OpenSansCondensedFontFamily
        else -> PlusJakartaSansFontFamily
    }
    val fontSynthesis = when {
        useDotMatrix || fontStyle == "Dot Matrix" -> FontSynthesis.None
        fontStyle == "Roboto Slab" -> FontSynthesis.None
        fontStyle == "Open Sans Condensed" -> FontSynthesis.None
        else -> FontSynthesis.All
    }
    
    return Typography(
        displayLarge = TextStyle(
            fontFamily = fontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 48.sp,
            lineHeight = 56.sp,
            letterSpacing = (-0.25).sp,
            fontSynthesis = fontSynthesis
        ),
        displayMedium = TextStyle(
            fontFamily = fontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 36.sp,
            lineHeight = 44.sp,
            letterSpacing = 0.sp,
            fontSynthesis = fontSynthesis
        ),
        displaySmall = TextStyle(
            fontFamily = fontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 28.sp,
            lineHeight = 36.sp,
            letterSpacing = 0.sp,
            fontSynthesis = fontSynthesis
        ),
        headlineMedium = TextStyle(
            fontFamily = fontFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 22.sp,
            lineHeight = 28.sp,
            letterSpacing = 0.sp,
            fontSynthesis = fontSynthesis
        ),
        titleLarge = TextStyle(
            fontFamily = fontFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = 20.sp,
            lineHeight = 26.sp,
            letterSpacing = 0.sp,
            fontSynthesis = fontSynthesis
        ),
        bodyLarge = TextStyle(
            fontFamily = fontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp,
            lineHeight = 24.sp,
            letterSpacing = 0.5.sp,
            fontSynthesis = fontSynthesis
        ),
        bodyMedium = TextStyle(
            fontFamily = fontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp,
            lineHeight = 20.sp,
            letterSpacing = 0.25.sp,
            fontSynthesis = fontSynthesis
        ),
        labelLarge = TextStyle(
            fontFamily = fontFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp,
            lineHeight = 20.sp,
            letterSpacing = 0.1.sp,
            fontSynthesis = fontSynthesis
        ),
        labelMedium = TextStyle(
            fontFamily = fontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 12.sp,
            lineHeight = 16.sp,
            letterSpacing = 0.5.sp,
            fontSynthesis = fontSynthesis
        )
    )
}
