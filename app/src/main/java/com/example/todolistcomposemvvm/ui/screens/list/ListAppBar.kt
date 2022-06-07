package com.example.todolistcomposemvvm.ui.screens.list

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.todolistcomposemvvm.R
import com.example.todolistcomposemvvm.data.models.Priority
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import com.example.todolistcomposemvvm.components.DisplayAlertDialog
import com.example.todolistcomposemvvm.components.PriorityItem
import com.example.todolistcomposemvvm.ui.theme.*
import com.example.todolistcomposemvvm.ui.viewModels.SharedViewModel
import com.example.todolistcomposemvvm.util.Action
import com.example.todolistcomposemvvm.util.SearchAppBarState
import com.example.todolistcomposemvvm.util.SearchAppBarState.CLOSED
import com.example.todolistcomposemvvm.util.SearchAppBarState.OPENED

@Composable
fun ListAppBar(
    sharedViewModel: SharedViewModel,
    searchAppBarState: SearchAppBarState,
    searchTexState: String
) {
    when(searchAppBarState) {
        CLOSED -> {
            DefaultListAppBar(
                onSearchClicked = {
                    sharedViewModel.searchAppBarState.value = OPENED
                },
                onSortClicked = {
                    sharedViewModel.persistSortingState(priority = it)
                },
                onDeleteAllConfirmed = {
                    sharedViewModel.action.value = Action.DELETE_ALL
                }
            )
        }
        else -> {
            SearchAppBar(
                text = searchTexState,
                onTextChanged = { newText ->
                    sharedViewModel.searchTextState.value = newText
                },
                onCloseClicked = {
                    sharedViewModel.searchAppBarState.value = CLOSED
                    sharedViewModel.searchTextState.value = ""
                },
                onSearchClicked = { searchText ->
                    sharedViewModel.searchDatabase(searchText)
                }
            )
        }
    }
}

@Composable
fun DefaultListAppBar(
    onSearchClicked: () -> Unit,
    onSortClicked: (Priority) -> Unit,
    onDeleteAllConfirmed: () -> Unit
) {
    TopAppBar(
        title = {
            Text(
                text = stringResource(R.string.top_bar_title),
                color = MaterialTheme.colors.topAppBarContentColor)
        },
        actions = {
            ListAppBarActions(
                onSearchClicked,
                onSortClicked,
                onDeleteAllConfirmed)
        },
        backgroundColor = MaterialTheme.colors.topAppBarBackgroundColor
    )
}

@Composable
fun ListAppBarActions(
    onSearchClicked: () -> Unit,
    onSortClicked: (Priority) -> Unit,
    onDeleteAllConfirmed: () -> Unit
) {
    var openDialog by remember { mutableStateOf(false) }

    DisplayAlertDialog(
        title = stringResource(R.string.delete_all_notes),
        message = stringResource(R.string.delete_all_notes_confirm),
        openDialog = openDialog,
        closeDialog = { openDialog = false },
        onYesClicked = { onDeleteAllConfirmed() }
    )
    
    SearchAction(onSearchClicked)
    SortAction(onSortClicked)
    DeleteAllAction { openDialog = true }
}

@Composable
fun SearchAction(onSearchClicked: () -> Unit) {
    IconButton(
        onClick = {
            onSearchClicked()
        }
    ) {
        Icon(
            imageVector = Icons.Filled.Search,
            contentDescription = stringResource(R.string.search_notes),
            tint = MaterialTheme.colors.topAppBarContentColor
        )
    }
}

@Composable
fun SortAction(onSortClicked: (Priority) -> Unit) {

    var expanded by remember { mutableStateOf(false) }

    IconButton(onClick = {expanded = true}) {
        Icon(
            painter = painterResource(R.drawable.ic_filter_list),
            contentDescription = stringResource(R.string.sort_action),
            tint = MaterialTheme.colors.topAppBarContentColor
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(
                onClick = {
                    expanded = false
                    onSortClicked(Priority.LOW)
                }
            ) {
                PriorityItem(priority = Priority.LOW)
            }
            DropdownMenuItem(
                onClick = {
                    expanded = false
                    onSortClicked(Priority.HIGH)
                }
            ) {
                PriorityItem(priority = Priority.HIGH)
            }
            DropdownMenuItem(
                onClick = {
                    expanded = false
                    onSortClicked(Priority.NONE)
                }
            ) {
                PriorityItem(priority = Priority.NONE)
            }
        }
    }
}

@Composable
fun DeleteAllAction(
    onDeleteAllConfirmed: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    IconButton(onClick = {expanded = true}) {
        Icon(
            painter = painterResource(R.drawable.ic_vertical_menu),
            contentDescription = stringResource(R.string.delete_all_action),
            tint = MaterialTheme.colors.topAppBarContentColor
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(
                onClick = {
                    expanded = false
                    onDeleteAllConfirmed()
                }
            ) {
                Text(
                    modifier = Modifier.padding(start = LARGE_PADDING),
                    text = stringResource(R.string.delete_all_action),
                    style = Typography.subtitle2
                )
            }
        }
    }
}

@Composable
fun SearchAppBar(
    text: String,
    onTextChanged: (String) -> Unit,
    onCloseClicked: () -> Unit,
    onSearchClicked: (String) -> Unit
) {

    var isReadyToClose = false

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(TOP_APP_BAR_HEIGHT),
        elevation = AppBarDefaults.TopAppBarElevation,
        color = MaterialTheme.colors.topAppBarBackgroundColor
    ) {
        TextField(
            value = text,
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            onValueChange = { onTextChanged(it) },
            placeholder = {
                Text(
                    text = stringResource(R.string.search_bar_placeholder),
                    color = Color.White,
                    modifier = Modifier.alpha(ContentAlpha.disabled)
                )},
            textStyle = TextStyle(
                color = MaterialTheme.colors.topAppBarContentColor,
                fontSize = MaterialTheme.typography.subtitle1.fontSize
            ),
            leadingIcon = {
                IconButton(
                    modifier = Modifier.alpha(ContentAlpha.disabled),
                    onClick = {}
                ) {
                    Icon(
                        imageVector = Icons.Filled.Search,
                        contentDescription = stringResource(R.string.search_bar_search_icon),
                        tint = MaterialTheme.colors.topAppBarContentColor)
                }
            },
            trailingIcon = {
                IconButton(
                    onClick = {
                        when(isReadyToClose) {
                            false -> {
                                if (text.isEmpty()) {
                                    onCloseClicked()
                                } else {
                                    onTextChanged("")
                                    isReadyToClose = true
                                }
                            }
                            true -> {
                                if (text.isNotEmpty()) {
                                    onTextChanged("")
                                } else {
                                    onCloseClicked()
                                    isReadyToClose = false
                                }
                            }
                        }
                    }
                ) {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = stringResource(R.string.search_bar_close_icon),
                        tint = MaterialTheme.colors.topAppBarContentColor)
                }
            },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Search
            ),
            keyboardActions = KeyboardActions(
                onSearch = {
                    onSearchClicked(text)
                }
            ),
            colors = TextFieldDefaults.textFieldColors(
                cursorColor = MaterialTheme.colors.topAppBarContentColor,
                focusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                backgroundColor = Color.Transparent
            )
        )
    }
}

@Composable
@Preview(name = "Light Theme", uiMode = UI_MODE_NIGHT_YES)
fun PreviewDefaultListAppBar() {
    DefaultListAppBar(
        onSearchClicked = {},
        onSortClicked = {},
        onDeleteAllConfirmed = {}
    )
}


@Composable
@Preview(name = "Light Theme")
fun PreviewSearchAppBar() {
    SearchAppBar(
        text = "",
        onTextChanged = {},
        onCloseClicked = {}) {
    }
}