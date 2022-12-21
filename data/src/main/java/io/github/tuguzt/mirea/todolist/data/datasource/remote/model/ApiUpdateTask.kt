package io.github.tuguzt.mirea.todolist.data.datasource.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class ApiUpdateTask(
    val content: String? = null,
    val description: String? = null,
    val labels: List<String>? = null,
    val priority: Int? = null,
    @SerialName("due_string") val dueString: String? = null,
    @SerialName("due_date") val dueDate: String? = null,
    @SerialName("due_datetime") val dueDatetime: String? = null,
    @SerialName("due_lang") val dueLang: String? = null,
    @SerialName("assignee_id") val assigneeId: String? = null,
)
