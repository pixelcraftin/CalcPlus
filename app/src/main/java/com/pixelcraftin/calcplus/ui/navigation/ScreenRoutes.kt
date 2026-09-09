package com.pixelcraftin.calcplus.ui.navigation

sealed class Screen(val route: String) {
    data object Main : Screen("main")
    data object Settings : Screen("settings")
    data object OpenSourceLibraries : Screen("open_source_libraries")
    data object About : Screen("about")
    
    // More Section Calculators
    data object PricePerKg : Screen("price_per_kg")
    data object AgeCalculator : Screen("age_calculator")
    data object EmiCalculator : Screen("emi_calculator")
    data object SplitterCalculator : Screen("splitter_calculator")
    data object BmiCalculator : Screen("bmi_calculator")
    data object DiscountCalculator : Screen("discount_calculator")
}
