package com.example.jettodo2.presenter.screen

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.tooling.preview.Preview
import com.example.jettodo2.domain.model.Task
import com.example.jettodo2.presenter.components.TaskList
import com.example.jettodo2.presenter.viewmodel.TaskListViewModel

@Composable
fun TaskListScreen(
    viewModel: TaskListViewModel,
    toCreateTaskScreen: () -> Unit,
    onClickListRow: (Long) -> Unit,
    // TODO:下のように記載しない理由：MyApp.ktのNavHost設定で、
    //  　　　　　　　"hiltViewModel()"で外部から注入しているため
//    viewModel: TaskListViewModel = hiltViewModel(),
) {
    // TODO: 下記は同じくタスクリストを取得する
    //      ・by : 直接DB取得値の taskList が格納される
    //      ・= :  DB取得値が state の状態で格納される（taskListにアクセスするには、.valueをつける必要がある）。
    // TODO: 【collectAsState】 : StateFlow　や　Flow　からデータを収集し、そのデータを　ComposeUI　の　State　オブジェクトに変換。
    //       これにより、Compose が状態の変更を検知して UI を再描画できるようになる
    val taskList = viewModel.taskList.collectAsState(initial = emptyList()).value
    TaskListScreen(
        taskList = taskList,
        toCreateTaskScreen = toCreateTaskScreen,
        onCheckedChange = { updatedTask ->
            viewModel.updateTask(updatedTask)
        },
        onDelete = {},
        onClickListRow = onClickListRow
    )
}

@Composable
private fun TaskListScreen(
    taskList: List<Task>,
    toCreateTaskScreen: () -> Unit,
    onCheckedChange: (Task) -> Unit,
    onDelete: (Task) -> Unit,
    onClickListRow: (Long) -> Unit,
) {
    // TODO: Stateは画面回転時に初期化されてしまう：MainActivityが再描画されるため
    //    val isShowEditDialog = remember {
    //        mutableStateOf(false)
    //    }
    // 【解決方法】：
    //   🌟 Activityよりライフサイクルの長い、viewModelでStateは管理するようにする
    //   🌟 viewModelを使用しない形なら「rememberSavale{}」を使用する
    //   　　　（これなら、Activityよりもライフサイクルがながいため、横回転でも状態を保持してくれる）,
    //   ※下記コメント：viewModelで管理しない場合（FlutterのuseState的な感じ）
    // TODO: remember と rememberSavableの使い分け
    //      🌟 remember: 値が変化したときの再描トリガー
    //      🌟 rememberSaveable: ユーザーが行った操作の状態を保存・復元するのに使用（スクロール位置も含む）

//    if (viewModel.isShowAddTaskDialog) {
//        EditDialog()
//    }
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
        // TODO: Scaffoldのなかに、LasyColumnをセットするときは、
        //  　　　ModifierのPaddingではなく、”contentPadding”に、PaddingValuesをセットする
        //       　そうすることで、AppBarの下にコンテンツが配置されるようになる。
        TaskList(
            paddingValues = paddingValues,
            taskList = taskList,
            onCheckedChange = onCheckedChange,
            onClickDelete = {
                onDelete(it)
            },
            onClickListRow = onClickListRow,
        )

    }
}

@Preview
@Composable
private fun Preview() {
    TaskListScreen(
        taskList = emptyList(),
        toCreateTaskScreen = {},
        // TODO: 使用しない引数の時は「_」表記
        onCheckedChange = { _ -> },
        onDelete = { _ -> },
        onClickListRow = { _ -> }
    )
}
