package com.zjgsu.todoapp.ui.todo.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.zjgsu.todoapp.data.model.Todo
import com.zjgsu.todoapp.data.model.TodoStatus
import com.zjgsu.todoapp.ui.theme.TodoAppTheme
import java.util.UUID

@Composable
fun TodoItem(
    todo: Todo,
    onDeleteClick: (String) -> Unit,
    onItemClick: (String) -> Unit,
    onStatusChange: (String, TodoStatus) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onItemClick(todo.id.toString()) }
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
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
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = todo.status == TodoStatus.COMPLETED,
                        onCheckedChange = { isChecked ->
                            val newStatus = if (isChecked) TodoStatus.COMPLETED else TodoStatus.PENDING
                            onStatusChange(todo.id.toString(), newStatus)
                        },
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    IconButton(
                        onClick = { onDeleteClick(todo.id.toString()) }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete Todo",
                            tint = Color.Red
                        )
                    }
                }
            }
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
            ),
            onDeleteClick = {},
            onItemClick = {},
            onStatusChange = { _, _ -> }
        )
    }
}