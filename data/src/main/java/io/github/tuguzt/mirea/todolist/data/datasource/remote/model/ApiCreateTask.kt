package io.github.tuguzt.mirea.todolist.data.datasource.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class ApiCreateTask(
    val content: String,
    val description: String,
    @SerialName("project_id") val projectId: String,
)
