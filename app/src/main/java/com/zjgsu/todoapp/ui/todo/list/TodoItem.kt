package com.zjgsu.todoapp.ui.todo.list

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.zjgsu.todoapp.data.model.Todo
import com.zjgsu.todoapp.data.model.TodoStatus
import com.zjgsu.todoapp.ui.theme.TodoAppTheme
import java.util.UUID

@Composable
fun TodoItem(
    todo: Todo,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = todo.title,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = todo.status.toString(),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Preview
@Composable
fun TodoItemPreview() {
    TodoAppTheme {
        TodoItem(
            todo = Todo(
                id = UUID.randomUUID(),
                title = "Sample Todo Item",
                status = TodoStatus.PENDING,
                description = "This is a sample todo item"
            )
        )
    }
}