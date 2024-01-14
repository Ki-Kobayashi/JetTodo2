package com.example.jettodo2.presenter.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.jettodo2.R
import com.example.jettodo2.presenter.viewmodel.EditTaskViewModel

@Composable
fun EditTaskScreen(
    // TODO:ViewModelは基本外から注入、Nav関連もMyApp（NavHostの定義場所）で制御するようにする
    viewModel: EditTaskViewModel,
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
        loadData = { viewModel.loadData() },
        changeIdle = { viewModel.changeIdle() }
    )
}

@Composable
private fun EditTaskScreen(
    uiState: EditTaskViewModel.UiState,
    loadData: () -> Unit,
    changeIdle: () -> Unit,
) {
    val title = rememberSaveable { mutableStateOf("") }
    val description = rememberSaveable { mutableStateOf("") }

    // TODO: 状態によって見た目を出し分ける
    Scaffold { paddingValues ->
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

            EditTaskViewModel.UiState.Idle -> {
                // TODO: ここで余白を渡すことで、書くComposableで統一の余白をもたせる（明示的）
                EditTaskForm(
                    modifier = Modifier.padding(paddingValues),
                    title = title.value,
                    description = description.value,
                    onChangeTitle = { title.value = it },
                    onChangeDescription = { description.value = it },
                )
            }
        }
    }

    // TODO: 状態によって、処理する内容を出し分ける
    LaunchedEffect(uiState) {
        when (uiState) {
            EditTaskViewModel.UiState.Initial -> {
                loadData()
            }

            EditTaskViewModel.UiState.Loading -> {

            }

            is EditTaskViewModel.UiState.LoadSuccess -> {
                title.value = uiState.task.title
                description.value = uiState.task.description
                changeIdle()
            }

            is EditTaskViewModel.UiState.LoadError -> {

            }


            EditTaskViewModel.UiState.Idle -> {

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
            modifier = modifier
                .padding(
                    vertical = 8.dp,
                    horizontal = 16.dp
                )
                .fillMaxWidth(),
            onValueChange = onChangeTitle,
        )
        OutlinedTextField(
            label = {
                Text(text = stringResource(id = R.string.label_description_field))
            },
            value = description,
            modifier = modifier
                .padding(
                    vertical = 8.dp,
                    horizontal = 16.dp
                )
                .fillMaxSize(),
            onValueChange = onChangeDescription,
        )
    }
}

//@Preview
//@Composable
//private fun Preview() {
//    EditTaskScreen()
//}
