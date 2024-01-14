package com.example.jettodo2.presenter.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.jettodo2.domain.model.Task
import java.util.Date

//TODO: 下記アノテーション：試験運用版の API となっており、それがまだ安定しておらず将来的に変更される可能性もあるが導入しつかえるようにするもの
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskItemView(
    task: Task,
    onCheckedChange: (Task) -> Unit,
    onClickItem: (Long) -> Unit,
    onClickDelete: (Task) -> Unit,
) {
    // TODO: ()内：　その部品自体の設定
    // TODO: {}内：　内部に配置する部品
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = 16.dp,
                vertical = 8.dp
            ),
//            .clickable { onClickItem(task) },
        elevation = CardDefaults.cardElevation(
            defaultElevation = 5.dp
        ),
        // TODO:カードの背景色を変える
        colors = CardDefaults.cardColors(
            containerColor = Color.White,
        ),
        onClick = {
            onClickItem(task.id)
        }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Checkbox(
                checked = task.isDone,
                onCheckedChange = { isChecked ->
                    onCheckedChange(task.copy(isDone = isChecked))
//                    onCheckedChange(task.copy(isDone = it))
                }
            )
            Text(
                modifier = Modifier.weight(1f),
                text = task.title,
                minLines = 1,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
            IconButton(
                // TODO:IconButtonの不要な余白の削除（24.dpに設定する）
                modifier = Modifier
                    .padding(0.dp)
                    .size(24.dp),
                onClick = {
                    onClickDelete(task)
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    tint = Color.Black,
                    contentDescription = "削除",
                )
            }
        }

    }
}

@Preview
@Composable
fun PreviewTaskItemView() {
    TaskItemView(
        task = Task(
            id = 0L,
            title = "タイトルタイトルタイトルタイトルタイトルタイトルタイトルタイトルタイトルタイトルタイトルタイトルタイトル",
            description = "詳細詳細詳細",
            isDone = true,
            updatedAt = Date(System.currentTimeMillis()),
            createdAt = Date(System.currentTimeMillis()),
        ),
        onCheckedChange = {},
        onClickItem = {},
        onClickDelete = {},
    )
}
