package com.example.todolistcomposemvvm.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.todolistcomposemvvm.util.Constants.DATABASE_TABLE

@Entity(tableName = DATABASE_TABLE)
data class ToDoNote(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val title: String,
    val description: String,
    val priority: Priority

)
