package com.zjgsu.todoapp.data.repository

import com.zjgsu.todoapp.data.model.ApiError
import com.zjgsu.todoapp.data.model.ErrorDetail
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

open class BaseRepository {
    suspend fun <T> safeApiCall(apiCall: suspend () -> T): Result<T> {
        return withContext(Dispatchers.IO) {
            try {
                Result.success(apiCall())
            } catch (e: HttpException) {
                val error = try {
                    e.response()?.errorBody()?.string()?.let {
                        // Parse error response if available
                        ApiError(ErrorDetail(code = e.code().toString(), message = it))
                    } ?: ApiError(ErrorDetail(code = e.code().toString(), message = e.message()))
                } catch (ex: Exception) {
                    ApiError(ErrorDetail(code = e.code().toString(), message = e.message()))
                }
                Result.failure(Exception(error.error.message))
            } catch (e: IOException) {
                Result.failure(Exception("Network error occurred: ${e.message}"))
            } catch (e: Exception) {
                Result.failure(Exception("Unexpected error: ${e.message}"))
            }
        }
    }
}