package io.github.tuguzt.mirea.todolist.domain

import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

public sealed interface Result<T, E : Throwable> {
    public data class Success<T, E : Throwable>(val data: T) : Result<T, E>

    public data class Error<T, E : Throwable>(val error: E) : Result<T, E>
}

public fun <T, E : Throwable> Result<T, E>.dataOrNull(): T? = when (this) {
    is Result.Success -> data
    is Result.Error -> null
}

public fun <T, E : Throwable> success(data: T): Result.Success<T, E> =
    Result.Success(data)

public fun <T, E : Throwable> error(error: E): Result.Error<T, E> =
    Result.Error(error)

@OptIn(ExperimentalContracts::class)
public fun <T, E : Throwable, N> Result<T, E>.map(transform: (T) -> N): Result<N, E> {
    contract {
        callsInPlace(transform, InvocationKind.AT_MOST_ONCE)
    }
    return when (this) {
        is Result.Error -> cast()
        is Result.Success -> map(transform)
    }
}

@OptIn(ExperimentalContracts::class)
public fun <T, E : Throwable, N : Throwable> Result<T, E>.mapError(transform: (E) -> N): Result<T, N> {
    contract {
        callsInPlace(transform, InvocationKind.AT_MOST_ONCE)
    }
    return when (this) {
        is Result.Error -> map(transform)
        is Result.Success -> cast()
    }
}

@OptIn(ExperimentalContracts::class)
public fun <T, E : Throwable, N> Result.Success<T, E>.map(transform: (T) -> N): Result.Success<N, E> {
    contract {
        callsInPlace(transform, InvocationKind.EXACTLY_ONCE)
    }
    return Result.Success(transform(data))
}

@OptIn(ExperimentalContracts::class)
public fun <T, E : Throwable, N : Throwable> Result.Error<T, E>.map(transform: (E) -> N): Result.Error<T, N> {
    contract {
        callsInPlace(transform, InvocationKind.EXACTLY_ONCE)
    }
    return Result.Error(transform(error))
}

public fun <S, E : Throwable, N : Throwable> Result.Success<S, E>.cast(): Result.Success<S, N> =
    success(data)

public fun <S, E : Throwable, N> Result.Error<S, E>.cast(): Result.Error<N, E> =
    error(error)
