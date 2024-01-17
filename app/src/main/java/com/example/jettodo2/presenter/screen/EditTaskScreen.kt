package com.example.jettodo2.presenter.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.jettodo2.R
import com.example.jettodo2.domain.model.Task
import com.example.jettodo2.presenter.viewmodel.EditTaskViewModel
import java.util.Date

@Composable
fun EditTaskScreen(
    // TODO:ViewModelは基本外から注入、Nav関連もMyApp（NavHostの定義場所）で制御するようにする
    viewModel: EditTaskViewModel,
    back: () -> Unit,
) {
    // TODO: private　な　Composable　は部品として使いまわすこともある　→　処理は public な Composable から渡すようにする
    //         →　ViewModelは、MyAppでさらに外部から渡すようにする（一元管理できるから？）
    //         .
    // TODO: 【collectAsState】 : StateFlow　や　Flow　からデータを収集し、そのデータを　ComposeUI　の　State　オブジェクトに変換。
    //       これにより、Compose が状態の変更を検知して UI を再描画できるようになる
    val uiState by viewModel.uiState.collectAsState()
    // TODO: uiState を渡して、変化があれば再コンポーズさせる？
    EditTaskScreen(
        uiState = uiState,
        back = back,
        loadData = { viewModel.loadData() },
        changeIdle = { viewModel.changeIdle() },
        updateTask = { title, description ->
            viewModel.updateTask(title, description)
        },
        showDeleteDialog = {
            viewModel.showDeleteDialog()
        },
        deleteTask = {
            viewModel.deleteTask()
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EditTaskScreen(
    uiState: EditTaskViewModel.UiState,
    back: () -> Unit,
    loadData: () -> Unit,
    changeIdle: () -> Unit,
    updateTask: (String, String) -> Unit,
    showDeleteDialog: () -> Unit,
    deleteTask: () -> Unit,
) {
    // TODO: 「by」で書くと、.valueに直接アクセスできる（titleだけで中身の取得が可能）。
    //  　　　「＝」で書くと、title.valueのように、「.value」とつけないと中身にアクセスできない
    var title by rememberSaveable { mutableStateOf("") }
    var description by rememberSaveable { mutableStateOf("") }
    var menuExpandedState by rememberSaveable { mutableStateOf(false) }

    // TODO: 【SnackBarの表示:設定】　下記2行が各Composable（主に表示したい画面のComposable）に必要
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    // TODO: 状態によって見た目を出し分ける
    Scaffold(
        // TODO: 【SnackBarの表示:設定】
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.edit_task_title)) },
                navigationIcon = {
                    IconButton(onClick = back) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Back",
                        )
                    }
                },
                // TODO:AppBarの右端に配置されるボタン群（複数指定可能）
                //    actionは、RowScopeのため、Rowを逐一書かなくとも要素が横並びになる.
                actions = {
                    // Taskが読み込み中の時にボタンを押されると困る　→　UIStateがIdleのときのみ、ボタンは表示させるようにする
                    if (uiState is EditTaskViewModel.UiState.Idle) {
                        // [✔] 編集完了ボタン
                        IconButton(
                            onClick = {
                                updateTask(title, description)
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Done,
                                contentDescription = "Save",
                            )
                        }
                        // メニュー表示ボタン
                        IconButton(onClick = { menuExpandedState = !menuExpandedState }) {
                            Icon(
                                imageVector = Icons.Filled.MoreVert,
                                contentDescription = "Menu",
                            )
                        }

                        // TODO: 【DropdownMenu】の使い方（AppBarと紐づけるなら、AppBar > action 内に定義）
                        DropdownMenu(
                            // menuExpandedState が true　の間だけ、menuが表示される
                            expanded = menuExpandedState,
                            // メニュー表示部分の背景をタップした時に呼ばれる
                            onDismissRequest = { menuExpandedState = false },
                        ) {
                            // TODO: 【DropdownMenu】表示させるアイテムはここに記載する
                            //      【DropdownMenu】のContent内は、ColumnScopeのため、Columnを書かなくても縦並びになる.
                            DropdownMenuItem(
                                text = { Text(text = stringResource(id = R.string.button_delete)) },
                                onClick = {
                                    // メニューを閉じる
                                    menuExpandedState = false
                                    // 削除用ダイアログを開く
                                    showDeleteDialog()
                                },
                            )
                        }
                    }
                },
            )
        },
    ) { paddingValues ->
        when (uiState) {
            EditTaskViewModel.UiState.Initial,
            EditTaskViewModel.UiState.Loading,
            is EditTaskViewModel.UiState.LoadSuccess -> {
                // 画面中央に表示
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .padding(paddingValues)
                        .fillMaxSize()
                ) {
                    CircularProgressIndicator()
                }
            }

            is EditTaskViewModel.UiState.LoadError -> {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .padding(paddingValues)
                        .fillMaxSize()
                ) {
                    Text(text = uiState.error.toString())
                }
            }
//            is EditTaskViewModel.UiState.Idle,
//            is EditTaskViewModel.UiState.InputError,
//            EditTaskViewModel.UiState.UpdateInProgress,
//            is EditTaskViewModel.UiState.UpdateSuccess,
//            is EditTaskViewModel.UiState.UpdateError,
//            is EditTaskViewModel.UiState.ConfirmDelete,
//            EditTaskViewModel.UiState.DeleteInProgress,
//            is EditTaskViewModel.UiState.DeleteSuccess,
//            is EditTaskViewModel.UiState.DeleteError
            else -> {
                // TODO: 【paddingValues】ここで余白を渡すことで、書くComposableで統一の余白をもたせる（明示的）
                EditTaskForm(
                    modifier = Modifier.padding(paddingValues),
                    title = title,
                    description = description,
                    onChangeTitle = { title = it },
                    onChangeDescription = { description = it },
                )

                if (uiState is EditTaskViewModel.UiState.ConfirmDelete) {
                    // TODO　:【AlertDialog】の使い方（ AlertDialog を呼ぶだけで、ダイアログが表示される（ DialogFragment 不要））
                    AlertDialog(
                        onDismissRequest = {
                            // UI状態を Idle 状態に戻せば、ダイアログは消える
                            // 　　（UI状態が ConfirmDelete の時だけDialog表示させるようにしているため）
                            changeIdle()
                        },
                        title = {
                            Text(
                                text = stringResource(id = R.string.confirm_dialog_title)
                            )
                        },
                        confirmButton = {
                            TextButton(onClick = { deleteTask() }) {
                                Text(text = stringResource(id = android.R.string.ok))
                            }
                        },
                        dismissButton = {
                            TextButton(onClick = { changeIdle() }) {
                                Text(text = stringResource(id = android.R.string.cancel))
                            }
                        },
                    )
                }
            }
        }
    }

    // TODO: 状態によって、処理する内容を出し分ける
    LaunchedEffect(uiState) {
        when (uiState) {
            EditTaskViewModel.UiState.Initial -> {
                loadData()
            }

            EditTaskViewModel.UiState.Loading -> {}

            is EditTaskViewModel.UiState.LoadSuccess -> {
                //LoadSuccessステートに変化したら、ステートに格納されたTaskを画面にセットし、Idle状態へ変更
                title = uiState.task.title
                description = uiState.task.description
                changeIdle()
            }

            is EditTaskViewModel.UiState.LoadError -> {}

            is EditTaskViewModel.UiState.InputError -> {
                // 入力値エラーステートに変化したら、スナックバー表示 ＋ Idle状態へ変更
                // TODO: 【SnackBarの表示】
                snackbarHostState.showSnackbar(message = uiState.errMessage)
                // TODO: 【非同期内でstringリソース値を呼ぶ方法👇】　@Composable内でないため、stringResorcesは使用不可
//                snackbarHostState.showSnackbar(message = context.getString(R.string.xxx_xxx))
                changeIdle()
            }

            is EditTaskViewModel.UiState.Idle -> {}
            // Task更新時
            EditTaskViewModel.UiState.UpdateInProgress -> {}
            EditTaskViewModel.UiState.UpdateSuccess -> {
                back()
            }

            is EditTaskViewModel.UiState.UpdateError -> {
                // 更新失敗ステートに変化したら、スナックバー表示
                snackbarHostState.showSnackbar(message = uiState.err.toString())
                changeIdle()
            }
            // 削除ダイアログ表示時
            is EditTaskViewModel.UiState.ConfirmDelete -> {}
            // Task削除時
            EditTaskViewModel.UiState.DeleteInProgress -> {}
            EditTaskViewModel.UiState.DeleteSuccess -> {
                back()
                snackbarHostState.showSnackbar(message = context.getString(R.string.deleted_task_sccess))
            }

            is EditTaskViewModel.UiState.DeleteError -> {
                snackbarHostState.showSnackbar(message = uiState.err.toString())
                changeIdle()
            }

        }
    }
}

@Composable
fun EditTaskForm(
    modifier: Modifier = Modifier,
    title: String,
    description: String,
    onChangeTitle: (String) -> Unit,
    onChangeDescription: (String) -> Unit,
) {
    Column(
        modifier.fillMaxWidth()
    ) {
        OutlinedTextField(
            singleLine = true,
            label = { Text(text = stringResource(id = R.string.label_title_field)) },
            value = title,
            modifier = Modifier
                .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 8.dp)
                .fillMaxWidth(),
            onValueChange = onChangeTitle,
        )
        OutlinedTextField(
            label = {
                Text(text = stringResource(id = R.string.label_description_field))
            },
            value = description,
            modifier = Modifier
                .padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 16.dp)
                .fillMaxSize(),
//            modifier = modifier.fillMaxSize(),
            onValueChange = onChangeDescription,
        )
    }
}

@Preview
@Composable
private fun Preview() {
    EditTaskScreen(
        uiState = EditTaskViewModel.UiState.Idle(
            Task(
                id = 1L,
                title = "テスト",
                description = "これは説明文",
                isDone = false,
                createdAt = Date(),
                updatedAt = Date(),
            )
        ),
        back = { },
        loadData = { },
        changeIdle = { },
        updateTask = { title, description ->
        },
        showDeleteDialog = {
        },
        deleteTask = {
        }
    )
}
