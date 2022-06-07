package com.example.todolistcomposemvvm.ui.screens.note

import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.*
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.example.todolistcomposemvvm.R
import com.example.todolistcomposemvvm.components.DisplayAlertDialog
import com.example.todolistcomposemvvm.data.models.Priority
import com.example.todolistcomposemvvm.data.models.ToDoNote
import com.example.todolistcomposemvvm.ui.theme.topAppBarBackgroundColor
import com.example.todolistcomposemvvm.ui.theme.topAppBarContentColor
import com.example.todolistcomposemvvm.util.Action

@Composable
fun NoteAppBar(
    selectedNote: ToDoNote?,
    navigateToListScreen: (Action) -> Unit
) {
    if (selectedNote == null) {
        NewNoteAppBar(navigateToListScreen)
    } else {
        EditNoteAppBar(selectedNote, navigateToListScreen)
    }
}

@Composable
fun NewNoteAppBar(
    navigateToListScreen: (Action) -> Unit
) {
    TopAppBar(
        navigationIcon = {
            BackAction(onBackClicked = navigateToListScreen)
        },
        title = {
            Text(
                text = stringResource(R.string.add_note),
                color = MaterialTheme.colors.topAppBarContentColor)
        },
        backgroundColor = MaterialTheme.colors.topAppBarBackgroundColor,
        actions = {
            AddAction(onAddClicked = navigateToListScreen)
        }
    )
}

@Composable
fun BackAction(
    onBackClicked: (Action) -> Unit
) {
    IconButton(
        onClick = { onBackClicked(Action.NO_ACTION) }
    ) {
        Icon(
            imageVector = Icons.Filled.ArrowBack,
            contentDescription = stringResource(R.string.back_arrow),
            tint = MaterialTheme.colors.topAppBarContentColor
        )
    }
}

@Composable
fun AddAction(
    onAddClicked: (Action) -> Unit
) {
    IconButton(
        onClick = { onAddClicked(Action.ADD) }
    ) {
        Icon(
            imageVector = Icons.Filled.Check,
            contentDescription = stringResource(R.string.add_note),
            tint = MaterialTheme.colors.topAppBarContentColor
        )
    }
}

@Composable
fun CloseAction(
    onCloseClicked: (Action) -> Unit
) {
    IconButton(
        onClick = { onCloseClicked(Action.NO_ACTION) }
    ) {
        Icon(
            imageVector = Icons.Filled.Close,
            contentDescription = stringResource(R.string.close_icon),
            tint = MaterialTheme.colors.topAppBarContentColor
        )
    }
}

@Composable
fun DeleteAction(
    onDeleteClicked: () -> Unit
) {
    IconButton(
        onClick = { onDeleteClicked() }
    ) {
        Icon(
            imageVector = Icons.Filled.Delete,
            contentDescription = stringResource(R.string.delete_icon),
            tint = MaterialTheme.colors.topAppBarContentColor
        )
    }
}

@Composable
fun UpdateAction(
    onUpdateClicked: (Action) -> Unit
) {
    IconButton(
        onClick = { onUpdateClicked(Action.UPDATE) }
    ) {
        Icon(
            imageVector = Icons.Filled.Check,
            contentDescription = stringResource(R.string.update_icon),
            tint = MaterialTheme.colors.topAppBarContentColor
        )
    }
}

@Composable
fun EditNoteAppBar(
    selectedNote: ToDoNote,
    navigateToListScreen: (Action) -> Unit
) {
    TopAppBar(
        navigationIcon = {
            CloseAction(onCloseClicked = navigateToListScreen)
        },
        title = {
            Text(
                text = stringResource(R.string.edit_note),
                color = MaterialTheme.colors.topAppBarContentColor,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        backgroundColor = MaterialTheme.colors.topAppBarBackgroundColor,
        actions = {
            EditNoteAppBarActions(
                selectedNote, navigateToListScreen
            )
        }
    )
}

@Composable
fun EditNoteAppBarActions(
    selectedNote: ToDoNote,
    navigateToListScreen: (Action) -> Unit
) {
    var openDialog by remember { mutableStateOf(false) }

    DisplayAlertDialog(
        title = stringResource(R.string.delete_note, selectedNote.title),
        message = stringResource(R.string.delete_note_confirm, selectedNote.title),
        openDialog = openDialog,
        closeDialog = { openDialog = false },
        onYesClicked = { navigateToListScreen(Action.DELETE) }
    )

    DeleteAction(onDeleteClicked = { openDialog = true} )

    UpdateAction(navigateToListScreen)
}

@Preview
@Composable
fun PreviewNewNoteAppBar() {
    NewNoteAppBar(
        navigateToListScreen = {}
    )
}

@Preview
@Composable
fun PreviewEditNoteAppBar() {
    EditNoteAppBar(
        selectedNote = ToDoNote(0, "Test Title", "test description", Priority.MEDIUM),
        navigateToListScreen = {}
    )
}