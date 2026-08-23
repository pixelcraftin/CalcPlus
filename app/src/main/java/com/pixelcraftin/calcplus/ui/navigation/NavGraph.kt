package com.pixelcraftin.calcplus.ui.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.pixelcraftin.calcplus.data.util.PreferencesManager
import com.pixelcraftin.calcplus.ui.screens.AboutScreen
import com.pixelcraftin.calcplus.ui.screens.MainScreen
import com.pixelcraftin.calcplus.ui.screens.OpenSourceLibrariesScreen
import com.pixelcraftin.calcplus.ui.screens.SettingsScreen

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
    }
}
