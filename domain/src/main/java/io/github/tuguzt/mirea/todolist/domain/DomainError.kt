package io.github.tuguzt.mirea.todolist.domain

import java.io.IOException

public sealed class DomainError(
    message: String?,
    cause: Throwable?,
) : Exception(message, cause) {

    public class LogicError(
        message: String?,
        cause: Throwable?,
    ) : DomainError(message, cause)

    public class StorageError(
        message: String?,
        cause: Throwable?,
    ) : DomainError(message, cause)

    public class NetworkError(
        message: String?,
        cause: IOException,
    ) : DomainError(message, cause)

    public class UnknownError(
        message: String?,
        cause: Throwable?,
    ) : DomainError(message, cause)
}

public typealias DomainResult<T> = Result<T, DomainError>
