package com.example.todolistcomposemvvm.navigation.destinations

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.google.accompanist.navigation.animation.composable
import com.example.todolistcomposemvvm.ui.screens.note.NoteScreen
import com.example.todolistcomposemvvm.ui.viewModels.SharedViewModel
import com.example.todolistcomposemvvm.util.Action
import com.example.todolistcomposemvvm.util.Constants.NOTE_ARGUMENT_KEY
import com.example.todolistcomposemvvm.util.Constants.NOTE_SCREEN

@ExperimentalAnimationApi
fun NavGraphBuilder.noteComposable(
    navigateToListScreen: (Action) -> Unit,
    sharedViewModel: SharedViewModel
) {
    composable(
        route = NOTE_SCREEN,
        arguments = listOf(navArgument(NOTE_ARGUMENT_KEY) { type = NavType.IntType }),
        enterTransition = {
            slideInHorizontally(
                initialOffsetX = { -it } ,
                animationSpec = tween(600)
            )
        },
        exitTransition = {
            slideOutHorizontally(
                targetOffsetX = { -it },
                animationSpec = tween(600)
            )
        }
    ) {
        val noteId: Int = it.arguments!!.getInt(NOTE_ARGUMENT_KEY)
        sharedViewModel.getSelectedNote(noteId)

        val selectedNote by sharedViewModel.selectedNote.collectAsState()

        LaunchedEffect(key1 = selectedNote) {
            if (selectedNote != null || noteId == 1) {
                sharedViewModel.updateNoteFields(selectedNote)
            }
        }

        NoteScreen(
            selectedNote,
            sharedViewModel,
            navigateToListScreen
        )
    }
}