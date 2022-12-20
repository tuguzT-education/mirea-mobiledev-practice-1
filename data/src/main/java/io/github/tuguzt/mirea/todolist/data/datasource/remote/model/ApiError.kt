package io.github.tuguzt.mirea.todolist.data.datasource.remote.model

import kotlinx.serialization.Serializable

@JvmInline
@Serializable
internal value class ApiError(val text: String)
