package com.example.todolistcomposemvvm.data.models

import androidx.compose.ui.graphics.Color
import com.example.todolistcomposemvvm.ui.theme.HighPriority
import com.example.todolistcomposemvvm.ui.theme.LowPriority
import com.example.todolistcomposemvvm.ui.theme.MediumPriority
import com.example.todolistcomposemvvm.ui.theme.NonePriority

enum class Priority(val color: Color) {

    HIGH(HighPriority),
    MEDIUM(MediumPriority),
    LOW(LowPriority),
    NONE(NonePriority)

}