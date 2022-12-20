package io.github.tuguzt.mirea.todolist.data.datasource.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class ApiProject(
    val id: String,
    val name: String,
    val color: String,
    @SerialName("parent_id") val parentId: String?,
    val order: Int,
    @SerialName("comment_count") val commentCount: Int,
    @SerialName("is_shared") val isShared: Boolean,
    @SerialName("is_favorite") val isFavorite: Boolean,
    @SerialName("is_inbox_project") val isInboxProject: Boolean,
    @SerialName("is_team_inbox") val isTeamInbox: Boolean,
    @SerialName("view_style") val viewStyle: String,
    val url: String,
)
