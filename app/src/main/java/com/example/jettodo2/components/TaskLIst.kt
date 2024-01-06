package com.example.jettodo2.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.example.jettodo2.domain.model.Task

@Composable
fun TaskList(
    taskList: List<Task>,
    onClickItem: (Task) -> Unit,
    onClickDelete: (Task) -> Unit,
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(
            items = taskList,
            key = { task -> task.id },
        ) { task ->
            TaskItemView(
                task = task,
                onClickItem = onClickItem,
                onClickDelete = onClickDelete,
            )
        }
    }
}
