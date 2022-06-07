package com.example.todolistcomposemvvm.navigation.destinations

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.google.accompanist.navigation.animation.composable
import com.example.todolistcomposemvvm.ui.screens.list.ListScreen
import com.example.todolistcomposemvvm.ui.viewModels.SharedViewModel
import com.example.todolistcomposemvvm.util.Action
import com.example.todolistcomposemvvm.util.Constants.LIST_ARGUMENT_KEY
import com.example.todolistcomposemvvm.util.Constants.LIST_SCREEN
import com.example.todolistcomposemvvm.util.toAction

@ExperimentalMaterialApi
@ExperimentalAnimationApi
fun NavGraphBuilder.listComposable(
    navigateToNoteScreen: (Int) -> Unit,
    sharedViewModel: SharedViewModel
) {
    composable(
        route = LIST_SCREEN,
        arguments = listOf(navArgument(LIST_ARGUMENT_KEY) { type = NavType.StringType })
    ) {
        val action: Action = it.arguments?.getString(LIST_ARGUMENT_KEY).toAction()
        
        LaunchedEffect(key1 = action) {
            sharedViewModel.action.value = action
        }

        ListScreen(navigateToNoteScreen, sharedViewModel)
    }
}