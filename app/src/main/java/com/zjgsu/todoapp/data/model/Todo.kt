package com.zjgsu.todoapp.data.model

import com.google.gson.annotations.SerializedName
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
    @SerializedName("pending")
    PENDING,
    @SerializedName("in_progress")
    IN_PROGRESS,
    @SerializedName("completed")
    COMPLETED;
}