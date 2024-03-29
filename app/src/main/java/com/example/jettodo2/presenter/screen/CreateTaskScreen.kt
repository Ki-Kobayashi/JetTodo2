package com.example.jettodo2.presenter.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.jettodo2.R
import com.example.jettodo2.presenter.viewmodel.CreateTaskViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
// TODO: 画面レイアウトは、ViewModelとは疎結合にしたいため、ViewModel内の処理は外から渡すようにする
//  　　　→（※　private でレイアウト部分を分離し、処理は外側から渡すようにする）
//   処理を切り離すことで、レイアウトを使いまわせるようになる（画面（Screen）ですら、部品扱いするということ）。
fun CreateTaskScreen(
    viewModel: CreateTaskViewModel,
    onBack: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()
    CreateTaskContent(
        onBack = { onBack() },
        // TODO: 【🌟１】CreateTaskContent内で、title/descriptionがセットされるコールバック
        onCreate = { title, description ->
            viewModel.create(title, description)
        },
        uiState = uiState,
        changeIdle = {
            viewModel.changeIdle()
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CreateTaskContent(
    onBack: () -> Unit,
    onCreate: (String, String) -> Unit,
    uiState: CreateTaskViewModel.UiState,
    changeIdle: () -> Unit,
) {
    // TODO: ComposableのContextを取得するには、以下のように書く
    val context = LocalContext.current
    // TODO: remember と rememberSavable　の使い分け【両方とも、viewModelよりライフサイクルは短い】
    //      🌟 remember: 値が変化したときの再描トリガー（画面回転後、Activityの再生成後はdisposeされ、値が初期化されてしまう）
    //      🌟 rememberSaveable: ユーザーが行った操作の状態を保存・復元するのに使用（画面回転やスクロール位置も含む）
    var title by rememberSaveable {
        mutableStateOf("")
    }
    var description by rememberSaveable {
        mutableStateOf("")
    }
    val snackbarState = remember {
        SnackbarHostState()
    }

    // TODO:値が変化したときに”1度だけ”処理させる（引数には、監視対象の変数を入れる）
    // TODO: 【LaunchedEffect】：コンポジション（画面構築）に入ると、コンポジションのCoroutineContextにブロックを起動
    //      🚨※ 重要 ※
    //         ※ UI を組み立てる Composable 内でコルーチンを使用する（ suspend 関数を呼び出す）場合は、LaunchedEffect を使用すること.
    //         ※ LaunchedEffect を使用しない場合（※：onClick内など Composable 内でない場合）は、以下のコメントアウトのように書く
    //
    //    val scope = rememberCoroutineScope()
    //    scope.launch {
    //        snackbarState.showSnackbar("スナックバーに表示させたい文言")
    //    }

    LaunchedEffect(uiState) {
        when (uiState) {
            CreateTaskViewModel.UiState.CreateSuccess -> {
                onBack()
                changeIdle()
            }

            is CreateTaskViewModel.UiState.CreateError -> {
                uiState.e.message?.let { snackbarState.showSnackbar(it) }
            }

            is CreateTaskViewModel.UiState.InputErr -> {
                // TODO: ここでは stringResuorce は呼べない
                //  （ Composable 呼び出しは、@Composable 関数のコンテキストからのみ可能）
                //  　　→ showSnackBar :
                //  　　　　・@Composable でない
                //  　　　　・suspend関数 = この関数を呼べるのはコルーチン内のみ（LaunchedEffectはコルーチンを起動している）
//               【✖】 snackbarState.showSnackbar(message = stringResource(id = R.string.input_err_description))
//               【〇】　snackbarState.showSnackbar(message = context.getString(R.string.xxxxx))
                snackbarState.showSnackbar(uiState.errMessage)
                changeIdle()
            }

            else -> {}
        }
    }
    Scaffold(
        snackbarHost = {
            SnackbarHost(snackbarState)
        },
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        // TODO: Composable内では、「stringResource」を使用してstring.xmlの値を読み出せる（Composable以外は不可）
                        text = stringResource(id = R.string.create_task_title),
                        // TODO: これをいれないと、アイコンとタイトルがズレる（タイトルがTopにへばりつく形）
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        onBack()
                    }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = stringResource(id = R.string.back)
                        )
                    }
                },
                actions = {
                    IconButton(
                        // TODO:【🌟１】コールバックに、title/descriptionをセット
                        onClick = { onCreate(title, description) }
                    ) {
                        Icon(imageVector = Icons.Filled.Done, contentDescription = "create")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
//            verticalArrangement = Arrangement.Bottom,
            modifier = Modifier.padding(paddingValues)
        ) {
            OutlinedTextField(
                value = title,
                label = { Text(text = stringResource(id = R.string.label_title_field)) },
                singleLine = true, // デフォルトはfalse（複数行）
                modifier = Modifier
                    .padding(
                        vertical = 8.dp,
                        horizontal = 16.dp
                    )
                    .fillMaxWidth(),
                onValueChange = { title = it },
            )
            OutlinedTextField(
                value = description,
                label = { Text(text = stringResource(id = R.string.label_description_field)) },
                modifier = Modifier
                    .padding(
                        vertical = 8.dp,
                        horizontal = 16.dp
                    )
                    .fillMaxSize(),
                onValueChange = { description = it },
            )
        }
    }
}

@Preview
@Composable
private fun Preview() {
    CreateTaskContent(
        onBack = {},
        onCreate = { _, _ -> },
        uiState = CreateTaskViewModel.UiState.Idle,
        changeIdle = {}
    )
}
