package com.example.jettodo2.presenter.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.jettodo2.presenter.viewmodel.TaskListViewModel
import com.example.jettodo2.components.EditDialog

@Composable
fun TaskListScreen(
    toCreateTaskScreen: () -> Unit,
    viewModel: TaskListViewModel = hiltViewModel(),
) {
    // TODO: Stateは画面回転時に初期化されてしまう：MainActivityが再描画されるため
    // 【解決方法】：
    //   🌟 Activityよりライフサイクルの長い、viewModelでStateは管理するようにする
    //   🌟 viewModelを使用しない形なら「rememberSavale{}」を使用する
    //   　　　（これなら、Activityよりもライフサイクルがながいため、横回転でも状態を保持してくれる）,
    //   ※下記コメント：viewModelで管理しない場合（FlutterのuseState的な感じ）
    // TODO: remember と remenberSavableの使い分け
    //      🌟remember: 値が変化したときの再描トリガー
    //      🌟 rememberSavable: ユーザーが行った操作の状態を保存・復元する野に使用（スクロール位置も含む）
//    val isShowEditDialog = remember {
//        mutableStateOf(false)
//    }
    if (viewModel.isShowAddTaskDialog) {
        EditDialog()
    }
//    if (viewModel.isShowComfirmDialog) {
//        ConfirmDialog(
//            title = "タスク削除",
//            description = "タスクを削除してもよろしいですか？",
//            positiveLabel = "削除",
//            onDismissRequest = { viewModel.isShowComfirmDialog = false },
//            onClickCancel = {
//                // TODO:たすくどう渡す？
////                viewModel.deleteTask()
//            }
//        )
//    }
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    // TODO: 画面へ遷移する場合（※設定は MyApp.kt を参照）
                    toCreateTaskScreen()
                    // TODO: ダイアログを開く場合
//                    viewModel.isShowAddTaskDialog = true
                }
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "追加")
            }
        }
    ) { paddingValues ->
        // TODO: 下記は同じくタスクリストを取得する
        //      ・by : 直接DB取得値の taskList が格納される
        //      ・= :  DB取得値が state の状態で格納される（taskListにアクセスするには、.valueをつける必要がある）。
        // TODO: collectAsStateメソッドは、StateFlowやFlowからデータを収集し、そのデータをCompose UIのStateオブジェクトに変換。これにより、Composeが状態の変更を検知してUIを再描画できるように
//        val taskList by viewModel.taskList.collectAsState(initial = emptyList())
        Box(
            modifier = Modifier.padding(paddingValues)
        ) {
//            TaskList(
//                taskList = taskList,
//                onClickItem = {},
//                onClickDelete = {
//                    viewModel.deleteTask(it)
//                },
//            )
        }
    }
}

@Preview
@Composable
private fun Preview() {
//    TaskListScreen(
//        toCreateScreen = {},
//    )
}
