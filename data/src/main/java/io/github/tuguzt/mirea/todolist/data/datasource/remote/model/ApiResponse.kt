package io.github.tuguzt.mirea.todolist.data.datasource.remote.model

import com.haroldadmin.cnradapter.CompletableResponse
import com.haroldadmin.cnradapter.NetworkResponse
import io.github.tuguzt.mirea.todolist.domain.DomainError
import io.github.tuguzt.mirea.todolist.domain.DomainResult
import io.github.tuguzt.mirea.todolist.domain.error
import io.github.tuguzt.mirea.todolist.domain.success

internal typealias ApiResponse<T> = NetworkResponse<T, ApiError>

internal typealias ApiCompletableResponse = CompletableResponse<ApiError>

internal fun <T> ApiResponse<T>.toResult(): DomainResult<T> = when (this) {
    is NetworkResponse.Success -> success(body)
    is NetworkResponse.ServerError -> error(DomainError.LogicError(body?.text, error))
    is NetworkResponse.NetworkError -> error(DomainError.NetworkError(body?.text, error))
    is NetworkResponse.UnknownError -> error(DomainError.UnknownError(body?.text, error))
}
