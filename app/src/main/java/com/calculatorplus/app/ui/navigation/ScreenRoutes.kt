package com.calculatorplus.app.ui.navigation

sealed class Screen(val route: String) {
    data object Main : Screen("main")
    data object Settings : Screen("settings")
    data object OpenSourceLibraries : Screen("open_source_libraries")
    data object About : Screen("about")
    data object License : Screen("license")
    data object PrivacyPolicy : Screen("privacy_policy")
}
