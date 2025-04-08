package com.zjgsu.todoapp.data.remote

import com.zjgsu.todoapp.data.model.Category
import com.zjgsu.todoapp.data.model.Memo
import com.zjgsu.todoapp.data.model.Reminder
import com.zjgsu.todoapp.data.model.Todo
import com.zjgsu.todoapp.data.model.Tag
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface TodoApiService {
    // Todos
    @GET("todos")
    suspend fun getTodos(
        @Query("status") status: String? = null,
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 20
    ): Response<PaginatedResponse<Todo>>

    @POST("todos")
    suspend fun createTodo(@Body request: CreateTodoRequest): Response<Todo>

    @GET("todos/{todoId}")
    suspend fun getTodo(@Path("todoId") todoId: String): Response<Todo>

    @PUT("todos/{todoId}")
    suspend fun updateTodo(
        @Path("todoId") todoId: String,
        @Body todo: Todo
    ): Response<Todo>

    @DELETE("todos/{todoId}")
    suspend fun deleteTodo(@Path("todoId") todoId: String): Response<Unit>

    // Reminders
    @GET("todos/{todoId}/reminders")
    suspend fun getReminders(@Path("todoId") todoId: String): Response<List<Reminder>>

    @POST("todos/{todoId}/reminders")
    suspend fun createReminder(
        @Path("todoId") todoId: String,
        @Body reminder: Reminder
    ): Response<Reminder>

    // Categories
    @GET("categories")
    suspend fun getCategories(): Response<List<Category>>

    @POST("categories")
    suspend fun createCategory(@Body category: Category): Response<Category>

    // Tags
    @GET("tags")
    suspend fun getTags(): Response<List<Tag>>

    @POST("tags")
    suspend fun createTag(@Body tag: Tag): Response<Tag>

    // Memos
    @GET("memos/{memoId}")
    suspend fun getMemo(@Path("memoId") memoId: String): Response<Memo>

    @PUT("memos/{memoId}")
    suspend fun updateMemo(
        @Path("memoId") memoId: String,
        @Body memo: Memo
    ): Response<Memo>
}

data class PaginatedResponse<T>(
    val data: List<T>,
    val pagination: Pagination
)

data class Pagination(
    val total: Int,
    val page: Int,
    val limit: Int
)

data class CreateTodoRequest(
    val title: String,
    val description: String? = null,
    val categoryId: String? = null,
    val tagIds: List<String> = emptyList()
)