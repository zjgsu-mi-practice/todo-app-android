package com.zjgsu.todoapp.data.model

import java.util.UUID

data class Category(
    val id: UUID,
    val name: String,
    val color: String? = null
)