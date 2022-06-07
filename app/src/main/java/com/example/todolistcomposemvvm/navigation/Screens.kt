package com.example.todolistcomposemvvm.navigation

import androidx.navigation.NavHostController
import com.example.todolistcomposemvvm.util.Action
import com.example.todolistcomposemvvm.util.Constants.LIST_SCREEN
import com.example.todolistcomposemvvm.util.Constants.NOTE_SCREEN
import com.example.todolistcomposemvvm.util.Constants.SPLASH_SCREEN

class Screens(navController: NavHostController) {

    val splash: () -> Unit = {
        navController.navigate("list/${Action.NO_ACTION.name}") {
            popUpTo(SPLASH_SCREEN) { inclusive = true }
        }
    }

    val list: (Int) -> Unit = {
        navController.navigate("note/${it}")
    }

    val note: (Action) -> Unit = {
        navController.navigate("list/${it.name}") {
            popUpTo(LIST_SCREEN) { inclusive = true }
        }
    }
}