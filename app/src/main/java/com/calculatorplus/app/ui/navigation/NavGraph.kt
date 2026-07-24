package com.calculatorplus.app.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.calculatorplus.app.ui.screens.*
import com.calculatorplus.app.data.util.PreferencesManager

@Composable
fun NavGraph(
    navController: NavHostController,
    preferencesManager: PreferencesManager,
    onThemeChange: () -> Unit,
    onShapeChange: () -> Unit,
    onFontChange: () -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Main.route,
        enterTransition = {
            slideIntoContainer(
                towards = androidx.compose.animation.AnimatedContentTransitionScope.SlideDirection.Left,
                animationSpec = androidx.compose.animation.core.spring(
                    dampingRatio = androidx.compose.animation.core.Spring.DampingRatioNoBouncy,
                    stiffness = androidx.compose.animation.core.Spring.StiffnessMedium
                )
            )
        },
        exitTransition = {
            slideOutOfContainer(
                towards = androidx.compose.animation.AnimatedContentTransitionScope.SlideDirection.Left,
                animationSpec = androidx.compose.animation.core.spring(
                    dampingRatio = androidx.compose.animation.core.Spring.DampingRatioNoBouncy,
                    stiffness = androidx.compose.animation.core.Spring.StiffnessMedium
                )
            )
        },
        popEnterTransition = {
            slideIntoContainer(
                towards = androidx.compose.animation.AnimatedContentTransitionScope.SlideDirection.Right,
                animationSpec = androidx.compose.animation.core.spring(
                    dampingRatio = androidx.compose.animation.core.Spring.DampingRatioNoBouncy,
                    stiffness = androidx.compose.animation.core.Spring.StiffnessMedium
                )
            )
        },
        popExitTransition = {
            slideOutOfContainer(
                towards = androidx.compose.animation.AnimatedContentTransitionScope.SlideDirection.Right,
                animationSpec = androidx.compose.animation.core.spring(
                    dampingRatio = androidx.compose.animation.core.Spring.DampingRatioNoBouncy,
                    stiffness = androidx.compose.animation.core.Spring.StiffnessMedium
                )
            )
        }
    ) {
        composable(Screen.Main.route) {
            MainScreen(
                navController = navController,
                preferencesManager = preferencesManager
            )
        }
        composable(Screen.Settings.route) {
            SettingsScreen(
                navController = navController,
                preferencesManager = preferencesManager,
                onThemeChange = onThemeChange,
                onShapeChange = onShapeChange,
                onFontChange = onFontChange
            )
        }
        composable(Screen.OpenSourceLibraries.route) {
            OpenSourceLibrariesScreen(navController = navController)
        }
        composable(Screen.About.route) {
            AboutScreen(navController = navController)
        }
        composable(Screen.License.route) {
            LicenseScreen(navController = navController)
        }
        composable(Screen.PrivacyPolicy.route) {
            PrivacyPolicyScreen(navController = navController)
        }
    }
}
