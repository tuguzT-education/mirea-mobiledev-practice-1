package io.github.tuguzt.mirea.todolist.viewmodel

import io.github.tuguzt.mirea.todolist.domain.DomainError
import java.util.*

interface MessageState<T : MessageKind> {
    val userMessages: List<UserMessage<out T>>
}

interface MessageKind

data class UserMessage<T : MessageKind>(
    val kind: T,
    val id: UUID = UUID.randomUUID(),
)

enum class DomainErrorKind : MessageKind {
    LogicError,
    StorageError,
    NetworkError,
    UnknownError,
}

fun DomainError.kind(): DomainErrorKind = when (this) {
    is DomainError.LogicError -> DomainErrorKind.LogicError
    is DomainError.NetworkError -> DomainErrorKind.NetworkError
    is DomainError.StorageError -> DomainErrorKind.StorageError
    is DomainError.UnknownError -> DomainErrorKind.UnknownError
}
