package com.zjgsu.todoapp.ui.todo.addedit

import androidx.lifecycle.SavedStateHandle
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
import java.util.UUID

data class AddEditTodoUiState(
    val todoId: String? = null, // Store as String since we're using UUID strings
    val title: String = "",
    val description: String = "",
    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val error: String? = null,
    val navigateBack: Boolean = false // Used to signal navigation after save
)

class AddEditTodoViewModel(
    private val repository: TodoRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(AddEditTodoUiState())
    val uiState: StateFlow<AddEditTodoUiState> = _uiState.asStateFlow()

    private val todoId: String? = savedStateHandle.get<String>("todoId")

    init {
        _uiState.update { it.copy(todoId = todoId) }
        if (todoId != null) {
            loadTodo(todoId)
        }
    }

    fun loadTodo(id: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            val result = repository.getTodo(id)
            result.onSuccess { todo ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        title = todo.title,
                        description = todo.description ?: ""
                    )
                }
            }.onFailure { throwable ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = throwable.message ?: "Failed to load todo"
                    )
                }
            }
        }
    }

    fun updateTitle(newTitle: String) {
        _uiState.update { it.copy(title = newTitle) }
    }

    fun updateDescription(newDescription: String) {
        _uiState.update { it.copy(description = newDescription) }
    }

    fun saveTodo() {
        val currentState = _uiState.value
        if (currentState.title.isBlank()) {
            _uiState.update { it.copy(error = "Title cannot be empty") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true, error = null) }
            val result = if (currentState.todoId != null) {
                // Update existing todo
                val updatedTodo = Todo(
                    id = currentState.todoId?.let { UUID.fromString(it) } ?: UUID.randomUUID(),
                    title = currentState.title,
                    description = currentState.description.takeIf { it.isNotBlank() },
                    status = TodoStatus.PENDING // Default status
                    // Add other fields if necessary, fetching the original might be better
                )
                repository.updateTodo(updatedTodo)
            } else {
                // Create new todo
                repository.createTodo(
                    title = currentState.title,
                    description = currentState.description.takeIf { it.isNotBlank() }
                )
            }

            result.onSuccess {
                _uiState.update { it.copy(isSaving = false, navigateBack = true) }
            }.onFailure { throwable ->
                _uiState.update {
                    it.copy(
                        isSaving = false,
                        error = throwable.message ?: "Failed to save todo"
                    )
                }
            }
        }
    }

    // Call this after navigation has occurred
    fun onNavigationHandled() {
        _uiState.update { it.copy(navigateBack = false) }
    }
}