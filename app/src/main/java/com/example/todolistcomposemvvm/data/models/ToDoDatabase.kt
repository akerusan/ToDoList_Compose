package com.example.todolistcomposemvvm.data.models

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.todolistcomposemvvm.data.ToDoDao

@Database(entities = [ToDoNote::class], version = 1, exportSchema = false)
abstract class ToDoDatabase: RoomDatabase() {
    abstract fun toDoDao(): ToDoDao
}