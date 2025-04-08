package com.zjgsu.todoapp.di

import com.zjgsu.todoapp.data.remote.RetrofitClient
import com.zjgsu.todoapp.data.remote.TodoApiService
import com.zjgsu.todoapp.data.repository.TodoRepository
import com.zjgsu.todoapp.data.repository.TodoRepositoryImpl
import com.zjgsu.todoapp.ui.todo.list.TodoListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single<TodoApiService> { RetrofitClient.todoApiService }
    single<TodoRepository> { TodoRepositoryImpl(get()) }
    viewModel { TodoListViewModel(get()) }
}