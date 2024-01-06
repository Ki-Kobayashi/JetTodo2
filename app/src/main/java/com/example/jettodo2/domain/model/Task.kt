package com.example.jettodo2.domain.model

import java.util.Date

data class Task(
    val id: Long,
    val title: String,
    val description: String,
    val isDone: Boolean,
    val createdAt: Date,
    val updatedAt: Date,
)
