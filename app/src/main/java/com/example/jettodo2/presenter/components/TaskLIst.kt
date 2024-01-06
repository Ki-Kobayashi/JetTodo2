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
    onClickItem: (Task) -> Unit,
    onClickDelete: (Task) -> Unit,
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = paddingValues,
    ) {
        items(
//            items = taskList,
            count = taskList.size,
            key = { index -> taskList[index].id },
        ) { index ->
            TaskItemView(
                task = taskList[index],
                onClickItem = onClickItem,
                onClickDelete = onClickDelete,
            )
        }
    }
}
