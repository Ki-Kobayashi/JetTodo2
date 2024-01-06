package com.example.jettodo2.database.entiry

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "task")
data class TaskEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val title: String,
    val description: String,
    val done: Int,
    val createdAt: Long, // TODO: Unix timeStampの値を保存するためLong
    val updatedAt: Long,
)
