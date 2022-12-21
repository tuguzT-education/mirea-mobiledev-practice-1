package io.github.tuguzt.mirea.todolist.data.datasource.local.model

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.annotation.Unique
import io.objectbox.relation.ToOne

@Entity
internal data class TaskEntity(
    @Id var id: Long = 0,
    @Unique var uid: String? = null,
    var name: String? = null,
    var content: String? = null,
    var completed: Boolean = false,
    var createdAt: Long = 0,
) {
    lateinit var project: ToOne<ProjectEntity>
    lateinit var due: ToOne<TaskDueEntity>
}
