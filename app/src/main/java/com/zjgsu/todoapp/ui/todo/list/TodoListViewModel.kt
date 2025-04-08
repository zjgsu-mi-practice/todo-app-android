package com.zjgsu.todoapp.ui.todo.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zjgsu.todoapp.data.model.Todo
import com.zjgsu.todoapp.data.model.TodoStatus
import com.zjgsu.todoapp.data.repository.TodoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class TodoListUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val todos: List<Todo> = emptyList(),
    val currentFilter: String? = null,
    val currentPage: Int = 1,
    val totalPages: Int = 1,
    val isRefreshing: Boolean = false,
    val isLoadingMore: Boolean = false
)

class TodoListViewModel(
    private val todoRepository: TodoRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(TodoListUiState())
    val uiState: StateFlow<TodoListUiState> = _uiState.asStateFlow()

    init {
        loadTodos()
    }

    fun loadTodos(isRefresh: Boolean = false) {
        if (isRefresh) {
            _uiState.update { it.copy(isRefreshing = true) }
        } else {
            _uiState.update { it.copy(isLoading = true) }
        }

        viewModelScope.launch {
            todoRepository.getTodos(
                status = _uiState.value.currentFilter,
                page = 1,
                limit = PAGE_SIZE
            ).collect { result ->
                result.onSuccess { pageResult ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            isRefreshing = false,
                            error = null,
                            todos = pageResult.todos,
                            currentPage = pageResult.pagination.page,
                            totalPages = (pageResult.pagination.total + PAGE_SIZE - 1) / PAGE_SIZE
                        )
                    }
                }.onFailure { error ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            isRefreshing = false,
                            error = error.message ?: "Failed to load todos"
                        )
                    }
                }
            }
        }
    }

    fun loadNextPage() {
        if (_uiState.value.currentPage >= _uiState.value.totalPages || 
            _uiState.value.isLoadingMore) return

        _uiState.update { it.copy(isLoadingMore = true) }

        viewModelScope.launch {
            todoRepository.getTodos(
                status = _uiState.value.currentFilter,
                page = _uiState.value.currentPage + 1,
                limit = PAGE_SIZE
            ).collect { result ->
                result.onSuccess { pageResult ->
                    _uiState.update {
                        it.copy(
                            isLoadingMore = false,
                            error = null,
                            todos = it.todos + pageResult.todos,
                            currentPage = it.currentPage + 1
                        )
                    }
                }.onFailure { error ->
                    _uiState.update {
                        it.copy(
                            isLoadingMore = false,
                            error = error.message ?: "Failed to load more todos"
                        )
                    }
                }
            }
        }
    }

    fun setFilter(status: String?) {
        _uiState.update { it.copy(currentFilter = status) }
        loadTodos(isRefresh = true)
    }

    fun deleteTodo(todoId: String) {
        viewModelScope.launch {
            todoRepository.deleteTodo(todoId).onSuccess {
                // Optimistically remove the todo from the list
                _uiState.update { currentState ->
                    currentState.copy(
                        todos = currentState.todos.filterNot { it.id.toString() == todoId }
                    )
                }
            }.onFailure { error ->
                _uiState.update {
                    it.copy(
                        error = error.message ?: "Failed to delete todo"
                    )
                }
            }
        }
    }

    fun updateTodoStatus(todoId: String, newStatus: TodoStatus) {
        viewModelScope.launch {
            val todoToUpdate = _uiState.value.todos.firstOrNull { it.id.toString() == todoId }
            todoToUpdate?.let { todo ->
                val updatedTodo = todo.copy(status = newStatus)
                // Optimistically update the todo in the list
                _uiState.update { currentState ->
                    currentState.copy(
                        todos = currentState.todos.map {
                            if (it.id.toString() == todoId) updatedTodo else it
                        }
                    )
                }
                // Persist the change
                todoRepository.updateTodo(updatedTodo).onFailure { error ->
                    // Revert if update fails
                    _uiState.update { currentState ->
                        currentState.copy(
                            todos = currentState.todos.map {
                                if (it.id.toString() == todoId) todo else it
                            },
                            error = error.message ?: "Failed to update todo status"
                        )
                    }
                }
            }
        }
    }

    companion object {
        private const val PAGE_SIZE = 20
    }
}