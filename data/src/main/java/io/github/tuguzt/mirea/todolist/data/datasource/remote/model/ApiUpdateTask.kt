package io.github.tuguzt.mirea.todolist.data.datasource.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class ApiUpdateTask(
    val content: String?,
    val description: String?,
    val labels: List<String>?,
    val priority: Int?,
    @SerialName("due_string") val dueString: String?,
    @SerialName("due_date") val dueDate: String?,
    @SerialName("due_datetime") val dueDatetime: String?,
    @SerialName("due_lang") val dueLang: String?,
    @SerialName("assignee_id") val assigneeId: String?,
)
