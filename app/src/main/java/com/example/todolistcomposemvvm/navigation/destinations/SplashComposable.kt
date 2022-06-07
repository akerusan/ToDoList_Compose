package com.example.todolistcomposemvvm.navigation.destinations

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideOutVertically
import androidx.navigation.NavGraphBuilder
import com.google.accompanist.navigation.animation.composable
import com.example.todolistcomposemvvm.ui.screens.splash.SplashScreen
import com.example.todolistcomposemvvm.util.Constants

@ExperimentalAnimationApi
fun NavGraphBuilder.splashComposable(
    navigateToListScreen: () -> Unit
) {
    composable(
        route = Constants.SPLASH_SCREEN,
        exitTransition = {
            slideOutVertically(
                targetOffsetY = { -it },
                animationSpec = tween(3000)
            )
        }
    ) {
        SplashScreen(navigateToListScreen)
    }
}