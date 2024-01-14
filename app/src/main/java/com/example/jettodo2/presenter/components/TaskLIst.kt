package com.example.jettodo2.presenter.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.example.jettodo2.domain.model.Task

@Composable
fun TaskList(
    paddingValues: PaddingValues,
    taskList: List<Task>,
    onCheckedChange: (Task) -> Unit,
    onClickDelete: (Task) -> Unit,
    onClickListRow: (Long) -> Unit,
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = paddingValues,
    ) {
        items(
//            items = taskList,
            count = taskList.size,
            // TODO: 複数アイテムを配置する場合、Keyを付与することで、変化している箇所（削除・並び替え・末尾以外の追加）だけを再コンポ―スできるようになる
            key = { index -> taskList[index].id },
        ) { index ->
            TaskItemView(
                task = taskList[index],
                onCheckedChange = onCheckedChange,
                onClickItem = onClickListRow,
                onClickDelete = onClickDelete,
            )
        }
    }
}
