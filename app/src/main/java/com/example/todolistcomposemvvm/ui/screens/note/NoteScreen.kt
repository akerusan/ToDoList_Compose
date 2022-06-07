package com.example.todolistcomposemvvm.ui.screens.note

import android.content.Context
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.OnBackPressedDispatcher
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.material.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import com.example.todolistcomposemvvm.data.models.Priority
import com.example.todolistcomposemvvm.data.models.ToDoNote
import com.example.todolistcomposemvvm.ui.viewModels.SharedViewModel
import com.example.todolistcomposemvvm.util.Action

@Composable
fun NoteScreen(
    selectedNote: ToDoNote?,
    sharedViewModel: SharedViewModel,
    navigateToListScreen: (Action) -> Unit
) {
    val context = LocalContext.current

    val title: String by sharedViewModel.title
    val description: String by sharedViewModel.description
    val priority: Priority by sharedViewModel.priority

    HandleBackButton(onBackPressed = { navigateToListScreen(Action.NO_ACTION) } )

    Scaffold(
        topBar = {
                 NoteAppBar(
                     selectedNote = selectedNote,
                     navigateToListScreen = {
                         if (it == Action.NO_ACTION) {
                             navigateToListScreen(it)
                         } else {
                             if (sharedViewModel.validateFields()) {
                                 navigateToListScreen(it)
                             } else {
                                displayToastMessage(context)
                             }
                         }
                     }
                 )
        },
        content = {
            NoteContent(
                title = title,
                onTitleChange = {
                    sharedViewModel.updateTitle(it)
                },
                description = description,
                onDescriptionChange = {
                    sharedViewModel.description.value = it
                },
                priority = priority,
                onPrioritySelected = {
                    sharedViewModel.priority.value = it
                }
            )
        }
    )
}

fun displayToastMessage(context: Context) {
    Toast.makeText(context, "Fields are Empty", Toast.LENGTH_SHORT).show()
}

@Composable
fun HandleBackButton(
    backPressedDispatcher: OnBackPressedDispatcher? =
        LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher,
    onBackPressed: () -> Unit
) {
    val currentOnBackPressed by rememberUpdatedState(newValue = (onBackPressed))

    val callback = remember {
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                currentOnBackPressed()
            }
        }
    }

    DisposableEffect(key1 = backPressedDispatcher) {
        backPressedDispatcher?.addCallback(callback)

        onDispose {
            callback.remove()
        }
    }
}