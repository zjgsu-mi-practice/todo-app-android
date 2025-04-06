package com.zjgsu.todoapp.data.model

import java.util.UUID

data class Memo(
    val id: UUID,
    val content: String? = null,
    val attachments: List<String> = emptyList()
)