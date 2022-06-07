package com.example.todolistcomposemvvm.ui.viewModels

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todolistcomposemvvm.data.models.Priority
import com.example.todolistcomposemvvm.data.models.ToDoNote
import com.example.todolistcomposemvvm.data.repositories.DataStoreRepository
import com.example.todolistcomposemvvm.data.repositories.ToDoRepository
import com.example.todolistcomposemvvm.util.Action
import com.example.todolistcomposemvvm.util.Constants.MAX_TITLE_LENGTH
import com.example.todolistcomposemvvm.util.RequestState
import com.example.todolistcomposemvvm.util.SearchAppBarState
import com.example.todolistcomposemvvm.util.SearchAppBarState.CLOSED
import com.example.todolistcomposemvvm.util.SearchAppBarState.TRIGGERED
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor(
    private val repository: ToDoRepository,
    private val dataStoreRepository: DataStoreRepository) : ViewModel() {

    val action: MutableState<Action> = mutableStateOf(Action.NO_ACTION)

    private val id: MutableState<Int> = mutableStateOf(0)
    val title: MutableState<String> = mutableStateOf("")
    val description: MutableState<String> = mutableStateOf("")
    val priority: MutableState<Priority> = mutableStateOf(Priority.LOW)

    val searchAppBarState: MutableState<SearchAppBarState> = mutableStateOf(CLOSED)
    val searchTextState: MutableState<String> = mutableStateOf("")

    private val _searchNotes = MutableStateFlow<RequestState<List<ToDoNote>>>(RequestState.Idle)
    val searchNotes: StateFlow<RequestState<List<ToDoNote>>> = _searchNotes

    fun searchDatabase(searchQuery: String) {
        _searchNotes.value = RequestState.Loading
        try {
            viewModelScope.launch {
                repository.searchDatabase(search = "%$searchQuery%")
                    .collect {
                        _searchNotes.value = RequestState.Success(it)
                    }
            }
        } catch (error: Exception) {
            _searchNotes.value = RequestState.Error(error)
        }
        searchAppBarState.value = TRIGGERED
    }

    val lowPriorityNote: StateFlow<List<ToDoNote>> =
        repository.sortByLowPriority.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            emptyList()
        )

    val highPriorityNote: StateFlow<List<ToDoNote>> =
        repository.sortByHighPriority.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            emptyList()
        )

    private val _sortState = MutableStateFlow<RequestState<Priority>>(RequestState.Idle)
    val sortState: StateFlow<RequestState<Priority>> = _sortState

    fun readSortState() {
        _sortState.value = RequestState.Loading
        try {
            viewModelScope.launch {
                dataStoreRepository.readSortState
                    .map { Priority.valueOf(it) }
                    .collect {
                        _sortState.value = RequestState.Success(it)
                    }
            }
        } catch (error: Exception) {
            _sortState.value = RequestState.Error(error)
        }
    }

    fun persistSortingState(priority: Priority) {
        viewModelScope.launch(Dispatchers.IO) {
            dataStoreRepository.persistSortState(priority)
        }
    }


    private val _allNotes = MutableStateFlow<RequestState<List<ToDoNote>>>(RequestState.Idle)
    val allNotes: StateFlow<RequestState<List<ToDoNote>>> = _allNotes

    fun getAllNotes() {
        _allNotes.value = RequestState.Loading
        try {
            viewModelScope.launch {
                repository.getAllNotes.collect {
                    _allNotes.value = RequestState.Success(it)
                }
            }
        } catch (error: Exception) {
            _allNotes.value = RequestState.Error(error)
        }
    }

    private val _selectedNote: MutableStateFlow<ToDoNote?> = MutableStateFlow(null)
    val selectedNote: StateFlow<ToDoNote?> = _selectedNote

    fun getSelectedNote(noteId: Int) {
        viewModelScope.launch {
            repository.getSelectedNote(noteId).collect { note ->
                _selectedNote.value = note
            }
        }
    }

    private fun addNote() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addNote(
                ToDoNote(
                    title = title.value,
                    description = description.value,
                    priority = priority.value
                )
            )
        }
        searchAppBarState.value = CLOSED
    }

    private fun updateNote() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateNote(
                ToDoNote(
                    id = id.value,
                    title = title.value,
                    description = description.value,
                    priority = priority.value
                )
            )
        }
    }

    private fun deleteNote() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteNote(
                ToDoNote(
                    id = id.value,
                    title = title.value,
                    description = description.value,
                    priority = priority.value
                )
            )
        }
    }

    private fun deleteAllNotes() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAllNotess()
        }
    }

    fun handleDatabaseActions(action: Action) {
        when (action) {
            Action.ADD -> {
                addNote()
            }
            Action.UPDATE -> {
                updateNote()
            }
            Action.DELETE -> {
                deleteNote()
            }
            Action.DELETE_ALL -> {
                deleteAllNotes()
            }
            Action.UNDO -> {
                addNote()
            }
            else -> {

            }
        }
        this.action.value = Action.NO_ACTION
    }

    fun updateNoteFields(selectedNote: ToDoNote?) {
        if (selectedNote != null) {
            id.value = selectedNote.id
            title.value = selectedNote.title
            description.value = selectedNote.description
            priority.value = selectedNote.priority
        } else {
            id.value = 0
            title.value = ""
            description.value = ""
            priority.value = Priority.LOW
        }
    }

    fun updateTitle(newTitle: String) {
        if (newTitle.length < MAX_TITLE_LENGTH) {
            title.value = newTitle
        }
    }

    fun validateFields(): Boolean {
        return title.value.isNotEmpty() && description.value.isNotEmpty()
    }
}