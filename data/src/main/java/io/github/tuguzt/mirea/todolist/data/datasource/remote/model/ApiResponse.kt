package io.github.tuguzt.mirea.todolist.data.datasource.remote.model

import com.haroldadmin.cnradapter.CompletableResponse
import com.haroldadmin.cnradapter.NetworkResponse

internal typealias ApiResponse<T> = NetworkResponse<T, ApiError>

internal typealias ApiCompletableResponse = CompletableResponse<ApiError>
