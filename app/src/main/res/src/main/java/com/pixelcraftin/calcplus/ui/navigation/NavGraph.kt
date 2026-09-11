package com.pixelcraftin.calcplus.ui.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.pixelcraftin.calcplus.data.util.PreferencesManager
import com.pixelcraftin.calcplus.ui.screens.*

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
                towards = AnimatedContentTransitionScope.SlideDirection.Left,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioNoBouncy,
                    stiffness = Spring.StiffnessMedium
                )
            )
        },
        exitTransition = {
            slideOutOfContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Left,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioNoBouncy,
                    stiffness = Spring.StiffnessMedium
                )
            )
        },
        popEnterTransition = {
            slideIntoContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Right,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioNoBouncy,
                    stiffness = Spring.StiffnessMedium
                )
            )
        },
        popExitTransition = {
            slideOutOfContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Right,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioNoBouncy,
                    stiffness = Spring.StiffnessMedium
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
        
        // Calculators opened in new window / screen
        composable(Screen.PricePerKg.route) {
            PricePerKgScreen(navController = navController)
        }
        composable(Screen.AgeCalculator.route) {
            AgeCalculatorScreen(navController = navController)
        }
        composable(Screen.EmiCalculator.route) {
            EmiCalculatorScreen(navController = navController)
        }
        composable(Screen.SplitterCalculator.route) {
            SplitterCalculatorScreen(navController = navController)
        }
        composable(Screen.BmiCalculator.route) {
            BmiCalculatorScreen(navController = navController)
        }
        composable(Screen.DiscountCalculator.route) {
            DiscountCalculatorScreen(navController = navController)
        }
    }
}
