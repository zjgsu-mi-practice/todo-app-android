package com.zjgsu.todoapp.data.model

import java.util.UUID

data class Reminder(
    val id: UUID,
    val todoId: UUID,
    val time: String,
    val notifyMethod: NotifyMethod = NotifyMethod.PUSH
)

enum class NotifyMethod {
    EMAIL,
    PUSH,
    SMS;

    companion object {
        fun fromString(value: String): NotifyMethod {
            return when (value.lowercase()) {
                "email" -> EMAIL
                "push" -> PUSH
                "sms" -> SMS
                else -> throw IllegalArgumentException("Unknown NotifyMethod: $value")
            }
        }
    }
}