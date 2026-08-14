package com.calculatorplus.app.ui.theme

import androidx.compose.ui.graphics.Color

// ==========================================
// LIGHT MODE COLORS
// ==========================================
val LightCanvas = Color(0xFFFFFFFF)          // White Background
val LightDisplayBorder = Color(0xFFE5E5EA)   // Display Border Outline

// Keypad Button Colors
val LightNumberButton = Color(0xFFF0F0F2)    // Bright Off-White for Numbers (0-9, decimal)
val LightOperatorButton = Color(0xFFE5E5EA)  // Light Grey for Functions/Operators (+, -, ×, ÷)
val LightEquals = Color(0xFF111111)    // Equals Key
val RedAccent = Color(0xFFE12628)           // Vibrant Red for AC/Clear & Active Tab

// Navigation & Top Bar
val LightNavContainer = Color(0xFFEEEEEE)    // Top Navigation Pill Container
val LightNavIconBg = Color(0xFFEEEEEE)       // Gear/Option Icon Container

// Text Colors
val LightTextPrimary = Color(0xFF000000)     // Black Text / Glyphs
val LightTextSecondary = Color(0xFF5A5A5E)   // Inactive Tab Text Color

// ==========================================
// OLED DARK MODE COLORS
// ==========================================
val DarkCanvas = Color(0xFF000000)          // Pure OLED Black
val DarkDisplayBorder = Color(0xFF1C1C1E)

val DarkNumberButton = Color(0xFF141414)    // Dark Slate
val DarkOperatorButton = Color(0xFF222222)  // Medium Slate
val DarkEquals = Color(0xFFF5F5F5)   // Equals

val DarkNavContainer = Color(0xFF414141)
val DarkNavIconBg = Color(0xFF262626)

val DarkTextPrimary = Color(0xFFFFFFFF)     // White Glyphs
val DarkTextSecondary = Color(0xFF8E8E93)
