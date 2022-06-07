package com.example.todolistcomposemvvm

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.navigation.NavHostController
import com.example.todolistcomposemvvm.navigation.SetUpNavGraph
import com.example.todolistcomposemvvm.ui.theme.ToDoListComposeMVVMTheme
import com.example.todolistcomposemvvm.ui.viewModels.SharedViewModel
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
@ExperimentalMaterialApi
@ExperimentalAnimationApi
class MainActivity : ComponentActivity() {

    private lateinit var navController: NavHostController
    private val sharedViewModel: SharedViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ToDoListComposeMVVMTheme {
                navController = rememberAnimatedNavController()
                SetUpNavGraph(
                    navController = navController,
                    sharedViewModel = sharedViewModel
                )
            }
        }
    }
}