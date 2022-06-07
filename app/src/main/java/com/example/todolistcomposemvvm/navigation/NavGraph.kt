package com.example.todolistcomposemvvm.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import com.example.todolistcomposemvvm.navigation.destinations.listComposable
import com.example.todolistcomposemvvm.navigation.destinations.noteComposable
import com.example.todolistcomposemvvm.navigation.destinations.splashComposable
import com.example.todolistcomposemvvm.ui.viewModels.SharedViewModel
import com.example.todolistcomposemvvm.util.Constants.SPLASH_SCREEN
import com.google.accompanist.navigation.animation.AnimatedNavHost

@Composable
@ExperimentalMaterialApi
@ExperimentalAnimationApi
fun SetUpNavGraph(navController: NavHostController, sharedViewModel: SharedViewModel) {

    val screen = remember(navController) { Screens(navController) }

    AnimatedNavHost(
        navController = navController,
        startDestination = SPLASH_SCREEN
    ) {
        splashComposable(
            navigateToListScreen = screen.splash
        )
        listComposable(
            navigateToNoteScreen = screen.list,
            sharedViewModel = sharedViewModel
        )
        noteComposable(
            navigateToListScreen = screen.note,
            sharedViewModel = sharedViewModel
        )
    }
}