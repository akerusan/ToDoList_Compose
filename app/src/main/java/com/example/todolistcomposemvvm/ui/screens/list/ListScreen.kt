package com.example.todolistcomposemvvm.ui.screens.list

import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.example.todolistcomposemvvm.R
import com.example.todolistcomposemvvm.data.models.ToDoNote
import com.example.todolistcomposemvvm.ui.theme.fabBackgroundColor
import com.example.todolistcomposemvvm.ui.viewModels.SharedViewModel
import com.example.todolistcomposemvvm.util.Action
import kotlinx.coroutines.launch

@ExperimentalMaterialApi
@Composable
fun ListScreen(navigateToNoteScreen: (Int) -> Unit, sharedViewModel: SharedViewModel) {

    LaunchedEffect(key1 = true) {
        sharedViewModel.getAllNotes()
        sharedViewModel.readSortState()
    }

    val action by sharedViewModel.action

    val allNotes by sharedViewModel.allNotes.collectAsState()
    val searchedNotes by sharedViewModel.searchNotes.collectAsState()

    val sortState by sharedViewModel.sortState.collectAsState()
    val lowPriorityNote by sharedViewModel.lowPriorityNote.collectAsState()
    val highPriorityNote by sharedViewModel.highPriorityNote.collectAsState()


    val searchAppBarState by sharedViewModel.searchAppBarState
    val searchTextState by sharedViewModel.searchTextState

    val scaffoldState = rememberScaffoldState()

    DisplaySnackBar(
        scaffoldState = scaffoldState,
        handleDatabaseActions = { sharedViewModel.handleDatabaseActions(action) },
        onUndoClicked = { sharedViewModel.action.value = it },
        noteTitle = sharedViewModel.title.value,
        action = action
    )

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            ListAppBar(
                sharedViewModel,
                searchAppBarState,
                searchTextState
            )
        },
        content = {
            ListContent(
                allNotes = allNotes,
                searchedNotes = searchedNotes,
                lowPriorityNote = lowPriorityNote,
                highPriorityNote = highPriorityNote,
                sortState = sortState,
                searchAppBarState = searchAppBarState,
                onSwipeToDelete = { action, notes ->
                    sharedViewModel.action.value = action
                    sharedViewModel.updateNoteFields(notes)
                },
                navigateToNoteScreen = navigateToNoteScreen
            )
        },
        floatingActionButton = {
            ListFab(navigateToNoteScreen)
        }
    )
}

@Composable
fun ListFab(noteId: (Int) -> Unit) {
    FloatingActionButton(
        onClick = { noteId(-1) },
        backgroundColor = MaterialTheme.colors.fabBackgroundColor
    ) {
        Icon(
            imageVector = Icons.Filled.Add,
            contentDescription = stringResource(R.string.add_button),
            tint = Color.White
        )
    }
}

@Composable
fun DisplaySnackBar(
    scaffoldState: ScaffoldState,
    handleDatabaseActions: () -> Unit,
    onUndoClicked: (Action) -> Unit,
    noteTitle: String,
    action: Action
) {
    handleDatabaseActions()

    val scope = rememberCoroutineScope()
    LaunchedEffect(key1 = action) {
        if (action != Action.NO_ACTION) {
            scope.launch {
                val snackBarResult = scaffoldState.snackbarHostState.showSnackbar(
                    message = setSnackBarMessage(action, noteTitle),
                    actionLabel = setActionLabel(action)
                )
                undoDeleteNote(action, snackBarResult, onUndoClicked)
            }
        }
    }
}

private fun setSnackBarMessage(action: Action, noteTitle: String): String {
    return when(action) {
        Action.DELETE_ALL -> "All Notes Removed"
        else -> "${action.name}: $noteTitle"
    }
}

private fun setActionLabel(action: Action): String {
    return if (action.name == "DELETE") {
        "UNDO"
    } else {
        "OK"
    }
}

private fun undoDeleteNote(
    action: Action,
    snackBarResult: SnackbarResult,
    onUndoClicked: (Action) -> Unit
) {
    if (snackBarResult == SnackbarResult.ActionPerformed && action == Action.DELETE) {
        onUndoClicked(Action.UNDO)
    }
}