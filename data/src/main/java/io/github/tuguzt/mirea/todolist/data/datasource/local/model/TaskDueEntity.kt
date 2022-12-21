package io.github.tuguzt.mirea.todolist.data.datasource.local.model

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id

@Entity
internal data class TaskDueEntity(
    @Id var id: Long = 0,
    var string: String? = null,
    var datetime: Long = 0,
)
