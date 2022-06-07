package com.example.todolistcomposemvvm.ui.screens.note

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.todolistcomposemvvm.R
import com.example.todolistcomposemvvm.components.PriorityDropDown
import com.example.todolistcomposemvvm.data.models.Priority
import com.example.todolistcomposemvvm.ui.theme.LARGE_PADDING
import com.example.todolistcomposemvvm.ui.theme.MEDIUM_PADDING

@Composable
fun NoteContent(
    title: String,
    onTitleChange: (String) -> Unit,
    description: String,
    onDescriptionChange: (String) -> Unit,
    priority: Priority,
    onPrioritySelected: (Priority) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colors.background)
            .padding(all = LARGE_PADDING))
    {
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            label = { Text(stringResource(R.string.title)) },
            value = title,
            onValueChange = { onTitleChange(it) },
            textStyle = MaterialTheme.typography.body1,
            maxLines = 1,
            singleLine = true
        )
        Divider(
            modifier = Modifier.height(MEDIUM_PADDING),
            color = MaterialTheme.colors.background
        )
        PriorityDropDown(
            priority = priority,
            onPrioritySelected = { onPrioritySelected(it) }
        )
        OutlinedTextField(
            modifier = Modifier.fillMaxSize(),
            label = { Text(stringResource(R.string.description)) },
            value = description,
            onValueChange = { onDescriptionChange(it) },
            textStyle = MaterialTheme.typography.body1
        )
    }
}

@Preview
@Composable
fun PreviewNoteContent() {
    NoteContent(
        title = "",
        onTitleChange = {},
        description = "",
        onDescriptionChange = {},
        priority = Priority.LOW,
        onPrioritySelected = {}
    )
}