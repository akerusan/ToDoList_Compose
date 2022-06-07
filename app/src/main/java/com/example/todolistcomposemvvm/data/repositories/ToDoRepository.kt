package com.example.todolistcomposemvvm.data.repositories

import com.example.todolistcomposemvvm.data.ToDoDao
import com.example.todolistcomposemvvm.data.models.ToDoNote
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@ViewModelScoped
class ToDoRepository @Inject constructor(private val toDoDao: ToDoDao) {

    val getAllNotes: Flow<List<ToDoNote>> = toDoDao.getAllNotes()
    val sortByLowPriority: Flow<List<ToDoNote>> = toDoDao.sortByLowPriority()
    val sortByHighPriority: Flow<List<ToDoNote>> = toDoDao.sortByHighPriority()

    fun getSelectedNote(noteId: Int): Flow<ToDoNote> {
        return toDoDao.getSelectedNote(noteId)
    }

    suspend fun addNote(toDoNote: ToDoNote) {
        toDoDao.addNote(toDoNote)
    }

    suspend fun updateNote(toDoNote: ToDoNote) {
        toDoDao.updateNote(toDoNote)
    }

    suspend fun deleteNote(toDoNote: ToDoNote) {
        toDoDao.deleteNote(toDoNote)
    }

    suspend fun deleteAllNotess() {
        toDoDao.deleteAllNotes()
    }

    fun searchDatabase(search: String): Flow<List<ToDoNote>> {
        return toDoDao.searchDatabase(search)
    }
}