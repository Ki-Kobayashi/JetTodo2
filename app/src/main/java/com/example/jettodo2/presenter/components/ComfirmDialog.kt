package com.example.jettodo2.presenter.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

@Composable
fun ConfirmDialog(
    title: String,
    description: String,
    positiveLabel: String,
    onDismissRequest: () -> Unit,
    onClickAction: () -> Unit,
    onClickCancel: () -> Unit
) {
    AlertDialog(
        title = {
            Text(text = title)
        },
        text = {
            Text(text = description)
        },
        onDismissRequest = { onDismissRequest() },
        confirmButton = {
            TextButton(
                onClick = {
                    onClickAction()
                },
            ) {
                Text(text = positiveLabel)
            }
        },
        dismissButton = {
            onClickCancel
        },
    )
}
