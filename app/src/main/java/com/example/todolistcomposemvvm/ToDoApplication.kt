package com.example.todolistcomposemvvm

import android.app.Application
import android.content.Context
import androidx.compose.animation.ContentTransform
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class ToDoApplication: Application() {
}