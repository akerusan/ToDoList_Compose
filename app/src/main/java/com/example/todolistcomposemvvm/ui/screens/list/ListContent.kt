package com.example.todolistcomposemvvm.ui.screens.list

import android.annotation.SuppressLint
import androidx.compose.animation.*
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.todolistcomposemvvm.R
import com.example.todolistcomposemvvm.data.models.Priority
import com.example.todolistcomposemvvm.data.models.ToDoNote
import com.example.todolistcomposemvvm.ui.theme.*
import com.example.todolistcomposemvvm.util.Action
import com.example.todolistcomposemvvm.util.RequestState
import com.example.todolistcomposemvvm.util.SearchAppBarState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
@ExperimentalMaterialApi
fun ListContent(
    allNotes: RequestState<List<ToDoNote>>,
    searchedNotes: RequestState<List<ToDoNote>>,
    lowPriorityNote: List<ToDoNote>,
    highPriorityNote: List<ToDoNote>,
    sortState: RequestState<Priority>,
    searchAppBarState: SearchAppBarState,
    onSwipeToDelete: (Action, ToDoNote) -> Unit,
    navigateToNoteScreen: (noteId: Int) -> Unit
) {
    if (sortState is RequestState.Success) {
        when {
            searchAppBarState == SearchAppBarState.TRIGGERED -> {
                if (searchAppBarState == SearchAppBarState.TRIGGERED) {
                    if (searchedNotes is RequestState.Success) {
                        HandleListContent(
                            notes = searchedNotes.data,
                            onSwipeToDelete = onSwipeToDelete,
                            navigateToNoteScreen = navigateToNoteScreen
                        )
                    }
                }
            }
            sortState.data == Priority.NONE -> {
                if (allNotes is RequestState.Success) {
                    HandleListContent(
                        notes = allNotes.data,
                        onSwipeToDelete = onSwipeToDelete,
                        navigateToNoteScreen = navigateToNoteScreen
                    )
                }
            }
            sortState.data == Priority.LOW -> {
                if (allNotes is RequestState.Success) {
                    HandleListContent(
                        notes = lowPriorityNote,
                        onSwipeToDelete = onSwipeToDelete,
                        navigateToNoteScreen = navigateToNoteScreen
                    )
                }
            }
            sortState.data == Priority.HIGH -> {
                if (allNotes is RequestState.Success) {
                    HandleListContent(
                        notes = highPriorityNote,
                        onSwipeToDelete = onSwipeToDelete,
                        navigateToNoteScreen = navigateToNoteScreen
                    )
                }
            }
        }
    }
}

@Composable
@ExperimentalMaterialApi
fun HandleListContent(
    notes: List<ToDoNote>,
    onSwipeToDelete: (Action, ToDoNote) -> Unit,
    navigateToNoteScreen: (noteId: Int) -> Unit
) {
    if (notes.isEmpty()) {
        EmptyContent()
    } else {
        DisplayNotes(notes, onSwipeToDelete, navigateToNoteScreen)
    }
}

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
@ExperimentalMaterialApi
fun DisplayNotes(
    notes: List<ToDoNote>,
    onSwipeToDelete: (Action, ToDoNote) -> Unit,
    navigateToNoteScreen: (noteId: Int) -> Unit
) {
    LazyColumn {
        items(
            items = notes,
            key = { note -> note.id }
        )
        { note ->
            val dismissState = rememberDismissState()
            val dismissDirection = dismissState.dismissDirection
            val isDismissedToDelete = dismissState.isDismissed(DismissDirection.EndToStart)
            val isDismissedToEdit = dismissState.isDismissed(DismissDirection.StartToEnd)

            if (isDismissedToDelete && dismissDirection == DismissDirection.EndToStart) {
                val scope = rememberCoroutineScope()
                scope.launch {
                    delay(300)
                    onSwipeToDelete(Action.DELETE, note)
                }
            } else if (isDismissedToEdit && dismissDirection == DismissDirection.StartToEnd) {
                navigateToNoteScreen(note.id)
            }

            val degrees by animateFloatAsState(
                targetValue = when (dismissState.targetValue) {
                    DismissValue.Default -> 0f
                    DismissValue.DismissedToEnd -> -90f
                    DismissValue.DismissedToStart -> -45f
                }
            )

            var itemAppeared by remember { mutableStateOf(false) }
            LaunchedEffect(key1 = true) {
                itemAppeared = true
            }

            AnimatedVisibility(
                visible = itemAppeared && !isDismissedToDelete && !isDismissedToEdit,
                enter = expandVertically(
                    animationSpec = tween(
                        durationMillis = 300
                    )
                ),
                exit = shrinkVertically(
                    animationSpec = tween(
                        durationMillis = 300
                    )
                )
            ) {
                SwipeToDismiss(
                    state = dismissState,
                    directions = setOf(DismissDirection.EndToStart, DismissDirection.StartToEnd),
                    dismissThresholds = { FractionalThreshold(fraction = 0.3f) },
                    background = {
                        when (dismissDirection) {
                            DismissDirection.StartToEnd -> EditBackground(degrees = degrees)
                            DismissDirection.EndToStart -> DeleteBackground(degrees = degrees)
                            null -> Color.Transparent
                        }
                    },
                    dismissContent = {
                        NoteItem(toDoNote = note, navigateToNoteScreen = navigateToNoteScreen)
                    }
                )
            }
        }
    }
}

@Composable
fun DeleteBackground(degrees: Float) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Red)
            .padding(horizontal = LARGE_PADDING),
        contentAlignment = Alignment.CenterEnd)
    {
        Row {
            Text(
                text = "DELETE",
                Modifier.padding(end = 10.dp),
                fontSize = 18.sp,
                color = Color.White,
                fontWeight = FontWeight.Bold)
            Icon(
                modifier = Modifier.rotate(degrees),
                imageVector = Icons.Filled.Delete,
                contentDescription = stringResource(R.string.delete_icon),
                tint = Color.White)
        }
    }
}
@Composable
fun EditBackground(degrees: Float) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Blue)
            .padding(horizontal = LARGE_PADDING),
        contentAlignment = Alignment.CenterStart)
    {
        Row {
            Icon(
                modifier = Modifier.rotate(degrees),
                imageVector = Icons.Filled.Edit,
                contentDescription = stringResource(R.string.edit_icon),
                tint = Color.White)
            Text(
                text = "Edit",
                Modifier.padding(start = 10.dp),
                fontSize = 18.sp,
                color = Color.White,
                fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
@ExperimentalMaterialApi
fun NoteItem(
    toDoNote: ToDoNote,
    navigateToNoteScreen: (noteId: Int) -> Unit
) {
    var expanded by remember{mutableStateOf(false)}

    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colors.noteItemBackgroundColor,
        shape = RectangleShape,
        elevation = NOTE_ITEM_ELEVATION,
        onClick = {
            navigateToNoteScreen(toDoNote.id)
        }
    ) {
        Card {
            Column(
                modifier = Modifier
                    .padding(all = LARGE_PADDING)
                    .fillMaxWidth()
            ) {
                Row {
                    Text(
                        modifier = Modifier.weight(8f),
                        text = toDoNote.title,
                        color = MaterialTheme.colors.noteItemTextColor,
                        style = MaterialTheme.typography.h5,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        contentAlignment = Alignment.TopEnd
                    ) {
                        Canvas(
                            modifier = Modifier.size(PRIORITY_INDICATOR)
                        ) {
                            drawCircle(color = toDoNote.priority.color)
                        }
                    }
                }
                Row {
                    if (expanded) {
                        Text(
                            text = toDoNote.description,
                            color = MaterialTheme.colors.noteItemTextColor,
                            style = MaterialTheme.typography.subtitle1
                        )
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.TopEnd
                        ) {
                            IconButton(
                                modifier = Modifier.size(18.dp),
                                onClick = { expanded = false}
                            ) {
                                Icon(Icons.Filled.KeyboardArrowUp, contentDescription = "collapse")
                            }
                        }
                    } else {
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.TopEnd
                        ) {
                            IconButton(
                                modifier = Modifier.size(18.dp),
                                onClick = { expanded = true },
                            ) {
                                Icon(Icons.Filled.KeyboardArrowDown, contentDescription = "Expanded")
                            }
                        }
                    }
                }
            }
        }

    }
}

//@Composable
//@ExperimentalMaterialApi
//fun NoteItem2(
//    toDoNote: ToDoNote,
//    navigateToNoteScreen: (noteId: Int) -> Unit
//) {
//    var expanded by remember{mutableStateOf(false)}
//    val rotationState by animateFloatAsState(targetValue = if (expanded) 180f else 0f)
//
//    Card(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(4.dp)
//            .animateContentSize(
//                animationSpec = tween(300, easing = LinearOutSlowInEasing)
//            ),
//        shape = (Shapes.medium),
//        onClick = {
//            expanded = !expanded
//            navigateToNoteScreen(toDoNote.id)
//        }
//    ) {
//        Column(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(LARGE_PADDING)
//        ) {
//            Row(
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//                Text(
//                    modifier = Modifier.weight(6f),
//                    text = toDoNote.title,
//                    fontSize = MaterialTheme.typography.h6.fontSize,
//                    fontWeight = FontWeight.Bold,
//                    maxLines = 1,
//                    overflow = TextOverflow.Ellipsis
//                )
//                IconButton(
//                    modifier = Modifier
//                        .alpha(ContentAlpha.medium)
//                        .weight(1f)
//                        .rotate(rotationState),
//                    onClick = { expanded = !expanded },
//                ) {
//                    Icon(Icons.Filled.KeyboardArrowDown, contentDescription = "Expanded")
//                }
//            }
//            if (expanded) {
//                Text(
//                    text = toDoNote.description,
//                    color = MaterialTheme.colors.noteItemTextColor,
//                    style = MaterialTheme.typography.subtitle1
//                )
//            }
//        }
//    }
//}

@Preview
@Composable
@ExperimentalMaterialApi
fun PreviewNoteItem() {
    NoteItem(
        toDoNote = ToDoNote(
            id = 0,
            title = "Title",
            description = "Some random text",
            priority = Priority.MEDIUM
        ),
        navigateToNoteScreen = {}
    )
}

//@Preview
//@Composable
//@ExperimentalMaterialApi
//fun PreviewNoteItem2() {
//    NoteItem2(
//        toDoNote = ToDoNote(
//            id = 0,
//            title = "Title",
//            description = "Some random text",
//            priority = Priority.MEDIUM
//        ),
//        navigateToNoteScreen = {}
//    )
//}