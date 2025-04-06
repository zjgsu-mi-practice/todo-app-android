package com.zjgsu.todoapp.data.repository

import com.zjgsu.todoapp.data.model.Todo
import com.zjgsu.todoapp.data.remote.CreateTodoRequest
import com.zjgsu.todoapp.data.remote.TodoApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

interface TodoRepository {
    fun getTodos(status: String? = null, page: Int = 1, limit: Int = 20): Flow<Result<List<Todo>>>
    suspend fun getTodo(todoId: String): Result<Todo>
    suspend fun createTodo(title: String, description: String? = null): Result<Todo>
    suspend fun updateTodo(todo: Todo): Result<Todo>
    suspend fun deleteTodo(todoId: String): Result<Unit>
}

class TodoRepositoryImpl(
    private val apiService: TodoApiService
) : BaseRepository(), TodoRepository {
    
    override fun getTodos(status: String?, page: Int, limit: Int): Flow<Result<List<Todo>>> = flow {
        emit(safeApiCall { 
            apiService.getTodos(status, page, limit).body()?.data ?: emptyList()
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