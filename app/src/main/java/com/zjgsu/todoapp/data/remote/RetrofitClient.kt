package com.zjgsu.todoapp.data.remote

import kotlinx.serialization.json.Json
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object RetrofitClient {
    private const val BASE_URL = "http://192.168.88.3:8000/"
    
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(MoshiConverterFactory.create())
        .build()

    val todoApiService: TodoApiService by lazy {
        retrofit.create(TodoApiService::class.java)
    }
}