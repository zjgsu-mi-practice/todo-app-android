package com.zjgsu.todoapp.data.model


data class ApiError(
    val error: ErrorDetail
)

data class ErrorDetail(
    val code: String,
    val message: String,
    val details: Map<String, String>? = null
)