package com.zjgsu.todoapp.ui.todo.addedit

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditTodoScreen(
    navController: NavController,
    viewModel: AddEditTodoViewModel = koinViewModel() // Inject ViewModel
) {
    val uiState by viewModel.uiState.collectAsState()

    // Handle navigation when save is successful
    LaunchedEffect(uiState.navigateBack) {
        if (uiState.navigateBack) {
            navController.popBackStack()
            viewModel.onNavigationHandled() // Reset the flag
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (uiState.todoId == null) "Add Todo" else "Edit Todo") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            if (uiState.isLoading) {
                CircularProgressIndicator()
            } else {
                OutlinedTextField(
                    value = uiState.title,
                    onValueChange = viewModel::updateTitle,
                    label = { Text("Title") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = uiState.error?.contains("Title", ignoreCase = true) == true
                )

                OutlinedTextField(
                    value = uiState.description,
                    onValueChange = viewModel::updateDescription,
                    label = { Text("Description (Optional)") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp) // Give description more space
                )

                Button(
                    onClick = viewModel::saveTodo,
                    enabled = !uiState.isSaving,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    if (uiState.isSaving) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            strokeWidth = 2.dp,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                        Spacer(Modifier.width(8.dp))
                    }
                    Text("Save Todo")
                }

                if (uiState.error != null) {
                    Text(
                        text = uiState.error ?: "An unknown error occurred",
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }
        }
    }
}