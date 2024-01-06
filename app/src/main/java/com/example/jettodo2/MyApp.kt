package com.example.jettodo2

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.jettodo2.presenter.screen.CreateTaskScreen
import com.example.jettodo2.presenter.screen.TaskListScreen
import com.example.jettodo2.presenter.viewmodel.CreateTaskViewModel
import com.example.jettodo2.presenter.viewmodel.TaskListViewModel

@Composable
fun MyApp(modifier: Modifier) {
    // TODO: Navを追加するには、以下のように書く
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "/") {
        composable("/") {
            val vm: TaskListViewModel = hiltViewModel()
            TaskListScreen(
                viewModel = vm,
                toCreateTaskScreen = {
                    navController.navigate("/create")
                },
            )
        }
        composable("/create") {
            val vm: CreateTaskViewModel = hiltViewModel()
            CreateTaskScreen(
                viewModel = vm,
                onBack = {
                    navController.popBackStack()
                }
            )
        }
//        composable("/detail", arguments = []) {
//            TaskDetailScreen(task =)
//        }
    }
//    Surface(
//        modifier = modifier,
//        color = MaterialTheme.colorScheme.background
//    ) {
//        MainContent()
//    }
}
