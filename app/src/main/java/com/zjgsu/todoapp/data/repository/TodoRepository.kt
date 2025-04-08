package com.zjgsu.todoapp.data.repository

import com.zjgsu.todoapp.data.model.Todo
import com.zjgsu.todoapp.data.remote.CreateTodoRequest
import com.zjgsu.todoapp.data.remote.Pagination
import com.zjgsu.todoapp.data.remote.TodoApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

data class TodoPageResult(
    val todos: List<Todo>,
    val pagination: Pagination
)

interface TodoRepository {
    fun getTodos(status: String? = null, page: Int = 1, limit: Int = 20): Flow<Result<TodoPageResult>>
    suspend fun getTodo(todoId: String): Result<Todo>
    suspend fun createTodo(title: String, description: String? = null): Result<Todo>
    suspend fun updateTodo(todo: Todo): Result<Todo>
    suspend fun deleteTodo(todoId: String): Result<Unit>
}

class TodoRepositoryImpl(
    private val apiService: TodoApiService
) : BaseRepository(), TodoRepository {
    
    override fun getTodos(status: String?, page: Int, limit: Int): Flow<Result<TodoPageResult>> = flow {
        emit(safeApiCall {
            val response = apiService.getTodos(status, page, limit).body()
                ?: throw Exception("Failed to fetch todos")
            TodoPageResult(
                todos = response.data,
                pagination = response.pagination
            )
        })
    }

    override suspend fun getTodo(todoId: String): Result<Todo> = safeApiCall {
        apiService.getTodo(todoId).body() ?: throw Exception("Todo not found")
    }

    override suspend fun createTodo(title: String, description: String?): Result<Todo> = safeApiCall {
        apiService.createTodo(
            CreateTodoRequest(title = title, description = description)
        ).body() ?: throw Exception("Failed to create todo")
    }

    override suspend fun updateTodo(todo: Todo): Result<Todo> = safeApiCall {
        apiService.updateTodo(todo.id.toString(), todo).body() ?: throw Exception("Failed to update todo")
    }

    override suspend fun deleteTodo(todoId: String): Result<Unit> = safeApiCall {
        apiService.deleteTodo(todoId)
        Result.success(Unit)
    }
}