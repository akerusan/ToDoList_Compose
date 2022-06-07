package com.example.todolistcomposemvvm.data

import androidx.room.*
import com.example.todolistcomposemvvm.data.models.ToDoNote
import kotlinx.coroutines.flow.Flow

@Dao
interface ToDoDao {

    @Query("SELECT * FROM todo_table ORDER BY id ASC")
    fun getAllNotes(): Flow<List<ToDoNote>>

    @Query("SELECT * FROM todo_table WHERE id=:noteId")
    fun getSelectedNote(noteId: Int): Flow<ToDoNote>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addNote(toDoNote: ToDoNote)

    @Update
    suspend fun updateNote(toDoNote: ToDoNote)

    @Delete
    suspend fun deleteNote(toDoNote: ToDoNote)

    @Query("DELETE FROM todo_table")
    suspend fun deleteAllNotes()

    @Query("SELECT * FROM todo_table WHERE title LIKE :searchQuery OR description LIKE :searchQuery")
    fun searchDatabase(searchQuery: String): Flow<List<ToDoNote>>

    @Query("SELECT * FROM todo_table ORDER BY CASE WHEN priority LIKE 'L%' THEN 1 WHEN priority LIKE 'M%' THEN 2 WHEN priority LIKE 'H%' THEN 3 END")
    fun sortByLowPriority(): Flow<List<ToDoNote>>

    @Query("SELECT * FROM todo_table ORDER BY CASE WHEN priority LIKE 'H%' THEN 1 WHEN priority LIKE 'M%' THEN 2 WHEN priority LIKE 'L%' THEN 3 END")
    fun sortByHighPriority(): Flow<List<ToDoNote>>

}