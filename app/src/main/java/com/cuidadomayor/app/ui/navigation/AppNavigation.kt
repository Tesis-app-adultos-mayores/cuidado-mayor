package com.cuidadomayor.app.ui.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.cuidadomayor.app.ui.auth.AuthViewModel
import com.cuidadomayor.app.ui.screens.LoginScreen
import com.cuidadomayor.app.ui.screens.MainDashboardScreen

sealed class Route(val path: String) {
    object Auth : Route("auth")
    object Dashboard : Route("dashboard")
}

@Composable
fun AppNavigation(
    authViewModel: AuthViewModel,
    navController: NavHostController = rememberNavController()
) {
    val currentUser by authViewModel.currentUser.collectAsState()

    // Redirect logic based on auth state
    LaunchedEffect(currentUser) {
        if (currentUser == null) {
            navController.navigate(Route.Auth.path) {
                popUpTo(0) { inclusive = true } // Clear backstack completely
                launchSingleTop = true
            }
        } else {
            navController.navigate(Route.Dashboard.path) {
                popUpTo(0) { inclusive = true } // Clear backstack completely
                launchSingleTop = true
            }
        }
    }

    NavHost(
        navController = navController,
        startDestination = if (currentUser == null) Route.Auth.path else Route.Dashboard.path,
        modifier = Modifier.fillMaxSize()
    ) {
        composable(
            route = Route.Auth.path,
            enterTransition = {
                slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Up, animationSpec = tween(500))
            },
            exitTransition = {
                slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Down, animationSpec = tween(500))
            }
        ) {
            LoginScreen(authViewModel = authViewModel)
        }

        composable(
            route = Route.Dashboard.path,
            enterTransition = {
                slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Left, animationSpec = tween(500))
            },
            exitTransition = {
                slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Right, animationSpec = tween(500))
            }
        ) {
            // Protección contra null (Evita el NullPointerException si Compose evalúa esta ruta rápido antes del LaunchedEffect)
            if (currentUser != null) {
                MainDashboardScreen(
                    authViewModel = authViewModel,
                    currentUser = currentUser!!
                )
            }
        }
    }
}
