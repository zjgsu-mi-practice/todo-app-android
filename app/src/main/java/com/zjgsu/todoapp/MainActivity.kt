package com.zjgsu.todoapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.zjgsu.todoapp.ui.navigation.AppNavigation // Import AppNavigation
import com.zjgsu.todoapp.ui.theme.TodoAppTheme
// Remove TodoListScreen import if no longer directly used here
import org.koin.androidx.compose.koinViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TodoAppTheme {
                TodoApp()
            }
        }
    }
}

@Composable
fun TodoApp() {
    val navController = rememberNavController()
    AppNavigation(navController = navController) // Use AppNavigation
}

@Preview(showBackground = true)
@Composable
fun TodoAppPreview() {
    TodoAppTheme {
        TodoApp()
    }
}