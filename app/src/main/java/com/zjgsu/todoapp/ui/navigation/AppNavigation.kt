package com.zjgsu.todoapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.zjgsu.todoapp.ui.todo.addedit.AddEditTodoScreen
import com.zjgsu.todoapp.ui.todo.list.TodoListScreen

object Screen {
    const val TodoList = "todoList"
    const val AddEditTodo = "addEditTodo"
}

object RouteArgs {
    const val TODO_ID = "todoId"
}

@Composable
fun AppNavigation(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Screen.TodoList) {
        composable(Screen.TodoList) {
            TodoListScreen(navController = navController)
        }
        composable(
            route = "${Screen.AddEditTodo}?${RouteArgs.TODO_ID}={${RouteArgs.TODO_ID}}",
            arguments = listOf(navArgument(RouteArgs.TODO_ID) {
                type = NavType.StringType
                nullable = true // Make todoId optional for "Add" mode
            })
        ) {
            // ViewModel will be injected by koinViewModel in the Screen composable
            AddEditTodoScreen(navController = navController)
        }
         composable( // Route without argument for adding new todos
            route = Screen.AddEditTodo
        ) {
             AddEditTodoScreen(navController = navController)
        }
    }
}