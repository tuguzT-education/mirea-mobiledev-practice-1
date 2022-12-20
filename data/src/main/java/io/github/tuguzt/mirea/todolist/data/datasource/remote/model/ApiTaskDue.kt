package io.github.tuguzt.mirea.todolist.data.datasource.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class ApiTaskDue(
    val string: String,
    val date: String,
    @SerialName("is_recurring") val isRecurring: Boolean,
    val datetime: String,
    val timezone: String,
)
