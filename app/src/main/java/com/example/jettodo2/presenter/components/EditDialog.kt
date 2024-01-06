package com.example.jettodo2.presenter.components

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.jettodo2.presenter.viewmodel.TaskListViewModel

@Composable
fun EditDialog(
    // TODO ＠ViewModel とついた指定型の ViewModel を @Composable に DI するための記述
    viewModel: TaskListViewModel = hiltViewModel(),
) {
    // TODO: Dialogのバックグラウンド：エミュレータでは黒透過の色にならない。実機ではなるため問題ないと判断
    AlertDialog(
        icon = {
            Icon(Icons.Default.Edit, contentDescription = "Example Icon")
        },
        title = {
            Text(
                text = "新規タスク追加",
                color = Color.Gray
            )
        },
        text = {
            Column {
                Text(text = "タイトル")
                TextField(
                    value = viewModel.title,
                    placeholder = { Text(text = "タイトルを入力してください。") },
                    onValueChange = {
                        viewModel.title = it
                    }
                )
                Text(text = "詳細")
                TextField(
                    value = viewModel.description,
                    placeholder = { Text(text = "タスクの詳細を入力してください。") },
                    onValueChange = {
                        viewModel.description = it
                    }
                )
            }
        },
        onDismissRequest = {
            Log.d("EditDialog", "@@@@@ dismiss")
            viewModel.isShowAddTaskDialog = false
        },
        confirmButton = {
            TextButton(
                onClick = {
                    Log.d("EditDialog", "@@@@@ confirmButton")
                    viewModel.isShowAddTaskDialog = false
                    // タスクの新規追加
                    viewModel.createTask("タイとる", "aaaa")
                }
            ) {
                Text("確認")
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    Log.d("EditDialog", "@@@@@ dismissButton")
                    viewModel.isShowAddTaskDialog = false
                }
            ) {
                Text("キャンセル")
            }
        },
        //        containerColor = Color.LightGray
        //        tonalElevation = 150.dp
    )
//    Dialog(onDismissRequest = { }) {
////        (LocalView.current.parent as DialogWindowProvider)?.window?.setDimAmount(1f)
//        // Draw a rectangle shape with rounded corners inside the dialog
//        Card(
//            modifier = Modifier
//                .fillMaxWidth()
//                .height(375.dp)
//                .padding(16.dp),
//            shape = RoundedCornerShape(16.dp),
//        ) {
//            Column(
//                modifier = Modifier
//                    .fillMaxSize(),
//                verticalArrangement = Arrangement.Center,
//                horizontalAlignment = Alignment.CenterHorizontally,
//            ) {
////                Image(
////                    painter = Painter,
////                    contentDescription = "imageDescription",
////                    contentScale = ContentScale.Fit,
////                    modifier = Modifier
////                        .height(160.dp)
////                )
//                Text(
//                    text = "This is a dialog with buttons and an image.",
//                    modifier = Modifier.padding(16.dp),
//                )
//                Row(
//                    modifier = Modifier
//                        .fillMaxWidth(),
//                    horizontalArrangement = Arrangement.Center,
//                ) {
//                    TextButton(
//                        onClick = { isShowEditDialog.value = false },
//                        modifier = Modifier.padding(8.dp),
//                    ) {
//                        Text("Dismiss")
//                    }
//                    TextButton(
//                        onClick = { isShowEditDialog.value = false },
//                        modifier = Modifier.padding(8.dp),
//                    ) {
//                        Text("Confirm")
//                    }
//                }
//            }
//        }
//    }
}
