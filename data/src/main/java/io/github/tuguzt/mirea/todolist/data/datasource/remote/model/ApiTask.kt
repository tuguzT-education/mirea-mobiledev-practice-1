package io.github.tuguzt.mirea.todolist.data.datasource.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class ApiTask(
    val id: String,
    @SerialName("project_id") val projectId: String,
    @SerialName("section_id") val sectionId: String,
    val content: String,
    val description: String,
    @SerialName("is_completed") val isCompleted: Boolean,
    val labels: List<String>,
    @SerialName("parent_id") val parentId: String,
    val order: Int,
    val priority: Int,
    val due: ApiTaskDue?,
    val url: String,
    @SerialName("comment_count") val commentCount: Int,
    @SerialName("created_at") val createdAt: String,
    @SerialName("creator_id") val creatorId: String,
    @SerialName("assignee_id") val assigneeId: String?,
    @SerialName("assigner_id") val assignerId: String?,
)
