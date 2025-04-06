package com.zjgsu.todoapp.data.model

import java.util.UUID

data class Todo(
    val id: UUID,
    val title: String,
    val description: String? = null,
    val status: TodoStatus,
    val dueDate: String? = null,
    val categoryId: UUID? = null,
    val tagIds: List<String> = emptyList(),
    val memoId: UUID? = null
)

enum class TodoStatus {
    PENDING,
    IN_PROGRESS,
    COMPLETED;

    companion object {
        fun fromString(value: String): TodoStatus {
            return when (value.lowercase()) {
                "pending" -> PENDING
                "in_progress" -> IN_PROGRESS
                "completed" -> COMPLETED
                else -> throw IllegalArgumentException("Unknown TodoStatus: $value")
            }
        }
    }
}