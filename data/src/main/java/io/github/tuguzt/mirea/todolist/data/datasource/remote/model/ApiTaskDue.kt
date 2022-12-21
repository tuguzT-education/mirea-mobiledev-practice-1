package io.github.tuguzt.mirea.todolist.data.datasource.remote.model

import kotlinx.datetime.LocalDate
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class ApiTaskDue(
    val string: String,
    val date: LocalDate,
    @SerialName("is_recurring") val isRecurring: Boolean,
    val lang: String,
    val datetime: String? = null,
    val timezone: String? = null,
)
