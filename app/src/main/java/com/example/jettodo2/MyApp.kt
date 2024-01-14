package com.example.jettodo2

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.jettodo2.presenter.screen.CreateTaskScreen
import com.example.jettodo2.presenter.screen.EditTaskScreen
import com.example.jettodo2.presenter.screen.TaskListScreen
import com.example.jettodo2.presenter.viewmodel.CreateTaskViewModel
import com.example.jettodo2.presenter.viewmodel.EditTaskViewModel
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
                // TODO: 遷移時に値をわたすときは、シンプルな値のみ（API取得に使用するKeyなど）
                onClickListRow = { id ->
                    navController.navigate("/tasks/$id")
                }
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
        composable(
            "/tasks/{taskId}",
            // TODO:画面遷移時に渡せる値はごくシンプルな値に限る（取得に必要なデータのみ渡すようにする）
            arguments = listOf(
                navArgument("taskId") {
                    type = NavType.LongType
                }
            )
        ) {
            val vm: EditTaskViewModel = hiltViewModel()
            EditTaskScreen(viewModel = vm)
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
